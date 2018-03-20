package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.jetbrains.annotations.NotNull;
import ru.maklas.bodymaker.impl.beans.MPoly;
import ru.maklas.bodymaker.impl.beans.MShape;
import ru.maklas.bodymaker.impl.beans.Vec;

public class DefaultPolyRenderer {

    private final ShapeRenderer renderer;
    private final OrthographicCamera cam;

    public DefaultPolyRenderer(OrthographicCamera cam) {
        this.renderer = new ShapeRenderer();
        this.cam = cam;
    }


    public void render(@NotNull MPoly poly){
        if (poly.size() == 0){
            return;
        }

        renderer.setProjectionMatrix(cam.combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);

        for (MShape shapes : poly) {
            renderShape(shapes);
        }
        renderer.end();
    }

    public void renderPoint(float x, float y, Color color){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(color);
        renderer.circle(x, y, 5, 6);
        renderer.end();
    }

    void renderShape(MShape shape){
        renderLines(shape);
        renderPoints(shape);
    }

    void renderLines(MShape shape){
        if (shape.size() > 1) {
            renderer.setColor(shape.getLineColor());
            for (int i = 0; i < shape.size() - 1; i++) {
                renderer.line(shape.get(i), shape.get(i + 1));
            }
        }
        if (shape.size() > 2){
            renderer.setColor(shape.getEndLineColor());
            renderer.line(shape.get(0), shape.get(shape.size()  -1));
        }
    }

    private void renderPoints(MShape shape) {
        if (shape.size() > 0){
            renderer.setColor(shape.getPointColor());
            for (Vec point : shape) {
                renderer.circle(point.x, point.y, 5, 6);
            }
        }
    }
}
