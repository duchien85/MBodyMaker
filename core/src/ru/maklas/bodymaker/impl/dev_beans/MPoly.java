package ru.maklas.bodymaker.impl.dev_beans;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.impl.save_beans.BodyPoly;
import ru.maklas.bodymaker.impl.save_beans.FixShape;
import ru.maklas.bodymaker.impl.save_beans.NamedPoint;

import java.util.Iterator;

public class MPoly implements Iterable<MShape>{

    Array<MShape> shapes;
    Array<NamedPoint> points;
    Color namedPointColor = Color.PURPLE;

    public MPoly() {
        shapes = new Array<MShape>();
        points = new Array<NamedPoint>();
    }

    @NotNull
    @Override
    public Iterator<MShape> iterator() {
        return shapes.iterator();
    }

    public void add(MShape shape){
        shape.setPoly(this);
        shapes.add(shape);
    }

    public void remove(MShape shape){
        shapes.removeValue(shape, true);
    }

    @Nullable
    public MShape closestShape(float x, float y){
        final Vec vec = closestVec(x, y);
        return vec == null ? null : vec.getShape();
    }

    public void addNamedPoint(String name, float x, float y){
        points.add(new NamedPoint(name, x, y));
    }

    public void remove(NamedPoint point){
        points.removeValue(point, true);
    }

    public NamedPoint removeClosestInRange(float x, float y){
        final NamedPoint namedPoint = VecUtils.closestNamed(points, x, y);
        if (namedPoint != null){
            points.removeValue(namedPoint, true);
            return namedPoint;
        }
        return null;
    }

    public Array<NamedPoint> getNamedPoints() {
        return points;
    }

    @Nullable
    public Vec closestVec(float x, float y){
        if (shapes.size == 0){
            return null;
        }
        Array<Vec> vecs = new Array<Vec>();
        for (int i = 0; i < shapes.size; i++) {
            final Vec value = VecUtils.closestIn(shapes.get(i).getPoints(), x, y);
            if (value != null)
                vecs.add(value);
        }

        return VecUtils.closestIn(vecs, x, y);
    }

    public int size() {
        return shapes.size;
    }

    public MShape last() {
        if (shapes.size > 0)
            return shapes.get(shapes.size - 1);
        else
            return null;
    }

    public Color getNamedPointColor() {
        return namedPointColor;
    }

    public void setNamedPointColor(Color namedPointColor) {
        this.namedPointColor = namedPointColor;
    }

    public Array<Vec> findInRange(float x, float y, float snapRange) {
        Array<Vec> ret = new Array<Vec>();

        for (MShape shape : shapes) {
            shape.findInRange(ret, x, y, snapRange);
        }
        return ret;


    }

    public Array<MShape> getShapes() {
        return shapes;
    }

    public Body createBody(World world) {
        if (shapes.size == 0){
            return null;
        }

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        final Body body = world.createBody(bDef);

        for (MShape shape : shapes) {
            shape.createFixture(body);
        }
        body.setLinearVelocity(0, 0);

        return body;
    }


    public BodyPoly toBeans(){
        final BodyPoly bodyPoly = new BodyPoly();
        bodyPoly.addPoints(points); //named points

        for (MShape shape : shapes) {
            final FixShape fixShape = new FixShape();
            fixShape.setName(shape.getName()); //shape names
            for (Vec vec : shape.getPoints()) {
                fixShape.addPoint(vec); //shape points
            }
            bodyPoly.addShape(fixShape); //shapes
        }

        return bodyPoly;
    }

    public String toJson(){
        return toBeans().toJson();
    }

    public void load(String json){
        BodyPoly bodyPoly = BodyPoly.fromJson(json);
        points.clear();
        shapes.clear();
        for (FixShape shape : bodyPoly.getShapes()) {
            MShape s = new MShape(shape.getName());
            for (Vector2 p : shape.getPoints()) {
                s.add(p.x, p.y);
            }
            add(s);
        }

        for (NamedPoint namedPoint : bodyPoly.getPoints()) {
            addNamedPoint(namedPoint.getName(), namedPoint.x, namedPoint.y);
        }
    }
}
