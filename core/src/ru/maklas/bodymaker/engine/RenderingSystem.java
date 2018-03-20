package ru.maklas.bodymaker.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ru.maklas.bodymaker.engine.rendering.RenderComponent;
import ru.maklas.bodymaker.engine.rendering.RenderUnit;
import ru.maklas.bodymaker.libs.Utils;
import ru.maklas.mengine.Entity;
import ru.maklas.mengine.components.IRenderComponent;
import ru.maklas.mengine.systems.IterableZSortedRenderSystem;

public class RenderingSystem extends IterableZSortedRenderSystem {

    private SpriteBatch batch;
    private OrthographicCamera cam;

    public RenderingSystem(SpriteBatch batch, OrthographicCamera cam) {
        super(RenderComponent.class);
        this.batch = batch;
        this.cam = cam;
        setEnabled(false);
    }


    @Override
    protected void renderStarted() {
        cam.update();
        batch.setProjectionMatrix(cam.combined);
    }

    @Override
    protected void renderEntity(Entity entity, IRenderComponent iRenderComponent) {
        Vector2 tempVec = Utils.vec1;
        RenderComponent rc = (RenderComponent) iRenderComponent;

        batch.setColor(rc.color);

        for (RenderUnit ru : rc.renderUnits) {

            float originX = ru.width * ru.pivotX;
            float originY = ru.height * ru.pivotY;

            tempVec.set(ru.localX, ru.localY);
            tempVec.rotate(entity.getAngle());

            batch.draw(ru.region,
                    entity.x - originX + tempVec.x, entity.y - originY + tempVec.y,
                    originX, originY,
                    ru.width, ru.height,
                    ru.flipX ? -ru.scaleX: ru.scaleX, ru.scaleY,
                    entity.getAngle() + ru.angle);
        }
    }

    @Override
    protected void renderFinished() {
        batch.setColor(Color.WHITE);
    }

    public OrthographicCamera getCamera() {
        return cam;
    }
}
