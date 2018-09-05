package Game.Spawners;

import Engine.Algebra.Vectors.Vector3;
import ML.MLUtils;

public class PipeSpawner extends  ChainSpawner {

    private float minStep, maxStep, minY, maxY;

    /**
     * 
     * @param template to spawn object
     * @param time time between spawns 
     * @param minStep minimal spawner movement
     * @param maxStep maximal spawner movement
     * @param minY min y position relative to the spawner
     * @param maxY max y position relative to the spawner
     */
    public PipeSpawner(Class template, float time, float minStep, float maxStep, final float minY, final float maxY) {
        super(template, time);
        this.minStep = minStep;
        this.maxStep = maxStep;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * Position of object spawned
     * @return spawn position
     */
    @Override
    protected Vector3 SpawnPosition() {
        final Vector3 curPos = Transform().Position();
        final float randomY = MLUtils.Random(minY,maxY);
        return new Vector3(curPos.x(),curPos.y()+randomY,curPos.z()) ;
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
