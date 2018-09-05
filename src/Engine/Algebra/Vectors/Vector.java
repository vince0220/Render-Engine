package Engine.Algebra.Vectors;

public class Vector {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public float[] axis;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor of vector
     * @param axis array containing all the vector axis
     */
    public Vector(float[] axis){ this.axis = axis;}

    /**
     * Constructor of vector
     * @param axisCount count of how many axis the vector should have
     */
    public Vector(int axisCount){ this(new float[axisCount]);}

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Normalize this vector and returns itself (Does not make new vector)
     * @return
     */
    public Vector Normalize(){
        float magnitude = Magnitude();
        for(int i = 0; i < axis.length; i++){
            axis[i] /= magnitude;
        }
        return this;
    }

    /**
     * Magnitude(length) of vector
     * @return magnitude
     */
    public float Magnitude(){
        float Sum = 0f;
        for(int i = 0; i < axis.length; i++){
            Sum += axis[i] * axis[i];
        }
        return (float)Math.sqrt((float)Sum);
    }

    /**
     * Converts vector to string
     * @return vector as string
     */
    public String toString(){
        String string = new String();
        for(int i = 0; i < axis.length; i++){
            string += axis[i];
            if(i != (axis.length - 1)){
                string += " / ";
            }
        }
        return string;
    }

    /**
     * Distance between self and to vector
     * @param to vector you want to calculate distance to
     * @return distance between self and to
     */
    public float Distance(Vector to){
        return Math.abs(Substract(this,to,new Vector(to.axis.length)).Magnitude());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public static
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Checks if the axis length of a and b match
     * @param a vector a
     * @param b vector b
     * @return if the axis length of a and b match or not (True = yes, False = no)
     */
    public static boolean Match(Vector a, Vector b){
        return a.axis.length == b.axis.length;
    }

    /**
     * Dot product between vector a and b
     * @param a vector
     * @param b vector
     * @return dot product
     */
    public static float Dot(Vector a, Vector b){
        if(a.axis.length == b.axis.length) {
            float dot = 0f;
            for (int i = 0; i < a.axis.length; i++) {
                dot += a.axis[i] * b.axis[i];
            }
            return dot;
        }
        return  0f;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Static Operators
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Substract b from a and store result in given result parameter
     * @param a vector
     * @param b vector
     * @param result vector to store the result in
     * @param <T> generic return type (Has to be vector)
     * @return subtracted result vector
     */
    public static <T extends Vector> T Substract(T a, T b,T result){
        if(Match(a,b)) {
            for (int i = 0; i < result.axis.length; i++) {
                result.axis[i] = a.axis[i] - b.axis[i];
            }
            return result;
        }
        return result;
    }

    /**
     * Substract b from a and store result in given result parameter
     * @param a vector
     * @param val value to remove from b
     * @param result vector to store the result in
     * @param <T> generic return type (Has to be vector)
     * @return subtracted result vector
     */
    public static <T extends Vector> T Substract(T a, float val,T result){
        for (int i = 0; i < result.axis.length; i++) {
            result.axis[i] = a.axis[i] - val;
        }
        return result;
    }

    /**
     * Add b to a and store result in given result parameter
     * @param a vector
     * @param b vector
     * @param result vector to store result in
     * @param <T> generic return type (Has to be vector)
     * @return added result vector
     */
    public static <T extends Vector> T Add(T a, T b,T result){
        if(Match(a,b)) {
            for (int i = 0; i < result.axis.length; i++) {
                result.axis[i] = a.axis[i] + b.axis[i];
            }
            return result;
        }
        return result;
    }

    /**
     * Add b to a and store result in given result parameter
     * @param a vector
     * @param val float value to add to a
     * @param result vector to store result in
     * @param <T> generic return type (Has to be vector)
     * @return added result vector
     */
    public static <T extends Vector> T Add(T a, float val,T result){
        for (int i = 0; i < result.axis.length; i++) {
            result.axis[i] = a.axis[i] + val;
        }
        return result;
    }

    /**
     * Divide a by b and store result in given result parameter
     * @param a vector
     * @param b vector
     * @param result vector to store result in
     * @param <T> generic return type (Has to be vector)
     * @return divided result vector
     */
    public static <T extends Vector> T Divide(T a, T b,T result){
        if(Match(a,b)) {
            for (int i = 0; i < result.axis.length; i++) {
                result.axis[i] = a.axis[i] / b.axis[i];
            }
            return result;
        }
        return result;
    }

    /**
     * Divide a by b and store result in given result parameter
     * @param a vector
     * @param val float to divide a with
     * @param result vector to store result in
     * @param <T> generic return type (Has to be vector)
     * @return divided result vector
     */
    public static <T extends Vector> T Divide(T a, float val,T result){
        for (int i = 0; i < result.axis.length; i++) {
            result.axis[i] = a.axis[i] / val;
        }
        return result;
    }

    /**
     * Multiply a by b and store result in given result parameter
     * @param a vector
     * @param b vector
     * @param result vector to store result in
     * @param <T> generic return type (Has to be vector)
     * @return multiplied result vector
     */
    public static <T extends Vector> T Multiply(T a, T b,T result){
        if(Match(a,b)) {
            for (int i = 0; i < result.axis.length; i++) {
                result.axis[i] = a.axis[i] * b.axis[i];
            }
            return result;
        }
        return result;
    }

    /**
     * Multiply a by b and store result in given result parameter
     * @param a vector
     * @param val value to multiply a with
     * @param result vector to store result in
     * @param <T> generic return type (Has to be vector)
     * @return multiplied result vector
     */
    public static <T extends Vector> T Multiply(T a, float val, T result){
        for (int i = 0; i < result.axis.length; i++) {
            result.axis[i] = a.axis[i] * val;
        }
        return result;
    }

    /**
     * return minimal value of a and b
     * @param a vector
     * @param b vector
     * @param result vector to store result in
     * @param <T> generic return type (Has to be vector)
     * @return minimal result vector
     */
    public static <T extends Vector> T Min(T a, T b, T result){
        for(int i = 0; i < result.axis.length; i++){
            result.axis[i] = Math.min(a.axis[i],b.axis[i]);
        }
        return result;
    }

    /**
     * return maximal value of a and b
     * @param a vector
     * @param b vector
     * @param result vector to store result in
     * @param <T> generic return type (Has to be vector)
     * @return maximal result vector
     */
    public static <T extends Vector> T Max(T a, T b, T result){
        for(int i = 0; i < result.axis.length; i++){
            result.axis[i] = Math.max(a.axis[i],b.axis[i]);
        }
        return result;
    }
}
