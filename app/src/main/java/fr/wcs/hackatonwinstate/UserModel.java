package fr.wcs.hackatonwinstate;

/**
 * Created by apprenti on 12/21/17.
 */

public class UserModel {

    private String user_name;
    private String user_password;
    private String user_numero;
    private int user_win_numbers;

    public UserModel() {
        // Needed for firebase
    }

    public UserModel(String user_name, String user_password, String user_numero, int user_win_numbers) {
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_numero = user_numero;
        this.user_win_numbers = user_win_numbers;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_numero() {
        return user_numero;
    }

    public void setUser_numero(String user_numero) {
        this.user_numero = user_numero;
    }

    public int getUser_win_numbers() {
        return user_win_numbers;
    }

    public void setUser_win_numbers(int user_win_numbers) {
        this.user_win_numbers = user_win_numbers;
    }
}