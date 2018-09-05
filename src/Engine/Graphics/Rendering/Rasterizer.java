package Engine.Graphics.Rendering;

import Engine.Algebra.Vectors.*;
import Engine.Core.Objects.Components.Camera;
import Engine.Core.Scenes.Time;
import Engine.Core.Screen.Screen;
import Engine.Debug.DebugEngine;
import Engine.Graphics.Graphics2D.Canvas2D;
import Engine.Utils.Utilities;
import sun.security.provider.certpath.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Rasterizer {

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Canvas2D _canvas;
    private float[][] _depthBuffer;
    private Vector2 _canvasSize;
    private Camera _cam;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Rasterizer(Camera cam){
        _cam = cam;
        _canvasSize = new Vector2(Screen.I().CanvasSize().x(),Screen.I().CanvasSize().y());
        _canvas = new Canvas2D(_canvasSize);
        InitializeDepthBuffer();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void Rasterize(DrawCall[] drawCalls, DebugEngine debug){
        ResetDepthBuffer(_depthBuffer); // reset depth buffer
        _canvas.Clear(_cam.background); // reset canvas

        // rasterize draw calls
        for(int i = 0; i < drawCalls.length; i++){
            if(!drawCalls[i].culled) { RasterizeDrawCall(drawCalls[i]);}
        }

        // overlay debug
        if(debug != null){debug.OverlayDebug(_canvas,_cam);} // overlay debug
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public BufferedImage FrameBuffer(){
        return _canvas.Frame();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void RasterizeDrawCall(DrawCall call){
        VertexData[] Buffer = call.VertexBuffer(); // get vertex buffer

        int triangleIndex = 0;
        for(int i = 0; i < call.Mesh().triangles.length / 3; i++){
            VertexData v0 = Buffer[call.Mesh().triangles[triangleIndex++].vertIndex];
            VertexData v1 = Buffer[call.Mesh().triangles[triangleIndex++].vertIndex];
            VertexData v2 = Buffer[call.Mesh().triangles[triangleIndex++].vertIndex];

            if(!Clip(v0,v1,v2)) {
                RasterizeTriangle(call, v0,v1,v2);
            }
        }
    }
    private void RasterizeTriangle(DrawCall call,VertexData v0,VertexData v1, VertexData v2){
        if(!Cull(call,v0,v1,v2)) { // culling
            Vector2[] minmax = CalculateMinMax(v0,v1,v2); // calculate bounds of triangle
            //OrderTriangle(vertices); // order triangle clock wise
            float area = 1f / TriangleArea(v0, v1, v2); // calculate triangle area
            if(area <= 0){return;} // if triangle doesnt have an area return since you cant divide by zero later on

            // cache fragment required values to save array lookup time
            Vector3[] edges = CalculateTriangleEdge(v0,v1,v2);
            boolean rule1 = CalculateTriangleEdgeRule(edges[0]);
            boolean rule2 = CalculateTriangleEdgeRule(edges[1]);
            boolean rule3 = CalculateTriangleEdgeRule(edges[2]);

            // min max cache
            int minY = (int)minmax[0].y();
            int maxY = (int)minmax[1].y();
            int minX = (int)minmax[0].x();
            int maxX = (int)minmax[1].x();
            float depthCache = 0f;
            Vector3 camDir = new Vector3(0,0,1);



                for (int y = minY; y <= maxY; ++y) { // loop through y pixels
                    for (int x = minX; x <= maxX; ++x) { // loop through x pixels
                        // seperate floats to avoid initialization of float arrays
                        float w1 = TriangleArea(x,y, v1.screen, v2.screen) * area;
                        float w2 = TriangleArea(x,y, v2.screen, v0.screen) * area;
                        float w3 = TriangleArea(x,y, v0.screen, v1.screen) * area;

                        if (OverlapsTriangle(rule1,rule2,rule3,w1,w2,w3)) { // triangle overlap test / Top left test
                            depthCache = CalculateDepth(v0,v1,v2,w1,w2,w3);
                            if ((_depthBuffer[x][y] > depthCache)) { // perform depth test
                                _depthBuffer[x][y] = depthCache;
                                _canvas.SetPixel(x, y,call.ShadeFragment(InitializeFragmentData(x,y, v0,v1,v2,w1,w2,w3,camDir,depthCache))); // shade fragment and draw in canvas
                            }
                        }
                    }
            }

        }
    }

    private Vector3 SurfaceNormal(VertexData[] vertices){
        Vector4 U4 = vertices[0].vertex.Substract(vertices[1].vertex);
        Vector4 V4 = vertices[2].vertex.Substract(vertices[0].vertex);

        Vector3 U = new Vector3(U4.x(),U4.y(),U4.z());
        Vector3 V = new Vector3(V4.x(),V4.y(),V4.z());

        Vector3 Normal = U.Cross(V);
        Normal.Normalize();

        return Normal;
    }

    // Initialization
    private FragmentData InitializeFragmentData(int x, int y,VertexData v0,VertexData v1, VertexData v2,float w0,float w1,float w2,Vector3 camDir,float depth){
        return new FragmentData(
                x,
                y,
                PerspectiveCorrection(v0.dividedNormal,v1.dividedNormal,v2.dividedNormal,v0,v1,v2,w0,w1,w2,depth),
                PerspectiveCorrection(v0.dividedUV,v1.dividedUV,v2.dividedUV,v0,v1,v2,w0,w1,w2,depth),
                camDir,
                depth
        );
    }

    private void InitializeDepthBuffer(){
        _depthBuffer = new float[(int)_canvasSize.x()][(int)_canvasSize.y()];
        ResetDepthBuffer(_depthBuffer);
    }
    private void ResetDepthBuffer(float[][] depth){
        for(int x = 0; x < depth.length; x++){
            for(int y = 0; y < depth[x].length; y++){
                depth[x][y] = Float.MAX_VALUE;
            }
        }
    }

    // Calculations
    private Vector3 PerspectiveCorrection(Vector3 a, Vector3 b, Vector3 c,VertexData v0,VertexData v1, VertexData v2,float w0,float w1,float w2,float depth){
        float w = 1 / ((v0.InverseW) * w0 + (v1.InverseW) * w1 + (v2.InverseW) * w2);

        return new Vector3(
                ((a.x() * w0) + (b.x() * w1) + (c.x() * w2)) * w,
                ((a.y() * w0) + (b.y() * w1) + (c.y() * w2)) * w,
                ((a.z() * w0) + (b.z() * w1) + (c.z() * w2)) * w
        );
    }
    private Vector2 PerspectiveCorrection(Vector2 a, Vector2 b,Vector2 c,VertexData v0,VertexData v1, VertexData v2,float w0,float w1,float w2,float depth){
        float w = 1 / ((v0.InverseW) * w0 + (v1.InverseW) * w1 + (v2.InverseW) * w2);

        return new Vector2(
                ((a.x() * w0) + (b.x() * w1) + (c.x() * w2)) * w,
                ((a.y() * w0) + (b.y() * w1) + (c.y() * w2)) * w
        );
    }
    private boolean Cull(DrawCall call,VertexData v0,VertexData v1,VertexData v2){
        return Cull(call,v0,v1,v2,new Vector3(0,0,1));
    }
    private boolean Cull(DrawCall call,VertexData v0,VertexData v1,VertexData v2,Vector3 dir){
        return dir.Dot(CalculateTriangleProjectionNormal(v0,v1,v2).Multiply(call.Shader().Culling())) <= 0f;
    }
    private boolean Clip(VertexData v0,VertexData v1, VertexData v2){
        int clipCount = 0;

        if(Clip(v0)){clipCount++;}
        if(Clip(v1)){clipCount++;}
        if(Clip(v2)){clipCount++;}

        return (clipCount >= 3);
    }
    private boolean Clip(VertexData v){
        return (v.projection.x() < -1 || v.projection.x() > 1 || v.projection.z() < 0 || v.projection.z() > 1);
    }
    private boolean DepthTest(int x, int y, float Depth){
        if(_depthBuffer[x][y] > Depth){
            _depthBuffer[x][y] = Depth;
            return true;
        }
        return false;
    }
    private float CalculateDepth(VertexData v0, VertexData v1, VertexData v2, float w1, float w2, float w3){
        return 1f / ((v0.screen.z() * w1) + (v1.screen.z() * w2) + (v2.screen.z() * w3));
    }
    private boolean CalculateTriangleEdgeRule(Vector3 edge){
        return edge.y() == 0 && edge.x() > 0;
    }
    private Vector3[] CalculateTriangleEdge(VertexData v0,VertexData v1, VertexData v2){
        return new Vector3[]{
                v2.screen.Substract(v1.screen),
                v0.screen.Substract(v2.screen),
                v1.screen.Substract(v0.screen)
        };
    }
    private Vector2[] CalculateMinMax(VertexData v1, VertexData v2, VertexData v3){
        Vector2[] minmax = new Vector2[]{
                new Vector2(Float.MAX_VALUE,Float.MAX_VALUE),
                new Vector2(-Float.MAX_VALUE,-Float.MAX_VALUE)
        };

        // Calculate min max
        CalculateMinMax(v1,minmax);
        CalculateMinMax(v2,minmax);
        CalculateMinMax(v3,minmax);

        // Clamp in canvas
        for(int i = 0; i < minmax.length; i++){
            minmax[i].axis[0] = Math.max(0,Math.min(_canvasSize.x() - 1,(int)minmax[i].x()));  // clamp X
            minmax[i].axis[1] = Math.max(0,Math.min(_canvasSize.y() - 1,(int)minmax[i].y()));  // clamp Y
        }

        return minmax;
    }
    private void CalculateMinMax(VertexData v,Vector2[] minmax){
        if (v.screen.x() < minmax[0].x()) minmax[0].axis[0] = v.screen.x();
        if (v.screen.y() < minmax[0].y()) minmax[0].axis[1] = v.screen.y();
        if (v.screen.x() > minmax[1].x()) minmax[1].axis[0] = v.screen.x();
        if (v.screen.y() > minmax[1].y()) minmax[1].axis[1] = v.screen.y();
    }
    private boolean OverlapsTriangle(boolean[] edges,float[] weights){
        for (int i = 0; i < 3; i++) {
            if(weights[i] < 0 || (weights[i] == 0 && edges[i])){ return false; }
        }
        return true;
    }
    private boolean OverlapsTriangle(boolean e1,boolean e2, boolean e3,float w1,float w2, float w3){ // faster methode
        if(w1 < 0 || (e1 && w1 == 0)){ return false; }
        if(w2 < 0 || (e2 && w2 == 0)){ return false; }
        if(w3 < 0 || (e3 && w3 == 0)){ return false; }

        return true;
    }
    private float TriangleArea(int x, int y, Vector3 v0,Vector3 v1){
        return (x - v0.x()) * (v1.y() - v0.y()) - (y - v0.y()) * (v1.x() - v0.x());
    }
    private float TriangleArea(VertexData p, VertexData v0,VertexData v1){
        return (p.screen.x() - v0.screen.x()) * (v1.screen.y() - v0.screen.y()) - (p.screen.y() - v0.screen.y()) * (v1.screen.x() - v0.screen.x());
    }
    private void OrderTriangle(VertexData[] vertices){
        Float[] ClockVals = CalculateAtan2 (vertices);
        if (ClockVals [0] < ClockVals [1]) {
            Utilities.<VertexData>Swamp(vertices,0,1);
            Utilities.<Float>Swamp(ClockVals,0,1);
        }
        if (ClockVals [0] < ClockVals [2]) {
            Utilities.<VertexData>Swamp(vertices,0,2);
            Utilities.<Float>Swamp(ClockVals,0,2);
        }
        if (ClockVals [1] < ClockVals [2]) {
            Utilities.<VertexData>Swamp(vertices,1,2);
            Utilities.<Float>Swamp(ClockVals,1,2);
        }
    }
    private Float[] CalculateAtan2(VertexData[] vertices){
        Vector2 center = CalculateCenter (vertices);
        Float[] result = new Float[vertices.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (float)Math.atan2(vertices[i].screen.y() - center.y(),vertices[i].screen.x() - center.x());
        }
        return result;
    }
    private Vector2 CalculateCenter(VertexData[] vertices){
        if (vertices.length > 0) {
            Vector2 vector = new Vector2(0,0);
            for (int i = 0; i < vertices.length; i++) {
                vector.axis[0] += vertices[i].screen.axis[0]; // add
                vector.axis[1] += vertices[i].screen.axis[1]; // add
            }
            return vector.Multiply(1f / vertices.length);
        }
        return new Vector2 (0,0);
    }
    private Vector3 CalculateTriangleProjectionNormal(VertexData v0,VertexData v1,VertexData v2){
        Vector3 U = new Vector3(v1.projection.Substract(v0.projection));
        Vector3 V = new Vector3(v2.projection.Substract(v0.projection));
        return  U.Cross(V);
    }
}
