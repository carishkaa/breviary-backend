clean:
	../gradlew --no-daemon :backend:clean

detekt:
	../gradlew --no-daemon detekt

test: clean make-version-file
	../gradlew --no-daemon :backend:test

gradle-check:
	../gradlew :backend:check

check: check-all

check-all: clean gradle-check

generate-swagger-json: clean make-version-file
	../gradlew --no-daemon :backend:cleanTest :backend:test --tests "blue.mild.breviary.backend.generators.GenerateSwaggerTest"

run-pipeline: check-all

make-version-file:
	../gradlew --no-daemon :backend:createVersionFile
