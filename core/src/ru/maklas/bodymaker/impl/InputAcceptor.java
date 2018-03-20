package ru.maklas.bodymaker.impl;

public interface InputAcceptor {

    void keyPressed(int key);

    void dragged(float dx, float dy, int button);

    void leftMouseDown(float x, float y);

    void leftMouseUp(float x, float y);

    void rightMouseDown(float x, float y);

    void rightMouseUp(float x, float y);

    void moved(float x, float y);

    void zoomChanged(int zoom);

}
