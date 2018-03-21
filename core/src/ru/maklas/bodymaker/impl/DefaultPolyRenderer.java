package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import ru.maklas.bodymaker.impl.dev_beans.MPoly;
import ru.maklas.bodymaker.impl.dev_beans.MShape;
import ru.maklas.bodymaker.impl.dev_beans.Vec;
import ru.maklas.bodymaker.impl.save_beans.NamedPoint;

public class DefaultPolyRenderer {

    private final ShapeRenderer renderer;
    private final OrthographicCamera cam;
    private boolean renderPoints = true;
    private boolean renderNamedPoints = true;
    private boolean renderLines = true;
    SpriteBatch batch;
    BitmapFont font = new BitmapFont();

    public DefaultPolyRenderer(OrthographicCamera cam) {
        this.renderer = new ShapeRenderer();
        this.cam = cam;
    }

    public DefaultPolyRenderer(OrthographicCamera cam, ShapeRenderer renderer) {
        this.renderer = renderer;
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

        if (renderNamedPoints) renderNamedPoints(poly);

        renderer.end();
        if (batch != null){
            renderNamedPointsNames(poly, batch);
        }
    }

    public void renderPoint(float x, float y, Color color){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(color);
        renderer.circle(x, y, 5, 6);
        renderer.end();
    }

    void renderShape(MShape shape){
        if (renderLines) renderLines(shape);
        if (renderPoints) renderPoints(shape);
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

    private void renderNamedPoints(MPoly poly){

        final Array<NamedPoint> namedPoints = poly.getNamedPoints();
        if (namedPoints.size == 0){
            return;
        }

        renderer.setColor(poly.getNamedPointColor());

        for (NamedPoint namedPoint : namedPoints) {
            renderer.circle(namedPoint.x, namedPoint.y, 5, 6);
            renderer.circle(namedPoint.x, namedPoint.y, 3, 6);
            renderer.circle(namedPoint.x, namedPoint.y, 1, 6);
        }

    }

    private void renderNamedPointsNames(MPoly poly, SpriteBatch batch) {

        final Array<NamedPoint> namedPoints = poly.getNamedPoints();
        if (namedPoints.size == 0){
            return;
        }

        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        for (NamedPoint namedPoint : namedPoints) {
            int textWidth = calculateTextWidth(namedPoint.getName());
            font.draw(batch, namedPoint.getName(), namedPoint.x - textWidth/2, namedPoint.y, 5, Align.center, false);
        }

        batch.end();

    }

    private int calculateTextWidth(String name) {
        return 0;
    }

    public boolean isRenderPoints() {
        return renderPoints;
    }

    public void setRenderPoints(boolean renderPoints) {
        this.renderPoints = renderPoints;
    }

    public boolean isRenderNamedPoints() {
        return renderNamedPoints;
    }

    public void setRenderNamedPoints(boolean renderNamedPoints) {
        this.renderNamedPoints = renderNamedPoints;
    }

    public boolean isRenderLines() {
        return renderLines;
    }

    public void setRenderLines(boolean renderLines) {
        this.renderLines = renderLines;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }
}
