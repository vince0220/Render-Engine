package Engine.Core.Objects.Components;

import Engine.Core.Scenes.Time;
import Engine.Graphics.Graphics3D.Mesh;
import Engine.Graphics.Rendering.Shader;

public class AnimatedMeshRenderer extends MeshRenderer {

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * All frame meshes of animation renderer
     */
    public Mesh[] frames;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private float _currentFrame;
    private float _speed;
    private boolean _paused;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor
     * @param frames all mesh frames
     * @param shader shader to use for rendering
     */
    public AnimatedMeshRenderer(Mesh[] frames, Shader shader) {
        this(frames,0,shader);
    }

    /**
     * Constructor
     * @param frames all mesh frames
     * @param baseMesh index of the base mesh. used for boundary calculations etc.
     * @param shader shader to use for rendering
     */
    public AnimatedMeshRenderer(Mesh[] frames, int baseMesh, Shader shader){
        super(frames[baseMesh], shader); // call super with first frame given
        this.frames = frames; // set frames
        this._speed = Time.framesPerSecond; // set speed
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Base
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Mesh of current frame
     * @return current frame mesh
     */
    @Override
    public Mesh Mesh() {
        return frames[CurrentFrame()]; // return current frame
    }

    /**
     * Update animated mesh renderer
     */
    @Override
    public void Update(){
        if(!_paused){
            UpdateCurrentFrame();
        }
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Pause animator
     */
    public void Pause(){
        _paused = true;
    }

    /**
     * Resume animator
     */
    public void Resume(){
        _paused = false;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void UpdateCurrentFrame(){
        _currentFrame += (_speed * Time.DeltaTime());
        if(CurrentFrame() >= frames.length){ _currentFrame = 0f;} // check for up reset
        if(CurrentFrame() < 0){_currentFrame = (frames.length - 1);} // check for down reset
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Get current frame of animation
     * @return current frame
     */
    public int CurrentFrame(){
        return (int)_currentFrame;
    }

    /**
     * Get animation speed
     * @return animation speed
     */
    public float AnimationSpeed(){
        return _speed;
    }

    /**
     * Set speed of animation
     * @param speed desired speed
     */
    public void SetSpeed(float speed){
        _speed = speed;
    }
}
