package ru.maklas.bodymaker.impl.controllers;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface FixtureEditorController {

    void densityChanged(Fixture f, float density);

    void restitutionChanged(Fixture f, float restitution);

    void frictionChanged(Fixture f, float friction);

}
