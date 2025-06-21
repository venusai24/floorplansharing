# FloorPlanner&Sharing

## Overview

**FloorPlannerSharing** is a Java Swing-based application for creating, editing, saving, and sharing 2D floor plans. It features a graphical interface for drawing rooms, placing furniture, adding doors and windows, and managing user accounts with file upload/download capabilities (including Dropbox integration). The codebase is modular, with each class handling a specific aspect of the application.

---

## Core Components

### 1. **Canvas and Drawing**

- **`CanvasObject`**  
  Abstract base class for all drawable objects (rooms, furniture, doors, windows). Extends `Rectangle` and defines methods for drawing, moving, rotating, and type identification.

- **`Room`**  
  Represents a room with color and transparency. Implements drawing logic for filled rectangles.

- **`Furniture`**  
  Represents furniture items, rendered as PNG images with support for rotation.

- **`Door`** and **`Window`**  
  Represent doors and windows, drawn as rectangles with specific styles (e.g., dashed lines for windows).

- **`CanvasObjectManager`**  
  Singleton class managing all canvas objects, their layering, addition/removal, and selection.

---

### 2. **Drawing Tools**

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

### 3. **UI Components**

- **`SketchApp`**  
  Main application window. Initializes the UI, menu bar, tool panel, and sketch panel. Handles file operations and status updates.

- **`SketchPanel`**  
  The main drawing area. Handles mouse events for drawing, moving, and aligning objects. Supports grid snapping, collision detection, and context menus for object manipulation.

- **`ToolPanel`**  
  Sidebar with buttons for selecting drawing tools and options for customizing rooms and furniture.

---

### 4. **Persistence and Serialization**

- **`FileManager`**  
  Handles saving/loading canvas data to/from JSON files using Gson. Supports both manual file selection and programmatic loading.

- **`CanvasObjectTypeAdapter`** and **`CanvasObjectAdapterFactory`**  
  Custom Gson adapters for serializing/deserializing polymorphic `CanvasObject` types.

- **`ColorTypeAdapter`**  
  Gson adapter for serializing/deserializing `Color` objects.

---

### 5. **User Management and Sharing**

- **`UserLogin`** and **`UserRegistration`**  
  Swing forms for user authentication and registration, with database integration.

- **`UserHome`**  
  User dashboard for managing uploaded/saved plans, searching public plans, and uploading/downloading files via Dropbox. Includes account management features.

---

### 6. **Utilities**

- **`FurnitureLoader`**  
  Singleton for loading and providing furniture images.

- **`RotationUtility`**  
  Utility for rotating objects (and contained objects) around a pivot.

- **`PathManager`**  
  Manages freeform paths (not heavily used in the main UI).


---

## Key Features

- **Interactive Drawing:**  
  Draw rooms, place furniture, add doors/windows with mouse interactions.

- **Object Manipulation:**  
  Move, align, rotate, and delete objects. Context menus for advanced actions.

- **Layered Rendering:**  
  Ensures correct visual stacking of rooms, furniture, doors, and windows.

- **Grid and Snap:**  
  Optional grid display and snapping for precise placement.

- **Persistence:**  
  Save/load floor plans as JSON files.

- **User Accounts:**  
  Register, login, and access personalized dashboards.

- **Cloud Integration:**  
  Upload/download plans using Dropbox. Share public/private plans.

- **Search and Browse:**  
  Search for public plans by name, view uploaded/saved/archived plans.

---

## How to Run

1. **Run `SketchApp`** for the main drawing application.
2. **Run `UserLogin` or `UserRegistration`** for creating an account and logging in to the platform to share, upload and download floorplans.
---

## Dependencies

- **Swing** (Java standard library)
- **Gson** (for JSON serialization)
- **Dropbox Java SDK** (for file sharing)
- **JDBC** (for database connection)


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

## Running the Application

```
mvn exec:java -Dexec.mainClass="com.floorplannersharing.UserLogin"
```
