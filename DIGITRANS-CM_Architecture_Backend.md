# Architecture Backend — Projet DIGITRANS-CM
**CAMTECH SOLUTIONS S.A. × AGROCAM S.A.**
**Stack : Antigravity · AWS · Microsoft Azure · Hyperledger Fabric**
**Version : 1.0 — Mai 2026**

---

## Table des matières

1. [Contexte et contraintes](#1-contexte-et-contraintes)
2. [Vue d'ensemble de l'architecture](#2-vue-densemble-de-larchitecture)
3. [Couche API Gateway & Sécurité périphérique](#3-couche-api-gateway--sécurité-périphérique)
4. [Couche Authentification & Autorisation (C21, C25)](#4-couche-authentification--autorisation)
5. [Microservices métier (C21)](#5-microservices-métier)
6. [Couche Données & Souveraineté (C21, C23)](#6-couche-données--souveraineté)
7. [Résilience & Mode Offline-First (C23)](#7-résilience--mode-offline-first)
8. [Blockchain — Traçabilité Supply Chain (C25, C26)](#8-blockchain--traçabilité-supply-chain)
9. [Infrastructure as Code & CI/CD (C22)](#9-infrastructure-as-code--cicd)
10. [Conteneurisation & Orchestration (C22)](#10-conteneurisation--orchestration)
11. [Réseau & Haute Disponibilité (C23)](#11-réseau--haute-disponibilité)
12. [Monitoring & Observabilité (C23, C24)](#12-monitoring--observabilité)
13. [Performance & Optimisation (C24)](#13-performance--optimisation)
14. [Sécurité globale (C25)](#14-sécurité-globale)
15. [Structure du dépôt Git](#15-structure-du-dépôt-git)
16. [Synthèse des choix techniques](#16-synthèse-des-choix-techniques)

---

## 1. Contexte et contraintes

### 1.1 Contexte métier

AGROCAM S.A. migre d'un système monolithique (2009) vers un SI distribué et partiellement cloud. Le projet couvre quatre modules fonctionnels : **ERP**, **CRM**, **Supply Chain** et **BI**, développés avec le framework **Antigravity** côté backend.

### 1.2 Contraintes imposées

| Contrainte | Description | Impact architectural |
|---|---|---|
| **Souveraineté des données** | Loi camerounaise n°2010/012 — données RH, financières et clients sur sol camerounais | ERP on-premise obligatoire |
| **Latence réseau** | 150–250 ms vers Europe/USA, ~80 ms vers af-south-1 (Le Cap) | Régions cloud africaines privilégiées |
| **Coupures réseau/électricité** | Délestages 6–12h/j à Douala | Mode offline-first, sync asynchrone |
| **Budget maîtrisé** | 480 M FCFA sur 18 mois | Auto-scaling, optimisation des coûts cloud |
| **Deux fournisseurs cloud imposés** | AWS (services applicatifs) + Azure (identité, supervision) | Architecture multi-cloud hybride |
| **Conformité réglementaire** | Traçabilité des accès au SI (loi n°2010/012) | Audit logs + Blockchain |

---

## 2. Vue d'ensemble de l'architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          CLIENTS (Frontend / Mobile / PWA)                  │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │ HTTPS / TLS 1.3
                                 ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                    COUCHE PÉRIPHÉRIQUE (AWS CloudFront CDN)                 │
│              Cache statique · Réduction latence · Edge locations            │
└────────────────────────────────┬────────────────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│              API GATEWAY (AWS API Gateway — af-south-1)                     │
│       Routing · Rate Limiting · WAF · SSL Termination · Logging             │
└───────┬──────────────┬──────────────┬──────────────┬───────────────────────┘
        │              │              │              │
        ▼              ▼              ▼              ▼
 ┌────────────┐ ┌────────────┐ ┌───────────┐ ┌──────────────┐
 │erp-service │ │crm-service │ │supply-    │ │  bi-service  │
 │(on-premise)│ │(AWS EKS)   │ │chain-svc  │ │  (AWS EKS)   │
 │ Douala     │ │af-south-1  │ │(AWS EKS)  │ │  af-south-1  │
 └─────┬──────┘ └─────┬──────┘ └─────┬─────┘ └──────┬───────┘
       │              │              │               │
       └──────────────┴──────────────┴───────────────┘
                              │
              ┌───────────────┴───────────────┐
              │                               │
              ▼                               ▼
   ┌─────────────────────┐        ┌─────────────────────┐
   │  sync-service       │        │  blockchain-service  │
   │  (AWS SQS + Redis)  │        │  (Hyperledger Fabric │
   │  Offline → Online   │        │   on-premise Douala) │
   └─────────────────────┘        └─────────────────────┘
              │
              ▼
   ┌─────────────────────────────────────────────────────┐
   │              COUCHE DONNÉES                         │
   │  On-premise (PostgreSQL) · AWS RDS · Redis Cache    │
   │  AWS S3 (data lake BI) · AWS Secrets Manager        │
   └─────────────────────────────────────────────────────┘
              │
              ▼
   ┌─────────────────────────────────────────────────────┐
   │        AZURE — Identité & Supervision               │
   │  Azure AD B2C · Azure Monitor · Grafana             │
   └─────────────────────────────────────────────────────┘
```

---

## 3. Couche API Gateway & Sécurité périphérique

### 3.1 AWS API Gateway (af-south-1)

Point d'entrée unique de toutes les requêtes backend. Il assure :

- **Routing** : redirection vers le microservice concerné selon le préfixe de route (`/erp`, `/crm`, `/supply-chain`, `/bi`)
- **Rate Limiting** : 1 000 req/min par client pour éviter les abus
- **WAF intégré** : protection contre les injections SQL, XSS, et les 10 vulnérabilités OWASP
- **SSL Termination** : TLS 1.3 obligatoire sur tous les endpoints
- **Logging centralisé** : chaque requête est loggée vers AWS CloudWatch (conformité loi n°2010/012)

### 3.2 AWS CloudFront CDN

- Distribution des réponses statiques depuis des edge locations proches du Cameroun
- Réduction de la latence perçue pour les appels fréquents (réponses cachées 5 min par défaut)
- Failover automatique si une région cloud est indisponible

### 3.3 Routes exposées

| Préfixe | Service cible | Auth requise |
|---|---|---|
| `GET /api/erp/**` | erp-service | JWT + rôle `erp_user` |
| `GET/POST /api/crm/**` | crm-service | JWT + rôle `crm_user` |
| `GET/POST /api/supply-chain/**` | supply-chain-service | JWT + rôle `operator` |
| `GET /api/bi/**` | bi-service | JWT + rôle `manager` ou `admin` |
| `POST /api/auth/**` | auth-service (Azure AD B2C) | Public |
| `GET /api/health` | Tous les services | Public |

---

## 4. Couche Authentification & Autorisation

> Compétences couvertes : **C21** (sécurité), **C25** (IAM, politique d'accès)

### 4.1 Fournisseur d'identité : Azure AD B2C

Azure AD B2C est le fournisseur d'identité centralisé imposé par le cahier des charges. Il gère l'ensemble du cycle d'authentification.

**Flux d'authentification :**

```
Client → POST /api/auth/login
       → Azure AD B2C vérifie credentials
       → Émet un Access Token JWT (RS256, durée 15 min)
       → Émet un Refresh Token (durée 7 jours, rotation à chaque usage)
       → Client stocke les tokens côté navigateur (httpOnly cookie)

Requête API :
Client → Authorization: Bearer <JWT>
       → API Gateway valide la signature JWT (clé publique Azure AD)
       → Extrait les claims (rôle, userId, tenantId)
       → Transmet la requête au microservice
```

### 4.2 Politique RBAC — 4 rôles définis

| Rôle | Périmètre d'accès | Modules autorisés |
|---|---|---|
| `admin` | Accès complet en lecture/écriture | ERP, CRM, Supply Chain, BI, Audit |
| `manager` | Lecture complète + écriture partielle | ERP (RH), CRM, BI |
| `operator` | Écriture terrain | Supply Chain uniquement |
| `auditor` | Lecture seule + logs | Audit, Blockchain, BI |

### 4.3 MFA (Multi-Factor Authentication)

- Obligatoire pour les rôles `admin` et `manager`
- Méthode : TOTP (application mobile) ou SMS OTP
- Géré nativement par Azure AD B2C (aucun développement supplémentaire)

### 4.4 Procédure de départ d'un développeur

1. Désactivation immédiate du compte Azure AD (révocation de tous les tokens actifs)
2. Suppression des accès AWS IAM liés au compte
3. Rotation des secrets AWS Secrets Manager auxquels le développeur avait accès
4. Révocation des clés SSH et des accès GitHub
5. Audit des actions des 30 derniers jours dans CloudWatch
6. Documentation de la procédure dans le ticket de départ (traçabilité loi n°2010/012)

---

## 5. Microservices métier

> Compétence couverte : **C21**

Chaque service est développé avec **Antigravity**, exposé via une API REST documentée OpenAPI/Swagger, containerisé avec Docker, et déployé sur AWS EKS (sauf ERP on-premise).

### 5.1 erp-service

| Attribut | Valeur |
|---|---|
| Rôle | Gestion RH, Comptabilité, Approvisionnements |
| Hébergement | **On-premise — Douala** (données sensibles) |
| Runtime | Antigravity (Node.js) |
| Base de données | PostgreSQL on-premise (chiffré AES-256) |
| Port | 3001 |
| Communication | REST interne + événements SQS vers supply-chain |

**Endpoints principaux :**
```
GET    /api/erp/employees          — Liste des employés
POST   /api/erp/employees          — Créer un employé
GET    /api/erp/payroll/:month     — Paie du mois
GET    /api/erp/procurement        — Commandes en cours
POST   /api/erp/procurement        — Nouvelle commande
GET    /api/erp/accounting/balance — Bilan comptable
```

### 5.2 crm-service

| Attribut | Valeur |
|---|---|
| Rôle | Gestion relation client — restaurants SavoirManger |
| Hébergement | **AWS EKS — af-south-1** |
| Runtime | Antigravity (Node.js) |
| Base de données | AWS RDS PostgreSQL (af-south-1), chiffré at-rest |
| Cache | Redis ElastiCache (TTL 5 min sur fiches clients) |
| Port | 3002 |

**Endpoints principaux :**
```
GET    /api/crm/customers          — Liste clients
POST   /api/crm/customers          — Créer client
GET    /api/crm/orders             — Commandes restaurants
POST   /api/crm/orders             — Nouvelle commande
GET    /api/crm/loyalty/:id        — Points fidélité
PUT    /api/crm/loyalty/:id        — Mettre à jour points
```

### 5.3 supply-chain-service

| Attribut | Valeur |
|---|---|
| Rôle | Suivi temps réel des marchandises plantation → transformation → vente |
| Hébergement | **AWS EKS — af-south-1** |
| Runtime | Antigravity (Node.js) + WebSocket (temps réel) |
| Base de données | AWS RDS PostgreSQL |
| Queue | AWS SQS (sync offline → online) |
| Port | 3003 |

**Endpoints principaux :**
```
GET    /api/supply-chain/shipments         — Tous les flux en cours
POST   /api/supply-chain/shipments         — Créer un mouvement de marchandise
PUT    /api/supply-chain/shipments/:id     — Mettre à jour le statut
GET    /api/supply-chain/shipments/:id     — Détail d'un flux
POST   /api/supply-chain/sync             — Endpoint de sync offline (agents terrain)
WS     /api/supply-chain/live             — Flux WebSocket temps réel
```

> Chaque mouvement de marchandise validé déclenche automatiquement une transaction sur la **blockchain Hyperledger** via le `blockchain-service`.

### 5.4 bi-service

| Attribut | Valeur |
|---|---|
| Rôle | Tableaux de bord stratégiques pour la direction AGROCAM |
| Hébergement | **AWS EKS — af-south-1** |
| Runtime | Antigravity (Node.js) + Python workers ETL |
| Data store | AWS S3 (data lake) + Amazon Redshift |
| Cache | Redis (TTL 30 min sur agrégats BI) |
| Port | 3004 |

**Endpoints principaux :**
```
GET    /api/bi/dashboard/overview         — KPIs globaux
GET    /api/bi/dashboard/sales            — Ventes par région / période
GET    /api/bi/dashboard/supply           — Performance supply chain
GET    /api/bi/dashboard/hr              — Indicateurs RH
GET    /api/bi/reports/:type             — Rapports exportables (PDF, CSV)
```

Les données sont alimentées par des **pipelines ETL asynchrones** déclenchés toutes les heures, qui agrègent les données des autres services vers AWS S3, puis Redshift.

### 5.5 sync-service

| Attribut | Valeur |
|---|---|
| Rôle | Synchronisation des données créées hors-ligne (agents terrain) |
| Hébergement | **AWS EKS — af-south-1** |
| Runtime | Antigravity (Node.js) |
| File de messages | AWS SQS (Standard Queue + Dead Letter Queue) |
| Port | 3005 |

### 5.6 notification-service

| Attribut | Valeur |
|---|---|
| Rôle | Envoi d'alertes, emails, SMS aux utilisateurs |
| Hébergement | **AWS EKS — af-south-1** |
| Provider | AWS SNS (notifications) + AWS SES (emails) |
| Port | 3006 |

---

## 6. Couche Données & Souveraineté

> Compétences couvertes : **C21**, **C23**

### 6.1 Cartographie des données par sensibilité

| Type de données | Classification | Hébergement | Technologie |
|---|---|---|---|
| Données RH (employés, paie) | **SENSIBLE** | On-premise Douala | PostgreSQL chiffré AES-256 |
| Données financières (comptabilité) | **SENSIBLE** | On-premise Douala | PostgreSQL chiffré AES-256 |
| Données clients CRM | **SENSIBLE** | AWS RDS af-south-1 | PostgreSQL chiffré at-rest |
| Données Supply Chain | Opérationnel | AWS RDS af-south-1 | PostgreSQL |
| Données BI / Analytics | Agrégé | AWS S3 + Redshift af-south-1 | Parquet / Redshift |
| Logs d'audit | Conformité | AWS CloudWatch + on-premise | Structured JSON |
| Ledger Blockchain | Immuable | On-premise Douala (3 nœuds) | Hyperledger Fabric |

### 6.2 PostgreSQL on-premise (ERP — Douala)

- Hébergé sur un serveur dédié au siège AGROCAM à Douala
- Chiffrement AES-256 des colonnes sensibles (salaire, données fiscales)
- Sauvegardes automatiques quotidiennes vers AWS S3 (chiffrées, région af-south-1)
- Réplique de lecture vers Azure SQL Database (Afrique du Sud) pour le reporting
- **RTO cible : 4h | RPO cible : 24h**

### 6.3 AWS RDS PostgreSQL (Services cloud)

- Multi-AZ activé (2 zones de disponibilité en af-south-1)
- Chiffrement at-rest activé (AWS KMS)
- Sauvegardes automatiques : rétention 7 jours
- Snapshots manuels avant chaque déploiement majeur
- **RTO cible : 30 min | RPO cible : 5 min**

### 6.4 Redis ElastiCache (Cache distribué)

Stratégie de cache appliquée sur les données fréquemment lues :

| Service | Données cachées | TTL |
|---|---|---|
| crm-service | Fiches clients, commandes récentes | 5 min |
| bi-service | Agrégats et KPIs | 30 min |
| supply-chain-service | Statut des envois en cours | 2 min |
| erp-service | Référentiel employés | 15 min |

**Stratégie d'invalidation :** Cache-aside pattern — le service lit d'abord le cache, sinon frappe la base et réécrit dans Redis.

### 6.5 AWS S3 — Stockage objet

- Documents RH, factures, bons de commande : bucket `agrocam-erp-docs` (accès restreint, chiffré SSE-S3)
- Data lake BI : bucket `agrocam-bi-datalake` (Parquet, partitionné par date et module)
- Sauvegardes PostgreSQL on-premise : bucket `agrocam-backups` (chiffré, lifecycle 90 jours)
- Versioning activé sur tous les buckets sensibles

### 6.6 Gestion des secrets

- **AWS Secrets Manager** : mots de passe BDD, clés API tierces, tokens de service
- **Azure Key Vault** : certificats TLS, clés de chiffrement partagées entre Azure AD et AWS
- Rotation automatique des secrets BDD toutes les **90 jours**
- Aucun secret en dur dans le code source (vérifié par GitHub Actions à chaque push)

---

## 7. Résilience & Mode Offline-First

> Compétence couverte : **C23**

### 7.1 Problématique

Les agents terrain de la Supply Chain opèrent dans des zones rurales avec une connectivité intermittente. Les applications doivent continuer à fonctionner localement et synchroniser leurs données à la reconnexion.

### 7.2 Architecture Offline-First

```
Agent terrain (PWA mobile)
│
├─ [Mode CONNECTÉ]
│    └── Requêtes directes vers supply-chain-service via API Gateway
│
└─ [Mode HORS-LIGNE — détecté automatiquement]
     └── Données stockées localement (IndexedDB navigateur)
     └── À la reconnexion :
           POST /api/supply-chain/sync
               └── sync-service reçoit le lot de données
               └── Publie dans AWS SQS (Standard Queue)
               └── supply-chain-service consomme et persiste
               └── Notification de confirmation envoyée à l'agent
```

### 7.3 Gestion de la file SQS

- **Standard Queue** (ordre non garanti, tolérance aux doublons avec idempotency key côté service)
- **Dead Letter Queue** : messages en échec après 3 tentatives → alerte CloudWatch → intervention manuelle
- **Visibilité timeout** : 30 secondes par message
- **Retention** : 4 jours maximum

### 7.4 Détection de la latence et bascule automatique

- Health check toutes les 30 secondes sur chaque service
- Si latence > 500 ms ou timeout > 5 s : bascule automatique en mode dégradé (réponses depuis le cache Redis)
- Alerte automatique vers le `notification-service` (email à l'équipe DevOps)

---

## 8. Blockchain — Traçabilité Supply Chain

> Compétences couvertes : **C25**, **C26**

### 8.1 Choix de la plateforme : Hyperledger Fabric

**Justification du choix :**

| Critère | Hyperledger Fabric | Ethereum public |
|---|---|---|
| Hébergement on-premise | ✅ Oui (nœuds sur Douala) | ❌ Non (réseau public) |
| Confidentialité des données | ✅ Canaux privés | ❌ Tout est public |
| Frais de transaction | ✅ Aucun | ❌ Gas fees |
| Latence | ✅ < 1s (PBFT) | ❌ 15s–60s (PoW/PoS) |
| Souveraineté données (loi n°2010/012) | ✅ Respectée | ❌ Non respectée |
| Adapté réseau d'entreprise privé | ✅ Conçu pour ça | ❌ Conçu pour décentralisation publique |

### 8.2 Architecture du réseau Hyperledger

```
Nœud 1 — Siège Douala (Peer + Orderer)
Nœud 2 — Antenne Yaoundé (Peer)
Nœud 3 — Antenne Bafoussam (Peer)

Canal : agrocam-supply-channel
Chaincode : TraçabilitéMarchandises (Go)
Consensus : PBFT (Practical Byzantine Fault Tolerance)
```

3 nœuds suffisent pour le réseau initial. PBFT garantit le consensus même si un nœud est hors ligne (tolérance de 1 faute sur 3 nœuds).

### 8.3 Structure d'un bloc

```json
{
  "blockNumber": 1042,
  "previousHash": "a3f9c2...",
  "dataHash": "e7b1d4...",
  "timestamp": "2026-05-21T10:35:00Z",
  "transactions": [
    {
      "txId": "tx-uuid-v4",
      "type": "SHIPMENT_VALIDATED",
      "payload": {
        "shipmentId": "SHP-2026-0541",
        "productType": "Cacao brut",
        "quantityKg": 500,
        "origin": "Plantation Mbang",
        "destination": "Usine Bonabéri",
        "validatedBy": "operator-id-007",
        "gpsCoordinates": "3.8667,11.5167"
      },
      "signature": "3045022100..."
    }
  ]
}
```

L'intégrité de la chaîne est garantie par le champ `previousHash` : chaque bloc contient le hash SHA-256 du bloc précédent. Toute modification d'un bloc passé invalide tous les blocs suivants, ce qui est immédiatement détecté par les nœuds pairs.

### 8.4 Mécanisme de consensus : PBFT

PBFT est choisi car :
- Il ne nécessite pas de minage (pas de consommation énergétique excessive)
- Finalité immédiate (pas de fork possible)
- Adapté aux réseaux privés avec un nombre limité de nœuds connus et identifiés
- Tolérance aux pannes : fonctionne avec `f` nœuds défaillants si le réseau compte `3f+1` nœuds minimum (ici 3 nœuds → tolérance 1 panne)

### 8.5 Smart Contract (Chaincode) — TraçabilitéMarchandises

**Langage :** Go (langage natif Hyperledger Fabric)

```go
// Chaincode principal — agrocam-supply-chaincode

package main

import (
    "encoding/json"
    "github.com/hyperledger/fabric-contract-api-go/contractapi"
)

type Shipment struct {
    ShipmentID  string `json:"shipmentId"`
    ProductType string `json:"productType"`
    QuantityKg  int    `json:"quantityKg"`
    Origin      string `json:"origin"`
    Destination string `json:"destination"`
    Status      string `json:"status"` // CREATED | IN_TRANSIT | DELIVERED
    ValidatedBy string `json:"validatedBy"`
    Timestamp   string `json:"timestamp"`
}

// CreateShipment — Enregistre un nouveau mouvement de marchandise
func (c *SupplyChainContract) CreateShipment(ctx contractapi.TransactionContextInterface,
    shipmentId, productType string, quantityKg int, origin, destination, operatorId string) error {

    // Vérification d'idempotence : le shipmentId ne doit pas déjà exister
    existing, _ := ctx.GetStub().GetState(shipmentId)
    if existing != nil {
        return fmt.Errorf("shipment %s already exists", shipmentId)
    }

    shipment := Shipment{
        ShipmentID:  shipmentId,
        ProductType: productType,
        QuantityKg:  quantityKg,
        Origin:      origin,
        Destination: destination,
        Status:      "CREATED",
        ValidatedBy: operatorId,
        Timestamp:   time.Now().UTC().Format(time.RFC3339),
    }

    data, _ := json.Marshal(shipment)
    return ctx.GetStub().PutState(shipmentId, data)
}

// UpdateShipmentStatus — Met à jour le statut d'un envoi
func (c *SupplyChainContract) UpdateShipmentStatus(ctx contractapi.TransactionContextInterface,
    shipmentId, newStatus, operatorId string) error {
    // Récupère, valide, met à jour, réécrit
}

// GetShipment — Lecture d'un envoi (pas de modification du ledger)
func (c *SupplyChainContract) GetShipment(ctx contractapi.TransactionContextInterface,
    shipmentId string) (*Shipment, error) {
    // Lecture seule
}

// GetShipmentHistory — Historique complet d'un envoi (conformité loi n°2010/012)
func (c *SupplyChainContract) GetShipmentHistory(ctx contractapi.TransactionContextInterface,
    shipmentId string) ([]HistoryEntry, error) {
    // Retourne toutes les transactions liées à cet ID
}
```

### 8.6 Flux technique : de l'événement à la blockchain

```
1. Agent terrain valide un mouvement de marchandise
          ↓
2. supply-chain-service reçoit POST /api/supply-chain/shipments
          ↓
3. Données persistées en base RDS PostgreSQL
          ↓
4. supply-chain-service appelle blockchain-service via HTTP interne
          ↓
5. blockchain-service soumet la transaction au chaincode Hyperledger
          ↓
6. Les 3 nœuds approuvent (consensus PBFT)
          ↓
7. Transaction inscrite dans le ledger (immuable)
          ↓
8. blockchain-service retourne le TxID à supply-chain-service
          ↓
9. TxID stocké en base RDS (référence croisée BDD ↔ Blockchain)
          ↓
10. Réponse HTTP 201 renvoyée à l'agent terrain avec le TxID
```

### 8.7 Bonnes pratiques sécurité du smart contract

**Vulnérabilité 1 — Reentrancy Attack**
Non applicable en Go/Hyperledger (pas d'appels externes pendant l'exécution d'une transaction). Le chaincode Hyperledger est isolé : aucun appel réseau externe n'est possible depuis le chaincode.

**Vulnérabilité 2 — Integer Overflow**
Go gère les entiers nativement avec des types fortement typés (`int`, `int64`). On valide explicitement que `quantityKg > 0` et `quantityKg < 1_000_000` avant tout `PutState`.

**Autres mesures :**
- Vérification systématique de l'identité de l'appelant (`ctx.GetClientIdentity()`) avant toute écriture
- Idempotence sur `CreateShipment` (vérification de l'existence avant création)
- Logs de toutes les opérations dans les événements Hyperledger
- Tests unitaires du chaincode avec le framework `mock` Hyperledger

### 8.8 Conformité loi n°2010/012

La loi exige la traçabilité des accès au SI. Hyperledger répond à cette exigence :
- Chaque transaction est signée par l'identité numérique de l'opérateur (certificat X.509 émis par la CA Hyperledger)
- `GetShipmentHistory` retourne l'intégralité des modifications d'un enregistrement avec horodatage et identité
- Le ledger est **immuable** — aucune modification ou suppression n'est possible a posteriori
- Les logs d'audit sont également répliqués vers AWS CloudWatch

### 8.9 Procédure en cas de compromission d'une clé privée de nœud

1. **Isolation immédiate** : retrait du nœud compromis du canal Hyperledger (le réseau continue avec 2 nœuds — toujours fonctionnel en lecture, consensus suspendu temporairement)
2. **Révocation du certificat** : révocation du certificat X.509 du nœud via la CA Hyperledger (CRL mise à jour)
3. **Rotation des clés** : génération d'une nouvelle paire de clés pour le nœud
4. **Réintégration** : réintégration du nœud avec les nouvelles clés après validation par 2 autres nœuds
5. **Audit** : analyse des transactions signées par le nœud compromis sur les 30 derniers jours
6. Dans le contexte de coupures réseau fréquentes à Douala : le nœud Yaoundé et Bafoussam maintiennent le consensus PBFT pendant l'intervention

---

## 9. Infrastructure as Code & CI/CD

> Compétence couverte : **C22**

### 9.1 Terraform — Infrastructure as Code

Tout le provisionnement AWS et Azure est géré par Terraform :

```hcl
# infrastructure/terraform/main.tf

# Région principale : AWS Afrique du Sud (af-south-1)
provider "aws" {
  region = "af-south-1"
}

# VPC principal
resource "aws_vpc" "agrocam_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true
  tags = { Name = "agrocam-vpc-prod" }
}

# Sous-réseau public (API Gateway, Load Balancer)
resource "aws_subnet" "public" {
  vpc_id            = aws_vpc.agrocam_vpc.id
  cidr_block        = "10.0.1.0/24"
  availability_zone = "af-south-1a"
}

# Sous-réseau privé (microservices, bases de données)
resource "aws_subnet" "private" {
  vpc_id            = aws_vpc.agrocam_vpc.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "af-south-1b"
}

# Cluster EKS
resource "aws_eks_cluster" "agrocam" {
  name     = "agrocam-cluster"
  role_arn = aws_iam_role.eks_role.arn
  vpc_config {
    subnet_ids = [aws_subnet.public.id, aws_subnet.private.id]
  }
}

# RDS PostgreSQL Multi-AZ
resource "aws_db_instance" "agrocam_rds" {
  identifier           = "agrocam-db-prod"
  engine               = "postgres"
  engine_version       = "15.4"
  instance_class       = "db.t3.medium"
  allocated_storage    = 100
  multi_az             = true
  storage_encrypted    = true
  deletion_protection  = true
}

# Redis ElastiCache
resource "aws_elasticache_cluster" "agrocam_redis" {
  cluster_id      = "agrocam-cache"
  engine          = "redis"
  node_type       = "cache.t3.micro"
  num_cache_nodes = 1
}
```

### 9.2 Trois environnements distincts

| Environnement | Branche Git | Ressources | Variables |
|---|---|---|---|
| `dev` | `develop` | Instances réduites (t3.micro) | `.env.dev` — BDD locale |
| `staging` | `staging` | Instances moyennes (t3.small) | `.env.staging` — BDD staging |
| `prod` | `main` | Instances production (t3.medium, Multi-AZ) | Secrets Manager |

Chaque environnement a ses propres secrets, rôles IAM et groupes de sécurité. Aucun secret n'est partagé entre environnements.

### 9.3 Pipeline CI/CD — GitHub Actions

```yaml
# .github/workflows/deploy.yml

name: CI/CD DIGITRANS-CM

on:
  push:
    branches: [develop, staging, main]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - name: Install dependencies
        run: npm install
      - name: Run unit tests
        run: npm test
      - name: Check for secrets in code
        run: npx detect-secrets scan
      - name: Lint code
        run: npm run lint

  build-and-push:
    needs: test
    runs-on: ubuntu-latest
    steps:
      - name: Build Docker image
        run: docker build -t agrocam/${{ matrix.service }}:${{ github.sha }} .
      - name: Push to AWS ECR
        run: |
          aws ecr get-login-password | docker login --username AWS --password-stdin $ECR_REGISTRY
          docker push $ECR_REGISTRY/agrocam/${{ matrix.service }}:${{ github.sha }}

  deploy:
    needs: build-and-push
    runs-on: ubuntu-latest
    steps:
      - name: Deploy to EKS
        run: |
          kubectl set image deployment/${{ matrix.service }} \
            ${{ matrix.service }}=$ECR_REGISTRY/agrocam/${{ matrix.service }}:${{ github.sha }}
          kubectl rollout status deployment/${{ matrix.service }}
```

---

## 10. Conteneurisation & Orchestration

> Compétence couverte : **C22**

### 10.1 Dockerfile type (Antigravity — Node.js)

```dockerfile
# Multi-stage build pour minimiser la taille de l'image

FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .

FROM node:20-alpine AS production
WORKDIR /app
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/src ./src
COPY --from=builder /app/package.json ./

# Utilisateur non-root (sécurité)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

EXPOSE 3001
HEALTHCHECK --interval=30s --timeout=5s CMD wget -qO- http://localhost:3001/api/health || exit 1
CMD ["node", "src/index.js"]
```

### 10.2 Déploiement Kubernetes (EKS)

```yaml
# kubernetes/deployments/crm-service.yaml

apiVersion: apps/v1
kind: Deployment
metadata:
  name: crm-service
  namespace: agrocam-prod
spec:
  replicas: 2                    # Haute disponibilité — 2 pods minimum
  selector:
    matchLabels:
      app: crm-service
  template:
    spec:
      containers:
        - name: crm-service
          image: <ECR_REGISTRY>/agrocam/crm-service:latest
          ports:
            - containerPort: 3002
          env:
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: agrocam-secrets
                  key: db-password
          resources:
            requests:
              memory: "128Mi"
              cpu: "100m"
            limits:
              memory: "256Mi"
              cpu: "500m"
          livenessProbe:
            httpGet:
              path: /api/health
              port: 3002
            initialDelaySeconds: 10
            periodSeconds: 30
          readinessProbe:
            httpGet:
              path: /api/health
              port: 3002
            initialDelaySeconds: 5
            periodSeconds: 10
---
# Auto-scaling horizontal
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: crm-service-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: crm-service
  minReplicas: 2
  maxReplicas: 8
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
```

---

## 11. Réseau & Haute Disponibilité

> Compétence couverte : **C23**

### 11.1 Architecture réseau AWS VPC

```
AWS VPC — 10.0.0.0/16 (af-south-1)
│
├── Sous-réseau PUBLIC — 10.0.1.0/24 (AZ af-south-1a)
│     └── AWS API Gateway (endpoint public)
│     └── AWS Application Load Balancer
│     └── NAT Gateway
│
├── Sous-réseau PRIVÉ A — 10.0.2.0/24 (AZ af-south-1a)
│     └── EKS Worker Nodes (crm, supply-chain, bi, sync)
│     └── Redis ElastiCache
│
└── Sous-réseau PRIVÉ B — 10.0.3.0/24 (AZ af-south-1b)
      └── EKS Worker Nodes (réplique — haute disponibilité)
      └── RDS PostgreSQL (standby Multi-AZ)
```

### 11.2 Groupes de sécurité (Principe du moindre privilège)

| Groupe | Trafic entrant autorisé | Trafic sortant |
|---|---|---|
| `sg-api-gateway` | HTTPS 443 depuis 0.0.0.0/0 | Port 3001–3006 vers sg-services |
| `sg-services` | Ports 3001–3006 depuis sg-api-gateway uniquement | Port 5432 vers sg-rds, 6379 vers sg-redis |
| `sg-rds` | Port 5432 depuis sg-services uniquement | Aucun sortant |
| `sg-redis` | Port 6379 depuis sg-services uniquement | Aucun sortant |

### 11.3 Load Balancing & Haute Disponibilité

- **AWS Application Load Balancer (ALB)** : distribue le trafic entre les pods EKS des deux AZ
- **Target Groups** : health check toutes les 30s sur `/api/health` — pod retiré si 2 checks consécutifs échouent
- **Disponibilité cible : 99,9 %** (SLA imposé par le cahier des charges)
- Basculement automatique Multi-AZ : si af-south-1a est indisponible, le trafic bascule sur af-south-1b en < 60s

### 11.4 Stratégie de sauvegarde et reprise après sinistre

| Composant | Fréquence sauvegarde | RTO | RPO | Méthode de restauration |
|---|---|---|---|---|
| RDS PostgreSQL (cloud) | Automatique toutes les 5 min (PITR) | 30 min | 5 min | Restore from snapshot |
| PostgreSQL on-premise (ERP) | Quotidienne vers S3 | 4h | 24h | Restore + import S3 |
| AWS S3 (data lake) | Versioning continu | 1h | 0 (versionné) | Restore version antérieure |
| Ledger Hyperledger | Réplication 3 nœuds | 15 min | 0 (distribué) | Resync nœud depuis pairs |
| Config Terraform | Git versionné | 2h | 0 (Git) | `terraform apply` |

---

## 12. Monitoring & Observabilité

> Compétences couvertes : **C23**, **C24**

### 12.1 Stack de monitoring

| Outil | Rôle | Données collectées |
|---|---|---|
| **AWS CloudWatch** | Logs applicatifs + métriques AWS | CPU, mémoire, requêtes API, erreurs |
| **Azure Monitor** | Supervision identité et ressources Azure | Connexions AD, alertes Azure |
| **Grafana** | Dashboards centralisés (source : CloudWatch) | KPIs temps réel, alertes visuelles |

### 12.2 KPIs et indicateurs de performance

| KPI | Cible | Alerte si |
|---|---|---|
| Uptime global | ≥ 99,9 % | < 99,5 % sur 1h |
| Latence API (p95) | < 200 ms | > 500 ms pendant 5 min |
| Taux d'erreur HTTP | < 1 % | > 5 % pendant 2 min |
| CPU pods EKS | < 70 % (HPA seuil) | > 85 % pendant 5 min |
| Mémoire pods EKS | < 80 % | > 90 % pendant 5 min |
| Messages SQS en attente | < 100 | > 500 (sync bloquée) |
| Temps de sync offline | < 30 s | > 120 s |
| Transactions blockchain | 100 % validées | Rejet ou timeout nœud |

### 12.3 Format des logs (JSON structuré)

```json
{
  "timestamp": "2026-05-21T10:35:22Z",
  "level": "INFO",
  "service": "supply-chain-service",
  "requestId": "req-uuid-v4",
  "userId": "operator-007",
  "method": "POST",
  "path": "/api/supply-chain/shipments",
  "statusCode": 201,
  "durationMs": 87,
  "message": "Shipment SHP-2026-0541 created and recorded on blockchain"
}
```

Tous les logs sont envoyés vers CloudWatch Logs en temps réel via le driver `awslogs` Docker.

---

## 13. Performance & Optimisation

> Compétence couverte : **C24**

### 13.1 Tests de performance (outils imposés : JMeter / K6 / Locust)

Outil retenu : **K6** (léger, scriptable en JavaScript, adapté aux APIs REST)

**Scénarios de test :**

| Scénario | Charge | Durée | KPI cible |
|---|---|---|---|
| Charge normale | 100 utilisateurs simultanés | 10 min | Latence p95 < 200 ms |
| Charge forte | 500 utilisateurs simultanés | 10 min | Latence p95 < 500 ms |
| Stress test | Montée progressive 0 → 1000 users | 20 min | Pas de crash, HPA déclenché |
| Spike test | Pic brutal 0 → 800 users | 2 min | Latence p95 < 1s, 0 erreur 5xx |

### 13.2 Optimisations appliquées

- **Cache Redis** : réduit les requêtes BDD de 70 % sur les données fréquentes (clients, statuts envois)
- **Auto-scaling HPA** : scale de 2 à 8 pods selon la charge CPU (seuil 70 %)
- **Index PostgreSQL** : index sur `shipment_id`, `customer_id`, `created_at` — requêtes < 5 ms
- **Pagination** : tous les endpoints liste retournent max 50 résultats par page
- **Compression gzip** : activée sur l'API Gateway pour les réponses > 1 KB
- **CDN CloudFront** : réponses cachées pour les endpoints BI (TTL 30 min)
- **Connection pooling** : PgBouncer devant PostgreSQL (max 100 connexions actives)

### 13.3 Optimisation des coûts cloud

- **AWS Cost Explorer** : budget mensuel configuré avec alerte si dépassement > 10 %
- **Savings Plans** : instances EC2/RDS réservées 1 an (économie ~30 %)
- **Auto-scaling down** : réduction automatique des pods en dehors des heures ouvrées (21h–6h)
- **S3 Lifecycle** : données BI > 90 jours archivées vers S3 Glacier (coût divisé par 10)
- **Spot Instances** : worker EKS non-critiques (bi-service, sync-service) sur instances Spot

---

## 14. Sécurité globale

> Compétence couverte : **C25**

### 14.1 Chiffrement des données

| Niveau | Mécanisme | Algorithme |
|---|---|---|
| **En transit** | TLS 1.3 sur tous les endpoints | ECDHE + AES-256-GCM |
| **Au repos (RDS)** | AWS KMS (clés gérées par AWS) | AES-256 |
| **Au repos (on-premise)** | PostgreSQL TDE + chiffrement colonnes sensibles | AES-256 |
| **Au repos (S3)** | SSE-S3 (Server-Side Encryption) | AES-256 |
| **Secrets** | AWS Secrets Manager + Azure Key Vault | AES-256 |

### 14.2 Quatre risques de sécurité cloud identifiés

| Risque | Responsabilité entreprise | Responsabilité AWS/Azure | Contre-mesure |
|---|---|---|---|
| **Accès non autorisé aux données** | Configuration IAM, gestion des comptes | Protection infrastructure physique | RBAC + MFA + rotation des clés |
| **Injection SQL / XSS** | Validation des entrées, code sécurisé | WAF managé | WAF API Gateway + requêtes préparées (ORM) |
| **Fuite de secrets (clés API)** | Pas de secrets dans le code, rotation | Chiffrement au repos des secrets | AWS Secrets Manager + scan CI/CD |
| **Indisponibilité (DDoS)** | Architecture résiliente | Infrastructure anti-DDoS (AWS Shield) | Rate Limiting + AWS Shield Standard |

### 14.3 Rotation des clés de la base de données

| Étape | Action | Fréquence |
|---|---|---|
| 1 | AWS Secrets Manager génère un nouveau mot de passe BDD | Automatique — tous les 90 jours |
| 2 | Nouveau mot de passe appliqué sur RDS (rotation sans downtime) | Automatique |
| 3 | L'ancien mot de passe reste valide 24h (période de transition) | Automatique |
| 4 | Les services récupèrent le nouveau secret au prochain démarrage | Au redémarrage du pod |
| 5 | L'ancien mot de passe est révoqué | Automatique — J+1 |
| 6 | Notification CloudWatch confirmant la rotation réussie | Automatique |

---

## 15. Structure du dépôt Git

```
digitrans-cm-backend/
│
├── services/
│   ├── erp-service/
│   │   ├── src/
│   │   │   ├── routes/          # endpoints Express/Antigravity
│   │   │   ├── controllers/     # logique métier
│   │   │   ├── models/          # schémas PostgreSQL (Prisma/Sequelize)
│   │   │   ├── middlewares/     # auth JWT, validation, logging
│   │   │   └── index.js         # point d'entrée
│   │   ├── tests/               # tests unitaires et d'intégration
│   │   ├── Dockerfile
│   │   ├── package.json
│   │   └── swagger.yaml         # Documentation OpenAPI
│   │
│   ├── crm-service/             # Structure identique
│   ├── supply-chain-service/    # Structure identique
│   ├── bi-service/              # Structure identique
│   ├── sync-service/            # Structure identique
│   ├── notification-service/    # Structure identique
│   └── blockchain-service/
│       ├── src/
│       │   ├── fabric-client.js # Connexion au réseau Hyperledger
│       │   └── routes/
│       ├── chaincode/
│       │   └── tracabilite/
│       │       └── chaincode.go # Smart contract Go
│       └── Dockerfile
│
├── infrastructure/
│   ├── terraform/
│   │   ├── main.tf              # Ressources AWS + Azure
│   │   ├── variables.tf
│   │   ├── outputs.tf
│   │   ├── environments/
│   │   │   ├── dev.tfvars
│   │   │   ├── staging.tfvars
│   │   │   └── prod.tfvars
│   │   └── modules/
│   │       ├── vpc/
│   │       ├── eks/
│   │       ├── rds/
│   │       └── redis/
│   │
│   └── kubernetes/
│       ├── namespaces.yaml
│       ├── deployments/         # Un fichier par service
│       ├── services/            # ClusterIP + LoadBalancer
│       ├── hpa/                 # Auto-scaling configs
│       └── secrets/             # Kubernetes Secrets (depuis AWS SM)
│
├── .github/
│   └── workflows/
│       ├── ci.yml               # Tests + lint sur chaque PR
│       └── deploy.yml           # Build + deploy sur push main/staging
│
└── docs/
    ├── architecture.md          # Ce document
    ├── api-specs/
    │   ├── erp-api.yaml
    │   ├── crm-api.yaml
    │   ├── supply-chain-api.yaml
    │   └── bi-api.yaml
    └── runbooks/
        ├── incident-response.md
        └── disaster-recovery.md
```

---

## 16. Synthèse des choix techniques

| Besoin | Solution retenue | Justification |
|---|---|---|
| Framework backend | **Antigravity (Node.js)** | Imposé par le projet |
| Cloud applicatif | **AWS af-south-1 (Le Cap)** | ~80 ms depuis Douala vs 200 ms vers Europe |
| Identité et auth | **Azure AD B2C** | Imposé + OAuth2/OIDC natif, MFA intégré |
| Supervision | **Azure Monitor + Grafana** | Imposé + visualisation centralisée |
| Orchestration | **AWS EKS (Kubernetes)** | Scalabilité, reprise automatique, HPA |
| Base de données | **PostgreSQL (RDS + on-premise)** | Robuste, open-source, Multi-AZ disponible |
| Cache | **Redis ElastiCache** | Faible latence, TTL configurable, support offline |
| Queue async | **AWS SQS** | Fiabilité, DLQ intégrée, retry automatique |
| IaC | **Terraform** | Multi-cloud (AWS + Azure), versionnable |
| CI/CD | **GitHub Actions** | Intégration Git native, gratuit pour les teams |
| Blockchain | **Hyperledger Fabric** | On-premise Douala, PBFT, 0 frais, conforme loi |
| Smart contract | **Go (chaincode)** | Langage natif Hyperledger, performant, typé |
| Tests performance | **K6** | Léger, scriptable JS, compatible CI/CD |
| Monitoring logs | **AWS CloudWatch** | Natif AWS, alertes intégrées, conformité |

---

*Document rédigé dans le cadre de l'examen BC04 — EADL 4 — Année académique 2025/2026*
*CAMTECH SOLUTIONS S.A. — Projet DIGITRANS-CM*
