node {
  def gradleWrapper = "./gradlew --refresh-dependencies --stacktrace -PbranchName=${env.BRANCH_NAME} -PtagName=${env.TAG_NAME} -PbuildNr=${env.BUILD_NUMBER}"
  echo "BRANCH: ${env.BRANCH_NAME}"

  stage("Clean & Checkout") {
     checkout scm
     sh 'git clean -dxf'
  }

  stage("Compile & Test") {
     withGradle {
        sh "${gradleWrapper} clean build check"
     }
  }
}