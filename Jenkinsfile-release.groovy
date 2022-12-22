pipeline {
    agent { label 'built-in' }

    // Simple : '^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(-[a-z0-9]+)?$'
    //  (?in)^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(-([a-z-][\da-z-]+|[\da-z-]+[a-z-][\da-z-]*|0|[1-9]\d*)(\.([a-z-][\da-z-]+|[\da-z-]+[a-z-][\da-z-]*|0|[1-9]\d*))*)?(\+[\da-z-]+(\.[\da-z-]+)*)?$
    parameters {
        validatingString(name: 'RELEASE_VERSION', defaultValue: '',description: 'Version de la release',
                regex: '^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(-([a-z-][\\da-z-]+|[\\da-z-]+[a-z-][\\da-z-]*|0|[1-9]\\d*)(\\.([a-z-][\\da-z-]+|[\\da-z-]+[a-z-][\\da-z-]*|0|[1-9]\\d*))*)?(\\+[\\da-z-]+(\\.[\\da-z-]+)*)?$',failedValidationMessage: 'Version suivant la norme  SemVer (https://semver.org/)')
    }

    options {
        timestamps()
        ansiColor('xterm')
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {

        stage('Clean Checkout') {
            steps {
                deleteDir()
                checkout scm
            }
        }

        stage('release') {
            steps {
                script {
                    withMaven(globalMavenSettingsConfig: 'maven-global-settings', mavenSettingsConfig: 'ditw-maven-settings', jdk: 'OpenJDK 11.x', maven: 'Maven 3.x') {
                        sh "mvn -B gitflow:release-start gitflow:release-finish -DpostReleaseGoals=deploy -DreleaseVersion=${RELEASE_VERSION}"
                    }
                }
            }
        }

   }
}
