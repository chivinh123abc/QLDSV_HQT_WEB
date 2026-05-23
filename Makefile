.PHONY: dev build clean format lint help

help:
	@echo "Available commands:"
	@echo "  make dev    - Setup Git hook and start Tomcat server (mvn clean package cargo:run)"
	@echo "  make build  - Compile and build the WAR package (mvn clean package)"
	@echo "  make clean  - Clean build target directory (mvn clean)"
	@echo "  make format - Auto-format code using Spotless (mvn spotless:apply)"
	@echo "  make lint   - Check code formatting using Spotless (mvn spotless:check)"

dev:
	git config core.hooksPath .githooks
	mvn clean package cargo:run

build:
	mvn clean package

clean:
	mvn clean

format:
	mvn spotless:apply

lint:
	mvn spotless:check
