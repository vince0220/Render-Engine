package Game.Interfaces;

import Engine.Core.Objects.Components.Camera;
import Engine.Core.Objects.GameObject;

public interface ICameraHolder {
    /**
     * Returns the camera the object is holding
     * @return
     */
    GameObject Camera();
}
