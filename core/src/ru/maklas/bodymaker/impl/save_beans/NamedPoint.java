package ru.maklas.bodymaker.impl.save_beans;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class NamedPoint extends Vector2 implements Json.Serializable{

    private String name;

    public NamedPoint(String name) {
        this.name = name;
    }

    public NamedPoint(String name, float x, float y) {
        super(x, y);
        this.name = name;
    }

    public NamedPoint() {

    }

    @Override
    public void write(Json json) {
        json.writeObjectStart();
        json.writeValue("name", name, String.class);
        json.writeValue("x", x, float.class);
        json.writeValue("y", y, float.class);
        json.writeObjectEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        name = jsonData.getString("name");
        x = jsonData.getFloat("x");
        y = jsonData.getFloat("y");
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
