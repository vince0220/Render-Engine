package Game.Scenes;
import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Input.InputManager;
import Engine.Core.Objects.GameObject;
import Engine.Core.Scenes.Scene;
import Engine.Resources.Assets.AssetManager;
import Game.AI.Activity;
import Game.AI.Brain;
import Game.Interfaces.ICameraHolder;
import Game.UI.InterfaceController;
import Game.Objects.*;
import Game.Spawners.*;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class GameScene extends BaseScene{

    /**
     * Scene camera
     */
    public GameCamera camera;

    private boolean gameOver;
    private Bird player,ai;
    private InterfaceController interfaceController;
    private java.util.List<Activity> activities;
    private List<DoublePipe> pipes;

    /**
     * Loads all objects and assets
     */
    @Override
    protected void Load() {
        pipes = new ArrayList<>();
        activities = new ArrayList<>();
        gameOver = false;
        Brain.GetInstance().PreTrain();
        InitSpawners();
        InitBirds();
        InitCamera();
        InitEffects();
        InitUI();
    }

    /**
     * Default unload method
     */
    @Override
    protected void Unload() { }

    /**
     * Default awake method
     */
    @Override
    public void Awake() { }

    /**
     * Default start method
     */
    @Override
    public void Start() { }

    /**
     * Default scene update method
     */
    @Override
    public void UpdateScene(){
        if(InputManager.I().GetKeyDown(KeyCode.SPACE)&&gameOver){
            ReloadScene();
        }
        if(InputManager.I().GetKeyDown(KeyCode.ENTER))GameOver();
    }


    /**
     * Inits birds
     */
    private void InitBirds(){
        ai = new AiBird();
        player = new PlayerBird();

        Instantiate(ai);
        Instantiate(player);

        ai.Transform().SetPosition(new Vector3(-2,2,0));
        player.Transform().SetPosition(new Vector3(0,2,0));
    }

    /**
     * Inits camera
     */
    private void InitCamera(){
        camera = new GameCamera();
        Instantiate(camera);
        camera.Follow(player);
    }

    /**
     * Inits ui controller
     */
    private void InitUI(){
        interfaceController = InterfaceController.GetInstance();
        Instantiate(interfaceController);
    }

    /**
     * Registers a activity
     * @param activity
     */
    public void registerActivity(final Activity activity){ activities.add(activity); }

    /**
     * Registers a double pipe
     * @param pipe
     */
    public void RegisterPipe(final DoublePipe pipe){
        pipes.add(pipe);
    }

    /**
     * Gets a pipe
     * @param index
     * @return DoublePipe
     */
    public DoublePipe GetPipe(final int index){ return pipes.get(index); }

    /**
     * Returns size of registered pipe list
     * @return amount of pipes
     */
    public int PipeCount(){ return pipes.size(); }

    /**
     * Checks if the game is over
     */
    public void checkIfGameIsOver(){
        if(!player.alive&&!ai.alive)
            GameOver();
        if(!player.alive&&ai.alive)
            camera.Follow(ai);
    }

    /**
     * Sets the game over
     */
    private void GameOver(){
        Brain.GetInstance().Learn();
        activities.clear();
        gameOver = true;
    }

    /**
     * Default PreRender methode of scene
     * @param frame the current frame
     * @return
     */
    @Override
    protected BufferedImage PreRender(BufferedImage frame) {
        interfaceController.DrawScore(frame);
        if(gameOver)interfaceController.DrawGameOver(frame);
        return frame;
    }

    /**
     * Default Camera methode of scene
     * @return
     */
    @Override
    public GameObject Camera() {
        return camera;
    }
}
