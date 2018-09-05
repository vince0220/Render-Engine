package Engine.Graphics.Graphics3D;

import Engine.Algebra.Matrices.Matrix;
import Engine.Algebra.Matrices.MatrixUtilities;
import Engine.Algebra.Vectors.Vector3;

public class Bounds {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // -----------------------------------------------------------------------------------------------------------------------------------------
    public Vector3 center;
    public Vector3 size;
    public Vector3[] corners;


    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------------------------------------------------------------------------
    public Bounds(Vector3 center, Vector3 size){
        this.center = center;
        this.size = size;
        RecalculateCorners();
    }
    public Bounds(){
        center = new Vector3();
        size = new Vector3();
        RecalculateCorners();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void InitializeMinMax(Vector3 min, Vector3 max){
        center = min.Add(max).Multiply(0.5f);
        size = max.Substract(center);
        RecalculateCorners(); // recalculate corners
    }
    public void RecalculateCorners(){
        corners = new Vector3[]{
                new Vector3(center.x() - size.x(),center.y() - size.y(),center.z() - size.z()), // (left / bottom / back)
                new Vector3(center.x() + size.x(),center.y() - size.y(),center.z() - size.z()), // (right / bottom / back)
                new Vector3(center.x() - size.x(),center.y() + size.y(),center.z() - size.z()), // (left / top / back)
                new Vector3(center.x() + size.x(),center.y() + size.y(),center.z() - size.z()), // (right / top / back)

                new Vector3(center.x() + size.x(),center.y() + size.y(),center.z() + size.z()), // (right / top / front)
                new Vector3(center.x() - size.x(),center.y() + size.y(),center.z() + size.z()), // (left / top / front)
                new Vector3(center.x() + size.x(),center.y() - size.y(),center.z() + size.z()), // (right / bottom / front)
                new Vector3(center.x() - size.x(),center.y() - size.y(),center.z() + size.z()), // (left / bottom / front)
        };
    }
    public Vector3[] WorldCorners(Matrix localToWorld){
        return new Vector3[]{
                MatrixUtilities.Multiply(localToWorld,corners[0],1f), // (left / bottom / back) 0
                MatrixUtilities.Multiply(localToWorld,corners[1],1f), // (right / bottom / back) 1
                MatrixUtilities.Multiply(localToWorld,corners[2],1f), // (left / top / back) 2
                MatrixUtilities.Multiply(localToWorld,corners[3],1f), // (right / top / back) 3
                MatrixUtilities.Multiply(localToWorld,corners[4],1f), // (right / top / front) 4
                MatrixUtilities.Multiply(localToWorld,corners[5],1f), // (left / top / front) 5
                MatrixUtilities.Multiply(localToWorld,corners[6],1f), // (right / bottom / front) 6
                MatrixUtilities.Multiply(localToWorld,corners[7],1f), // (left / bottom / front) 7
        };
    }
}
