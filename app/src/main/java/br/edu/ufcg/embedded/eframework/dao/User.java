package br.edu.ufcg.embedded.eframework.dao;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String idPlus;
    private String name;
    private String email;
    private String urlPhoto;
    private String registrationId;

    /** Constructor of the class User
     *   @param idPlus
     *   @param name
     *   @param email
     *   @param urlPhoto
     */
    public User(String idPlus, String name, String email, String urlPhoto, String registrationId) {
        this.idPlus = idPlus;
        this.name = name;
        this.email = email;
        this.urlPhoto = urlPhoto;
        this.registrationId = registrationId;
    }
    /** Method to get a user id of the google plus
     * @return idPlus
     */
    public String getIdPlus() {
        return idPlus;
    }
    /** Method to set a user id of the google plus
     * @param idPlus
     */
    public void setIdPlus(String idPlus) {
        this.idPlus = idPlus;
    }
    /** Method to get the user name
     * @return name
     */
    public String getName() {
        return name;
    }
    /** Method to set the user name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    /** Method to get the user email
     * @return email
     */
    public String getEmail() {
        return email;
    }
    /** Method to set the user email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /** Method to get a user url of your photo
     * @return urlPhoto
     */
    public String getUrlPhoto() {
        return urlPhoto;
    }
    /** Method to set the user url of your photo
     * @param urlPhoto
     */
    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }


    public String getRegistrationId() {
        return this.registrationId;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("id", getIdPlus());
            jsonObject.put("nome", getName());
            jsonObject.put("email", getEmail());
            jsonObject.put("url_foto", getUrlPhoto());
            jsonObject.put("registration_id", getRegistrationId());

            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}