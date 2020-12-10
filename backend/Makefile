clean:
	../gradlew --no-daemon :backend:clean

detekt:
	./gradlew --no-daemon detekt

test: clean
	../gradlew --no-daemon :backend:test

check-all:
	../gradlew --no-daemon :backend:clean && ../gradlew :backend:test && ../gradlew :backend:check

generate-swagger-json-to-client: clean
	../gradlew --no-daemon :backend:cleanTest :backend:test --tests "blue.mild.breviary.backend.generators.GenerateSwaggerTest"

run-pipeline: check-all

make-version-file:
	../gradlew --no-daemon :backend:createVersionFile