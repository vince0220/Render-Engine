package Engine.Core.Scenes;

public final class Time {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public static variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static int framesPerSecond = 60;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private static variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private static long _targetTime;
    private static long _startTime;
    private static float _deltaTime;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public static voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static boolean Update(){
        if(_startTime == 0){ _startTime = System.currentTimeMillis();} // set initial start time
        long currentTime = System.currentTimeMillis();
        if(currentTime > _targetTime){
            _deltaTime = ((float)(currentTime - _startTime) / 1000f);
            _targetTime = currentTime + (long)((1f / (float)framesPerSecond) * 1000f);
            _startTime = currentTime;
            return true;
        }
        return false;
    }
    public static float DeltaTime(){
        return _deltaTime;
    }
    public static int FrameRate(){
        return (int)(1f / _deltaTime);
    }
}
