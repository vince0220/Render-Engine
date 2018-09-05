package Engine.Debug;

import Engine.Algebra.Vectors.Vector3;
import Engine.Algebra.Vectors.Vector4;
import Engine.Core.Objects.Components.Camera;
import Engine.Core.Objects.Components.Transform;
import Engine.Graphics.Graphics2D.Canvas2D;
import Engine.Graphics.Graphics3D.Bounds;
import jdk.nashorn.internal.runtime.Debug;

import java.awt.*;
import java.util.ArrayList;

public class DebugEngine {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private ArrayList<DebugLineCall> lineCalls = new ArrayList<DebugLineCall>();

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void DrawLine(Vector3 a, Vector3 b,Color col,float duration){
        lineCalls.add(new DebugLineCall(
                duration,
                a,
                b,
                col
        ));
    }
    public void DrawRay(Vector3 pos, Vector3 dir,Color col,float duration){
        DrawLine(pos,pos.Add(dir),col,duration);
    }
    public void Clear(){
        lineCalls.clear();
    }

    public void DrawBound(Bounds bound,Color col,float duration){
        DrawBound(bound,null,col,duration);
    }
    public void DrawBound(Bounds bound, Transform local,Color col,float duration){
        Vector3[] corners = new Vector3[0]; // init corners array

        // calculate corners
        if(local != null){ corners = bound.WorldCorners(local.CartesianMatrix());
        } else { corners = bound.corners; }

        // draw bottom
        DrawLine(corners[0],corners[1],col,duration);
        DrawLine(corners[0],corners[7],col,duration);
        DrawLine(corners[7],corners[6],col,duration);
        DrawLine(corners[6],corners[1],col,duration);

        // draw top
        DrawLine(corners[2],corners[3],col,duration);
        DrawLine(corners[3],corners[4],col,duration);
        DrawLine(corners[4],corners[5],col,duration);
        DrawLine(corners[2],corners[5],col,duration);

        // draw middle
        DrawLine(corners[0],corners[2],col,duration);
        DrawLine(corners[1],corners[3],col,duration);
        DrawLine(corners[5],corners[7],col,duration);
        DrawLine(corners[6],corners[4],col,duration);
    }

    // Rendering
    public void OverlayDebug(Canvas2D canvas, Camera cam){
        for(int i = 0; i < lineCalls.size(); i++){
            DebugLineCall call = lineCalls.get(i);
            RenderLine(call,canvas,cam); // render call
            if(!call.Active()){
                lineCalls.remove(i);
                i--;} // step back
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void RenderLine(DebugLineCall line, Canvas2D canvas, Camera cam){
        // Transform to normalized device space
        Vector4 fromNDC = cam.TransformWorldToNDC(line.from);
        Vector4 toNDC = cam.TransformWorldToNDC(line.to);

       if(cam.PointInClip(toNDC) || cam.PointInClip(fromNDC)) { // check if is in canvas
           // Transform to raster space
           Vector3 fromRaster = cam.TransformToRasterSpace(fromNDC);
           Vector3 toRaster = cam.TransformToRasterSpace(toNDC);

           // Draw line
           canvas.DrawLine((int) fromRaster.x(), (int) fromRaster.y(), (int) toRaster.x(), (int) toRaster.y(), line.col);
       }
    }
}
