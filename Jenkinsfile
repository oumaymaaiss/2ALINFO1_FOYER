pipeline {
    agent any // Exécute le pipeline sur n'importe quel agent disponible

    tools {
        // Ces noms DOIVENT correspondre EXACTEMENT à ceux configurés dans Jenkins > Manage Jenkins > Global Tool Configuration
        jdk 'OpenJDK_17' // Nom que vous avez donné à votre JDK 17 dans Jenkins
        maven 'Maven_3.9.10' // Nom que vous avez donné à votre Maven 3.9.10 dans Jenkins
    }

    environment {
        // URL de votre serveur SonarQube (le nom du service dans docker-compose)
        SONAR_HOST_URL = 'http://sonarqube:9000'
        // ID du token SonarQube configuré dans Jenkins Credentials
        SONAR_AUTH_TOKEN = credentials('sonarqube-token') // L'ID que vous avez donné à votre Secret text
        // Nom de l'image Docker que vous allez construire (votre_nom_utilisateur_docker/nom_app)
        DOCKER_IMAGE_NAME = 'oumaymaaiss/foyer-app' // Votre nom d'utilisateur Docker Hub
        DOCKER_IMAGE_TAG = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                // Cloner le code depuis votre dépôt GitHub
                git branch: 'master', url: 'https://github.com/oumaymaaiss/2ALINFO1_FOYER.git'
            }
        }

        stage('Build') {
            steps {
                // Exécuter le build Maven
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                // Exécuter les tests unitaires Maven
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Lancer l'analyse SonarQube
                withSonarQubeEnv('SonarQube Local') { // Le nom que vous avez donné à votre serveur SonarQube dans Jenkins
                    sh "mvn sonar:sonar -Dsonar.projectKey=foyer-app -Dsonar.projectName=Foyer-App -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_AUTH_TOKEN}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                // Construire l'image Docker
                script {
                    docker.build("${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}", '.')
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                // Pousser l'image vers Docker Hub
                script {
                    // 'docker-hub-credentials' est l'ID de vos identifiants Docker Hub configurés dans Jenkins Credentials
                    // (Manage Jenkins > Manage Credentials > (global) > Add Credentials > Username with password)
                    docker.withRegistry("https://registry.hub.docker.com", 'docker-hub-credentials') {
                        docker.image("${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}").push()
                    }
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                // Exemple de déploiement simple sur un conteneur local
                // Arrêter et supprimer l'ancien conteneur si existant
                sh 'docker stop foyer-app-container || true'
                sh 'docker rm foyer-app-container || true'
                // Lancer un nouveau conteneur avec l'image fraîchement poussée
                sh "docker run -d -p 8080:8080 --name foyer-app-container ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
            }
        }
    }

    post {
        always {
            // Nettoyage après chaque build (succès ou échec)
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

