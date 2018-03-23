package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import ru.maklas.bodymaker.libs.Utils;

public class BodyMakerInput implements InputProcessor{

    private InputAcceptor acceptor;
    private final OrthographicCamera cam;

    public BodyMakerInput(InputAcceptor acceptor, OrthographicCamera cam) {
        this.acceptor = acceptor;
        this.cam = cam;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        acceptor.keyPressed(keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        final Vector2 mouse = Utils.toScreen(screenX, screenY, cam);
        if (button == Input.Buttons.LEFT) {
            acceptor.leftMouseDown(mouse.x, mouse.y);
        } else if (button == Input.Buttons.RIGHT){
            acceptor.rightMouseDown(mouse.x, mouse.y);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        final Vector2 mouse = Utils.toScreen(screenX, screenY, cam);
        if (button == Input.Buttons.LEFT) {
            acceptor.leftMouseUp(mouse.x, mouse.y);
        } else if (button == Input.Buttons.RIGHT){
            acceptor.rightMouseUp(mouse.x, mouse.y);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        final float widthScale = cam.viewportWidth / Gdx.graphics.getWidth();
        final float heightScale = cam.viewportHeight / Gdx.graphics.getHeight();
        acceptor.dragged(Gdx.input.getDeltaX() * widthScale, -Gdx.input.getDeltaY() * heightScale, pointer);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        acceptor.zoomChanged(amount);
        return false;
    }


    public InputAcceptor getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(InputAcceptor acceptor) {
        this.acceptor = acceptor;
    }
}
