# phone-checker
## _The Java API that allows to check the country of a phone number_

- Spring Boot 2.3.4
- Java 11
- Maven
- Spring Data
- Jsoup
- Testcontainers
- MySql

## Installation

Build + tests
```sh
mvn clean install
```
Run
- Create MySql database
- ```mvn spring-boot:run ```
- In order to provide database url, username and password either add ``-Dspring.boot.run.arguments`` with 
spring.datasource.url, DATASOURCE_USERNAME, DATASOURCE_PASSWORD or specify correct values at application.yml

## Use
1. Make sure to fulfill the directory using `POST /phone-check/update`,
quartz: `${app.allow-quartz} + ${WIKI_UPDATE_CRON}` or just default - `${app.wiki.allow-update-on-start}`
2. To check a phone number: `GET /phone-check` with 'phone' header.
### Exceptions
- InvalidInputPhoneFormat (input phone number can't be parsed)
- NoPrefixFound (can't resolve origin country of a phone number)
- WikiParseException (can't resolve wiki table with phone prefix)
