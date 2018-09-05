package Engine.Core.Objects.Components;

import Engine.Algebra.Matrices.Matrix;
import Engine.Algebra.Matrices.MatrixUtilities;
import Engine.Algebra.Vectors.Vector2;
import Engine.Algebra.Vectors.Vector3;
import Engine.Algebra.Vectors.Vector4;
import Engine.Core.Objects.Component;
import Engine.Core.Scenes.Time;
import Engine.Core.Screen.Screen;
import Engine.Debug.DebugEngine;
import Engine.Graphics.Graphics2D.Canvas2D;
import Engine.Graphics.Graphics3D.Bounds;
import Engine.Graphics.Graphics3D.Plane;
import Engine.Graphics.Rendering.DrawCall;
import Engine.Graphics.Rendering.Rasterizer;
import Engine.Graphics.Rendering.VertexData;
import Engine.Utils.MathUtillities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Timer;

public class Camera extends Component {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Field of view of camera
     */
    public float fieldOfView = 60;
    /**
     * Vector containing near and far clipping plane distances
     */
    public Vector2 clippingPlanes = new Vector2(1f,100f);
    /**
     * Background color of camera
     */
    public Color background = new Color(63, 78, 93);

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Rasterizer rasterizer;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Camera constructor
     */
    public Camera(){
        rasterizer = new Rasterizer(this); // initialize rasterizer
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Base voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Awake void (called by the engine)
     */
    public void Awake() { }

    /**
     * Start void (called by the engine)
     */
    public void Start() { }

    /**
     * Update void (called by the engine)
     */
    public void Update() { }

    /**
     * Post render void (called by the engine)
     */
    public void PostRender() { }

    /**
     * Pre render void (called by the engine)
     */
    public BufferedImage PreRender(BufferedImage frame) {
        return frame;
    }

    /**
     * Destroy void (called by the engine)
     */
    public void Destroy() { }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Render scene through this camera
     * @return rendered frame
     */
    public BufferedImage Render(){
        return Render(null);
    }

    /**
     * Render scene through this camera
     * @param debug debug engine used to render debug rays
     * @return rendered frame
     */
    public BufferedImage Render(DebugEngine debug){
        Plane[] clippingPlanes = ExtractCameraPlanes();
        Matrix worldToCamera = WorldToCameraMatrix(); // calculate world to camera matrix
        ArrayList<MeshRenderer> renderers = this.GameObject().Scene().<MeshRenderer>FindGameObjectsOfTypes(new Class[]{
                MeshRenderer.class,
                AnimatedMeshRenderer.class
        }); // find all renderers in scene

        DrawCall[] drawCalls = new DrawCall[renderers.size()]; // Initialize draw call array

        for(int i = 0; i < renderers.size(); i++){ // loop through all rendereres
            drawCalls[i] = new DrawCall(renderers.get(i),worldToCamera); // initialize draw call
            drawCalls[i].SetCulled(FrustumCull(drawCalls[i],clippingPlanes)); // frustum culling

            if(!drawCalls[i].culled) { // only if not culled
                drawCalls[i].ActivateVertexShader(); // activate vertex shader
                drawCalls[i].ModelToRasterSpace(this); // convert vertices from model to raster space
            }
        }

        rasterizer.Rasterize(drawCalls,debug); // start rasterizer

        return rasterizer.FrameBuffer(); // return frame buffer
    }

    /**
     * Project point in camera space onto canvas
     * @param cameraPoint point in camera space
     * @return projected coordinate
     */
    public Vector4 ProjectCoordinate(Vector4 cameraPoint){
        cameraPoint = MatrixUtilities.Multiply(ProjectionMatrix(),cameraPoint);
        cameraPoint.NormalizeHomogeneous(false);
        return cameraPoint;
    }

    /**
     * Transform projected point to raster space
     * @param ProjectionPoint projected point
     * @return raster space coordinate
     */
    public Vector3 TransformToRasterSpace(Vector3 ProjectionPoint){
        return TransformToRasterSpace (new Vector4(ProjectionPoint.x(),ProjectionPoint.y(), ProjectionPoint.z(),1));
    }

    /**
     * Transform projected point to raster space
     * @param ProjectionPoint projected point
     * @return raster space coordinate
     */
    public Vector3 TransformToRasterSpace(Vector4 ProjectionPoint){
        Vector3 NDC = new Vector3( // normalize
                (ProjectionPoint.x() + 1) * 0.5f, // normalize x
                (ProjectionPoint.y() + 1) * 0.5f, // normalize y
                ProjectionPoint.z()
        );

        NDC.axis[0] = (int)(NDC.x() * Screen.I().CanvasSize().x()); // to raster space
        NDC.axis[1] = (int)((1 - NDC.y()) * Screen.I().CanvasSize().y()); // to raster space
        NDC.axis[2] = 1 / ProjectionPoint.z();

        return NDC;
    }

    /**
     * Transforms world coordinate to normalize device space (World -> Camera -> NDC)
     * @param world world coordinate
     * @return ndc coordinate
     */
    public Vector4 TransformWorldToNDC(Vector4 world){
       return ProjectCoordinate(MatrixUtilities.Multiply(WorldToCameraMatrix(),world)); // transform to camera space and then to ndc
    }

    /**
     * Check if point falls in clipping boundaries
     * @param point point to check
     * @return falls in clipping (true) or not (false)
     */
    public boolean PointInClip(Vector4 point){
        return (point.x() >= -1 && point.x() <= 1) && (point.y() <= 1 && point.y() >= -1);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private float[][] InitializeDepthBuffer(){
        float[][] depth = new float[Screen.I().getWidth()][Screen.I().getHeight()];
        ResetDepthBuffer(depth);
        return depth;
    }
    private void ResetDepthBuffer(float[][] depth){
        for(int x = 0; x < depth.length; x++){
            for(int y = 0; y < depth[x].length; y++){
                depth[x][y] = Float.MAX_VALUE;
            }
        }
    }

    // culling
    private boolean FrustumCull(DrawCall call,Plane[] clippingPlanes){
        Vector3[] corners = call.Mesh().bounds.WorldCorners(call.LocalToWorld());
        return !RectangleInFrustum(corners,clippingPlanes);
    }
    private boolean RectangleInFrustum(Vector3[] corners,Plane[] clippingPlanes){
        for(int i = 0; i < corners.length; i++){
            if(PointInFrustum(corners[i],clippingPlanes)){
                return true;
            }
        }
        return false;
    }
    private boolean PointInFrustum(Vector3 point, Plane[] clippingPlanes){
        for (int i = 0; i < clippingPlanes.length; i++) {
            if(clippingPlanes[i].DistanceToPlane(point) < 0){ // check if point is outside of plane
                return false;
            }
        }
        return true;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Transformation matrix camera to world
     * @return Transformation matrix camera to world
     */
    public Matrix CameraToWorldMatrix(){
        return MatrixUtilities.TransformMatrix(
                GameObject().Transform().Position(),
                GameObject().Transform().Front(),
                GameObject().Transform().Right(),
                GameObject().Transform().Up(),
                new Vector3(1,1,1)
        );
    }

    /**
     * Transformation matrix world to camera
     * @return Transformation matrix world to camera
     */
    public Matrix WorldToCameraMatrix(){
        return CameraToWorldMatrix().Inverse();
    }

    /**
     * Projection matrix
     * @return Projection matrix
     */
    public Matrix ProjectionMatrix(){
        return new Matrix(new float[][]{
                {CanvasScale(),0,0,0},
                {0,CanvasScale(),0,0},
                {0,0,-(clippingPlanes.y() / (clippingPlanes.y() - clippingPlanes.x())),-1},
                {0,0, -((clippingPlanes.y() * clippingPlanes.x()) / (clippingPlanes.y() - clippingPlanes.x())),0},
        });

    }

    /**
     * Returns all planes of the camera frustum
     * @return frustum planes
     */
    public Plane[] ExtractCameraPlanes(){
        // canvas scales
        Vector2 ns = nearSize().Multiply(0.5f);
        Vector2 fs = farSize().Multiply(0.5f);

        // cache directions
        Vector3 front = GameObject().Transform().Back();
        Vector3 right = GameObject().Transform().Right();
        Vector3 up = GameObject().Transform().Up();

        // near and far
        Vector3 fc = GameObject().Transform().Position().Add(front.Multiply(clippingPlanes.y()));
        Vector3 nc = GameObject().Transform().Position().Add(front.Multiply(clippingPlanes.x()));

        // far points
        Vector3 ftr = fc.Add(up.Multiply(fs.y())).Add(right.Multiply(fs.x()));
        Vector3 ftl = fc.Add(up.Multiply(fs.y())).Substract(right.Multiply(fs.x()));
        Vector3 fbl = fc.Substract(up.Multiply(fs.y())).Substract(right.Multiply(fs.x()));
        Vector3 fbr = fc.Substract(up.Multiply(fs.y())).Add(right.Multiply(fs.x()));

        // near points
        Vector3 ntl = nc.Add(up.Multiply(ns.y())).Substract(right.Multiply(ns.x()));
        Vector3 ntr = nc.Add(up.Multiply(ns.y())).Add(right.Multiply(ns.x()));
        Vector3 nbl = nc.Substract(up.Multiply(ns.y())).Substract(right.Multiply(ns.x()));
        Vector3 nbr = nc.Substract(up.Multiply(ns.y())).Add(right.Multiply(ns.x()));

        return new Plane[]{
                Plane.CreatePlane(ftr,fbl,fbr), // far clipping plane
                Plane.CreatePlane(ntr,nbr,nbl), // near clipping plane
                Plane.CreatePlane(ftr,ntr,ntl), // top plane
                Plane.CreatePlane(fbr,nbr,ntr), // right plane
                Plane.CreatePlane(fbl,nbl,nbr), // bottom plane
                Plane.CreatePlane(ftl,ntl,nbl), // left plane
        };
    }

    // Privates
    private Vector2 nearSize(){
        float h = 2f * clippingPlanes.x() * (float)Math.tan(MathUtillities.Deg2Rad * (fieldOfView * 0.5f));
        return new Vector2 (
                h * Screen.I().DeviceAspectRatio(),
                h
        );
    }
    private Vector2 farSize(){
        float h = 2f * clippingPlanes.y() * (float)Math.tan(MathUtillities.Deg2Rad * (fieldOfView * 0.5f));
        return new Vector2 (
                h * Screen.I().DeviceAspectRatio(),
                h
        );
    }
    private float CanvasScale(){
        return 1f / (float)Math.tan((fieldOfView / 2.0) * (Math.PI / 180.0));
    }
}
