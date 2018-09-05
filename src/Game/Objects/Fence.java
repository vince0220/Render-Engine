package Game.Objects;

import java.awt.*;

public class Fence extends StaticObject {

    /**
     * Returns the name of the mesh that should be used for this object
     * @return name of mesh
     */
    @Override
    public String MeshName() {
        return "Fence";
    }

    /**
     * Returns the name of the albedo texture that should be used for this object
     * @return name of albedo texture
     */
    @Override
    public String TextureName() {
        return "FenceDiffuse";
    }

    /**
     * Returns the base color that should be used for this static object. The base color is only used when there is not albedo texture
     * @return base color
     */
    @Override
    public Color Color() {
        return new Color(0, 0, 0);
    }
}
