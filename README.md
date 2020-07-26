# Real-time price statistics

A restful API to monitor the incoming prices. The main use case for that API is to provide real-time price statistics
from the last 60 seconds (sliding time interval).

## Running locally

```sh
$ ./mvnw spring-boot:run
```

Interacting with the application:

```sh
$ curl -X POST localhost:8080/tick  -H "Content-Type: application/json" -d"{\"instrument\": \"IBM.N\", \"price\": 100, \"timestamp\": \"$(date +%s)000\"}"
$ curl -X POST localhost:8080/tick  -H "Content-Type: application/json" -d"{\"instrument\": \"Apple\", \"price\": 200, \"timestamp\": \"$(date +%s)000\"}"
$ curl -X POST localhost:8080/tick  -H "Content-Type: application/json" -d"{\"instrument\": \"Apple\", \"price\": 300, \"timestamp\": \"$(date +%s)000\"}"
$ curl localhost:8080/statistics
{"avg":200.0,"max":300.0,"min":100.0,"count":3}
$ curl localhost:8080/statistics/Apple
{"avg":250.0,"max":300.0,"min":200.0,"count":2}
```

## What would you improve if you had more time
1. Add automatic test for concurrent requests
2. Experiment with other ways to achieve thread safety that might be more efficient such as reentrant and readwrite locks.
3. Use Spring scheduling for refreshing task instead of starting a thread manually

## Assumptions
The challenge did not specify the response for statistics of an instrument that was not collected before, so per default 
the app returns status 200 with empty body.
