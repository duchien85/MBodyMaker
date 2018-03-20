package ru.maklas.bodymaker.impl.beans;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class MPoly implements Iterable<MShape>{

    Array<MShape> shapes;

    public MPoly() {
        shapes = new Array<MShape>();
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

    public Array<Vec> findInRange(float x, float y, float snapRange) {
        Array<Vec> ret = new Array<Vec>();

        for (MShape shape : shapes) {
            shape.findInRange(ret, x, y, snapRange);
        }
        return ret;
    }
}
