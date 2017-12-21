package fr.wcs.hackatonwinstate;

import java.util.HashMap;

/**
 * Created by apprenti on 12/21/17.
 */

public class UserModel {

    private String user_name;
    private String user_password;
    private String user_numero;
    private int user_win_numbers;
    private int user_level_number;
    private String user_level_name;
    private String user_avatar;
    private String uid;
    private HashMap<String, String> user_smiles = new HashMap<>();

    public UserModel() {
        // Needed for firebase
    }

    public UserModel(String user_name, String user_password, String user_numero, int user_win_numbers,
                     int user_level_number, String user_level_name,String user_avatar,String uid) {
        this.user_name = user_name;
        this.user_password = user_password;
        this.user_numero = user_numero;
        this.user_win_numbers = user_win_numbers;
        this.user_level_number = user_level_number;
        this.user_level_name = user_level_name;
        this.user_avatar = user_avatar;
        this.uid = uid;
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

    public int getUser_level_number() {
        return user_level_number;
    }

    public void setUser_level_number(int user_level_number) {
        this.user_level_number = user_level_number;
    }

    public String getUser_level_name() {
        return user_level_name;
    }

    public void setUser_level_name(String user_level_name) {
        this.user_level_name = user_level_name;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public HashMap<String, String> getUser_smiles() {
        return user_smiles;
    }

    public void setUser_smiles(HashMap<String, String> user_smiles) {
        this.user_smiles = user_smiles;
    }
}