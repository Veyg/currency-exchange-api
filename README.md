# Currency Exchange API

This project is a simple currency exchange API built with Spring Boot.

## Features

1. **Currency Conversion**: Converts an amount from a base currency to a target currency.
2. **Caching**: Uses Caffeine cache to improve performance for frequently requested exchange rates.
3. **Rate Limiting**: Limits the number of requests a client can make to the API within a certain period.
4. **API Documentation**: Provides a Swagger UI for easy interaction with the API and visualizes the API's resources.

## Getting Started

### Prerequisites

- Java 17
- Maven 3.8.1

### Setting up the Application

1. Clone the repository to your local machine.
2. Navigate to the directory containing the project.
3. Run `mvn clean install` to build the project.

### Running the Application

1. Run `mvn spring-boot:run` to start the application.
2. Visit `http://localhost:8080/swagger-ui/` in your web browser to interact with the API using the Swagger UI.

## Configuration

Configuration settings can be found and modified in the `application.properties` file:

- `api.url`: The URL of the external API for fetching exchange rates.
- `secret.file`: The path to the properties file containing the API key for the external API.
- `bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity`: The maximum number of requests a client can make within the specified time frame.
- `bucket4j.filters[0].rate-limits[0].bandwidths[0].time`: The time frame for rate limiting, in minutes.

## API Endpoints

- `GET /api/currency/convert/{baseCurrency}/{targetCurrency}/{amount}`: Converts an amount from the base currency to the target currency.

## Swagger UI

Swagger UI provides a visual interface for interacting with the API. It automatically generates documentation for the API's endpoints, including the HTTP methods, parameters, and responses. You can access it by navigating to `http://localhost:8080/swagger-ui/`.

## Caching

Caching is implemented using the Caffeine library. The cache is set to have an initial capacity of 100 and a maximum size of 500. Cached values expire after 1 hour of access.

## Rate Limiting

Rate limiting is implemented using the Bucket4j library. By default, a client can make 1 request every 5 minutes. These settings can be modified in the `application.properties` file.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

