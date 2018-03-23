package ru.maklas.bodymaker.impl.view;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.spinner.SimpleFloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import ru.maklas.bodymaker.impl.controllers.FixtureEditorController;

public class FixtureInfoPanel extends VisTable {

    private final Fixture fixture;
    private final FixtureEditorController controller;

    public FixtureInfoPanel(final Fixture fixture, final FixtureEditorController controller) {
        this.fixture = fixture;
        this.controller = controller;

        final SimpleFloatSpinnerModel densityModel = new SimpleFloatSpinnerModel(fixture.getDensity(), 0, 100, 0.05f);
        final SimpleFloatSpinnerModel restitutionModel = new SimpleFloatSpinnerModel(fixture.getRestitution(), 0, 100, 0.05f);
        final SimpleFloatSpinnerModel frictionModel = new SimpleFloatSpinnerModel(fixture.getFriction(), 0, 100, 0.05f);

        Spinner densitySpinner = new Spinner("Density: ", densityModel);
        Spinner restitutionSpinner = new Spinner("Restitution: ", restitutionModel);
        Spinner frictionSpinner = new Spinner("Friction: ", frictionModel);

        densitySpinner.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.densityChanged(fixture, densityModel.getValue());
            }
        });
        restitutionSpinner.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.restitutionChanged(fixture, restitutionModel.getValue());
            }
        });
        frictionSpinner.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.frictionChanged(fixture, frictionModel.getValue());
            }
        });

        add(densitySpinner);
        row();
        add(restitutionSpinner);
        row();
        add(frictionSpinner);
    }
}
