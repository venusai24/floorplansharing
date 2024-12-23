package com.floorplannersharing;

import java.io.IOException;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.awt.Color;

public class CanvasObjectTypeAdapter extends TypeAdapter<CanvasObject> {

    @Override
    public void write(JsonWriter out, CanvasObject object) throws IOException {
        if (object == null) {
            out.nullValue();
            return;
        }

        out.beginObject();
        out.name("type").value(object.getType());
        out.name("x").value(object.x);
        out.name("y").value(object.y);
        out.name("width").value(object.width);
        out.name("height").value(object.height);
        // out.name("layer").value(object.layer);

        switch (object.getType()) {
            case "Room":
                Room room = (Room) object;
                out.name("color").value(String.format("(%d,%d,%d,%d)",
                    room.getColor().getRed(),
                    room.getColor().getGreen(),
                    room.getColor().getBlue(),
                    room.getColor().getAlpha()));
                // out.name("alpha").value(room.getAlpha());
                break;
            case "Furniture":
                Furniture furniture = (Furniture) object;
                out.name("imageIndex").value(furniture.getImageIndex());
                break;
        }

        out.endObject();

    }

    @Override
    public CanvasObject read(JsonReader in) throws IOException {
        JsonObject jsonObject = JsonParser.parseReader(in).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        int x = jsonObject.get("x").getAsInt();
        int y = jsonObject.get("y").getAsInt();
        int width = jsonObject.get("width").getAsInt();
        int height = jsonObject.get("height").getAsInt();
        // int layer = jsonObject.get("layer").getAsInt();
        
        switch (type) {
            case  "Room":
                String colorString = jsonObject.get("color").getAsString();
                Color color;
                if (colorString.startsWith("(") && colorString.endsWith(")")) {
                    colorString = colorString.substring(1, colorString.length() - 1);
                    String[] components = colorString.split(",");
                    if (components.length == 4) {
                        int red = Integer.parseInt(components[0].trim());
                        int green = Integer.parseInt(components[1].trim());
                        int blue = Integer.parseInt(components[2].trim());
                        int alpha = Integer.parseInt(components[3].trim());
                        color = new Color(red, green, blue, alpha);
                        return new Room(x, y, width, height, color);
                    }
                }
                // float alpha = jsonObject.get("alpha").getAsFloat(); // comment this line, alpha not initialized in constructor
                // no need to get layer, comment out that later
                throw new IOException("Invalid color format: " + colorString);
                
            case "Furniture":
                int imageIndex = jsonObject.get("imageIndex").getAsInt();
                return new Furniture(x, y, width, height, imageIndex);
            case "Door":
                return new Door(x, y, width, height);
            case "Window":
                return new Window(x, y, width, height);
            default:
                throw new JsonIOException("Unkown Type " + type);
        }
    }
    
}
