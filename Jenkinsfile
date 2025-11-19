pipeline {
    agent {
        docker {
            image 'maven:3.8.5-openjdk-17'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    environment {
        SONAR_PROJECT_KEY = "SupplyChainBrief"
        // CORRECTION CRITIQUE: Doit utiliser le nom du service Docker 'sonarqube'
        SONAR_HOST_URL = "http://sonarqube:9000"
    }

    stages {
        stage('Checkout') {
                    steps {
                        // Syntaxe complète pour spécifier la branche et l'URL
                        git branch: 'master', url: 'https://github.com/AbdeljalilElouafi/SupplyChainX.git'
                        // Assurez-vous que 'main' est le nom de votre branche.
                    }
        }

        stage('Build') {
            steps {
                sh "mvn clean install -DskipTests"
            }
        }

        stage('Test & Analyze') {
            steps {
                withSonarQubeEnv('SonarQube-Server') {
                    sh "mvn clean verify sonar:sonar -Dsonar.projectKey=${SONAR_PROJECT_KEY} -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_TOKEN_JENKINS_CREDS_ID}"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "mvn spring-boot:build-image -Dspring-boot.build-image.imageName=supplychainbrief:latest"
            }
        }
    }
}