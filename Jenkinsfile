node {
   try {
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


      // Setzen des erfolgreichen Buildresultat. Wichtig für Stash-Buildstatus
      currentBuild.result = 'SUCCESS'
   } catch (err) {
      println err
      currentBuild.result = 'FAILURE'

      def previousResult = currentBuild.getPreviousBuild() != null ? currentBuild.getPreviousBuild().getResult() : null
      def buildBroken = previousResult == 'SUCCESS' && currentBuild.getResult() == 'FAILURE'
      def msgSubject = buildBroken ? "Hypno-Book-Build broken" : "Hypno-Book-Build still failing:"
      def msgBody = """
${msgSubject}
Pipeline-Branch : ${env.JOB_URL}
Build-Run       : ${env.BUILD_URL}
"""
      emailext body: msgBody, recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], subject: msgSubject
   } finally {
      // Buildstatus in Stash auf SUCCESS oder FAILURE setzen. Abhängig von currentBuild.result
      //step([$class: 'StashNotifier'])
   }
}