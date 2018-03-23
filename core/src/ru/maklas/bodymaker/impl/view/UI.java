package ru.maklas.bodymaker.impl.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.kotcrab.vis.ui.widget.file.internal.FileListAdapter;
import org.jetbrains.annotations.NotNull;
import ru.maklas.bodymaker.impl.DevState;
import ru.maklas.bodymaker.impl.controllers.FixtureEditorController;
import ru.maklas.bodymaker.impl.controllers.ImageLoadingController;
import ru.maklas.bodymaker.impl.controllers.PolygonEditorController;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class UI extends Stage {


    FileChooser fileSaver;
    private DevState state;
    private ImageLoadingUI imageLoadingTable;
    private PolygonEditorUI polygonEditorTable;
    private FixtureEditorUI fixtureEditorTable;

    public UI(ImageLoadingController imageController, PolygonEditorController polygonEditorController, FixtureEditorController fixtureEditorController) {
        super();
        FileChooser.setDefaultPrefsName("maklas");
        VisUI.load();

        imageLoadingTable = new ImageLoadingUI(imageController);
        polygonEditorTable = new PolygonEditorUI(polygonEditorController);
        fixtureEditorTable = new FixtureEditorUI(fixtureEditorController);


        final FileTypeFilter jsonTypeFilter = new FileTypeFilter(true);
        jsonTypeFilter.addRule("Json files (*.json)", "json", "jsn");
        fileSaver = new FileChooser(FileChooser.Mode.SAVE);
        fileSaver.setFileTypeFilter(jsonTypeFilter);

        addActor(imageLoadingTable);
        state = DevState.ImageLoading;
    }

    public void nameInputWindow(String title, final Array<String> invalidNames, InputDialogListener listener){
        final InputValidator inputValidator = new InputValidator() {
            @Override
            public boolean validateInput(String input) {
                return (!invalidNames.contains(input, false)) &&
                        (!input.equals(""));
            }
        };

        Dialogs.showInputDialog(this, title, "", inputValidator, listener);
    }

    public void changeState(DevState state){
        if (this.state == state){
            return;
        }
        DevState oldState = this.state;
        this.state = state;
        stateChanged(oldState, state);
    }

    public void saveWindow(FileChooserListener listener){
        final float width = Gdx.graphics.getWidth() * 0.6f;
        final float height = Gdx.graphics.getHeight() * 0.9f;
        fileSaver.setMultiSelectionEnabled(false);

        fileSaver.setSize(width, height);
        fileSaver.setPosition(Gdx.graphics.getWidth() / 2 - width/2, Gdx.graphics.getHeight() / 2 - height/2);
        fileSaver.setListener(listener);
        addActor(fileSaver.fadeIn(0.5f));


        try {
            final Field selectedFileTextField = FileChooser.class.getDeclaredField("fileListAdapter");
            selectedFileTextField.setAccessible(true);
            final FileListAdapter fileList = (FileListAdapter) selectedFileTextField.get(fileSaver);
            final FileHandle child = fileSaver.getCurrentDirectory().child("body.json");
            final Constructor<FileChooser.FileItem> constructor = FileChooser.FileItem.class.getConstructor(FileChooser.class, FileHandle.class, FileChooser.ViewMode.class);
            final FileChooser.FileItem item = constructor.newInstance(fileSaver, child, fileSaver.getViewMode());
            fileList.getViews().put(child, item);
            fileSaver.setSelectedFiles(child);

        } catch (Throwable ignore) {
            ignore.printStackTrace();
        }
    }

    private void stateChanged(DevState oldState, DevState newState){
        getTableByState(oldState).remove();
        addActor(getTableByState(newState));
    }

    @NotNull
    private UIScreen getTableByState(DevState state){
        switch (state){
            case ImageLoading:
                return imageLoadingTable;
            case PolygonEditor:
                return polygonEditorTable;
            case FixtureEditor:
                return fixtureEditorTable;
        }
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        VisUI.dispose();
    }

    public void error(IOException e) {
        Dialogs.showErrorDialog(this, "Error: " + e);
    }

}
