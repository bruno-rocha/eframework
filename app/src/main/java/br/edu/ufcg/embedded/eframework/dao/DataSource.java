package br.edu.ufcg.embedded.eframework.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ufcg.embedded.eframework.models.Amigo;
import br.edu.ufcg.embedded.eframework.models.Evento;
import br.edu.ufcg.embedded.eframework.models.User;


public class DataSource {

    private SQLiteDatabase database;
    private final DbHelper dbHelper;
    private static DataSource dataSource;
    /** Constructor of the DataSource
     *   @param context
     */
    private DataSource(Context context) {
        dbHelper = new DbHelper(context);
    }
    /** Method to open the database
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public static DataSource getInstance(Context context){
        if (dataSource == null){
            dataSource = new DataSource(context);
        }
        return dataSource;
    }

    public int deleteAllAmigos() {
        return getDatabase().delete(dbHelper.AMIGO, null, null);
    }

    public int deleteAllEventos() {
        return getDatabase().delete(dbHelper.EVENTO, null, null);
    }

    public int deleteAllUsers() {
        return getDatabase().delete(dbHelper.USUARIO, null, null);
    }

    public List<Amigo> getAmigos() {
        Cursor cursor = getDatabase().query(dbHelper.AMIGO,
                dbHelper.COLUNAS_AMIGO, null , null, null, null, null, null);

        List<Amigo> amigos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Amigo model = createAmigo(cursor);
            amigos.add(model);
        }
        cursor.close();
        return amigos;
    }

    public List<Evento> getEvents() {
        Cursor cursor = getDatabase().query(dbHelper.EVENTO,
                dbHelper.COLUNAS_EVENTO, null , null, null, null, null, null);

        List<Evento> eventos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Evento model = createEvento(cursor);
            eventos.add(model);
        }
        cursor.close();
        return eventos;
    }


    public List<User> getUsers() {
        Cursor cursor = getDatabase().query(dbHelper.USUARIO,
                dbHelper.COLUNAS_USER, null , null, null, null, null, null);

        List<User> users = new ArrayList<>();
        while (cursor.moveToNext()) {
            User model = createUser(cursor);
            users.add(model);
        }
        cursor.close();
        return users;
    }

    public boolean saveAllUsers(List<User> usuarios) {
        deleteAllUsers();
        for (User usuario : usuarios){
            ContentValues valores = new ContentValues();
            valores.put(dbHelper.ID_USUARIO, usuario.getIdPlus());
            valores.put(dbHelper.NOME_USUARIO, usuario.getName());

            if(getDatabase().insert(dbHelper.USUARIO, null, valores) < 1){
                return false;
            }
        }
        return true;
    }

    public boolean saveAllAmigos(List<Amigo> amigos) {
        deleteAllAmigos();
        for (Amigo amigo : amigos){
            ContentValues valores = new ContentValues();
            valores.put(dbHelper.ID_AMIGO, amigo.getIdAmigo());
            valores.put(dbHelper.NOME_AMIGO, amigo.getNomeAmigo());
            valores.put(dbHelper.URL_IMAGEM_AMIGO, amigo.getUrlPhoto());

            if(getDatabase().insert(dbHelper.AMIGO, null, valores) < 1){
                return false;
            }
        }
        return true;
    }

    public boolean saveAllEventos(List<Evento> eventos) {
        deleteAllEventos();
        for (Evento evento : eventos){
            ContentValues valores = new ContentValues();
            valores.put(dbHelper.NOME_EVENTO, evento.getNome());
            valores.put(dbHelper.DESCRICAO_EVENTO, evento.getDescricao());
            valores.put(dbHelper.LATITUDE_EVENTO, evento.getLatitude());
            valores.put(dbHelper.LONGITUDE_EVENTO, evento.getLongitude());
            valores.put(dbHelper.URL_IMAGEM_EVENTO, evento.getUrlFoto());

            if(getDatabase().insert(dbHelper.EVENTO, null, valores) < 1){
                return false;
            }
        }
        return true;
    }

    private Amigo createAmigo(Cursor cursor) {
        Amigo model = new Amigo(cursor.getString(cursor.getColumnIndex(dbHelper.ID_AMIGO)),
                cursor.getString(cursor.getColumnIndex(dbHelper.NOME_AMIGO)),
                cursor.getString(cursor.getColumnIndex(dbHelper.URL_IMAGEM_AMIGO))
        );
        return model;
    }

    private Evento createEvento(Cursor cursor) {
        Evento model = new Evento(
                cursor.getString(cursor.getColumnIndex(dbHelper.NOME_EVENTO)),
                cursor.getString(cursor.getColumnIndex(dbHelper.DESCRICAO_EVENTO)),
                cursor.getDouble(cursor.getColumnIndex(dbHelper.LATITUDE_EVENTO)),
                cursor.getDouble(cursor.getColumnIndex(dbHelper.LONGITUDE_EVENTO)),
                cursor.getString(cursor.getColumnIndex(dbHelper.URL_IMAGEM_EVENTO))
        );
        return model;
    }


    private User createUser(Cursor cursor) {
        User model = new User(cursor.getString(cursor.getColumnIndex(dbHelper.ID_USUARIO)),
                cursor.getString(cursor.getColumnIndex(dbHelper.NOME_USUARIO)),
                "",
                "",
                ""
        );
        return model;
    }

    /** Method to get a database
     *   @return SQLiteDatabase
     */
    private SQLiteDatabase getDatabase() {
        if (this.database == null) {
            this.database = this.dbHelper.getWritableDatabase();
        }

        return this.database;
    }
}