package Game.UI;

import Engine.Core.Input.InputManager;
import Engine.Core.Objects.GameObject;
import Engine.Core.Screen.Screen;
import com.sun.istack.internal.NotNull;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class InterfaceController extends GameObject {

    private static InterfaceController _instance;
    
    /**
     * Returns a instance of the interface controller
     * @return instance of the interface controller
     */
    public static InterfaceController GetInstance(){
        return _instance = _instance==null ? new InterfaceController():_instance;
    }

    private Font gameFont;
    private int scoreCache = 0;

    private InterfaceController(){
        InitFont();
    }

    /**
     * Default engine update method
     */
    @Override
    public void Update(){
        if(InputManager.I().GetKeyDown(KeyCode.A)){
                Scene().ReloadScene();
        }
    }

    /**
     * Draws game over screen
     * @param image
     */
    public void DrawGameOver(final BufferedImage image){
        DrawMessage(image,"GAME OVER", "Press space to try again");
    }

    /**
     * Draws start screen message
     * @param image buffered image to draw on
     */
    public void DrawStart(final BufferedImage image){
        DrawMessage(image,"BEST OF THE NEST","Press space to play!");
    }

    /**
     * Draws a message and a submessage in the center of the screen
     * @param image buffered image to draw on
     * @param message message to draw on the screen
     * 
     * @param subMessage
     */
    public void DrawMessage(final BufferedImage image, final String message, final String subMessage){
        final float fontSize = 160;
        final float smallFontSize = 60;
        final int centerY = image.getHeight()/2;
        final int centerX = image.getWidth()/2;
        DrawText(image,message,centerX,centerY,fontSize);
        DrawText(image,subMessage,centerX,centerY+20,smallFontSize);
    }

    /**
     * Draws score on screen
     * @param image buffered image to draw on
     */
    public void DrawScore(final BufferedImage image){
        final float scale = Screen.I().DownSampleRatio();
        float marge = 100;
        if(scale>0){
            marge*=scale;
        }
        DrawText(image,"Score:"+scoreCache,image.getWidth()-(int)marge,(int)marge,60f);
    }

    /**
     * Draws text on screen
     * @param image buffered image to draw on
     * @param text message message to draw on the screen
     * @param x x drawing point
     * @param y y drawing point
     * @param size text size
     */
    private void DrawText(@NotNull final BufferedImage image,@NotNull final String text,
                          @NotNull int x,@NotNull int y, @NotNull final float size){
        final Graphics g = image.getGraphics();
        final float scale = Screen.I().DownSampleRatio();
        if(gameFont!=null){
            g.setFont(gameFont.deriveFont(size*scale));
            g.getFontMetrics(g.getFont());
            final FontMetrics metrics = g.getFontMetrics(g.getFont());
            final Rectangle2D stringBounds = metrics.getStringBounds(text,g);
            final double stringWidth = stringBounds.getWidth();
            final double stringHeight = stringBounds.getHeight();
            x-=(stringWidth/2);
            y-=(stringHeight/2);
        }
        g.setColor(Color.black);
        g.drawString(text,(x+2),(y+2));
        g.setColor(Color.white);
        g.drawString(text,x,y);
    }

    /**
     * Loads font from filesystem en loads the font
     */
    private void InitFont(){
        gameFont = loadFont("res/font.ttf");
    }

    /**
     * Caches player score value
     * @param score score value
     */
    public void SetScoreCache(final int score){
        scoreCache = score;
    }

    /**
     * Loads a font from the filesystem
     * @param name name of the file
     * @return loaded font 
     */
    private Font loadFont(final String name) {
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File(name));
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return font;
    }
}
