# Calculator API

## Description
This is a REST API for a calculator that allows performing basic mathematical operations such as addition, subtraction, multiplication, and division. The API is also integrated with Apache Kafka for message processing.

## Technologies Used
- Java 17
- Spring Boot 3.4.3
- Apache Kafka
- Docker & Docker Compose
- Maven

## **Requirements**
Before starting, make sure you have installed:

- **Java 17**: `java -version`
- **Maven**: `mvn -version`
- **Docker**: `docker -v`
- **Docker Compose**: `docker-compose -v`
- **Git**: `git --version`

## **Installation and Execution**

### **1️⃣ Clone the Repository**
```sh
git clone https://github.com/noooanyway/calculator-api.git
cd calculator-api
```
If you prefer using **SSH**:
```sh
git clone git@github.com:noooanyway/calculator-api.git
cd calculator-api
```

### **2️⃣ Start Containers with Docker**
The API is already configured to run automatically on port **8081** with Docker.
```sh
docker-compose up -d
```
Check running containers:
```sh
docker ps
```
To stop everything:
```sh
docker-compose down
```

### **3️⃣ Test the API**
The API will be running at `http://localhost:8081/api/calculator`

#### **Perform an Addition**
```sh
curl "http://localhost:8081/api/calculator/sum?a=5&b=3"
```
Expected response:
```json
{
  "requestId": "xxx-yyy-zzz",
  "operation": "sum",
  "a": 5,
  "b": 3,
  "result": 8
}
```

#### **Division by Zero**
```sh
curl "http://localhost:8081/api/calculator/division?a=10&b=0"
```
Expected response:
```json
{
  "requestId": "xxx-yyy-zzz",
  "error": "Erro na operação: Divisão por zero não permitida."
}
```

### **4️⃣ Run Tests**
To run tests inside the Docker container:
```sh
docker exec -it container-name mvn test
```

To run locally:
```sh
mvn test
```
Expected output:
```
[INFO] BUILD SUCCESS
```

## **Contribution**
If you want to contribute:
1. Fork the repository
2. Create a new branch (`git checkout -b my-feature`)
3. Commit your changes (`git commit -m 'My new feature'`)
4. Push to the repository (`git push origin my-feature`)
5. Open a Pull Request

 
