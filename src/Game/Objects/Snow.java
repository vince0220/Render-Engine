package Game.Objects;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Scenes.Time;

import java.awt.*;

public class Snow extends StaticObject {

    /**
     * Returns the name of the mesh that should be used for this object
     * @return name of mesh
     */
    @Override
    public String MeshName() {
        return "Snow";
    }

    /**
     * Returns the name of the albedo texture that should be used for this object
     * @return name of albedo texture
     */
    @Override
    public String TextureName() {
        return null;
    }

    /**
     * Returns the base color that should be used for this static object. The base color is only used when there is not albedo texture
     * @return base color
     */
    @Override
    public void Update(){
        super.Update();
        final Vector3 cur = Transform().Position();
        final float fall = Time.DeltaTime()*1;
        Transform().SetPosition(new Vector3(cur.x(),cur.y()-fall,cur.z()));
    }

    /**
     * Gets snow color
     */
    @Override
    public Color Color() {
        return Color.WHITE;
    }
}
