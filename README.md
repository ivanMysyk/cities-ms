# City List Application

This is an enterprise-grade city list application that allows the user to browse through a paginated list of cities with the corresponding photos, search by name, and edit the city (both name and photo).

## Getting Started

To get started with the City List Application, follow these steps:

1. Clone the repository:

   ```
   git clone https://github.com/your-username/city-list.git
   cd city-list
   ```

2. Copy the example environment file and set the required environment variables:

   ```
   cp .env.example .env
   ```

   Edit the `.env` file and set the `POSTGREDB_USER`, `POSTGREDB_ROOT_PASSWORD`, and `POSTGREDB_DATABASE` variables as required.

3. Start the application and the database using Docker Compose:

   ```
   docker-compose up --build
   ```

   This will start the application and the PostgresSQL database. The application will be available at `http://localhost:8080`.

## Development

To build and run the application locally:

1. Install Java and Maven.

2. Set the required environment variables:

   ```
   export POSTGREDB_USER=your-postgres-user
   export POSTGREDB_ROOT_PASSWORD=your-postgres-password
   export POSTGREDB_DATABASE=your-postgres-database
   ```

3. Build the application:

   ```
   mvn clean install -DdisableIntTests=true
   ```

4. Start the application:

   ```
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

   This will start the application in development mode. The application will be available at `http://localhost:8080`.

## Contributing

Contributions to the City List Application are welcome! To contribute:

1. Fork the repository.

2. Create a new branch for your changes:

   ```
   git checkout -b my-feature-branch
   ```

3. Make your changes and commit them:

   ```
   git commit -am "Add some feature"
   ```

4. Push your changes to your fork:

   ```
   git push origin my-feature-branch
   ```

5. Create a pull request.