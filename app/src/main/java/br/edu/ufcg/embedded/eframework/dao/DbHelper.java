package br.edu.ufcg.embedded.eframework.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbHelper extends SQLiteOpenHelper {

    public static final String ID = "_id";

    public static final String ID_USER = "id_user";

    public static final String EVENTO = "EVENTO";
    public static final String NOME_EVENTO = "nome_evento";
    public static final String DESCRICAO_EVENTO = "descricao_evento";
    public static final String LATITUDE_EVENTO = "latitude_evento";
    public static final String LONGITUDE_EVENTO = "longitude_evento";
    public static final String URL_IMAGEM_EVENTO = "url_imagem_evento";
    public static final String INTERESSE_EVENTO = "interesse_evento";

    public static final String[] COLUNAS_EVENTO = new String []{
            ID, NOME_EVENTO, DESCRICAO_EVENTO, LATITUDE_EVENTO, LONGITUDE_EVENTO, URL_IMAGEM_EVENTO, INTERESSE_EVENTO
    };

    public static final String AMIGO = "AMIGO";
    public static final String ID_AMIGO = "id_amigo";
    public static final String NOME_AMIGO = "nome_amigo";
    public static final String URL_IMAGEM_AMIGO = "url_imagem_amigo";

    public static final String[] COLUNAS_AMIGO = new String []{
            ID, ID_AMIGO, NOME_AMIGO, URL_IMAGEM_AMIGO
    };

    public static final String USUARIO = "USUARIO";
    public static final String ID_USUARIO = "id_usuario";
    public static final String NOME_USUARIO = "nome_usuario";

    public static final String[] COLUNAS_USER = new String []{
            ID, ID_USUARIO, NOME_USUARIO
    };

    private static final int DB_VERSION = 4;
    private static final String DB_NAME = "APP_BD";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createTables(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
        // createTables(dataBase);
    }
    /** Method to create the table in the database
     *   @param dataBase
     */
    private void createTables(SQLiteDatabase dataBase) {
        Log.i(DB_NAME, "Criando Tabelas do Banco de Dados");

        dataBase.execSQL("CREATE TABLE " + USUARIO + "(_id integer primary key autoincrement, "
                + ID_USUARIO + " text, " + NOME_USUARIO + " text);");


        dataBase.execSQL("CREATE TABLE " + AMIGO + "(_id integer primary key autoincrement, "
                + ID_AMIGO + " text, " + NOME_AMIGO + " text, " + URL_IMAGEM_AMIGO + " text);");

        dataBase.execSQL("CREATE TABLE " + EVENTO + "(_id integer primary key autoincrement, "
                + NOME_EVENTO + " text, " + DESCRICAO_EVENTO + " text, "
                + LATITUDE_EVENTO + " integer, " + LONGITUDE_EVENTO + " integer, " + URL_IMAGEM_EVENTO + " text, " + INTERESSE_EVENTO + " flag integer default 0);");
    }
}
