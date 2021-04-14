package win95.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import win95.constants.CommonData;
import win95.constants.Fonts;
import win95.constants.Icons;
import win95.model.wirelessTransfer.ConnectionIO;
import win95.model.wirelessTransfer.FileMetaData;
import win95.model.wirelessTransfer.connection.Common;
import win95.model.wirelessTransfer.connection.InitConnectionClient;
import win95.model.wirelessTransfer.connection.InitConnectionClientIP;
import win95.model.wirelessTransfer.connection.InitConnectionServer;
import win95.model.wirelessTransfer.connection.callbacks.ConnectionCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;
import win95.model.wirelessTransfer.connection.sockets.FileMetaDataSender;
import win95.model.wirelessTransfer.connection.sockets.FileReceiver;
import win95.model.wirelessTransfer.connection.sockets.FileSender;
import win95.model.wirelessTransfer.iohandler.FileReader;
import win95.model.wirelessTransfer.iohandler.FileWriter;
import win95.model.wirelessTransfer.terminate.PublicThreads;
import win95.model.wirelessTransfer.wirelessfileslistview.ListOfFileTransfer;
import win95.model.wirelessTransfer.wirelessfileslistview.WirelessCellFactory;
import win95.model.wirelessTransfer.wirelessfileslistview.WirelessListEntry;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Transfer implements Initializable {
    private int confirmation = 0;
    ConnectionIO CLIENT = ConnectionIO.WAITING, SERVER = ConnectionIO.WAITING,
            CURRENT_TRANSFER = ConnectionIO.COMPLETED, FILE_META_RECEIVER = ConnectionIO.INACTIVE;
    @FXML
    private Button remove;
    @FXML
    private ImageView loaderGif;

    @FXML
    private ListView<WirelessListEntry> listView;

    ObservableList<WirelessListEntry> observableList = FXCollections.observableArrayList();
    ArrayList<File> arrayList = new ArrayList<>();
    FileChooser fil_chooser = new FileChooser();
    @FXML
    private Button serverButton;

    @FXML
    private Button clientButton;

    TextField ip = new TextField();

    @FXML
    private Button select;

    @FXML
    private Button send;

    @FXML
    private HBox loader;

    @FXML
    void selectFile(ActionEvent event) {

        List<File> selectedFiles = fil_chooser.showOpenMultipleDialog(select.getScene().getWindow());
        if (selectedFiles != null) {
            for (File file : selectedFiles) {
                System.out.println(file.length());
                System.out.println(file.length() / (1024 * 1024) + " MB");
                if (file.isFile()) {
                    System.out.println("File");
                    arrayList.add(file);
                    observableList.add(new WirelessListEntry(file.getName()));
                } else System.out.println("Directory");
            }
        }
    }

    ConnectionCNF callback = new ConnectionCNF() {
        @Override
        public void clientConnected(String message) {
            if (message.startsWith("FAILURE")) {
                confirmation = -1;
                CLIENT = ConnectionIO.BREAK;
                ip.setDisable(false);
                ip.clear();
                ip.setPromptText("problem in connecting...");
                ip.setStyle("-fx-border-color: red");
            } else {
                confirmation++;
                CLIENT = ConnectionIO.OK;
                loaderGif.setVisible(false);
                loader.getChildren().clear();
                Label confirm = new Label("Connected : " + message);
                confirm.setFont(Fonts.SUCCESS);
                confirm.setStyle("-fx-text-fill: green");
                Common.ip = message;
                ip.setStyle("-fx-border-color: green");
            }
            System.out.println("Client ==> " + message);
        }

        @Override
        public void serverConnected(String message) {
            if (message.equals("FAILURE")) {
                confirmation = -1;
                SERVER = ConnectionIO.BREAK;
            } else {
                confirmation++;
                SERVER = ConnectionIO.OK;
            }
            System.out.println("Server message " + message);
        }
    };

    public FileCNF fileCNFCallback = new FileCNF() {
        @Override
        public void onReceived(String fileMetaData) {
            System.out.println("Received " + fileMetaData);
            int nameIdx = fileMetaData.lastIndexOf(FileSystems.getDefault().getSeparator()) + 1;
            String name = fileMetaData.substring(nameIdx);
            System.out.println("Received string menu : " + name);
            WirelessListEntry wirelessListEntry = new WirelessListEntry(name);
            boolean exist = false;
            for (WirelessListEntry entry : observableList) {
                if (entry.getNameStr().equals(name)) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            entry.getName().setStyle("-fx-text-fill: green");
                            entry.getPercent().setText("100 %");
                            entry.getCancel().setVisible(false);
                            entry.getCancel().setDisable(true);
                        }
                    });

                    exist = true;
                }
            }
            if (!exist) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        wirelessListEntry.getName().setStyle("-fx-text-fill: green");
                        wirelessListEntry.getPercent().setText("100 %");
                        wirelessListEntry.getCancel().setVisible(false);
                        wirelessListEntry.getCancel().setDisable(true);
                        observableList.add(wirelessListEntry);
                    }
                });

            }
            CURRENT_TRANSFER = ConnectionIO.COMPLETED;
        }

        @Override
        public void onSend(String fileMetaData) {
            System.out.println("transfer complete " + fileMetaData);
            CURRENT_TRANSFER = ConnectionIO.COMPLETED;
            System.out.println("File send successfully " + fileMetaData);
            int nameIdx = fileMetaData.lastIndexOf(FileSystems.getDefault().getSeparator()) + 1;
            String name = fileMetaData.substring(nameIdx);
            System.out.println("String menu : " + name);
            WirelessListEntry wirelessListEntry = new WirelessListEntry(name);
            boolean exist = false;
            for (WirelessListEntry entry : observableList) {
                if (entry.getNameStr().equals(name)) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            entry.getName().setStyle("-fx-text-fill: green");
                            entry.getPercent().setText("100 %");
                            entry.getCancel().setDisable(true);
                            entry.getCancel().setVisible(false);
                        }
                    });
                    exist = true;
                }
            }

            if (!exist) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        wirelessListEntry.getName().setStyle("-fx-text-fill: green");
                        wirelessListEntry.getPercent().setText("100 %");
                        wirelessListEntry.getCancel().setDisable(true);
                        wirelessListEntry.getCancel().setVisible(false);
                        observableList.add(wirelessListEntry);
                    }
                });

            }
            System.out.println("start query for next file");
            next();
        }
    };

    FileMetaDataCallBack fileMetaDataCallBack = new FileMetaDataCallBack() {
        @Override
        public void onReceived(FileMetaData fileMetaData) {
            if (fileMetaData != null) {
                System.out.println("in receiver side metaData : " + fileMetaData.toString());
                try {
                    WirelessListEntry wirelessListEntry = new WirelessListEntry(fileMetaData.getName());
                    boolean checker = true;
                    for (WirelessListEntry wirelessListEntry1 : observableList) {
                        if (wirelessListEntry1.getNameStr().equals(wirelessListEntry.getNameStr())) {
                            checker = false;
                        }
                    }
                    if (checker) {
                        observableList.add(wirelessListEntry);
                    }
                    System.out.println("writing data at " + Common.receivedPath + fileMetaData.getName());
                    FileWriter fileWriter = new FileWriter(Common.receivedPath + fileMetaData.getName());
                    FileReceiver fileReceiver = new FileReceiver(fileMetaData, fileCNFCallback, fileWriter);
                    Thread receiverThread = new Thread(fileReceiver);
                    CURRENT_TRANSFER = ConnectionIO.RECEIVING;
                    receiverThread.start();
                    PublicThreads.add(receiverThread);
                    System.out.println("Started Receiving data");
                } catch (IOException e) {
                    System.out.println("this file is already exist ");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error in meta sending");
            }

        }

        @Override
        public void onSend(FileMetaData fileMetaData) throws IOException {
            if (fileMetaData != null) {
                System.out.println("On sender side MetaData : " + fileMetaData.toString());
                FileSender fileSender = new FileSender();
                assert fileSender != null;
                try {
                    System.out.println("Starting sending thread of file " + fileMetaData.getPath());
                    FileReader fileReader = new FileReader(fileSender, fileMetaData.getPath(), fileCNFCallback);
                    Thread fileReaderThread = new Thread(fileReader);
                    CURRENT_TRANSFER = ConnectionIO.SENDING;
                    fileReaderThread.start();
                    PublicThreads.add(fileReaderThread);
                    System.out.println("Thread started successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error in meta received");
            }
        }

    };
    String type;

    @FXML
    void initClient(ActionEvent event) {
        Common.CLIENT = true;
        type = "CLIENT";
        clientButton.setDisable(true);
//        InitConnectionClient initConnectionClient = new InitConnectionClient(callback);
//        Thread thread = new Thread(initConnectionClient);
//        thread.start();
        ip.setPromptText("Enter link code for faster connection");
        loader.getChildren().add(ip);

        ip.setOnKeyPressed(event1 -> {
            if (event1.getCode() == KeyCode.ENTER) {
                String code = ip.getText();
                if (code.length() != 6) {
                    ip.clear();
                    ip.setPromptText("Invalid code");
                    ip.setStyle("-fx-border-color: red");
                } else {
                    String codeStr = ip.getText();
                    try {
                        Integer.parseInt(codeStr);
                    } catch (NumberFormatException e) {
                        ip.clear();
                        ip.setPromptText("Invalid code");
                        ip.setStyle("-fx-border-color: red");
                        return;
                    }
                    String firstThree = codeStr.substring(0, 3);
                    String lastThree = codeStr.substring(3);
                    String serverIp = "192.168." + firstThree + "." + lastThree;
                    ip.setText(serverIp);
                    ip.setDisable(true);
                    Common.ip = serverIp;
                    InitConnectionClientIP initConnectionClientIP = new InitConnectionClientIP(callback);
                    System.out.println("Starting thread to make meta link on given ip ...");
                    Thread start = new Thread(initConnectionClientIP);
                    start.start();
                    PublicThreads.add(start);
                }
            }
        });

    }

    @FXML
    void initServer(ActionEvent event) throws UnknownHostException {
        Common.CLIENT = false;
        type = "SERVER";
        serverButton.setDisable(true);

        Label label = new Label("Generating link code..");
        label.setAlignment(Pos.CENTER);
        loader.getChildren().add(label);

        Label ip = new Label();
        Thread thread1 = new Thread(() -> {
            InetAddress inetAddress = null;
            try {
                inetAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            assert inetAddress != null;
            String strIP = inetAddress.getHostAddress();
            String[] ipcode = strIP.split("\\.");
            System.out.println(strIP);
            String code = ipcode[2] + ipcode[3];
            ip.setText("Link Code : " + code);
            Platform.runLater(() -> {
                loader.getChildren().clear();
                loader.getChildren().add(ip);
            });
        });
        thread1.start();
        PublicThreads.add(thread1);
        InitConnectionServer initConnectionServer = new InitConnectionServer(callback);
        Thread thread = new Thread(initConnectionServer);
        thread.start();
        PublicThreads.add(thread);
    }

    @FXML
    void sendFile(ActionEvent event) throws IOException {


        if ((type.equals("SERVER") && SERVER == ConnectionIO.WAITING) || (type.equals("CLIENT") && CLIENT == ConnectionIO.WAITING)) {
            if (type.equals("SERVER")) {
                System.out.println("SERVER WAITING");
            } else {
                System.out.println("CLIENT WAITING");
            }
        } else {
            if (type.equals("CLIENT") && CLIENT == ConnectionIO.BREAK) {
                InitConnectionClient initConnectionClient = new InitConnectionClient(callback);
                Thread clientInitThread = new Thread(initConnectionClient);
                clientInitThread.start();
                PublicThreads.add(clientInitThread);
                System.out.println("Starting client thread again");
            } else if (type.equals("SERVER") && SERVER == ConnectionIO.BREAK) {
                InitConnectionServer initConnectionServer = new InitConnectionServer(callback);
                Thread serverInitThread = new Thread(initConnectionServer);
                serverInitThread.start();
                PublicThreads.add(serverInitThread);
                System.out.println("Starting server thread again");
            } else {
                if (FILE_META_RECEIVER == ConnectionIO.INACTIVE) {
                    send.setDisable(true);
                    next();
                    FILE_META_RECEIVER = ConnectionIO.ACTIVE;
                }

            }
        }
    }

    private File getNext() {
        if (arrayList.size() > 0) {
            File file = arrayList.get(0);
            arrayList.remove(0);
            return file;
        }
        return null;
    }

    private boolean makeConnection() {
        return true;
    }

    public void next() {
        File file = getNext();
        if (file == null) {
            send.setDisable(false);
            return;
        }
        System.out.println("prepare meta data");
        System.out.println(file.getName());
        FileMetaData fileMetaData = new FileMetaData(file);
        FileMetaDataSender fileMetaDataSender = new FileMetaDataSender(fileMetaDataCallBack, fileMetaData);
        Thread fileMetaDataSenderThread = new Thread(fileMetaDataSender);
        System.out.println("start fileMetaDataSender thread");
        fileMetaDataSenderThread.start();
        PublicThreads.add(fileMetaDataSenderThread);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Common.transfer = this;
        CommonData.transfer = this;
        listView.setItems(observableList);

        Image image = new Image(new File(Icons.LOADER).toURI().toString());
        String pa = new File(Icons.LOADER).toURI().toString();
        System.out.println("path = " + pa);
        loaderGif.setImage(new Image(pa));
        loaderGif.setImage(image);
        loader.setAlignment(Pos.CENTER);

//        Button btPause = new Button( "Pause");
//        btPause.setOnAction( e -> loaderGif.setVisible(false));
//
//        Button btResume = new Button( "Resume");
//        btResume.setOnAction( e ->loaderGif.setVisible(true));
//        loader.getChildren().addAll( btPause, btResume);

        listView.setCellFactory(new WirelessCellFactory());
        ArrayList<FileMetaData> fileMetaDataArrayList = ListOfFileTransfer.fetch();
        for (FileMetaData fileMetaData : fileMetaDataArrayList) {
            String name = fileMetaData.getName();
            WirelessListEntry wirelessListEntry = new WirelessListEntry(name);
            observableList.add(wirelessListEntry);
        }
        fil_chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Select files", "*.*"));
    }

    public void appendListView(FileMetaData fileMetaData) {

        String name = fileMetaData.getName();
        WirelessListEntry wirelessListEntry = new WirelessListEntry(name);
        for (WirelessListEntry wirelessListEntry1 : observableList) {
            if (wirelessListEntry1.getNameStr().equals(name)) {
                return;
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                observableList.add(wirelessListEntry);
            }
        });
    }

    public void appendListViewFinish(FileMetaData fileMetaData) {
        String name = fileMetaData.getName();
        WirelessListEntry wirelessListEntry = new WirelessListEntry(name);
        wirelessListEntry.getPercent().setText("100 %");
        for (WirelessListEntry wirelessListEntry1 : observableList) {
            if (wirelessListEntry1.getNameStr().equals(name)) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        wirelessListEntry1.getPercent().setText("100 %");
                    }
                });
                return;
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                observableList.add(wirelessListEntry);
            }
        });
    }

    public void closeWindow() {
        Platform.runLater(() -> CommonData.transferStage.close());
    }

    public void fetchAll() {
        observableList.clear();
        ArrayList<FileMetaData> fileMetaDataArrayList = ListOfFileTransfer.fetch();
        for (FileMetaData fileMetaData : fileMetaDataArrayList) {
            observableList.add(new WirelessListEntry(fileMetaData.getName()));
        }
    }

    public void showStage() {
        Stage stage = (Stage) (listView.getScene()).getWindow();
        stage.setMaximized(true);
        stage.requestFocus();
        stage.setIconified(false);
        stage.isAlwaysOnTop();
    }

    public void fetchNewlyAdded() {
        ArrayList<FileMetaData> fileMetaDataArrayList = ListOfFileTransfer.fetch(observableList.size());
        for (FileMetaData fileMetaData : fileMetaDataArrayList) {
            WirelessListEntry wirelessListEntry = new WirelessListEntry(fileMetaData.getName());
//            wirelessListEntry.getName().setStyle("-fx-background-color: green");
            observableList.add(wirelessListEntry);
        }
        Platform.runLater(() -> {
            listView.setVisible(false);
            listView.setVisible(true);
        });
    }
}
