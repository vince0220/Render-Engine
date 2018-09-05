package Engine.Utils;

import Engine.Algebra.Vectors.Vector2;
import Engine.Algebra.Vectors.Vector3;
import Engine.Graphics.Graphics3D.Bounds;

public class MathUtillities {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static final float Deg2Rad = 0.0174532925f;
    public static final float Rad2Deg = 57.2957795f;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Calculations
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static float GetRadius(final Bounds bounds){
        return (float) (0.5*Math.sqrt(Math.pow(bounds.size.x()*2,2)+Math.pow(bounds.size.y()*2,2)+Math.pow(bounds.size.z()*2,2)));
    }
    public static boolean IsSeperatingAxis(Vector3[] p1, Vector3[] p2, Vector3 axis){
        Vector2 MinMax1 = ProjectionMinMax (p1,axis);
        Vector2 MinMax2 = ProjectionMinMax (p2, axis);

        float span = Math.max (MinMax1.y(), MinMax2.y()) - Math.min (MinMax1.x(),MinMax2.x()); // calculate total length
        float sum = (MinMax1.y() - MinMax1.x()) + (MinMax2.y() - MinMax2.x()); // (length of 1 plus length of 2)

        return (span > sum); // if the total length is lower then the sum there is no overlap
    }
    public static Vector2 ProjectionMinMax(Vector3[] points, Vector3 axis){
        axis.Normalize();
        Vector2 MinMax = new Vector2 (Float.MAX_VALUE,Float.MIN_VALUE);

        for (int i = 0; i < points.length; i++) {
            float projection = Vector3.Dot (axis,points[i]);
            MinMax.axis[0] = Math.min (projection,MinMax.x());
            MinMax.axis[1] = Math.max (projection,MinMax.y());
        }
        return MinMax;
    }
    public static float Clamp(float val, float min, float max){
        if(val > max){val = max;}
        if(val < min){val = min;}
        return val;
    }
    public static float FastLerp(float a,float b,float t){
        return a + t * (b - a);
    }
    public static float Lerp(float a, float b, float t){
        return (1 - t) * a + t * b;
    }
}
