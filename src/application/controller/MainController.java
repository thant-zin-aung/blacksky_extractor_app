package application.controller;

import application.Main;
import application.model.AnimationStyle;
import application.model.FileManipulator;
import application.model.LoginUserManipulator;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class MainController {

    private final int MAX_ITEM_PER_ROW = 3;
    private final int PROGRESS_MAX_VALUE = 20000;

    private String DEST_FOLDER = null;
    public static String FILES_FOLDER = null;
    private ObservableList<FileManipulator> availableFiles;
    private ObservableList<FileManipulator> selectedFiles;
    private VBox mainVBox = null;

    @FXML ImageView exitButton;


    // Extract field components
    @FXML private ScrollPane scrollPane;
    @FXML private JFXButton extractButton;
    @FXML private JFXButton choosePathButton;
    @FXML private JFXTextField destFolderPath;
    @FXML private Label selectionLabel;
    @FXML private JFXComboBox<String> sortBox;
    @FXML private VBox leftSideComponents1,leftSideComponents2,leftSideComponents3,leftSideComponents4,leftSideComponents5;

    // Compress Field Components
    @FXML private AnchorPane compressPane;
    @FXML private ImageView browseButton;
    @FXML private JFXPasswordField compressSzPassword,compressRarPassword;
    @FXML private JFXTextField browseField;
    @FXML private JFXButton compressButton;
    @FXML private Label compressResultLabel;

    // Add User Field Components
    @FXML private AnchorPane adduserPane;
    @FXML private JFXTextField adduserUsername;
    @FXML private JFXPasswordField adduserPassword1;
    @FXML private JFXPasswordField adduserPassword2;
    @FXML private Label adduserResultStatus;

    // Remove User Field Components
    @FXML private AnchorPane removeuserPane;
    @FXML private JFXTextField removeuserUsername;
    @FXML private JFXPasswordField removeuserPassword;
    @FXML private Label removeuserResultStatus;

    // Change User Info Field Components
    @FXML private AnchorPane changeuserinfoPane;
    @FXML private HBox box1;
    @FXML private HBox box2;
    @FXML private JFXTextField userinfoOldUsername;
    @FXML private JFXTextField userinfoNewUsername;
    @FXML private JFXPasswordField userinfoOldPassword;
    @FXML private JFXPasswordField userinfoNewPassword;
    @FXML private Label userinfoResultStatus;
    @FXML private JFXButton changeinfoButton;
    private boolean isBox1;
    private boolean isBox2;

    ObservableList<VBox> leftSideComponents;
    ObservableMap<VBox,Node> leftAndRightSideMap;
    ObservableList<String> sortBoxItems;

    public void initialize() {
        exitButton.setOnMouseClicked( e -> System.exit(0) );
        hideItems(true,scrollPane,compressResultLabel);
        sortBoxItems = FXCollections.observableArrayList("Sort by name","Sort by size","Sort by date");
        leftSideComponents = FXCollections.observableArrayList(leftSideComponents1,leftSideComponents2,leftSideComponents3,
                leftSideComponents4,leftSideComponents5);
        leftAndRightSideMap = FXCollections.observableHashMap();
        leftAndRightSideMap.put(leftSideComponents1,scrollPane); leftAndRightSideMap.put(leftSideComponents2,compressPane);
        leftAndRightSideMap.put(leftSideComponents3,adduserPane); leftAndRightSideMap.put(leftSideComponents4,removeuserPane);
        leftAndRightSideMap.put(leftSideComponents5,changeuserinfoPane);
        sortBox.setItems(sortBoxItems);
        sortBox.getSelectionModel().selectFirst();
        selectedFiles = FXCollections.observableArrayList();
        availableFiles = FileManipulator.scanAndGetAllAvailableFileSortByName(new File(FILES_FOLDER));
        mainVBox = new VBox();
        mainVBox.setSpacing(20);
        mainVBox.setStyle("-fx-background-color: white;");
        mainVBox.setPadding(new Insets(20,0,20,0));
        mainVBox.getChildren().addAll(createHBoxes());
        scrollPane.setContent(mainVBox);
        hideItems(false,extractButton,sortBox,selectionLabel,choosePathButton,destFolderPath,scrollPane);
        leftSideComponents1.getStyleClass().add("blue-dark-background");



    }

    private VBox getReadyMadeVBox(FileManipulator currentFile,Node ... items) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        vBox.setStyle("-fx-background-color: white;" +
                "-fx-border-width: 1px;" +
                "-fx-background-radius: 30 30 30 30;" +
                "-fx-border-radius: 30 30 30 30;");
        vBox.setPrefWidth(340);
        vBox.setPrefHeight(200);
        vBox.setOnMouseEntered( e -> AnimationStyle.getInstance().playScaleEffect(vBox,200,1,false,
                1,1,1.05,1.05));
        vBox.setOnMouseExited( e -> AnimationStyle.getInstance().playScaleEffect(vBox,200,1,false,
                1.05,1.05,1,1));
        vBox.setOnMouseClicked( e -> {
            if ( !currentFile.getClicked() ) {
                vBox.setStyle("-fx-border-color: linear-gradient(to bottom left,#FF00F7,#006BFF);" +
                "-fx-border-radius: 30; -fx-border-width: 2;");
                currentFile.setClicked(true);
                selectedFiles.add(currentFile);
                if ( selectedFiles.size() != 0 ) {
                    selectionLabel.setVisible(true);
                    selectionLabel.setText("Selected: "+(selectedFiles.size())+" files");
                }
                else selectionLabel.setVisible(false);
            } else {
                vBox.setStyle("-fx-background-color: white;" +
                        "-fx-border-width: 1px;" +
                        "-fx-background-radius: 30 30 30 30;" +
                        "-fx-border-radius: 30 30 30 30;");
                currentFile.setClicked(false);
                selectedFiles.remove(currentFile);
                if ( selectedFiles.size() != 0 ) {
                    selectionLabel.setVisible(true);
                    selectionLabel.setText("Selected: "+(selectedFiles.size())+" files");
                }
                else selectionLabel.setVisible(false);
            }

            if ( selectedFiles.size() != 0 ) {
                if ( !destFolderPath.getText().equals("") ) {
                    disableItems(false,extractButton);
                }
            } else {
                disableItems(true,extractButton);
            }
        });
        vBox.getChildren().addAll(items);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setSpread(0.1);
        dropShadow.setColor(Color.rgb(222,222,222));
        vBox.setEffect(dropShadow);
        return vBox;
    }

    private ObservableList<HBox> createHBoxes() {
        ObservableList<HBox> hBoxes = FXCollections.observableArrayList();
        HBox hBox = new HBox();
        for ( int fileCount=0 ; fileCount<availableFiles.size() ; fileCount++ ) {
            hBox.setSpacing(20);
            hBox.setAlignment(Pos.CENTER);
            Label fileName = new Label(availableFiles.get(fileCount).getFileName());
            Label fileSizeInString = new Label(availableFiles.get(fileCount).getFileSizeInString());
            Label fileCreationDate = new Label(availableFiles.get(fileCount).getFileCreationDate());
            fileName.setStyle("-fx-font-weight: bold; -fx-font-size: 18");
            fileSizeInString.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
            fileCreationDate.setWrapText(true);
            fileCreationDate.setStyle("-fx-text-alignment: right;");
            JFXProgressBar progressBar = new JFXProgressBar();
            progressBar.setProgress(availableFiles.get(fileCount).getFileSize()/PROGRESS_MAX_VALUE);
            // Progress color
            progressBar.getStyleClass().add("blacksky-progress-bar");
            if ( availableFiles.get(fileCount).getFileSize() > 14000 ) progressBar.getStyleClass().add("progress-bar-red");
            else if ( availableFiles.get(fileCount).getFileSize() > 6000 ) progressBar.getStyleClass().add("progress-bar-pink");
            else progressBar.getStyleClass().add("progress-bar-blue");
            // Progress color
            ImageView storageIcon = new ImageView(new Image(getClass().getResourceAsStream(
                    "/application/assests/photos/storage.png"
            )));
            storageIcon.setFitWidth(40);
            storageIcon.setFitHeight(40);
            HBox firstChild = new HBox();
            firstChild.setSpacing(30);
            firstChild.setAlignment(Pos.CENTER);
            firstChild.setPrefHeight(100);

            VBox firstVChild = new VBox();
            firstVChild.setSpacing(10);
            firstVChild.setPrefWidth(200);
            firstVChild.setAlignment(Pos.CENTER_LEFT);
            VBox secondVChild = new VBox();
            secondVChild.setPrefWidth(60);
            secondVChild.setPrefHeight(70);
            secondVChild.setSpacing(20);
            secondVChild.setAlignment(Pos.CENTER);

            firstVChild.getChildren().addAll(fileName,fileSizeInString);
            secondVChild.getChildren().addAll(fileCreationDate);
            firstChild.getChildren().addAll(firstVChild,secondVChild);


            HBox secondChild = new HBox();
            secondChild.setSpacing(30);
            secondChild.setAlignment(Pos.CENTER);
            secondChild.getChildren().addAll(storageIcon,progressBar);
            hBox.getChildren().add(getReadyMadeVBox(availableFiles.get(fileCount).getInstance(),firstChild,secondChild));
            if ( !hBoxes.contains(hBox) ) hBoxes.add(hBox);
            if ( hBox.getChildren().size() > MAX_ITEM_PER_ROW-1 ) {
                hBox = new HBox();
            }
        }
        return hBoxes;
    }

    private void hideItems(boolean flag,Node ... items) {
        for ( Node item:items ) {
            item.setVisible(!flag);
        }
    }

    private void disableItems(boolean flag,Node ... items) {
        for (Node item:items ) {
            item.setDisable(flag);
        }
    }



