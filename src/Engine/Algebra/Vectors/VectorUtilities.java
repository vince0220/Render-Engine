package Engine.Algebra.Vectors;

public final class VectorUtilities {
    /**
     * Add value to a
     * @param a vector
     * @param b vector
     * @return a with b added to itself
     */
    public static Vector3 AddToA(Vector3 a, Vector3 b){
        return AddToAWeighted(a,b,1f);
    }

    /**
     * Add b to a based on the given weight
     * @param a vector
     * @param b vector
     * @param weight weight of b add
     * @return weight added vector
     */
    public static Vector3 AddToAWeighted(Vector3 a, Vector3 b, float weight){
        a.axis[0] += b.axis[0] * weight;
        a.axis[1] += b.axis[1] * weight;
        a.axis[2] += b.axis[2] * weight;
        return a;
    }
}
