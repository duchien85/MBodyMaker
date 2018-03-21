package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.impl.dev_beans.MShape;
import ru.maklas.bodymaker.impl.dev_beans.Vec;
import ru.maklas.bodymaker.impl.dev_beans.VecUtils;

public class PolygonEditorState extends CreationState {

    public PolygonEditorState(Model model) {
        super(model);
    }


    @Override
    public void onEnterState(DevState oldState) {

    }

    @Override
    public void onLeaveState(DevState newState) {

    }

    @Override
    public void keyPressed(int key) {
        if (model.ui.haveAnyDialogsOpen()){
            return;
        }
        switch (key){
            case Input.Keys.A : addShapePointAtLocation(getMouse());
                break;
            case Input.Keys.D : deleteShapePointUnder(getMouse());
                break;
            case Input.Keys.S : snapTwoPointsUnder(getMouse());
                break;
            case Input.Keys.N : startNewShape();
                break;
            case Input.Keys.Q : changeState(DevState.PointsAndSave);
                break;
        }

    }

    @Override
    public void dragged(float dx, float dy, int button) {
        dx *= model.cam.zoom;
        dy *= model.cam.zoom;

        if (model.drag.isDraggingPoint()){
            model.drag.getPoint().add(dx, dy);
        } else {
            moveCamera(-dx, -dy);
        }
    }

    @Override
    public void leftMouseDown(float x, float y) {
        model.drag.stopAllDrag();
        final Vec hoverPoint = getHoveredPointShapesOnly(x, y);
        if (hoverPoint != null) {
            selectCurrentShape(hoverPoint.getShape());
            model.drag.startPointDrag(hoverPoint);
        }
    }

    @Override
    public void leftMouseUp(float x, float y) {
        model.drag.stopAllDrag();
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



    private void startNewShape() {
        if (model.currentShape == null || model.currentShape.size() != 0){
            final Vector2 mouse = new Vector2(getMouse());

            model.ui.nameWindow("Enter name for new shape",
                    getInvalidNamesForShapes(),
                    new InputDialogAdapter(){
                        @Override
                        public void finished(String name) {
                            MShape newShape = new MShape(name);
                            model.poly.add(newShape);
                            selectCurrentShape(newShape);

                            addShapePointAtLocation(mouse);
                        }
                    });
        }
    }



    private void addShapePointAtLocation(Vector2 pos){
        if (model.currentShape == null){
            startNewShape();
            return;
        }
        model.currentShape.insert(pos.x, pos.y);
        checkNonConvexity(model.currentShape);
    }


    private void selectCurrentShape(final MShape newShape){
        final MShape oldShape = model.currentShape;
        if (oldShape == newShape){
            return;
        }

        model.currentShape = newShape;

        changeShapeColors(oldShape, newShape);
    }


    private void changeShapeColors(@Nullable MShape oldShape, @NotNull MShape newShape){
        if (oldShape != null){
            oldShape.setLineColor(model.defaultLineColor);
            oldShape.setPointColor(model.defaultPointColor);
            oldShape.setEndLineColor(model.defaultEndLineColor);
        }

        newShape.setLineColor(model.selectedLineColor);
        newShape.setPointColor(model.selectedPointColor);
        newShape.setEndLineColor(model.selectedEndLineColor);
    }




    public void deleteShapePointUnder(Vector2 pos){
        final Vec hoveredPoint = getHoveredPointShapesOnly(pos.x, pos.y);
        if (hoveredPoint != null){
            final MShape shape = hoveredPoint.getShape();
            shape.remove(hoveredPoint);
            if (shape.size() == 0){
                model.poly.remove(shape);
                if (model.currentShape == shape){
                    model.currentShape = null;
                    if (model.poly.size() > 0){
                        selectCurrentShape(model.poly.last());
                    }
                }
            }
        }
    }

    public void snapTwoPointsUnder(Vector2 pos){
        final float snapRange = 20;
        final Array<Vec> inRange = model.poly.findInRange(pos.x, pos.y, snapRange);
        if (inRange.size >= 2){
            final Vec first = VecUtils.closestIn(inRange, pos.x, pos.y);
            inRange.removeValue(first, true);
            final Vec second = VecUtils.closestIn(inRange, pos.x, pos.y);
            if (first.getShape() != second.getShape()) {
                second.set(first);
            } else {
                model.gsm.print("Closest points should be from different shapes!");
            }
        }
    }



    public Array<String> getInvalidNamesForShapes() {
        Array<String> names = new Array<String>();
        for (MShape shape : model.poly) {
            names.add(shape.getName());
        }

        return names;
    }

}
