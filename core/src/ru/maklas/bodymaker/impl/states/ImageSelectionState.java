package ru.maklas.bodymaker.impl.states;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.maklas.bodymaker.engine.rendering.RenderComponent;
import ru.maklas.bodymaker.engine.rendering.RenderUnit;
import ru.maklas.bodymaker.impl.DevState;
import ru.maklas.bodymaker.impl.Model;
import ru.maklas.bodymaker.impl.controllers.ImageLoadingController;

public class ImageSelectionState extends CreationState implements ImageLoadingController {

    public ImageSelectionState(Model model) {
        super(model);
    }

    @Override
    public void onEnterState(DevState oldState) {
        model.ui.changeState(DevState.ImageLoading);
        model.drag.stopAllDrag();
    }

    @Override
    public void imageSelected(FileHandle fileHandle) {
        final TextureRegion region = model.loader.loadFromFile(fileHandle);
        if (region == null){
            model.gsm.print("Bad file: " + fileHandle.name());
            return;
        }
        RenderUnit ru = new RenderUnit(region);
        ru.pivotX = ru.pivotY = 0;
        final RenderComponent rc = new RenderComponent(ru);
        model.entity.add(rc);
        moveCamera(ru.width/2, ru.height/2);

        changeState(DevState.PolygonEditor);
    }


    @Override
    public void onLeaveState(DevState newState) {
        model.drag.stopAllDrag();
    }

    @Override
    public void render(SpriteBatch batch) {
        model.ui.draw();
    }
}
