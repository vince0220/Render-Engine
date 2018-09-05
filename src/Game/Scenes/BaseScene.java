package Game.Scenes;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Scenes.Scene;
import Engine.Resources.Assets.AssetManager;
import Game.Interfaces.ICameraHolder;
import Game.Objects.*;
import Game.Spawners.*;

public abstract class BaseScene extends Scene implements ICameraHolder {


    /**
     * Inits spawners
     */
    protected void InitSpawners(){
        InitiFoliageSpawner();
        InitPipeSpawner();
        InitFenceSpawner();
        InitCloudSpawner();
        InitFloorSpawner();
    }

    /**
     * Inits effects
     */
    protected void InitEffects(){
        final FollowSpawner snow = new FollowSpawner(
                Snow.class,0.1f,0,0,
                new Vector3(-4,-2,-2),new Vector3(4,2,2),
                0.01f,0.02f
        );
        Instantiate(snow);
        snow.Follow(Camera(),new Vector3(2,0,-5));
    }


    /**
     * Init cloud spawner
     */
    protected void InitCloudSpawner(){
        final float floorLevel = -3;
        final RandomPositionSpawner cloudSpawner = new RandomPositionSpawner(
                Cloud.class,0.1f,7f,14f,
                new Vector3(-1,6,-1),new Vector3(1,7,1),
                0.8f,1.2f
        );
        cloudSpawner.SetPreSpawnAmount(50);
        Instantiate(cloudSpawner);
        cloudSpawner.Transform().SetPosition(new Vector3(0,floorLevel,-6));
    }

    /**
     * Init fence spawner
     */
    protected void InitFenceSpawner(){
        final float floorLevel = -3;
        final RandomPositionSpawner fenceSpawner = new RandomPositionSpawner(
                Fence.class,0f,2.5f,2.5f,
                new Vector3(0,0,0), new Vector3(0,0,0),
                0.7f,0.7f
        );
        fenceSpawner.SetPreSpawnAmount(10);
        Instantiate(fenceSpawner);
        fenceSpawner.Transform().SetPosition(new Vector3(-10,floorLevel,-4));
    }

    /**
     * Init foliage spawner
     */
    protected void InitiFoliageSpawner(){
        final float floorLevel = -3;
        final int  preSpawnAmount = 10;
        final FoliageSpawner foliageSpawner = new FoliageSpawner(Tree.class,0.1f,2,5,-3,3f,1.5f,2f);
        final FoliageSpawner bushSpawner = new FoliageSpawner(Bush.class,0.3f,2,5,-2,2,0.5f,1f);
        foliageSpawner.SetPreSpawnAmount(preSpawnAmount);
        bushSpawner.SetPreSpawnAmount(preSpawnAmount);
        Instantiate(foliageSpawner);
        Instantiate(bushSpawner);
        foliageSpawner.Transform().SetPosition(new Vector3(-10,floorLevel,-9));
        bushSpawner.Transform().SetPosition(new Vector3(-10,floorLevel,-8));
    }

    /**
     * Init pipe spawner
     */
    protected void InitPipeSpawner(){
        final float minMax = 0.3f;
        final Spawner pipeSpawner = new PipeSpawner(DoublePipe.class,0.1f,6,8,-minMax,minMax);
        Instantiate(pipeSpawner);
        pipeSpawner.Transform().SetPosition(new Vector3(10,1f,0));
    }

    /**
     * Inits floor(s) spawner(s)
     */
    protected void InitFloorSpawner(){
        final float floorLevel = -3;
        final int  preSpawnAmount = 10;
        final ChainSpawner backfloorSpawner = new ChainSpawner(BackgroundFloor.class,0.2f,10);
        final ChainSpawner floorSpawner = new ChainSpawner(Floor.class,0.2f,10);
        backfloorSpawner.SetPreSpawnAmount(10);
        floorSpawner.SetPreSpawnAmount(10);
        Instantiate(floorSpawner);
        Instantiate(backfloorSpawner);
        floorSpawner.Transform().SetPosition(new Vector3(-2,floorLevel,0));
        backfloorSpawner.Transform().SetPosition(new Vector3(-2,floorLevel,-15));
    }

    /**
     * Loads all scene assets
     */
    protected void LoadAssets() {
        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Pipe/Pipe.obj");
        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Pipe/PipeDiffuse.jpg");

        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Snow/Snow.obj");

        AssetManager.I().PreloadAsset("./res/3D/Enviroment/BackgroundPlane/BackgroundPlane.obj");
        AssetManager.I().PreloadAsset("./res/3D/Enviroment/BackgroundPlane/GroundDiffuse.jpg");

        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Path/Path.obj");
        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Path/PathDiffuse.jpg");

        AssetManager.I().PreloadAsset("./res/3D/Enviroment/PineTree/PineTree.obj");
        AssetManager.I().PreloadAsset("./res/3D/Enviroment/PineTree/PineTree_Diffuse.jpg");

        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Clouds/Clouds.obj");
        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Clouds/CloudDiffuse.jpg");

        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Bush/Bush.obj");
        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Bush/BushDiffuse.jpg");

        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Fence/Fence.obj");
        AssetManager.I().PreloadAsset("./res/3D/Enviroment/Fence/FenceDiffuse.jpg");

        // Load bird frames
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/Frames/BirdFrame1.obj"); // frame1
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/Frames/BirdFrame2.obj"); // frame2
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/Frames/BirdFrame3.obj"); // frame3
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/Frames/BirdFrame4.obj"); // frame4
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/Frames/BirdFrame5.obj"); // frame5
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/Frames/BirdFrame6.obj"); // frame6
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/Frames/BirdFrame7.obj"); // frame7
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/Frames/BirdFrame8.obj"); // frame8
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/Frames/BirdFrame9.obj"); // frame9

        // Load bird textures
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/BirdBlue.jpg"); // blue
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/BirdCyan.jpg"); // cyan
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/BirdGreen.jpg"); // green
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/BirdPink.jpg"); // pink
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/BirdPurple.jpg"); // purple
        AssetManager.I().PreloadAsset("./res/3D/Characters/Bird/BirdRed.jpg"); // red
    }
}
