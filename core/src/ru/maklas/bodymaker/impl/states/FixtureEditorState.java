package ru.maklas.bodymaker.impl.states;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.engine.PhysicsComponent;
import ru.maklas.bodymaker.impl.DevState;
import ru.maklas.bodymaker.impl.Model;
import ru.maklas.bodymaker.impl.controllers.BodyEditorController;
import ru.maklas.bodymaker.impl.controllers.FixtureEditorController;
import ru.maklas.bodymaker.impl.dev_beans.MShape;
import ru.maklas.bodymaker.impl.dev_beans.Vec;
import ru.maklas.bodymaker.impl.save_beans.BodyPoly;
import ru.maklas.bodymaker.impl.save_beans.NamedPoint;
import ru.maklas.mengine.ComponentMapper;

import java.io.IOException;
import java.io.Writer;

public class FixtureEditorState extends CreationState implements FixtureEditorController, BodyEditorController {

    public FixtureEditorState(Model model) {
        super(model);
    }


    @Override
    public void onEnterState(DevState oldState) {
        addBodyToEntity();
        model.polyRenderer.setRenderNamedPoints(true);
        model.polyRenderer.setRenderLines(false);
        resetShapeColors();
        createCenterOfMassPoint();
        createOriginPoint();
        model.ui.getFixtureView().getBodyView().setMass(model.body.getMassData().mass);
    }

    @Override
    public void onLeaveState(DevState newState) {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        model.engine.render();
        batch.end();

        model.debug.update(0);

        model.polyRenderer.render(model.poly);

        final Vec vec = getHoveredPointShapesOnly(getMouse());
        if (vec != null){
            model.polyRenderer.renderPoint(vec.x, vec.y, Color.CORAL);
        }
        model.ui.draw();
    }

    @Override
    public void keyDown(int key) {
        switch (key) {
            case Input.Keys.A: addNamedPointAtMouseLocation();
                break;
            case Input.Keys.D: removeNamedPointAt(getMouse());
                break;
            case Input.Keys.C: createOriginPoint(); createCenterOfMassPoint();
                break;
            case Input.Keys.Q: save();
                break;
        }
    }


    @Override
    public void dragged(float dx, float dy, int button) {
        dx *= model.cam.zoom;
        dy *= model.cam.zoom;

        if (model.drag.isDraggingNamedPoint()){
            model.drag.getNamedPoint().add(dx, dy);
        } else {
            moveCamera(-dx, -dy);
        }
    }

    @Override
    public void leftMouseDown(float x, float y) {
        model.drag.stopAllDrag();
        final NamedPoint hoverPoint = getHoveredPointNamedOnly(x, y);
        if (hoverPoint != null && hoverPoint.dst(x, y) < model.maxDstToHighlight && pointCanBeChanged(hoverPoint)) {
            model.drag.startNamedPointDrag(hoverPoint);
        } else {
            final Fixture fixture = testFixture(x, y);
            selectCurrentFixture(fixture);
        }
    }

    @Override
    public void leftMouseUp(float x, float y) {
        model.drag.stopAllDrag();
    }

    public void save(){
        model.ui.saveWindow(new FileChooserAdapter(){
            @Override
            public void selected(Array<FileHandle> files) {
                if (files.size < 1){
                    return;
                }

                FileHandle file = files.get(0);

                final BodyPoly bodyPoly = model.poly.toBeans();
                System.out.println(new Json().prettyPrint(bodyPoly.toJson()));
                String jsonString  = bodyPoly.toJson();
                final BodyPoly result = BodyPoly.fromJson(jsonString);

                final Writer writer = file.writer(false, "UTF-8");
                try {
                    writer.write(jsonString);
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    model.ui.error(e);
                }
            }
        });
    }







    public Array<String> getNamedPointNames() {
        final Array<String> names = new Array<String>();
        for (NamedPoint namedPoint : model.poly.getNamedPoints()) {
            names.add(namedPoint.getName());
        }

        return names;
    }

