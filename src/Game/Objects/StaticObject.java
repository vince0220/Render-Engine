package Game.Objects;

import Engine.Core.Objects.Component;
import Engine.Core.Objects.Components.MeshRenderer;
import Engine.Core.Objects.GameObject;
import Engine.Events.IFrustumEvents;
import Engine.Graphics.Graphics3D.Mesh;
import Engine.Resources.Assets.AssetManager;
import Game.Shading.StandardShader;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class StaticObject extends GameObject implements IFrustumEvents {

    private boolean becameVisible = false;

    /**
     * Empty constructor of static object
     */
    public StaticObject(){ }

    /**
     * Override out of GameObject that is called when the object became visible
     */
    @Override
    public void OnBecameVisible(){
        becameVisible = true;
    }

    /**
     * Override out of GameObject that is called when the object became invisible
     */
    @Override
    public void OnBecameInvisible(){
        if(becameVisible){
            OnDispose();
        }
    }

    /**
     * Disposes object
     */
    public void OnDispose(){
        Scene().Destroy(this);
    }

    /**
     * Override out of GameObject that is called on Awake
     */
    @Override
    public void Awake(){
        InitializeMeshRenderer();
    }

    /**
     * Inits mesh renderer
     */
    public void InitializeMeshRenderer(){
        if(GetComponent(MeshRenderer.class) == null) {
            this.AddComponent(
                    new MeshRenderer(AssetManager.I().<Mesh>FindAsset(MeshName(), Mesh.class),
                            new StandardShader(Color(), AssetManager.I().<BufferedImage>FindAsset(TextureName(), BufferedImage.class))
                    )
            );
        }
    }

    /**
     * Returns the name of the mesh that should be used for this object
     * @return name of mesh
     */
    public abstract String MeshName();

    /**
     * Returns the name of the albedo texture that should be used for this object
     * @return name of albedo texture
     */
    public abstract String TextureName();

    /**
     * Returns the base color that should be used for this static object. The base color is only used when there is not albedo texture
     * @return base color
     */
    public abstract Color Color();
}
