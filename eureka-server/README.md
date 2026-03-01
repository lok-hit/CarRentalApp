Eureka Server
The Eureka Server provides centralized service discovery for the CarRentalApp microservices ecosystem. It enables dynamic registration, load‑balanced routing, and fault‑tolerant communication between services.

Purpose
Acts as a registry for all microservices

Allows services to discover each other dynamically

Enables client‑side load balancing via Spring Cloud LoadBalancer

Supports zero‑downtime deployments and scaling

Features
Self‑Preservation Mode
Protects the registry during network partitions.

Health Monitoring
Actuator endpoints expose server health and metrics.

Integration with API Gateway
The gateway uses Eureka to resolve service instances for routing.

High Availability (optional)
Supports multi‑node Eureka clusters for production environments.

Endpoints
http://localhost:8761/ — Eureka dashboard

/eureka/apps — registry API

/actuator/health

/actuator/info

Running the Server
Maven:
Kod
mvn spring-boot:run
Docker:
Kod
docker build -t eureka-server .
docker run -p 8761:8761 eureka-server
Configuration
Located in:

Kod
src/main/resources/application.yml
Key settings:

register-with-eureka: false

fetch-registry: false

enable-self-preservation: true

Service Registration Example
Any microservice registers automatically with:

yaml
eureka:
client:
service-url:
defaultZone: http://eureka-server:8761/eureka/