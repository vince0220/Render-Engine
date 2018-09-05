package Engine.Algebra.Vectors;

public class Vector4 extends Vector {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector4(float x,float y,float z,float w){super(new float[]{x,y,z,w});}
    public Vector4(float[] axis) { super(axis);}
    public Vector4(){super(4);}
    public Vector4(Vector3 vector, float w){
        this(vector.x(),vector.y(),vector.z(),w);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public functions
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public float x(){ return axis[0];}
    public float y(){ return axis[1];}
    public float z(){ return axis[2];}
    public float w(){ return axis[3];}
    public float Dot(Vector4 vector){return Vector.Dot(this,vector);}

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Operators
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector4 Add(Vector4 b){
        return Vector4.Add(this,b,new Vector4());
    }
    public Vector4 Add(float val){
        return Vector4.Add(this,val,new Vector4());
    }
    public Vector4 Substract(Vector4 b){
        return Vector4.Substract(this,b,new Vector4());
    }
    public Vector4 Substract(float val){
        return Vector4.Substract(this,val,new Vector4());
    }
    public Vector4 Divide(Vector4 b){
        return Vector4.Divide(this,b,new Vector4());
    }
    public Vector4 Divide(float val){
        return Vector4.Divide(this,val,new Vector4());
    }
    public Vector4 Multiply(Vector4 b){
        return Vector4.Multiply(this,b,new Vector4());
    }
    public Vector4 Multiply(float val){
        return Vector4.Multiply(this,val,new Vector4());
    }
    public void NormalizeHomogeneous(boolean w){
        for(int i = 0; i < axis.length; i++){
            if(i == axis.length - 1 && !w){break;} // dont normalize w
            axis[i] = axis[i] / w();
        }
    }
    public Vector4 Max(Vector4 b){
        return Vector.<Vector4>Max(this,b,new Vector4());
    }
    public Vector4 Min(Vector4 b){
        return Vector.<Vector4>Min(this,b,new Vector4());
    }
}
