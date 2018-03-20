package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import ru.maklas.bodymaker.libs.Utils;

public class BodyMakerInput implements InputProcessor{

    private final InputAcceptor acceptor;
    private final OrthographicCamera cam;

    public BodyMakerInput(InputAcceptor acceptor, OrthographicCamera cam) {
        this.acceptor = acceptor;
        this.cam = cam;
    }

    @Override
    public boolean keyDown(int keycode) {
        acceptor.keyPressed(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        final Vector2 mouse = Utils.toScreen(screenX, screenY, cam);
        acceptor.clicked(mouse.x, mouse.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    Vector2 previousPos = new Vector2();
    Vector2 currentPos = new Vector2();

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        updateMovement();
        final Vector2 dt = Utils.vec1.set(currentPos).sub(previousPos);
        acceptor.dragged(dt.x, dt.y);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        updateMovement();
        return false;
    }

    private void updateMovement(){
        previousPos.set(currentPos);
        currentPos.set(Utils.toScreen(Gdx.input.getX(), Gdx.input.getY(), cam));
    }

    @Override
    public boolean scrolled(int amount) {
        acceptor.zoomChanged(amount);
        return false;
    }
}
