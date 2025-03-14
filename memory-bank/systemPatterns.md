# System Patterns

## Architecture
The application follows a layered architecture, with distinct layers for presentation, business logic, and data access.

## Key Components
- **Controllers:** Handle user requests and interact with the service layer.
- **Services:** Implement business logic and validation rules.
- **Models:** Represent data entities.
- **Repositories:** Provide access to the data persistence layer.
- **Aspects:** Implement cross-cutting concerns, such as logging.
- **Security:** Handle security concerns including header escaping and API key validation.
- **Interceptors:** Process requests before they reach controllers.
- **Validators:** Implement validation logic for different types of inputs.

## Design Patterns
- **Dependency Injection:** Used to manage dependencies between components.
- **Aspect-Oriented Programming:** Used to separate cross-cutting concerns from core logic.
- **Model-View-Controller (MVC):** Used to structure the application and separate concerns.
- **Chain of Responsibility:** Used in the multi-layered defense strategy for HTTP header validation.
- **Decorator Pattern:** Used in the header escaping mechanism.

## HTTP Header Security Strategy
The application implements an API key authentication strategy:

### API Key Authentication (Spring MVC Based Approach)
- Uses the `x-api-key` header to authenticate API requests
- Validates the API key against a predefined value in application.properties
- Rejects requests with missing or invalid API keys with 401 Unauthorized status
- Implemented using Spring MVC's HandlerInterceptor and ControllerAdvice
- Example:
  ```java
  @Component
  public class ApiKeyInterceptor implements HandlerInterceptor {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
          // APIパスの場合のみ検証を実行
          if (request.getRequestURI().startsWith("/api/")) {
              headerValidator.validate(request);
          }
          return true;
      }
  }

  @ControllerAdvice
  public class ApiKeyExceptionHandler {
      @ExceptionHandler(ValidationException.class)
      public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
          return new ResponseEntity<>(
              new ErrorResponse(e.getStatus().value(), e.getMessage()),
              e.getStatus()
          );
      }
  }
  ```

### Benefits of the Spring MVC Based Approach
- Leverages Spring MVC's standard features for authentication
- Centralized error handling through ControllerAdvice
- Consistent with Spring Security's authentication patterns
- Improved testability through Spring's testing support
- Follows Spring Framework's best practices

## Relationships
- Controllers depend on Services
- Services depend on Models and Repositories
- Aspects intercept calls to various components
- Security Interceptors handle authentication and request sanitization
- HandlerInterceptors process requests in a specific order:
  1. HeaderEscapeInterceptor (all requests)
  2. ApiKeyInterceptor (API endpoints only)
- ControllerAdvice provides centralized exception handling
- Validators implement reusable validation logic
