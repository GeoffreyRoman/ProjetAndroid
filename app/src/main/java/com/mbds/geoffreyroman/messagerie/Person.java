package com.mbds.geoffreyroman.messagerie;

class Person {
    private String nom;
    private String cleprive;
    private String clepublic;



    public Person(String nom, String cleprive, String clepublic) {
        this.nom = nom;
        this.cleprive = cleprive;
        this.clepublic = clepublic;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCleprive() {
        return cleprive;
    }

    public void setCleprive(String cleprive) {
        this.cleprive = cleprive;
    }

    public String getClepublic() {
        return clepublic;
    }

    public void setClepublic(String clepublic) {
        this.clepublic = clepublic;
    }

}
