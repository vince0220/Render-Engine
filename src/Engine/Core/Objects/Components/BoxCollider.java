package Engine.Core.Objects.Components;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.Component;
import Engine.Graphics.Graphics3D.Bounds;
import Engine.Physics.Collider;
import Engine.Utils.MathUtillities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BoxCollider extends Collider {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Bounds bound;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor box collider
     * @param center center of the box collider boundary. Relative to the transform position
     * @param size size of the box collider (Half the actual size, aka extends)
     */
    public BoxCollider(Vector3 center, Vector3 size){
        this(new Bounds(center,size));
    }

    /**
     * Constructor of box collider
     * @param bound boundary of the box collider
     */
    public BoxCollider(Bounds bound){
        this.bound = bound;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Base voids
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Update void (Called by the engine)
     */
    public void Update() { }

    /**
     * Post render (Called by the engine)
     */
    public void PostRender() { }

    /**
     * Pre render (Called by the engine)
     * @param frame
     * @return
     */
    public BufferedImage PreRender(BufferedImage frame) {
        return frame;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Abstract overrides
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Returns center of the boundary box of the collider (Relative to the transform)
     * @return center of the boundary box
     */
    @Override
    protected Vector3 Center() {
        return bound.center;
    }

    /**
     * Max radius of the collider
     * @return max radius
     */
    @Override
    public float MaxRadius() {
        return MathUtillities.GetRadius(bound);
    }

    /**
     * If box collider intersects collider
     * @param col other collider
     * @return true for intersection and false for no intersection
     */
    @Override
    public boolean Intersect(Collider col) {
        Vector3[] points1 = Points ();
        Vector3[] points2 = col.Points ();

        Vector3[] axis1 = SurfaceNormals ();
        Vector3[] axis2 = col.SurfaceNormals ();

        for (int i = 0; i < axis1.length; i++) {
            if (MathUtillities.IsSeperatingAxis (points1, points2, axis1[i])) {return false;}
            if (MathUtillities.IsSeperatingAxis (points1, points2, axis2 [i])) {return false;}
        }

        for (int i = 0; i < axis1.length; i++) {
            for (int y = 0; y < axis2.length; y++) {
                Vector3 axis = Vector3.Cross (axis1[i],axis2[y]);
                if (MathUtillities.IsSeperatingAxis (points1, points2,axis)) {
                        return false;
                }
            }
        }

        return true;
    }

    /**
     * Corner points of collider in world space
     * @return corner points
     */
    @Override
    public Vector3[] Points() {
        return bound.WorldCorners(GameObject().Transform().CartesianMatrix());
    }

    /**
     * World surface normals of the collider (front,up,right)
     * @return world surface normals (front,up,right)
     */
    @Override
    public Vector3[] SurfaceNormals() {
        return new Vector3[]{
                GameObject().Transform().Up(),
                GameObject().Transform().Front(),
                GameObject().Transform().Right()
        };
    }
}
