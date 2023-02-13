pipeline {
    agent { label 'built-in' }

    options {
        timestamps()
        ansiColor('xterm')
        disableConcurrentBuilds()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Cleanup') {
            steps {
                deleteDir()
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Clean deploy') {
            steps {
                script {
                    withMaven(globalMavenSettingsConfig: 'maven-global-settings', mavenSettingsConfig: 'socle-maven-settings', jdk: 'OpenJDK 17.x', maven: 'Maven 3.x') {
                        sh "mvn clean deploy"
                    }
                }
            }
        }

    }
}
