package win95.constants;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.HashMap;

public class Icons {
    private final static HashMap<String,String> iconPath = new HashMap<>();
    private static final String fileIconDirectory = FileSystems.getDefault().getPath("src/win95/asset/file_icons").normalize().toAbsolutePath().toString();
    private static final String otherIconDirectory = FileSystems.getDefault().getPath("src/win95/asset/other").normalize().toAbsolutePath().toString();

    public static final String DARK_MENU_DOT = otherIconDirectory+"/menu_dark.png";
    public static final String LIGHT_MENU_DOT = otherIconDirectory+"/menu_light.png";

    public static final String LIGHT_LEFT_ARROW = otherIconDirectory+"/left_light1.png";
    public static final String DARK_LEFT_ARROW = otherIconDirectory+"/left_dark.png";

    public static final String LIGHT_RIGHT_ARROW = otherIconDirectory+"/right_light1.png";
    public static final String DARK_RIGHT_ARROW = otherIconDirectory+"/right_dark.png";

    public static final String HOME_LIGHT = otherIconDirectory+"/home_light.png";
    public static final String HOME_DARK = otherIconDirectory+"/home_dark.png";

    public static final String DOWNLOAD_LIGHT = otherIconDirectory+"/download_light.png";
    public static final String DOWNLOAD_DARK = otherIconDirectory+"/download_dark.png";

    public static final String DOCUMENT_LIGHT = otherIconDirectory+"/document_light.png";
    public static final String DOCUMENT_DARK = otherIconDirectory+"/document_dark.png";

    public static final String DESKTOP_LIGHT = otherIconDirectory+"/desktop_light.png";
    public static final String DESKTOP_DARK = otherIconDirectory+"/desktop_dark.png";

    public static final String RECENT_LIGHT = otherIconDirectory+"/recent_light.png";
    public static final String RECENT_DARK = otherIconDirectory+"/recent_dark.png";

    static {
        iconPath.put("ai", fileIconDirectory +"/ai.png");
        iconPath.put("apk", fileIconDirectory +"/apk.png");
        iconPath.put("archive", fileIconDirectory +"/archive.png");
        iconPath.put("avi", fileIconDirectory +"/avi.png");
        iconPath.put("c", fileIconDirectory +"/c.png");
        iconPath.put("cpp", fileIconDirectory +"/cpp.png");
        iconPath.put("css", fileIconDirectory +"/css.png");
        iconPath.put("csv", fileIconDirectory +"/csv.png");
        iconPath.put("doc", fileIconDirectory +"/doc.png");
        iconPath.put("doc#", fileIconDirectory +"/doc#.png");
        iconPath.put("exe", fileIconDirectory +"/exe.png");
        iconPath.put("directory", fileIconDirectory +"/directory.png");
        iconPath.put("gif", fileIconDirectory +"/gif.png");
        iconPath.put("html", fileIconDirectory +"/html.png");
        iconPath.put("ios", fileIconDirectory +"/ios.png");
        iconPath.put("java", fileIconDirectory +"/java.png");
        iconPath.put("jpg", fileIconDirectory +"/jpg.png");
        iconPath.put("js", fileIconDirectory +"/js.png");
        iconPath.put("pdf", fileIconDirectory +"/pdf.png");
        iconPath.put("php", fileIconDirectory +"/php.png");
        iconPath.put("ppt", fileIconDirectory +"/ppt.png");
        iconPath.put("pptx", fileIconDirectory +"/ppt.png");
        iconPath.put("rar", fileIconDirectory +"/rar.png");
        iconPath.put("ttf", fileIconDirectory +"/ttf.png");
        iconPath.put("unknown", fileIconDirectory +"/unknown.png");
        iconPath.put("mp4", fileIconDirectory +"/video.png");
        iconPath.put("mkv", fileIconDirectory +"/video.png");
        iconPath.put("wav", fileIconDirectory +"/wav.png");
        iconPath.put("woff", fileIconDirectory +"/woff.png");
        iconPath.put("xlsx", fileIconDirectory +"/xlsx.png");
        iconPath.put("xml", fileIconDirectory +"/xml.png");
        iconPath.put("zip", fileIconDirectory +"/zip.png");

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
