-include .env
export

pipeline-local:
	docker build . -f ./backend/Dockerfile --target build-worker -t breviary-api
	docker run --rm breviary-api ./gradlew detekt
	docker-compose -f docker-compose.pipeline.yml -f docker-compose.pipeline.local.yml up --abort-on-container-exit breviary-api
	docker-compose -f docker-compose.pipeline.yml -f docker-compose.pipeline.local.yml down

detekt:
	./gradlew detekt

docker-login:
	docker login

docker-up: docker-login
	docker-compose up --build

docker-prune:
	docker-compose down
	docker volume rm breviary_breviary-db

api-up: docker-login
	docker-compose up api

docker-start-local-db:
	 docker-compose -f docker-compose.yml up -d db

docker-stop-local-db:
	 docker-compose -f docker-compose.yml down db

regenerate-types:
	./gradlew --no-daemon :backend:cleanTest :backend:test --tests "blue.mild.breviary.backend.generators.GenerateSwaggerTest"

regenerate-localization-messages:
	./gradlew --no-daemon :backend:cleanTest :backend:test --tests "blue.mild.breviary.backend.generators.GenerateLocalizationTest"

# Gitbook helper scripts.
# Initialize Gitbook.
book-init:
	docker run -t -i --rm -v "${PWD}":/gitbook -w /gitbook/book billryan/gitbook:latest gitbook install

# Kill Gitbook
book-kill:
	docker kill breviary-gitbook

# Run Gitbook as a website.
book-serve: book-init
	docker run -t -i --rm --name breviary-gitbook -v "${PWD}":/gitbook -p 4000:4000 -p 35729:35729 -w /gitbook/book \
		billryan/gitbook:latest gitbook serve

# Generates Gitbook PDF version.
book-pdf: book-init
	docker run -t -i --rm -v "${PWD}":/gitbook -w /gitbook/book billryan/gitbook:latest gitbook pdf && \
	cp "${PWD}/book/book.pdf" "${PWD}/book/breviary.pdf" && \
	rm "${PWD}/book/book.pdf" && \
	open "${PWD}/book/breviary.pdf"
