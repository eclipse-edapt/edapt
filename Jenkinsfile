pipeline {
    agent {
        kubernetes {
            label 'centos-7'
        }
    }
    tools {
        maven 'apache-maven-3.8.6'
        jdk 'adoptopenjdk-hotspot-jdk11-latest'
    }
    options {
        timeout(time: 30, unit: 'MINUTES')
    }
    stages {
        stage('Build') {
            steps {
                wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
                    sh '''
                        cd builds/org.eclipse.emf.edapt.releng
                        mvn clean verify -Dtycho.disableP2Mirrors=true -B
                    '''
                }
            }
        }
    }
    post {
        always {
            junit '**/TEST-*.xml'
        }
    }
}