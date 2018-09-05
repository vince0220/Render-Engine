package Engine.Graphics.Rendering;

import Engine.Algebra.Vectors.*;

import java.awt.*;

public class VertexData {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector4 vertex;
    public Vector4 projection;
    public Vector3 normal;
    public Vector3 screen;
    public Vector3 color = new Vector3(0,0,0);
    public Vector2 uv;

    // cache variables
    public float InverseW;
    public Vector3 dividedNormal;
    public Vector2 dividedUV;


    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public VertexData(Vector4 vertex, Vector3 normal, Vector2 uv){
        this.vertex = vertex;
        this.normal = normal;
        this.uv = uv;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void SetProjection(Vector4 projection){
        this.projection = projection;
        ReCache();
    }
    public Vector GetAttributeByIndex(int index){
        switch (index){
            case 0:
                return vertex;
            case 1:
                return projection;
            case 2:
                return normal;
            case 3:
                return screen;
            case 4:
                return uv;
            case 5:
                return color;
        }
        return vertex;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void ReCache(){
        this.InverseW = 1f / this.projection.w();
        this.dividedNormal = normal.Divide(this.projection.w());
        this.dividedUV = uv.Divide(this.projection.w());
    }
}
