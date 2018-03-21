package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ru.maklas.bodymaker.engine.rendering.RenderComponent;
import ru.maklas.bodymaker.engine.rendering.RenderUnit;
import ru.maklas.bodymaker.impl.dev_beans.Vec;

public class ImageSelectionState extends CreationState {

    public ImageSelectionState(Model model) {
        super(model);
    }

    @Override
    public void onEnterState(DevState oldState) {
        model.ui.changeState(DevState.Image);
        model.drag.stopAllDrag();
    }

    @Override
    public void fileSelected(FileHandle fileHandle) {
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

        changeState(DevState.Poly);
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
