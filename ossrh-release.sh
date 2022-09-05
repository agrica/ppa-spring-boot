#!/bin/bash -e

VERSION=${1}
 if [[ -z "${VERSION}" ]]; then
    read -p "Version à releaser ? " VERSION
 fi

git co -b prepare-release
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$VERSION
mvn clean install
git commit -am "Prepare Release version $VERSION"
git tag -a ossrh-$VERSION -m "Release version $VERSION"
git push origin ossrh-$VERSION
git checkout master
git branch -D prepare-release

# Not work pour 0-M3
git merge ossrh-$VERSION
mvn org.codehaus.mojo:versions-maven-plugin:2.7:set -DgenerateBackupPoms=false -DnextSnapshot=true
git commit -am "Prepare Next Snapshot"
git push origin master
