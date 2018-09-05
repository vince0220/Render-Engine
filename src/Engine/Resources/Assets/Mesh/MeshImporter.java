package Engine.Resources.Assets.Mesh;

import Engine.Resources.Assets.IAssetImporter;
import Engine.Resources.Assets.Mesh.Parsers.ObjParser;

import java.io.*;

public class MeshImporter implements IAssetImporter {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // IAssetImporter implementation
    // ------------------------------------------------------------------------------------------------------------------------------------------
    @Override
    public String[] Extensions() {
        return new String[]{
                "obj"
        };
    }
    @Override
    public Object ImportAsset(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            return new ObjParser().Parse(reader);
        } catch (IOException e){
            System.out.println("Mesh import error! "+e.getMessage());
        }
        return null;
    }
}
