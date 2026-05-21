# ÉPREUVE DE SEMESTRE N°2

| | |
|---|---|
| **Filière / Classe** | EADL 4 |
| **Date** | Mai 2026 |
| **Intitulé** | **Manager les Projets Numériques** |
| **Durée** | 03 Jours |
| **Examinateur** | Département Génie Informatique |
| **Documents Autorisés** | OUI ☑ / NON ☐ |
| **Calculatrice autorisée** | OUI ☑ / NON ☐ |
| **Semestre** | II |
| **Année académique** | 2025/2026 |

---

## RÉFÉRENCEMENT

| | |
|---|---|
| **Nom d'usage de l'épreuve** | Manager les Projets Numériques |
| **Titre RNCP** | RNCP39763 |
| **Bloc** | BC02 |
| **Numéro EC** | EC02 |
| **Compétences associées** | C10 à C15 |
| **Nombre de critères du référentiel** | 6 |
| **Type** | Mise en situation professionnelle reconstituée (1 à 3 jours consécutifs) |
| **Mode** | **Partie 1 : Collective (livrables) / Partie 2 : Individuelle (oral)** |
| **Durée** | 03 Jours |
| **Modules concernés** | BC02-FM01, BC02-FM02, BC02-FM03, BC02-FM04, BC02-FM05, BC02-FM06, BC02-FM07 |

---

## DESCRIPTION

| | |
|---|---|
| **Principe** | Management et Pilotage de Projet Numérique |
| **Travaux préalables** | |
| **Présence d'un jury** | |
| **Équipements nécessaires** | – Poste de travail avec accès à Internet (accès aux ressources techniques : documentation officielle, GitHub, Stack Overflow) |
| | – Accès à un environnement de développement préconfiguré (IDE, Docker, Git) |
| | – Accès aux comptes cloud (AWS Academy, GCP for Education ou Azure for Students) |
| | – Logiciels de gestion de projet (Trello ou Jira ou tout autre) |
| | – Suite bureautique pour la rédaction des rapports et la création des présentations |
| **Ressources fournies** | |
| **Livrables** | Code source, documentation technique, rapport collectif de projet |

---

## SPÉCIFICITÉS

Toutes les productions réalisées dans le cadre de ces épreuves doivent être originales et résulter du travail personnel des candidats ou des équipes. L'utilisation d'outils d'intelligence artificielle générative est autorisée à titre d'assistance, mais tout contenu produit devra être maîtrisé, adapté et assumé par le candidat. Le jury se réserve le droit de poser des questions spécifiques sur tout élément soumis afin de vérifier la compréhension réelle du candidat.

---

*Page 1 sur 10*

---

## CONTEXTE PROFESSIONNEL

### Présentation de l'Entreprise : CAMTECH SOLUTIONS S.A.

**CAMTECH SOLUTIONS S.A.** est une entreprise camerounaise de services numériques (ESN) fondée en 2015 à Douala, dans le quartier Bonanjo, cœur économique et financier du Cameroun. Dotée d'un capital social de 75 millions de FCFA, la société emploie aujourd'hui 87 collaborateurs permanents, répartis entre son siège de Douala, une antenne à Yaoundé dans le quartier Bastos, et une représentation à Bafoussam pour couvrir les régions anglophones et francophones du pays. Depuis sa création, CAMTECH SOLUTIONS s'est positionnée comme un acteur de référence dans la transformation numérique des organisations au Cameroun et en Afrique centrale.

La société intervient auprès d'une clientèle diversifiée comprenant des institutions publiques (ministères, agences gouvernementales, collectivités locales), des entreprises privées (PME, grandes entreprises des secteurs bancaire, pétrolier, agroalimentaire et telecom), ainsi que des organisations internationales présentes sur le territoire camerounais. Parmi ses clients les plus emblématiques figurent la Société Nationale des Hydrocarbures (SNH), CAMTEL, plusieurs filiales du groupe Orange Cameroun, ainsi que des administrations publiques dans le cadre de la stratégie gouvernementale « Cameroun Numérique 2025 ».

CAMTECH SOLUTIONS propose une gamme complète de services allant du développement d'applications sur mesure, à l'intégration de systèmes d'information, en passant par la cybersécurité, le conseil en transformation digitale et, depuis 2022, des solutions de cloud computing adaptées aux contraintes locales de connectivité et d'infrastructure. L'entreprise a notamment développé des solutions pour la gestion des douanes via un portail en ligne, un système de traçabilité des marchandises portuaires au Port Autonome de Douala, ainsi qu'une plateforme de gestion des dossiers fiscaux en partenariat avec la Direction Générale des Impôts.

