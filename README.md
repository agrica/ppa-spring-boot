# ppa-spring-boot

Projet PPA pour les projets Spring Boot

## pom.xml

Il contient :
- **dependencyManagement** : les versions des dépendances
- **distributionManagement** : les repos release et snapshot
- **issueManagement** : l'emplacement du JIRA   
IMPORTANT : **redéfinir _<jira-project>_ dans les applications Spring Boot**
- pour les définitions des builds de configurations, rechercher :
    - **make-it-assembly-config** : pour les configuration de l'environnement (eg. BDD)
    - **make-it-assembly-resources** : pour les configuration applicatives par environnement (eg. niveau de LOG)  
    IMPORTANT : pour chacune des configurations, l'applications Spring Boot devra respecter l'arborescence prévue et les  
    redéfinir avec **[itemsWithProperties](https://khmarbaise.github.io/iterator-maven-plugin/iterator-mojo.html)** qui précise les environnements sur lesquels l'application sera déployée
- **A COMPLETER...**
---
     Notes : 
     
     1. l'utilisation du "iterator-maven-plugin" permet d'exécuter avec différentes properties 
     "maven-assembly-plugin" et de générer autant d'artefacts (dans les deux cas décrits)
     
     2. Il faut respecter l'arborescence attendue dans ${project.basedir}/src/main/configuration/ 
     
     3. Par défaut, s'il manque le descripteur d'assembly, la tâche ne sera pas effectuée silencieusement
     grâce à la configuration <ignoreMissingDescriptor>true</ignoreMissingDescriptor>  
---     


## remarques / propositions :
- plus de **${serverName}** => les confs sont agnostiques de la machine
- associer la génération des configurations à un profil et ne plus mettre 
    ```
    <ignoreMissingDescriptor>true</ignoreMissingDescriptor>
    ``` 
- kezako si plusieurs projets JIRA ? **eg. HDF / PILPA**
    
- le profil **todir** pour déployer les artefact dans des répertoires ne fonctionne plus... 
 