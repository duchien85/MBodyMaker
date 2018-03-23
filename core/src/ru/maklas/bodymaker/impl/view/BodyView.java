package ru.maklas.bodymaker.impl.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.spinner.SimpleFloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import ru.maklas.bodymaker.impl.controllers.BodyEditorController;
import ru.maklas.bodymaker.libs.Utils;

public class BodyView extends VisTable {

    final VisLabel bodyMassLabel;
    private final BodyEditorController controller;

    public BodyView(final BodyEditorController controller) {
        super();
        left();
        this.controller = controller;

        bodyMassLabel = new VisLabel("Body mass: " + 0);

        final SimpleFloatSpinnerModel scaleModel;
        scaleModel = new SimpleFloatSpinnerModel(1, 0, 150, 0.05f);
        Spinner spinner = new Spinner("World scale: ", scaleModel);
        add(spinner).left();
        row();
        add(bodyMassLabel).left();
        spinner.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.scaleChanged(scaleModel.getValue());
            }
        });
    }

    public void setMass(float mass) {
        this.bodyMassLabel.setText("Body mass: " + Utils.floatFormatted(mass, 2));
    }
}
