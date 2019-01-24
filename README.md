# ppa-spring-boot

Projet PPA pour les projet springBoot

## pom.xml

Il contient :
- **dependencyManagement** : les versions des dépendances
- **distributionManagement** : les repos release et snapshot
- **issueManagement** : l'emplacement du JIRA
- pour les définitions des builds de configurations, rechercher :
    - **make-it-assembly-config** : pour les configuration de l'environnement (eg. BDD)
    - **make-it-assembly-resources** : pour les configuration applicatives par environnement (eg. niveau de LOG)
---
     Notes : 
     
     1. l'utilisation du "iterator-maven-plugin" permet d'exécuter avec différentes properties 
     "maven-assembly-plugin" et de générer autant d'artefacts (dans les deux cas décrits)
     
     2. Il faut respecter l'arborescence attendue dans ${project.basedir}/src/main/configuration/ 
     
     3. Par défaut, s'il manque le descripteur d'assembly, la tâche ne sera pas effectuée silencieusement
     grâce à la configuration <ignoreMissingDescriptor>true</ignoreMissingDescriptor>  
---     

## propositions

- associer la génération des configurations à un profil et ne plus mettre 
    ```
    <ignoreMissingDescriptor>true</ignoreMissingDescriptor>
    ``` 
- 