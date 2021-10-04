# Qardio - Sample

Basic Temperature Sensor API Sample

## Standards
- Language: `Java`
- Package Manager: `Maven`
- Static Code Analysis: `Yes` *IntelliJ Code Inspections*
- DDD - Document Driven: `Yes`
- DDD - Domain Driven: `Yes`
- EDD - Exception Driven: `Yes`
- TDD - Test Driven: `Yes`
- LDD - Log Driven: `30%` *Partially*
- 12FA - 12 Factor-App: `50%` *Partially*

## Commands
- `git clone https://github.com/mustafayelmer/qardio-demo.git` *// clone git into local*
- `cd qardio-demo` *// go to project folder*
- `chmod +x mvnw` *// change mode for folder to avoid credential errors*
- `./mvnw test` *// run test*
- `./mvnw package` *// build project*
- `java -jar target/*.jar` *// run project*


## Postman
> At environment panel, you should set {{host}} and {{apiKey}}
> 
- `host` : `http://localhost:8080/`
- `apiKey` : run devices/list and select api-key of one of devices
- [Postman Collection Export](./qardio.postman_collection.json)
- [Postman Environment Export](./qardio.postman_environment.json)

## OpenAPI
> All endpoints, dto(models) and entities are documented
> Authentication (ApiKey style) was implemented
> 
- [OpenAPI Interface / Swagger UI](http://localhost:8080/swagger-ui.html)
- `apiKey` : run devices/list and select api-key of one of devices, and authenticate to server this apiKey

## Samples

### Device

#### Mock
- Endpoint: `GET` `{{host}}v1/devices/mock`
- Variables: *none*
- Curl: `curl -X GET "http://localhost:8080/v1/devices/mock" -H "accept: */*"`

#### List
- Endpoint: `GET` `{{host}}v1/devices`
- Variables: *none*
- Curl: `curl -X GET "http://localhost:8080/v1/devices" -H "accept: */*"`

#### Add
- Endpoint: `POST` `{{host}}v1/devices`
- Variables: *none*
- Curl: `curl -X POST "http://localhost:8080/v1/devices" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"apiKey\": \"faecd20d-04c9-46fa-a7c6-cb758ad2faf9\", \"enabled\": true, \"mac\": \"39-A5-66-3B-B6-39\", \"name\": \"Device #444\"}"`

#### Edit
- Endpoint: `PUT` `{{host}}v1/devices/{id}`
- Variables: `path` > `id`
- Curl: `curl -X PUT "http://localhost:8080/v1/devices/9a57a616-537f-4372-b2c7-ab0a75ae2c80" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"name\": \"Device #666\", \"apiKey\": \"faecd203-04c0-46fa-a7c6-cb758ad2faf9\", \"enabled\": true}"`

#### Reset Api Key
- Endpoint: `PATCH` `{{host}}v1/devices/{id}`
- Variables: `path` > `id`
- Curl: `curl -X PATCH "http://localhost:8080/v1/devices/78e8cde2-5315-4bde-a3af-c42bbe2b5b78" -H "accept: */*"`

#### Get by Id
- Endpoint: `GET` `{{host}}v1/devices/{id}`
- Variables: `path` > `id`
- Curl: `curl -X GET "http://localhost:8080/v1/devices/78e8cde2-5315-4bde-a3af-c42bbe2b5b78" -H "accept: */*"`

#### Get by Api Key
- Endpoint: `GET` `{{host}}v1/devices/{apiKey}/api-key`
- Variables: `path` > `apiKey`
- Curl: `curl -X GET "http://localhost:8080/v1/devices/561ba1cf-711c-4d69-9c55-35a0f649fe66/api-key" -H "accept: */*"`

#### Get by Mac Address
- Endpoint: `GET` `{{host}}v1/devices/{mac}/mac-address`
- Variables: `path` > `mac`
- Curl: `curl -X GET "http://localhost:8080/v1/devices/39-A5-66-4B-B6-D9/mac-address" -H "accept: */*"`


### Temperature

#### List
- Endpoint: `GET` `{{host}}v1/temperatures`
- Variables: `header` > `X-Api-Key`
- Curl: `curl -X GET "http://localhost:8080/v1/temperatures" -H "accept: */*" -H "X-Api-Key: 561ba1cf-711c-4d69-9c55-35a0f649fe66"`

#### Save with time
- Endpoint: `POST` `{{host}}v1/temperatures`
- Variables: `header` > `X-Api-Key`
- Curl: `curl -X POST "http://localhost:8080/v1/temperatures" -H "accept: */*" -H "X-Api-Key: 561ba1cf-711c-4d69-9c55-35a0f649fe66" -H "Content-Type: application/json" -d "{ \"degree\": 34, \"occurredAt\": \"2021-10-04T04:10:52.396Z\"}"`

#### Save without time / now
- Endpoint: `POST` `{{host}}v1/temperatures`
- Variables: `header` > `X-Api-Key`
- Curl: `curl -X POST "http://localhost:8080/v1/temperatures" -H "accept: */*" -H "X-Api-Key: 561ba1cf-711c-4d69-9c55-35a0f649fe66" -H "Content-Type: application/json" -d "{ \"degree\": 23}"`

#### Save Bulk
- Endpoint: `POST` `{{host}}v1/temperatures/bulk`
- Variables: `header` > `X-Api-Key`
- Curl: `curl -X POST "http://localhost:8080/v1/temperatures/bulk" -H "accept: */*" -H "X-Api-Key: 561ba1cf-711c-4d69-9c55-35a0f649fe66" -H "Content-Type: application/json" -d "[ { \"degree\": 14, \"occurredAt\": \"2021-10-04T05:43:06.673778\" }, { \"degree\": 18, \"occurredAt\": \"2021-10-04T05:53:06.673778\" }, { \"degree\": 11, \"occurredAt\": \"2021-10-04T06:03:06.673778\" }, { \"degree\": 23, \"occurredAt\": \"2021-10-04T06:13:06.673778\" }]"`

#### Aggregate Daily
- Endpoint: `GET` `{{host}}v1/temperatures/daily`
- Variables: `header` > `X-Api-Key`
- Curl: `curl -X GET "http://localhost:8080/v1/temperatures/daily" -H "accept: */*" -H "X-Api-Key: 561ba1cf-711c-4d69-9c55-35a0f649fe66"`

#### Aggregate Hourly
- Endpoint: `GET` `{{host}}v1/temperatures/hourly`
- Variables: `header` > `X-Api-Key`
- Curl: `curl -X GET "http://localhost:8080/v1/temperatures/hourly" -H "accept: */*" -H "X-Api-Key: 561ba1cf-711c-4d69-9c55-35a0f649fe66"`


---
### Prepared by
- Mustafa Yelmer
- mustafayelmer(at)gmail.com
- `2021-10-04`