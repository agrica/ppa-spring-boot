#!/bin/bash -e

BRANCH_NAME="release/2.4.x"
VERSION=${1}
 if [[ -z "${VERSION}" ]]; then
    read -p "Version à releaser ? " VERSION
 fi

echo "1- Create branch 'prepare-release'"
git co -b prepare-release

echo "2- Set Maven Version $VERSION"
mvn org.codehaus.mojo:versions-maven-plugin:2.11.0:set -DgenerateBackupPoms=false -DnewVersion=$VERSION
mvn clean install
git commit -am "Prepare Release version $VERSION"

echo "3- Create Maven Tag $VERSION"
git tag -a ossrh-$VERSION -m "Release version $VERSION"
git push origin ossrh-$VERSION

echo "4- Delete branch 'prepare-release'"
git checkout ${BRANCH_NAME}
git branch -D prepare-release

# Not work pour 0-M3
git merge ossrh-$VERSION

echo "6- Increment Version '${BRANCH_NAME}'"
mvn org.codehaus.mojo:versions-maven-plugin:2.11.0:set -DgenerateBackupPoms=false -DnextSnapshot=true
git commit -am "Prepare Next Snapshot"
git push origin ${BRANCH_NAME}
