pipeline {
    agent any

    environment {
        GRADLE_WRAPPER = "./gradlew --refresh-dependencies --stacktrace -PbranchName=${env.BRANCH_NAME} -PtagName=${env.TAG_NAME} -PbuildNr=${env.BUILD_NUMBER}"
    }

    stages {
        stage('Prepaire') {
            steps {
                script {
                    sh 'chmod +x gradlew'
                }
            }
        }
        stage('Build Frontend & Backend') {
            steps {
                script {
                    sh "${GRADLE_WRAPPER} clean vaadinClean vaadinPrepareFrontend vaadinBuildFrontend build"
                }
            }
        }
        stage('Test & check') {
            steps {
                script {
                    sh "${GRADLE_WRAPPER} test check"
                }
            }
        }
        stage('Publishing jar to Maven-Repo') {
            environment {
                REPO = credentials('user-maven-repository')
            }
            steps {
                script {
                    sh "${GRADLE_WRAPPER} publish"
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh "${GRADLE_WRAPPER} dockerBuildImage"
                }
            }
        }
        stage('Push Docker Image') {
            environment {
                DOCKER = credentials('user-docker-hub')
            }
            steps {
                script {
                    sh "${GRADLE_WRAPPER} dockerPushImage"
                }
            }
        }
        stage('Deploy HypnoBook') {
            steps {
                script {
                    sh "ssh grande@10.12.0.1 'cd /docker/addons/hypno/; docker-compose up -d'"
                }
            }
        }

    }
}
