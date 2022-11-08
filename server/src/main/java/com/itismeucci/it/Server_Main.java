package com.itismeucci.it;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server_Main {
    private static ServerSocket serverSocket;
    static ArrayList<Biglietto> bigliettiDisponibili = new ArrayList<>();

    public Server_Main() { 
        Biglietto bc1 = new Biglietto("centrale-1");
        Biglietto bc2 = new Biglietto("centrale-2");
        Biglietto bc3 = new Biglietto("centrale-3");

        Biglietto bl1 = new Biglietto("laterale-1");
        Biglietto bl2 = new Biglietto("laterale-2");
        Biglietto bl3 = new Biglietto("laterale-3");
        
        Biglietto bp1 = new Biglietto("posteriore-1");
        Biglietto bp2 = new Biglietto("posteriore-2");
        Biglietto bp3 = new Biglietto("posteriore-3");

        bigliettiDisponibili.add(bc1);
        bigliettiDisponibili.add(bc2);
        bigliettiDisponibili.add(bc3);

        bigliettiDisponibili.add(bl1);
        bigliettiDisponibili.add(bl2);
        bigliettiDisponibili.add(bl3);

        bigliettiDisponibili.add(bp1);
        bigliettiDisponibili.add(bp2);
        bigliettiDisponibili.add(bp3);        
    }

    public void avvia(){
        try {
            serverSocket = new ServerSocket(7777);
            System.out.println("Server Avviato");

            while(true){

                Socket socket = serverSocket.accept();              // creo il csocket con il client dopo averlo accettato.
                Server_Thread thread = new Server_Thread(socket);   // creo il tread che si occuperà del client 
                thread.start();                                     // lo avvio

            }

        } catch (Exception e) {
            System.out.println("Server spento");
        }
    }
}
