package Game.Objects;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.Components.BoxCollider;
import Engine.Core.Objects.Components.MeshRenderer;
import Engine.Graphics.Graphics3D.Bounds;

import java.awt.*;

public class Pipe extends StaticObject {

    /**
     * Object tag used to identify pipes on for example: on collision
     */
    public static String TAG = "PIPE";

    /**
     * Empty constructor of Pipe
     */
    public Pipe(){
        tag = TAG;
    }

    /**
     * Default start method
     */
    @Override
    public void Start(){
        super.Start();
        final Bounds bounds = GetComponent(MeshRenderer.class).Mesh().bounds;
        final Vector3 size =  GetComponent(MeshRenderer.class).Mesh().bounds.size;
        AddComponent(
                new BoxCollider(new Vector3(bounds.center.x(),bounds.center.y(),bounds.center.z()),new Vector3(size.x(),size.y(),size.z()))
        );
    }

    /**
     * Returns the name of the mesh that should be used for this object
     * @return name of mesh
     */
    @Override
    public String MeshName() {
        return "Pipe";
    }

    /**
     * Returns the name of the albedo texture that should be used for this object
     * @return name of albedo texture
     */
    @Override
    public String TextureName() {
        return "PipeDiffuse";
    }

    /**
     * Returns the base color that should be used for this static object. The base color is only used when there is not albedo texture
     * @return base color
     */
    @Override
    public Color Color() {
        return new Color(76, 154, 54);
    }

    /**
     * Cleans up components
     */
    @Override
    public void OnDispose(){
        RemoveComponent(BoxCollider.class);
        RemoveComponent(MeshRenderer.class);
    }

}
