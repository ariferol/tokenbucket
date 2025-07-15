# 🪣 Token Bucket Rate Limiting Project

This project is an educational, MIT-licensed sample project that implements IP-based rate limiting using the "Token Bucket" algorithm with Spring Boot.

## 📋 Project Description

The Token Bucket algorithm is a popular rate limiting technique used to control incoming requests to APIs. This project creates separate token buckets for each IP address, limiting the rate of asynchronous incoming requests per client. It can also be used in small-scale projects.

## ✨ Features

- **Token Bucket Algorithm**: Thread-safe token bucket implementation
- **IP-based Rate Limiting**: Separate rate limit for each IP address
- **Servlet Filter**: Automatically controls all HTTP requests
- **Asynchronous Controller**: Async endpoint for long-running operations
- **Comprehensive Testing**: Unit and integration tests

## 🛠️ Technologies

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Web MVC**
- **JUnit 5**
- **Maven**
- **OpenAPI/Swagger** (springdoc-openapi)

## ⚙️ Token Bucket Parameters

- **Capacity**: 10 tokens (maximum token count)
- **Refill Rate**: 5 tokens/second
- **Refill Period**: 1000ms (1 second)

These settings mean that an IP address can make a maximum of 10 requests per second, and then continue at a rate of 5 requests per second.

## 🚀 Installation and Running

### 📋 Requirements

- Java 17 or higher
- Maven 3.6 or higher

### ▶️ Running the Project

1. Clone or download the project
2. Navigate to the project directory in Terminal/CMD
3. Run the following command:

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

The application will run at `http://localhost:8080`.

### 🧪 Testing

To run tests:

```bash
# Windows
mvnw.cmd test

# Linux/Mac
./mvnw test
```

## 🔗 API Endpoints

### `/api/async-task` (GET)
- Runs an asynchronous task
- Waits for 2 seconds and returns a result
- Subject to rate limiting rules

### 🚦 Rate Limiting Test

To test rate limiting:

```bash
# Make 10 consecutive requests - all should succeed
curl http://localhost:8080/api/async-task

# The 11th request should return 429 (Too Many Requests)
curl http://localhost:8080/api/async-task
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/tokenbucket/
│   │   ├── TokenBucketApplication.java      # Main application class
│   │   ├── core/
│   │   │   └── TokenBucket.java             # Token bucket implementation
│   │   ├── filter/
│   │   │   └── TokenBucketRateLimitFilter.java  # Rate limiting filter
│   │   └── controller/
│   │       └── AsyncController.java         # Test controller
│   └── resources/
│       └── application.properties           # Application settings
└── test/
    └── java/com/example/tokenbucket/test/
        ├── TokenBucketUnitTest.java         # Unit tests
        └── TokenBucketIntegrationTest.java  # Integration tests
```

## 🔬 Algorithm Details

### 🪣 How Does Token Bucket Work?

1. **Bucket Capacity**: Maximum token count per IP (10)
2. **Token Consumption**: Each request consumes 1 token
3. **Token Refill**: New tokens are added at regular intervals (1 second) (5 tokens)
4. **Overflow Control**: Token count cannot exceed capacity

> **Detailed Information**: For more detailed information about Token Bucket and other rate limiting algorithms: [Rate Limit Algorithms](https://yazilimgelistirmeyontemleri.blogspot.com/2021/12/hzlimit-algoritmalarrate-limit.html)

### 🔒 Thread Safety

- Token count is managed thread-safely using `AtomicLong`
- Race conditions are prevented with `compareAndSet` operations
- Refill operations are performed atomically with `synchronized` methods

## 🛠️ Development

### 🚀 TO-DO/Future Work

To extend the project:

- Different rate limits for different endpoints
- Database-based rate limiting
- Distributed rate limiting (with Redis)
- Configurable rate limit parameters

### ⚙️ Configuration

To change rate limiting parameters, edit the constants in the `TokenBucketRateLimitFilter` class:

```java
private static final long CAPACITY = 10;        // Bucket capacity
private static final long REFILL_TOKENS = 5;    // Refill token count
private static final long REFILL_PERIOD_MS = 1000; // Refill period (ms)
```

## 📚 Swagger UI

The project includes Swagger UI. You can access the API documentation at the following address when the application is running:

```
http://localhost:8080/swagger-ui.html
```

## 📜 License

This project is licensed under the MIT License. For more information, see the [LICENSE](LICENSE) file.
