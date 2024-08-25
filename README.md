# Project Setup and Usage

## Prerequisites

- **Java Development Kit (JDK)**: Ensure you have JDK installed and configured on your system.
- **Maven**: Make sure Apache Maven is installed. You can check this by running `mvn -version` in your terminal.

## Build and Run the Project

Follow these steps to build and run the Java project using Maven:

### Step 1: Open Command Line Interface

Open a terminal (Linux or macOS) or Command Prompt (Windows).

### Step 2: Navigate to the Project Directory

Navigate to the root directory of your Java project where the `pom.xml` file is located:

```bash
cd path/to/your/project
```

### Step 3: Build the Project Using Maven
To clean, compile, and package the project, run:
```bash
mvn clean package
```

### Step 4: Run the Application
After a successful build, the JAR file will be located in the target directory. Run the JAR file using the java command:
```bash
java -cp target/brokerage-1.1.jar org.ialibrahim.brokerage.BrokerageApplication
```

## Testing with Postman
To test the API endpoints, follow these steps using the provided Postman collection:

### Step 1: Import the Postman Collection
* Download the Postman collection file from the repository `Brokerage API.postman_collection.json`.
* Open Postman and click on the "Import" button.
* Select the downloaded `.json` file to import the collection into Postman.

### Step 2: Run the Collection
* Locate the imported collection in Postman.
* Expand the collection to view all available requests.
* Click on each request to view details and send them to your local server to test the endpoints.

### Step 3: Using Environment Variables
The collection contains the required credentials as variables at the collection level, you have to consume the `/api/login` end point first. The `Post-response` script copies the `api-token` to the variables to be used in the API successor calls. So, once the login is successfull, other endpoints can be called without any manual handling of the Bearer token.
