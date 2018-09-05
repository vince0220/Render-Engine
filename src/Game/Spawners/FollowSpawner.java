package Game.Spawners;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.GameObject;
import com.sun.istack.internal.NotNull;

public class FollowSpawner extends RandomPositionSpawner {

    private GameObject followTarget;
    private Vector3 followOffset;

    /**
     * Creates instance of FollowSpawner
     * @param template object to spawn
     * @param time time between spawns
     * @param minStep minimal spawner movement
     * @param maxStep maximal spawner movement
     * @param minPosition minimal object spawn position relative to the spawner
     * @param maxPosition maximal object spawn position relative to the spawner
     * @param minScale minimal object scale
     * @param maxScale maximal object scale
     */
    public FollowSpawner(Class template, float time, float minStep, float maxStep, Vector3 minPosition, Vector3 maxPosition, float minScale, float maxScale) {
        super(template, time, minStep, maxStep, minPosition, maxPosition, minScale, maxScale);
    }

    /**
     * Follows a given gameobject
     * @param target to follow target
     * @param offset following offset
     */
    public void Follow(@NotNull final GameObject target,@NotNull final Vector3 offset){
        followTarget = target;
        followOffset = offset;
    }

    /**
     * Spawns object if possible
     */
    @Override
    public void Spawn(){
        if(!MaySpawn())return;
        super.Spawn();
        if(followTarget!=null){
            Transform().SetPosition(followTarget.Transform().Position().Add(followOffset));
        }
    }
}
