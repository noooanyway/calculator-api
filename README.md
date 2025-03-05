# **Calculator API**

## **Description**
This is a REST API for a calculator that allows performing basic mathematical operations such as:
- Addition
- Subtraction
- Multiplication
- Division

The API is integrated with **Apache Kafka** for message processing and runs inside a **Dockerized** environment.

---

## **Technologies Used**
- **Java 17**
- **Spring Boot 3.4.3**
- **Apache Kafka**
- **Docker & Docker Compose**
- **Maven**

---

## **Requirements**
Before starting, make sure you have installed:

- **Java 17**: `java -version`
- **Maven**: `mvn -version`
- **Docker**: `docker -v`
- **Docker Compose**: `docker-compose -v`
- **Git**: `git --version`

---

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

---

### **2️⃣ Build and Package the Application (If Needed)**
To ensure a fresh build before running:
```sh
mvn clean package
```
If you encounter any issues, try forcing an update:
```sh
mvn clean package -U
```

---

### **3️⃣ Start the Application with Docker**
Run the following command to start the required services (**Spring Boot App, Kafka, Zookeeper**) inside containers:
```sh
docker-compose up -d --build
```
**Check running containers:**
```sh
docker ps
```
To **stop everything**:
```sh
docker-compose down
```

---

### **4️⃣ Test the API**
The API will be running at **`http://localhost:8081/api/calculator`**.

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

#### **Perform a Subtraction**
```sh
curl "http://localhost:8081/api/calculator/subtraction?a=10&b=4"
```
Expected response:
```json
{
  "requestId": "xxx-yyy-zzz",
  "operation": "subtraction",
  "a": 10,
  "b": 4,
  "result": 6
}
```

#### **Perform a Multiplication**
```sh
curl "http://localhost:8081/api/calculator/multiplication?a=7&b=6"
```
Expected response:
```json
{
  "requestId": "xxx-yyy-zzz",
  "operation": "multiplication",
  "a": 7,
  "b": 6,
  "result": 42
}
```

#### **Perform a Division**
```sh
curl "http://localhost:8081/api/calculator/division?a=10&b=2"
```
Expected response:
```json
{
  "requestId": "xxx-yyy-zzz",
  "operation": "division",
  "a": 10,
  "b": 2,
  "result": 5
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

---

### **5️⃣ Run Tests**
To run tests **inside the Docker container**:
```sh
docker exec -it container-name mvn test
```

To run tests **locally**:
```sh
mvn test
```
Expected output:
```
[INFO] BUILD SUCCESS
```

---

## **Troubleshooting**
### **Docker Issues**
- If **Docker containers are not starting properly**, force a rebuild:
  ```sh
  docker-compose up --build --force-recreate
  ```

- If **Kafka is not working**, restart the services:
  ```sh
  docker-compose down && docker-compose up -d
  ```

- To check **logs of a specific container**:
  ```sh
  docker logs -f container-name
  ```

### **Maven Issues**
- If `mvn package` fails due to dependencies:
  ```sh
  mvn clean install -U
  ```
- If you need to force a **dependency update**:
  ```sh
  mvn dependency:purge-local-repository
  ```

---
 

## **Contribution**
If you want to contribute:
1. **Fork** the repository
2. **Create a new branch** (`git checkout -b my-feature`)
3. **Make your changes** and **commit** (`git commit -m 'My new feature'`)
4. **Push the changes** (`git push origin my-feature`)
5. **Open a Pull Request**

---
