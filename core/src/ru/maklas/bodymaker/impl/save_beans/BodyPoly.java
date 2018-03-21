package ru.maklas.bodymaker.impl.save_beans;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class BodyPoly implements Json.Serializable{

    Array<FixShape> shapes;
    Array<NamedPoint> points;

    public BodyPoly() {
        shapes = new Array<FixShape>();
        points = new Array<NamedPoint>();
    }

    //***********//
    //* SETTERS *//
    //***********//

    public void addShape(FixShape shape){
        shapes.add(shape);
    }

    public void addPoints(Array<NamedPoint> points){
        for (NamedPoint point : points) {
            this.points.add(new NamedPoint(point));
        }
    }

    //***********//
    //* Getters *//
    //***********//

    public FixShape findShape(String name){
        for (FixShape shape : shapes) {
            if (shape.getName().equals(name)){
                return shape;
            }
        }
        return null;
    }

    public NamedPoint findPoint(String name){
        for (NamedPoint point : points) {
            if (point.getName().equals(name)){
                return point;
            }
        }
        return null;
    }


    //**********//
    //* JSON *//
    //**********//

    @Override
    public String toString() {
        return "BodyPoly{" +
                "shapes=" + shapes +
                ", points=" + points +
                '}';
    }

    @Override
    public void write(Json json) {

        json.writeArrayStart("shapes");
        for (FixShape shape : shapes) {
            json.writeValue(shape);
        }
        json.writeArrayEnd();

        json.writeArrayStart("points");
        for (NamedPoint point: points) {
            json.writeValue(point);
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue data) {

        Array<FixShape> shapesArr = new Array<FixShape>();
        final JsonValue shapes = data.get("shapes");
        for (JsonValue entry = shapes.child; entry != null; entry = entry.next){
            final FixShape fixShape = new FixShape();
            fixShape.read(json, entry);
            shapesArr.add(fixShape);
        }
        this.shapes = shapesArr;


        Array<NamedPoint> pointsArr = new Array<NamedPoint>();
        final JsonValue points = data.get("points");
        for (JsonValue entry = points.child; entry != null; entry = entry.next){
            final NamedPoint nextPoint = new NamedPoint();
            nextPoint.read(json, entry);
            pointsArr.add(nextPoint);
        }
        this.points = pointsArr;
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

    public Array<FixShape> getShapes() {
        return shapes;
    }

    public Array<NamedPoint> getPoints() {
        return points;
    }
}