### Contexte Sectoriel et Enjeux du Marché Camerounais

Le secteur du numérique au Cameroun connaît une croissance soutenue depuis le début de la décennie 2020, portée par plusieurs dynamiques convergentes. La pénétration d'Internet mobile dépasse désormais les 35 % de la population, et le gouvernement camerounais a fait du numérique l'un des piliers de son Plan de Développement National (PDN) et de la Vision 2035. Dans ce contexte, la demande en solutions informatiques fiables, sécurisées et adaptées aux réalités locales est en pleine expansion.

Cependant, le marché local présente des défis significatifs que toute ESN doit impérativement prendre en compte dans ses projets : des coupures d'électricité fréquentes dans les grandes villes (notamment à Douala où les délestages peuvent durer 6 à 12 heures par jour durant certaines périodes de l'année), une connectivité Internet inégale avec des débits variables selon les zones géographiques, un manque de data centers locaux de niveau international (ce qui impose souvent des compromis entre souveraineté des données et performances techniques), ainsi qu'une pénurie de talents numériques qualifiés qui oblige les entreprises à investir massivement dans la formation interne.

---

*Page 2 sur 10*

---

Par ailleurs, le tissu économique camerounais est dominé par des PME dont les budgets alloués aux projets numériques restent modestes, ce qui contraint les prestataires comme CAMTECH SOLUTIONS à proposer des solutions évolutives et économiquement accessibles. Le recours au cloud computing, en mode SaaS ou PaaS, s'est ainsi imposé comme une alternative stratégique à l'investissement en infrastructures physiques locales, permettant de réduire les coûts d'investissement des clients tout en maintenant un niveau de service acceptable.

### La Mission Confiée : Projet DIGITRANS-CM

Dans le cadre de sa stratégie de développement pour l'exercice 2025-2026, CAMTECH SOLUTIONS a remporté un appel d'offres lancé par AGROCAM S.A., l'un des plus grands groupes agroalimentaires du Cameroun, dont le siège est situé à Douala. AGROCAM S.A. emploie plus de 1 200 personnes et opère à travers trois segments d'activité : la transformation de cacao et café, la distribution de produits alimentaires en Afrique centrale, et la gestion d'une chaîne de restauration rapide sous l'enseigne « **SavoirManger** » présente dans les principales villes du pays (Douala, Yaoundé, Bafoussam, Garoua, Ngaoundéré).

Le projet remporté, baptisé **DIGITRANS-CM** (Digitalisation et Transformation Numérique au Cameroun), a pour objectif de moderniser l'intégralité du Système d'Information (SI) d'AGROCAM S.A. en remplaçant un système legacy vieillissant, développé en 2009 sur une architecture monolithique, par un écosystème applicatif moderne, distribué et partiellement hébergé sur le cloud. Le budget global du projet est estimé à 480 millions de FCFA, répartis sur 18 mois, de janvier 2026 à juin 2027.

Le projet **DIGITRANS-CM** se décline en quatre grands modules fonctionnels : un module ERP (Enterprise Resource Planning) pour la gestion des ressources humaines, de la comptabilité et des approvisionnements ; un module CRM (Customer Relationship Management) pour la gestion de la relation client au niveau des restaurants SavoirManger ; un module Supply Chain pour le suivi en temps réel des flux de marchandises entre les plantations, les unités de transformation et les points de vente ; et enfin, un module BI (Business Intelligence) permettant aux dirigeants d'AGROCAM de disposer de tableaux de bord stratégiques alimentés en temps réel.

Le Directeur Général d'AGROCAM S.A., M. Henri-Claude MOUKAM, a exprimé des exigences claires lors des réunions de cadrage : les données sensibles de l'entreprise (données RH, données financières, données clients) devront impérativement être hébergées sur le sol camerounais pour des raisons de conformité réglementaire, tandis que les services à forte charge (notamment le module BI et les APIs du CRM) pourront être déployés sur des infrastructures cloud internationales. Il a également insisté sur la nécessité d'une architecture résiliente capable de fonctionner en mode dégradé lors des coupures d'électricité ou de connectivité, problème récurrent dans les différentes villes où opère l'entreprise.

### Organisation de l'Équipe Projet

