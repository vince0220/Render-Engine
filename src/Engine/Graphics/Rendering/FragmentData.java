package Engine.Graphics.Rendering;

import Engine.Algebra.Vectors.Vector2;
import Engine.Algebra.Vectors.Vector3;

public class FragmentData {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public int x;
    public int y;
    public Vector3 normal;
    public Vector2 uv;
    public Vector3 cameraDir;
    public float depth;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public FragmentData(int x, int y,Vector3 normal,Vector2 uv,Vector3 cameraDir,float depth){
        this.x = x;
        this.y = y;
        this.normal = normal;
        this.uv = uv;
        this.cameraDir = cameraDir;
        this.depth = depth;
    }
}
