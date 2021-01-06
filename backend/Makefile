clean:
	../gradlew :backend:clean

detekt:
	../gradlew detekt

test: clean make-version-file
	../gradlew  :backend:test

check:
	../gradlew :backend:check

generate-swagger-json: clean make-version-file
	../gradlew :backend:cleanTest :backend:test --tests "blue.mild.breviary.backend.generators.GenerateSwaggerTest"

run-pipeline: check

make-version-file:
	../gradlew :backend:createVersionFile
