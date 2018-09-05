package Engine.Algebra.Vectors;

public class Vector3 extends Vector {

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector3(float x,float y,float z){super(new float[]{x,y,z});}
    public Vector3(float[] axis) { super(axis);}
    public Vector3(){super(3);}
    public Vector3(Vector4 vector){
        super(3);
        axis[0] = vector.x();
        axis[1] = vector.y();
        axis[2] = vector.z();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public functions
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public float x(){ return axis[0];}
    public float y(){ return axis[1];}
    public float z(){ return axis[2];}
    public float Dot(Vector3 vector){return Vector.Dot(this,vector);}
    public Vector3 Cross(Vector3 vector){
        return Vector3.Cross(this,vector);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Operators
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector3 Add(Vector3 b){
        return Vector3.Add(this,b,new Vector3());
    }
    public Vector3 Add(float val){
        return Vector3.Add(this,val,new Vector3());
    }
    public Vector3 Substract(Vector3 b){
        return Vector3.Substract(this,b,new Vector3());
    }
    public Vector3 Substract(float val){
        return Vector3.Substract(this,val,new Vector3());
    }
    public Vector3 Divide(Vector3 b){
        return Vector3.Divide(this,b,new Vector3());
    }
    public Vector3 Divide(float val){
        return Vector3.Divide(this,val,new Vector3());
    }
    public Vector3 Multiply(Vector3 b){
        return Vector3.Multiply(this,b,new Vector3());
    }
    public Vector3 Multiply(float val){
        return Vector3.Multiply(this,val,new Vector3());
    }
    public Vector3 Max(Vector3 b){
        return Vector.<Vector3>Max(this,b,new Vector3());
    }
    public Vector3 Min(Vector3 b){
        return Vector.<Vector3>Min(this,b,new Vector3());
    }

    // self operators
    public Vector3 AddToSelf(Vector3 b){
        this.axis[0] += b.axis[0];
        this.axis[1] += b.axis[1];
        this.axis[2] += b.axis[2];
        return this;
    }
    public Vector3 MinSelf(Vector3 b){
        return Vector.<Vector3>Min(this,b,this);
    }
    public Vector3 MaxSelf(Vector3 b){
        return Vector.<Vector3>Max(this,b,this);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public statics
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static Vector3 Cross(Vector3 a, Vector3 b){
        return new Vector3(
                (a.y() * b.z()) - (a.z() * b.y()),
                (a.z() * b.x()) - (a.x() * b.z()),
                (a.x() * b.y()) - (a.y() * b.x())
        );
    }

    // Defaults
    public static Vector3 Zero(){
        return new Vector3(0,0,0);
    }
    public static Vector3 One(){
        return new Vector3(1,1,1);
    }
    public static Vector3 Up(){
        return new Vector3(0,1,0);
    }
    public static Vector3 Down(){
        return new Vector3(0,-1,0);
    }
    public static Vector3 Forward(){
        return new Vector3(0,0,1);
    }
    public static Vector3 Back(){
        return new Vector3(0,0,-1);
    }
    public static Vector3 Right(){
        return new Vector3(1,0,0);
    }
    public static Vector3 Left(){
        return new Vector3(-1,0,0);
    }
}
