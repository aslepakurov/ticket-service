# ticket-service
### Ticket service using akka-http

1. Clone project https://github.com/aslepakurov/ticket-service.git
2. Put OMDB api key in `ticket-service/src/main/resources/application.conf` key `omdb.apiKey`
3. Install redis or use embedded (`redis.embedded`)
4. Install maven (check it with `mvn -v`)
5. Get into project directory `cd ticket-service`
6. Build project `mvn clean package`
7. Run project with `java -jar target/ticket-service-1.0-SNAPSHOT-allinone.jar > app.log 2>&1 &`
