#!/usr/bin/env groovy
package tech.nhatnguyen;

def getcode(String giturl) {
    stage("Checkout") {
        checkout([$class                           : 'GitSCM',
                  branches                         : [[name: "${BRANCH_NAME}"]],
                  doGenerateSubmoduleConfigurations: false,
                  extensions                       : [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[credentialsId: "${CREDENTIAL_GITHUB}", url: giturl]]])

    }

}

def getcode(String giturl) {
    stage("Checkout") {
        checkout([$class                           : 'GitSCM',
                  branches                         : [[name: "${BRANCH_NAME}"]],
                  doGenerateSubmoduleConfigurations: false,
                  extensions                       : [],
                  submoduleCfg                     : [],
                  userRemoteConfigs                : [[credentialsId: "${CREDENTIAL_GITHUB}", url: giturl]]])

    }

}
def pulldocker(String image){
    stage("Pull Latest Images"){
        sh "sudo docker pull ${image}:latest"
    }   
}
def buildbydocker(){
    stage("Build"){
        sh 'sudo docker-compose up  -d --force-recreate'
        sh 'sudo docker-compose exec -T web bash docker/install.sh'           
    }
}
def startdockerrails(){
    stage('Start Server') {
        sh 'sudo docker-compose exec -d -T web bash docker/startserver.sh'
        } 
    }
def errhandlerwithdocker(){
    currentBuild.result = 'FAILURE'
    sh 'sudo docker-compose down'
}


