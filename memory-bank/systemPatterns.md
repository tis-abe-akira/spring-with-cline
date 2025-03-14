# System Patterns

## Architecture
The application follows a layered architecture, with distinct layers for presentation, business logic, and data access.

## Key Components
- **Controllers:** Handle user requests and interact with the service layer.
- **Services:** Implement business logic and validation rules.
- **Models:** Represent data entities.
- **Repositories:** Provide access to the data persistence layer.
- **Aspects:** Implement cross-cutting concerns, such as logging.
- **Filters:** Validate and sanitize HTTP request headers.
- **Validators:** Implement validation logic for different types of inputs.

## Design Patterns
- **Dependency Injection:** Used to manage dependencies between components.
- **Aspect-Oriented Programming:** Used to separate cross-cutting concerns from core logic.
- **Model-View-Controller (MVC):** Used to structure the application and separate concerns.
- **Chain of Responsibility:** Used in the multi-layered defense strategy for HTTP header validation.
- **Decorator Pattern:** Used in the header escaping mechanism.

## HTTP Header Security Strategy
The application implements an API key authentication strategy:

### API Key Authentication (Whitelist Approach)
- Uses the `x-api-key` header to authenticate API requests
- Validates the API key against a predefined value in application.properties
- Rejects requests with missing or invalid API keys
- Example:
  ```java
  // APIキーの検証
  String apiKey = request.getHeader("x-api-key");
  if (apiKey == null || apiKey.isEmpty()) {
      throw new ValidationException("Missing required header: x-api-key");
  }
  
  if (!expectedApiKey.equals(apiKey)) {
      throw new ValidationException("Invalid API key");
  }
  ```

### Benefits of the API Key Approach
- Simple and effective authentication mechanism
- Easy to implement and maintain
- Provides basic access control for API endpoints
- Appropriate for educational and demonstration purposes

## Relationships
- Controllers depend on Services.
- Services depend on Models and Repositories.
- Aspects intercept calls to various components.
- Filters intercept incoming HTTP requests before they reach controllers.
- Validators are used by both Controllers and Filters.
