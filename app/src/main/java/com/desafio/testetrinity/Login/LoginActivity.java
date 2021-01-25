package com.desafio.testetrinity.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.desafio.testetrinity.Cadastro.CadastroActivity;
import com.desafio.testetrinity.Home.HomeActivity;
import com.desafio.testetrinity.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    // Declaração da String para o uso no SheredPreference
    public static final String CADASTRO = "CADASTRO";

     Button btn_login; //Declaração do botão login
     EditText edit_email, edit_pass; //Declaração do EditText email e senha
     TextView txt_registro, txt_esqueciSenha;//Declaração dos textView registrar e esqueceu senha
     ProgressBar progressBar;//Declaração do progressBar

    TextInputLayout input_email,input_senha; //Declaração do TextInput para tratar os erros

    //Declaração de uma String para fazer o matcher da senha com um Regex
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,16}$";

    //Declaração de um Pattern para ser usado com o objeto matcher afim de comparar a sequencia passada pelo usuario com a da String passada por parametro
    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Atribuindo o id dos input email e senha
        input_email = findViewById(R.id.email_input_layout);
        input_senha = findViewById(R.id.pass_input_layout);

        progressBar = findViewById(R.id.progress_bar); //Atribuindo o id da progressBar

        //Atribuindo o id dos editText para recuperar o email e senha digitado
        edit_email = findViewById(R.id.login_email);
        edit_pass = findViewById(R.id.login_pass);

        //Atribuindo o id dos textView para abrir o registro ou a recuperação de senha
        txt_registro = findViewById(R.id.registrar_text);
        txt_esqueciSenha = findViewById(R.id.esquecei_senha);

        btn_login = findViewById(R.id.btn_login); //Atribuindo o id do botão login

        //Função onClick do botão Login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDados();

            }
        });

    }
    //Função login onde fazemos a comparação no SheredPreference dos dados cadastrados na tela de Cadastro
    public void Login(){
        String str_email = edit_email.getText().toString();
        String str_pass = edit_pass.getText().toString();

        SharedPreferences prefs = getSharedPreferences(CADASTRO, MODE_PRIVATE);
        String email = prefs.getString("email", "");
        String senha = prefs.getString("senha", "");


            if (email.equals(str_email) && senha.equals(str_pass)) {
                exibirProgress();
                Toast.makeText(this, "Logando...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else if (!email.equals(str_email)) {
                Toast.makeText(this, "O email está errado.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "A senha está errada.", Toast.LENGTH_SHORT).show();
            }
    }

    private boolean emailValido (){
        String regEmail = edit_email.getText().toString().trim();
        if (regEmail.isEmpty()){
            input_email.setError("O campo email não pode estar vazio.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(regEmail).matches()) {
            input_email.setError("Digite um email válido.");
            return false;
        } else {
            input_email.setError(null);
            return true;
        }
    }
    public boolean validarSenha(final String password){

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    private boolean senhaValida (){
        String regPass = edit_pass.getText().toString().trim();

        if (regPass.isEmpty()){
            input_senha.setError("O campo senha não pode estar vazio.");
            return false;
        }else if (!(edit_pass.length() >= 8 && edit_pass.length() <= 16 )) {
            input_senha.setError("A senha deve conter de 8 a 16 digitos. ");
            return false;
        }else if (!validarSenha(regPass)){
            input_senha.setError("A senha deve conter pelo menos uma letra\n" +
                    "maiuscula, uma minuscula, um número e um caractére especial.");
            return false;
        } else {
            input_senha.setError(null);
            return true;
        }
    }

    private void exibirProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void validarDados() {
        boolean boo_email = emailValido();
        boolean boo_senha = senhaValida();

        if (boo_email && boo_senha){
            Login();
        }

    }

    public void abrir_cadastro(View view){
        startActivity(new Intent(this, CadastroActivity.class));
    }
    public void abrir_alterar(View view){
        startActivity(new Intent(this, RecuperarActivity.class));
    }
}
