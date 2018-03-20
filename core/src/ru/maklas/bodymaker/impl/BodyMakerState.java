package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.engine.PhysicsDebugSystem;
import ru.maklas.bodymaker.engine.PhysicsSystem;
import ru.maklas.bodymaker.engine.RenderingSystem;
import ru.maklas.bodymaker.engine.rendering.RenderComponent;
import ru.maklas.bodymaker.impl.dev_beans.MPoly;
import ru.maklas.bodymaker.impl.dev_beans.MShape;
import ru.maklas.bodymaker.impl.dev_beans.Vec;
import ru.maklas.bodymaker.impl.dev_beans.VecUtils;
import ru.maklas.bodymaker.impl.save_beans.BodyPoly;
import ru.maklas.bodymaker.libs.Utils;
import ru.maklas.bodymaker.libs.gsm_lib.State;
import ru.maklas.mengine.Engine;
import ru.maklas.mengine.Entity;

public class BodyMakerState extends State implements InputAcceptor, UIController{

    float maxDstToHighlight = 8;
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

    private float maxZoom = 4f;
    private float minZoom = 0.1f;

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
        final Vec vec = getHoveredPoint();
        if (vec != null){
            renderer.renderPoint(vec.x, vec.y, Color.CORAL);
        }
    }

    private Vector2 getMouse() {
        return Utils.toScreen(Gdx.input.getX(), Gdx.input.getY(), cam);
    }

    @Override
    protected void dispose() {

    }

    @Nullable
    Vec getHoveredPoint(){
        return getHoveredPoint(getMouse());
    }

    @Nullable
    Vec getHoveredPoint(Vector2 pos){
        return getHoveredPoint(pos.x, pos.y);
    }

    @Nullable
    Vec getHoveredPoint(float x, float y){
        final Vec vec = poly.closestVec(x, y);
        if (vec != null && vec.dst2(x, y) < maxDstToHighlight){
            return vec;
        }
        return null;
    }


    @Override
    public void keyPressed(int key) {
        if (key >= Input.Keys.NUM_0 && key <= Input.Keys.NUM_9){
            numPressed(key -  Input.Keys.NUM_0);
        }


        switch (key){
            case Input.Keys.A : addPointAtMouseLocation();
            break;
            case Input.Keys.D : deletePointUnder(getMouse());
            break;
            case Input.Keys.S : snapTwoPointsUnder(getMouse());
            break;
            case Input.Keys.N : startNewShape(); addPointAtMouseLocation();
            break;
            case Input.Keys.Q : save();
            break;
        }
    }

    public void numPressed(int number){
        switch (number){
            case 0:
                break;
        }
    }

    private int shapeNameCounter = 0;

    private void startNewShape() {
        if (currentShape == null || currentShape.size() != 0){
            MShape newShape = new MShape("Shape " + shapeNameCounter++);
            poly.add(newShape);
            selectCurrentShape(newShape);
        }
    }

    private void addPointAtMouseLocation(){
        if (currentShape == null){
            startNewShape();
        }
        final Vector2 mouse = getMouse();
        currentShape.insert(mouse.x, mouse.y);
        checkNonConvexity(currentShape);
    }


    Color defaultLineColor = Color.WHITE;
    Color defaultPointColor = Color.YELLOW;

    Color selectedLineColor = Color.CYAN;
    Color selectedPointColor = Color.GREEN;

    Color endLineColor = Color.LIGHT_GRAY;
    private void changeShapeColors(@Nullable MShape oldShape, @NotNull MShape newShape){
        if (oldShape != null){
            oldShape.setLineColor(defaultLineColor);
            oldShape.setPointColor(defaultPointColor);
            oldShape.setEndLineColor(endLineColor);
        }

        newShape.setLineColor(selectedLineColor);
        newShape.setPointColor(selectedPointColor);
        newShape.setEndLineColor(endLineColor);
    }


    private void selectCurrentShape(final MShape newShape){
        final MShape oldShape = this.currentShape;
        if (oldShape == newShape){
            return;
        }

        this.currentShape = newShape;

        changeShapeColors(oldShape, newShape);
    }

    @Nullable private Vec draggingPoint = null;

    boolean isDragging(){
        return draggingPoint != null;
    }

    @Override
    public void dragged(float dx, float dy, int button) {
        dx *= cam.zoom;
        dy *= cam.zoom;

        if (draggingPoint != null){
            movePoint(draggingPoint, dx, dy);
        } else {
            moveCamera(-dx, -dy);
        }
    }

    private void movePoint(Vec hoveredPoint, float dx, float dy) {
        hoveredPoint.add(dx, dy);
    }

    private void moveCamera(float dx, float dy){
        cam.position.add(dx, dy, 0);
        cam.position.limit(1000);
    }

    @Override
    public void leftMouseDown(float x, float y) {
        final Vec hoveredPoint = getHoveredPoint(x, y);
        if (hoveredPoint != null){
            draggingPoint = hoveredPoint;
            selectCurrentShape(draggingPoint.getShape());
        }
    }

    @Override
    public void leftMouseUp(float x, float y) {
        if (isDragging()){
            checkNonConvexity(currentShape);
        }
        draggingPoint = null;
    }

    private void checkNonConvexity(@Nullable MShape shape){
        if (shape != null && shape.isNotConvex()){
            getGsm().print("SHAPE BECAME NOT CONVEX!");
        }
    }

    @Override
    public void rightMouseDown(float x, float y) {
        if (isDragging()){
            return;
        }

        deletePointUnder(new Vector2(x, y));
    }

    public void deletePointUnder(Vector2 pos){
        final Vec hoveredPoint = getHoveredPoint(pos.x, pos.y);
        if (hoveredPoint != null){
            final MShape shape = hoveredPoint.getShape();
            shape.remove(hoveredPoint);
            if (shape.size() == 0){
                poly.remove(shape);
                if (currentShape == shape){
                    currentShape = null;
                    if (poly.size() > 0){
                        selectCurrentShape(poly.last());
                    }
                }
            }
        }
    }

    public void snapTwoPointsUnder(Vector2 pos){
        final float snapRange = 20;
        final Array<Vec> inRange = poly.findInRange(pos.x, pos.y, snapRange);
        if (inRange.size >= 2){
            final Vec first = VecUtils.closestIn(inRange, pos.x, pos.y);
            inRange.removeValue(first, true);
            final Vec second = VecUtils.closestIn(inRange, pos.x, pos.y);
            if (first.getShape() != second.getShape()) {
                second.set(first);
            } else {
                getGsm().print("Closest points should be from different shapes!");
            }
        }
    }

    @Override
    public void rightMouseUp(float x, float y) {

    }

    @Override
    public void moved(float x, float y) {

    }

    @Override
    public void zoomChanged(int zoom) {
        float newZoom = cam.zoom + 0.1f * zoom;
        cam.zoom = MathUtils.clamp(newZoom, minZoom, maxZoom);
    }

    public void save(){
        final BodyPoly bodyPoly = new BodyPoly(poly);
        System.out.println(bodyPoly);
        String jsonString  = bodyPoly.toJson();
        final BodyPoly result = BodyPoly.fromJson(jsonString);
        System.out.println(result);

    }
}
