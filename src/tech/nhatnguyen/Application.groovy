#!/usr/bin/env groovy
package tech.nhatnguyen;

def run(Object step){
    try{
        step.execute();
    }catch(err){
        currentBuild.result = "FAILURE"
        error(message)
    }
}
return this