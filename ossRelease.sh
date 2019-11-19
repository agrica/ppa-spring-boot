#!/bin/bash -e

VERSION=${1:-0.0.1-SNAPSHOT}

git co -b prepare-release
mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$VERSION
mvn clean install
git commit -am "Prépare Release version $VERSION"
git tag -a ossrh-$VERSION -m "Release version $VERSION"
git push origin ossrh-$VERSION
git checkout master
git branch -D prepare-release