package Engine.Algebra.Matrices;

import Engine.Algebra.Vectors.Vector3;
import Engine.Algebra.Vectors.Vector4;
import Engine.Utils.MathUtillities;

public final class MatrixUtilities {
    /**
     * Generate transformation matrix
     * @param Translation transform translation
     * @param Rotation transform rotation
     * @param Scale transform scale
     * @return transformation matrix
     */
    public static Matrix TransformMatrix(Vector3 Translation, Vector3 Rotation, Vector3 Scale){
        Rotation = Rotation.Multiply(MathUtillities.Deg2Rad); // convert rotation to radians

        Matrix Cartesian = new Matrix(4,4);
        Cartesian.data[0][0] = Scale.x(); // set scale of matrix m (X Axis)
        Cartesian.data[1][1] = Scale.y(); // set scale of matrix m (Y Axis)
        Cartesian.data[2][2] = Scale.z(); // set scale of matrix m (Z Axis)
        Cartesian.data[0][3] = Translation.x(); // set Translation of matrix m (X Axis)
        Cartesian.data[1][3] = Translation.y(); // set Translation of matrix m (Y Axis)
        Cartesian.data[2][3] = Translation.z(); // set Translation of matrix m (Z Axis)
        Cartesian.data[3][3] = 1;

        // Rotation matrices
        Matrix Rx = new Matrix(4,4); // X axis
        Rx.data[0][0] = 1;
        Rx.data[1][1] = (float)Math.cos(Rotation.x());
        Rx.data[1][2] = (float)-Math.sin(Rotation.x());
        Rx.data[2][1] = (float)Math.sin(Rotation.x());
        Rx.data[2][2] = (float)Math.cos(Rotation.x());
        Rx.data[3][3] = 1;

        Matrix Ry = new Matrix(4,4); // Y axis
        Ry.data[0][0] = (float)Math.cos(Rotation.y());
        Ry.data[0][2] = (float)Math.sin(Rotation.y());
        Ry.data[1][1] = 1;
        Ry.data[2][0] = (float)-Math.sin(Rotation.y());
        Ry.data[2][2] = (float)Math.cos(Rotation.y());
        Ry.data[3][3] = 1;

        Matrix Rz = new Matrix(4,4); // Z axis
        Rz.data[0][0] = (float)Math.cos(Rotation.z());
        Rz.data[0][1] = (float)-Math.sin(Rotation.z());
        Rz.data[1][0] = (float)Math.sin(Rotation.z());
        Rz.data[1][1] = (float)Math.cos(Rotation.z());
        Rz.data[2][2] = 1;
        Rz.data[3][3] = 1;

        return Cartesian.Multiply(Ry).Multiply(Rx).Multiply(Rz); // merge transformations
    }

    /**
     * Generate transformation matrix
     * @param Translation transform translation
     * @param Forward forward of the transform
     * @param Right right of the transform
     * @param Up up of the transform
     * @param Scale transform scale
     * @return transformation matrix
     */
    public static Matrix TransformMatrix(Vector3 Translation, Vector3 Forward,Vector3 Right, Vector3 Up, Vector3 Scale){
        Matrix Base = new Matrix(4,4);
        Base.data[0][0] = Scale.x();
        Base.data[1][1] = Scale.y();
        Base.data[2][2] = Scale.z();
        Base.data[3][3] = 1;

        Matrix RT = new Matrix(new float[][]{
                {Right.x(),Up.x(),Forward.x(),Translation.x()},
                {Right.y(),Up.y(),Forward.y(),Translation.y()},
                {Right.z(),Up.z(),Forward.z(),Translation.z()},
                {0,0,0,1}
        });

        return Base.Multiply(RT);
    }

    /**
     * Multiply a vector by a matrix
     * @param m matrix
     * @param vector vector
     * @return transformed vector
     */
    public static Vector3 Multiply(Matrix m, Vector3 vector){
        return Multiply(m,vector,0f);
    }

    /**
     * Multiply a vector by a matrix with a custom given homogenous coordinate w
     * @param m matrix
     * @param vector vector
     * @param w homogenous coordinate w
     * @return transformed vector
     */
    public static Vector3 Multiply(Matrix m, Vector3 vector,float w){
        if(m.columns != 4 && m.rows != 4){throw new RuntimeException("Illegal matrix dimensions.");}
        Matrix result = m.Multiply(new Matrix(new float[][]{
                {vector.x()},
                {vector.y()},
                {vector.z()},
                {w}
        }));

        return new Vector3(result.data[0][0],result.data[1][0],result.data[2][0]);
    }

    /**
     * Multiply a vector by a matrix
     * @param m matrix
     * @param vector vector
     * @return transformed vector
     */
    public static Vector4 Multiply(Matrix m, Vector4 vector){
        if(m.columns != 4 && m.rows != 4){throw new RuntimeException("Illegal matrix dimensions.");}
        Matrix result = m.Multiply(new Matrix(new float[][]{
                {vector.x()},
                {vector.y()},
                {vector.z()},
                {vector.w()}
        }));
        return new Vector4(result.data[0][0],result.data[1][0],result.data[2][0],result.data[3][0]);
    }
}
