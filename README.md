# FloorPlan & Sharing

## Overview

**FloorPlannerSharing** is a Java desktop application for designing, saving, and sharing 2D floor plans. It provides a graphical interface for drawing rooms, placing furniture, and managing user accounts. The application supports file upload/download, including Dropbox integration, and features a user dashboard for managing and searching shared plans.

---

## Main Components

### 1. Drawing and Canvas Management

- **CanvasObject & Subclasses**:  
  The abstract `CanvasObject` class is the base for all drawable items (rooms, furniture, doors, windows). Each subclass (e.g., `Room`, `Furniture`, `Door`, `Window`) implements its own drawing and interaction logic.

- **CanvasObjectManager**:  
  Singleton managing all objects on the canvas, including selection, layering, and object lists.

- **DrawingTool & ToolPanel**:  
  Abstracts the concept of a drawing tool (e.g., room tool, furniture tool, select tool). The `ToolPanel` provides a sidebar for switching between tools and customizing options (like room color).

- **SketchPanel**:  
  The main drawing area. Handles mouse events for drawing, moving, aligning, and snapping objects to a grid.

- **SketchApp**:  
  The main application window. Initializes the UI, menu bar, tool panel, and sketch panel. Handles file operations and status updates.

- **Core Components**

- **`Room`**  
  Represents a room with color and transparency. Implements drawing logic for filled rectangles.

- **`Furniture`**  
  Represents furniture items, rendered as PNG images with support for rotation.

- **`Door`** and **`Window`**  
  Represent doors and windows, drawn as rectangles with specific styles (e.g., dashed lines for windows).


---
- **Drawing Tools**

- **`DrawingTool`**  
  Abstract class for tools that create or manipulate canvas objects.

- **`RoomTool`**  
  Tool for drawing rooms, with color selection.

- **`FurnitureTool`**  
  Tool for placing furniture, with image selection.

- **`DoorTool`** and **`WindowTool`**  
  Tools for adding doors and windows, including logic for placement validation (e.g., must be on a room border).

- **`SelectTool`**  
  Tool for selecting and moving existing objects.

---
---

### 2. File Management

- **FileManager**:  
  Handles saving and loading floor plans as JSON files using Gson. Integrates with the UI for file dialogs and updates the canvas after import.

- **Gson Type Adapters**:  
  Custom adapters (`CanvasObjectTypeAdapter`, `CanvasObjectAdapterFactory`, `ColorTypeAdapter`) ensure correct serialization/deserialization of polymorphic canvas objects and colors.

---

### 3. User Management and Sharing

#### **UserLogin.java**

- Provides a login form for users to authenticate.
- Connects to a database using credentials from a `config.properties` file.
- On successful login, opens the user dashboard (`UserHome`).
- Offers a button to open the registration form (`UserRegistration`).

#### **UserRegistration.java**

- Presents a registration form for new users (fields: first name, last name, email, username, mobile, password).
- Validates input (e.g., mobile number length).
- Stores new user credentials in the database.
- On successful registration, opens the user dashboard (`UserHome`).

#### **UserHome.java**

- The main dashboard after login.
- **Navigation Panel**:  
  Buttons for "All Plans", "Uploaded", "Saved", "Most Popular", "Archived", and "Account".
- **Search Bar**:  
  Allows searching for public plans by name.
- **File Upload**:  
  Lets users upload files to Dropbox, set access mode (public/private), and specify custom users for sharing.
- **File Listing**:  
  Displays uploaded and saved files, with clickable links to download and open them in the editor.
- **Account Management**:  
  Shows user details, number of uploads/saved files, and provides logout/change password options.
- **Dropbox Integration**:  
  Handles file upload/download using Dropbox API, with credentials loaded from `config.properties`.
- **Database Integration**:  
  Stores file metadata (name, uploader, access, link, file ID) and supports queries for searching and listing files.

---

## How User Management Works

1. **Login**:  
   Users enter their credentials in `UserLogin`. If valid, they are taken to `UserHome`.

2. **Registration**:  
   New users fill out the form in `UserRegistration`. On success, they are logged in and redirected to `UserHome`.

3. **Dashboard (`UserHome`)**:  
   - Users can upload new plans, search for public plans, and view/manage their files.
   - Uploaded files are stored in Dropbox and indexed in the database.
   - Users can download and open plans directly in the editor.
   - Account details and logout/change password options are available.

---

## Example User Flow

1. **Start the application** and register a new account or log in.
2. **Design a floor plan** using the drawing tools.
3. **Save your plan** locally or upload it to Dropbox via the dashboard.
4. **Search for public plans** or view your uploaded/saved files.
5. **Download and open** any plan for further editing.

---

## File Reference

- `UserHome.java`: User dashboard, file sharing, Dropbox and DB integration.
- `UserLogin.java`: Login form and authentication logic.
- `UserRegistration.java`: Registration form and new user creation.

---

## Dependencies

- Java Swing (UI)
- Gson (JSON serialization)
- Dropbox Java SDK (cloud storage)
- JDBC (database access)

---

## ⚙️ Configuration

Before running the project, make sure to configure your credentials and database connection settings in `config.properties` which should be created in the path : 

```
src/main/resources
```

`config.properties` file:

```properties
dropbox.access.token="YOUR_DROPBOX_ACCESS_TOKEN"
DB_URL="jdbc:mysql://localhost:3306/your_database_name"
DB_USER="your_db_username"
DB_PASSWORD="your_db_password"

```
---

## Getting Started

1. Build and run the project in your Java IDE.<br>
Build:
```
mvn compile
```
Run:
```
mvn exec:java -Dexec.mainClass="com.floorplannersharing.UserLogin"
```


```
