package Engine.Core.Objects.Components;

import Engine.Core.Objects.Component;
import Engine.Events.IFrustumEvents;
import Engine.Graphics.Graphics3D.Mesh;
import Engine.Graphics.Rendering.Shader;

import java.awt.image.BufferedImage;

public class MeshRenderer extends Component implements IFrustumEvents {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Shader shader;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Mesh mesh;
    private boolean isVisible = false;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public MeshRenderer(Mesh mesh,Shader shader){
        this.mesh = mesh;
        this.shader = shader;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Base voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void Awake() { }
    public void Start() { }
    public void Update() { }
    public void PostRender() { }
    public void Destroy() { }
    public BufferedImage PreRender(BufferedImage frame) {
        return frame;
    }
    public void OnBecameVisible() {
        isVisible = true;

        IFrustumEvents e = GameobjectFrustum();
        if(e != null){e.OnBecameVisible();}
    }
    public void OnBecameInvisible() {
        isVisible = false;

        IFrustumEvents e = GameobjectFrustum();
        if(e != null){e.OnBecameInvisible();}
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public boolean IsVisible(){
        return isVisible;
    }
    public Mesh Mesh(){
        return mesh;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private IFrustumEvents GameobjectFrustum() {
        if (GameObject() instanceof IFrustumEvents) {
            return (IFrustumEvents) GameObject();
        }
        return null;
    }
}
