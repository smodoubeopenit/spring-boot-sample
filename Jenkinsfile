
pipeline {
    agent any
    stages {
        stage ("init"){
                    agent any
                    steps {
                        script {
                            withCredentials([usernamePassword(credentialsId: 'auth-github-nappemy', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')]) {
                                env.VERSION = sh (
                                        script: 'git -c \'versionsort.suffix=-\' ls-remote --exit-code --refs --sort=\'version:refname\'  --tags  https://$GIT_USERNAME:$GIT_PASSWORD@github.com/smodoubeopenit/spring-boot-sample.git | tail -n1 | sed \'s/.*\\///; s/\\^{}//\'',
                                        returnStdout: true
                                ).trim()
                                if (env.VERSION==""){
                                    env.VERSION = "0.0.0"
                                }
                            }
                        }
                    }
                }
        stage ('pipeline-ci-cd'){
            parallel {
                stage('build and test for feature branch') {
                    when {
                        not {
                            anyOf {
                                branch 'master'
                                }
                            }
                        }
                    agent {
                        docker { image 'maven:3.8.3' }
                        }
                    steps {
                        sh 'make mvn_build VERSION=${VERSION}'
                        }
                    }


                stage('build and test branch master') {
                    when {
                        branch 'master'  
                        }
                        agent {
                            docker { image 'maven:3.8.3' }
                            }
                        steps {
                            sh 'make mvn_build VERSION=${VERSION}'
                        }
                    }

                stage('build and push image dev') {
                        when {
                                branch 'master'
                            }
                            agent any
                        steps {
                                withCredentials([usernamePassword(credentialsId: 'modou-docker-credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
                                    sh 'docker login -u $username -p $password'
                                }
                                sh 'make build_image_dev VERSION=${VERSION}'
                                sh 'make push_image_dev VERSION=${VERSION}'
                        }
                    }

                    stage('build and push image feature branch') {
                        when {
                            not {
                                anyOf {
                                    branch 'master'
                                        }
                                    }
                                }
                        agent any
                        steps {
                            withCredentials([usernamePassword(credentialsId: 'modou-docker-credentials', passwordVariable: 'password', usernameVariable: 'username')]) {
                                sh 'docker login -u $username -p $password'
                            }
                                sh 'make build_image_dev VERSION=${VERSION}'
                                sh 'make push_image_dev VERSION=${VERSION}'
                            }
                        }
                    }
                }
            }
        }