package ru.maklas.bodymaker.impl.view;

import com.badlogic.gdx.physics.box2d.Fixture;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.impl.controllers.BodyEditorController;
import ru.maklas.bodymaker.impl.controllers.FixtureEditorController;

import java.util.HashMap;
import java.util.Map;

public class FixtureEditorUI extends UIScreen {

    private final FixtureEditorController controller;
    private BodyView bodyView;
    private Map<Fixture, FixtureInfoPanel> map = new HashMap<Fixture, FixtureInfoPanel>();
    @Nullable FixtureInfoPanel current = null;

    public FixtureEditorUI(final FixtureEditorController controller, BodyEditorController bodyController) {
        super();
        this.controller = controller;

        bodyView = new BodyView(bodyController);


        String text =
                "A - Add new Point in Polygon" + '\n' +
                        "D - Delete Point" + '\n' +
                        "C - Add origin and Center of mass" + '\n' +
                        "Q - Save";
        add(tableWithText(text));
        row();
        add(bodyView);
        row();
    }


    public void hideFixtureData() {
        if (current != null){
            current.remove();
            current = null;
        }
    }

    public void showFixtureData(Fixture fixture) {
        FixtureInfoPanel panel = map.get(fixture);
        if (panel == null){
            panel = new FixtureInfoPanel(fixture, controller);
            map.put(fixture, panel);
        }
        if (current != null){
            current.remove();
        }

        current = panel;
        add(panel);
    }

    public BodyView getBodyView() {
        return bodyView;
    }
}
