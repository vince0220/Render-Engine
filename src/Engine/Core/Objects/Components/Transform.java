package Engine.Core.Objects.Components;

import Engine.Algebra.Matrices.Matrix;
import Engine.Algebra.Matrices.MatrixUtilities;
import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.Component;
import javafx.geometry.Pos;

import java.awt.image.BufferedImage;

public class Transform extends Component {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Vector3 _position = new Vector3(0,0,0);
    private Vector3 _scale = new Vector3(1,1,1);
    private Vector3 _rotation = new Vector3(0,0,0);
    private Matrix _cartesianMatrix;
    private Matrix _cartesianMatrixInverse;

    // Cache variables
    private Vector3 _front;
    private Vector3 _right;
    private Vector3 _up;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Transform(){
        super(); // call parent constructor
        UpdateCartesian(); // initialize cartesian
        UpdateLocalDirections();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Base voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void Awake() { }
    public void Start() { }
    public void Update() { }
    public void PostRender() { }
    public BufferedImage PreRender(BufferedImage frame) {
        return frame;
    }
    public void Destroy() { }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector3 Position(){
        return new Vector3(_position.axis);
    }
    public void SetPosition(Vector3 Position){
        this._position = Position;
        UpdateCartesian();
    }
    public Vector3 Rotation(){
        return new Vector3(_rotation.axis);
    }
    public void SetRotation(Vector3 Rotation){
        this._rotation = Rotation;
        UpdateCartesian();
    }
    public Vector3 Scale(){
        return new Vector3(_scale.axis);
    }
    public void SetScale(Vector3 Scale){
        this._scale = Scale;
        UpdateCartesian();
    }
    public void SetTransform(Vector3 position,Vector3 rotation,Vector3 scale){
        this._position = position;
        this._rotation = rotation;
        this._scale = scale;
        UpdateCartesian();
    }
    public Matrix CartesianMatrix(){
        return _cartesianMatrix;
    }
    public Matrix CartesianMatrixInverse(){
        return _cartesianMatrixInverse;
    }

    // Sides
    public Vector3 Front(){
        return _front;
    }
    public Vector3 Back(){
        return _front.Multiply(-1);
    }
    public Vector3 Right(){
        return _right;
    }
    public Vector3 Left(){
        return _right.Multiply(-1);
    }
    public Vector3 Up(){
        return _up;
    }
    public Vector3 Down(){
        return _up.Multiply(-1);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void UpdateCartesian(){
        _cartesianMatrix = MatrixUtilities.TransformMatrix(_position, _rotation, _scale); // calculate transformation matrix
        _cartesianMatrixInverse = _cartesianMatrix.Inverse(); // calculate inverse
        UpdateLocalDirections(); // update local directions
    }
    private void UpdateLocalDirections(){
        _front = MatrixUtilities.Multiply(CartesianMatrix(),new Vector3(0,0,1));
        _right = MatrixUtilities.Multiply(CartesianMatrix(),new Vector3(1,0,0));
        _up = MatrixUtilities.Multiply(CartesianMatrix(),new Vector3(0,1,0));
    }
}
