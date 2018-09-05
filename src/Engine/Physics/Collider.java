package Engine.Physics;

import Engine.Algebra.Matrices.MatrixUtilities;
import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.Component;
import Engine.Events.ITriggerEvents;

public abstract class Collider extends Component implements ITriggerEvents {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Base
    // ------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void Awake() {
        Scene().RegisterPhysicsObject(this); // register self
    }
    @Override
    public void Start() {

    }
    @Override
    public void Update() {

    }
    public void Destroy() {
        Scene().RemovePhysicsObject(this); // remove physics object
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Abstract voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    protected abstract Vector3 Center();
    public abstract float MaxRadius();
    public abstract boolean Intersect(Collider col);
    public abstract Vector3[] Points();
    public abstract Vector3[] SurfaceNormals();

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void OnTriggerEnter(Collider col){
        ITriggerEvents e = GameobjectTriggers();
        if(e != null){e.OnTriggerEnter(col);}
    }
    public void OnTriggerExit(Collider col){
        ITriggerEvents e = GameobjectTriggers();
        if(e != null){e.OnTriggerExit(col);}
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public Vector3 WorldPosition(){
        return (MatrixUtilities.Multiply(GameObject().Transform().CartesianMatrix(),Center(),0f)).Add(GameObject().Transform().Position());
    }
    private ITriggerEvents GameobjectTriggers(){
        if(GameObject() instanceof  ITriggerEvents){
            return (ITriggerEvents)GameObject();
        }
        return null;
    }
}
