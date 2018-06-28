#!/usr/bin/env groovy
package tech.nhatnguyen;

def getsource(String giturl){
    stage("Checkout"){
        checkout([$class: 'GitSCM',
            branches: [[name: "${BRANCH_NAME}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [],
            submoduleCfg: [],
            userRemoteConfigs: [[credentialsId: "${CREDENTIAL_GITHUB}", url: giturl]]])

        }

}