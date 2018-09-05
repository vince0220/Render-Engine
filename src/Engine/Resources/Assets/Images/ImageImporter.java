package Engine.Resources.Assets.Images;

import Engine.Resources.Assets.IAssetImporter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageImporter implements IAssetImporter {
    @Override
    public String[] Extensions() {
        return new String[]{
                "jpg",
                "png"
        };
    }

    @Override
    public Object ImportAsset(String path) {
        try
        {
            return ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
