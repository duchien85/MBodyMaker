package ru.maklas.bodymaker.impl;

import ru.maklas.bodymaker.impl.dev_beans.Vec;
import ru.maklas.bodymaker.runtime.save_beans.NamedPoint;

public class DragManager {

    Vec point;

    NamedPoint namedPoint;

    public boolean isDraggingPoint(){
        return point != null;
    }

    public Vec getPoint(){
        return point;
    }

    public void startPointDrag(Vec point){
        this.point = point;
    }

    public void stopPointDrag(){
        this.point = null;
    }





    public boolean isDraggingNamedPoint(){
        return namedPoint != null;
    }

    public NamedPoint getNamedPoint(){
        return namedPoint;
    }

    public void startNamedPointDrag(NamedPoint p){
        this.namedPoint = p;
    }

    public void stopNamedPointDrag(){
        this.namedPoint = null;
    }


    public void stopAllDrag(){
        stopPointDrag();
        stopNamedPointDrag();
    }

}
