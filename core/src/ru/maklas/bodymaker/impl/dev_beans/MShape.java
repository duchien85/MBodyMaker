package ru.maklas.bodymaker.impl.dev_beans;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

import static ru.maklas.bodymaker.impl.dev_beans.VecUtils.closestIn;
import static ru.maklas.bodymaker.impl.dev_beans.VecUtils.closestInRageOf;

public class MShape implements Iterable<Vec>{

    private final Array<Vec> points;
    private Color lineColor;
    private Color pointColor;
    private Color endLineColor;
    private String name;
    @Nullable private MPoly poly;

    public MShape(String name) {
        this.name = name;
        points = new Array<Vec>(true, 8);
        pointColor = Color.GREEN;
        lineColor = Color.WHITE;
        endLineColor = Color.GRAY;
    }

    public String getName() {
        return name;
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

    /**
     * Finds the right place for point to be inserted.
     */
    public boolean insert(float x, float y){
        if (points.size < 3){
            add(x, y);
            return true;
        }

        final Vec vec = new Vec(x, y, this);

        int index = 0;
        float minDist = 0;
        boolean found;
        {
            final Vector2 pointOnALine = VecUtils.pointOnALine(x, y, points.get(0), points.get(1));
            if (pointOnALine != null) {
                minDist = pointOnALine.dst(x, y);
                found = true;
            } else {
                found = false;
            }
        }

        for (int i = 1; i < points.size; i++) {
            Vec current = points.get(i);
            Vec next = points.get((i + 1) % points.size);
            Vector2 pointOnALine = VecUtils.pointOnALine(x, y, current, next);
            if (pointOnALine == null){
                continue;
            }
            float dst = pointOnALine.dst(x, y);

            if (!found){
                found = true;
                minDist = dst;
                index = i;
            } else {
                if (dst < minDist){
                    minDist = dst;
                    index = i;
                }
            }
        }

        if (found) {
            points.insert((index + 1) % points.size, vec);
            return true;
        }
        return false;
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

    public void remove(Vec point) {
        points.removeValue(point, true);
    }

    public void findInRange(Array<Vec> ret, float x, float y, float snapRange) {
        final float range2 = snapRange * snapRange;
        for (Vec point : points) {
            if (point.dst2(x, y) < range2){
                ret.add(point);
            }
        }
    }

    public boolean isNotConvex(){
        final Array<Vec> points = this.points;
        if (points.size <= 3){
            return false;
        }

        boolean firstIsGoingRight = VecUtils.goingRight(points.get(0), points.get(1), points.get(2));
        for (int i = 1; i < points.size; i++) {
            int i1 = (i + 1) % points.size;
            int i2 = (i + 2) % points.size;
            if (VecUtils.goingRight(points.get(i), points.get(i1), points.get(i2)) != firstIsGoingRight){
                return true;
            }
        }

        return false;
    }

}
