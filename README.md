# Fever Events Service

## Overview

...
## Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or later
- **Maven 3.6** or later
- **PostgreSQL 13** or later
- **Docker**

---

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/your-organization/fever-events-service.git
cd fever-events-service
```

### 2. Configure application properties

Copy the `application.properties.example` file to `application.properties` and update the database connection details:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Edit `src/main/resources/application.properties` and update the following properties:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fever_events
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build the project

```bash
mvn clean install
```

---

## Running the Application

### Run locally

```bash
mvn spring-boot:run
```

The application will start and listen on [`http://localhost:8080`](http://localhost:8080).

### Run with Docker

1. Build the Docker image:

   ```bash
   docker build -t fever-events-service .
   ```

2. Run the container:

   ```bash
   docker run -p 8080:8080 fever-events-service
   ```

---

## Running Tests

Execute the following command to run the tests:

```bash
mvn test
```

---

### Database Migrations

This project uses **Flyway** for database migrations. Migrations are automatically applied when the application starts. If you need to manually trigger a migration, use:

```bash
mvn flyway:migrate
```

### Monitoring

The application exposes health and metrics endpoints:

- **Health:** [`http://localhost:8080/actuator/health`](http://localhost:8080/actuator/health)
- **Metrics:** [`http://localhost:8080/actuator/metrics`](http://localhost:8080/actuator/metrics)

---

## Troubleshooting

If you encounter any issues with Flyway migrations, you can clean the database and start fresh:

```bash
mvn flyway:clean
```

Then, run the application again to apply all migrations.

---

## API Documentation

The API documentation is available via **Swagger UI**. Once the application is running, you can access it at:

[`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

This provides an interactive interface to explore and test the API endpoints.

