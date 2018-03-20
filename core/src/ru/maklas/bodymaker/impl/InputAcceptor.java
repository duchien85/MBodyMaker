package ru.maklas.bodymaker.impl;

public interface InputAcceptor {

    void keyPressed(int key);

    void dragged(float dx, float dy);

    void clicked(float x, float y);

    void moved(float x, float y);

    void zoomChanged(int zoom);

}
