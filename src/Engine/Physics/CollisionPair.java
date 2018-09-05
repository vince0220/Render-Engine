package Engine.Physics;

import Engine.Algebra.Vectors.Vector3;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.awt.*;

public class CollisionPair {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Collider a;
    private Collider b;
    private boolean collision;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public CollisionPair(Collider a, Collider b){
        this.a = a;
        this.b = b;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public boolean Contains(Collider col){
        return a == col || b == col;
    }
    public void Simulate(){
        if(BroadCollision()){
            if(NarrowCollision()){
                SetCollision(true);
                return; // stop simulation
            }
        }

        SetCollision(false); // no collision
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void SetCollision(boolean col){
        if(collision != col){ // has changed
            if(collision){OnCollisionExit();} // there was collision but not anymore
            if(!collision){OnCollisionEnter();} // there was no collision but now there is
            collision = col; // change collision
        }
    }
    private boolean BroadCollision(){
        return a.WorldPosition().Distance(b.WorldPosition()) < (a.MaxRadius() + b.MaxRadius()); // if is lower then one of two there is collision
    }
    private boolean NarrowCollision(){
        return a.Intersect(b);
    }

    // events
    private void OnCollisionExit(){
        a.OnTriggerExit(b); // on trigger exit a
        b.OnTriggerExit(a); // on trigger exit b
    }
    private void OnCollisionEnter(){
        a.OnTriggerEnter(b); // on trigger enter a
        b.OnTriggerEnter(a); // on trigger enter b
    }
}