Pour répondre aux exigences de ce projet d'envergure, la Direction Technique de CAMTECH SOLUTIONS a constitué une équipe projet pluridisciplinaire de douze personnes, placée sous la responsabilité d'un Chef de Projet Senior. L'équipe se compose de développeurs full-stack, d'architectes

---

*Page 3 sur 10*

---

cloud, d'ingénieurs DevOps, d'un expert cybersécurité, d'un designer UX/UI et d'un responsable qualité. La méthodologie retenue est une approche hybride combinant les principes Agile (sprints de deux semaines, daily stand-up, rétrospectives) avec des jalons contractuels imposés par le client, caractéristiques des projets en mode forfait encore très répandus dans le marché camerounais.

Vous êtes membre de cette équipe projet. Selon votre rôle au sein de l'équipe (développeur, architecte, ingénieur DevOps ou responsable qualité), vous serez évalué sur votre capacité à planifier et conduire des activités techniques, à coordonner avec vos pairs, à suivre les indicateurs de performance, à mobiliser des ressources documentaires (y compris en anglais), à évaluer périodiquement l'avancement des travaux et à organiser des sessions collaboratives de partage de connaissances. Sur le plan technique, vous devrez également démontrer votre maîtrise de l'intégration des services cloud, de l'automatisation des déploiements, de l'optimisation des infrastructures, du monitoring des systèmes et de la mise en œuvre de stratégies de sécurité robustes incluant des mécanismes de traçabilité basés sur la technologie blockchain.

### Contraintes Techniques et Réglementaires

Le projet DIGITRANS-CM est soumis à un ensemble de contraintes techniques et réglementaires qui doivent guider toutes vos décisions architecturales et opérationnelles. Sur le plan réglementaire, la loi camerounaise n°2010/012 du 21 décembre 2010 relative à la cybersécurité et à la cybercriminalité impose des exigences en matière de protection des données personnelles et de traçabilité des accès aux systèmes d'information. Par ailleurs, les données relevant de la souveraineté nationale (données fiscales, données d'identité) ne peuvent être hébergées à l'étranger sans accord préalable des autorités compétentes.

Sur le plan technique, l'architecture cloud choisie devra prendre en compte la latence réseau élevée observée entre le Cameroun et les régions cloud européennes ou américaines (en moyenne 150 à 250 ms), ce qui impose de privilégier des régions cloud africaines (Afrique du Sud chez AWS et Google Cloud, Égypte chez Microsoft Azure) pour les services nécessitant des temps de réponse courts. L'utilisation de mécanismes de cache distribué, de CDN (Content Delivery Network) et d'architectures événementielles sera nécessaire pour garantir des performances acceptables. Enfin, la stratégie de déploiement devra prévoir des mécanismes de fonctionnement offline-first pour les applications utilisées dans des zones de faible connectivité, notamment pour les agents terrain de la chaîne d'approvisionnement opérant dans des zones rurales.

> **Note aux candidats** : Le présent contexte professionnel constitue le fil directeur de l'ensemble des épreuves certifiantes des blocs BC02 et BC04. Toutes vos productions (livrables, code, présentations) doivent s'inscrire dans ce cadre.

---

*Page 4 sur 10*

---

## PARTIE I — TRAVAIL COLLECTIF : LIVRABLES TECHNIQUES

| | |
|---|---|
| **Mode** | Collectif (équipes de 3 étudiants) |
| **Type** | Mise en situation professionnelle reconstituée (03 jours) |
| **Livrables attendus** | Code source, Documentation technique, Rapport collectif d'activité |
| **Compétences évaluées** | C10, C11, C12, C13, C14, C15 |

### I.1. Objectif général de la Partie

Dans le cadre du projet DIGITRANS-CM, votre équipe est chargée de réaliser la mise en œuvre technique d'un ou plusieurs composants logiciels du système cible. Cette première partie vise à évaluer votre capacité à fonctionner efficacement en équipe projet, à planifier et à conduire des activités techniques dans un contexte professionnel réaliste, à respecter des délais et des jalons tout en produisant des livrables de qualité professionnelle.

### I.2. Plan de Projet et Méthodologie (C10)

Votre équipe devra formaliser et soumettre un plan de projet structuré pour le module qui lui est assigné (ERP, CRM, Supply Chain ou BI). Ce plan devra démontrer la pertinence du choix méthodologique retenu (Agile, cycle en V ou approche hybride) au regard des contraintes du projet DIGITRANS-CM, notamment les contraintes de budget fixe imposées par le contrat avec AGROCAM S.A.

