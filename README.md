

# FloorPlannerSharing - User Authentication Module

This project is a Java-based application that provides user login functionality. It connects to a database and integrates with Dropbox using an access token.

## 📁 Project Structure

- `src/main/java/com/floorplannersharing/UserLogin.java`: Main class to run the application.
- `config.properties`: Configuration file for database and Dropbox credentials.
- `user_auth.sql`: SQL script to create the user authentication schema.
- `files.sql`: SQL script to create file tracking or sharing schema.

---

## ⚙️ Configuration

Before running the project, make sure to configure your credentials and database connection settings in `config.properties`:

```properties
dropbox.access.token="YOUR_DROPBOX_ACCESS_TOKEN"
DB_URL="jdbc:mysql://localhost:3306/your_database_name"
DB_USER="your_db_username"
DB_PASSWORD="your_db_password"

## Running the Application

```
mvn exec:java -Dexec.mainClass="com.floorplannersharing.UserLogin"
