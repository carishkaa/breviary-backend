# Tests

Running tests requires a set up database. The default settings that are used are database location `localhost:5432` and
password `breviary-db-pass`. The defaults can be changed by setting the environment variables `DB_URL` and `DB_PASSWORD`.

Test can be run from the command line using `./gradlew test`. To rerun tests when no changes happened, you can
use `./gradlew cleanTest test`.

To run tests from IntelliJ IDEA, create a new Gradle configuration with `cleanTest test` set as tasks and database override variables in the
Environment variables section, if necessary (the EnvFile extension doesn't seem to work to load the variables from an `.env` file). For
running individual tests, the environment variables should also be set in the Gradle configuration Template.
