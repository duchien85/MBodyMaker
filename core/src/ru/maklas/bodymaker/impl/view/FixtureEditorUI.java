package ru.maklas.bodymaker.impl.view;

import ru.maklas.bodymaker.impl.controllers.FixtureEditorController;

public class FixtureEditorUI extends UIScreen {

    public FixtureEditorUI(final FixtureEditorController controller) {
        super();

        String text =
                "A - Add new Point in Polygon" + '\n' +
                        "D - Delete Point" + '\n' +
                        "C - Add origin and Center of mass" + '\n' +
                        "Q - Save";
        add(tableWithText(text));
    }


}
