package Game.Spawners;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.GameObject;
import Engine.Core.Scenes.Time;
import Game.Interfaces.ICameraHolder;
import Game.Scenes.BaseScene;
import Game.Scenes.GameScene;

import java.lang.reflect.Constructor;

public abstract class Spawner extends GameObject {

    private static final float DISTANCE_TO_CAMERA = 100f;

    /**
     * Templated constructor that is used to construct new objects
     */
    protected Constructor<?> constructor;

    private float timer = 0;
    
    /**
     * Abstract method to define the spawntime
     * @return spawn time 
     */
    public abstract float SpawnTime();
    protected abstract Vector3 SpawnPosition();
    protected abstract Vector3 SpawnRotation();
    protected abstract Vector3 SpawnScale();

    /**
     * Creates instance of spawner
     * @param template object to spawn
     */
    public Spawner(final Class template){
        try {
            constructor = template.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Default engine update method
     */
    @Override
    public void Update()
    {
        timer -= Time.DeltaTime();
        if (timer < 0)
        {
            Spawn();
            ResetTimer();
        }
    }

    /**
     * Resets spawn timer
     */
    private void ResetTimer()
    {
        timer = SpawnTime();
    }


    /**
     * Creates new object
     */
    private void CreateObject(){
        try {
            final GameObject object = (GameObject)constructor.newInstance();
            Scene().Instantiate(object,SpawnPosition(),SpawnRotation(),SpawnScale());
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the spawner is not to far away from the camera
     * @return
     */
    protected boolean MaySpawn(){
        if(Scene() instanceof BaseScene){
            return ((BaseScene)Scene()).Camera().Transform().Position().Distance(Transform().Position())<DISTANCE_TO_CAMERA;
        }
        return false;
    }

    /**
     * Places/spawns a object
     */
    public void Spawn(){
        if(MaySpawn())CreateObject();
    }
}
