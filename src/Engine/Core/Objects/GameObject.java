package Engine.Core.Objects;

import Engine.Core.Objects.Components.Transform;
import Engine.Core.Scenes.Scene;
import Engine.Events.*;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class GameObject implements IAwakeEvent,IStartEvent,IUpdateEvent,IPostRenderEvent,IPreRender,IDestroyEvent {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Map<Class,Component> _components = new HashMap<Class,Component>();
    private Transform _transform;
    private Scene _scene;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public String tag;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public GameObject(){}

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Base voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void Awake(){}
    public void Start(){}
    public void Update(){
    }
    public void Destroy(){}
    public void PostRender() { }
    public BufferedImage PreRender(BufferedImage frame) { return frame;}

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public final <T extends Component> T AddComponent(Component component){
        if(!HasComponent(component.getClass())) {
            _components.put(component.getClass(), component);
            component.InitializeComponent(this);
            return (T)component;
        }
        return null;
    }
    public final void RemoveComponent(Class type){
        if(HasComponent(type)){
            RemoveComponent(_components.get(type));
        }
    }
    public final void RemoveComponent(Component component){
        if(_components.containsKey(component.getClass())) {
            _components.remove(component);
            component.Destroy(); // call on destroy
        }
    }
    public final boolean HasComponent(Class c){
       return _components.containsKey(c);
    }
    public <T extends Component> T GetComponent(Class<T> type){
        if(_components.containsKey(type)){
            return (T)_components.get(type);
        }
        return null;
    }
    public <T extends Component> T GetComponent(Class<T>[] types){
        for(int i = 0; i < types.length; i++){
            if(_components.containsKey(types[i])){
                return (T)_components.get(types[i]);
            }
        }
        return null;
    }
    public final void InitializeGameObject(Scene scene){
        this._scene = scene;
    }
    public final void CoreUpdate(){
        UpdateComponents();
        Update();
    }
    public final void CorePostRender(){
        PostRenderComponents();
        PostRender();
    }
    public final BufferedImage CorePreRender(BufferedImage frame){
        return PreRender(PreRenderComponents(frame));
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public final Transform Transform(){
        if(_transform == null){
            _transform = AddComponent(new Transform());
        }
        return _transform;
    }
    public final Scene Scene(){
        return _scene;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void UpdateComponents(){
        for(Component value : _components.values()){
            value.Update();
        }
    }
    private void PostRenderComponents(){
        for(Component value : _components.values()){
            value.PostRender();
        }
    }
    private BufferedImage PreRenderComponents(BufferedImage frame){
        for(Component value : _components.values()){
            frame = value.PreRender(frame);
        }
        return frame;
    }
}
