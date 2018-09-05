package Game.Shading;

import Engine.Graphics.Rendering.FragmentData;
import Engine.Graphics.Rendering.Shader;
import Engine.Graphics.Rendering.VertexData;
import Engine.Utils.MathUtillities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StandardShader extends Shader {
    private BufferedImage albedo;
    private Color color;

    /**
     * Initializes standard shader with default white color
     */
    public StandardShader(){
        this.color = new Color(255,255,255);
    }

    /**
     * Initializes standard shader with given color
     * @param col color of mesh
     */
    public StandardShader(Color col){
        this.color = col;
    }

    /**
     * Initializes standard shader with given color and albedo texture
     * @param col color of mesh
     * @param albedo albedo texture
     */
    public StandardShader(Color col, BufferedImage albedo){
        this.color = col;
        this.albedo = albedo;
    }

    /**
     * Initializes standard shader with given albedo texture and default white color
     * @param albedo albedo texture to use
     */
    public StandardShader(BufferedImage albedo){
        this.albedo = albedo;
        this.color = new Color(255,255,255);
    }

    /**
     * Default vertexdata event
     * @param data
     * @return
     */
    @Override
    public VertexData VertexShader(VertexData data) {
        return data;
    }

    /**
     * Returns color of the pixel
     * @param fragment
     * @return
     */
    @Override
    public Color FragmentShader(FragmentData fragment) {
        float depth = MathUtillities.Clamp((float)Math.pow(fragment.depth + 0.04f,40),0f,1f);
        Color col = new Color(255,255,255);
        if(this.albedo != null){
            col = SampleTexture(albedo,fragment.uv);
        }
        return LerpColor(col,new Color(98, 182,255),depth);
    }
}
