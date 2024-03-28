package application.controller;

import application.Main;
import application.model.FileManipulator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    private Connection connection;
    private String filesFolderPath;

    @FXML private AnchorPane loginPane;
    @FXML private AnchorPane signupPane;
    @FXML private JFXTextField loginUsername;
    @FXML private JFXPasswordField loginPassword;
    @FXML private Text forgetLabel;
    @FXML private JFXButton loginButton;
    @FXML private Text signUpLabel;
    @FXML private JFXButton goWebsiteButton;
    @FXML private Label loginResultStatus;
    @FXML private JFXTextField signupUsername;
    @FXML private JFXPasswordField signupPassword1;
    @FXML private JFXPasswordField signupPassword2;
    @FXML private Label signupResultStatus;
    @FXML private JFXTextField filesFolderField;
    private double xOffSet;
    private double yOffSet;


    @FXML
    public void initialize() {
        signupPane.setVisible(false);
        hideItems(true,loginResultStatus);
        goWebsiteButton.setOnAction(e->System.exit(0));
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/extractor"
                    ,"BlackskyExtractor","blacksky123!@#");
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }



    @FXML
    public void clickOnSignUpHereLabel() {
        hideItems(false,signupPane);
    }
    @FXML
    public void clickOnSignupLoginButton() {
        loginUsername.clear();
        loginPassword.clear();
        hideItems(true,loginResultStatus,signupPane);
    }
    @FXML
    public void clickOnSignUpButton() {
        boolean isUserFieldSuccess=true;
        String username=signupUsername.getText();
        String password1=signupPassword1.getText();
        String password2=signupPassword2.getText();
        if ( username.contains(" ") || password1.contains(" ") || password2.contains(" ") ) {
            hideItems(false,signupResultStatus);
            signupResultStatus.setText("Fields are not allowed to contain white spaces.");
            isUserFieldSuccess=false;
        }
        if ( !password1.equals(password2) ) {
            hideItems(false,signupResultStatus);
            signupResultStatus.setText("Password do not match!");
            isUserFieldSuccess=false;
        }

        if ( !isUserFieldSuccess ) {
            return;
        }

        try {

            String checkUserQuery = "SELECT * FROM user_table where username=?";
            PreparedStatement checkUserStatement = connection.prepareStatement(checkUserQuery);
            checkUserStatement.setString(1,username);
            ResultSet resultSet = checkUserStatement.executeQuery();
            if ( resultSet.next() ) {
                signupResultStatus.setText("\""+username+"\" is already exists");
                hideItems(false,signupResultStatus);
                return;
            }
            String addUserQuery = "INSERT INTO user_table(username,password) VALUES(?,?)";
            PreparedStatement addUserStatement = connection.prepareStatement(addUserQuery);
            addUserStatement.setString(1,username);
            addUserStatement.setString(2,password1);
            addUserStatement.execute();
            signupResultStatus.setTextFill(Color.GREEN);
            signupResultStatus.setText("User account created successfully...");
            hideItems(false,signupResultStatus);
        } catch ( Exception e ) {
            signupResultStatus.setText("Failed to create user");
            hideItems(false,signupResultStatus);
            e.printStackTrace();
        }

    }
    @FXML
    public void keyPressOnUserFields() {
        if ( signupPane.isVisible() ) {
            signupResultStatus.setTextFill(Color.RED);
            hideItems(true,signupResultStatus);
        } else {
            loginResultStatus.setTextFill(Color.RED);
            hideItems(true,loginResultStatus);
        }
    }
    @FXML
    public void clickOnLoginButton() {
        System.out.println("clicked");
        boolean isUserCorrect=false;
        boolean isPasswordCorrect=false;

        String username = loginUsername.getText();
        String password = loginPassword.getText();
        try {
            String currentQuery = "SELECT * FROM user_table WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(currentQuery);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while ( resultSet.next() ) {
                if ( resultSet.getString("username").equals(username) ) {
                    isUserCorrect=true;
                    if ( resultSet.getString("password").equals(password) ) {
                        isPasswordCorrect=true;
                    } else {
                        hideItems(false,loginResultStatus);
                        loginResultStatus.setText("Incorrect password");
                    }
                }
            }

            if ( !isUserCorrect ) {
                hideItems(false,loginResultStatus);
                loginResultStatus.setText("Username not found in database");
            }

            if ( isUserCorrect && isPasswordCorrect ) {
                try {
                    if (FileManipulator.isFileExist(filesFolderPath) ) {
                        MainController.FILES_FOLDER = filesFolderPath;
                        System.out.println("Username and password are correct");
                        Stage currentStage = (Stage)loginPane.getScene().getWindow();
                        Parent root = FXMLLoader.load(new Main().getClass().getResource("view/mainView.fxml"));

                        root.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                xOffSet = event.getSceneX();
                                yOffSet = event.getSceneY();
                            }
                        });
                        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                currentStage.setX(event.getScreenX()-xOffSet);
                                currentStage.setY(event.getScreenY()-yOffSet);
                                currentStage.setOpacity(0.8);
                            }
                        });
                        root.setOnMouseReleased(e->currentStage.setOpacity(1));

                        Scene scene = new Scene(root);
                        scene.setFill(Color.TRANSPARENT);
                        currentStage.setScene(scene);
                    } else {
                        hideItems(false,loginResultStatus);
                        loginResultStatus.setText("Path can't be empty");
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                    hideItems(false,loginResultStatus);
                    loginResultStatus.setText("Error with path");
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }



    }
    @FXML
    public void clickOnPathIcon() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if ( selectedDirectory != null ) {
            filesFolderPath = selectedDirectory.getAbsolutePath();
            filesFolderField.setText(selectedDirectory.getAbsolutePath());
        } else {
            filesFolderPath = null;
        }
    }

    private void hideItems(boolean flag , Node ... items ) {
        for ( Node item:items ) item.setVisible(!flag);
    }
}