Le plan de projet devra contenir au minimum les éléments suivants :

- Un découpage du module en tâches (Work Breakdown Structure) avec estimation des charges en jours-homme
- Un planning prévisionnel sous forme de diagramme de Gantt couvrant la durée de la mise en situation
- Une identification des risques techniques et organisationnels propres au contexte camerounais (coupures réseau, turnover des équipes, contraintes de licences logicielles)
- Un plan de mitigation des risques identifiés
- Les jalons contractuels convenus avec le client fictif (AGROCAM S.A.)

### I.3. Coordination d'Équipe et Outils Collaboratifs (C11)

L'évaluation de cette sous-partie portera sur la manière dont votre équipe a organisé et documenté sa collaboration tout au long de la mise en situation. Vous devrez produire des preuves de votre coordination effective, notamment :

- Un journal de bord de projet (sprint log ou journal d'activité) documentant les réunions tenues, les décisions prises et les problèmes rencontrés
- Les outils collaboratifs utilisés : tableau Kanban (Trello, Jira ou équivalent), dépôt de code versionné (Git), wiki ou espace documentaire partagé
- La répartition explicite des tâches entre les membres de l'équipe et les ajustements effectués en cours de projet

---

*Page 5 sur 10*

---

- La documentation des actions de montée en compétences entreprises au sein de l'équipe (tutoriels partagés, sessions de pair-programming, revues de code)

Le rapport collectif devra inclure une section dédiée à la dynamique d'équipe, décrivant honnêtement les difficultés de coordination rencontrées et les solutions apportées. Il est attendu des équipes qu'elles fassent preuve de réflexivité professionnelle dans cette section.

### I.4. Suivi Budgétaire et Indicateurs de Performance (C12)

Le projet DIGITRANS-CM est doté d'un budget global de 480 millions de FCFA réparti sur 18 mois (janvier 2026 — juin 2027). Ce budget est ventilé entre les quatre modules fonctionnels (ERP, CRM, Supply Chain et BI) et couvre les charges de main-d'œuvre, les licences logicielles, les infrastructures cloud et les frais de formation. Dans le cadre de votre mission au sein de CAMTECH SOLUTIONS, vous êtes tenu(e) d'assurer un suivi rigoureux des dépenses et des indicateurs de performance de votre module, afin de garantir la maîtrise des coûts et le respect des engagements contractuels vis-à-vis d'AGROCAM S.A.

#### A. Contexte budgétaire du projet

Le tableau ci-dessous présente la répartition budgétaire prévisionnelle par module, telle qu'elle a été validée lors de la réunion de cadrage avec AGROCAM S.A. en décembre 2025 :

| Module | Budget prévu (FCFA) | Dépenses réelles (FCFA) | Écart (FCFA) |
|---|---|---|---|
| Module ERP (RH, Comptabilité, Achats) | 132 000 000 | 138 500 000 | + 6 500 000 |
| Module CRM (SavoirManger) | 96 000 000 | 91 200 000 | - 4 800 000 |
| Module Supply Chain | 120 000 000 | 127 800 000 | + 7 800 000 |
| Module BI (Tableaux de bord) | 132 000 000 | 124 500 000 | - 7 500 000 |
| **TOTAL PROJET** | **480 000 000** | **482 000 000** | **+ 2 000 000** |

À la date de l'épreuve (mai 2026, soit 5 mois d'exécution sur 18), le projet présente un léger dépassement global de **2 millions de FCFA (+0,42 %)**. Les dépassements les plus significatifs sont enregistrés sur le module Supply Chain (+6,5 %) et le module ERP (+4,9 %), principalement en raison des surcoûts liés à l'intégration des API douanières du Port de Douala et aux difficultés d'adaptation du schéma de base de données hérité du système legacy de 2009. Les modules CRM et BI affichent en revanche une sous-consommation budgétaire, permettant de compenser partiellement ces dérives.

#### Suivi des charges en jours-homme (module assigné)

Chaque équipe devra compléter le tableau de suivi ci-dessous en renseignant les charges réelles consommées sur son module pour les sprints 1 à 10 (période janvier — mai 2026). Les données prévisionnelles sont fournies ; les colonnes « Réel » et « Écart » sont à compléter par l'équipe :

---

*Page 6 sur 10*

---

