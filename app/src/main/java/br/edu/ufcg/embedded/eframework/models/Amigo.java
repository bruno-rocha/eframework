package br.edu.ufcg.embedded.eframework.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Amigo {
    private String idAmigo;
    private String nomeAmigo;
    private String urlPhoto;
    private boolean convidado;

    public Amigo(String idAmigo, String nomeAmigo, String urlPhoto){
        this.idAmigo = idAmigo;
        this.nomeAmigo = nomeAmigo;
        this.urlPhoto = urlPhoto;
        this.convidado = false;
    }

    public String getIdAmigo() {
        return idAmigo;
    }

    public void setIdAmigo(String idAmigo) {
        this.idAmigo = idAmigo;
    }

    public String getNomeAmigo() {
        return nomeAmigo;
    }

    public void setNomeAmigo(String nomeAmigo) {
        this.nomeAmigo = nomeAmigo;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public void setConvidado(boolean convidado){
        this.convidado = convidado;
    }

    public boolean getConvidado(){
        return this.convidado;
    }

    public JSONObject toJSON(String idUser, String diaEvento){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id_receptor", getIdAmigo());
            jsonObject.put("id_remetente", idUser);
            jsonObject.put("status_convite", "true");
            jsonObject.put("aceitou_convite", "false");
            jsonObject.put("data_dia", diaEvento);

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
