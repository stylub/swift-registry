## Swift Registry
API built with Kotlin and Spring Boot, designed for fast and efficient retrieval of SWIFT code information.
### Endpoints 

* Retrieve details of a single SWIFT code whether for a headquarters or
branches. <br>
`GET: /v1/swift-codes/{swift-code}:`
Response Structure for headquarter swift code:
```json
{
"address": string,
"bankName": string,
"countryISO2": string,
"countryName": string,
"isHeadquarter": bool,
"swiftCode": string,
“branches”: [
{
"address": string,
"bankName": string,
"countryISO2": string,
"isHeadquarter": bool,
"swiftCode": string
},
{
"address": string,
"bankName": string,
"countryISO2": string,
"isHeadquarter": bool,
"swiftCode": string
}, . . .
]
}
```
Response Structure for branch swift code:
```json
{
"address": string,
"bankName": string,
"countryISO2": string,
"countryName": string,
"isHeadquarter": bool,
"swiftCode": string
}
```
* Return all SWIFT codes with details for a specific country (both
headquarters and branches).
`GET: /v1/swift-codes/country/{countryISO2code}`:
Response Structure :
```json
{
"countryISO2": string,
"countryName": string,
"swiftCodes": [
{
"address": string,
"bankName": string,
"countryISO2": string,
"isHeadquarter": bool,
"swiftCode": string
},
{
"address": string,
"bankName": string,
"countryISO2": string,
"isHeadquarter": bool,
"swiftCode": string
}, . . .
]
}
```
* Adds new SWIFT code entries to the database for a specific country.
`POST: /v1/swift-codes`:
Request Structure :
```json
{
"address": string,
"bankName": string,
"countryISO2": string,
"countryName": string,
“isHeadquarter”: bool,
"swiftCode": string,
}
```
Response Structure:
```json
{
"message": string,
}
```
* Deletes swift-code data if swiftCode matches the one in the database.
`DELETE: /v1/swift-codes/{swift-code}`
Response Structure:
```json
{
"message": string,
}
```
### Installation


To get started with the backend, you need to have Maven installed. Follow these steps:

* Clone the repository:
* Install the dependencies:
```
mvn clean install
```
* Usage
To start the database you need to have docker installed.
```
docker compose up
```
To run the application, use the following command:
```
mvn spring-boot:run
```

Then it should be available at `http://localhost:8080`.
