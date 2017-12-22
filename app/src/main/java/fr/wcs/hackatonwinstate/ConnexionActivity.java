package fr.wcs.hackatonwinstate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ConnexionActivity extends AppCompatActivity {

    final String userName = "NameKey";
    final String userPassword = "PasswordKey";
    private String mUserId = "UserKey";
    private String mEncrypt = "encrypt";
    private boolean auth = false;
    private ProgressBar simpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        final EditText editTextUserName = findViewById(R.id.connexionUserName);
        final EditText editTextUserPassword = findViewById(R.id.connexionUserPassword);
        final Button buttonSend = findViewById(R.id.buttonConnexionSend);

        // On recupere les Shared  Preferences
        final SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String sharedPrefUserName = sharedpreferences.getString(userName, "");
        final String sharedPrefUserPassword = sharedpreferences.getString(userPassword, "");
        final String sharedPrefUserKey = sharedpreferences.getString(mUserId, "");
        simpleProgressBar = findViewById(R.id.simpleProgressBar);

        //On rempli les editText avec les sharedPreferences si c'est pas notre premiere connexion
        if (!sharedPrefUserName.isEmpty() && !sharedPrefUserPassword.isEmpty()) {
            editTextUserName.setText(sharedPrefUserName);
            editTextUserPassword.setText(sharedPrefUserPassword);
        }

        // Au clic du bouton, c'est la que tout se passe !!!!!!!!
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                simpleProgressBar.setVisibility(View.VISIBLE);

                //On recupere le contenu des edit text
                final String userNameContent = editTextUserName.getText().toString();
                final String userPasswordContent = editTextUserPassword.getText().toString();

                // Toast si les champs ne sont pas remplis
                if (TextUtils.isEmpty(userNameContent) || TextUtils.isEmpty(userPasswordContent)) {
                    Toast.makeText(getApplicationContext(), R.string.error_fill, Toast.LENGTH_SHORT).show();
                    simpleProgressBar.setVisibility(view.GONE);
                } else {
                    // Sinon on recupere tous les users
                    final DatabaseReference refUser = FirebaseHelper.getDatabase().getReference("User");
                    refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                UserModel userValues = dsp.getValue(UserModel.class);

                                //On compare le contenu des edit text avec Firebase grâce au user_name
                                if (userValues.getUser_name().equals(userNameContent)) {
                                    // On verifie le password
                                    if (userValues.getUser_password().equals(mEncrypt(userPasswordContent, "AES"))) {

                                        // La clé de l'utilisateur qu'on va utiliser partout dans l'application.
                                        mUserId = dsp.getKey();
                                        // On sauvegarde l'utilisateur connu dans les sharedPreferences
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(userName, userNameContent);
                                        editor.putString(userPassword, userPasswordContent);
                                        editor.putString("mUserId", mUserId);
                                        editor.apply();

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        simpleProgressBar.setVisibility(View.GONE);
                                    } else {
                                        // Si le mot de passe ou le pseudo ne concordent pas
                                        Toast.makeText(getApplicationContext(), R.string.error_password, Toast.LENGTH_SHORT).show();
                                        simpleProgressBar.setVisibility(View.GONE);
                                    }
                                    return;
                                }
                            }

                            // Utilisateur nouveau : le compte n'existe pas, on le créer !
                            UserModel user = new UserModel();
                            user.setUser_name(userNameContent);
                            user.setUser_password(mEncrypt(userPasswordContent, "AES"));
                            user.setUser_numero("");
                            user.setUser_level_number(0);
                            user.setUser_level_name("Bizuth du Win");
                            user.setUser_avatar("https://firebasestorage.googleapis.com/v0/b/hackatonwinstate.appspot.com/o/Avatars%2Finfos.png?alt=media&token=03488c0d-e97e-41ed-8880-9ddcbf122236");
                            String userId = refUser.push().getKey();
                            user.setUid(userId);

                            refUser.child(userId).setValue(user);
                            // La clé de l'utilisateur qu'on va utiliser partout dans l'application.
                            mUserId = userId;

                            // On enregistre dans les shared Preferences
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(userName, userNameContent);
                            editor.putString(userPassword, userPasswordContent);
                            editor.putString("mUserId", userId);
                            editor.apply();
                            Toast.makeText(getApplicationContext(), R.string.created_user, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            simpleProgressBar.setVisibility(View.GONE);
                        }

                        // Encryptage du mot de passe
                        public String mEncrypt(String userPassword, String key) {
                            try {
                                Key clef = new SecretKeySpec(key.getBytes("ISO-8859-2"), "Blowfish");
                                Cipher cipher = Cipher.getInstance("Blowfish");
                                cipher.init(Cipher.ENCRYPT_MODE, clef);
                                return new String(cipher.doFinal(userPassword.getBytes()));
                            } catch (Exception e) {
                                return null;
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        simpleProgressBar.setVisibility(View.GONE);
    }
}