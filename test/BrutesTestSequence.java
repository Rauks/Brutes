

import brutes.client.ScenesContext;
import brutes.client.net.ErrorResponseException;
import brutes.client.net.InvalidResponseException;
import brutes.client.net.NetworkClient;
import brutes.client.user.Session;
import brutes.net.Protocol;
import brutes.server.db.DatasManager;
import brutes.server.net.NetworkLocalTestServer;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Karl
 */
public class BrutesTestSequence {
    private static Thread SERVER = new Thread(){
        @Override
        public void run(){
            try (ServerSocket sockserv = new ServerSocket (42666)) {
                
                // DEBUG
                (new File("~$bdd.db")).delete();
                DatasManager.getInstance("sqlite", "~$bdd.db");

                while(true){
                    Socket sockcli = sockserv.accept();
                    try(NetworkLocalTestServer n = new NetworkLocalTestServer(sockcli)){
                        n.read();
                    } catch (Exception ex) {
                        Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    public static void main(String[] args){
        BrutesTestSequence.SERVER.start();
        
        String token = "";
        int me = -1;
        int ch = -1;
        
        System.out.println("Login");
        try(NetworkClient sut = new NetworkClient(new Socket("localhost", Protocol.CONNECTION_PORT))){
            token = sut.sendLogin("Kirauks", "root2");
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        ScenesContext.getInstance().setSession(new Session("localhost", token));
        
        System.out.println("Delete Brute");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.sendDeleteBrute(token);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Create Brute");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.sendCreateBrute(token, "Rauks");
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Update Brute");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.sendUpdateBrute(token, "Rauks le Brave");
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Get Me");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            me = sut.sendGetMyBruteId(token);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Datas Me");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.getDataBrute(me);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Get Ch");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            ch = sut.sendGetChallengerBruteId(token);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Datas Ch");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.getDataBrute(ch);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Fight Regular");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.sendDoFight(token);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Fight Loose");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.sendCheatFightLoose(token);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Fight Win");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.sendCheatFightWin(token);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Fight Random");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.sendCheatFightRandom(token);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Logout");
        try(NetworkClient sut = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))){
            sut.sendLogout(token);
        } catch (IOException | ErrorResponseException | InvalidResponseException ex) {
            Logger.getLogger(BrutesTestSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
