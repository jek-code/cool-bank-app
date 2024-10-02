test_demo_bank
A simple REST API for a banking application. The API allows users to perform basic banking operations such as creating accounts, making deposits, and transferring funds. The project is fully documented with Swagger for ease of API exploration and testing.

Features
Create a new bank account
Make deposits to accounts
Transfer funds between accounts
View account details and transactions
Technologies Used
Java Spring Boot
Spring Data JPA
H2 Database (for local development)
Docker
Swagger (API documentation)
Prerequisites
Before running this application locally, ensure you have the following installed:

Docker
Docker Compose
Setup Instructions
1. Clone the Repository
   bash
   Copy code
   git clone https://github.com/your-username/test_demo_bank.git
   cd test_demo_bank
2. Build and Run the Application with Docker
   Use Docker Compose to build and run the application. The docker-compose.yml file is already set up to handle all dependencies.

bash
Copy code
docker-compose up --build
This command will:

Build the necessary Docker images
Set up the required services (e.g., database and application)
Expose the API on a local port (usually http://localhost:8080)
3. Access Swagger Documentation
   Once the application is running, you can explore the API using the Swagger UI at:

bash
Copy code
http://localhost:8080/swagger-ui/
Endpoints
Here are some of the key API endpoints:

POST /accounts - Create a new account
POST /accounts/{id}/deposit - Make a deposit to an account
POST /accounts/transfer - Transfer funds between accounts
GET /accounts/{id} - View account details
Refer to the Swagger documentation for the complete list of endpoints and details.

Running Tests
To run the unit tests for the application, use the following command:

bash
Copy code
./mvnw test
License
This project is licensed under the MIT License - see the LICENSE file for details.
