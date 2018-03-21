package ru.maklas.bodymaker.engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import ru.maklas.bodymaker.libs.Utils;
import ru.maklas.mengine.ComponentMapper;
import ru.maklas.mengine.Engine;
import ru.maklas.mengine.Entity;
import ru.maklas.mengine.EntityListener;
import ru.maklas.mengine.systems.CollisionEntitySystem;
import ru.maklas.mengine.utils.ImmutableArray;

/**
 * Стандартная система для физики. Сопоставляет body.transform() с entity.x, y, angle.
 * Убеждается что при добавлении Entity в Engine, у body будет Entity в userData,
 * а у каждого Fixture будет FixtureData или кинет RuntimeException
 * При удалении из движка Entity, удаляет и Body, потому не нужно удалять Body самостоятельно.
 */
public class PhysicsSystem extends CollisionEntitySystem implements EntityListener {

    private final World world;
    private ImmutableArray<Entity> entities;
    private Array<PhysicsComponent> toDestroy = new Array<PhysicsComponent>();
    public static final ComponentMapper<PhysicsComponent> mapper = ComponentMapper.of(PhysicsComponent.class);

    public PhysicsSystem(World world) {
        this.world = world;
    }

    @Override
    public void onAddedToEngine(final Engine engine) {
        entities = engine.entitiesFor(PhysicsComponent.class);
        for (Entity entity : entities) {
            entity.get(mapper).body.setUserData(entity);
        }
        engine.addListener(this);
    }


    @Override
    public void update(float dt) {
        World world = this.world;
        float scale = Utils.scale;

        destroyPendings();

        world.step(0.016666667f, 6, 2);

        ComponentMapper<PhysicsComponent> collisionM = mapper;
        for (Entity entity : entities) {
            PhysicsComponent cc = entity.get(collisionM);

            Vector2 bodyPos = cc.body.getPosition();
            entity.x = bodyPos.x * scale;
            entity.y = bodyPos.y * scale;
            entity.setAngle(cc.body.getAngle() * MathUtils.radiansToDegrees);

        }

    }

    private void destroyPendings() {
        for (PhysicsComponent pc : toDestroy) {
            world.destroyBody(pc.body);
            pc.body = null;
        }
        toDestroy.clear();
    }

    @Override
    public void entityAdded(Entity entity) {
        PhysicsComponent cc = entity.get(mapper);
        if (cc != null) {
            cc.body.setUserData(entity);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        PhysicsComponent cc = entity.get(mapper);
        if (cc != null) {
            toDestroy.add(cc);
        }
    }
}
