package Game.Objects;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.Components.BoxCollider;
import Engine.Core.Objects.Components.MeshRenderer;
import Engine.Core.Objects.GameObject;
import Game.Scenes.GameScene;

public class DoublePipe extends GameObject {

    /**
     * Object tag
     */
    public static String TAG = "DOUBLE_PIPE";

    /**
     * Ideal bird target positions
     */
    public float targetX, targetY = 0;

    private Pipe topPipe,bottomPipe;
    private float gapSize = 2f;

    /**
     * Empty constructor of DoublePipe
     */
    public DoublePipe(){
        tag = TAG;
    }

    /**
     * Default start methode of GameObject
     */
    @Override
    public void Start(){
        ((GameScene)Scene()).RegisterPipe(this); // register self
        bottomPipe = InitPipe(0,Vector3.Down().Multiply(gapSize * 0.5f)); // init bottom pipe
        topPipe = InitPipe(180,Vector3.Up().Multiply(gapSize * 0.5f)); // init top pipe

        final Vector3 pipeSize = bottomPipe.GetComponent(MeshRenderer.class).Mesh().bounds.size;
        AddComponent(new BoxCollider(new Vector3(0,0,0),new Vector3(pipeSize.x() * 0.5f,gapSize * 0.5f,pipeSize.z() * 0.5f)));

        CalculateIdealGoTrough();
    }

    /**
     * Calculates the ideal position for the bird(s) to fly trough
     */
    private void CalculateIdealGoTrough(){
        final Vector3 curPos = Transform().Position();
        final Vector3 pipeSize = bottomPipe.GetComponent(MeshRenderer.class).Mesh().bounds.size;
        targetY = curPos.y();
        targetX = curPos.x()+pipeSize.x();
    }

    /**
     * Inits pipe
     *
     * Constructs a new pipe object
     * and places the pipe in the scene
     *
     * @param rotation
     * @param offset
     * @return initialized pipe
     */
    private Pipe InitPipe(final float rotation, final Vector3 offset){
        final Pipe pipe = (Pipe)Scene().Instantiate(new Pipe());
        pipe.InitializeMeshRenderer();
        pipe.Transform().SetTransform(Transform().Position().Add(offset),new Vector3(rotation,0,0),Vector3.One());
        return pipe;
    }

}
