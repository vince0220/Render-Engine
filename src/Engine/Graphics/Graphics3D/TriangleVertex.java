package Engine.Graphics.Graphics3D;

public class TriangleVertex {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public int vertIndex;
    public int normalIndex;
    public int uvIndex;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public TriangleVertex(int vertIndex, int uvIndex, int normalIndex){
        this.vertIndex = vertIndex;
        this.uvIndex = uvIndex;
        this.normalIndex = normalIndex;
    }
}