| Tâche / Livrable | JH prévus | JH réels | Écart (JH) | Avancement (%) |
|---|---|---|---|---|
| Analyse et conception du module | 18 | *à compléter* | *à compléter* | *à compléter* |
| Développement back-end (API REST) | 42 | *à compléter* | *à compléter* | *à compléter* |
| Développement front-end (UI/UX) | 28 | *à compléter* | *à compléter* | *à compléter* |
| Intégration cloud & tests (UAT) | 22 | *à compléter* | *à compléter* | *à compléter* |
| Documentation technique et recette | 10 | *à compléter* | *à compléter* | *à compléter* |
| **TOTAL MODULE** | **120** | *à compléter* | *à compléter* | *à compléter* |

**JH = Jours-Homme.** Les cellules sur fond jaune sont à renseigner par l'équipe sur la base des entrées réelles du journal de bord (sprint log). Le taux d'avancement global est calculé par la formule : (Story points livrés / Story points totaux planifiés) × 100.

#### C. Indicateurs clés de performance (KPI) du projet DIGITRANS-CM

En complément du suivi budgétaire, votre équipe devra renseigner et analyser au moins cinq indicateurs de performance technique, choisis parmi les KPI ci-dessous et adaptés au contexte spécifique du projet DIGITRANS-CM (contraintes de connectivité, exigences de sécurité, mode hybride Agile/Forfait) :

| Indicateur (KPI) | Description / Pertinence pour DIGITRANS-CM | Cible | Résultat à compléter |
|---|---|---|---|
| **Taux de couverture des tests** | % du code couvert par les tests unitaires et d'intégration. Critique pour un système manipulant des données RH et financières sensibles d'AGROCAM. | ≥ 80 % | |
| **Nombre de bugs critiques en revue de code** | Nombre de défauts bloquants détectés lors des revues de code (peer review Git). Mesure la maturité des pratiques de développement de l'équipe. | ≤ 3 / sprint | |
| **Temps de déploiement (pipeline CI/CD)** | Durée moyenne d'un cycle de build-test-deploy vers l'environnement de staging sur AWS Afrique du Sud. Essentiel vu la latence réseau Douala — cloud (150–250 ms). | ≤ 15 min | |
| **Disponibilité en mode dégradé (offline-first)** | % de fonctionnalités opérationnelles lors d'une coupure réseau. Exigence forte d'AGROCAM pour les agents Supply Chain sur le terrain (zones rurales sans couverture stable). | ≥ 70 % | |
| **Vélocité de l'équipe (story points/sprint)** | Nombre de story points livrés par sprint de deux semaines. Permet de projeter la date de fin réelle et d'alerter le client en cas de dérive par rapport aux jalons contractuels du forfait. | ≥ 30 SP | |

#### D. Analyse des écarts et actions correctives attendues

Pour chaque écart significatif identifié (budgétaire ou sur un KPI), votre équipe devra :

---

*Page 7 sur 10* *(repris depuis page suivante)*

---

- Identifier la cause racine de l'écart (technique, organisationnelle ou contextuelle — ex. : délestage, turnover, problème de connectivité) ;
- Proposer une action corrective concrète (réaffectation de ressources, révision du périmètre, optimisation du pipeline CI/CD, etc.) ;
- Estimer l'impact de cette action sur le respect du jalon contractuel suivant vis-à-vis d'AGROCAM S.A., en tenant compte des obligations du contrat au forfait.

L'ensemble de ces éléments (tableau de bord budgétaire, suivi des KPI, analyse des écarts et actions correctives) constituera une section dédiée de votre rapport collectif, étiquetée « Tableau de Bord de Pilotage — DIGITRANS-CM ». Cette section doit être rédigée dans un style professionnel, compréhensible par le Directeur Général d'AGROCAM S.A. sans formation technique préalable.

### I.5. Veille Technologique et Résolution de Problèmes (C13)

Votre rapport collectif devra comporter une section de veille technologique documentant au moins deux problèmes techniques significatifs rencontrés lors de la mise en œuvre, et la démarche de résolution adoptée. Pour chaque problème, vous devrez :

- Décrire précisément le problème rencontré dans le contexte du projet DIGITRANS-CM
- Identifier et citer les sources d'information consultées pour sa résolution (documentation officielle, articles techniques, forums professionnels)
- Inclure au moins une source en langue anglaise et démontrer comment elle a contribué à la résolution
- Présenter la solution retenue et justifier son choix par rapport aux alternatives considérées
- Évaluer l'impact de la solution sur la qualité globale du composant développé

### I.6. Revues d'Avancement et RETEX (C14, C15)

