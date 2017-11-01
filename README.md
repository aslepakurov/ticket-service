# ticket-service
##Ticket service using akka-http

### How to run

1. Clone project https://github.com/aslepakurov/ticket-service.git
2. Put OMDB api key in `ticket-service/src/main/resources/application.conf` key `omdb.apiKey`
3. Install redis or use embedded (`redis.embedded`)
4. Install maven (check it with `mvn -v`)
5. Get into project directory `cd ticket-service`
6. Build project `mvn clean package`
7. Run project with `java -jar target/ticket-service-1.0-SNAPSHOT-allinone.jar > app.log 2>&1 &`


### How to test

1. _Test health_  (*GET* /health)           - returns OK 
2. _Create movie_ (*POST* /movie/register)  - create movie screening

```
    {
        "imdbId": "tt0111161",      //valid IMDB ID: String
        "availableSeats": 100,      //available sittings: String
        "screenId": "screen_123456" //screening ID: String
    }    
```

3. _Get movie info_ (*POST* /movie/info)     - get movie information

```
    {
        "imdbId": "tt0111161",      //valid IMDB ID: String
        "screenId": "screen_123456" //screening ID: String
    }    
```

4. _Remove movie screening_ (*POST* /movie/remove)     - remove movie screening

```
    {
        "imdbId": "tt0111161",      //valid IMDB ID: String
        "screenId": "screen_123456" //screening ID: String
    }    
```

5. _Buy ticket_ (*POST* /ticket/buy)     - reserve seat at screening

```
    {
        "imdbId": "tt0111161",      //valid IMDB ID: String
        "screenId": "screen_123456" //screening ID: String
    }    
```

All REST services are fail-safe
