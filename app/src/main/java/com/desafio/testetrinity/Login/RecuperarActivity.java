package com.desafio.testetrinity.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.desafio.testetrinity.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;

public class RecuperarActivity extends AppCompatActivity {

    EditText edit_email, edit_pass, edit_confirmar;//Declaração dos editText email, senha e confirm a senha
    Button btn_cadastro;//Declaração do botão cadastro
    TextView txt_login;//Declaração do text login
    ProgressBar progressBar;//Declaração da progressbar

    TextInputLayout input_email,input_senha,input_confirmar; //Declaração dos TextInput para tratar os erros

    //Declaração de uma String para fazer o matcher da senha com um Regex
    private static final String SENHA_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,16}$";

    //Declaração de um Pattern para ser usado com o objeto matcher afim de comparar a sequencia passada pelo usuario com a da String passada por parametro
    private static final Pattern pattern = Pattern.compile(SENHA_PATTERN);

    //Declaração de uma String para o SharedPreferences
    public static final String CADASTRO = "CADASTRO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        //Atribuição dos inputTextLayout para configurar as validações independentes
        input_email = findViewById(R.id.alt_email_input_layout);
        input_senha = findViewById(R.id.alt_senha_input_layout);
        input_confirmar = findViewById(R.id.alt_confirm_senha_input_layout);

        progressBar = findViewById(R.id.progress_bar); // Atribuição da progressBar
        edit_email = findViewById(R.id.alt_email); //Atribuição do editText do email
        edit_pass = findViewById(R.id.alt_senha); //Atribuição do editText da senha
        edit_confirmar = findViewById(R.id.alt_confm_senha); //Atribuição do editText da confirmação da senha
        txt_login = findViewById(R.id.alt_login_agora); //Atribuição do textView para voltar a tela de Login
        btn_cadastro = findViewById(R.id.btn_alterar); //Atribuição do botão de Login

        //Função OnClick do botão
        btn_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDados();
            }
        });
    }
    //Função para salvar os dados alterados no SharedPreferences
    private void alterarSenha() {
        String str_pass = edit_pass.getText().toString();

        SharedPreferences.Editor preferencia = getSharedPreferences(CADASTRO, MODE_PRIVATE).edit();
        preferencia.putString("senha", str_pass);
        preferencia.apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();

    }
    //Função para verificar se o email é valido
    private boolean emailValido (){

        SharedPreferences prefs = getSharedPreferences(CADASTRO, MODE_PRIVATE);
        String email = prefs.getString("email", "");

        String regEmail = edit_email.getText().toString().trim();
        if (!regEmail.equals(email)){
            input_email.setError("Esse email não está cadastrado");
            return false;
        }  else {
            input_email.setError(null);
            return true;
        }
    }
    //Função para validar o padrão da senha
    public boolean validarSenha(final String password){

        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
    //Função para verificar as condições da senha, se são todas validas ou não
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
    //Função para verificar se as duas senhas são iguais
    private boolean confirmarSenha(){

        if(!edit_pass.getText().toString().equals(edit_confirmar.getText().toString())){
            input_confirmar.setError("As senhas não são iguais.");
            return false;
        } else{
            input_confirmar.setError(null);
            return true ;
        }
    }
    //Função para exibir uma progressBar
    private void exibirProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    //Função para verificar se todos as condições estão OK, estando chama a função cadastrarUser.
    private void validarDados() {
        boolean boo_email = emailValido();
        boolean boo_senha = senhaValida();
        boolean boo_confirm_senha = confirmarSenha();

        if (boo_email && boo_senha && boo_confirm_senha){
            exibirProgress();
            Toast.makeText(this,"Alterando senha...", Toast.LENGTH_SHORT).show();
            alterarSenha();
            Toast.makeText(this,"Senha Alterada com Sucesso.", Toast.LENGTH_SHORT).show();
        }

    }
    //Uma função com o onClick para voltar a tela de Login do app
    public void abrir_login(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }
}
