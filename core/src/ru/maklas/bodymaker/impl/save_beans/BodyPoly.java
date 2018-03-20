package ru.maklas.bodymaker.impl.save_beans;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import ru.maklas.bodymaker.impl.dev_beans.MPoly;
import ru.maklas.bodymaker.impl.dev_beans.MShape;

import java.util.Arrays;

public class BodyPoly implements Json.Serializable{

    FixShape[] shapes;
    boolean hasCenter;
    float centerX;
    float centerY;

    public BodyPoly(MPoly poly, float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        hasCenter = true;
        setShapes(poly);
    }
    public BodyPoly(MPoly poly) {
        hasCenter = false;
        setShapes(poly);
    }

    public BodyPoly() {

    }

    void setShapes(MPoly poly){
        shapes = new FixShape[poly.size()];
        int counter = 0;
        for (MShape shape : poly) {
            shapes[counter++] = new FixShape(shape);
        }
    }

    public FixShape find(String name){
        for (FixShape shape : shapes) {
            if (shape.getName().equals(name)){
                return shape;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder()
                .append("BodyPoly{")
                .append("shapes=")
                .append(Arrays.toString(shapes));

        if (hasCenter){
            builder
                    .append(", centerX=")
                    .append(centerX)
                    .append(", centerY=")
                    .append(centerY);
        }

        return builder.append('}').toString();
    }

    public boolean isHasCenter() {
        return hasCenter;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    @Override
    public void write(Json json) {
        if (hasCenter) {
            json.writeValue("cX", centerX);
            json.writeValue("cY", centerY);
        }
        json.writeArrayStart("shapes");
        for (FixShape shape : shapes) {
            json.writeValue(shape);
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue data) {
        if (data.has("cX")){
            this.centerX = data.get("cX").asFloat();
            this.centerY = data.get("cY").asFloat();
            this.hasCenter = true;
        } else {
            hasCenter = false;
        }

        Array<FixShape> shapesArr = new Array<FixShape>();
        final JsonValue shapes = data.get("shapes");
        for (JsonValue entry = shapes.child; entry != null; entry = entry.next){
            final FixShape fixShape = new FixShape();
            fixShape.read(json, entry);
            shapesArr.add(fixShape);
        }
        this.shapes = shapesArr.toArray(FixShape.class);
    }

    public String toJson() {
        Json json = new Json();
        return json.toJson(this);
    }

    public static BodyPoly fromJson(String jsonString){
        Json json = new Json();
        final BodyPoly bodyPoly = json.fromJson(BodyPoly.class, jsonString);
        return bodyPoly;
    }
}
