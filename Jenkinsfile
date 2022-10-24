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
        stage('Build Vaadin Frontend') {
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
        stage('Publishing') {
            environment {
                REPO = credentials('user-maven-repository')
            }
            steps {
                sh "${GRADLE_WRAPPER} publish"
            }
        }
    }
}
