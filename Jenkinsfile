pipeline {
    agent any

    environment {
        ARCHIVA = credentials('jenkins-user-for-mavel-artifact-repository')
    }

    stages {
        stage('Prepaire') {
            steps {
                script {
                    sh 'chmod +x gradlew'
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    sh './gradlew clean build'
                }
            }
        }
        stage('Build Vaadin Frontend') {
            steps {
                script {
                    sh './gradlew vaadinClean vaadinPrepareFrontend vaadinBuildFrontend'
                }
            }
        }
        stage('Test & check') {
            steps {
                script {
                    sh './gradlew test check'
                }
            }
        }
        stage('Publishing') {
            steps {
                sh './gradlew publish'
            }
        }
    }
}
