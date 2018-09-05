package Game.Objects;

import Engine.Core.Input.InputManager;
import Engine.Physics.Collider;
import Game.UI.InterfaceController;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.io.File;

public class PlayerBird extends Bird {

    /**
     * Path to sound file used for the flapping action
     */
    final String FLAP_SOUND = "res/Audio/flap.wav";
    /**
     * Path to sound file that is used when the player score is incremented
     */
    final String SCORE_SOUND = "res/Audio/point.wav";

    private File flapSound,scoreSound;

    /**
     * Empty constructor of PlayerBird
     */
    public PlayerBird() {
        InterfaceController.GetInstance().SetScoreCache(score);
    }

    /**
     * Default update method
     */
    @Override
    public void Update(){
        super.Update();
        if(InputManager.I().GetKeyDown(KeyCode.SPACE)&&alive){
            Flap();
            MakeSound(flapSound);
        }
    }

    /**
     * Inits sounds from resources
     */
    @Override
    protected void InitSounds(){
        super.InitSounds();
        try{
            flapSound = new File(FLAP_SOUND);
            scoreSound = new File(SCORE_SOUND);
        } catch(final Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Gets called when the bird enters a trigger
     * @param col
     */
    @Override
    public void OnTriggerEnter(final Collider col){
        super.OnTriggerEnter(col);
        if(col.GameObject().tag == DoublePipe.TAG){
            InterfaceController.GetInstance().SetScoreCache(score);
        }
    }
}
