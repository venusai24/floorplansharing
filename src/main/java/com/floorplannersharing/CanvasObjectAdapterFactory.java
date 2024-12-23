package com.floorplannersharing;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.google.gson.TypeAdapterFactory;

public class CanvasObjectAdapterFactory {
    
    public static TypeAdapterFactory getAdapterFactory() {
        return RuntimeTypeAdapterFactory
                .of(CanvasObject.class, "type") 
                .registerSubtype(Room.class, "Room")
                .registerSubtype(Furniture.class, "Furniture")
                .registerSubtype(Door.class, "Door")
                .registerSubtype(Window.class, "Window");
    }
}
