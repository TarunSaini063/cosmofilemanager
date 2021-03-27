package win95.constants;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.HashMap;

public class FileIcons {
    private final static HashMap<String,String> iconPath = new HashMap<>();
    private static final String iconDirectory = FileSystems.getDefault().getPath("src/win95/asset/file_icons").normalize().toAbsolutePath().toString();
    static {
        iconPath.put("ai",iconDirectory+"/ai.png");
        iconPath.put("apk",iconDirectory+"/apk.png");
        iconPath.put("archive",iconDirectory+"/archive.png");
        iconPath.put("avi",iconDirectory+"/avi.png");
        iconPath.put("c",iconDirectory+"/c.png");
        iconPath.put("cpp",iconDirectory+"/cpp.png");
        iconPath.put("css",iconDirectory+"/css.png");
        iconPath.put("csv",iconDirectory+"/csv.png");
        iconPath.put("doc",iconDirectory+"/doc.png");
        iconPath.put("doc#",iconDirectory+"/doc#.png");
        iconPath.put("exe",iconDirectory+"/exe.png");
        iconPath.put("directory",iconDirectory+"/directory.png");
        iconPath.put("gif",iconDirectory+"/gif.png");
        iconPath.put("html",iconDirectory+"/html.png");
        iconPath.put("ios",iconDirectory+"/ios.png");
        iconPath.put("java",iconDirectory+"/java.png");
        iconPath.put("jpg",iconDirectory+"/jpg.png");
        iconPath.put("js",iconDirectory+"/js.png");
        iconPath.put("pdf",iconDirectory+"/pdf.png");
        iconPath.put("php",iconDirectory+"/php.png");
        iconPath.put("ppt",iconDirectory+"/ppt.png");
        iconPath.put("pptx",iconDirectory+"/ppt.png");
        iconPath.put("rar",iconDirectory+"/rar.png");
        iconPath.put("ttf",iconDirectory+"/ttf.png");
        iconPath.put("unknown",iconDirectory+"/unknown.png");
        iconPath.put("mp4",iconDirectory+"/video.png");
        iconPath.put("mkv",iconDirectory+"/video.png");
        iconPath.put("wav",iconDirectory+"/wav.png");
        iconPath.put("woff",iconDirectory+"/woff.png");
        iconPath.put("xlsx",iconDirectory+"/xlsx.png");
        iconPath.put("xml",iconDirectory+"/xml.png");
        iconPath.put("zip",iconDirectory+"/zip.png");

    }

    public static File getFileIcon(String fileType){
        if(iconPath.containsKey(fileType)){
            return new  File(iconPath.get(fileType));
        }else{
            return new File(iconPath.get("unknown"));
        }

    }
    public static String getFileIconPath(String fileType){
        if(iconPath.containsKey(fileType)){
            return iconPath.get(fileType);
        }else{
            return iconPath.get("unknown");
        }

    }
}
