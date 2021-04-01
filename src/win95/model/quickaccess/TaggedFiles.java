package win95.model.quickaccess;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import win95.debug.LogsPrinter;
import win95.utilities.pathmanipulation.PathHandling;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaggedFiles {
    public static final Map<String, TagDetail> taggedFile = new HashMap<>();
    public static final Map<String, String> taggedColorToNameMap = new HashMap<>();

    public static Map<String, TagDetail> getTaggedFile() {
        return taggedFile;
    }

    public static Map<String, String> getTaggedColorToNameMap() {
        return taggedColorToNameMap;
    }

    public static void fetchTagged() {
        JSONParser jsonParser = new JSONParser();

        File USER_HOME = new File(System.getProperty("user.home"));
        String homeFolderPath = USER_HOME.getAbsolutePath() + "/.CosmoFileManager";
        File homeFolder = new File(homeFolderPath);

        if (homeFolder.exists() && homeFolder.isDirectory()) {

            String tagFilePath = homeFolder + "/tagged.json";
            File tagFile = new File(tagFilePath);

            if (tagFile.exists() && tagFile.isFile()) {
                try (FileReader reader = new FileReader(tagFilePath)) {

                    Object tagFileListObject = jsonParser.parse(reader);
                    JSONArray tagFileList = (JSONArray) tagFileListObject;
                    System.out.println("fetch from json : "+tagFileList.toJSONString());
                    for (Object jsonObject : tagFileList) {
                        addTaggedQueue((JSONObject) jsonObject);
                    }

                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    if (tagFile.createNewFile()) {
                        setDefaultTag();
                        LogsPrinter.printLogic("TaggedFiles", 42,
                                "Successfully crated tagged.json");
                    } else {
                        LogsPrinter.printLogic("TaggedFiles", 42,
                                "return false in crating tagged.json");
                    }
                } catch (IOException e) {
                    LogsPrinter.printError("TaggedFiles", 42,
                            "IOException in crating tagged.json");
                }
            }
        } else {
            if (homeFolder.mkdir()) {
                String tagFilePath = homeFolder + "/tagged.json";
                File tagFile = new File(tagFilePath);
                LogsPrinter.printLogic("TaggedFiles", 52,
                        "Successfully crated home folder");
                try {
                    if (tagFile.createNewFile()) {
                        setDefaultTag();
//                        saveTagged();
                        LogsPrinter.printLogic("TaggedFiles", 52,
                                "Successfully crated tagged.json");
                    } else {
                        LogsPrinter.printLogic("TaggedFiles", 52,
                                "return false in crating tagged.json");
                    }
                } catch (IOException e) {
                    LogsPrinter.printError("TaggedFiles", 52,
                            "IOException in crating tagged.json");
                }
            } else {
                LogsPrinter.printError("TaggedFiles", 52,
                        "some error occur in crating home folder");
            }
        }
    }

    public static void addTaggedQueue(JSONObject jsonObject) throws IOException {
        JSONArray path = (JSONArray) jsonObject.get("path");
        String color = (String) jsonObject.get("color");
        String name = (String) jsonObject.get("name");
        addTaggedQueueNoPath(color,name);
        if(path.isEmpty()) addNewCreatedTag(color, name);
        System.out.println("Adding tag after fetch : "+jsonObject.toJSONString());
        for (Object pathJson : path) {
            addTaggedQueue(color, (String) pathJson, name);
        }


    }

    public static void addTaggedQueueNoPath(String color, String name){
        TagDetail tagDetail = new TagDetail(color, name);
        taggedFile.put(color, tagDetail);
        taggedColorToNameMap.put(color, name);
    }
    // use to add from json
    public static void addTaggedQueue(String color, String path, String name) {
        path = new PathHandling(path).getFixedPath();
        if (taggedFile.containsKey(color)) {
            if (!taggedFile.get(color).getPath().contains(path)) {
                taggedFile.get(color).getPath().add(path);
            }
        } else {
            TagDetail tagDetail = new TagDetail(color, name);
            tagDetail.getPath().add(path);
            taggedFile.put(color, tagDetail);
            taggedColorToNameMap.put(color, name);
        }
        addNewCreatedTag(color, name);
    }
    //use to add file to already exist tag
    public static void addThisFileToTag(String color,String path,String tagName){
        path = new  PathHandling(path).getFixedPath();
//        System.out.println("in add this file To tag "+color+" "+path+" "+tagName);
        if(!taggedFile.get(color).getPath().contains(path)){
            taggedFile.get(color).getPath().add(path);
        }
    }

    public static boolean addNewCreatedTag(String color, String name) {
        if (taggedColorToNameMap.containsKey(color)) {
            return false;
        } else {
            for (Map.Entry<String, String> tag : taggedColorToNameMap.entrySet()) {
                if (tag.getValue().equals(name)) return false;
            }
//            System.out.println("new tag added : " + color + " " + name);
            taggedColorToNameMap.put(color, name);
            taggedFile.put(color, new TagDetail(color, name));
            return true;
        }
    }

    public static void setDefaultTag() {
        taggedFile.clear();
        taggedFile.put("0XFF0000FF", new TagDetail("0XFF0000FF", "RED"));
        taggedFile.put("0X808080FF", new TagDetail("0X808080FF", "GRAY"));
        taggedFile.put("0XFFFF00FF", new TagDetail("0XFFFF00FF", "YELLOW"));
        taggedFile.put("0X00FF00FF", new TagDetail("0X00FF00FF", "GREEN"));
        taggedFile.put("0XFFA500FF", new TagDetail("0XFFA500FF", "ORANGE"));
        taggedColorToNameMap.clear();
        taggedColorToNameMap.put("0XFF0000FF", "RED");
        taggedColorToNameMap.put("0X808080FF", "GRAY");
        taggedColorToNameMap.put("0XFFFF00FF", "YELLOW");
        taggedColorToNameMap.put("0X00FF00FF", "GREEN");
        taggedColorToNameMap.put("0XFFA500FF", "ORANGE");
    }

    public static void saveTagged() {
        String taggedFilePath = new File(System.getProperty("user.home")).getAbsolutePath() +
                "/.CosmoFileManager/tagged.json";

        JSONArray taggedFileArray = new JSONArray();
        for (Map.Entry<String, TagDetail> color : taggedFile.entrySet()) {
            TagDetail tagDetail = color.getValue();
            JSONArray pathArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("color", tagDetail.getColor());
            jsonObject.put("name", tagDetail.getName());
            for (String path : tagDetail.getPath()) {
                pathArray.add(path);
            }
            jsonObject.put("path", pathArray);
            taggedFileArray.add(jsonObject);
        }
//        System.out.println("saving json object to file : ");
//        System.out.println(taggedFileArray.toJSONString());
        try (FileWriter file = new FileWriter(taggedFilePath)) {
            file.write(taggedFileArray.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getList(String color) {
        if (taggedFile.containsKey(color)) return taggedFile.get(color).getPath();
        return new ArrayList<>();
    }

    public static void setUserTag() {
        if (taggedFile.isEmpty()) {
//            System.out.println("setting default");
            setDefaultTag();
        }
        else{
//            System.out.println("Setting tags fetch from file");
        }

    }

    public static ArrayList<String> getThisColorFile(String color) {
        return taggedFile.get(color).getPath();
    }

    public String toString() {
        String res = "Color = ";
        for (Map.Entry<String, TagDetail> tags : taggedFile.entrySet()) {
            String tag = tags.getKey() + "\n";
            res += tag + "\nPath = ";
            for (String path : tags.getValue().getPath()) {
                res += path + " ";
            }

        }
        return res;
    }

    public static void deleteTag(String color){
        taggedFile.remove(color);
        taggedColorToNameMap.remove(color);
    }

    public static boolean editTag(String color,String newColor,String name,String newName){
        if(newColor.equals(color)){
            if(name.equals(newName)) {
                return true;
            }
            else{
                for(Map.Entry<String,String> tag : taggedColorToNameMap.entrySet()){
                    if(tag.getValue().equals(newName)) return false;
                }
                taggedColorToNameMap.put(color,newName);
                taggedFile.get(color).setName(name);
                return true;
            }
        }else{
            if(taggedColorToNameMap.containsKey(newColor)) {
                return false;
            }
            else{
                if(!newName.equals(name)) {
                    for (Map.Entry<String, String> tag : taggedColorToNameMap.entrySet()) {
                        if (tag.getValue().equals(newName)) return false;
                    }
                }
                taggedColorToNameMap.remove(color);
                taggedColorToNameMap.put(newColor,newName);
                TagDetail tagDetail = taggedFile.get(color);
                tagDetail.setName(newName);
                tagDetail.setColor(newColor);
                taggedFile.put(newColor,tagDetail);
                taggedFile.remove(color);
                return true;
            }
        }
    }

    public static String print() {
        String res = "";
//        for (Map.Entry<String, TagDetail> tags : taggedFile.entrySet()) {
//            String tag = "Color = " + tags.getKey() + "\n";
//            String name = "Name = " + taggedColorToNameMap.get(tags.getKey()) + "\n";
//            String paths = "paths = ";
//            for (String path : tags.getValue().getPath()) {
//                paths += path + "  ";
//            }
//            res += name + tag + paths + "\n\n";
//        }
        return res;
    }

}
