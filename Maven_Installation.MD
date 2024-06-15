# Maven Installation and Setup Enviorment 

```shell
java --version
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz
sudo tar -xvzf apache-maven-3.9.6-bin.tar.gz
sudo mv apache-maven-3.9.6 /opt/
cd /opt/apache-maven-3.9.6/bin/
sudo vim /etc/profile
  export PATH=$PATH:'/opt/apache-maven-3.9.6/bin/'
source /etc/profile
bash
mvn --version
```
# Clone the Developer App repository

```shell
sudo -i
cd /opt
sudo git clone https://github.com/chetansomkuwar254/studentapp.ui.git
cd studentapp.ui
mvn package
# mvn clean package
```

## Error:
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-war-plugin:2.6:war (default-war)
on project studentapp: Execution default-war of goal org.apache.maven.plugins:maven-war-plugin:2.6:war
failed: Unable to load the mojo 'war' in the plugin 'org.apache.maven.plugins:maven-war-plugin:2.6' 
due to an API incompatibility: org.codehaus.plexus.component.repository.exception.ComponentLookupException: null

==> .mojo is not supported by Java-17, so we need to install java-11 or below.

``` shell 
java --version
sudo apt update -y
sudo apt install openjdk-11-jdk -y
```
## To check diffrent version of java or which Java version is curretnly in used. To ensure that use below command.
```shell
sudo update-alternatives --config java
# now to select the exact Java 11 version and Enter.
java --version   ... It should be java 11 running
```
Now, Build it again.


## Note: mvn command always run from the pwd where our pom.xml file is avialable

# Jenkins-App: Global Tool configuration
----------------------------------------
here we are adding the maven executable command path to provide the maven enviorment in pipeline
---------------------------------------
# Add Maven installation
    Name = mvn
    Home_Directory =  /opt/apache-maven-3.9.6

# Pipeline syntax generator
1. Snippet Generator
2. Sample step- sh:Shell Script
   shell script- mvn clean package
3. Generate the pipeline script

# Declarative Pipeline Stage Build

```shell
        stage('Pull') {
            steps {
                echo 'Jarvis Pull the Developer repository'
                sh '/opt/apache-maven-3.9.6/bin/mvn clean package
            }
        }
```