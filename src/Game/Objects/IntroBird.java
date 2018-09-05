package Game.Objects;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Scenes.Time;
import ML.MLUtils;

public class IntroBird extends Bird {

    private float speed = 2;

    /**
     * Default start method
     */
    @Override
    public void Start(){
        super.Start();
        speed = MLUtils.Random(1,2);
        Transform().SetRotation(new Vector3(0,20,0));
    }
    /**
     * Default update method
     */
    @Override
    public void Update(){
        final Vector3 curPos = Transform().Position();
        final Vector3 curRot = Transform().Rotation();
        float vel = speed * Time.DeltaTime();
        float rotVel = vel*8;
        Transform().SetPosition(
                new Vector3(curPos.x()+vel,curPos.y()+vel,curPos.z()+vel)
        );
        Transform().SetRotation(
                new Vector3(curRot.x()-rotVel,curRot.y()+rotVel,curRot.z()+rotVel)
        );
    }
}
