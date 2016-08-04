package br.edu.ufcg.embedded.eframework.models;

public class Evento {
    String nome;
    String descricao;
    double latitude;
    double longitude;
    String urlFoto;
    String data;
    boolean interesse;

    public Evento(String nome, String descricao, String data, double latitude, double longitude, String urlFoto, boolean interesse) {
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
        this.latitude = latitude;
        this.longitude = longitude;
        this.urlFoto = urlFoto;
        this.interesse = interesse;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public boolean haveInteresse() {
        return interesse;
    }

    public void setInteresse(boolean interesse) {
        this.interesse = interesse;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Evento evento = (Evento) o;

        if (Double.compare(evento.latitude, latitude) != 0) return false;
        if (Double.compare(evento.longitude, longitude) != 0) return false;
        return nome != null ? nome.equals(evento.nome) : evento.nome == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = nome != null ? nome.hashCode() : 0;
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", interesse=" + interesse +
                '}';
    }
}

