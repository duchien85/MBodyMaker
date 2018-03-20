package ru.maklas.bodymaker.engine;

import com.badlogic.gdx.physics.box2d.Body;
import ru.maklas.mengine.Component;

public class PhysicsComponent implements Component{

    public Body body;

    public PhysicsComponent(Body body) {
        this.body = body;
    }


}
