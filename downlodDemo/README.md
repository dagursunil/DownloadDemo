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
	
 
