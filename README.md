# Congestion Tax Calculator

Welcome the Volvo Cars Congestion Tax Calculator assignment.

This repository contains a developer [assignment](ASSIGNMENT.md) used as a basis for candidate intervew and evaluation.

Clone this repository to get started. Due to a number of reasons, not least privacy, you will be asked to zip your
solution and mail it in, instead of submitting a pull-request.

## Solution description

The application was built using Spring Boot Framework and Java 17 as base version.

The test framework is Spock, and the externalized configuration is using a relation database. For the simplicity of the
example I used h2 database that is reset everytime during development mode.

Using spring boot with JPA it's possible to configure different environments just changing the environments variables
from the datasource.

## Requisites

Java 17+

## Run tests

```
    ./gradlew test
```

## Run application

To run the application simply run

```
    ./gradlew bootRun
```

## Integration test

It's possible to run the file `http/congestion.http` that will make requests in local environment.

### Request example

```json
{
  "city": "gothenburg",
  "vehicleType": "car",
  "passes": [
    "2013-01-14 21:00:00",
    "2013-01-15 21:00:00",
    "2013-02-07 06:23:27",
    "2013-02-07 15:27:00",
    "2013-02-08 06:27:00",
    "2013-02-08 06:20:27",
    "2013-02-08 14:35:00",
    "2013-02-08 15:29:00",
    "2013-02-08 15:47:00",
    "2013-02-08 16:01:00",
    "2013-02-08 16:48:00",
    "2013-02-08 17:49:00",
    "2013-02-08 18:29:00",
    "2013-02-08 18:35:00",
    "2013-03-26 14:25:00",
    "2013-03-28 14:07:27"
  ]
}
```

### Response example

```json
{
  "total": 89,
  "details": [
    {
      "amount": 8,
      "date": "2013-03-26"
    },
    {
      "amount": 0,
      "date": "2013-01-15"
    },
    {
      "amount": 0,
      "date": "2013-01-14"
    },
    {
      "amount": 21,
      "date": "2013-02-07"
    },
    {
      "amount": 60,
      "date": "2013-02-08"
    },
    {
      "amount": 0,
      "date": "2013-03-28"
    }
  ]
}
```