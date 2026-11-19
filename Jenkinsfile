
pipeline {
    agent any
environment {
    PATH = "C:\\Users\\velug\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin;${env.PATH}"
}
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                bat 'mvnw.cmd clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                bat 'mvnw.cmd test'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t devi45/library-reservation:latest .'
            }
        }

        
stage('Docker Push') {
    steps {
        withCredentials([usernamePassword(
            credentialsId: 'dockerhub-pat-new',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASSWORD'
        )]) {
            bat 'powershell -NoProfile -NonInteractive -Command "$env:DOCKER_PASSWORD | docker login --username $env:DOCKER_USER --password-stdin"'
            bat 'docker push devi45/library-reservation:latest'
        }
    }
}


        stage('Kubernetes Deploy') {
            steps {
                bat 'kubectl apply -f k8s/deployment.yaml'
                bat 'kubectl apply -f k8s/service.yaml'
                bat 'kubectl rollout restart deployment/library-reservation'
            }
        }
    }
}
