package Engine.Algebra.Vectors;

public class Vector2 extends Vector {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor of vector2
     * @param x X axis of the vector
     * @param y Y axis of the vector
     */
    public Vector2(float x,float y){ super(new float[]{x,y}); }

    /**
     * Empty constructor of vector2 (Vector.zero as default)
     */
    public Vector2(){ super(2); }

    /**
     * Constructor of vector2
     * @param axis axis array of new vector2. Has to be 2 in length
     */
    public Vector2(float[] axis) {
        super(axis);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Get x axis of vector
     * @return x axis
     */
    public float x(){
        return axis[0];
    }

    /**
     * Get y axis of vector
     * @return y axis
     */
    public float y(){
        return axis[1];
    }

    /**
     * Calculate dot product between current vector and target
     * @param vector target vector
     * @return dot product
     */
    public float Dot(Vector2 vector){return Vector.Dot(this,vector);}

    /**
     * Calculates cross product between current vector and target
     * @param vector target vector
     * @return cross product
     */
    public float Cross(Vector2 vector){
        return (x() * vector.y()) - (y()*vector.x());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Operators
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Add a vector to this vector (Returns a new vector instance)
     * @param b vector to add
     * @return new vector instance of a + b
     */
    public Vector2 Add(Vector2 b){
        return Vector2.Add(this,b,new Vector2());
    }

    /**
     * Add a float to this vector (Returns a new vector instance)
     * @param val float to add to a
     * @return new vector instance of a + val
     */
    public Vector2 Add(float val){
        return Vector2.Add(this,val,new Vector2());
    }

    /**
     * Substract this vector with b (Returns a new vector instance)
     * @param b vector to substract
     * @return new vector instance of a - b
     */
    public Vector2 Substract(Vector2 b){
        return Vector2.Substract(this,b,new Vector2());
    }

    /**
     * Substract this vector with val (Returns a new vector instance)
     * @param val float to substract
     * @return new vector instance of a - val
     */
    public Vector2 Substract(float val){
        return Vector2.Substract(this,val,new Vector2());
    }

    /**
     * Divide this vector by b (Returns a new vector instance)
     * @param b vector to divide a by
     * @return new vector instance of a / b
     */
    public Vector2 Divide(Vector2 b){
        return Vector2.Divide(this,b,new Vector2());
    }

    /**
     * Divide this vector by float (Returns a new vector instance)
     * @param val float to divide a by
     * @return new vector instance of a / val
     */
    public Vector2 Divide(float val){
        return Vector2.Divide(this,val,new Vector2());
    }

    /**
     * Multiply this vector by b (Returns a new vector instance)
     * @param b vector to multiply with
     * @return new vector instance of a * b
     */
    public Vector2 Multiply(Vector2 b){
        return Vector2.Multiply(this,b,new Vector2());
    }

    /**
     * Multiply this vector by float (Returns a new vector instance)
     * @param val float to multiply with
     * @return new vector instance of a * val
     */
    public Vector2 Multiply(float val){
        return Vector2.Multiply(this,val,new Vector2());
    }

    /**
     * Max vector between a and b
     * @param b vector
     * @return max vector
     */
    public Vector2 Max(Vector2 b){
        return Vector.<Vector2>Max(this,b,new Vector2());
    }

    /**
     * Min vector between a and b
     * @param b vector
     * @return min vector
     */
    public Vector2 Min(Vector2 b){
        return Vector.<Vector2>Min(this,b,new Vector2());
    }
}
