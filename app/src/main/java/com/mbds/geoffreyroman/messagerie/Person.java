package com.mbds.geoffreyroman.messagerie;

class Person {
    private String nom;
    private String clepublic;



    public Person(String nom, String clepublic) {
        this.nom = nom;
        this.clepublic = clepublic;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getClepublic() {
        return clepublic;
    }

    public void setClepublic(String clepublic) {
        this.clepublic = clepublic;
    }

}
