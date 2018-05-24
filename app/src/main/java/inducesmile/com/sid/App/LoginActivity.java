package inducesmile.com.sid.App;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import inducesmile.com.sid.Helper.UserLogin;
import inducesmile.com.sid.R;
//Esta aplicação serve como base para vos ajudar, precisam de completar os métodos To do de modo a que a aplicação faça o minimo que é suposto, podem adicionar novas features ou mudar a UI se acharem relevante.
public class LoginActivity extends AppCompatActivity {
    private EditText ip, port,username,password;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ip = findViewById(R.id.ip);
        port = findViewById(R.id.port);
        username=findViewById(R.id.username);
        password = findViewById(R.id.password);
        login =  findViewById(R.id.login);
    }

    public void loginClick(View v){

        new UserLogin(ip.getText().toString(), port.getText().toString(),username.getText().toString(),password.getText().toString());
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }


}
