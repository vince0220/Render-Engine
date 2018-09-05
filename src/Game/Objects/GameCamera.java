package Game.Objects;
import Engine.Algebra.Vectors.Vector2;
import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.Components.Camera;
import Engine.Core.Objects.GameObject;
import Engine.Utils.MathUtillities;
import com.sun.istack.internal.NotNull;

import java.awt.*;

public class GameCamera extends GameObject {
    private Vector3 targetOffset = new Vector3(-3f,3.4f,7f);
    private GameObject followTarget;
    private Vector2 lerpExtends = new Vector2(2f,4f);

    /**
     * Empty constructor of GameCamera
     */
    public GameCamera(){ }

    /**
     * Default awake method
     */
    @Override
    public void Awake(){
        AddComponent(new Camera());
        Transform().SetRotation(new Vector3(-20,-15,0));
        GetComponent(Camera.class).background = new Color(98, 182,255);
    }

    /**
     * Default update method
     */
    @Override
    public void Update(){
        if(followTarget!=null){
            final Vector3 pos = followTarget.Transform().Position();
            Transform().SetPosition(new Vector3(pos.x()+targetOffset.x(),InterpolatedHeight(pos.y()),targetOffset.z()));
        }
    }

    /**
     * Makes the camera follow a gameobject
     * @param gameObject
     */
    public void Follow(@NotNull final GameObject gameObject){
        followTarget = gameObject;
    }

    /**
     *  Interpolates the height of the camera to follow the followTarget. The height interpolation is kept between the lerpExtends range.
     * @param targetY target y position
     * @return interpolated height
     */
    private float InterpolatedHeight(float targetY){
        float difference = MathUtillities.Clamp((targetOffset.y() - targetY) / lerpExtends.y(),0f,1f);
        float target = Math.abs(targetOffset.y() + ((difference > 0)?-lerpExtends.x():lerpExtends.y()));
        return MathUtillities.Lerp(targetOffset.y(),target,difference);
    }
}
