package Engine.Resources.Assets;

import Engine.Resources.Assets.Images.ImageImporter;
import Engine.Resources.Assets.Mesh.MeshImporter;
import Engine.Utils.Utilities;

import java.util.HashMap;
import java.util.Map;

public class AssetManager {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Singleton
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private static AssetManager _manager;
    public static AssetManager I(){
        if(_manager == null){
            _manager = new AssetManager(new IAssetImporter[]{
                    new MeshImporter(),
                    new ImageImporter()
            });
        }
        return  _manager;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private variables
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private IAssetImporter[] _importers;
    private Map<String,Object> _Assets = new HashMap<String,Object>();

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private AssetManager(IAssetImporter[] importers) {
        this._importers = importers;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Public voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public final boolean PreloadAsset(String path,String identifier){
        if(Utilities.FileExists(path) && !Exists(identifier)) { // check if files exits and if its not already loaded
            IAssetImporter importer = FindImporter(path); // find importer
            if (importer != null) { // if found importer
                return InsertAsset(importer.ImportAsset(path), identifier);
            }
        }
        return false;
    }
    public final boolean PreloadAsset(String path){
        return PreloadAsset(path,Utilities.GetFileName(path));
    }
    public final <T> T FindAsset(String identifier,Class<T> type){
        if(Exists(identifier)){ // check if contains key
            Object obj = _Assets.get(identifier);
            if(type.isInstance(obj)){
                return (T) _Assets.get(identifier);
            }
        }
        return null;
    }
    public final boolean Exists(String identifier){
        return _Assets.containsKey(identifier);
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Private voids
    // ------------------------------------------------------------------------------------------------------------------------------------------
    private boolean InsertAsset(Object asset, String identifier){
        if(asset != null && !_Assets.containsKey(identifier)) {
            _Assets.put(identifier,asset);
            return true;
        }
        return false;
    }
    private IAssetImporter FindImporter(String path){
        String extension = Utilities.GetPathExtension(path);
        for(int i = 0; i < _importers.length; i++){
            if(ImporterContainsExtension(extension,_importers[i])){
                return _importers[i];
            }
        }
        return null;
    }
    private boolean ImporterContainsExtension(String extension,IAssetImporter importer){
        String[] importerExtensions = importer.Extensions();
        for(int i = 0; i < importerExtensions.length;i++){
            if(importerExtensions[i].equals(extension)){
                return  true;
            }
        }
        return false;
    }
}