// Styling imageviews....

    @FXML
    public void mouseEnterToLeftSideComponent(MouseEvent e) {
        if ( e.getSource() instanceof VBox ) {
            VBox vBox = (VBox) e.getSource();
            ImageView imageView = (ImageView) vBox.getChildren().get(0);
            AnimationStyle.getInstance().playScaleEffect(imageView,500,1,false,
                    1,1,1.2,1.2);
        } else {
            ImageView imageView = (ImageView) e.getSource();
            AnimationStyle.getInstance().playScaleEffect(imageView,500,1,false,
                    1,1,1.2,1.2);
        }
    }
    @FXML
    public void mouseExitToLeftSideComponent(MouseEvent e) {
        if ( e.getSource() instanceof VBox ) {
            VBox vBox = (VBox) e.getSource();
            ImageView imageView = (ImageView) vBox.getChildren().get(0);
            AnimationStyle.getInstance().playScaleEffect(imageView,500,1,false,
                    1.2,1.2,1,1);
        } else {
            ImageView imageView = (ImageView) e.getSource();
            AnimationStyle.getInstance().playScaleEffect(imageView,500,1,false,
                    1.2,1.2,1,1);
        }
    }
    @FXML
    public void clickOnLeftSideComponent(MouseEvent e) {
        VBox currentComponent = (VBox) e.getSource();
        if ( currentComponent.getStyleClass().size() > 1 ) {
            currentComponent.getStyleClass().remove(1);
            leftAndRightSideMap.get(currentComponent).setVisible(false);
        } else {
            currentComponent.getStyleClass().add("blue-dark-background");
            for ( VBox vBox:leftSideComponents ) {
                if ( vBox == currentComponent ) {
                    leftAndRightSideMap.get(vBox).setVisible(true);
                    if ( leftAndRightSideMap.get(vBox) instanceof ScrollPane ) {
                        hideItems(false,extractButton,sortBox,selectionLabel,choosePathButton,destFolderPath);
                    } else hideItems(true,extractButton,sortBox,selectionLabel,choosePathButton,destFolderPath);
                    continue;
                }
                if ( vBox.getStyleClass().size() > 1 ) {
                    vBox.getStyleClass().remove(1);
                }
                leftAndRightSideMap.get(vBox).setVisible(false);
            }
        }
    }

