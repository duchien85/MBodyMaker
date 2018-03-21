package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.engine.PhysicsDebugSystem;
import ru.maklas.bodymaker.engine.rendering.RenderUnit;
import ru.maklas.bodymaker.impl.dev_beans.MPoly;
import ru.maklas.bodymaker.impl.dev_beans.MShape;
import ru.maklas.bodymaker.libs.gsm_lib.GameStateManager;
import ru.maklas.mengine.Engine;
import ru.maklas.mengine.Entity;

public class Model {

    public OrthographicCamera cam;
    public BodyMakerInput bodyMakerInput;
    public UI ui;
    public InputMultiplexer input;
    public Engine engine;
    public World world;
    public PhysicsDebugSystem debug;
    public DefaultPolyRenderer polyRenderer;
    public RuntimeTextureLoader loader;
    public GameStateManager gsm;
    public ShapeRenderer shapeRenderer;
    public DragManager drag;

    public MPoly poly;
    @Nullable public MShape currentShape;
    public Entity entity;
    @Nullable public RenderUnit ru;

    public DevState currentStateEnum;
    public CreationState imaging;
    public CreationState polyChange;
    public CreationState pointCreationAndSave;
    public CreationState currentState;


    Color defaultLineColor = Color.WHITE;
    Color defaultPointColor = Color.YELLOW;

    Color selectedLineColor = Color.CYAN;
    Color selectedPointColor = Color.GREEN;

    Color defaultEndLineColor = Color.WHITE;
    Color selectedEndLineColor = Color.LIGHT_GRAY;


    public float maxZoom = 4f;
    public float minZoom = 0.1f;
    public float maxDstToHighlight = 8;


    CreationState getCreationState(DevState state){
        switch (state){
            case Image:
                return imaging;
            case Poly:
                return polyChange;
            case PointsAndSave:
                return pointCreationAndSave;
        }
        return null;
    }

}
