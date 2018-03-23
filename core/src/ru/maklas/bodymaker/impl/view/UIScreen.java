package ru.maklas.bodymaker.impl.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class UIScreen extends VisTable{

    public UIScreen() {
        setFillParent(true);
        right().top();
    }

    protected final VisLabel tableWithText(String text){
        final VisLabel label = new VisLabel(text);
        Pixmap pixmap = new Pixmap((int) label.getWidth(), (int) label.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(0.2f, 0.2f, 0.2f, 0.45f);
        pixmap.fill();
        label.getStyle().background = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        return label;
    }

    protected final float chooserWidth(){
        return Gdx.graphics.getWidth() * 0.6f;
    }

    protected final float chooserHeight(){
        return Gdx.graphics.getHeight() * 0.9f;
    }
}
