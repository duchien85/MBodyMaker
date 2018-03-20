package ru.maklas.bodymaker.impl.save_beans;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import ru.maklas.bodymaker.impl.dev_beans.MShape;
import ru.maklas.bodymaker.impl.dev_beans.Vec;

import java.util.Arrays;

public class FixShape implements Json.Serializable{

    private String name;
    private Vector2[] points;

    public FixShape() {

    }

    public FixShape(MShape s) {
        final Array<Vec> points = s.getPoints();
        this.points = points.toArray(Vector2.class);
        this.name = s.getName();
    }


    public FixShape mov(float x, float y){
        for (Vector2 point : points) {
            point.add(x, y);
        }
        return this;
    }

    public FixShape scl(float scalar){
        for (Vector2 point : points) {
            point.scl(scalar);
        }
        return this;
    }

    public PolygonShape toPolygonShape(){
        final PolygonShape shape = new PolygonShape();
        shape.set(points);
        return shape;
    }


    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{name='" + name + '\'' +
                ", points=" + Arrays.toString(points) +
                '}';
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", name, String.class);
        json.writeArrayStart("points");
        for (Vector2 point : points) {
            json.writeObjectStart();
            json.writeValue("x", point.x);
            json.writeValue("y", point.y);
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        name = jsonData.get("name").asString();
        final JsonValue points = jsonData.get("points");
        Array<Vector2> pointsArr = new Array<Vector2>();
        for (JsonValue entry = points.child; entry != null; entry = entry.next){
            pointsArr.add(new Vector2(entry.get("x").asFloat(), entry.get("y").asFloat()));
        }
        this.points = pointsArr.toArray(Vector2.class);
    }
}
