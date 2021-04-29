# ppa-spring-boot [![Build Status](https://travis-ci.com/agrica/ppa-spring-boot.svg?branch=master)](https://travis-ci.com/agrica/ppa-spring-boot)

* Travis CI: https://travis-ci.com/agrica/ppa-spring-boot
* Repo Mondial: https://repo1.maven.org/maven2/io/github/agrica/ppa-spring-boot/

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
