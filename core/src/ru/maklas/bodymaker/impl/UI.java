package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogListener;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileChooserListener;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import com.kotcrab.vis.ui.widget.file.internal.FileListAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class UI extends Stage {


    private UIController controller;
    private DevState state = DevState.Image;
    private VisTable polyTable;
    private VisTable pointsTable;

    //Image
    private VisTable imageTable;
    VisTextButton openImageButton;
    private int windowCounter = 0;

    final FileChooser chooser;
    FileTypeFilter imageTypeFilter;
    FileTypeFilter jsonTypeFilter;

    public UI(final UIController controller) {
        super();
        this.controller = controller;
        VisUI.load();


        FileChooser.setDefaultPrefsName("maklas");
        chooser = new FileChooser(FileChooser.Mode.OPEN);

        createImageTable();
        createPolyTable();
        createPointsTable();

        addActor(imageTable);
    }

    public UIController getController() {
        return controller;
    }

    public void setController(UIController controller) {
        this.controller = controller;
    }

    private void createImageTable() {

        imageTable = new VisTable();
        imageTable.setFillParent(true);
        openImageButton = new VisTextButton("Open image");
        imageTable.add(openImageButton);
        imageTable.right().top();


        imageTypeFilter = new FileTypeFilter(true);
        imageTypeFilter.addRule("Image files (*.png, *.jpg, *.gif)", "png", "jpg", "gif");
        jsonTypeFilter = new FileTypeFilter(true);
        jsonTypeFilter.addRule("Json (*.json)", "json", "jsn");

        fileSaver = new FileChooser(FileChooser.Mode.SAVE);
        fileSaver.setFileTypeFilter(jsonTypeFilter);
        final float width = Gdx.graphics.getWidth() * 0.6f;
        final float height = Gdx.graphics.getHeight() * 0.9f;
        chooser.setFileTypeFilter(imageTypeFilter);
        chooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        chooser.setMultiSelectionEnabled(false);
        final FileChooserAdapter imageChooserListener = new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                if (files.size < 1) {
                    return;
                }

                final FileHandle fileHandle = files.get(0);
                controller.fileSelected(fileHandle);
            }
        };
        chooser.setListener(wrap(imageChooserListener));

        openImageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setFileTypeFilter(imageTypeFilter);
                chooser.setSize(width, height);
                chooser.setPosition(Gdx.graphics.getWidth() / 2 - width/2, Gdx.graphics.getHeight() / 2 - height/2);
                windowCounter++;
                addActor(chooser.fadeIn(0.5f));
                String desktopPath = System.getProperty("user.home") + "/Desktop";
                chooser.setDirectory(desktopPath);
            }
        });
    }

    private void createPolyTable() {
        polyTable = new VisTable();
        polyTable.setFillParent(true);
        polyTable.right().top();

        String text =
                "A - Add new Point in shape" + '\n' +
                "D - Delete Point" + '\n' +
                "N - Create new Shape" + '\n' +
                "S - Snap two points together" + '\n' +
                "Q - Next step";
        polyTable.add(tableWithText(text));

        final float width = Gdx.graphics.getWidth() * 0.6f;
        final float height = Gdx.graphics.getHeight() * 0.9f;

        VisTextButton openJsonButton = new VisTextButton("Load");
        openJsonButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                chooser.setFileTypeFilter(jsonTypeFilter);
                chooser.setSize(width, height);
                chooser.setPosition(Gdx.graphics.getWidth() / 2 - width/2, Gdx.graphics.getHeight() / 2 - height/2);
                windowCounter++;
                addActor(chooser.fadeIn(0.5f));
                String desktopPath = System.getProperty("user.home") + "/Desktop";
                chooser.setDirectory(desktopPath);
            }
        });
        polyTable.row();
        polyTable.add(openJsonButton);
    }

    private void createPointsTable() {
        pointsTable = new VisTable();
        pointsTable.setFillParent(true);
        pointsTable.right().top();


        String text =
                "A - Add new Point in Polygon" + '\n' +
                "D - Delete Point" + '\n' +
                "C - Add origin and Center of mass" + '\n' +
                "Q - Save";
        pointsTable.add(tableWithText(text));
    }

    private VisLabel tableWithText(String text){
        final VisLabel label = new VisLabel(text);
        Pixmap pixmap = new Pixmap((int) label.getWidth(), (int) label.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(0.2f, 0.2f, 0.2f, 0.45f);
        pixmap.fill();
        label.getStyle().background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        return label;
    }


    public void nameWindow(String title, final Array<String> invalidNames, InputDialogListener listener){
        final InputValidator inputValidator = new InputValidator() {
            @Override
            public boolean validateInput(String input) {
                return (!invalidNames.contains(input, false)) &&
                        (!input.equals(""));
            }
        };

        windowCounter++;
        Dialogs.showInputDialog(this, title, "", inputValidator, wrap(listener));
    }

    public void changeState(DevState state){
        if (this.state == state){
            return;
        }
        DevState oldState = this.state;
        this.state = state;
        stateChanged(oldState, state);
    }

    FileChooser fileSaver;
    public void saveWindow(FileChooserListener listener){
        final float width = Gdx.graphics.getWidth() * 0.6f;
        final float height = Gdx.graphics.getHeight() * 0.9f;
        fileSaver.setMultiSelectionEnabled(false);

        fileSaver.setSize(width, height);
        fileSaver.setPosition(Gdx.graphics.getWidth() / 2 - width/2, Gdx.graphics.getHeight() / 2 - height/2);
        windowCounter++;
        fileSaver.setListener(wrap(listener));
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
    private VisTable getTableByState(DevState state){
        switch (state){
            case Image:
                return imageTable;
            case Poly:
                return polyTable;
            case PointsAndSave:
                return pointsTable;
        }
        return null;
    }

    @Override
    public void dispose() {
        super.dispose();
        VisUI.dispose();
    }

    public boolean haveAnyDialogsOpen(){
        return windowCounter > 0;
    }

    public void error(IOException e) {
        Dialogs.showErrorDialog(this, "Error: " + e);
    }

    private FileChooserListener wrap(final FileChooserListener l){
        return new FileChooserListener(){
            @Override
            public void selected(Array<FileHandle> files) {
                windowCounter--;
                l.selected(files);
            }

            @Override
            public void canceled() {
                windowCounter--;
                l.canceled();
            }
        };
    }

    private InputDialogListener wrap(final InputDialogListener l){
        return new InputDialogListener() {
            @Override
            public void finished(String input) {
                windowCounter--;
                l.finished(input);
            }

            @Override
            public void canceled() {
                windowCounter--;
                l.canceled();
            }
        };
    }
}
