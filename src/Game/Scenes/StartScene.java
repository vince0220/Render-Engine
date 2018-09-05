package Game.Scenes;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Input.InputManager;
import Engine.Core.Objects.GameObject;
import Engine.Core.Screen.Screen;
import Engine.Resources.Assets.AssetManager;
import Game.Objects.Bird;
import Game.Objects.GameCamera;
import Game.Objects.IntroBird;
import Game.Spawners.RandomPositionSpawner;
import Game.UI.InterfaceController;
import javafx.scene.input.KeyCode;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.File;

public class StartScene extends BaseScene {

    /**
     * Scene camera
     */
    public GameCamera camera;

    private InterfaceController interfaceController;
    private static Clip mainTrack;

    /**
     * Default load methode, used to load in and initialize all scene objects
     */
    @Override
    protected void Load() {
        InitWindow();
        InitCamera();
        InitUI();
        InitAudio();
        InitEffects();
        InitCloudSpawner();
        InitFloorSpawner();
        InitFenceSpawner();
        InitiFoliageSpawner();
        InitShowBirds();
    }

    /**
     * Inits show birds
     */
    private void InitShowBirds(){
        final RandomPositionSpawner spawner = new RandomPositionSpawner(
                IntroBird.class,10,0,0,
                new Vector3(-5,-1,-3),new Vector3(-1,2,3),
                0.3f,1f);
        spawner.SetPreSpawnAmount(2);
        Instantiate(spawner);
    }

    /**
     * Default unload method, used to remove objects from scene or assets out of memory on unload
     */
    @Override
    protected void Unload() { }

    /**
     * Default scene update method
     */
    @Override
    public void UpdateScene(){
        if(InputManager.I().GetKeyDown(KeyCode.SPACE)){
            Core().ChangeScene(1);
        }
    }

    /**
     * Inits the game window
     */
    private void InitWindow(){
        AssetManager.I().PreloadAsset("./res/2D/Icon/Icon.png");
        Screen.I().setTitle("Best of the nest");
        Screen.I().setIconImage(AssetManager.I().FindAsset("Icon",BufferedImage.class));
    }

    /**
     * Inits camera
     */
    private void InitCamera(){
        camera = new GameCamera();
        Instantiate(camera);
        camera.Transform().SetPosition(new Vector3(0,5,10));
        camera.Transform().SetRotation(new Vector3(0,0,0));
    }


    /**
     * Inits audio
     */
    private void InitAudio(){
        if(mainTrack==null){
            try {
                final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/Audio/track.wav"));
                mainTrack = AudioSystem.getClip();
                mainTrack.open(audioInputStream);
                mainTrack.loop(Clip.LOOP_CONTINUOUSLY);
                mainTrack.start();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inits ui controller
     */
    private void InitUI(){
        interfaceController = InterfaceController.GetInstance();
        Instantiate(interfaceController);
    }

    /**
     * pre render event handler for this scene
     * @param frame the current frame
     * @return
     */
    @Override
    protected BufferedImage PreRender(BufferedImage frame) {
        interfaceController.DrawStart(frame);
        return frame;
    }

    /**
     * Default awake method
     */
    @Override
    public void Awake() {

    }

    /**
     * Default start method
     */
    @Override
    public void Start() {

    }

    /**
     * Returns scene camera
     * @return
     */
    @Override
    public GameObject Camera() {
        return camera;
    }
}
