# spring-amq

Application to demostrate how to connect to artemis useing spring-boot

See [application.yml](src/main/resources/application.yml)

## compile
```shell
mvn package
``` 

## run
```shell
java -jar target/spring-amq-0.0.1-SNAPSHOT.jar
``` 

## test
```shell
curl http://localhost:8080/send\?text\=maui
curl http://localhost:8080/sendMany\?text\=maui\&many\=1000
``` 