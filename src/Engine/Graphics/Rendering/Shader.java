package Engine.Graphics.Rendering;

import Engine.Algebra.Matrices.Matrix;
import Engine.Algebra.Vectors.Vector2;
import Engine.Algebra.Vectors.Vector3;
import Engine.Algebra.Vectors.Vector4;
import Engine.Utils.MathUtillities;
import javafx.scene.shape.CullFace;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Shader {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Protected variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    protected Matrix localToWorld;
    protected Matrix worldToCamera;

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public abstract voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public abstract VertexData VertexShader(VertexData data);
    public abstract Color FragmentShader(FragmentData data);

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public void InitializeDrawCall(DrawCall Call) {
        this.localToWorld = Call.LocalToWorld();
        this.worldToCamera = Call.WorldToCamera();
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Get / Set
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public int Culling(){
        return 1;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Protected voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    protected Color SampleTexture(BufferedImage texture, Vector2 coord){
        int color = texture.getRGB(
                (int)MathUtillities.Clamp (coord.x() * (float)texture.getWidth(),0, texture.getWidth() - 1),
                (int)MathUtillities.Clamp ((1f - coord.y()) * texture.getHeight(),0f,texture.getHeight() - 1)
        );

        return new Color(
                ((color & 0x00ff0000) >> 16),
                ((color & 0x0000ff00) >> 8),
                (color & 0x000000ff)
        );
    }
    protected Color LerpColor(Color a,Color b, float t){
        return new Color(
                (int)MathUtillities.FastLerp(a.getRed(),b.getRed(),t),
                (int)MathUtillities.FastLerp(a.getGreen(),b.getGreen(),t),
                (int)MathUtillities.FastLerp(a.getBlue(),b.getBlue(),t)
        );
    }
}
