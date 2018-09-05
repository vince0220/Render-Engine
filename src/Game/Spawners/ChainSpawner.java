package Game.Spawners;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.Components.MeshRenderer;
import Engine.Core.Objects.GameObject;
import com.sun.istack.internal.NotNull;

public class ChainSpawner extends Spawner {
    /**
     * The size of the steps the spawner takes when spawning a new object
     */
    protected float stepSize = 0;

    private float time = 0;
    private float preSpawnAmount = 0;

    /**
     * Minimum contructor, constructs a instance of chainspawner
     * @param template Object to construct
     * @param time Spawn time step 
     */
    public ChainSpawner(@NotNull final Class template,@NotNull final float time) {
        super(template);
        this.time = time;
    }

    /**
     * Constructs a instance of chainspawner
     * @param template Object to construct
     * @param time spawn time step 
     * @param step size of the step the spawner moves
     */
    public ChainSpawner(@NotNull final Class template, @NotNull final float time, @NotNull final float step){
        super(template);
        this.time = time;
        stepSize = step;
    }

    /**
     * Default awake methode of spawner
     */
    @Override
    public void Awake(){
        super.Awake();
        if(stepSize!=0){
            CancelOutStepSize();
            PreSpawn();
            return;
        }
        TryAndGetStepSize();
    }

    /**
     * Tries to construct a gameobject to measure its boundaries
     */
    private void TryAndGetStepSize(){
        try {
            final GameObject object = (GameObject)constructor.newInstance();
            if(object.GetComponent(MeshRenderer.class)!=null){
                stepSize = object.GetComponent(MeshRenderer.class).Mesh().bounds.size.x() * 2;
            }
            CancelOutStepSize();
            PreSpawn();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets pre spawn amount
     * @param amount pre spawn amount
     */
    public void SetPreSpawnAmount(final int amount){
        preSpawnAmount = amount;
    }

    /**
     * Spawns preSpawnAmoount of objects when spawner gets active
     */
    private void PreSpawn(){
        for(int i = 0; i < preSpawnAmount; i++)
            Spawn();
    }

    /**
     * Cancels out the step size
     */
    private void CancelOutStepSize(){
        final Vector3 pos = Transform().Position();
        pos.axis[0]-=stepSize;
    }

    /**
     * Spawn interval
     * @return time of spawn
     */
    @Override
    public float SpawnTime() {
        return time;
    }

    /**
     * Position of object spawned
     * @return spawn position
     */
    @Override
    protected Vector3 SpawnPosition() {
        return new Vector3(Transform().Position().x(),Transform().Position().y(),Transform().Position().z());
    }

    /**
     * Rotation of object spawned
     * @return spawn rotation
     */
    @Override
    protected Vector3 SpawnRotation() {
        return new Vector3(Transform().Rotation().x(),Transform().Rotation().y(),Transform().Rotation().z());
    }

    /**
     * Scale of object spawned
     */
    @Override
    protected Vector3 SpawnScale() {
        return new Vector3(1,1,1);
    }

    /**
     * Spawns a object if possible
     */
    @Override
    public void Spawn(){
        if(!MaySpawn())return;
        super.Spawn();
        final Vector3 pos = Transform().Position();
        pos.axis[0]+=stepSize;
        Transform().SetPosition(pos);
    }
}
