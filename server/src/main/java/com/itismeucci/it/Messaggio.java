package com.itismeucci.it;

import java.util.ArrayList;

public class Messaggio {
    ArrayList<Biglietto> listaBiblietti = new ArrayList<>();

    public Messaggio() {  }    

    public Messaggio(ArrayList<Biglietto> listaBiblietti) { this.listaBiblietti = listaBiblietti; }

    public ArrayList<Biglietto> getListaBiblietti() {  return listaBiblietti;  }

    public void setListaBiblietti(ArrayList<Biglietto> listaBiblietti) {  this.listaBiblietti = listaBiblietti;  }
}