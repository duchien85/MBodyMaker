package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.impl.dev_beans.MShape;
import ru.maklas.bodymaker.impl.dev_beans.Vec;
import ru.maklas.bodymaker.impl.dev_beans.VecUtils;
import ru.maklas.bodymaker.impl.save_beans.NamedPoint;
import ru.maklas.bodymaker.libs.Utils;

public abstract class CreationState extends InputAcceptorAdapter implements UIController{

    protected final Model model;

    public CreationState(Model model) {
        this.model = model;
    }

    public abstract void onEnterState(DevState oldState);

    public abstract void onLeaveState(DevState newState);

    @Override
    public void fileSelected(FileHandle fileHandle) {}

    public void update(float dt) {
        model.engine.update(dt);
        model.ui.act(dt);
    }

    public abstract void render(SpriteBatch batch);


    @Override
    public void zoomChanged(int zoom) {
        float newZoom = model.cam.zoom + 0.1f * zoom;
        model.cam.zoom = MathUtils.clamp(newZoom, model.minZoom, model.maxZoom);
    }

    public void moveCamera(float dx, float dy){
        final OrthographicCamera cam = model.cam;
        model.cam.position.add(dx, dy, 0);
        if (model.ru != null) {
            limit(model.cam.position, 0, model.ru.width, 0, model.ru.height);
        }
    }

    private void limit(Vector3 vec, float minX, float maxX, float minY, float maxY){
        vec.x = vec.x < minX ? minX : vec.x;
        vec.x = vec.x > maxX ? maxX : vec.x;

        vec.y = vec.y < minY ? minY : vec.y;
        vec.y = vec.y > maxY ? maxY : vec.y;
    }


    protected final void changeState(DevState newState) {
        if (newState == model.currentStateEnum){
            return;
        }

        final DevState oldStateEnum = model.currentStateEnum;
        final CreationState oldState = model.getCreationState(oldStateEnum);
        if (oldState != null){
            oldState.onLeaveState(newState);
        }
        final CreationState creationState = model.getCreationState(newState);
        model.currentStateEnum = newState;
        model.currentState = creationState;
        model.currentState.onEnterState(oldStateEnum);

        model.ui.changeState(newState);
        model.ui.setController(creationState);
        model.bodyMakerInput.setAcceptor(creationState);
    }


    @Nullable
    protected final Vec getHoveredPointShapesOnly(float x, float y){
        final Vec vec = model.poly.closestVec(x, y);
        if (vec != null && vec.dst2(x, y) < model.maxDstToHighlight){
            return vec;
        }
        return null;
    }

    @Nullable
    protected final Vec getHoveredPointShapesOnly(Vector2 vec){
        return getHoveredPointShapesOnly(vec.x, vec.y);
    }


    @Nullable
    protected final NamedPoint getHoveredPointNamedOnly(Vector2 v) {
        return getHoveredPointNamedOnly(v.x, v.y);
    }

    @Nullable
    protected final NamedPoint getHoveredPointNamedOnly(float x, float y) {
        final Array<NamedPoint> namedPoints = model.poly.getNamedPoints();
        return VecUtils.closestNamed(namedPoints, x, y);
    }


    protected final Vector2 getMouse() {
        return Utils.toScreen(Gdx.input.getX(), Gdx.input.getY(), model.cam);
    }


    protected void checkNonConvexity(@Nullable MShape shape){
        if (shape != null && shape.isNotConvex()){
            model.gsm.print("SHAPE BECAME NOT CONVEX!");
        }
    }

}
