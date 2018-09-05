package Engine.Resources.Assets.Mesh.Parsers;

import Engine.Algebra.Vectors.*;
import Engine.Graphics.Graphics3D.Mesh;
import Engine.Graphics.Graphics3D.TriangleVertex;
import Engine.Resources.IO.Parsers.ITypeParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjParser implements ITypeParser<Mesh,BufferedReader> {

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private List<Vector3> vertices = new ArrayList<Vector3>();
    private List<Vector3> normals = new ArrayList<Vector3>();
    private List<Vector2> uv = new ArrayList<Vector2>();
    private List<TriangleVertex> triangles = new ArrayList<TriangleVertex>();
    private Map<Integer,String> vertexTracker = new HashMap<Integer, String>();

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Parser implement
    // ------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public boolean CanParse(BufferedReader reader) {
        return reader != null;
    }

    @Override
    public Mesh Parse(BufferedReader Data) {
        try {
            for(String ln = Data.readLine(); ln != null; ln = Data.readLine()){ // loop through lines
                ParseLine(ln); // parse all lines
            }
            return new Mesh(
                    vertices.toArray(new Vector3[vertices.size()]),
                    normals.toArray(new Vector3[normals.size()]),
                    uv.toArray(new Vector2[uv.size()]),
                    triangles.toArray(new TriangleVertex[triangles.size()])
            );
        } catch (IOException e) {e.printStackTrace(); }
        return null; // could not parse
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private functions
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void ParseLine(String ln){
        if(ln.startsWith("v ")){ // check if is vertex
            ParseVertex(ln);
        } else if(ln.startsWith("vn ")){ // check if is vertex normal
            ParseNormal(ln);
        } else if(ln.startsWith("vt ")){ // check if is vertex uv
            ParseUv(ln);
        } else if(ln.startsWith("f ")){ // check if is face
            ParseFace(ln);
        }
    }
    private void ParseVertex(String ln){
        String[] split = ln.split("\\s+");
        AddVertex(
                Float.valueOf(split[1]),
                Float.valueOf(split[2]),
                Float.valueOf(split[3])
        );
    }
    private void ParseNormal(String ln){
        String[] split = ln.split("\\s+");
        AddNormal(
                Float.valueOf(split[1]),
                Float.valueOf(split[2]),
                Float.valueOf(split[3])
        );
    }
    private void ParseUv(String ln){
        String[] split = ln.split("\\s+");
        AddUV(
                Float.valueOf(split[1]),
                Float.valueOf(split[2])
        );
    }
    private void ParseFace(String ln){
        String[] split = ln.split("\\s+");
        ParseTriangle(split[1]);
        ParseTriangle(split[2]);
        ParseTriangle(split[3]);
    }

    private void ParseTriangle(String ln){
        String[] split = ln.split("/");

        int vertex = Integer.valueOf(split[0]) - 1;
        vertex = CheckForDuplicates(vertex,ln);

        triangles.add(new TriangleVertex(
                vertex,
                Integer.valueOf(split[1]) - 1,
                Integer.valueOf(split[2])- 1
        ));
    }

    // Add voids
    private int AddVertex(float x, float y, float z){
        vertices.add(new Vector3( // add new vertex
                x,
                y,
                z
        ));
        return vertices.size() - 1; // return added index
    }
    private int AddVertex(Vector3 vector){
        return AddVertex(vector.x(),vector.y(),vector.z());
    }
    private int AddNormal(float x,float y,float z){
        normals.add(new Vector3( // add new vertex
                x,
                y,
                z
        ));
        return normals.size() - 1; // return added index
    }
    private int AddUV(float x,float y){
        uv.add(new Vector2( // add new uv coord
                x,
               y
        ));
        return uv.size() - 1;
    }
    private int CheckForDuplicates(int index, String ln){
        if(vertexTracker.containsKey(index)){
            if(!vertexTracker.get(index).equals(ln)) {
                index = AddVertex(vertices.get(index)); // add new vert at same location and set to be new vert
                vertexTracker.put(index,ln);
            }
        } else {
            vertexTracker.put(index,ln);
        }
        return  index;
    }
}
