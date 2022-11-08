package com.itismeucci.it;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Server_Thread extends Thread{
    ServerSocket server      = null;
    Socket client            = null;
    String stringaRicevuta   = null;
    String stringaModificata = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoIlClient;

    public Server_Thread (Socket socket){
        this.client = socket;
    }


    public void run(){
        try {
            comunica();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void comunica() throws Exception{
        inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outVersoIlClient = new DataOutputStream(client.getOutputStream());

        ObjectMapper toJson = new ObjectMapper(); // creo il mapper per la serializzazione in formato json
        
        while(true){
            stringaRicevuta = inDalClient.readLine();                                    // ricevo la stinga dal client
            Messaggio ricevuto = toJson.readValue(stringaRicevuta, Messaggio.class);    // la deserializzo

            if (ricevuto.listaBiblietti.size() == 0){ // se la lista di biglietti ricevuta dal cliuent è vuota
                Messaggio risposta = new Messaggio(Server_Main.bigliettiDisponibili);           //preparpo il messagio con i biglietti disponibili
                outVersoIlClient.writeBytes(toJson.writeValueAsString(risposta) + "\n");       //lo spedisco al client serializzato
            }else {
                ArrayList<Biglietto> biglietti_acquistati = new ArrayList<>();          // preparo l'arraylist dei biglietti acquistati

                for (int i = 0; i < ricevuto.listaBiblietti.size(); i++) {              //doppia iterazione per il controllo
                    for (int j = 0; j < Server_Main.bigliettiDisponibili.size(); j++) {
                        if (ricevuto.listaBiblietti.get(i).ID == Server_Main.bigliettiDisponibili.get(j).ID){      // se il biglietto da acquistare è presente tra i biglietti disponibili
                            biglietti_acquistati.add(Server_Main.bigliettiDisponibili.get(j));                    // lo aggiungo alla lista dei biglietti acquistati
                            Server_Main.bigliettiDisponibili.remove(j);                                          // lo rimuovo da querlli disponibili    
                            j--;                                                                                // di conseguenza sposto l'iteratore j indietro di 1
                        }
                    }
                }

                Messaggio risposta = new Messaggio(biglietti_acquistati);                       // preparpo il messagio con i biglietti il cui acquisto è andato a buon fine
                outVersoIlClient.writeBytes(toJson.writeValueAsString(risposta) + "\n");       //   lo spedisco al client serializzato
            }

        }
    }
}
