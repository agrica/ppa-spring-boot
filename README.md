# ppa-spring-boot

* Publication mondiale :https://central.sonatype.org/publish/publish-guide/
* Travis CI: https://travis-ci.com/github/agrica/ppa-spring-boot
* Nexus Staging  https://oss.sonatype.org:443/
* Repo Mondial: https://repo1.maven.org/maven2/io/github/agrica/ppa-spring-boot/

    
## Usage
Dans le POM maven du projet héritez du PPA Agrica
```xml
  <parent>
    <groupId>io.github.agrica</groupId>
      <artifactId>ppa-spring-boot</artifactId>
      <version>2.Y.Z</version>
   </parent>
```
## Releasing du PPA
```bash
./ossrh-release.sh 2.4.x
```
Pour
 * Créer une branche **prepare-release** depuis la branche master. Cette branche est juste une branche temporaire de préparation à la release
 * Dans la branche **prepare-release** modifier le pom.xml et changer la version à releaser, par exemple: 1.0.0-rc0 (il ne devrait plus y avoir SNAPSHOT)
 * Le build CI sur la branche **prepare-release** va se déclencher
 * Si le build CI est ok alors dans GitHub, créer une release: https://github.com/agrica/ppa-spring-boot/releases
   * Entrer le nom du tag qui doit correspondre à la version maven (ossrh-1.0.0-rc0)
   * Créer le tag à partir de la branche **prepare-release**
   * Publier la release
 * Le build CI du tag de release va se déclencher et les artefacts de release seront alors publier dans la partition des releases: https://repo1.maven.org/maven2/io/github/agrica/ppa-spring-boot/
 * Une fois la release déployée alors la branche **prepare-release** peut être supprimer
