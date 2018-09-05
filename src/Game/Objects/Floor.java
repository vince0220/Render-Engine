package Game.Objects;

import java.awt.*;

public class Floor extends StaticObject {

    /**
     * Returns the name of the mesh that should be used for this object
     * @return name of mesh
     */
    @Override
    public String MeshName() {
        return "Path";
    }

    /**
     * Returns the name of the albedo texture that should be used for this object
     * @return name of albedo texture
     */
    @Override
    public String TextureName() {
        return "PathDiffuse";
    }

    /**
     * Returns the base color that should be used for this static object. The base color is only used when there is not albedo texture
     * @return base color
     */
    @Override
    public Color Color() {
        return new Color(118, 97, 60);
    }

    /**
     * Default update method
     */
    @Override
    public void Update(){ }
}
