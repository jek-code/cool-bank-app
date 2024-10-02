test_demo_bank

A simple REST API for a banking application. The API allows users to perform basic banking operations such as creating accounts, making deposits, and transferring funds. The project is fully documented with Swagger and covered with tests using JaCoCo for code coverage reporting.

Features:
Create a new bank account;
Make deposits to accounts;
Transfer funds between accounts;
View account details and transactions;
View all accounts;

Technologies Used:

Java

Spring Boot

Spring Data JPA

MySQL Database

Docker

Swagger (API documentation)

JaCoCo (test coverage)


Prerequisites
Before running this application locally, ensure you have the following installed:

Docker
Docker Compose


Setup Instructions
1. Clone the Repository
   
         git clone https://github.com/your-username/test_demo_bank.git
         cd test_demo_bank

2. Build and Run the Application with Docker
   Use Docker Compose to build and run the application. The docker-compose.yml file is already set up to handle all dependencies, including the MySQL database.

         docker-compose up --build

      This command will:

      Build the necessary Docker images
      Set up the required services (e.g., MySQL database and the application)
      Expose the API on a local port (usually http://localhost:8080)

3. Access Swagger Documentation
   Once the application is running, you can explore the API using the Swagger UI at:

         http://localhost:8080/swagger-ui/
   
5. Database Configuration
   The application uses a MySQL database, which is set up automatically through Docker. The connection details can be modified in the application.yml file.

6. Running Tests and Viewing JaCoCo Report
   The application includes unit and integration tests, and code coverage is measured using JaCoCo.

To run the tests, use the following command:

      ./mvnw test
      
To generate and view the JaCoCo code coverage report:

      ./mvnw jacoco:report
      
The report will be generated in the target/site/jacoco directory. Open the index.html file in a web browser to view the coverage details.

      open target/site/jacoco/index.html

      
Endpoints

Here are some of the key API endpoints:

POST /api/v1/createUser - Create a new account

POST /api/v1/deposit - Make a deposit to an account

POST /api/v1/transfer - Transfer funds between accounts

GET  /api/v1/getById/{id} - View account details


Refer to the Swagger documentation for the complete list of endpoints and details.
