package Engine.Core.Scenes;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.EngineCore;
import Engine.Core.Objects.Component;
import Engine.Core.Objects.Components.Camera;
import Engine.Core.Objects.GameObject;
import Engine.Core.Screen.Screen;
import Engine.Debug.DebugEngine;
import Engine.Events.CallCollector;
import Engine.Events.IAwakeEvent;
import Engine.Events.IStartEvent;
import Engine.Events.IUpdateEvent;
import Engine.Physics.Collider;
import Engine.Physics.PhysicsEngine;
import Game.Objects.Bird;
import com.sun.xml.internal.ws.api.pipe.Engine;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Scene implements IAwakeEvent,IStartEvent,IUpdateEvent {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private final ArrayList<GameObject> _objects = new ArrayList<GameObject>(); // array list with all scene gameobjects
    private final CallCollector<IStartEvent> _startCollector = new CallCollector<IStartEvent>("Start"); // call collector for start events
    private final CallCollector<IAwakeEvent> _awakeCollector = new CallCollector<IAwakeEvent>("Awake"); // call collector for awake events
    private final PhysicsEngine _physics = new PhysicsEngine(); // initialize physics engine
    private final DebugEngine _debug = new DebugEngine(); // Initialize debug engine
    private EngineCore _core; // reference to core
    private boolean _brokenItteration; // itteration brokenn

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Protected abstract voids
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Load is used to instantiate all initial gameobjects and is called everytime you load and reload the scene. So loading assets here is not recommended
     */
    protected abstract void Load();

    /**
     * Unload is used to load in assets when switched to this scene.
     */
    protected abstract void Unload();

    /**
     * LoadAssets is called when the scene is initialized by the engine core. So its only called once for every engine instance
     */
    protected abstract void LoadAssets();

    /**
     * Updates the scene
     */
    protected abstract void UpdateScene();

    /**
     * Initializes pre render chain. Normally used for post processing and UI
     * @param frame the current frame
     * @return modified frame by pre render
     */
    protected abstract BufferedImage PreRender(BufferedImage frame);

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * (Do not call from outside, should only be called by the engine core) InitializeScene initializes the current scene. It should be called once for every scene to set default values etc.
     * @param core Reference to the engine core this scene is used in
     */
    public final void InitializeScene(EngineCore core){
        _core = core; // set core
        LoadAssets(); // load in assets
    }

    /**
     * Renders the scene in its current state
     * @return the current frame in the form of a buffered image
     */
    public final BufferedImage Render(){
        ArrayList<Camera> cameras = this.<Camera>FindGameObjectsOfType(Camera.class); // all cameras in the scene
        if(cameras.size() > 0){ return cameras.get(0).Render(_debug);} // render out first camera if there is one
        return new BufferedImage(Screen.I().getWidth(),Screen.I().getHeight(),BufferedImage.TYPE_INT_RGB); // return empty frame. No camera found
    }

    /**
     * (Do not call from outside, should only be called by the engine core) Updates the scene.
     */
    public final void Update(){
        SetBrokenItteration(false); // unbreak if broken
        UpdatePhysics(); // update physics
        UpdateCallCollectors(); // call collectors
        UpdateScene(); // update scene
        UpdateObjects(); // update objects
    }

    /**
     * (Do not call from outside, should only be called by the engine core) Initializes scene loading
     */
    public final void LoadScene(){
        Load();
    }

    /**
     * (Do not call from outside, should only be called by the engine core) Initializes post render chain
     */
    public final void PostRender(){
        PostRenderObjects();
    }


    /**
     * (Do not call from outside, should only be called by the engine core) Initializes post render chain
     * @param frame
     * @return
     */
    public final BufferedImage PreRenderScene(BufferedImage frame){
        return PreRender(frame);
    }

    /**
     * Reloads this scene
     */
    public final void ReloadScene(){
        SetBrokenItteration(true); // break itteration
        ClearScene(); // clear complete scene
        LoadScene(); // load scene again
    }

    /**
     * (Do not call from outside, should only be called by the engine core) Initializes the unload chain
     */
    public final void UnloadScene(){
        SetBrokenItteration(true); // break itteration
        Unload();
        ClearScene(); // clear complete scene
    }

    /**
     * Instantiate a new gameobject into the scene. Default transform is Vector.zero for translation,rotation and scale.
     * @param obj gameobject you want to instantiate
     * @return instantiated gameobject to be used like a builder pattern
     */
    public final GameObject Instantiate(GameObject obj){
        Instantiate(obj,Vector3.Zero(),Vector3.Zero());
        return obj;
    }

    /**
     * Instantiate a new gameobject into the scene.
     * @param obj gameobject you want to instantiate
     * @param position position to place the gameobject at
     * @param rotation rotation to give the gameobject
     * @return instantiated gameobject to be used like a builder pattern
     */
    public final GameObject Instantiate(GameObject obj, Vector3 position,Vector3 rotation) {
        Instantiate(obj,position,rotation,new Vector3(1,1,1));
        return obj;
    }

    /**
     * Instantiate a new gameobject into the scene.
     * @param obj gameobject you want to instantiate
     * @param position position to place the gameobject at
     * @param rotation rotation to give the gameobject
     * @param scale scale to give the gameobject
     * @return instantiated gameobject to be used like a builder pattern
     */
    public final GameObject Instantiate(GameObject obj, Vector3 position,Vector3 rotation, Vector3 scale) {
        _objects.add(obj);
        obj.InitializeGameObject(this);
        obj.Transform().SetTransform(position,rotation,scale);

        // Add to collectors
        if(obj instanceof IAwakeEvent) {RegisterAwakeEvent((IAwakeEvent)obj);} // add to awake collector
        if(obj instanceof IStartEvent) {RegisterStartEvent((IStartEvent)obj);} // add to start collector

        return obj;
    }

    /**
     * (Is normally called by the engine itself) Register a start event in the start call collector
     * @param event
     */
    public final void RegisterStartEvent(IStartEvent event){
        _startCollector.AddCall(event);
    }

    /**
     * (Is normally called by the engine itself) Register a awake event in the awake call collector
     * @param event
     */
    public final void RegisterAwakeEvent(IAwakeEvent event){
        _awakeCollector.AddCall(event);
    }

    /**
     * (Is normally called by the engine itself) Register a physics object in the physics engine of the scene
     * @param col physics object you want to register
     */
    public final void RegisterPhysicsObject(Collider col){
        _physics.RegisterCollider(col);
    }

    /**
     * (Is normally called by the engine itself) Remove a physics object from the physics engine of the scene
     * @param col physics object you want to remove
     */
    public final void RemovePhysicsObject(Collider col){
        _physics.RemoveCollider(col);
    }

    /**
     * Destory a gameobject in the scene
     * @param obj gameobject you want to destroy
     */
    public final void Destroy(GameObject obj){
        _objects.remove(obj);
        obj.Destroy();
    }

    /**
     * Find all components in scene by a given type
     * @param type the component type
     * @param <T> generic return type of components
     * @return array list containing all found components
     */
    public final <T extends Component> ArrayList<T> FindGameObjectsOfType(Class<T> type){
        ArrayList<T> _components = new ArrayList<T>();
        for(int i = 0; i < _objects.size(); i++){
            T component = _objects.get(i).<T>GetComponent(type);
            if(component != null){ _components.add(component);}
        }
        return _components;
    }

    /**
     * Find all components in scene with one of the types given by the type array
     * @param types all types you want to find
     * @param <T> generic return type that all types should be able to cast to
     * @return array list containing all found components
     */
    public final <T extends Component> ArrayList<T> FindGameObjectsOfTypes(Class<T>[] types){
        ArrayList<T> _components = new ArrayList<T>();
        for(int i = 0; i < _objects.size(); i++){
            T component = _objects.get(i).<T>GetComponent(types);
            if(component != null){ _components.add(component);}
        }
        return _components;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void UpdateObjects(){
        for(int i = 0; i < _objects.size(); i++){
            if(_brokenItteration){break;} // check for broken itteration
            _objects.get(i).CoreUpdate();
        }
    }
    private void PostRenderObjects(){
        for(int i = 0; i < _objects.size(); i++){
            if(_brokenItteration){break;} // check for broken itteration
            _objects.get(i).CorePostRender();
        }
    }
    private BufferedImage PreRenderObjects(BufferedImage frame){
        for(int i = 0; i < _objects.size(); i++){
            if(_brokenItteration){break;} // check for broken itteration
            frame = _objects.get(i).CorePreRender(frame);
        }
        return frame;
    }
    private void UpdateCallCollectors(){
        // Call awake
        _awakeCollector.Call();
        _awakeCollector.Clear();

        // Call start
        _startCollector.Call();
        _startCollector.Clear();
    }
    private void UpdatePhysics(){
        _physics.Update(); // update physics
    }
    private void ClearScene(){
        _objects.clear();
        _physics.Clear();
        _debug.Clear();
        _startCollector.Clear();
        _awakeCollector.Clear();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Get the debug engine of this scene
     * @return debug engine instance of this scene
     */
    public final DebugEngine Debug(){
        return _debug;
    }

    /**
     * Get engine core
     * @return engine core
     */
    public final EngineCore Core(){
        return _core;
    }

    // private
    private boolean BrokenItteration(){
        return _brokenItteration;
    }
    private void SetBrokenItteration(boolean broken){
        _brokenItteration = broken;
    }
}
