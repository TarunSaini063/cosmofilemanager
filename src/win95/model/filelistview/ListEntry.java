package win95.model.filelistview;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ListEntry {
    String name,url;


    ImageView imageView;
    Label label;
    Button delete,open;
    public ListEntry(String name){
        this.name = name;
        this.imageView = new ImageView();
        this.url = "/Users/tarun/IntellijProjects/CosmoFileManager/src/win95/asset/fileLogo.png";
        Image image = new Image(new File(url).toURI().toString());
        this.imageView.setImage(image);
        label = new Label(name);
        label.setMaxWidth(1000);
        label.setMinWidth(500);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        delete = new Button("Delete");
        open = new Button("Open");
        open.setOnAction(event -> {
            try {
                String command="open /Users/tarun/Desktop/Coding/Codechef/";
                if(name.contains(" ")){
                    command+="\""+name+"\"";
                }
                else command+=name;
                Process open = Runtime.getRuntime().exec(command);
                System.out.println(command);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(open.getErrorStream()));
                String error;
                while((error = bufferedReader.readLine())!=null){
                    System.out.println(error);
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        });
    }

    public Label getLabel() {
        return label;
    }
    public String getName() {
        return name;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Button getDelete() {
        return delete;
    }

    public Button getOpen() {
        return open;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public void setOpen(Button open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "ListEntry{" +
                "name='" + name + '\'' +
                ", imageView=" + "imageView" +
                ", delete=" + "delete" +
                ", open=" + "open" +
                '}';
    }

}
