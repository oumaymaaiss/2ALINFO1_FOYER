pipeline {
    agent any

    tools {
        jdk 'OpenJDK_17'
        maven 'Maven_3.9.10'
    }

    environment {
        SONAR_HOST_URL = 'http://localhost:8100'
        SONAR_AUTH_TOKEN = credentials('sonarqube-token')
        DOCKER_IMAGE_NAME = 'oumaymaaiss/foyer-app'
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {
        stage('Cloner le code') {
            steps {
                sh 'git clone --branch master https://github.com/oumaymaaiss/2ALINFO1_FOYER.git foyer-project'
            }
        }

        stage('Build') {
            steps {
                dir('foyer-project') {
                    sh 'mvn clean install'
                }
            }
        }

        stage('Tests') {
            steps {
                dir('foyer-project') {
                    sh '''
                        mvn test \
                        -Dspring.datasource.url="jdbc:mysql://localhost:3306/foyer?createDatabaseIfNotExist=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC" \
                        -Dspring.datasource.username=root \
                        -Dspring.datasource.password=0000
                    '''
                }
            }
        }

        stage('Analyse SonarQube') {
            steps {
                dir('foyer-project') {
                    withSonarQubeEnv('SonarQube Local') {
                        sh '''
                            mvn sonar:sonar \
                            -Dsonar.projectKey=foyer-app \
                            -Dsonar.projectName=Foyer-App \
                            -Dsonar.host.url=${SONAR_HOST_URL} \
                            -Dsonar.login=${SONAR_AUTH_TOKEN}
                        '''
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('foyer-project') {
                    script {
                        docker.build("${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}")
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image("${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }

        stage('Déploiement local (Docker)') {
            steps {
                sh 'docker stop foyer-app-container || true'
                sh 'docker rm foyer-app-container || true'
                sh "docker run -d -p 8081:8080 --name foyer-app-container ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline terminé avec succès !'
        }
        failure {
            echo 'Pipeline échoué ! Vérifiez les logs.'
        }
    }
}
