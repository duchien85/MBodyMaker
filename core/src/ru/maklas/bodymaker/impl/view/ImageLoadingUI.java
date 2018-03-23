package ru.maklas.bodymaker.impl.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;
import ru.maklas.bodymaker.impl.controllers.ImageLoadingController;

public class ImageLoadingUI extends UIScreen {

    public ImageLoadingUI(final ImageLoadingController controller) {
        super();
        setFillParent(true);
        right().top();

        VisTextButton openImageButton = new VisTextButton("Open image");
        add(openImageButton);

        final FileTypeFilter imageTypeFilter = new FileTypeFilter(true);
        imageTypeFilter.addRule("Image files (*.png, *.jpg, *.gif)", "png", "jpg", "gif");


        final FileChooser imageChooser = new FileChooser(FileChooser.Mode.OPEN);
        imageChooser.setFileTypeFilter(imageTypeFilter);
        imageChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        imageChooser.setMultiSelectionEnabled(false);
        final FileChooserAdapter imageChooserListener = new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                if (files.size < 1) {
                    return;
                }

                final FileHandle fileHandle = files.get(0);
                controller.imageSelected(fileHandle);
            }
        };
        imageChooser.setListener(imageChooserListener);

        openImageButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                imageChooser.setSize(chooserWidth(), chooserHeight());
                imageChooser.setPosition(Gdx.graphics.getWidth() / 2 - chooserWidth()/2, Gdx.graphics.getHeight() / 2 - chooserHeight()/2);
                addActor(imageChooser.fadeIn(0.5f));
            }
        });

    }
}
