package ru.maklas.bodymaker.impl.dev_beans;

import com.badlogic.gdx.math.Vector2;

public class Vec extends Vector2 {

    private final MShape shape;

    public Vec(float x, float y, MShape shape) {
        this.shape = shape;
        this.x = x;
        this.y = y;
    }

    public Vec(MShape shape) {
        this.shape = shape;
    }

    public MShape getShape() {
        return shape;
    }
}
