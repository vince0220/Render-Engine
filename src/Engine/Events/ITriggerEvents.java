package Engine.Events;

import Engine.Physics.Collider;

public interface ITriggerEvents {
    void OnTriggerEnter(Collider col);
    void OnTriggerExit(Collider col);
}
