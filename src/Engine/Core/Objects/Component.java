package Engine.Core.Objects;

import Engine.Core.Scenes.Scene;
import Engine.Events.*;

public abstract class Component implements IAwakeEvent,IStartEvent,IUpdateEvent,IPostRenderEvent,IPreRender,IDestroyEvent {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private GameObject _gameobject;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void InitializeComponent(GameObject parent){
        this._gameobject = parent;
        Scene().RegisterAwakeEvent(this); // register awake event
        Scene().RegisterStartEvent(this); // register start event
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public GameObject GameObject(){
        return _gameobject;
    }
    public Scene Scene(){
        return  _gameobject.Scene();
    }
}
