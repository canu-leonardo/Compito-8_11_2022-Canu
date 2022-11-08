package com.itismeucci.it;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Client {
    String nomeServer = "localhost";
    int porta = 7777;
    Socket mioSocket;
    BufferedReader tastiera;
    DataOutputStream outVersoIlServer;
    BufferedReader inDalServer;

    public Socket connetti(){

        try {
            //creo il thread con il server
            mioSocket = new Socket(nomeServer, porta);
            System.out.println("connesso");
            //istanzio tutti gli stream
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            outVersoIlServer = new DataOutputStream(mioSocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader( mioSocket.getInputStream()));

        } catch (Exception e) {
           
            System.out.println("===DISCONNESSO DAL SERVER===");
        
        }

        return mioSocket;
    }

    public void comunica(){

        ObjectMapper toJson = new ObjectMapper();                       // creo il mapper per la serializzazione in formato json
        try {
            ArrayList<Biglietto> lista_vuota = new ArrayList<>();       // creo una lista vuota( messaggio per far capire al server di inviarmi la lista dei biglietti completa)
            Messaggio m = new Messaggio(lista_vuota);                    // creo il messaggio con la lista vuota
            outVersoIlServer.writeBytes(toJson.writeValueAsString(m) + "\n");  // lo invio serializzato

            String stringaRicevuta = inDalServer.readLine();                                                         // aspetto la risposta dal server
            Messaggio listacompleta = toJson.readValue(stringaRicevuta, Messaggio.class);          // lo deserializzo


            //===STAMPA DEI BIGLIETTI DISPONIBILI===
            System.out.println("--------------------------");
            System.out.println("Biglietti diponibili:");
            for (int i = 0; i < listacompleta.listaBiblietti.size(); i++) {
                System.out.println("- ID:" + listacompleta.listaBiblietti.get(i).ID + ", " + listacompleta.listaBiblietti.get(i).Num_biglietto);
            }
            System.out.println("--------------------------");

            System.out.println("inserisci gli ID dei biglietti che vuoi comprare ( !!! scrivi soltanto gli ID, devono essere separati da uno spazio !!! )");
            String stringaUtente = tastiera.readLine();             // leggo la stringa dell'utente inserita da tastiera
            String[] Id_Biglietti = stringaUtente.split(" ");       // divido la stringa ricevuta tramite gli spazi, ricavando gli id dei biglietti singoli, messi nell'array "Id_Biglietti"
            ArrayList<Biglietto> daComprare = new ArrayList<>();    //preparo l'arraylist di biglietti da comprare
            for (int i = 0; i < Id_Biglietti.length; i++ ){         // creo biglietti basic e li metto nela lista, tanto il server confronterà gli ID
                Biglietto b = new Biglietto();
                b.ID = Integer.parseInt( Id_Biglietti[i] ); 
                daComprare.add(b);
            }
            Messaggio richiesta = new Messaggio(daComprare);                                                //creo il messaggio con la lista 
            outVersoIlServer.writeBytes(toJson.writeValueAsString(richiesta) + "\n");                       // lo invio serializzato
            String stringaRicevutadue = inDalServer.readLine();   
            Messaggio bigliettiComprati = toJson.readValue(stringaRicevutadue, Messaggio.class);//aspetto la risposta del server, con i biglietti ilcui acquisto è andato a buon fine      

            //===STAMPA DEI BIGLIETTI COMPRATI===
            System.out.println("--------------------------");
            System.out.println("Biglietti Acquitstati correttamente:");
            for (int i = 0; i < bigliettiComprati.listaBiblietti.size(); i++) {
                System.out.println("- ID:" + bigliettiComprati.listaBiblietti.get(i).ID + ", " + bigliettiComprati.listaBiblietti.get(i).Num_biglietto);
            }
            System.out.println("--------------------------");
            
            mioSocket.close(); //chiudo il socket, e quindi la connessione

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

}
