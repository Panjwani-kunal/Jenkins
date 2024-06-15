Pluging: Deploy to container

# CICD: Continuous Deployment & delivery
App --> version --> store in S3

1. For deployment we need infrastructure based server, for that we create a one srver.
2. Name: StudentApp-prod-server, t2.micro, 8080 Anywhere
3. Configure & ready the server for tomcat
```shell
sudo -i
sudo apt update -y
sudo apt install openjdk-11-jdk -y
sudo wget https://dlcdn.apache.org/tomcat/tomcat-8/v8.5.100/bin/apache-tomcat-8.5.100.tar.gz
sudo tar -xvzf apache-tomcat-8.5.100.tar.gz -C /opt
cd /opt/apache-tomcat-8.5.100/
./bin/catalina.sh start
sudo curl ifconfig.me.
```

# steps to deploy the java app in tomcat add plugin
Install plugin:
1. Deploy to container --> To upoad the artifact
2. Restart the jenkins (from App or manually restart "systemctl restart jenkins")

# pipeline synatx generator
1. Sample step: deploy war/ear to a container
2. WAR/EAR: **/*.war  (** --> wildcard, /*.war --> In all directories all .war file )
3. context path: /
4. conatier: tomcat 8.x
    # Need tomcat 8 credentials
    Note: basically admin can access the tomcat server from Manager App, and he can deploy the app in tomcat. If we wana give access to local user to deploy the app in tomcat, so in that we need to create and provide the credential.

    So we need to configure the user in tomcat on [conf/tomcat-users.xml]

```shell
cd /opt/apache-tomcat-8.5.100/conf
sudo vim tomcat-users.xml
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<role rolename="manager-jmx"/>
<role rolename="manager-status"/>
<role rolename="admin-status"/>
<user username="linux" password="redhat123" roles="manager-gui,manager-script,manager-jmx,manager-status,admin-gui"/>
## User configured !!
```

# now, this user publically, so we allow this user privately
```shell
cd ..
cd webapps/manager/META-INF/
sudo vim context.xml
: set nu  --> comment line number 21 to 24

<context>
127.0.0.0 --> allow only localhost
</context>

![alt text](image-2.png)

# comment the line
<!--
<value classname allow />
-->
:wq
cd /opt/apache-tomcat-8.5.100
./bin/catalina.sh stop
./bin/catalina.sh start
```

# Refresh the tomcat --> click on manager (username: linux password:redhat123)
1. we deploy the student.war. we can also able to deploy multiple application, here we are not doing mannually deploy the app in webapps, jenkins will deploy the student.war in webapps.

# So back to the pipeline syntax generator:
# pipeline synatx generator
1. Sample step: deploy war/ear to a container
2. WAR/EAR: **/*.war  (** --> wildcard, /*.war --> In all directories all .war file )
3. context path: /
4. conatier: tomcat 8.x
    # Need tomcat 8 credentials
    Note: basically admin can access the tomcat server from Manager App, and he can deploy the app in tomcat. If we wana give access to local user to deploy the app in tomcat, so in that we need to create and provide the credential.
5. credentials:
    1. Kind: Username with password
    2. scope: global
    3. Username: linux
    4. password: redhat123
    5. ID: tomcat-creds
    6. save
: Add credentials
6. URL: http://54.234.116.183:8080/
7. Advanced > Manager context path: As it is default
8. Generate pipeline script: o/p

    Add this final script in pipeline --> Run the pipeline