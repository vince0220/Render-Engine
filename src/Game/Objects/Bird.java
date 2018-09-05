package Game.Objects;

import Engine.Algebra.Vectors.Vector3;
import Engine.Core.Objects.Components.AnimatedMeshRenderer;
import Engine.Core.Objects.Components.BoxCollider;
import Engine.Core.Objects.GameObject;
import Engine.Core.Scenes.Time;
import Engine.Events.ITriggerEvents;
import Engine.Graphics.Graphics3D.Mesh;
import Engine.Physics.Collider;
import Engine.Resources.Assets.AssetManager;
import Game.AI.Activity;
import Game.Scenes.GameScene;
import Game.Shading.StandardShader;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Bird extends GameObject implements ITriggerEvents {

    /**
     * Indicator if bird is alive
     */
    public boolean alive = true;

    /**
     * The path to the audio file that should be played when the bird dies
     */
    final String DIE_SOUND = "res/Audio/die.wav";

    /**
     * The current score of the bird. Every time the bird flies through a pipe the score is incremented
     */
    protected int score = 0;

    // privates
    private float up = 2.5f;
    private float right = 2.5f;
    private float gravity = 6f;
    private float yVelocity = 0f;
    private Activity activity;
    private File dieSound;

    /**
     * Empty constructor of Bird
     */
    public Bird(){
        InitSounds();
    }

    /**
     * Default awake method
     */
    @Override
    public void Awake(){
        Mesh[] frames = new Mesh[]{
                AssetManager.I().<Mesh>FindAsset("BirdFrame1",Mesh.class),
                AssetManager.I().<Mesh>FindAsset("BirdFrame2",Mesh.class),
                AssetManager.I().<Mesh>FindAsset("BirdFrame3",Mesh.class),
                AssetManager.I().<Mesh>FindAsset("BirdFrame4",Mesh.class),
                AssetManager.I().<Mesh>FindAsset("BirdFrame5",Mesh.class),
                AssetManager.I().<Mesh>FindAsset("BirdFrame6",Mesh.class),
                AssetManager.I().<Mesh>FindAsset("BirdFrame7",Mesh.class),
                AssetManager.I().<Mesh>FindAsset("BirdFrame8",Mesh.class),
                AssetManager.I().<Mesh>FindAsset("BirdFrame9",Mesh.class),
        };

        BufferedImage albedo = AssetManager.I().FindAsset(RandomTexture(),BufferedImage.class);
        this.AddComponent(
                new AnimatedMeshRenderer(frames,4,new StandardShader(albedo))
        );
        final Vector3 bounds = frames[4].bounds.size;
        final float scale = 0.7f;
        AddComponent(new BoxCollider(Vector3.Zero(),new Vector3(bounds.x()*scale,bounds.y()*scale,bounds.z()*scale)));
    }

    /**
     * Default update method
     */
    @Override
    public void Update(){
        yVelocity -= gravity * Time.DeltaTime();
        final Vector3 curPos = Transform().Position();
        if(curPos.y()>5||curPos.y()<-2)Die();
        Transform().SetTransform(
                new Vector3(curPos.x()+ (right * Time.DeltaTime()),curPos.y()+(yVelocity*Time.DeltaTime()),curPos.z()),
                new Vector3(-yVelocity * 10f,90,0),
                Transform().Scale()
        );
    }

    /**
     * Inits sound files
     */
    protected void InitSounds(){
        dieSound = new File(DIE_SOUND);
    }

    /**
     * Plays a sound based on the given file
     * @param file the sound file you want to play
     */
    protected void MakeSound(final File file){
        try {
            final AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            final AudioFormat format = inputStream.getFormat();
            final DataLine.Info info = new DataLine.Info(Clip.class, format);
            final Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }

    /**
     * Performs a flap action, which makes the bird go up
     */
    protected void Flap(){
        final Vector3 curPos = Transform().Position();
        activity = new Activity(curPos.x(), curPos.y(), true);
        yVelocity = up;
    }

    /**
     * Lets the bird die
     */
    protected void Die(){
        if(!alive)return;
        right = 0;
        alive = false;
        MakeSound(dieSound);
        ((GameScene)Scene()).checkIfGameIsOver();
    }

    /**
     * Normalizes and adds activity to controller
     * @param success
     */
    protected void normalizeAndAddActivity(final boolean success)
    {
        final GameScene gs = (GameScene)Scene();
        if (score>gs.PipeCount()-1) { return; }
        final DoublePipe pipe = gs.GetPipe(score);
        if (pipe != null && activity != null)
        {
            activity.x = activity.x - pipe.targetX;
            activity.y = activity.y - pipe.targetY;
            activity.success = success;
            gs.registerActivity(activity);
            System.out.println("x : "+activity.x+" y: "+activity.y+" succes: "+activity.success);
        }
    }

    /**
     * Default trigger enter method
     * @param col
     */
    @Override
    public void OnTriggerEnter(final Collider col) {
        if(col.GameObject().tag == DoublePipe.TAG){
            score++;
            normalizeAndAddActivity(true);
        }
        if(col.GameObject().tag == Pipe.TAG){
            Die();
            normalizeAndAddActivity(false);
        }
    }

    /**
     * Default trigger exit method
     * @param col
     */
    @Override
    public void OnTriggerExit(Collider col) { }

    private String RandomTexture(){
        String[] names = new String[]{
                "BirdBlue",
                "BirdRed",
                "BirdGreen",
                "BirdPink",
                "BirdCyan",
                "BirdPurple"
        };
        int random =  (int)(Math.random() * names.length);
        return names[random];
    }
}
