package Game.Spawners;

import Engine.Algebra.Vectors.Vector3;
import ML.MLUtils;
import com.sun.istack.internal.NotNull;

public class FoliageSpawner extends ChainSpawner {

    private float minStep, maxStep, minZ, maxZ, minScale, maxScale;

    /**
     * Creates a instance of FoliageSpawner
     * @param template object to spawn
     * @param time time between spawn
     * @param minStep min spawner movement after spawn
     * @param maxStep max spawner movement after spawn
     * @param minZ min z offset
     * @param maxZ max z offset
     * @param minScale min object scale
     * @param maxScale max object scale
     */
    public FoliageSpawner(@NotNull final Class template, @NotNull float time,
                          float minStep, float maxStep, final float minZ, final float maxZ,
                          final float minScale, final float maxScale){
        super(template,time);
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.minStep = minStep;
        this.maxStep = maxStep;
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
        final float randomZ = MLUtils.Random(minZ,maxZ);
        return new Vector3(curPos.x(),curPos.y(),curPos.z()+randomZ) ;
    }

    /**
     * Scale of object spawned
     * @return spawn scale
     */
    @Override
    protected Vector3 SpawnScale(){
        final float val = MLUtils.Random(minScale,maxScale);
        return new Vector3(val,val,val);
    }

    /**
     * Spawns a object if possible
     */
    @Override
    public void Spawn(){
        if(!MaySpawn())return;
        stepSize = MLUtils.Random(minStep,maxStep);
        super.Spawn();
    }
}
