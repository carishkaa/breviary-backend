-include .env
export

prepare-repo:
	cp .env.template .env; \
	ln -s $(pwd)/.env backend/src/main/resources/application.properties; \
	$(MAKE) build; \
	$(MAKE) docker-build

pipeline-docker:
	docker build . -f Dockerfile.backend --target build-worker -t breviary-backend; \
	docker run --rm breviary-backend ./gradlew detekt; \
	docker-compose -f docker-compose.pipeline.yml up --abort-on-container-exit backend; \
	RESULT=$$?; \
	docker-compose -f docker-compose.pipeline.yml down; \
	exit $RESULT;

build:
	./gradlew assemble

detekt:
	./gradlew detekt

test:
	./gradlew test

check:
	./gradlew check

docker-login:
	docker login

docker-up: docker-login
	docker-compose up --build

docker-prune:
	docker-compose down
	docker volume rm breviary_breviary-db

backend-up: docker-login
	docker-compose up backend

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
	rm -f "${PWD}/book/book.pdf" && \
	open "${PWD}/book/breviary.pdf"

docker-build:
	docker build -t breviary-backend -f Dockerfile.backend .

db:
	docker-compose up -d db
