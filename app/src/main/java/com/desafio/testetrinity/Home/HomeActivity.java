package com.desafio.testetrinity.Home;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.desafio.testetrinity.Login.LoginActivity;
import com.desafio.testetrinity.R;
import com.desafio.testetrinity.Utils.Api.Article;
import com.desafio.testetrinity.Utils.Api.Service;
import com.desafio.testetrinity.Utils.Api.TopHeadline;
import com.desafio.testetrinity.Utils.Api.wsClient;
import com.desafio.testetrinity.Utils.Model.NewsAdapter;
import com.desafio.testetrinity.Utils.Receiver.ConexaoStatus;
import com.desafio.testetrinity.Utils.SQLite.dbHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    public static final String CADASTRO = "CADASTRO"; //Declaração da string para o SheredPreferences
    public static final String API_KEY = "e08a859b529f4c6792af89563625dc59"; //Declaração da string para o SheredPreferences

    TextView textNome;//Declaração do textView para recuperar o email do SharedPreferences

    private RecyclerView rvNews;
    private ProgressBar progressBar;

    private List<Article> articleList; //Setando uma Lista com uma coleção dos dados da API

    private NewsAdapter newsAdapter; //Setando o adapter

    dbHelper db;
    private ListView lista;

    private BroadcastReceiver broadcastReceiver;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Setando o id do textview
        textNome = findViewById(R.id.txt_nomeUser);

        //Recuperando dados do User cadastrado
        recuperarDados();


        articleList = new ArrayList<>(); // setando a coleção em um ArrayList
        rvNews = findViewById(R.id.rv_news);  //Recuperando do RecyclerView
        progressBar = findViewById(R.id.progress_bar); //Recuperando o ID da ProgressBar

        //Toolbar
        Toolbar tlb = findViewById(R.id.toolbar);
        tlb.setTitle("");
        setSupportActionBar(tlb);

        //Chamo a classe ConexãoStatus para verificar em tempo real o status da rede
        broadcastReceiver = new ConexaoStatus();
        registrarAtividadeNaRede();

        //A instacia desta mesma classe vai como parametro
        CarregarNews carregarNews = new CarregarNews(this);
        carregarNews.execute();

        //ListView para recuperar os dados no SQLite
        lista = findViewById(R.id.list_news);


    }

    @SuppressLint("StaticFieldLeak")
    public class CarregarNews extends AsyncTask<Void, Void, String> {

        // Aqui eu recupero a instancia da Activity
        @SuppressLint("StaticFieldLeak")
        HomeActivity homeActivity;

        CarregarNews(HomeActivity homeActivity) {
            this.homeActivity = homeActivity;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                //Tive que add essa Thread para a progressBar poder aparecer
                Thread.sleep(2000);
                getNews();
            } catch (Exception ignored) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String retorno) {
            progressBar.setVisibility(View.GONE);
        }
    }

    // Setando a Classe newsAdapter no RecyclerView
    private void setRecyclerView() {
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(newsAdapter);
    }

    //Recuperando as noticias
    private void getNews() {
        Service webService = wsClient.getClient().create(Service.class);
        Call<TopHeadline> call = webService.getTopHeadlines("pt", API_KEY);
        call.enqueue(new Callback<TopHeadline>() {
            @Override
            public void onResponse(@NonNull Call<TopHeadline> call, @NonNull Response<TopHeadline> response) {
                rvNews.setVisibility(View.VISIBLE);
                assert response.body() != null;
                articleList = response.body().getArticleList();
                newsAdapter = new NewsAdapter(HomeActivity.this, articleList);
                setRecyclerView();

                /*
                 * Fiz um for para varrer toda listar e add ao banco tudo da API
                 */
                for (int i = 0; i < articleList.size(); i++) {
                    Article dados = articleList.get(i);
                    String titulo = dados.getTitle();
                    String autor = dados.getSource().getName();
                    String data = dados.getPublishedAt();
                    String img = dados.getUrlToImage();
                    inserirDB(titulo, autor, data, img);

                }
                Log.i("DadosSalvos", "Dados salvos no BD com sucesso.");
            }

            @Override
            public void onFailure(@NonNull Call<TopHeadline> call, @NonNull Throwable t) {
                rvNews.setVisibility(View.GONE);
                lista.setVisibility(View.VISIBLE);
                recuperarDB();
            }
        });
    }

    /**
     * Função para recuperar o email salbo no Shared
     */
    public void recuperarDados() {
        SharedPreferences prefs = getSharedPreferences(CADASTRO, MODE_PRIVATE);
        String email = prefs.getString("email", "");
        textNome.setText(email);
    }

    /**
     * Função para deslogar do sistema
     */
    public void sair(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        Toast.makeText(this, "Deslogando do sistema", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Função para inserir os dados no bd vindo da API
     */
    public void inserirDB(String str_titulo, String str_fonte, String str_data, String str_img) {

        db = new dbHelper(getApplicationContext());

        String dados;
        dados = db.insereDado(str_titulo, str_fonte, str_data, str_img);
        Log.i("DB", dados);
    }

    /**
     * Função para recuperar os dados salvos no DB
     */
    public void recuperarDB() {

        dbHelper dbNews = new dbHelper(getBaseContext());
        Cursor cursor = dbNews.carregaDados();

        String[] nomeCampos = new String[]{dbHelper.TITULO, dbHelper.FONTE, dbHelper.DATA, dbHelper.IMAGEM};
        int[] idViews = new int[]{R.id.txt_titulo, R.id.txt_fonteNews, R.id.txt_data_noticia, R.id.img_news};

        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(getBaseContext(),
                R.layout.layout_news, cursor, nomeCampos, idViews, 0);
        lista.setAdapter(adaptador);
    }

    /**
     * Função para verificar a versão do SKD e registrar alteraçãos de rede
     */
    private void registrarAtividadeNaRede() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    /**
     * Função para cancelar qualquer registro de alteração na rede
     */
    protected void cacelarAlteracoesNaRede() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * No onDestroy chamei a função  cacelarAlteracoesNaRede para encerrar no momento que a Activity for destruída
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        cacelarAlteracoesNaRede();
    }
}