// Manipulating buttons...

    @FXML
    public void clickOnButtons(MouseEvent event) {
        if ( event.getSource() == extractButton && event.getButton()== MouseButton.PRIMARY && selectedFiles.size() != 0) {
//            scrollPane.setOpacity(0.5);
            AnimationStyle.getInstance().playFadeEffect(scrollPane,700,1,false,1,0.5);
            extractButton.setDisable(true);
            sortBox.setDisable(true);

    // Creating password box
            Stage stage = new Stage();
            VBox passwordBox = new VBox();
            passwordBox.setSpacing(30);
            passwordBox.setStyle(
                    "-fx-pref-width: 500;" +
                            "-fx-pref-height: 230;" +
                            "-fx-alignment: center;" +
                            "-fx-background-color: white;" +
                            "-fx-border-color: linear-gradient(to bottom left,#006BFF,#FF00F7);" +
                            "-fx-border-width: 3px;" +
                            "-fx-border-style: solid;" +
                            "-fx-border-radius: 20px;" +
                            "-fx-background-radius: 20px;" +
                            "-fx-padding: 30;"
            );
            Label titleLabel = new Label("Please enter the password");
            titleLabel.setStyle("-fx-font-weight: bolder; -fx-font-size: 21;");
            HBox szHBox = new HBox();
            szHBox.setSpacing(30);
            szHBox.setAlignment(Pos.CENTER);
            HBox rarHBox = new HBox();
            rarHBox.setSpacing(30);
            rarHBox.setAlignment(Pos.CENTER);
            HBox buttonHBox = new HBox();
            buttonHBox.setSpacing(20);
            buttonHBox.setAlignment(Pos.CENTER_RIGHT);
            Label szLabel = new Label("7z password");
            szLabel.setStyle("-fx-font-weight: bold");
            Label rarLabel = new Label("Rar password");
            rarLabel.setStyle("-fx-font-weight: bold");
            JFXPasswordField szPassField = new JFXPasswordField();
            szPassField.setPrefWidth(300);
            JFXPasswordField rarPassField = new JFXPasswordField();
            rarPassField.setPrefWidth(300);
            JFXButton okButton = new JFXButton("OK");
            okButton.setStyle("-fx-font-weight: bold; -fx-pref-width: 100; -fx-pref-height: 55;" +
                    "-fx-border-color: linear-gradient(to bottom left,#006BFF,#FF00F7); -fx-border-width: 2px;" +
                    "-fx-border-radius: 20;");
            JFXButton cancelButton = new JFXButton("CANCEL");
            cancelButton.setStyle("-fx-font-weight: bold; -fx-pref-width: 100; -fx-pref-height: 55;" +
                    "-fx-border-color: linear-gradient(to bottom left,#006BFF,#FF00F7); -fx-border-width: 2px;" +
                    "-fx-border-radius: 20px");
            Label wrongPasswordLabel = new Label("Wrong password");
            Pane gap = new Pane();
            buttonHBox.setHgrow(gap,Priority.ALWAYS);
            wrongPasswordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red; -fx-font-size: 13");
            szHBox.getChildren().addAll(szLabel,szPassField);
            rarHBox.getChildren().addAll(rarLabel,rarPassField);
            buttonHBox.getChildren().addAll(wrongPasswordLabel,gap,okButton,cancelButton);
            passwordBox.getChildren().addAll(titleLabel,szHBox,rarHBox,buttonHBox);

            Scene scene = new Scene(passwordBox);

            stage.initModality(Modality.APPLICATION_MODAL);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);

            wrongPasswordLabel.setVisible(false);
            szPassField.setOnKeyPressed(e->wrongPasswordLabel.setVisible(false));
            rarPassField.setOnKeyPressed(e->wrongPasswordLabel.setVisible(false));
            okButton.setOnAction( e -> {
                new Thread(() -> {
                    FileManipulator.checkPassword(szPassField.getText(),rarPassField.getText(),FILES_FOLDER);
                    Platform.runLater( () -> {
                        wrongPasswordLabel.setText("Checking password...");
                        wrongPasswordLabel.setTextFill(Color.BLACK);
                        wrongPasswordLabel.setVisible(true);
                    });
                    while (FileManipulator.isPasswordCorrect==null) {
                        try {
                            Thread.sleep(200);
                        } catch ( Exception exception ) {
                            exception.printStackTrace();
                        }
                    }
                    if ( FileManipulator.isPasswordCorrect ) {
                        wrongPasswordLabel.setVisible(false);
                        Platform.runLater(() -> stage.close());
                        FileManipulator.extractSelectedFiles(selectedFiles,scrollPane,FILES_FOLDER,DEST_FOLDER,
                                rarPassField.getText(),szPassField.getText());
                    } else {
                        wrongPasswordLabel.setVisible(true);
                        wrongPasswordLabel.setTextFill(Color.RED);
                        wrongPasswordLabel.setStyle("-fx-font-weight: bold;");
                        Platform.runLater(()->{
                            wrongPasswordLabel.setText("Wrong Password");
                        });
                    }
                }).start();
            });
            cancelButton.setOnAction( e -> {
                extractButton.setDisable(false);
                sortBox.setDisable(false);
                stage.close();
            });
            AnimationStyle.getInstance().playScaleEffect(passwordBox,700,1,false,
                    0.1,0.5,1,1);
            stage.showAndWait();

            System.out.println("showed");
    // Creating password box

//            scrollPane.setOpacity(1);
            AnimationStyle.getInstance().playFadeEffect(scrollPane,700,1,false,0.5,1);

        }

        else if ( event.getSource() == choosePathButton ) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDestFolder = directoryChooser.showDialog(null);
            if ( selectedDestFolder != null ) {
                DEST_FOLDER = selectedDestFolder.getAbsolutePath();
                destFolderPath.setText(DEST_FOLDER);
                if ( selectedFiles.size() != 0 ) disableItems(false,extractButton);
            }
        }



    }

    @FXML
    public void clickOnSortBox() {
        selectionLabel.setVisible(false);
        selectedFiles = FXCollections.observableArrayList();
        if ( sortBox.getSelectionModel().getSelectedItem().equals(sortBoxItems.get(0))) {
            availableFiles = FileManipulator.scanAndGetAllAvailableFileSortByName(new File(FILES_FOLDER));
            mainVBox = new VBox();
            mainVBox.getChildren().addAll(createHBoxes());
            scrollPane.setContent(mainVBox);
        } else if ( sortBox.getSelectionModel().getSelectedItem().equals(sortBoxItems.get(1))) {
            availableFiles = FileManipulator.scanAndGetAllAvailableFileSortBySize(new File(FILES_FOLDER));
            mainVBox = new VBox();
            mainVBox.getChildren().addAll(createHBoxes());
            scrollPane.setContent(mainVBox);
        } if ( sortBox.getSelectionModel().getSelectedItem().equals(sortBoxItems.get(2))) {
            availableFiles = FileManipulator.scanAndGetAllAvailableFileSortByDate(new File(FILES_FOLDER));
            mainVBox = new VBox();
            mainVBox.getChildren().addAll(createHBoxes());
            scrollPane.setContent(mainVBox);
        }

        mainVBox.setSpacing(20);
        mainVBox.setStyle("-fx-background-color: white;");
        mainVBox.setPadding(new Insets(20,0,20,0));
        for ( int id=0 ; id<mainVBox.getChildren().size() ; id++ ) {
            HBox currentHBox = (HBox) mainVBox.getChildren().get(id);
            for ( int innerId = 0 ; innerId < currentHBox.getChildren().size() ; innerId++ ) {
                VBox vBox = (VBox)currentHBox.getChildren().get(innerId);
                AnimationStyle.getInstance().playScaleEffect(
                        vBox,300,2,true,1,1,1.05,1.05
                );
            }
        }

    }


    // Compress Field...

    @FXML
    public void mouseEnterToBrowseIcon() {
        AnimationStyle.getInstance().playScaleEffect(browseButton,100,1,false,
                1,1,1.1,1.1);
    }
    @FXML
    public void mouseExitToBrowseIcon() {
        AnimationStyle.getInstance().playScaleEffect(browseButton,100,1,false,
                1.1,1.1,1,1);
    }
    @FXML
    public void clickOnBrowseIcon() {
        DirectoryChooser folderChooser = new DirectoryChooser();
        File selectedFolder = folderChooser.showDialog(null);
        if ( selectedFolder != null ) browseField.setText(selectedFolder.getAbsolutePath());
    }
    @FXML
    public void keyPressOnCompressPasswordFields() {
        compressResultLabel.setVisible(false);
    }
    @FXML
    public void clickOnCompressButton() {
        Stage compressStage = new Stage();
        HBox mainHBox = new HBox();
        mainHBox.setSpacing(60);
        mainHBox.setAlignment(Pos.CENTER);
        mainHBox.setPadding(new Insets(20));

        Label compressLabel = new Label("Compressing");
        compressLabel.setStyle("-fx-font-weight: bold;");
        ImageView successIcon = new ImageView(new Image(new Main().getClass().getResourceAsStream(
                "/application/assests/photos/extractionSuccessIcon.png"
        )));
        successIcon.setFitWidth(40);
        successIcon.setFitHeight(40);
        ImageView failIcon = new ImageView(new Image(new Main().getClass().getResourceAsStream(
                "/application/assests/photos/extractionFailIcon.png"
        )));
        failIcon.setFitWidth(40);
        failIcon.setFitHeight(40);
        JFXSpinner waitingProgress = new JFXSpinner();
        waitingProgress.setPrefWidth(40);
        waitingProgress.setPrefHeight(40);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(failIcon,successIcon,waitingProgress);

        JFXButton okButton = new JFXButton("OK");
        okButton.setStyle(
                "-fx-font-weight: bold; -fx-border-width: 1; -fx-border-style: solid; " +
                        "-fx-border-color: linear-gradient(to bottom left,#006BFF,#FF00F7); -fx-border-radius: 20px;" +
                        "-fx-pref-width: 100px; -fx-pref-height: 40;"
        );
        okButton.setFocusTraversable(false);

        mainHBox.getChildren().addAll(compressLabel,stackPane,okButton);
        mainHBox.setStyle("-fx-border-radius: 20px; -fx-border-color: linear-gradient(to bottom left,#006BFF,#FF00F7);" +
                "-fx-border-width: 2px; -fx-pref-width: 500px; -fx-pref-height: 50px;" +
                "-fx-background-color: white;");
        Scene scene = new Scene(mainHBox);
        scene.setFill(Color.TRANSPARENT);
        compressStage.initStyle(StageStyle.UNDECORATED);
        compressStage.initModality(Modality.APPLICATION_MODAL);
        compressStage.setScene(scene);

        okButton.setDisable(true);
        successIcon.setVisible(false);
        failIcon.setVisible(false);
        waitingProgress.setVisible(true);

        okButton.setOnAction( e -> {
            compressStage.close();
            compressPane.setOpacity(1);
            browseField.clear();
        } );

        Thread compressThread = new Thread ( () -> {
            FileManipulator.checkPassword(compressSzPassword.getText(),compressRarPassword.getText(),FILES_FOLDER);
            while(FileManipulator.isPasswordCorrect==null) {
                compressResultLabel.setVisible(true);
                compressResultLabel.setTextFill(Color.BLACK);
                try {
                    Thread.sleep(300);
                    Platform.runLater( () -> compressResultLabel.setText("Checking password."));
                    Thread.sleep(300);
                    Platform.runLater( () -> compressResultLabel.setText("Checking password.."));
                    Thread.sleep(300);
                    Platform.runLater( () -> compressResultLabel.setText("Checking password..."));
                    Thread.sleep(300);
                    Platform.runLater( () -> compressResultLabel.setText("Checking password...."));
                    Thread.sleep(300);
                    Platform.runLater( () -> compressResultLabel.setText("Checking password....."));
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
            compressResultLabel.setVisible(false);
            compressResultLabel.setTextFill(Color.RED);
            if ( FileManipulator.isPasswordCorrect ) {
                compressPane.setOpacity(0.2);
                Platform.runLater(() -> compressStage.showAndWait());
                new Thread(() -> {
                    while ( FileManipulator.isArchiveInSuccess == null ) {
                        try {
                            Thread.sleep(100);
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }
                    }
                    waitingProgress.setVisible(false);
                    okButton.setDisable(false);
                    if ( FileManipulator.isArchiveInSuccess ) {
                        successIcon.setVisible(true);
                    } else {
                        failIcon.setVisible(true);
                    }

                }).start();
                FileManipulator.compressFile(compressSzPassword.getText(),compressRarPassword.getText(),browseField.getText(),
                        FILES_FOLDER,availableFiles.size()+2);

            } else {
                Platform.runLater(() -> compressResultLabel.setText("Wrong password"));
                compressResultLabel.setVisible(true);
            }
        });

        if ( browseField.getText().trim().equals("") ) {
            compressResultLabel.setVisible(true);
            compressResultLabel.setText("Browse folder can't be empty");
        } else compressThread.start();


    }


    // Add User Field
    @FXML
    public void keyPressOnAddUserFields() {
        adduserResultStatus.setTextFill(Color.RED);
        hideItems(true,adduserResultStatus);
    }
    @FXML
    public void clickOnAddUserButton() {
        String username = adduserUsername.getText();
        String password1 = adduserPassword1.getText();
        String password2 = adduserPassword2.getText();

        if ( username.contains(" ") || password1.contains(" ") || password2.contains(" ") ) {
            hideItems(false,adduserResultStatus);
            adduserResultStatus.setText("Fields are not allowed to contain white spaces");
            return;
        }
        if ( !password1.equals(password2) ) {
            hideItems(false,adduserResultStatus);
            adduserResultStatus.setText("Password do not match");
            return;
        }
        if (!LoginUserManipulator.addUser(username,password1)) {
            hideItems(false,adduserResultStatus);
            adduserResultStatus.setText("\""+username+"\" is already exists");
            return;
        } else {
            hideItems(false,adduserResultStatus);
            adduserResultStatus.setTextFill(Color.GREEN);
            adduserResultStatus.setText("User account created successfully.");
            adduserUsername.clear();
            adduserPassword1.clear();
            adduserPassword2.clear();
        }
    }

    // Remove User Field
    @FXML
    public void keyPressOnRemoveUserFields() {
        removeuserResultStatus.setTextFill(Color.RED);
        hideItems(true,removeuserResultStatus);
    }
    @FXML
    public void clickOnRemoveUserButton() {
        if ( LoginUserManipulator.removeUser(removeuserUsername.getText(),removeuserPassword.getText()) ) {
            hideItems(false,removeuserResultStatus);
            removeuserResultStatus.setTextFill(Color.GREEN);
            removeuserResultStatus.setText("User removed from database successfully.");
            removeuserUsername.clear();
            removeuserPassword.clear();
        } else {
            hideItems(false,removeuserResultStatus);
            removeuserResultStatus.setText("Failed to remove user");
        }
    }

    // Change User Info Field
    @FXML
    public void mouseEnterOnBoxes(MouseEvent event) {
        if ( event.getSource() == box1 ) {
            AnimationStyle.getInstance().playScaleEffect(box1,100,1,false,1,1,1.08,1.08);
        } else {
            AnimationStyle.getInstance().playScaleEffect(box2,100,1,false,1,1,1.08,1.08);
        }
    }
    @FXML
    public void mouseExitOnBoxes(MouseEvent event) {
        if ( event.getSource() == box1 ) {
            AnimationStyle.getInstance().playScaleEffect(box1,100,1,false,1.08,1.08,1,1);
        } else {
            AnimationStyle.getInstance().playScaleEffect(box2,100,1,false,1.08,1.08,1,1);
        }

    }
    @FXML
    public void clickOnBoxes(MouseEvent event) {
        if ( event.getSource()==box1 ) {
            isBox1=true;
            isBox2=false;
            disableItems(true,userinfoNewPassword);
            disableItems(false,userinfoOldUsername,userinfoNewUsername,userinfoOldPassword,changeinfoButton);
            clearTextFromFields(userinfoOldUsername,userinfoNewUsername,userinfoOldPassword,changeinfoButton);
        } else {
            isBox2=true;
            isBox1=false;
            disableItems(true,userinfoNewUsername);
            disableItems(false,userinfoOldUsername,userinfoOldPassword,userinfoNewPassword,changeinfoButton);
            clearTextFromFields(userinfoOldUsername,userinfoNewUsername,userinfoOldPassword,changeinfoButton);
        }
    }
    @FXML
    public void keyPressOnUserInfoFields() {
        userinfoResultStatus.setTextFill(Color.YELLOW);
        hideItems(true,userinfoResultStatus);
    }
    @FXML
    public void clickOnChangeInfoButton() {
        String oldUser = userinfoOldUsername.getText();
        String newUser = userinfoNewUsername.getText();
        String oldPass = userinfoOldPassword.getText();
        String newPass = userinfoNewPassword.getText();
        if ( isBox1 ) {
            if ( !LoginUserManipulator.isUserExist(oldUser) ) {
                hideItems(false,userinfoResultStatus);
                userinfoResultStatus.setText("\""+oldUser+"\" not found in our database.");
                return;
            }
            if ( LoginUserManipulator.changeUsername(oldUser,newUser,oldPass) ) {
                hideItems(false,userinfoResultStatus);
                userinfoResultStatus.setTextFill(Color.WHITE);
                userinfoResultStatus.setText("User info changed successfully.");
                clearTextFromFields(userinfoOldUsername,userinfoNewUsername,userinfoOldPassword,userinfoNewPassword);
            } else {
                hideItems(false,userinfoResultStatus);
                userinfoResultStatus.setText("Failed to change user info.");
            }
        } else {
            if ( !LoginUserManipulator.isUserExist(oldUser) ) {
                hideItems(false,userinfoResultStatus);
                userinfoResultStatus.setText("\""+oldUser+"\" not found in our database.");
                return;
            }
            if ( LoginUserManipulator.changePassword(oldUser,oldPass,newPass)) {
                hideItems(false,userinfoResultStatus);
                userinfoResultStatus.setTextFill(Color.WHITE);
                userinfoResultStatus.setText("User info changed successfully.");
                clearTextFromFields(userinfoOldUsername,userinfoNewUsername,userinfoOldPassword,userinfoNewPassword);
            } else {
                hideItems(false,userinfoResultStatus);
                userinfoResultStatus.setText("Failed to change user info.");
            }
        }
    }
    private void clearTextFromFields(Node ... items) {
        for ( Node item:items ) {
            if ( item instanceof JFXTextField) {
                JFXTextField text=(JFXTextField) item;
                text.clear();
            } else if ( item instanceof JFXPasswordField ) {
                JFXPasswordField passwordField = (JFXPasswordField) item;
                passwordField.clear();
            }
        }
    }
}
