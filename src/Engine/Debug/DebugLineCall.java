package Engine.Debug;

import Engine.Algebra.Vectors.Vector3;
import Engine.Algebra.Vectors.Vector4;

import java.awt.*;

public class DebugLineCall {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector4 from;
    public Vector4 to;
    public Color col;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private long EndTime;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public DebugLineCall(float duration, Vector3 from, Vector3 to,Color col){
        EndTime = System.currentTimeMillis() + ((int)(duration * 1000));
        this.from = new Vector4(from,1f);
        this.to = new Vector4(to,1f);
        this.col = col;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public boolean Active(){
        return System.currentTimeMillis() < EndTime;
    }
}