Votre équipe devra avoir organisé et documenté au moins deux revues d'avancement formelles au cours de la mise en situation. Chaque revue fera l'objet d'un compte rendu structuré comprenant : l'ordre du jour, les points abordés, les décisions prises, les actions à mener et les responsables associés. Ces comptes rendus seront annexés au rapport collectif.

Par ailleurs, une session de retour d'expérience (RETEX) finale devra être organisée et documentée. Cette session devra permettre à l'équipe d'identifier les bonnes pratiques acquises, les axes d'amélioration pour les prochains projets et les enseignements tirés en termes de qualité de code (réduction des bugs, amélioration de la structure du code, suppression des duplications identifiées lors des revues de code).

> **Critères de qualité attendus pour les livrables de la Partie 1 :** Le code source devra être propre, commenté et versionné. La documentation technique devra être structurée et lisible par un développeur tiers. Le rapport devra être rédigé dans un français professionnel, sans fautes d'orthographe, et refléter fidèlement le travail réellement accompli par l'équipe.

---

*Page 8 sur 10* *(estimé — pages 8 non photographiée)*

---

## PARTIE II — SOUTENANCE INDIVIDUELLE

| | |
|---|---|
| **Mode** | Individuelle (oral) |
| **Type** | Mise en situation professionnelle reconstituée |
| **Livrables attendus** | Présentation orale (15 à 20 min) + questions du jury (10 à 15 min) |
| **Compétences évaluées** | C10, C11, C12, C13, C14, C15 |

### II.1. Objectif général de la Partie

La soutenance individuelle constitue l'occasion pour chaque étudiant de défendre sa contribution personnelle au projet collectif devant un jury composé de professionnels du secteur numérique. Il ne s'agit pas simplement de présenter les livrables collectifs, mais de démontrer votre compréhension approfondie des choix effectués, de votre rôle au sein de l'équipe et de votre capacité à vous positionner en tant que professionnel expert.

### II.2. Structure de la Présentation

Votre présentation orale de **15 à 20 minutes** devra s'articuler autour des axes suivants :

#### Axe 1 — Positionnement dans le projet *(Environ 5 minutes)*

Présentez votre rôle précis au sein de l'équipe DIGITRANS-CM, les tâches qui vous ont été confiées et les responsabilités que vous avez assumées. Expliquez comment vous avez contribué à la planification et à la conduite du projet, et comment vous avez coordonné vos activités avec les autres membres de l'équipe.

#### Axe 2 — Contributions techniques personnelles *(Environ 7 minutes)*

Présentez les composants logiciels que vous avez personnellement développés ou sur lesquels vous êtes intervenu en priorité. Vous devrez être en mesure de naviguer dans le code source, d'expliquer vos choix de conception, de démontrer le fonctionnement des éléments implémentés et de justifier les solutions techniques retenues face aux alternatives envisagées. Montrez également comment vous avez utilisé des sources techniques (y compris en anglais) pour résoudre des problèmes concrets.

#### Axe 3 — Bilan critique et projection professionnelle *(Environ 5 minutes)*

Présentez votre analyse critique de la démarche adoptée par l'équipe : ce qui a bien fonctionné, ce qui aurait pu être amélioré, et comment les apprentissages de ce projet pourraient être appliqués dans un contexte professionnel réel au Cameroun. Positionnez-vous également sur les compétences que vous avez renforcées et sur les lacunes que vous souhaitez combler dans la suite de votre parcours.

### II.3. Questions du Jury

À l'issue de la présentation, le jury (composé d'au moins un professionnel du secteur numérique et d'un représentant pédagogique) posera des questions portant sur les éléments présentés. Les questions pourront porter sur des aspects techniques spécifiques (architecture, sécurité, performance), sur la gestion de projet

---

*Page 9 sur 10*

---

(justification des choix méthodologiques, gestion des écarts), ou sur votre projection professionnelle dans le marché camerounais du numérique.

Il vous est conseillé de préparer également une réponse à des questions du type : « Comment auriez-vous géré ce projet si le client avait réduit le budget de 30 % en cours de route ? » ou « Quelles adaptations feriez-vous à cette architecture si le module devait être déployé dans une zone sans accès Internet permanent ? » Ces questions situées reflètent les réalités du marché camerounais et seront appréciées par le jury.

**Conseils de présentation :** Le support de présentation (diaporama) est obligatoire. Il doit être sobre, professionnel et structuré. Le jury évaluera autant la qualité de communication et la posture professionnelle du candidat.

---

*Page 10 sur 10*
