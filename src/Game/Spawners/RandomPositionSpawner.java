package Game.Spawners;

import Engine.Algebra.Vectors.Vector3;
import ML.MLUtils;
import com.sun.istack.internal.NotNull;

public class RandomPositionSpawner extends ChainSpawner {

    private float minStep, maxStep,minScale,maxScale;
    private Vector3 minPosition, maxPosition;

    /**
     * Creates a instance of the RandomPositionSpawner
     * @param template to spawn object
     * @param time time between spawns
     * @param minStep minimal spawner movement 
     * @param maxStep maximal spawner movement 
     * @param minPosition minimal object position relative to the spawn
     * @param maxPosition maximal object position relative to the  the spawner
     * @param minScale minimal object scale
     * @param maxScale maximal object scale
     */
    public RandomPositionSpawner(final Class template, float time,
                                 float minStep, float maxStep,
                                 final Vector3 minPosition, final Vector3 maxPosition,
                                 final float minScale, final float maxScale) {
        super(template, time);
        this.minStep = minStep;
        this.maxStep = maxStep;
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;
        this.minScale = minScale;
        this.maxScale = maxScale;
    }

    /**
     * Position of object spawned
     * @return spawn position
     */
    @Override
    protected Vector3 SpawnPosition() {
        final Vector3 curPos = Transform().Position();
        final Vector3 randPos = Random(minPosition,maxPosition);
        return new Vector3(
                curPos.x()+randPos.x(),
                curPos.y()+randPos.y(),
                curPos.z()+randPos.z()) ;
    }

    /**
     * Scale of object spawned
     * @return object scale
     */
    @Override
    protected Vector3 SpawnScale(){
        final float val = MLUtils.Random(minScale,maxScale);
        return new Vector3(val,val,val);
    }

    /**
     * Returns a random vector in range of given vectors
     * @param left
     * @param right
     * @return random value between the given vectors
     */
    private Vector3 Random(@NotNull final Vector3 left, @NotNull final Vector3 right){
        return new Vector3(
                MLUtils.Random(left.x(),right.x()),
                MLUtils.Random(left.y(),right.y()),
                MLUtils.Random(left.z(),right.z())
        );
    }

    /**
     * Spawns object if possible
     */
    @Override
    public void Spawn(){
        if(!MaySpawn())return;
        stepSize = MLUtils.Random(minStep,maxStep);
        super.Spawn();
    }

}
