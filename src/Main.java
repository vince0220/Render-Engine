import Engine.Algebra.Vectors.Vector2;
import Engine.Core.EngineCore;
import Engine.Core.Scenes.Scene;
import Engine.Core.Screen.Screen;
import Game.Scenes.GameScene;
import Game.Scenes.StartScene;

public class Main {
    private Main(){
        new EngineCore(new Scene[]{
                new StartScene(),
                new GameScene()
        },0,new Vector2(1024,1024),2);
    }
    
    public static void main(final String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
