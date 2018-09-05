package Engine.Utils;

import java.nio.file.*;

public class Utilities {
    // ------------------------------------------------------------------------------------------------------------------------------------------
    // Array utilities
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static final <T> T[] Swamp(T[] a, int ia, int ib){
        T b = a[ia];
        a[ia] = a[ib];
        a[ib] = b;
        return a;
    }
    public static final <T> T[] Swamp(T[] a){
        T b = a[0];
        a[0] = a[1];
        a[1] = b;
        return a;
    }

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // String utilities
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static String GetPathExtension(String path){
        int i = path.lastIndexOf('.');
        if(i > 0){
            return path.substring(i+1);
        }
        return "";
    }
    public static String GetPathWithoutExtension(String path){
        int i = path.lastIndexOf('.');
        if(i > 0){
            return path.substring(0,i);
        }
        return "";
    }
    public static String GetFileName(String path){
        int i = path.lastIndexOf('/');
        if(i > 0){
            return GetPathWithoutExtension(path.substring(i+1));
        }
        return "";
    };

    // ------------------------------------------------------------------------------------------------------------------------------------------
    // File utilities
    // ------------------------------------------------------------------------------------------------------------------------------------------
    public static boolean FileExists(String path){
        Path IOPath = Paths.get(path); // initialize io path
        return Files.exists(IOPath);// check if files exits
    }
}
