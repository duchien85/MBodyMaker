package ru.maklas.bodymaker.impl.dev_beans;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.runtime.save_beans.NamedPoint;

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

    @Nullable
    public static NamedPoint closestNamed(Array<NamedPoint> points, float x, float y){
        if (points.size == 0){
            return null;
        }

        NamedPoint closest = points.get(0);
        float dst2ToClosest = closest.dst2(x, y);

        for (NamedPoint point : points) {
            final float dst2 = point.dst2(x, y);
            if (dst2 < dst2ToClosest){
                dst2ToClosest = dst2;
                closest = point;
            }
        }
        return closest;
    }


    private static final Vector2 tv1 = new Vector2();
    private static final Vector2 tv2 = new Vector2();
    public static boolean goingRight(Vec start, Vec middle, Vec end){
        tv1.set(middle).sub(start);
        tv2.set(end).sub(middle);
        return tv1.angle(tv2) >= 0;
    }

    public static float distanceToLine(float x, float y, Vector2 lineStart, Vector2 lineEnd){
        final float f1 = (lineEnd.y - lineStart.y) * x;
        final float f2 = (lineEnd.x - lineStart.x) * y;
        final float f3 = lineEnd.x * lineStart.y - lineEnd.y * lineStart.x;
        final float f4 = lineEnd.y - lineStart.y;
        final float f5 = lineEnd.x - lineStart.x;
        return Math.abs(f1 - f2 + f3) / (float) (Math.sqrt((f4 * f4) + (f5 * f5)));
    }

    public static float distanceToLine2(float x, float y, Vector2 a, Vector2 b){
        Vector2 n = new Vector2(b).sub(a);
        Vector2 pa = new Vector2(a).sub(x, y);
        Vector2 c = new Vector2(n).scl((pa.dot(n) / n.dot(n))) ;
        Vector2 d = new Vector2(pa).sub(c);
        return (float) Math.sqrt(d.dot(d));
    }


    @Nullable
    public static Vector2 pointOnALine(float x, float y, Vector2 a, Vector2 b){
        float APx = x - a.x;
        float APy = y - a.y;
        float ABx = b.x - a.x;
        float ABy = b.y - a.y;
        float magAB2 = ABx*ABx + ABy*ABy;
        float ABdotAP = ABx*APx + ABy*APy;
        float t = ABdotAP / magAB2;

        if ( t < 0) {
            return null;
        }
        else if (t > 1) {
            return null;
        }
        else {
            return new Vector2(a.x + ABx*t, a.y + ABy*t);
        }
    }

}
