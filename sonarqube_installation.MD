# SonarQube Installation

## Prerequisites
- SonarQube server will require 3GB+ RAM to work effeciently

### Install Database
```shell
sudo -i
rpm -ivh http://repo.mysql.com/mysql57-community-release-el7.rpm
ls /etc/yum.repos.d/
rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2022
yum install mysql-server -y
systemctl start mysqld
systemctl enable mysqld
grep 'temporary password' /var/log/mysqld.log
#mysql_secure_installation -p <password>  -------------------------------> Temperory password  (Admin@123)
mysql_secure_installation
-------------------------------> Next Step: set-New-password
```
# Now, our Database server is ready #

### Install Java
```shell
yum install wget epel-release -y
yum install java -y
wget https://download.bell-sw.com/java/11.0.4/bellsoft-jdk11.0.4-linux-amd64.rpm
rpm -ivh bellsoft-jdk11.0.4-linux-amd64.rpm
#alternatives --config java
```

### Configure Linux System for Sonarqube
```shell
echo 'vm.max_map_count=262144' >/etc/sysctl.conf
sysctl -p
echo '* - nofile 80000' >>/etc/security/limits.conf
sed -i -e '/query_cache_size/ d' -e '$ a query_cache_size = 15M' /etc/my.cnf
systemctl restart mysqld
```
### Configure Database for Sonarqube
```shell
mysql -u root -p
mysql>
    create database sonarqube;
    show databases;
    create user 'sonarqube'@'localhost' identified by 'Redhat@123';
    grant all privileges on sonarqube.* to 'sonarqube'@'localhost';
    flush privileges;
```
### Install Sonarqube
```shell
yum install unzip -y
wget https://binaries.sonarsource.com/Distribution/sonarqube/sonarqube-7.9.1.zip
cd /opt
unzip ~/sonarqube-7.9.1.zip
mv sonarqube-7.9.1 sonar
```
### Configure Sonarqube
```shell
sed -i -e '/^sonar.jdbc.username/ d' -e '/^sonar.jdbc.password/ d' -e '/^sonar.jdbc.url/ d' -e '/^sonar.web.host/ d' -e '/^sonar.web.port/ d' /opt/sonar/conf/sonar.properties
sed -i -e '/#sonar.jdbc.username/ a sonar.jdbc.username=sonarqube' -e '/#sonar.jdbc.password/ a sonar.jdbc.password=Redhat@123' -e '/InnoDB/ a sonar.jdbc.url=jdbc.mysql://localhost:3306/sonarqube?useUnicode=true&characterEncoding=utf&rewriteBatchedStatements=true&useConfigs=maxPerformance' -e '/#sonar.web.host/ a sonar.web.host=0.0.0.0' /opt/sonar/conf/sonar.properties
useradd sonar
chown sonar:sonar /opt/sonar/ -R
sed -i -e '/^#RUN_AS_USER/ c RUN_AS_USER=sonar' /opt/sonar/bin/linux-x86-64/sonar.sh
```
### Start Sonarqube
```shell
/opt/sonar/bin/linux*/sonar.sh start
/opt/sonar/bin/linux*/sonar.sh status
cd /opt/sonar/logs
```

## Whitelist the ports
```shell
    - Sonarqube = 9000
```

## Try to access the Sonarqube application
```shell
Public IP:9000
```

## Default ID and Password
```shell
username = admin
```
## Understand the Sonarqube console

## Setup the project in sonarqube
1. Project Name: studentapp-ui
2. Genrate the token: save to token, it's only one time visible.
sonar-token: d684ecdc405d284e7448a5a151570301fd883792
3. Set-up the required java enviorment
4. Setup the build tool (maven)
    then we got the scanner maven commands, to run on jenkins server.
```shell
mvn sonar:sonar \
  -Dsonar.projectKey=studentapp-ui \
  -Dsonar.host.url=http://65.0.6.149:9000 \
  -Dsonar.login=d684ecdc405d284e7448a5a151570301fd883792
```
5. First of all clone the studentapp-ui repository in jenkins server
6. Run te 4th no's all maven commands in maven project's home directory, where pom.xml file is present.
```shell
git clone https://github.com/chetansomkuwar254/studentapp.ui.git
cd /studentapp.ui
mvn clean package
# run mvn sonar:sonar <commands>
```
7. Now this project made in sonarqube applicaiton, and it passed from quality-gate criteria.
8. Tester and developer will create and manage quality gate.

## Add the maven scanner commands in pipeline
```shell
run mvn sonar:sonar <commands>
```
----------------------------------------------------------------------------------------------------
## Another way to integrate sonarqube with jenkins
Plugins:
1. Sonarqube scanner
2. Sonar Quality GatesVersion 1.3.1

# Pass the credentials in Jenkins
Dashboard > System > Global Credentials > Add creds
    - Secret text: <token>
    - ID: sonar-token
![alt text](image.png)

# Meanwhile add Enviormental variable of sonarqube in Jenkins.
Dashboard > Manage jenkins > System > Add SonarQube Scanner Enviorment
    Name: Sonar-server
    URL: http://65.0.6.149:9000/
    Secret: <token>
![alt text](image-1.png)

# Create Pipeline script for sonarqube
    - Pipeline syntax
    - Sample step: withSonarQubeEnv: Prepare sonarqube Enviorment
    - Add token
    - Generate the pipeline script

# Pipepine script
```shell
        stage('Test') {
            steps {
                sh '/opt/apache-maven-3.9.6/bin/mvn sonar:sonar -Dsonar.projectKey=studentapp-ui -Dsonar.host.url=http://52.23.178.3:9000 -Dsonar.login=19f664a34d8599cd11e28611b4b9da6c14916402'
                echo 'Here Test the application code from repostiory'
            }
        }
```
----------------------------------------------------------------------------------------------------
# If we want to apply condition on quality-gate for our application code. For that we need to create a Quality-gate rule in sonarqube-server application.
Note: If our application code has gate failed then, our application is not able to deploy for that we set quality gate.
    # waitforquality gate

# Wait for quality gate syntax (timeout-time)
# Scenarion: If my quality-gate has not passed then my pipeline is in paused condition, at that time my quality-gate server is down for 2 days, till my pipeline has stucked for 2 days fro all remaining stages for that we add the timeout sesion in quality-gate


# we create script through pipeline syntax generator
    - Pipeline syntax
    - Sample step: waitForQualityGate wait for sonarqube analysis to be completed and return quality gate status
    - ServerAuthentication toke: sonar-token
    - Generate the pipeline sccript: <script> 
```shell
# If my quality gate is failed so,
waitForQualityGate abortPipeline: true;
```
----------------------------------------------------------------------------------------------------

# changed the quality-gate type in sonarqube applicaiton
    - Daufalut
    - Customized one

----------------------------------------------------------------------------------------------------


