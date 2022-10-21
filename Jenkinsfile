pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'chmod +x gradlew'
                    sh './gradlew clean build test --no-daemon'
                }
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
