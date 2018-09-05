package Game.Objects;

import java.awt.*;

public class Tree extends StaticObject {

    /**
     * Returns the name of the mesh that should be used for this object
     * @return name of mesh
     */
    @Override
    public String MeshName() {
        return "PineTree";
    }

    /**
     * Returns the name of the albedo texture that should be used for this object
     * @return name of albedo texture
     */
    @Override
    public String TextureName() {
        return "PineTree_Diffuse";
    }

    /**
     * Returns the base color that should be used for this static object. The base color is only used when there is not albedo texture
     * @return base color
     */
    @Override
    public Color Color() {
        return new Color(255,255,255);
    }
}
