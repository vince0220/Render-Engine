package Engine.Graphics.Graphics2D.Utils;

public class Graphics2DUtils {
    // calculations
    public static int GetIntFromColor(int r, int g, int b){
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;

        return 0xFF000000 | r | g | b;
    }
}
