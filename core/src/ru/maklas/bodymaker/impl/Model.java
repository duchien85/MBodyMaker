package ru.maklas.bodymaker.impl;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import org.jetbrains.annotations.Nullable;
import ru.maklas.bodymaker.engine.PhysicsDebugSystem;
import ru.maklas.bodymaker.engine.rendering.RenderUnit;
import ru.maklas.bodymaker.impl.dev_beans.MPoly;
import ru.maklas.bodymaker.impl.dev_beans.MShape;
import ru.maklas.bodymaker.impl.states.CreationState;
import ru.maklas.bodymaker.impl.states.FixtureEditorState;
import ru.maklas.bodymaker.impl.states.ImageSelectionState;
import ru.maklas.bodymaker.impl.states.PolygonEditorState;
import ru.maklas.bodymaker.impl.view.UI;
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
    @Nullable public Fixture currentFixture;
    public Entity entity;
    public Body body;
    @Nullable public RenderUnit ru;

    public DevState currentStateEnum;
    public ImageSelectionState imaging;
    public PolygonEditorState polyEditor;
    public FixtureEditorState fixtureEditor;
    public ru.maklas.bodymaker.impl.states.CreationState currentState;


    public Color defaultLineColor = Color.WHITE;
    public Color defaultPointColor = Color.YELLOW;

    public Color selectedLineColor = Color.CYAN;
    public Color selectedPointColor = Color.GREEN;

    public Color defaultEndLineColor = Color.WHITE;
    public Color selectedEndLineColor = Color.LIGHT_GRAY;


    public float maxZoom = 4f;
    public float minZoom = 0.1f;
    public float maxDstToHighlight = 8;


    public CreationState getCreationState(DevState state){
        switch (state){
            case ImageLoading:
                return imaging;
            case PolygonEditor:
                return polyEditor;
            case FixtureEditor:
                return fixtureEditor;
        }
        return null;
    }

}
