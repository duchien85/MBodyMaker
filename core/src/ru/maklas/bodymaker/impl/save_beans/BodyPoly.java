package ru.maklas.bodymaker.impl.save_beans;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class BodyPoly implements Json.Serializable{

    Array<FixShape> shapes;
    Array<NamedPoint> points;

    public static final String MASS_CENTER = "Mass center";
    public static final String ORIGIN = "Origin";

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


    public BodyPoly mov(float x, float y){
        for (FixShape shape : shapes) {
            shape.mov(x, y);
        }

        for (NamedPoint point : points) {
            point.add(x, y);
        }
        return this;
    }

    public BodyPoly movShapes(float x, float y){
        for (FixShape shape : shapes) {
            shape.mov(x, y);
        }
        return this;
    }

    public BodyPoly scale(float scalar){
        for (FixShape shape : shapes) {
            shape.scl(scalar);
        }

        for (NamedPoint point : points) {
            point.scl(scalar);
        }
        return this;
    }

    public NamedPoint getMassCenter(){
        return findPoint("Mass center");
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
    public void read(Json json, final JsonValue data) {

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

    public void printToCode() {

        //Point declarations
        for (NamedPoint point : points) {
            if (point.getName().equalsIgnoreCase("origin")){
                continue;
            }
            System.out.println("Vector2 " + point.getName().replaceAll(" ", "_") + " = new Vector2(" + point.x + "f, " + point.y + "f);");
        }
        System.out.println();


        //Shape declaration
        for (FixShape shape : shapes) {
            System.out.println("PolygonShape " + shape.getName() + "Shape = new PolygonShape();");
        }

        System.out.println();

        //Array fill
        for (FixShape shape : shapes) {
            String arrName = shape.getName() + "Points";
            System.out.println("Array<Vector2> " + arrName + " = new Array<>();");
            Array<Vector2> points = shape.getPoints();

            for (Vector2 point : points) {
                System.out.println(arrName + ".add(new Vector2(" + point.x + "f, " + point.y + "f));");
            }

            System.out.println();
        }


        //ALL in one Array
        System.out.println("MArray<Vector2> all = new MArray<>();");
        for (FixShape shape : shapes) {
            System.out.println("all.addAll(" + shape.getName() + "Points" + ");");
        }
        System.out.println();


        for (FixShape shape : shapes) {
            String arrName = shape.getName() + "Points";
            String shapeName = shape.getName() + "Shape";

            System.out.println(shapeName + ".set(" + arrName + ".toArray(Vector2.class));");
        }

        System.out.println();
        System.out.println();

        StringBuilder nameBuilder = new StringBuilder(shapes.get(0).getName() + "Shape");

        for (int i = 1; i < shapes.size; i++) {
            nameBuilder.append(", ").append(shapes.get(i).getName()).append("Shape");
        }

        System.out.println("return MArray.with(" + nameBuilder.toString() + ");");

    }
}
