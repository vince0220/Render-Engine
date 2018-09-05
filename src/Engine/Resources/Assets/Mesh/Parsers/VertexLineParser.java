package Engine.Resources.Assets.Mesh.Parsers;

import Engine.Graphics.Graphics3D.Mesh;
import Engine.Resources.IO.Parsers.ITypeParser;

public class VertexLineParser implements ITypeParser<Mesh,String> {
    @Override
    public boolean CanParse(String line) {
        return (line.charAt(0) == 'v' && line.charAt(1) == ' ');
    }

    @Override
    public Mesh Parse(String Data) {
        return null;
    }
}
