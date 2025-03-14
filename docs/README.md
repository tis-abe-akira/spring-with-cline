# Spring Boot API Key Authentication Sample

[日本語](../README.md)

## Overview
This project is a Spring Boot sample application that implements API key authentication.
It provides a practical implementation that balances security and educational value.

## Technology Stack
- Java 17 or later
- Spring Boot 3.4.3
- Maven
- Spring Web
- Spring Data JPA
- SpringDoc OpenAPI
- Spring Boot DevTools
- For other major dependencies, please refer to [pom.xml](../pom.xml)

## Prerequisites
- JDK 17 or later installed
- Maven installed
- (Recommended) IntelliJ IDEA

## Setup
1. Clone the project
2. Configure the API key in `application.properties`:
   ```properties
   api.key=your-api-key-here
   ```

## How to Start the Application
You can start the application using the following commands:

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Using system-installed Maven
mvn spring-boot:run
```

After startup, the application will be accessible at `http://localhost:8080`.

## How to Run Test Scripts

### API Testing with cURL
You can test the API using the following script:

```bash
# On Windows
scripts\curl\filter-test.sh

# On Unix/Linux
./scripts/curl/filter-test.sh
```

Note that you must specify the `x-api-key` header when making API requests:

```bash
curl -H "x-api-key: your-api-key-here" http://localhost:8080/api/items
```

### Running Unit Tests

```bash
# Using Maven wrapper
./mvnw test

# Using system-installed Maven
mvn test
```

## API Documentation
OpenAPI (Swagger) documentation is available at:
- http://localhost:8080/swagger-ui.html
- http://localhost:8080/v3/api-docs

## Error Responses
The API returns error responses in the following format:

```json
{
    "status": 401,
    "message": "Missing required header: x-api-key"
}
```

Main error responses:
- 401 Unauthorized: Authentication error (missing or invalid API key)
- 404 Not Found: Resource not found
- 500 Internal Server Error: Server-side error

## Known Issues
- API key is currently stored in plain text in `application.properties`
- A more secure API key management solution is needed for production
- Rate limiting might be needed for production use

## Project Structure
- `src/main/java/com/example/demo/controller`: API endpoint implementations
- `src/main/java/com/example/demo/service`: Business logic
- `src/main/java/com/example/demo/model`: Data models
- `src/main/java/com/example/demo/security/interceptor`: Security-related interceptors
  - API key authentication
  - Header escaping
- `src/main/java/com/example/demo/validation`: Validation logic
- `src/main/java/com/example/demo/exception`: Error handling
- `src/main/java/com/example/demo/config`: Application configuration

## License
This project is released under the MIT License.
