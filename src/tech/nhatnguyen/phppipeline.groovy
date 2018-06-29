#!/usr/bin/env groovy
package tech.nhatnguyen;

def cleanws(){
    stage('Clean') {
        sh 'sudo rm -rf *'
    }
}
def getsources(){
    stage('Checkout') {
        checkout scm
    }
}
def checkcodeQuality(String projectKey){
    stage('Analys'){
        def scannerHome = tool 'scanner';
        withSonarQubeEnv('Sonar') {
            sh "${scannerHome}/bin/sonar-scanner " +
                    "-Dsonar.projectKey=${projectKey} " +
                    "-Dsonar.sources=. -Dsonar.host.url=${env.SONAR_URL} " +
                    "-Dsonar.login=${env.SONAR_KEY} " +
                    "-Dsonar.exclusions=vendor/**,public/**,e2e/**,tests/**,Test-Automation/**,_ide_helper.php"
        }
    }
}
def build(String envpath){
    stage("Build"){
        sh 'sudo /usr/local/bin/docker-compose build'
        sh 'sudo /usr/local/bin/docker-compose up -d'
        sh "sudo cp ${envpath} .env"
        sh 'sudo /usr/local/bin/docker-compose exec -T web bash ./docker/build.sh'
    }
}
def e2eTest(){
    stage('Test'){
        sh 'cp e2e/.env.example e2e/.env'
        sh 'cd e2e && docker-compose up -d'
        sh "cd e2e && '${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean verify"
    }
}
def testReport(){
    stage('Results') {
            allure includeProperties: false, jdk: '',
             results: [[path: 'e2e/target/allure-results']]
    }
}
def tearDown(){
    stage('Tear down'){
            sh 'cd .. '
            sh 'sudo /usr/local/bin/docker-compose down'
    }
}
