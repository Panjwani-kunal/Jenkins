pipeline {
    agent any

    stages {
        stage('Pull') {
            steps {
                git changelog: false, poll: false, url: 'https://github.com/chetansomkuwar254/studentapp.ui'
                echo 'Here we are pulling the application code repostiory'
            }
        }
        stage('Build') {
            steps {
                sh '/opt/apache-maven-3.9.6/bin/mvn clean package'
                echo 'Here build the application code from repostiory'
            }
        }
        stage('Test') {
            steps {
                sh '/opt/apache-maven-3.9.6/bin/mvn sonar:sonar -Dsonar.projectKey=studentapp-ui -Dsonar.host.url=http://65.0.181.55:9000 -Dsonar.login=d684ecdc405d284e7448a5a151570301fd883792'
                echo 'Here we are testing the application code from repostiory'
            }
        }
        stage('Deploy') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'tomcat-creds', path: '', url: 'http://13.232.140.36:8080/')], contextPath: '/', war: '**/*.war'
                echo 'Here we are deploying the application code from repostiory'
            }
        }                                                                 
    }
}
