package ru.maklas.bodymaker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.maklas.bodymaker.impl.MainState;
import ru.maklas.bodymaker.libs.gsm_lib.GameStateManager;
import ru.maklas.bodymaker.libs.gsm_lib.MultilayerStateManager;
import ru.maklas.bodymaker.libs.gsm_lib.State;

public class MBodyMaker extends ApplicationAdapter {

    SpriteBatch batch;
    State state;
    GameStateManager gsm;

    @Override
    public void create () {
        batch = new SpriteBatch();
        state = new MainState();
        gsm = new MultilayerStateManager(state, batch);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float dt = Gdx.graphics.getDeltaTime();
        if (dt > 0.02f){
            dt = 0.016666667f;
        }
        gsm.update(dt);
    }
    
    @Override
    public void dispose () {
        gsm.dispose();
        batch.dispose();
    }
}
