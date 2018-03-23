package ru.maklas.bodymaker.impl.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import ru.maklas.bodymaker.impl.controllers.PolygonEditorController;

public class PolygonEditorUI extends UIScreen {


    public PolygonEditorUI(final PolygonEditorController controller) {
        super();


        String text =
                "A - Add new Point in shape" + '\n' +
                        "D - Delete Point" + '\n' +
                        "N - Create new Shape" + '\n' +
                        "S - Snap two points together" + '\n' +
                        "Q - Next step";
        add(tableWithText(text));


        final FileTypeFilter jsonTypeFilter = new FileTypeFilter(true);
        jsonTypeFilter.addRule("Json files (*.json)", "json", "jsn");

        final FileChooser jsonChooser = new FileChooser(FileChooser.Mode.OPEN);
        jsonChooser.setFileTypeFilter(jsonTypeFilter);
        jsonChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        jsonChooser.setMultiSelectionEnabled(false);
        final FileChooserAdapter jsonChooserListener = new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                if (files.size < 1) {
                    return;
                }

                final FileHandle fileHandle = files.get(0);
                controller.jsonSelected(fileHandle);
            }
        };
        jsonChooser.setListener(jsonChooserListener);


        VisTextButton openJsonButton = new VisTextButton("Load");
        openJsonButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                jsonChooser.setSize(chooserWidth(), chooserHeight());
                jsonChooser.setPosition(Gdx.graphics.getWidth() / 2 - chooserWidth()/2, Gdx.graphics.getHeight() / 2 - chooserHeight()/2);
                addActor(jsonChooser.fadeIn(0.5f));
            }
        });
        row();
        add(openJsonButton);
        
    }
}
