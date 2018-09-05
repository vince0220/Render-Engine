package Engine.Graphics.Graphics2D;

import Engine.Algebra.Vectors.Vector;
import Engine.Algebra.Vectors.Vector2;
import Engine.Graphics.Graphics2D.Utils.Graphics2DUtils;
import Engine.Utils.Utilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class Canvas2D {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private BufferedImage imageBuffer;

    public Canvas2D(Vector2 size){
        this((int)size.x(),(int)size.y());
    }
    public Canvas2D(int width, int height){
        imageBuffer = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public functions - Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void SetPixel(int x, int y, Color col){
        SetPixel(x,y,col.getRGB());
    }
    public void SetPixel(int x, int y, int r, int g, int b){
        SetPixel(x,y,Graphics2DUtils.GetIntFromColor(r,g,b));
    }
    public void SetPixel(int x, int y, int col){
        if(x >= 0 && x < imageBuffer.getWidth() && y >= 0 && y < imageBuffer.getHeight()) {
            imageBuffer.setRGB(x, y, col);
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public functions - Get
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public BufferedImage Frame(){
        return imageBuffer;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public draw functions
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void DrawTriangle(Vector2 a, Vector2 b, Vector2 c,Color col){
        DrawTriangle(new Vector2[]{a,b,c},col);
    }
    public void DrawTriangle(Vector2[] vertices,Color col){
        Vector2[] minmax = CalculateMinMax(vertices); // calculate bounds of triangle
        OrderTriangle(vertices); // order triangle clock wise
        float area = TriangleArea(vertices[0],vertices[1],vertices[2]); // calculate triangle area
        Vector2[] edges = CalculateTriangleEdges(vertices); // calculate edges of triangle

        for (int y = (int)minmax[0].y(); y <= (int)minmax[1].y(); ++y) { // loop through y pixels
            for (int x = (int)minmax[0].x(); x <= (int)minmax[1].x(); ++x) { // loop through x pixels
                Vector2 p = new Vector2(x,y); // point

                float[] weights = new float[]{ // calculate barycentric coordinate weights
                        TriangleArea (p,vertices[1],vertices[2]) / area, // weight 0
                        TriangleArea (p,vertices[2],vertices[0]) / area, // weight 1
                        TriangleArea (p,vertices[0],vertices[1]) / area // weight 2
                };

                if(OverlapsTriangle(edges,weights)) { // triangle overlap test / Top left test
                    imageBuffer.setRGB(x, y, col.getRGB()); // set rgb color
                }
            }
        }
    }

    public void DrawLine(Vector2 a, Vector2 b,Color col){
        DrawLine(
                (int)a.x(),
                (int)a.y(),
                (int)b.x(),
                (int)b.y(),
                col
        );
    }
    public void DrawLine(int x, int y, int x2, int y2, Color col){
        int w = x2 - x ; // gradient of w
        int h = y2 - y ; // gradient of h

        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ; // delta variabales
        if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ; // check if line is faced left or right
        if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ; // check if line y2 is above y1 or inversed
        if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ; // check if line is faced left or right

        int longest = Math.abs(w); // initialize absolute of w as longest axis
        int shortest = Math.abs(h); // initialize absolute of h as shortest axis
        if (!(longest>shortest)) { // check if longest is really longer then shortest
            longest = shortest; // inverse longest
            shortest = Math.abs(w) ; // inverse shortest
            if (h<0) dy2 = -1; else if (h>0) dy2 = 1;
            dx2 = 0;
        }
        int numerator = longest >> 1 ;
        for (int i=0;i<=longest;i++) {
            SetPixel(x,y,col);
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }
    }
    public void Clear(Color background){
        Graphics2D bg = imageBuffer.createGraphics();
        bg.setBackground(background);
        bg.clearRect(0,0,imageBuffer.getWidth(),imageBuffer.getHeight());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // private Calculation functions
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Vector2[] CalculateTriangleEdges(Vector2[] vertices){
        return new Vector2[]{
                vertices [2].Substract(vertices [1]),
                vertices [0].Substract(vertices [2]),
                vertices [1].Substract(vertices [0])
        };
    }
    private Vector2[] CalculateMinMax(Vector2[] vertices){
        Vector2[] minmax = new Vector2[]{
                new Vector2(Float.MAX_VALUE,Float.MAX_VALUE),
                new Vector2(-Float.MAX_VALUE,-Float.MAX_VALUE)
        };

        // Calculate min max
        for (int i = 0; i < 3; ++i) {
            if (vertices[i].x() < minmax[0].x()) minmax[0].axis[0] = vertices[i].x();
            if (vertices[i].y() < minmax[0].y()) minmax[0].axis[1] = vertices[i].y();
            if (vertices[i].x() > minmax[1].x()) minmax[1].axis[0] = vertices[i].x();
            if (vertices[i].y() > minmax[1].y()) minmax[1].axis[1] = vertices[i].y();
        }

        // Clamp in canvas
        for(int i = 0; i < minmax.length; i++){
            minmax[i].axis[0] = Math.max(0,Math.min(imageBuffer.getWidth() - 1,(int)minmax[i].x()));  // clamp X
            minmax[i].axis[1] = Math.max(0,Math.min(imageBuffer.getHeight() - 1,(int)minmax[i].y()));  // clamp Y
        }

        return minmax;
    }
    private boolean OverlapsTriangle(Vector2[] edges,float[] weights){
        for (int i = 0; i < edges.length; i++) {
            if (!(weights [i] == 0 ? ((edges[i].y() == 0 && edges[i].x() > 0) || edges[i].y() > 0) : (weights [i] > 0))) {
                return false;
            }
        }
        return true;
    }
    private float TriangleArea(Vector2 p, Vector2 v0,Vector2 v1){
        return (p.x() - v0.x()) * (v1.y() - v0.y()) - (p.y() - v0.y()) * (v1.x() - v0.x());
    }
    private void OrderTriangle(Vector2[] vectors){
        Float[] ClockVals = CalculateAtan2 (vectors);
        if (ClockVals [0] < ClockVals [1]) {
            Utilities.<Vector2>Swamp(vectors,0,1);
            Utilities.<Float>Swamp(ClockVals,0,1);
        }
        if (ClockVals [0] < ClockVals [2]) {
            Utilities.<Vector2>Swamp(vectors,0,2);
            Utilities.<Float>Swamp(ClockVals,0,2);
        }
        if (ClockVals [1] < ClockVals [2]) {
            Utilities.<Vector2>Swamp(vectors,1,2);
            Utilities.<Float>Swamp(ClockVals,1,2);
        }
    }
    private Float[] CalculateAtan2(Vector2[] vectors){
        Vector2 center = CalculateCenter (vectors);
        Float[] result = new Float[vectors.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (float)Math.atan2(vectors[i].y() - center.y(),vectors[i].x() - center.x());
        }
        return result;
    }
    private Vector2 CalculateCenter(Vector2[] points){
        if (points.length > 0) {
            Vector2 vector = new Vector2 (0,0);
            for (int i = 0; i < points.length; i++) {
                vector.axis[0] += points[i].axis[0]; // add
                vector.axis[1] += points[i].axis[1]; // add
            }
            return vector.Multiply(1f / points.length);
        }
        return new Vector2 (0,0);
    }
}
