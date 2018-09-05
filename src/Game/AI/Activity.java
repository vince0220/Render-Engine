package Game.AI;

public class Activity {

    /**
     * x position for this activity
     */
    public float x = 0;

    /**
     * y position for this activity
     */
    public float y = 0;

    /**
     * consequence for this activity
     */
    public boolean success = false;

    /**
     * Empty constructor, initiates a blank Activity
     */
    public Activity(){}

    /**
     * Initiates a Activity
     * @param x x position of the bird
     * @param y y position of the bird
     * @param success should be true if the activity did not kill the bird
     */
    public Activity(final float x, final float y, final boolean success){
        this.x = x;
        this.y = y;
        this.success = success;
    }
}
