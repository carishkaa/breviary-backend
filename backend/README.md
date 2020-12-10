# Backend

## Overview

Backend service is written in Kotlin and uses Spring Boot 2 as a framework.

## Project Structure

Project structure follows common patterns for such application type, i.e., IoC, ORM, etc.

## Gradle

Application uses Gradle as a build tool. All tasks such as `buidl`, `test`, `buildDistZip`, etc., are managed via Gradle.

## Configuration

The main configuration is defined in `resources/application.yml`. This configuration uses environment properties for particular configuration.
For this purpose, the exists `.env` file as mentioned in [Development](../book/development/README.md).

## DB Migration

As a DB migration tool is used `Flyway` library. Migrations are stored in `resources/application.yml`. Name of the migration must follow name pattern `V[MIGRATION_NUMBER]__[MIGRATION_DESCRITPION_UNDESCORED].sql`.
Migration is executed during each application start. So in case of invalid migration, application can fail to start.

## Tests

As a test library is used `Kotlin tests`. Name of the test should describe what it is testing as a sentence, e.g., `Should test that data is deleted properly`.


## Swagger REST API Specification Generation

Application generates *Swagger REST API Specification* to be able to implement (generate) client app with predefined types and route specification. 
This is done by special unit test `generateSwagger`. There is also make task `generate-swagger-json-to-client` that generates a JSON file with specification 
and copy in into `/client` folder.


TODO...
