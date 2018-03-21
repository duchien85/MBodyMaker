package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import ru.maklas.bodymaker.engine.PhysicsComponent;
import ru.maklas.bodymaker.impl.dev_beans.MShape;
import ru.maklas.bodymaker.impl.dev_beans.Vec;
import ru.maklas.bodymaker.impl.save_beans.BodyPoly;
import ru.maklas.bodymaker.impl.save_beans.NamedPoint;
import ru.maklas.mengine.ComponentMapper;

import java.io.IOException;
import java.io.Writer;

public class PointCreationState extends CreationState{

    public PointCreationState(Model model) {
        super(model);
    }


    @Override
    public void onEnterState(DevState oldState) {
        addBodyToEntity();
        model.polyRenderer.setRenderNamedPoints(true);
        model.polyRenderer.setRenderLines(false);
        resetShapeColors();
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
    public void keyPressed(int key) {
        if (model.ui.haveAnyDialogsOpen()){
            return;
        }
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
        if (hoverPoint != null && hoverPoint.dst(x, y) < model.maxDstToHighlight) {
            model.drag.startNamedPointDrag(hoverPoint);
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

                final BodyPoly bodyPoly = new BodyPoly(model.poly);
                System.out.println(bodyPoly);
                String jsonString  = bodyPoly.toJson();
                final BodyPoly result = BodyPoly.fromJson(jsonString);
                System.out.println(result);

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
        if (hovered != null){
            model.poly.remove(hovered);
        }
    }

    private void addNamedPointAtMouseLocation() {
        Array<String> namedPointNames = getNamedPointNames();
        final Vector2 mouse = new Vector2(getMouse());

        model.ui.nameWindow("Enter name for point: ",
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
        model.entity.add(new PhysicsComponent(body));
    }

    ComponentMapper<PhysicsComponent> mapper = ComponentMapper.of(PhysicsComponent.class);
    private void createCenterOfMassPoint(){
        final String mass_center = "Mass center";

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
        final String origin_name = "Origin";

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

}
