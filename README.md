# BE

## Setup Instructions

### Prerequisites

- Java 17
- Maven 3.9.0
- Docker

### Steps

1. **Install Java 17**

   Make sure you have Java 17 installed. You can download it from
   the [official website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).

2. **Install Maven 3.9.0**

   Download and install Maven 3.9.0 from the [official website](https://maven.apache.org/download.cgi).

3. **Setup PostgreSQL Database**

   Use Docker to run a PostgreSQL database with the following command:

   ```sh
   docker run -d --name postgres -e POSTGRES_DB=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_USER=postgres -p 5432:5432 postgres:latest
4. Open IntelliJ IDEA.
    1. Click on `File` > `Open` and select the project directory.
    2. Wait for IntelliJ to import the project and download the necessary dependencies.
    3. Ensure that the correct JDK is selected: `File` > `Project Structure` > `Project` and set `Project SDK` to Java
       17 .
   4. To run the application, navigate to the `src/main/java` directory, right-click on the main class (usually the one
       annotated with `@SpringBootApplication`), and select `Run`.