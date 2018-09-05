package Engine.Core.Screen;

import Engine.Algebra.Vectors.Vector2;
import Engine.Core.Input.InputManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class Screen extends JFrame {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Singleton
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private static Screen _i;
    public static Screen I(){
        if(_i == null){
            _i = new Screen();
        }
        return _i;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private ImageIcon _screen;
    private Vector2 _canvasSize;
    private int _downSample;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private Screen(){ }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private void InitializeGraphics(){
        this._screen = new ImageIcon();
        this.add(new JLabel(this._screen));
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void Init(Vector2 screenSize,int downSample){
        this.setTitle("Game window"); // set base title
        _downSample = downSample;
        this._canvasSize = new Vector2((int)(screenSize.x() / (float)downSample), (int)(screenSize.y() / (float) downSample));
        this.setVisible(true); // make visible
        this.setResizable(false); // none resizable
        InitializeGraphics(); // initialize screen
        SetSize(screenSize); // set frame size
        
        // Add listeners
        this.addKeyListener(InputManager.I());
        this.addMouseListener(InputManager.I());
    }
    public void SetSize(Vector2 size){
        this.setSize((int)size.x(),(int)size.y()); // set screen size
    }
    public void UpdateFrame(BufferedImage frame){
        this._screen.setImage(RescaleImage(frame)); // set frame
        this.repaint();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private BufferedImage RescaleImage(BufferedImage frame){
        if(_downSample != 1) {
                BufferedImage scaledImage = new BufferedImage(
                        getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics2D = scaledImage.createGraphics();
                graphics2D.drawImage(frame, 0, 0, getWidth(), getHeight(), null);
                graphics2D.dispose();
                return scaledImage;
        }
        return frame;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Aspect ratio of the screen
     * @return
     */
    public float DeviceAspectRatio(){
        return getWidth() / getHeight();
    }
    public float DownSampleRatio(){
        return 1f / _downSample;
    }
    public Vector2 CanvasSize(){
        return _canvasSize;
    }
}
