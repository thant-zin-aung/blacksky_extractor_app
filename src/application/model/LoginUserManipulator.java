package application.model;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class LoginUserManipulator {

    private static List<Map<String,String>> existUserinfo = new ArrayList<>();

    public static boolean isUserExist(String username) {
        try(Connection connection = DatabaseManipulator.getConnection()) {
            String checkUserQuery = "SELECT * FROM user_table where username=?";
            PreparedStatement checkUserStatement = connection.prepareStatement(checkUserQuery);
            checkUserStatement.setString(1,username);
            ResultSet resultSet = checkUserStatement.executeQuery();
            if ( resultSet.next() ) return true;
            else return false;
        } catch ( Exception e ) {
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String,String> getExistUserInfo(String username) {
        if ( isUserExist(username) ) {
            try ( Connection connection = DatabaseManipulator.getConnection() ) {
                String checkUserQuery = "SELECT * FROM user_table where username=?";
                PreparedStatement checkUserStatement = connection.prepareStatement(checkUserQuery);
                checkUserStatement.setString(1,username);
                ResultSet resultSet = checkUserStatement.executeQuery();
                Map<String,String> userInfo = new HashMap<>();
                while (resultSet.next()) {
                    userInfo.put(resultSet.getString("username"),resultSet.getString("password"));
                }

                return userInfo;
            }catch (Exception e ) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static boolean addUser(String username, String password) {
        boolean isUserAddedinSuccess=true;
        Connection connection = DatabaseManipulator.getConnection();
        try {
            String checkUserQuery = "SELECT * FROM user_table where username=?";
            PreparedStatement checkUserStatement = connection.prepareStatement(checkUserQuery);
            checkUserStatement.setString(1,username);
            ResultSet checkUserResultSet = checkUserStatement.executeQuery();
            if ( checkUserResultSet.next() ) {
                return false;
            }
            String addUserQuery = "INSERT INTO user_table(username,password) VALUES(?,?)";
            PreparedStatement addUserStatement = connection.prepareStatement(addUserQuery);
            addUserStatement.setString(1,username);
            addUserStatement.setString(2,password);
            addUserStatement.execute();
        } catch ( Exception e ) {
            isUserAddedinSuccess=false;
            e.printStackTrace();
        }
        return isUserAddedinSuccess;
    }

    public static boolean removeUser(String username,String password) {
        boolean isUserRemoveInSuccess=true;
        try {
            Connection connection = DatabaseManipulator.getConnection();
            String checkUserQuery = "SELECT * FROM user_table where username=?";
            PreparedStatement checkUserStatement = connection.prepareStatement(checkUserQuery);
            checkUserStatement.setString(1,username);
            ResultSet checkUserResultSet = checkUserStatement.executeQuery();
            if ( checkUserResultSet.next() ) {
                if ( !(checkUserResultSet.getString("username").equals(username) &&
                     checkUserResultSet.getString("password").equals(password)) ) {
                    return false;
                }
            } else {
                return false;
            }
            String removeUserQuery = "DELETE FROM user_table where username=?";
            PreparedStatement removeUserStatement = connection.prepareStatement(removeUserQuery);
            removeUserStatement.setString(1,username);
            removeUserStatement.execute();
        } catch ( Exception e ) {
            isUserRemoveInSuccess=false;
            e.printStackTrace();
        }
        return isUserRemoveInSuccess;
    }

    public static boolean changeUsername(String oldUsername,String newUsername,String oldPassword) {
        boolean isUserCredentialCorrect = true;
        try {
            Map<String,String> existUserInfo = getExistUserInfo(oldUsername);
            if ( existUserInfo != null ) {
                if ( !existUserInfo.get(oldUsername).equals(oldPassword) ) isUserCredentialCorrect=false;
            } else {
                isUserCredentialCorrect=false;
            }
        } catch ( Exception e ) {
            isUserCredentialCorrect=false;
            e.printStackTrace();
        }

        if ( isUserCredentialCorrect ) {
            try ( Connection connection = DatabaseManipulator.getConnection() ) {
                String updateUserQuery = "UPDATE user_table SET username=? where username=?";
                PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery);
                updateUserStatement.setString(1,newUsername);
                updateUserStatement.setString(2,oldUsername);
                updateUserStatement.execute();
                return true;
            } catch ( Exception e ) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean changePassword(String oldUsername,String oldPassword,String newPassword) {
        try ( Connection connection = DatabaseManipulator.getConnection() ) {
            Map<String,String> existUserInfo = getExistUserInfo(oldUsername);
            if ( existUserInfo.get(oldUsername).equals(oldPassword) ) {
                String changePasswordQuery = "UPDATE user_table SET password=? where username=?";
                PreparedStatement changePasswordStatement = connection.prepareStatement(changePasswordQuery);
                changePasswordStatement.setString(1,newPassword);
                changePasswordStatement.setString(2,oldUsername);
                changePasswordStatement.execute();
                return true;
            }
            return false;
        } catch ( Exception e ) {
            e.printStackTrace();
            return false;
        }
    }




}
