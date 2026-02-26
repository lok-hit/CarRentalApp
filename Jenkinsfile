pipeline {
    agent any

    environment {
        REGISTRY = 'your-registry.example.com'
        IMAGE_NAME = 'api-gateway'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn -B -q clean verify'
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('api-gateway') {
                    sh "docker build -t ${REGISTRY}/${IMAGE_NAME}:${env.BUILD_NUMBER} ."
                }
            }
        }

        stage('Push Docker Image') {
            when {
                expression { return env.BRANCH_NAME == 'develop' || env.BRANCH_NAME == 'main' }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-registry-creds',
                                                 usernameVariable: 'DOCKER_USER',
                                                 passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin ' + "${REGISTRY}"
                    sh "docker push ${REGISTRY}/${IMAGE_NAME}:${env.BUILD_NUMBER}"
                }
            }
        }
    }
}
