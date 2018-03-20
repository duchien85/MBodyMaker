package ru.maklas.bodymaker.engine.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import ru.maklas.mengine.components.IRenderComponent;

/**
 * @author maklas. Created on 26.04.2017.
 */

public class RenderComponent implements IRenderComponent, Pool.Poolable{

    public Array<RenderUnit> renderUnits = new Array<RenderUnit>(2);
    public Color color = Color.WHITE;

    public RenderComponent(TextureRegion textureRegion) {
        renderUnits.add(new RenderUnit(textureRegion));
    }

    public RenderComponent(RenderUnit renderUnit) {
        renderUnits.add(renderUnit);
    }

    public RenderComponent(Array<RenderUnit> renderUnits) {
        this.renderUnits = renderUnits;
    }

    public RenderComponent() {

    }

    public RenderComponent add(RenderUnit renderUnit){
        renderUnits.add(renderUnit);
        return this;
    }

    public RenderUnit getRenderUnitByName(String name){
        for(RenderUnit r:renderUnits){
            if (r.name.equals(name)) return r;
        }
        return null;
    }

    @Override
    public void reset() {
        renderUnits.clear();
        color = Color.WHITE;
    }
}
