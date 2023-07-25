pipeline {
    agent { label 'sonar-nodejs' }

    triggers {
        cron('@midnight')
    }

    options {
        timestamps()
        ansiColor('xterm')
        buildDiscarder(logRotator(numToKeepStr: '1'))
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps {
                deleteDir()
                checkout scm
            }
        }


        stage('SonarQube Analysis') {
            steps {
                configFileProvider([configFile(fileId: 'add-build-vars.sh', variable: 'ADD_BUILD_VARS')]) {
                    sh 'source $ADD_BUILD_VARS'
                }
                script {
                    def props = readProperties file: 'jenkins-build-vars.properties'
                    echo "jenkins-build-vars.properties=${props}"
                    withSonarQubeEnv('sonarqube') {
                        nodejs(nodeJSInstallationName: 'NodeJS 16.x', configId: 'config-npm') {
                            withMaven(globalMavenSettingsConfig: 'maven-global-settings', mavenSettingsConfig: 'socle-maven-settings', jdk: 'OpenJDK 11.x', maven: 'Maven 3.x', options: [artifactsPublisher(disabled: true)]) {
                                  sh "mvn  -f pom.xml clean test -Dodc.plugins.scan=true  $SONARQUBE_SCANNER4MVN_GOAL"
                            }
                        }
                    }
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                script {
                    archiveArtifacts artifacts: 'target/**.xml,target/**.xml,target/owasp-deps/*', followSymlinks: false
                }
            }
        }
        stage('Dependency Publish') {
            steps {
                script {
                    dependencyCheckPublisher pattern: 'target/**/owasp-deps/dependency-check-report.xml'
                }
            }
        }




    }
}
