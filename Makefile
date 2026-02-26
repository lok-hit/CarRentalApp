# ============================================================
# CarRentalApp - Makefile (root)
# Production-ready build & run automation + CI/CD
# Includes:
# - Maven build
# - Docker build & push
# - TLS certificate generation
# - Nginx Ingress deployment
# - Kubernetes apply
# - Helm deployment
# - OpenTelemetry Java Agent
# ============================================================

# Versions
OTEL_AGENT_VERSION = 2.7.0
OTEL_AGENT_JAR = opentelemetry-javaagent-$(OTEL_AGENT_VERSION).jar

# Registry / namespace
REGISTRY = your-registry.example.com/carrental
NAMESPACE = carrental

# Services
SERVICES = api-gateway eureka-server monitoring-service car-service reservation-service payment-service user-profile-service notification-service

# ============================================================
# OpenTelemetry Java Agent
# ============================================================
otel-agent:
    @if [ ! -f "$(OTEL_AGENT_JAR)" ]; then \
        echo "Downloading OpenTelemetry Java Agent..."; \
        curl -L -o $(OTEL_AGENT_JAR) https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v$(OTEL_AGENT_VERSION)/opentelemetry-javaagent.jar; \
    else \
        echo "OTEL Java Agent already exists."; \
    fi

# ============================================================
# Maven build
# ============================================================
build:
    mvn -q -B clean install

clean:
    mvn -q -B clean

# ============================================================
# Docker build & push
# ============================================================
docker-build:
    @for svc in $(SERVICES); do \
        echo "Building Docker image for $$svc..."; \
        docker build -t $(REGISTRY)/$$svc:latest ./$$svc; \
    done

docker-push:
    @for svc in $(SERVICES); do \
        echo "Pushing Docker image for $$svc..."; \
        docker push $(REGISTRY)/$$svc:latest; \
    done

docker-release:
    @if [ -z "$(TAG)" ]; then \
        echo "Usage: make docker-release TAG=1.0.0"; exit 1; \
    fi
    @for svc in $(SERVICES); do \
        echo "Tagging $$svc as $(TAG)..."; \
        docker tag $(REGISTRY)/$$svc:latest $(REGISTRY)/$$svc:$(TAG); \
        docker push $(REGISTRY)/$$svc:$(TAG); \
    done

# ============================================================
# Docker-compose (local platform)
# ============================================================
up: otel-agent
    docker-compose up -d --build

down:
    docker-compose down

logs:
    docker-compose logs -f

restart:
    $(MAKE) down
    $(MAKE) up

status:
    docker-compose ps

ps: status

# ============================================================
# Run a single service locally with OTEL agent
# Usage: make run SERVICE=api-gateway
# ============================================================
run:
    @if [ -z "$(SERVICE)" ]; then \
        echo "Usage: make run SERVICE=api-gateway"; exit 1; \
    fi
    @if [ ! -f "$(OTEL_AGENT_JAR)" ]; then \
        $(MAKE) otel-agent; \
    fi
    cd $(SERVICE) && \
        java -javaagent:../$(OTEL_AGENT_JAR) \
        -Dotel.resource.attributes=service.name=$(SERVICE) \
        -Dotel.exporter.otlp.endpoint=http://localhost:4317 \
        -jar target/*.jar

# ============================================================
# TLS Certificates for Nginx Ingress
# ============================================================
tls:
    @mkdir -p tls
    openssl req -x509 -nodes -days 365 \
        -newkey rsa:2048 \
        -keyout tls/tls.key \
        -out tls/tls.crt \
        -subj "/CN=carrental.local/O=CarRentalApp"
    kubectl create secret tls carrental-tls \
        --namespace $(NAMESPACE) \
        --key tls/tls.key \
        --cert tls/tls.crt \
        --dry-run=client -o yaml | kubectl apply -f -

# ============================================================
# Nginx Ingress
# ============================================================
ingress:
    kubectl apply -f k8s/ingress-nginx.yaml

# ============================================================
# Kubernetes deployment
# ============================================================
k8s-apply:
    kubectl apply -f k8s/

k8s-delete:
    kubectl delete -f k8s/

k8s-restart:
    kubectl rollout restart deployment -n $(NAMESPACE)

# ============================================================
# Helm deployment
# ============================================================
helm-install:
    helm install carrental charts/carrental-app -n $(NAMESPACE) --create-namespace

helm-upgrade:
    helm upgrade carrental charts/carrental-app -n $(NAMESPACE)

helm-uninstall:
    helm uninstall carrental -n $(NAMESPACE)

# ============================================================
# CI/CD pipelines
# ============================================================
ci:
    $(MAKE) build
    $(MAKE) docker-build

cd:
    $(MAKE) docker-push
    $(MAKE) helm-upgrade

release:
    @if [ -z "$(TAG)" ]; then \
        echo "Usage: make release TAG=1.0.0"; exit 1; \
    fi
    $(MAKE) docker-release TAG=$(TAG)
    $(MAKE) helm-upgrade

# ============================================================
# Help
# ============================================================
help:
    @echo "Available commands:"
    @echo "  make build             - Build all Maven modules"
    @echo "  make clean             - Clean Maven build"
    @echo "  make docker-build      - Build Docker images"
    @echo "  make docker-push       - Push Docker images"
    @echo "  make docker-release TAG=x - Tag & push images"
    @echo "  make up                - Start full platform (docker-compose)"
    @echo "  make down              - Stop platform"
    @echo "  make logs              - Follow logs"
    @echo "  make run SERVICE=x     - Run single service with OTEL agent"
    @echo "  make tls               - Generate TLS certs for Nginx Ingress"
    @echo "  make ingress           - Deploy Nginx Ingress"
    @echo "  make k8s-apply         - Apply Kubernetes manifests"
    @echo "  make k8s-delete        - Delete Kubernetes manifests"
    @echo "  make helm-install      - Install Helm chart"
    @echo "  make helm-upgrade      - Upgrade Helm release"
    @echo "  make helm-uninstall    - Uninstall Helm release"
    @echo "  make ci                - CI: build + docker-build"
    @echo "  make cd                - CD: docker-push + helm-upgrade"
    @echo "  make release TAG=x     - Release images + helm-upgrade"
