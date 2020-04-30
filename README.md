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
* Create AMQ Instances
```shell
<AMQ_BROKER_PATH>/bin/artemis  create  --user amq --password password --queues TestQueue --disable-persistence --allow-anonymous False AmqBroker
```

* Start instance
```shell
./AmqBroker/bin/artemis run
```
> if you have this WARN   
> [org.apache.activemq.artemis.core.server] AMQ222210: Storage usage is beyond max-disk-usage. System will start blocking producers.   
> modify the property <max-disk-usage> in ./AmqBroker/etc/broker.xml with value 100 

* Send Message
```shell
curl http://localhost:8080/send\?text\=maui
curl http://localhost:8080/sendMany\?text\=maui\&many\=1000
``` 

* Check Consumer
```shell
./AmqBroker/bin/artemis queue stat --user admin --password password --queueName TestQueue
``` 
