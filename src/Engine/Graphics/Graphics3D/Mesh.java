package Engine.Graphics.Graphics3D;

import Engine.Algebra.Vectors.Vector2;
import Engine.Algebra.Vectors.Vector3;

public class Mesh {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector3[] vertices;
    public Vector3[] normals;
    public Vector2[] uvs;
    public TriangleVertex[] triangles;
    public Bounds bounds;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Mesh(){}
    public Mesh(
            Vector3[] vertices,
            Vector3[] normals,
            Vector2[] uvs,
            TriangleVertex[] triangles
    ){
        this.vertices = vertices;
        this.normals = normals;
        this.uvs = uvs;
        this.triangles = triangles;
        RecalculateBounds();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public void
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void RecalculateBounds(){
        Vector3 min = new Vector3();
        Vector3 max = new Vector3();
        for(int i = 0; i < vertices.length; i++) {
            min = min.MinSelf(vertices[i]);
            max = max.MaxSelf(vertices[i]);}

        bounds = new Bounds();
        bounds.InitializeMinMax(min,max);
    }
}
