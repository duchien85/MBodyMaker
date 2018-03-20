package ru.maklas.bodymaker.impl.beans;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

import static ru.maklas.bodymaker.impl.beans.VecUtils.closestIn;
import static ru.maklas.bodymaker.impl.beans.VecUtils.closestInRageOf;

public class MShape implements Iterable<Vec>{

    private final Array<Vec> points;
    private Color lineColor;
    private Color pointColor;
    private Color endLineColor;
    @Nullable private MPoly poly;

    public MShape() {
        points = new Array<Vec>();
        pointColor = Color.GREEN;
        lineColor = Color.WHITE;
        endLineColor = Color.GRAY;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setPointColor(Color pointColor) {
        this.pointColor = pointColor;
    }

    public Color getEndLineColor() {
        return endLineColor;
    }

    public void setEndLineColor(Color endLineColor) {
        this.endLineColor = endLineColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public Color getPointColor() {
        return pointColor;
    }

    public void add(float x, float y){
        points.add(new Vec(x, y, this));
    }

    public Array<Vec> getPoints() {
        return points;
    }


    void setPoly(MPoly poly) {
        this.poly = poly;
    }

    @Nullable
    public MPoly getPoly() {
        return poly;
    }

    @NotNull
    @Override
    public Iterator<Vec> iterator() {
        return points.iterator();
    }

    @Nullable
    public Vec closest(float x, float y){
        return closestIn(points, x, y);
    }

    public Vec removeClosest(float x, float y){
        final Vec closest = closest(x, y);
        if (closest == null){
            return null;
        }

        points.removeValue(closest, true);
        return closest;
    }

    public Vec closestInRage(float x, float y, float maxRange){
        return closestInRageOf(points, x, y, maxRange);
    }


    public int size(){
        return points.size;
    }


    public MShape move(float x, float y){
        for (Vec point : points) {
            point.add(x, y);
        }
        return this;
    }

    public MShape scl(float scalar){
        for (Vec point : points) {
            point.scl(scalar);
        }
        return this;
    }

    public Vec get(int i) {
        return points.get(i);
    }
}
