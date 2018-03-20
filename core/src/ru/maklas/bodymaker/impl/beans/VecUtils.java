package ru.maklas.bodymaker.impl.beans;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.Nullable;

public class VecUtils {


    @Nullable
    public static Vec closestInRageOf(Array<Vec> points, float x, float y, float range){
        float maxRange2 = range * range;
        @Nullable Vec closest = null;

        for (Vec point : points) {
            float dst2 = point.dst2(x, y);
            if (dst2 < maxRange2){
                closest = point;
                maxRange2 = dst2;
            }
        }

        return closest;
    }

    @Nullable
    public static Vec closestIn(Array<Vec> points, float x, float y){
        if (points.size == 0){
            return null;
        }

        Vec closest = points.get(0);
        float dst2ToClosest = closest.dst2(x, y);

        for (Vec point : points) {
            final float dst2 = point.dst2(x, y);
            if (dst2 < dst2ToClosest){
                dst2ToClosest = dst2;
                closest = point;
            }
        }
        return closest;
    }

}
