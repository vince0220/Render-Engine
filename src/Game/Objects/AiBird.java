package Game.Objects;

import Engine.Algebra.Vectors.Vector3;
import Game.AI.Brain;
import Game.Scenes.GameScene;
import ML.NeuralNetwork.Neuron;

import java.awt.*;

public class AiBird extends Bird {

    /**
     * Empty constructor of AiBird
     */
    public AiBird() {
        super();
    }

    /**
     * Default update method
     */
    @Override
    public void Update(){
        super.Update();
        if(!alive)return;
        final GameScene gs = ((GameScene)Scene());
        final Vector3 pos = Transform().Position();
        if (score > (gs.PipeCount() - 1)) {
            return;
        }
        final DoublePipe pipe = gs.GetPipe(score);
        if(pipe!=null){
            float y = pos.y() - pipe.targetY;
            final Neuron neuron = Brain.GetInstance().network.run(new float[]{y}).get(0);
            if(neuron.value>0.55f){
                Flap();
            }
        }
    }
}
