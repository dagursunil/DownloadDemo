# DownloadDemo
This project is implemented to download file from different protocols like HTTP,HTTPS,FTP,SFTP.

# Product ready dependency
1. Logger configuration.
2. To create a ‘fully executable’ jar with Maven use the following plugin configuration:

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <executable>true</executable>
    </configuration>
</plugin>
```

# Points to be noted

a.) A new property 'input.urls' has been introduced which is being used by application to     get list of different protocols.values need to be comma separated.

b.)	A new property 'output.location' has been introduced which should be a directory available in system.

c.) last value in path separated by '/' is being regarded as file name. For example https://google.com/doodle in this url doodle will be considered as file name.

# How to build
Prerequisite :  maven should be installed and enabled.
For executing the project directly from directory please follow steps mentioned below :

Windows :
1.) Go to project directory .
2.) Open command prompt.
3.) Type mvnw clean install
4.) type mvnw spring-boot:run

Unix : 
1. cd to project directory.
2.) ./mvnw clean install
3.) ./mvnw spring-boot:run
	
 
