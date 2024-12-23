package com.floorplannersharing;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.awt.Color;
import java.io.IOException;

public class ColorTypeAdapter extends TypeAdapter<Color> {

    @Override
    public void write(JsonWriter out, Color color) throws IOException {
        if (color == null) {
            out.nullValue();
        } else {
            out.value(String.format("(%d,%d,%d,%d)",
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha()));
        }
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        String colorString = in.nextString();
        
        if (colorString.startsWith("(") && colorString.endsWith(")")) {
            colorString = colorString.substring(1, colorString.length() - 1);
            String[] components = colorString.split(",");
            if (components.length == 4) {
                int red = Integer.parseInt(components[0].trim());
                int green = Integer.parseInt(components[1].trim());
                int blue = Integer.parseInt(components[2].trim());
                int alpha = Integer.parseInt(components[3].trim());
                return new Color(red, green, blue, alpha);
            }
        }
        throw new IOException("Invalid color format: " + colorString);
    }
    
}
