package Engine.Core;

import Engine.Algebra.Vectors.Vector2;
import Engine.Core.Input.InputManager;
import Engine.Core.Scenes.Scene;
import Engine.Core.Scenes.Time;
import Engine.Core.Screen.Screen;
import com.sun.xml.internal.ws.api.pipe.Engine;
import jdk.internal.util.xml.impl.Input;

public class EngineCore extends Thread {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private final Scene[] _scenes;
    private int _currentScene = -1;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Constructor engine core
     * @param scenes array with all scenes you want to use for this engine instance
     * @param screenSize size of the game window
     * @param downSample how many times you want to downsample the screen resolution
     */
    public EngineCore(Scene[] scenes, Vector2 screenSize,int downSample){
        this(scenes,0,screenSize,downSample);
    }

    /**
     * Constructor engine core
     * @param scenes array with all scenes you want to use for this engine instance
     * @param startScene the index of the scene you want the engine to use as default scene
     * @param screenSize size of the game window
     * @param downSample how many times you want to downsample the screen resolution
     */
    public EngineCore(Scene[] scenes, int startScene, Vector2 screenSize, int downSample){
        this._scenes = scenes;
        InitializeScenes(); // initialize scenes
        Screen.I().Init(screenSize,downSample); // init screen
        SetScene(startScene); // set default scene
        start(); // start thread
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Thread
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Interface implementation of thread runnable. This function is called by the thread and keeps the engine going
     */
    public final void run(){
        while(true){
            if(Time.Update() && ValidSceneIndex(_currentScene)) { // update time
                _scenes[_currentScene].Update(); // update scene
                Screen.I().UpdateFrame(_scenes[_currentScene].PreRenderScene(_scenes[_currentScene].Render())); // update frame
                _scenes[_currentScene].PostRender(); // post render
                InputManager.I().UpdateManager(); // update input manager
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Change to a different scene
     * @param index scene you want to change to
     */
    public final void ChangeScene(int index) {
        if(_currentScene != index && ValidSceneIndex(index)){
            SetScene(index);
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private final void InitializeScenes(){
        for(int i = 0; i < _scenes.length; i++){
            _scenes[i].InitializeScene(this);
        }
    }
    private final boolean ValidSceneIndex(int index){
        return (index >= 0 && index < _scenes.length);
    }
    private final void SetScene(int index){
        if(ValidSceneIndex(index)) { // only set if your setting a valid scene index
            if (ValidSceneIndex(_currentScene)) { _scenes[_currentScene].UnloadScene();}
            _currentScene = index;
            _scenes[_currentScene].LoadScene();
            _scenes[_currentScene].Awake();
            _scenes[_currentScene].Start();
        }
    }
}
