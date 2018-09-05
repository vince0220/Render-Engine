package Game.Objects;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Scenes.Time;

import java.awt.*;

public class Cloud extends StaticObject {

    private float speed = 1f;

    /**
     * Returns the name of the mesh that should be used for this object
     * @return name of mesh
     */
    @Override
    public String MeshName() {
        return "Clouds";
    }

    /**
     * Returns the name of the albedo texture that should be used for this object
     * @return name of albedo texture
     */
    @Override
    public String TextureName() {
        return "CloudDiffuse";
    }

    /**
     * Returns the base color that should be used for this static object. The base color is only used when there is not albedo texture
     * @return base color
     */
    @Override
    public Color Color() {
        return null;
    }

    /**
     * Default update method
     */
    @Override
    public void Update(){
        final Vector3 currentPosition = Transform().Position();
        Transform().SetPosition(new Vector3(currentPosition.x()-speed * Time.DeltaTime(),currentPosition.y(),currentPosition.z()));
    }
}
