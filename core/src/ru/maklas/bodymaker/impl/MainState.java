package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import ru.maklas.bodymaker.engine.PhysicsDebugSystem;
import ru.maklas.bodymaker.engine.PhysicsSystem;
import ru.maklas.bodymaker.engine.RenderingSystem;
import ru.maklas.bodymaker.impl.dev_beans.MPoly;
import ru.maklas.bodymaker.libs.gsm_lib.State;
import ru.maklas.mengine.Engine;
import ru.maklas.mengine.Entity;

public class MainState extends State {

    Model model;

    @Override
    protected void onCreate() {
        model = new Model();
        model.imaging = new ImageSelectionState(model);
        model.polyChange = new PolygonEditorState(model);
        model.pointCreationAndSave = new PointCreationState(model);
        model.currentStateEnum = DevState.Image;
        model.currentState = model.imaging;

        model.cam = new OrthographicCamera(1280, 800);
        model.bodyMakerInput = new BodyMakerInput(model.imaging, model.cam);
        model.ui = new UI(model.imaging);
        model.input = new InputMultiplexer(model.bodyMakerInput, model.ui);
        model.engine = new Engine();
        model.world = new World(new Vector2(0, 0), true);
        model.debug = new PhysicsDebugSystem(model.world, model.cam);
        model.shapeRenderer = new ShapeRenderer();
        model.polyRenderer = new DefaultPolyRenderer(model.cam, model.shapeRenderer);
        model.loader = new RuntimeTextureLoader();
        model.gsm = getGsm();
        model.poly = new MPoly();
        model.entity = new Entity();
        model.drag = new DragManager();

        model.engine.add(model.entity);
        model.engine.add(new PhysicsSystem(model.world));
        model.engine.add(new RenderingSystem(batch, model.cam));
        model.polyRenderer.setBatch(batch);
    }

    @Override
    protected InputProcessor getInput() {
        return model.input;
    }

    @Override
    protected void update(float dt) {
        model.currentState.update(dt);
    }

    @Override
    protected void render(SpriteBatch batch) {
        model.currentState.render(batch);
    }

    @Override
    protected void dispose() {

    }

}
