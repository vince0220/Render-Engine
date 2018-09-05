package Engine.Physics;

import Engine.Core.Objects.Component;
import Engine.Events.IUpdateEvent;

import java.util.ArrayList;

public class PhysicsEngine implements IUpdateEvent {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private ArrayList<Collider> _colliders = new ArrayList<Collider>();
    private ArrayList<CollisionPair> _pairs = new ArrayList<CollisionPair>();

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Events
    // ------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public void Update() {
        for(int i = 0; i < _pairs.size(); i++){
            _pairs.get(i).Simulate(); // simulate pairs
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void RegisterCollider(Collider col){
        if(!_colliders.contains(col)){
            GeneratePairsFor(col);
            _colliders.add(col);
        }
    }
    public void RemoveCollider(Collider col){
        _colliders.remove(col);
        DegeneratePairsFor(col);
    }
    public void Clear(){
        _colliders.clear();
        _pairs.clear();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void GeneratePairsFor(Collider col){
        for(int i = 0; i < _colliders.size(); i++){
            if(_colliders.get(i) != col){
                _pairs.add(new CollisionPair(col, _colliders.get(i)));
            }
        }
    }
    private void DegeneratePairsFor(Collider col){
        for(int i = 0; i < _pairs.size(); i++){
            if(_pairs.get(i).Contains(col)){
                _pairs.remove(i);
                i--; // step i back
            }
        }
    }
}
