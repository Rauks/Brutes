/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.gui;

import brutes.ScenesContext;
import brutes.game.ObservableCharacter;
import brutes.net.Protocol;
import brutes.net.client.ErrorResponseException;
import brutes.net.client.InvalidResponseException;
import brutes.net.client.NetworkClient;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karl
 */
public class FightController implements Initializable {
    @FXML
    private Text myName;
    @FXML
    private Text myLevel;
    @FXML
    private Text myLifes;
    @FXML
    private Text myStrength;
    @FXML
    private Text mySpeed;
    @FXML
    private Text myBonus1;
    @FXML
    private Text myBonus2;
    @FXML
    private Text myBonus3;
    @FXML
    private ImageView myImage;
    @FXML
    private ImageView myBonus1Image;
    @FXML
    private ImageView myBonus2Image;
    @FXML
    private ImageView myBonus3Image;
    @FXML
    private Text chName;
    @FXML
    private Text chLevel;
    @FXML
    private Text chLifes;
    @FXML
    private Text chStrength;
    @FXML
    private Text chSpeed;
    @FXML
    private Text chBonus1;
    @FXML
    private Text chBonus2;
    @FXML
    private Text chBonus3;
    @FXML
    private ImageView chImage;
    @FXML
    private ImageView chBonus1Image;
    @FXML
    private ImageView chBonus2Image;
    @FXML
    private ImageView chBonus3Image;
    
    @FXML
    private void handleMenuFightWin(ActionEvent e){
        try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
            try {
                connection.sendCheatFightWin(ScenesContext.getInstance().getSession().getToken());
            } catch (InvalidResponseException | ErrorResponseException ex) {
                Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuFightLoose(ActionEvent e){
        try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
            try {
                connection.sendCheatFightLoose(ScenesContext.getInstance().getSession().getToken());
            } catch (InvalidResponseException | ErrorResponseException ex) {
                Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuFightRandom(ActionEvent e){
        try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
            try {
                connection.sendCheatFightRandom(ScenesContext.getInstance().getSession().getToken());
            } catch (InvalidResponseException | ErrorResponseException ex) {
                Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuFightRegular(ActionEvent e){
        try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
            try {
                connection.sendDoFight(ScenesContext.getInstance().getSession().getToken());
            } catch (InvalidResponseException | ErrorResponseException ex) {
                Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuCharacterNew(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("CreateCharacter.fxml"));
            Scene scene = new Scene(root);
            Stage createWindow = new Stage();
            createWindow.setScene(scene);
            createWindow.show();
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuCharacterUpdate(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("UpdateCharacter.fxml"));
            Scene scene = new Scene(root);
            Stage createWindow = new Stage();
            createWindow.setScene(scene);
            createWindow.show();
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuCharacterDelete(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("DeleteCharacter.fxml"));
            Scene scene = new Scene(root);
            Stage createWindow = new Stage();
            createWindow.setScene(scene);
            createWindow.show();
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuDisconnect(ActionEvent e){
        try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
            try {
                connection.sendLogout(ScenesContext.getInstance().getSession().getToken());
            } catch (InvalidResponseException | ErrorResponseException ex) {
                Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ScenesContext.getInstance().setSession(null);
        ScenesContext.getInstance().showLogin();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableCharacter me = ScenesContext.getInstance().getSession().getMyCharacter();
        ObservableCharacter ch = ScenesContext.getInstance().getSession().getChallengerCharacter();
        
        this.myName.textProperty().bind(me.getName());
        this.myLevel.textProperty().bind(me.getLevel().asString());
        this.myLifes.textProperty().bind(me.getLife().asString());
        this.myStrength.textProperty().bind(me.getStrength().asString());
        this.mySpeed.textProperty().bind(me.getSpeed().asString());
        this.myBonus1.textProperty().bind(me.getBonus(0).getName());
        this.myBonus2.textProperty().bind(me.getBonus(1).getName());
        this.myBonus3.textProperty().bind(me.getBonus(2).getName());
        
        this.chName.textProperty().bind(ch.getName());
        this.chLevel.textProperty().bind(ch.getLevel().asString());
        this.chLifes.textProperty().bind(ch.getLife().asString());
        this.chStrength.textProperty().bind(ch.getStrength().asString());
        this.chSpeed.textProperty().bind(ch.getSpeed().asString());
        this.chBonus1.textProperty().bind(ch.getBonus(0).getName());
        this.chBonus2.textProperty().bind(ch.getBonus(1).getName());
        this.chBonus3.textProperty().bind(ch.getBonus(2).getName());
        
        ScenesContext.getInstance().getSession().netLoadMyCharacter();
        ScenesContext.getInstance().getSession().netLoadChallengerCharacter();
    }    
}
