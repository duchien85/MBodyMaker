package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.engine.PhysicsDebugSystem;
import ru.maklas.bodymaker.engine.PhysicsSystem;
import ru.maklas.bodymaker.engine.RenderingSystem;
import ru.maklas.bodymaker.engine.rendering.RenderComponent;
import ru.maklas.bodymaker.impl.beans.MPoly;
import ru.maklas.bodymaker.impl.beans.MShape;
import ru.maklas.bodymaker.impl.beans.Vec;
import ru.maklas.bodymaker.libs.Utils;
import ru.maklas.bodymaker.libs.gsm_lib.State;
import ru.maklas.mengine.Engine;
import ru.maklas.mengine.Entity;

public class BodyMakerState extends State implements InputAcceptor, UIController{

    float maxDstToHighlight = 5;
    OrthographicCamera cam;
    BodyMakerInput bodyMakerInput;
    UI ui;
    InputMultiplexer input;
    Engine engine;
    World world;
    PhysicsDebugSystem debug;
    DefaultPolyRenderer renderer;
    RuntimeTextureLoader loader;

    MPoly poly;
    @Nullable MShape currentShape;

    @Override
    protected void onCreate() {
        loader = new RuntimeTextureLoader();
        cam = new OrthographicCamera(1280, 800);
        bodyMakerInput = new BodyMakerInput(this, cam);
        ui = new UI(this);
        input = new InputMultiplexer(ui, bodyMakerInput);
        engine = new Engine();
        world = new World(new Vector2(0, 0), false);
        debug = new PhysicsDebugSystem(world, cam);
        renderer = new DefaultPolyRenderer(cam);
        poly = new MPoly();

        engine.add(new PhysicsSystem(world));
        engine.add(new RenderingSystem(batch, cam));

        preset();
    }

    private void preset() {
        final TextureRegion region = loader.loadFromAsset("badlogic.jpg");
        engine.add(new Entity(0, 0, 0).add(new RenderComponent(region)));
    }

    @Override
    protected InputProcessor getInput() {
        return input;
    }

    @Override
    protected void update(float dt) {
        engine.update(dt);
    }

    @Override
    protected void render(SpriteBatch batch) {
        batch.begin();
        engine.render();
        batch.end();
        debug.update(0);
        renderer.render(poly);
        final Vector2 mouse = getMouse();
        final Vec vec = poly.closestVec(mouse.x, mouse.y);
        if (vec != null && vec.dst2(mouse) < maxDstToHighlight){
            renderer.renderPoint(vec.x, vec.y, Color.CORAL);
        }
    }

    private Vector2 getMouse() {
        return Utils.toScreen(Gdx.input.getX(), Gdx.input.getY(), cam);
    }

    @Override
    protected void dispose() {

    }





    @Override
    public void keyPressed(int key) {
        if (key == Input.Keys.NUM_1){
            if (currentShape == null){
                currentShape = new MShape();
                poly.add(currentShape);
            }
            final Vector2 mouse = getMouse();
            System.out.println(mouse);
            currentShape.add(mouse.x, mouse.y);
        }
    }

    @Override
    public void dragged(float dx, float dy) {

    }

    @Override
    public void clicked(float x, float y) {

    }

    @Override
    public void moved(float x, float y) {

    }

    @Override
    public void zoomChanged(int zoom) {
        cam.zoom += 0.1f * zoom;
    }
}
