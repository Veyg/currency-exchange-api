# Currency Exchange API

This project is a simple currency exchange API built with Spring Boot. It provides an interface for converting currency amounts between different currencies using real-time exchange rates.

## Features

1. **Currency Conversion**: Converts an amount from a base currency to a target currency.
2. **Caching**: Uses Caffeine cache to improve performance for frequently requested exchange rates.
3. **Rate Limiting**: Limits the number of requests a client can make to the API within a certain period.
4. **API Documentation**: Provides a Swagger UI for easy interaction with the API and visualizes the API's resources.

## Getting Started

### Prerequisites

- Java 17
- Docker

### Building the Application

1. Clone the repository to your local machine.
2. Navigate to the directory containing the project.
3. Run the following command to build the project and package it into a JAR file:
   ```sh
   mvn clean package
   ```

### Building the Docker Image

1. After building the JAR file, build the Docker image using the provided Dockerfile:
   ```sh
   docker build -t currency-exchange-api .
   ```

### Running the Docker Container

1. Run the Docker container using the built image:
   ```sh
   docker run -p 8080:8080 -p 3030 currency-exchange-api
   ```

2. Visit `http://localhost:8080/swagger-ui/index.html` in your web browser to interact with the API using the Swagger UI.

## Configuration

Configuration settings can be found and modified in the `application.properties` file:

- `api.url`: The URL of the external API for fetching exchange rates.
- `spring.datasource.url`: The URL of the database containing API key information.
- `spring.datasource.username` and `spring.datasource.password`: Database credentials.
- Rate Limiting: API rate limiting is managed through the database. You can configure rate limits for each API key in the database.

## API Endpoints

- `GET /api/currency/convert/{baseCurrency}/{targetCurrency}/{amount}`: Converts an amount from the base currency to the target currency.

## Swagger UI

Swagger UI provides a visual interface for interacting with the API. It automatically generates documentation for the API's endpoints, including the HTTP methods, parameters, and responses. You can access it by navigating to `http://localhost:8080/swagger-ui/index.html`.

## Caching

Caching is implemented using the Caffeine library. The cache is set to have an initial capacity of 100 and a maximum size of 500. Cached values expire after 1 hour of access.
Certainly, here's the updated section with the public test API key and the cURL command for testing on EC2:

```markdown
## Authentication

The Currency Converter API requires API key authentication. To access the API, you need to include your API key in the request headers.

```
Authorization: Bearer <your-api-key>
```

For testing purposes, you can use the following public test API key:

```
7a7a53803c2211eeb0030abbe126b77c
```
### Live test on EC2

To test the API on your EC2 instance, you can use a cURL command similar to the one mentioned above. Replace `ec2-16-170-219-123.eu-north-1.compute.amazonaws.com:8080` with your EC2 instance's hostname and port:

```bash
curl -X GET 'http://ec2-16-170-219-123.eu-north-1.compute.amazonaws.com:8080/api/currency/convert/USD/EUR/100?apiKey=7a7a53803c2211eeb0030abbe126b77c'
```

Remember to replace the API key in the `apiKey` parameter with the provided public test API key.
```

Make sure to replace the EC2 instance hostname in the cURL command with the actual hostname of your EC2 instance.
## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
