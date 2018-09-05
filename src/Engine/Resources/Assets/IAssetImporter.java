package Engine.Resources.Assets;

import java.io.FileNotFoundException;

public interface IAssetImporter {
    String[] Extensions();
    Object ImportAsset(String path);
}
