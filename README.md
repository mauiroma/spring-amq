# spring-amq

Application to demostrate how to connect to artemis using spring-boot

See [application.yml](src/main/resources/application.yml)

## compile
```shell
mvn package
``` 

## run
```shell
java -jar target/spring-amq-0.0.1-SNAPSHOT.jar
``` 


## Test
### Zero Persistence
* Create AMQ Instances
```shell
<AMQ_BROKER_PATH>/bin/artemis  create  --user amq --password password --queues SpringAmqQueue --disable-persistence --allow-anonymous False AmqBroker
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
curl http://localhost:8080/send\?text=maui
curl http://localhost:8080/sendMany\?text=maui\&many=1000
``` 

* Check Consumer
```shell
./AmqBroker/bin/artemis queue stat --user admin --password password --queueName SpringAmqQueue
``` 

### Jdbc Persistence (Oracle XE) with Active/Standby Cluster
* Create Oracle XE Docker Instance   
Clone [oracle github repository](https://github.com/oracle/docker-images.git)   
```
cd docker-images/OracleDatabase/SingleInstance/dockerfiles

./buildContainerImage.sh -x -v 18.4.0

docker run --name OracleXE -p 1521:1521 -p 5500:5500 -e ORACLE_PWD=password oracle/database:18.4.0-xe
```
---
* Create Node1
```shell
<AMQ_BROKER_PATH>/bin/artemis create --user amq --password password --clustered --cluster-user cls_user --cluster-password cls_pwd --jdbc --jdbc-connection-url jdbc:oracle:thin:@localhost:1521/XE --jdbc-driver-class-name oracle.jdbc.driver.OracleDriver --staticCluster "tcp://localhost:62616" --relax-jolokia  --no-autocreate --queues SpringAmqQueue --host localhost --require-login true  --name node1 node1

curl https://repo1.maven.org/maven2/com/oracle/database/jdbc/ojdbc8/18.3.0.0/ojdbc8-18.3.0.0.jar -o node1/lib/ojdbc8-18.3.0.0.jar 
```

edit node1/etc/broker.xml adding this xml fragment just after jdbc-connection-url node 
```xml
  <jdbc-user>system</jdbc-user>
  <jdbc-password>password</jdbc-password>
```

edit node1/etc/broker.xml adding this xml fragment just after cluster-connections node 
```xml
  <ha-policy>
      <shared-store>
          <master>
              <failover-on-shutdown>true</failover-on-shutdown>
          </master>
      </shared-store>
  </ha-policy>
```

* Start instance
```shell
./node1/bin/artemis run
```
---
* Create Node2
```shell
<AMQ_BROKER_PATH>/bin/artemis create --user amq --password password --clustered --cluster-user cls_user --cluster-password cls_pwd --jdbc --jdbc-connection-url jdbc:oracle:thin:@localhost:1521/XE --jdbc-driver-class-name oracle.jdbc.driver.OracleDriver --staticCluster "tcp://localhost:61616" --relax-jolokia --port-offset 1000 --no-autocreate --queues SpringAmqQueue --host localhost --require-login true  --name node2 node2

curl https://repo1.maven.org/maven2/com/oracle/database/jdbc/ojdbc8/18.3.0.0/ojdbc8-18.3.0.0.jar -o node2/lib/ojdbc8-18.3.0.0.jar 
```
edit node2/etc/broker.xml adding this xml fragment just after jdbc-connection-url node 
```xml
  <jdbc-user>system</jdbc-user>
  <jdbc-password>password</jdbc-password>
```

edit node2/etc/broker.xml adding this xml fragment just after cluster-connections node 
```xml
<ha-policy>
    <shared-store>
        <slave>
            <failover-on-shutdown>true</failover-on-shutdown>
            <allow-failback>true</allow-failback>
            <restart-backup>true</restart-backup>
        </slave>
    </shared-store>
</ha-policy>
```

* Start instance
```shell
./node2/bin/artemis run
```