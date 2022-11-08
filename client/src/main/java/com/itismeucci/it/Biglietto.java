package com.itismeucci.it;

public class Biglietto {
    static int ID_generale = 0;
    int ID;
    String Num_biglietto;
    
    public Biglietto(String num_biglietto) {
        this.ID_generale ++;
        this.ID = this.ID_generale;
        this.Num_biglietto = num_biglietto;
    }

    public Biglietto() {  }

    public int getID() {  return ID;  }
    

    public void setID(int iD) {
        ID = iD;
    }
    public void setNum_biglietto(String num_biglietto) {
        Num_biglietto = num_biglietto;
    }

    public String getNum_biglietto() {  return Num_biglietto;  }

    
}
