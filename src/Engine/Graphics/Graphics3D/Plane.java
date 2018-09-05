package Engine.Graphics.Graphics3D;

import Engine.Algebra.Vectors.Vector3;

public class Plane {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector3 normal;
    public float distance;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Plane(Vector3 normal, float distance){
        this.normal = normal;
        this.distance = distance;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public float DistanceToPlane(Vector3 point){
        return Vector3.Dot (normal,point) + distance;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public static voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static Plane CreatePlane(Vector3 p0, Vector3 p1, Vector3 p2){
        Vector3 v = p1.Substract(p0);
        Vector3 u = p2.Substract(p0);
        Vector3 normal = (Vector3) Vector3.Cross (v,u).Normalize();
        return new Plane (normal,-Vector3.Dot (normal,p0));
    }
}