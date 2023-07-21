# Currency Converter API Documentation

The Currency Converter API allows you to perform currency conversion using real-time exchange rates. You can use this API to get the latest exchange rate between two currencies or convert a specific amount from one currency to another.

## Base URL

The base URL for all API endpoints is: `https://api.example.com`

## Authentication

The Currency Converter API requires API key authentication. To access the API, you need to include your API key in the request headers.

```
Authorization: Bearer <your-api-key>
```

## Endpoints

### Get Exchange Rate

Retrieves the current exchange rate between two currencies.

**Endpoint:** `/api/exchange-rate`

**Method:** `GET`

**Parameters:**

- `baseCurrency`: The base currency code (e.g., `USD`).
- `targetCurrency`: The target currency code (e.g., `EUR`).

**Example Request:**

```http
GET /api/exchange-rate?baseCurrency=USD&targetCurrency=EUR
Authorization: Bearer <your-api-key>
```

**Example Response:**

```json
{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "exchangeRate": 0.88
}
```

### Convert Currency

Converts a specific amount from one currency to another.

**Endpoint:** `/api/convert-currency`

**Method:** `POST`

**Parameters:**

- `baseCurrency`: The base currency code (e.g., `USD`).
- `targetCurrency`: The target currency code (e.g., `EUR`).
- `amount`: The amount to convert.

**Example Request:**

```http
POST /api/convert-currency
Authorization: Bearer <your-api-key>
Content-Type: application/json

{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "amount": 100
}
```

**Example Response:**

```json
{
  "baseCurrency": "USD",
  "targetCurrency": "EUR",
  "amount": 100,
  "convertedAmount": 88
}
```

## Error Handling

If an error occurs, the API will respond with an appropriate HTTP status code and an error message in the response body.

**Example Error Response:**

```json
{
  "error": "Error during the conversion request: <error_details>"
}
```

## Caching
To improve performance and reduce waiting times, we implemented caching for the currency conversions. However, please note that due to caching, the currency conversion rates may not always reflect real-time rates.

## Rate Limiting

The Currency Converter API has a rate limit of 100 requests per minute per API key. If you exceed this limit, you will receive a `429 Too Many Requests` response. Please ensure you manage your API usage accordingly.

## Conclusion

The Currency Converter API provides a simple and reliable way to perform currency conversion. Please refer to the API documentation for more details on available endpoints, request/response examples, and error handling. If you have any questions or need further assistance, don't hesitate to contact our support team.

---
**Note:** Replace `<your-api-key>` with your actual API key obtained from our service.