    private void removeNamedPointAt(Vector2 mouse) {
        final NamedPoint hovered = getHoveredPointNamedOnly(mouse);
        if (hovered != null && pointCanBeChanged(hovered)){
            model.poly.remove(hovered);
        }
    }

    private boolean pointCanBeChanged(NamedPoint point) {
        return !point.getName().equals(BodyPoly.MASS_CENTER) && !point.getName().equals(BodyPoly.ORIGIN);
    }

    private void addNamedPointAtMouseLocation() {
        Array<String> namedPointNames = getNamedPointNames();
        final Vector2 mouse = new Vector2(getMouse());

        model.ui.nameInputWindow("Enter name for the point",
                namedPointNames,
                new InputDialogAdapter(){
                    @Override
                    public void finished(String name) {
                        model.poly.addNamedPoint(name, mouse.x, mouse.y);
                    }
                });
    }

    private void addBodyToEntity(){
        Body body = model.poly.createBody(model.world);
        model.body = body;
        model.entity.add(new PhysicsComponent(body));
    }

    ComponentMapper<PhysicsComponent> mapper = ComponentMapper.of(PhysicsComponent.class);
    private void createCenterOfMassPoint(){
        final String mass_center = BodyPoly.MASS_CENTER;

        final Body body = model.entity.get(mapper).body;
        Vector2 center = body.getWorldPoint(body.getMassData().center);
        final Array<NamedPoint> namedPoints = model.poly.getNamedPoints();
        for (NamedPoint namedPoint : namedPoints) {
            if (namedPoint.getName().equals(mass_center)){
                namedPoint.set(center);
                return;
            }
        }

        model.poly.addNamedPoint(mass_center, center.x, center.y);
    }

    private void createOriginPoint(){
        final String origin_name = BodyPoly.ORIGIN;

        final Array<NamedPoint> namedPoints = model.poly.getNamedPoints();
        Vector2 center = model.entity.get(mapper).body.getPosition();

        for (NamedPoint namedPoint : namedPoints) {
            if (namedPoint.getName().equals(origin_name)){
                namedPoint.set(center);
                return;
            }
        }

        model.poly.addNamedPoint(origin_name, center.x, center.y);
    }

    private void resetShapeColors(){
        for (MShape shape : model.poly) {
            shape.setLineColor(model.defaultLineColor);
            shape.setPointColor(model.defaultPointColor);
            shape.setEndLineColor(model.defaultEndLineColor);
        }
    }


    private Fixture testFixture(float x, float y){
        final Array<Fixture> fixtureList = model.body.getFixtureList();
        for (Fixture fixture : fixtureList) {
            if (fixture.testPoint(x, y)){
                return fixture;
            }
        }

        return null;
    }

    private void selectCurrentFixture(@Nullable Fixture fixture) {
        model.currentFixture = fixture;
        if (fixture == null) {
            model.ui.getFixtureView().hideFixtureData();
        } else {
            model.ui.getFixtureView().showFixtureData(fixture);
        }
    }

    private void updateMassData() {
        model.body.resetMassData();
        model.ui.getFixtureView().getBodyView().setMass(model.body.getMassData().mass / (scale * scale));
        createCenterOfMassPoint();
    }

    @Override
    public void densityChanged(Fixture f, float density) {
        f.setDensity(density);
        updateMassData();
    }

    @Override
    public void restitutionChanged(Fixture f, float restitution) {
        f.setRestitution(restitution);
    }

    @Override
    public void frictionChanged(Fixture f, float friction) {
        f.setFriction(friction);
    }

    @Override
    public void sensorChanged(Fixture f, boolean isSensor) {
        f.setSensor(isSensor);
    }

    float scale = 1;

    @Override
    public void scaleChanged(float scale) {
        this.scale = scale;
        updateMassData();
    }
}
