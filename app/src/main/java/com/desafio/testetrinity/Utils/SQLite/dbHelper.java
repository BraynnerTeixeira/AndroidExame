package com.desafio.testetrinity.Utils.SQLite;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class dbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public dbHelper(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }
     private static final String NOME_BANCO = "newsDB.db";
     private static final String ID = "_id";
     private static final String TABELA = "news";
     public static final String TITULO = "titulo";
     public static final String FONTE = "fonte";
     public static final String DATA = "data";
     public static final String IMAGEM = "imagem";
     private static final int VERSAO = 1;


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABELA+"("
                + ID + " INTEGER primary key autoincrement,"
                + TITULO + " VARCHAR,"
                + FONTE + " VARCHAR,"
                + DATA + " VARCHAR,"
                + IMAGEM + " VARCHAR"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }

    public String insereDado(String titulo, String autor, String data,String imagem){
        ContentValues valores;
        long resultado;

        db = this.getWritableDatabase();
        valores = new ContentValues();
        valores.put(TITULO, titulo);
        valores.put(FONTE, autor);
        valores.put(DATA, data);
        valores.put(IMAGEM, imagem);

        resultado = db.insert(TABELA, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso" + resultado;

    }
    public Cursor carregaDados() {
        Cursor cursor;
        String[] campos = {ID,TITULO, FONTE, DATA, IMAGEM};
        db = this.getWritableDatabase();
        cursor = db.query(TABELA, campos, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

        }
        db.close();
        return cursor;
    }

}
