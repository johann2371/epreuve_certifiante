# Documentation Technique : DIGITRANS-CM

**Auteur :** Johann Gouaffo
**Date :** Mai 2026
**Version :** 1.0
**Projet :** Système d'Information Intégré (ERP, CRM, Supply Chain, BI) pour AGROCAM S.A.

---

## 1. Contexte et Objectifs

Dans le cadre de la transformation numérique d'AGROCAM S.A., le projet **DIGITRANS-CM** vise à remplacer l'ancien système monolithique par une architecture moderne, résiliente et évolutive. L'objectif de cette implémentation technique est de fournir la fondation logicielle et l'infrastructure Cloud nécessaires pour supporter les 4 modules métiers principaux (ERP, CRM, Supply Chain, BI) tout en respectant des normes strictes de sécurité (DevSecOps) et de séparation des environnements.

---

## 2. Architecture Logicielle (Microservices)

Le système a été conçu selon le modèle architectural des **Microservices**, garantissant une séparation stricte des domaines métiers. 

### 2.1. Technologies Utilisées
- **Langage principal** : Java 17 (LTS)
- **Framework** : Spring Boot 3.2.x, Spring Cloud OpenFeign, Spring Cloud Gateway
- **Base de données** : PostgreSQL 15 (Isolée par domaine/microservice)
- **Persistance** : Spring Data JPA (Hibernate)
- **Outils de Build** : Maven

### 2.2. Description des Modules
Le système est composé de 5 services indépendants :

1. **API Gateway (`api-gateway` - Port 8080)** : Point d'entrée unique. Gère le routage dynamique vers les autres services et implémente un filtre de sécurité pour la validation des requêtes.
2. **Module ERP (`erp-service` - Port 8081)** : Gestion des ressources humaines et des achats (Entités `Employee`, `PurchaseOrder`).
3. **Module CRM (`crm-service` - Port 8082)** : Gestion de la relation client pour l'entité *SavoirManger* (Entités `Customer`, `Order`).
4. **Module Supply Chain (`supply-chain-service` - Port 8083)** : Traçabilité logistique des flux de marchandises (Entité `Shipment`).
5. **Module BI (`bi-service` - Port 8084)** : Tableau de bord stratégique. Il n'a pas de base de données propre, mais agrège les données en temps réel via des appels **OpenFeign** vers l'ERP, le CRM et la Supply Chain.

### 2.3. Sécurité (JWT)
L'API Gateway embarque un filtre `AuthenticationFilter` qui intercepte chaque requête entrante. Toute requête dépourvue d'un token d'accès JWT valide (Header `Authorization: Bearer <token>`) est rejetée avec un code HTTP 401 (Unauthorized). Un contrôleur d'authentification a été mis en place pour la génération de tokens signés via `jjwt`.

---

## 3. Gestion Multi-Environnements

Le projet a été architecturé pour supporter le cycle de vie logiciel complet via 3 environnements distincts, configurés via le mécanisme de Profils Spring Boot (`application-{env}.properties`) et des fichiers de variables Terraform.

| Environnement | Profil Spring | Rôle | Base de données | Infrastructure EC2 |
|---|---|---|---|---|
| **Développement** | `dev` | Tests locaux et développement métier | Locale (via Docker Compose) | `t3.micro` (1 nœud) |
| **Test / UAT** | `test` | Recette utilisateur, Validation CI | Distante interne | `t3.micro` (1 nœud) |
| **Production** | `prod` | Serveur final, haute disponibilité | AWS RDS (prod-db) | `t3.micro` (1 nœud) |

---

## 4. Conteneurisation (Docker)

Chaque microservice dispose d'un `Dockerfile` multi-stage basé sur `eclipse-temurin:17-jre-alpine` pour générer des images extrêmement légères et sécurisées.
Un fichier d'orchestration globale, **`docker-compose.yml`**, est fourni à la racine. Il permet de déployer l'intégralité de la plateforme localement en une seule commande :
```bash
docker-compose up -d --build
```
Ce fichier crée un réseau privé virtuel (`digitrans_net`) et monte un conteneur PostgreSQL central servant tous les microservices.

---

## 5. Déploiement et Intégration Continue (CI/CD & DevSecOps)

Le pipeline de déploiement est automatisé via **GitHub Actions** (`.github/workflows/deploy.yml`). Ce pipeline unique et centralisé exécute des travaux conditionnels selon la branche Git modifiée :

### 5.1. Déclencheurs (Triggers)
- **Push sur `dev`** : Déploie automatiquement l'environnement de développement.
- **Pull Request vers `main`** : Déploie l'environnement de Test (UAT).
- **Push sur `main`** : Déploie la Production.

### 5.2. Pipeline DevSecOps
La sécurité "Shift-Left" a été au cœur de la conception. Avant tout déploiement, le pipeline valide 3 étapes de sécurité critiques :
1. **Gitleaks** : Analyse complète de l'historique Git pour détecter des secrets ou tokens hardcodés par erreur.
2. **TruffleHog** : Validation avancée des secrets sur le dernier commit.
3. **Trivy** : Scanner de vulnérabilités (SCA) analysant le système de fichiers pour détecter des failles de criticité *Haute* ou *Critique* dans les dépendances Maven.

### 5.3. Qualité du Code
Chaque service est couvert par des **tests unitaires** (JUnit 5 + Mockito) ciblant les contrôleurs et garantissant la robustesse des API. Ces tests sont joués systématiquement (`mvn test`) durant le pipeline.

---

## 6. Infrastructure as Code (Terraform)

L'infrastructure cible sur le Cloud AWS est entièrement codée via **Terraform** (`infrastructure/terraform/main.tf`). 
Afin de respecter les contraintes budgétaires strictes (AWS Free Tier), la solution s'appuie sur le provisionnement automatique d'une instance **EC2 `t3.micro`** exécutant Ubuntu.

- **Région** : `af-south-1` (Le Cap) pour garantir une faible latence vers le Cameroun.
- **Automatisation** : Le script injecte un `user_data` au lancement de la VM pour préinstaller Docker et Docker Compose, rendant le serveur immédiatement prêt à recevoir l'application.
- **Sécurité Réseau** : Un *Security Group* (`aws_security_group`) est configuré pour n'exposer que les ports strictement nécessaires (Port 22 pour le SSH, Port 8080 pour la Gateway). Les ports des microservices (8081, 8082...) restent inaccessibles depuis l'extérieur.

---
*Fin du document.*
