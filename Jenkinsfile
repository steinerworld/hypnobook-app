pipeline {
    agent any

    environment {
        GRADLE_WRAPPER = "./gradlew --refresh-dependencies --stacktrace -PbranchName=${env.BRANCH_NAME} -PtagName=${env.TAG_NAME} -PbuildNr=${env.BUILD_NUMBER}"
    }

    stages {
        stage('Prepaire') {
            steps {
                sh 'chmod +x gradlew'
                script {
                    if (env.TAG_NAME != null) {
                        currentBuild.description = "Version: ${env.TAG_NAME}"
                    } else {
                        def props = readProperties  file: 'gradle.properties'
                        def ver = "${props.MAJOR_VERSION}.${props.MINOR_VERSION}+${env.BUILD_NUMBER}"
                        currentBuild.description = "Version: $ver"
                    }
                }
            }
        }
        stage('Build Frontend & Backend') {
            steps {
                sh "${GRADLE_WRAPPER} clean vaadinClean currentVersion vaadinPrepareFrontend vaadinBuildFrontend build"
            }
        }
        stage('Test & check') {
            steps {
                sh "${GRADLE_WRAPPER} test check"
            }
        }
        stage('Publishing jar to Maven-Repo') {
            when { buildingTag() }
            environment {
                REPO = credentials('user-maven-repository')
            }
            steps {
                sh "${GRADLE_WRAPPER} publish"
            }
        }
        stage('Build Docker Image') {
            when { buildingTag() }
            steps {
                sh "${GRADLE_WRAPPER} dockerBuildImage"
            }
        }
        stage('Push Docker Image') {
            when { buildingTag() }
            environment {
                DOCKER = credentials('user-docker-hub')
            }
            steps {
                sh "${GRADLE_WRAPPER} dockerPushImage"
            }
        }
        stage('Deploy HypnoBook') {
            when { buildingTag() }
            steps {
                sh "ssh grande@10.12.0.1 'cd /docker/addons/hypno/; export VERSION=${env.TAG_NAME}; docker-compose up -d'"
            }
        }
    }
}
