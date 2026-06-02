.PHONY: dev build clean format lint help

help:
	@echo "Available commands:"
	@echo "  make dev    - Setup Git hooks, start Watcher (Auto-reload) & Tomcat (cargo:run)"
	@echo "  make build  - Compile and build the WAR package (mvn clean package)"
	@echo "  make clean  - Clean build target directory (mvn clean)"
	@echo "  make format - Auto-format code using Spotless (mvn spotless:apply)"
	@echo "  make lint   - Check code formatting using Spotless (mvn spotless:check)"

dev:
	git config core.hooksPath .githooks
	@echo "Starting Auto-Reload system (Server + Watcher)..."
	cmd /c start "Maven File Watcher" cmd /c "mvn fizzed-watcher:run"
	mvn clean spotless:apply package cargo:run

build:
	mvn clean package

clean:
	mvn clean

format:
	mvn spotless:apply

lint:
	mvn spotless:check
