# Currency Converter API

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Description

The Currency Converter API is a RESTful API that allows users to convert currencies based on the latest exchange rates. It utilizes the Open Exchange Rates API to fetch the exchange rate data.

## Features

- Convert currencies from one to another based on the latest exchange rates.
- Supports a wide range of currencies.
- Easy-to-use API endpoints.
- Secure and reliable.

## Installation

1. Clone the repository:

```
git clone https://github.com/your-username/currency-converter-api.git
```

2. Navigate to the project directory:

```
cd currency-converter-api
```

3. Build the project:

```
./mvnw clean package
```

4. Run the application:

```
java -jar target/currency-converter-api.jar
```

## Usage

The Currency Converter API provides the following endpoints:

- `/api/currency/convert/{baseCurrency}/{targetCurrency}/{amount}` - Converts the specified amount from the base currency to the target currency.

Make sure to replace `{baseCurrency}`, `{targetCurrency}`, and `{amount}` with the appropriate values.

## Configuration

The API requires an API key to access the Open Exchange Rates API. Please obtain an API key from the Open Exchange Rates website and update the `application.properties` file with your API key:

```
api.key=your-api-key
```

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvement, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).