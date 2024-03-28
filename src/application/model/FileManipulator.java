package application.model;

import application.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileManipulator {
    private final static int MAX_MB_SIZE=900;
    private final static String TEST_PASS_FILE_NAME="testpass";
    private final String fileName;
    private final double fileSize;
    private final String fileSizeInString;
    private final int fileIndex;
    private final String fileCreationDate;
    private boolean clicked;
    private static boolean isExtractedInSuccess=true;
    public static Boolean isPasswordCorrect=null;
    public static Boolean isArchiveInSuccess=null;

    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    public FileManipulator(String fileName, double fileSize, String fileSizeInString,int fileIndex,String fileCreationDate) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileIndex = fileIndex;
        this.fileSizeInString = fileSizeInString;
        this.fileCreationDate = fileCreationDate;
        this.clicked = false;
    }

    public String getFileName() {
        return this.fileName;
    }

    public double getFileSize() {
        return this.fileSize;
    }

    public int getFileIndex() {
        return this.fileIndex;
    }

    public FileManipulator getInstance() {
        return this;
    }

    public String getFileSizeInString() {
        return fileSizeInString;
    }

    public boolean getClicked() { return clicked; }

    public String getFileCreationDate() {
        return fileCreationDate;
    }

    public void setClicked(boolean isClicked) { this.clicked = isClicked; }


    public static ObservableList<FileManipulator> scanAndGetAllAvailableFileSortByName(File destinationFolder) {
        ObservableList<FileManipulator> availableFiles = FXCollections.observableArrayList();
        if ( destinationFolder.isDirectory() ) {
            File[] files = destinationFolder.listFiles();
            for ( File file:files ) {
                try {
                    if ( file.isFile() ) {
                        String fileName = file.getName();
                        Path currentFile = FileSystems.getDefault().getPath(destinationFolder.getAbsolutePath()+"\\"+fileName);
                        BasicFileAttributes currentFileAttr = Files.readAttributes(currentFile,BasicFileAttributes.class);
                        String fileCreationTime = currentFileAttr.creationTime().toString();
                        fileName = fileName.substring(0,fileName.indexOf("."));
                        int tempFileIndex=Integer.parseInt(fileName.substring(9,fileName.length()));
                        double tempFileSize = memoryConverter(file.length());
                        String tempFileSizeInString = memoryConverterInString(tempFileSize);
                        availableFiles.add(new FileManipulator(fileName,tempFileSize,tempFileSizeInString,tempFileIndex,fileCreationTime));
                    }
                } catch ( Exception e ) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.out.println(destinationFolder+" is not a directory");
        }
        availableFiles.sort(new Comparator<FileManipulator>() {
            @Override
            public int compare(FileManipulator o1, FileManipulator o2) {
                return o1.getFileIndex()-o2.getFileIndex();
            }
        });
        return availableFiles;
    }

    public static ObservableList<FileManipulator> scanAndGetAllAvailableFileSortBySize(File destinationFolder) {
        ObservableList<FileManipulator> availableFiles = FXCollections.observableArrayList();
        if ( destinationFolder.isDirectory() ) {
            File[] files = destinationFolder.listFiles();
            for ( File file:files ) {
                try {
                    if ( file.isFile() ) {
                        String fileName = file.getName();
                        Path currentFile = FileSystems.getDefault().getPath(destinationFolder.getAbsolutePath()+"\\"+fileName);
                        BasicFileAttributes currentFileAttr = Files.readAttributes(currentFile,BasicFileAttributes.class);
                        String fileCreationTime = currentFileAttr.creationTime().toString();
                        fileName = fileName.substring(0,fileName.indexOf("."));
                        int tempFileIndex=Integer.parseInt(fileName.substring(9,fileName.length()));
                        double tempFileSize = memoryConverter(file.length());
                        String tempFileSizeInString = memoryConverterInString(tempFileSize);
                        availableFiles.add(new FileManipulator(fileName,tempFileSize,tempFileSizeInString,tempFileIndex,fileCreationTime));
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println(destinationFolder+" is not a directory");
        }
        availableFiles.sort(new Comparator<FileManipulator>() {
            @Override
            public int compare(FileManipulator o1, FileManipulator o2) {
                int o1Value = (int)Math.round(o1.getFileSize());
                int o2Value = (int)Math.round(o2.getFileSize());
                return o2Value-o1Value;
            }
        });
        return availableFiles;
    }

    public static ObservableList<FileManipulator> scanAndGetAllAvailableFileSortByDate(File destinationFolder) {
        ObservableList<FileManipulator> availableFiles = FXCollections.observableArrayList();
        if ( destinationFolder.isDirectory() ) {
            File[] files = destinationFolder.listFiles();
            for ( File file:files ) {
                try {
                    if ( file.isFile() ) {
                        String fileName = file.getName();
                        Path currentFile = FileSystems.getDefault().getPath(destinationFolder.getAbsolutePath()+"\\"+fileName);
                        BasicFileAttributes currentFileAttr = Files.readAttributes(currentFile,BasicFileAttributes.class);
                        String fileCreationTime = currentFileAttr.creationTime().toString();
                        fileName = fileName.substring(0,fileName.indexOf("."));
                        int tempFileIndex=Integer.parseInt(fileName.substring(9,fileName.length()));
                        double tempFileSize = memoryConverter(file.length());
                        String tempFileSizeInString = memoryConverterInString(tempFileSize);
                        availableFiles.add(new FileManipulator(fileName,tempFileSize,tempFileSizeInString,tempFileIndex,fileCreationTime));
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println(destinationFolder+" is not a directory");
        }
        availableFiles.sort( (o1,o2) -> o1.getFileCreationDate().compareTo(o2.getFileCreationDate()));
        return availableFiles;
    }

    private static double memoryConverter(double value) {
        double finalMemory;
        double toMB = Math.round(value/10240);
        finalMemory=toMB/100;
        return finalMemory;
    }

    public static String memoryConverterInString(double value) {
        String finalMemory;
        if ( value > MAX_MB_SIZE ) {
            value = Math.round((value/1024)*100);
            value /= 100;
            return value+" GB";
        }
        return value+" MB";

    }

    public static void checkPassword(String szPassword,String rarPassword,String filesFolderName) {
        isPasswordCorrect=null;
        String rarCommand="rar x -y -p"+rarPassword+" \""+filesFolderName+"\\"+TEST_PASS_FILE_NAME+".rar\" \""+filesFolderName+"\"";
        String szCommand="7z x -y -p"+szPassword+" \""+filesFolderName+"\\"+TEST_PASS_FILE_NAME+".7z\" -o\""+filesFolderName+"\"";
        Thread szThread = new Thread( () -> {
            try {
                Process process = Runtime.getRuntime().exec(szCommand);
                if ( process.waitFor()!=0 ) isPasswordCorrect=false;
                else isPasswordCorrect=true;
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        });
        Thread rarThread = new Thread( () -> {
            try {
                Process process = Runtime.getRuntime().exec(rarCommand);
                if ( process.waitFor()!=0 ) isPasswordCorrect=false;
                else isPasswordCorrect=true;
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            File rarFile = new File(filesFolderName+"\\"+TEST_PASS_FILE_NAME+".rar");
            File testPassFile = new File(filesFolderName+"\\"+TEST_PASS_FILE_NAME+".txt");
            if ( rarFile.exists() ) rarFile.delete();
            if ( testPassFile.exists() ) testPassFile.delete();
        });

        new Thread( () -> {
            try {
                szThread.start();
                szThread.join();
                rarThread.start();
                rarThread.join();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void extractSelectedFiles(ObservableList<FileManipulator> selectedFiles, ScrollPane scrollPane, String filesFolderName,
                                               String destFolderName, String rarPass, String szPass) {
        List<Thread> commandThreads = new ArrayList<>();
        VBox mainVBox = new VBox();
        mainVBox.setSpacing(20);
        mainVBox.setStyle("-fx-background-color: white");
        mainVBox.setPadding(new Insets(0,100,0,100));
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setMinHeight(615);
        File filesFolder = new File(filesFolderName);
        File destFolder = new File(destFolderName);
        if ( !destFolder.exists() ) destFolder.mkdir();

        for ( int id=0 ; id<selectedFiles.size() ; id++ ) {
            final String currentFileName = selectedFiles.get(id).getFileName();

    // Creating child extraction boxes
            VBox childVBox = new VBox();
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.rgb(222,222,222));
            childVBox.setEffect(dropShadow);
            Label extractionTitle = new Label(selectedFiles.get(id).getFileName()+" - "+selectedFiles.get(id).getFileSizeInString());
            Label szLabel = new Label("Extracting 7z");
            Label rarLabel = new Label("Extracting rar");
            ImageView firstChildExtractionSuccessIcon = new ImageView(new Image(new Main().getClass().getResourceAsStream(
                    "/application/assests/photos/extractionSuccessIcon.png")));
            ImageView secondChildExtractionSuccessIcon = new ImageView(new Image(new Main().getClass().getResourceAsStream(
                    "/application/assests/photos/extractionSuccessIcon.png")));
            ImageView firstChildExtractionFailIcon = new ImageView(new Image(new Main().getClass().getResourceAsStream(
                    "/application/assests/photos/extractionFailIcon.png")));
            ImageView secondChildExtractionFailIcon = new ImageView(new Image(new Main().getClass().getResourceAsStream(
                    "/application/assests/photos/extractionFailIcon.png")));
            JFXSpinner firstChildWaitingProgress = new JFXSpinner();
            JFXSpinner secondChildWaitingProgress = new JFXSpinner();
            firstChildExtractionSuccessIcon.setFitWidth(40);
            firstChildExtractionSuccessIcon.setFitHeight(40);
            secondChildExtractionSuccessIcon.setFitWidth(40);
            secondChildExtractionSuccessIcon.setFitHeight(40);
            firstChildExtractionFailIcon.setFitWidth(40);
            firstChildExtractionFailIcon.setFitHeight(40);
            secondChildExtractionFailIcon.setFitWidth(40);
            secondChildExtractionFailIcon.setFitHeight(40);
            firstChildWaitingProgress.setPrefWidth(40);
            firstChildWaitingProgress.setPrefHeight(40);
            secondChildWaitingProgress.setPrefWidth(40);
            secondChildWaitingProgress.setPrefHeight(40);
            szLabel.getStyleClass().add("extraction-label");
            rarLabel.getStyleClass().add("extraction-label");
            extractionTitle.getStyleClass().add("extraction-title-label");
            childVBox.getStyleClass().add("extraction-child-boxes");
            StackPane firstExtractionRightSideParent = new StackPane();
            StackPane secondExtractionRightSideParent = new StackPane();
            firstExtractionRightSideParent.getChildren().addAll(firstChildExtractionFailIcon,firstChildExtractionSuccessIcon,firstChildWaitingProgress);
            secondExtractionRightSideParent.getChildren().addAll(secondChildExtractionFailIcon,secondChildExtractionSuccessIcon,secondChildWaitingProgress);
            HBox firstHChild = new HBox();
            firstHChild.getStyleClass().add("extraction-child-hboxes");
            HBox secondHChild = new HBox();
            secondHChild.getStyleClass().add("extraction-child-hboxes");
            Pane firstChildGap = new Pane();
            Pane secondChildGap = new Pane();
            firstHChild.getChildren().addAll(szLabel,firstChildGap,firstExtractionRightSideParent);
            secondHChild.getChildren().addAll(rarLabel,secondChildGap,secondExtractionRightSideParent);
            firstHChild.setHgrow(firstChildGap, Priority.ALWAYS);
            secondHChild.setHgrow(secondChildGap,Priority.ALWAYS);
            childVBox.getChildren().addAll(extractionTitle,firstHChild,secondHChild);
            mainVBox.getChildren().add(childVBox);

            firstChildExtractionSuccessIcon.setVisible(false);
            firstChildExtractionFailIcon.setVisible(false);
            secondChildExtractionSuccessIcon.setVisible(false);
            secondChildExtractionFailIcon.setVisible(false);

    // Creating child extraction boxes

            String rarCommand="rar x -y -p"+rarPass+" \""+destFolderName+"\\"+selectedFiles.get(id).getFileName()+".rar\" \""+destFolderName+"\"";
            String szCommand="7z x -y -p"+szPass+" \""+filesFolderName+"\\"+selectedFiles.get(id).getFileName()+".7z\" -o\""+destFolderName+"\"";

            Thread szThread = new Thread(() -> {
                try {
                    Process process = Runtime.getRuntime().exec(szCommand);
                    displayLogMessageToConsole(process.getInputStream());
                    if ( process.waitFor() != 0 ) {
                        isExtractedInSuccess=false;
                        firstChildWaitingProgress.setVisible(false);
                        firstChildExtractionFailIcon.setVisible(true);
                    } else {
                        firstChildWaitingProgress.setVisible(false);
                        firstChildExtractionSuccessIcon.setVisible(true);
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                System.out.println("SZThread finished");
            });

            Thread rarThread = new Thread(() -> {
               try {
                   Process process = Runtime.getRuntime().exec(rarCommand);
                   displayLogMessageToConsole(process.getInputStream());
                   if ( process.waitFor() != 0 ) {
                       isExtractedInSuccess=false;
                       secondChildWaitingProgress.setVisible(false);
                       secondChildExtractionFailIcon.setVisible(true);
                   } else {
                       secondChildWaitingProgress.setVisible(false);
                       secondChildExtractionSuccessIcon.setVisible(true);
                   }
               } catch ( Exception e ) {
                   e.printStackTrace();
               }
               File tempRarFile = new File(destFolderName+"\\"+currentFileName+".rar");
               if ( tempRarFile.exists() ) tempRarFile.delete();
               System.out.println("RARThread finished");
            });

            commandThreads.add(szThread);
            commandThreads.add(rarThread);
        }

        Platform.runLater(()->scrollPane.setContent(mainVBox));

        new Thread( () -> {
            for ( Thread thread:commandThreads ) {
                thread.start();
                try {
                    thread.join();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }).start();
        
    }

    public static void compressFile(String szPassword,String rarPassword,String compressFileName,String filesFolderName,
                                    int compressFileIndex) {
        isArchiveInSuccess=null;


            File compressFile = new File(compressFileName);
            File compressFileLocation = new File(filesFolderName+"\\"+"Encrypted"+compressFileIndex);
            if ( !compressFileLocation.exists() ) {
                compressFileLocation.mkdir();
            }


            String rarFile = compressFileLocation.getAbsolutePath()+".rar";
            String szFile = compressFileLocation.getAbsolutePath()+".7z";



            String rarCommand = "rar a -p"+rarPassword+" -ep1 -dw -y \""+rarFile+"\" \""+compressFileLocation.getAbsolutePath()+"\"";
            String szCommand = "7z a -p"+szPassword+" -y \""+szFile+"\" \""+rarFile+"\"";

            Thread compressThread = new Thread(() -> {

                try {
                    Process moveProcess = Runtime.getRuntime().exec("robocopy \""+compressFileName+"\" \""+compressFileLocation.getAbsolutePath()+"\" /E /MOVE");
                    displayLogMessageToConsole(moveProcess.getInputStream());
                    moveProcess.waitFor();
                    Process rarProcess = Runtime.getRuntime().exec(rarCommand);
                    displayLogMessageToConsole(rarProcess.getInputStream());
                    if ( rarProcess.waitFor()==0 ) {
                        Process szProcess = Runtime.getRuntime().exec(szCommand);
                        displayLogMessageToConsole(szProcess.getInputStream());
                        if ( szProcess.waitFor() != 0 ) isArchiveInSuccess=false;
                        else isArchiveInSuccess=true;
                    } else isArchiveInSuccess=false;

                    if ( compressFileLocation.exists() ) {
                        Runtime.getRuntime().exec("cmd /c rmdir /s /q \""+compressFileLocation.getAbsolutePath()+"\"");
                    }
                    if ( new File(rarFile).exists() ) new File(rarFile).delete();

                } catch ( Exception e ) {
                    isArchiveInSuccess=false;
                    e.printStackTrace();
                }
            });

            compressThread.start();

    }

    private static void displayLogMessageToConsole(InputStream inputStream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String readLine;
            while ( (readLine=br.readLine()) != null ) {
                System.out.println(readLine);
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


}
