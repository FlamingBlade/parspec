# Order Service

The **Order Service** is a backend system designed to manage and process orders in an e-commerce platform. It provides RESTful APIs for order management, asynchronous order processing, and metrics reporting.

---

## Features

1. **Order Management**:
   - Create new orders with fields: `userId`, `itemIds`, and `totalAmount`.
   - Retrieve order details by `orderId`.
   - Fetch all orders.

2. **Asynchronous Order Processing**:
   - Orders are processed asynchronously using an in-memory queue.
   - Order status transitions: `PENDING` → `PROCESSING` → `COMPLETED`.

3. **Metrics Reporting**:
   - Total number of orders processed.
   - Average processing time for orders.
   - Count of orders in each status: `PENDING`, `PROCESSING`, `COMPLETED`.

---

## Technologies Used

- **Backend**: Spring Boot
- **Database**: MySQL
- **Queue**: In-memory queue (`ConcurrentLinkedQueue`)
- **Concurrency**: Thread pool for asynchronous processing
- **API Documentation**: Included in this README

---

## Setup Instructions

### Prerequisites

1. **Java Development Kit (JDK)**:
   - Ensure JDK 17 or later is installed.
   - Verify installation:
     ```bash
     java -version
     ```

2. **MySQL**:
   - Install MySQL and create a database named `ecommerce`.
   - Update the `application.properties` file with your MySQL credentials.

3. **Maven**:
   - Ensure Maven is installed for building the project.
   - Verify installation:
     ```bash
     mvn -v
     ```

---

### Steps to Run the Application

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/FlamingBlade/parspec.git
   cd order
## Configure Database

1. **Create MySQL Database**:
   - Log in to MySQL:
     ```bash
     mysql -u root -p
     ```
   - Create the `ecommerce` database:
     ```sql
     CREATE DATABASE ecommerce;
     ```

2. **Update `application.properties`**:
   - Open the `application.properties` file in your Spring Boot project.
   - Update the username and password here with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
     spring.datasource.username=yourusername
     spring.datasource.password=yourpassword
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Verify Database Connection**:
   - Run the Spring Boot application.
   - Check the logs to ensure the application connects to the database successfully.

---

## Database Schema

The `orders` table is defined as follows:
You dont need to create this table. It will be auto created by Spring Boot

```sql
CREATE TABLE `orders` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `item_ids` json DEFAULT NULL,
  `processed_at` datetime(6) DEFAULT NULL,
  `status` enum('COMPLETED','PENDING','PROCESSING') DEFAULT NULL,
  `total_amount` decimal(38,2) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5029 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```
## Build the Project

1. **Navigate to the Project Directory**:
   - Open a terminal and navigate to the root directory of the project:
     ```bash
     cd order
     ```

2. **Build the Project Using Maven**:
   - Run the following command to clean and build the project:
     ```bash
     mvn clean install
     ```
   - This will compile the code, run tests, and package the application into a `.jar` file.

3. **Verify the Build**:
   - After the build completes, check the `target` directory for the generated `.jar` file:
     ```bash
     ls target/
     ```
   - You should see a file named `order-service-1.0.0.jar` (or similar).

---

## Run the Application

1. **Run the Application Using Maven**:
   - Use the following command to start the Spring Boot application:
     ```bash
     mvn spring-boot:run
     ```

2. **Run the Application Using the `.jar` File**:
   - Alternatively, you can run the application directly from the `.jar` file:
     ```bash
     java -jar target/order-queue.jar
     ```

3. **Verify the Application**:
   - Once the application starts, you should see logs indicating that the server is running:
     ```
     Started OrderServiceApplication in 2.5 seconds
     ```
   - The application will be accessible at `http://localhost:8080`.

---

## Access the APIs

- Use tools like **Postman**, **cURL**, or your browser to interact with the APIs.
- Example API endpoints:
  - Create an order: `POST http://localhost:8080/orders`
  - Get order details: `GET http://localhost:8080/orders/{orderId}`
  - Fetch metrics: `GET http://localhost:8080/metrics`

---

## Example API Requests

### Create an Order
```bash
curl --location 'http://localhost:8080/orders' \
--header 'Content-Type: application/json' \
--data '{
    "userId": "1",
    "itemIds": [1, 2, 3],
    "totalAmount": 100.00
}
```
The response will be of the form
```json
{
    "orderId": 28,
    "userId": 1,
    "itemIds": [
        1,
        2,
        3
    ],
    "totalAmount": 100.00,
    "status": "PENDING",
    "createdAt": "2025-02-21T16:20:51.9331695",
    "processedAt": null
}

```
### Get an Order
```bash
curl --location 'http://localhost:8080/orders/26'
```
The response will be of the form
```json
{
    "orderId": 26,
    "userId": 1,
    "itemIds": [
        1,
        2,
        3
    ],
    "totalAmount": 100.00,
    "status": "COMPLETED",
    "createdAt": "2025-02-21T16:20:23.152625",
    "processedAt": "2025-02-21T16:20:25.25447"
}

```
### Get All Orders
```bash
curl --location 'http://localhost:8080/orders'
```
The response will be of the form
```json
[
    {
        "orderId": 4029,
        "userId": 12,
        "itemIds": [
            1,
            2,
            3
        ],
        "totalAmount": 100.00,
        "status": "COMPLETED",
        "createdAt": "2025-02-21T19:23:14.323166",
        "processedAt": "2025-02-21T19:23:20.237998"
    },
    {
        "orderId": 4030,
        "userId": 109,
        "itemIds": [
            1,
            2,
            3
        ],
        "totalAmount": 100.00,
        "status": "COMPLETED",
        "createdAt": "2025-02-21T19:23:14.322166",
        "processedAt": "2025-02-21T19:23:20.119278"
    }
]

```
### Show Metrics
```bash
curl --location 'http://localhost:8080/orders/metrics'
```
The response will be of the form. Time is in seconds
```json
{
    "totalOrders": 1000,
    "orderStatusCounts": {
        "PROCESSING": 200,
        "COMPLETED": 300,
        "PENDING": 500
    },
    "averageProcessingTime": 9.969
}

```

## Design Decisions and trade-offs

1. Used ConcurrentLinkedQueue for simplicity and scalability within a single instance.
2. It is a good in-memory queue in java.
3. I am running a 6 core- 12 thread machine so the thread pool is configured according for maximum performance
4. Horizontal and Vertical Scaling is possible in this regard.
5. The actual content of items and orders is of little significance for this specific functionality so I have taken random values. Actual tables can be easily attached.
6. New orders are consistently added to the order queue and once every 50 ms order processor is called which asyncronously assigns threads to process the current orders.
7. What I am doing is essentially batch processing orders asyncronously. 
8. 50 ms seems to be the bottleneck for my system for maximum performance. In a scalable system this can be further adjusted accordingly
9. A python script for the simulation test.py is present in order/src/main/java/com/project/order/loadtest. It concurrently tries to hit 1000 requests at the same time.


