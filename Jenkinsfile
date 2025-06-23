pipeline {
    agent {
        kubernetes {
            label 'centos-7'
        }
    }
    tools {
        maven 'apache-maven-3.9.5'
        jdk 'temurin-jdk17-latest'
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
                        mvn clean verify -Pbuild-server -Dtycho.disableP2Mirrors=true -B
                        mvn clean verify -Platest-tp -Dtycho.disableP2Mirrors=true -B
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