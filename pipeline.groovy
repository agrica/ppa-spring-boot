pipeline {
    agent { node { label 'master' } }

    stages {
       stage('Checkout') {
		   steps {
			 deleteDir()
			 git branch: '7.x', credentialsId: 'compte-gitlab', url: 'git@gitlab-dei:socle/drupal.git'
		   }
	   }

        stage('Mirror Official') {
            steps {
               sh '''
                    git remote add upstream https://github.com/drupal/drupal.git
                    git fetch upstream
                    git push --all origin
                    git push --tags origin
                    git  tag --list 7*
                '''
                //git  tag --list 7* --sort version:refname
            }
        }

        stage('Input tag') {
            agent none
            steps {
                 timeout(10) {
                    script {
                        forkTag =input id: 'ForkTag', message: 'Version de drupal CORE à packager ?', parameters: [string(defaultValue: '', description: '', name: 'forkTag', trim: true)]
                        echo ("forkTag: "+ forkTag)
                    }
                 }
            }
        }

       stage('branch Agrica') {
            steps {
                script {
                  sh """
                    echo forkTag=$forkTag
                    git checkout -b $forkTag-agrica $forkTag
                    git cherry-pick 54b6d730276ee9c4fbedc3924ddb094a4833f1c1 4158bfa07cfa6dd11052c2769c4dd218d6bdec83
                  """
                    withMaven(globalMavenSettingsConfig: 'org.jenkinsci.plugins.configfiles.maven.GlobalMavenSettingsConfig1467648083719', jdk: 'JDK 8.x', maven: 'Maven 3.x', mavenSettingsConfig: 'maven_settings') {
                        sh """
                           mvn versions:update-parent -DgenerateBackupPoms=false  -DparentVersion=0.1.9
                           mvn versions:set -DgenerateBackupPoms=false  -DnewVersion=$forkTag-SNAPSHOT
                        """
                    }
                    sh """
                     git commit -am "Change version to $forkTag-SNAPSHOT"
                     git status
                    """
                }
            }
        }

        stage('branch Agrica patch') {
            steps {
                script {
                  sh """
                    git checkout -b $forkTag-agrica-patch
                    git cherry-pick 040fca7550ea31960884a77a68af4e5e4c1826e9 d1bfd762edd8d64306bf20e31171c58f534d88d3
                  """
                    withMaven(globalMavenSettingsConfig: 'org.jenkinsci.plugins.configfiles.maven.GlobalMavenSettingsConfig1467648083719', jdk: 'JDK 8.x', maven: 'Maven 3.x', mavenSettingsConfig: 'maven_settings') {
                        sh "mvn versions:set -DgenerateBackupPoms=false  -DnewVersion=$forkTag-PATCH-SNAPSHOT"
                    }
                    sh """
                     git commit -am "Change version to $forkTag-PATCH-SNAPSHOT"
                     git status
                    """
                }
            }
        }

        stage('Push branches') {
              steps {
                script {
                     sh """
                         git push --all origin
                        """
                }
              }
        }

        stage('Release branch Agrica') {
             steps {
                script {
                    sh """
                        git checkout $forkTag-agrica
                        git status
                    """
                    withMaven(globalMavenSettingsConfig: 'org.jenkinsci.plugins.configfiles.maven.GlobalMavenSettingsConfig1467648083719', jdk: 'JDK 8.x', maven: 'Maven 3.x', mavenSettingsConfig: 'maven_settings') {
                        sh  """
                            mvn -B release:prepare release:perform  -Ddist.releases.url=http://nexus/repository/maven-thirdparty/ -DreleaseVersion=$forkTag.0 -DdevelopmentVersion=$forkTag.1-SNAPSHOT
                        """
                    }
                }
            }
        }


        stage('Release branch Agrica patch') {
             steps {
                script {
                    sh """
                        git checkout $forkTag-agrica-patch
                        git status
                    """
                    withMaven(globalMavenSettingsConfig: 'org.jenkinsci.plugins.configfiles.maven.GlobalMavenSettingsConfig1467648083719', jdk: 'JDK 8.x', maven: 'Maven 3.x', mavenSettingsConfig: 'maven_settings') {
                        sh  """
                            mvn -B release:prepare release:perform  -Ddist.releases.url=http://nexus/repository/maven-thirdparty/ -DreleaseVersion=$forkTag.0-PATCH -DdevelopmentVersion=$forkTag.1-PATCH-SNAPSHOT
                        """
                    }
                }
            }
        }


    }
}