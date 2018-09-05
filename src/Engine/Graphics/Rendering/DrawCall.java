package Engine.Graphics.Rendering;

import Engine.Algebra.Matrices.Matrix;
import Engine.Algebra.Matrices.MatrixUtilities;
import Engine.Algebra.Vectors.Vector2;
import Engine.Algebra.Vectors.Vector3;
import Engine.Algebra.Vectors.Vector4;
import Engine.Core.Objects.Components.AnimatedMeshRenderer;
import Engine.Core.Objects.Components.Camera;
import Engine.Core.Objects.Components.MeshRenderer;
import Engine.Graphics.Graphics3D.Mesh;
import sun.security.provider.certpath.Vertex;

import java.awt.*;
import java.nio.file.Paths;

public class DrawCall {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public boolean culled;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private MeshRenderer _renderer;
    private Mesh _mesh;
    private Shader _shader;
    private VertexData[] _vertexBuffer;
    private Matrix _localToWorld;
    private Matrix _worldToLocal;
    private Matrix _worldToCamera;
    private Matrix _normalToWorld;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public DrawCall(MeshRenderer renderer,Matrix WorldToCamera){
        this._renderer = renderer;
        this._mesh = renderer.Mesh();
        this._shader = renderer.shader;
        this._localToWorld = renderer.GameObject().Transform().CartesianMatrix();
        this._worldToLocal = renderer.GameObject().Transform().CartesianMatrixInverse();
        this._worldToCamera = WorldToCamera;
        this._normalToWorld = _worldToLocal.Transpose();
        InitializeVertexBuffer(this._mesh);
        this.culled = !renderer.IsVisible();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void ActivateVertexShader(){
        Shader().InitializeDrawCall(this); // set shader settings
        for(int i = 0; i < _vertexBuffer.length; i++){
            _vertexBuffer[i] = _shader.VertexShader(_vertexBuffer[i]);
        }
    }
    public Color ShadeFragment(FragmentData fragment){
        return _shader.FragmentShader(fragment);
    }
    public void ModelToRasterSpace(Camera Cam){
        for(int i = 0; i < _vertexBuffer.length; i++){
            // Normal
            _vertexBuffer[i].normal = MatrixUtilities.Multiply(_normalToWorld,_vertexBuffer[i].normal); // transform normal
            _vertexBuffer[i].normal.Normalize(); // normalize normal again

            // Vertex
            _vertexBuffer[i].vertex = MatrixUtilities.Multiply(_localToWorld,_vertexBuffer[i].vertex); // to world space
            _vertexBuffer[i].vertex = MatrixUtilities.Multiply(_worldToCamera,_vertexBuffer[i].vertex); // to camera space
            _vertexBuffer[i].SetProjection(Cam.ProjectCoordinate(_vertexBuffer[i].vertex)); // project on image plane
            _vertexBuffer[i].screen = Cam.TransformToRasterSpace(_vertexBuffer[i].projection); // to raster space
        }
    }
    public void SetCulled(boolean culled){
        if(this.culled != culled){
            this.culled = culled;

            if(!this.culled){_renderer.OnBecameVisible();
            } else {_renderer.OnBecameInvisible();}
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void InitializeVertexBuffer(Mesh mesh){
        _vertexBuffer = new VertexData[mesh.vertices.length];
        for(int i = 0; i < mesh.triangles.length; i++){
            int vertIndex = mesh.triangles[i].vertIndex;
            if(_vertexBuffer[vertIndex] == null) {
                int normalIndex = mesh.triangles[i].normalIndex;
                int uvIndex = mesh.triangles[i].uvIndex;

                _vertexBuffer[vertIndex] = new VertexData(
                        new Vector4(
                                mesh.vertices[vertIndex].x(),
                                mesh.vertices[vertIndex].y(),
                                mesh.vertices[vertIndex].z(),
                                1
                        ),
                        new Vector3(
                                mesh.normals[normalIndex].x(),
                                mesh.normals[normalIndex].y(),
                                mesh.normals[normalIndex].z()
                        ),
                        mesh.uvs[uvIndex]
                );
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Mesh Mesh(){
        return this._mesh;
    }
    public Shader Shader(){
        return this._shader;
    }
    public VertexData[] VertexBuffer(){
        return this._vertexBuffer;
    }
    public Matrix LocalToWorld(){
        return this._localToWorld;
    }
    public Matrix WorldToCamera(){
        return this._worldToCamera;
    }
}
