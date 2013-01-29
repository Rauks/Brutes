package brutes.client.gui;

import brutes.client.ScenesContext;
import brutes.client.game.ObservableBrute;
import brutes.client.net.ErrorResponseException;
import brutes.client.net.InvalidResponseException;
import brutes.client.net.NetworkClient;
import brutes.client.user.Session;
import brutes.net.Protocol;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karl
 */
public class FightController implements Initializable {
    private Stage currentDialogStage;
    private ReadOnlyBooleanWrapper isFighting;
    
    @FXML
    private VBox myBruteStats;
    @FXML
    private VBox chBruteStats;
    @FXML
    private Text myName;
    @FXML
    private Text myLevel;
    @FXML
    private Text myLifes;
    @FXML
    private Text myBonusLife;
    @FXML
    private Text myStrength;
    @FXML
    private Text myBonusStrength;
    @FXML
    private Text mySpeed;
    @FXML
    private Text myBonusSpeed;
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
    private Text chBonusLife;
    @FXML
    private Text chStrength;
    @FXML
    private Text chBonusStrength;
    @FXML
    private Text chSpeed;
    @FXML
    private Text chBonusSpeed;
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
    private MenuItem menuFightWin;
    @FXML
    private MenuItem menuFightLoose;
    @FXML
    private MenuItem menuFightRandom;
    @FXML
    private MenuItem menuFightRegular;
    @FXML
    private MenuItem menuOptCreate;
    @FXML
    private MenuItem menuOptRename;
    @FXML
    private MenuItem menuOptDelete;
    
    private void doFight(FightTask.FightType type){
        final FightTask fightTask = new FightTask(type);
        fightTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newState) {
                if(newState == Worker.State.SUCCEEDED){
                    Session s = ScenesContext.getInstance().getSession();
                    s.getMyBrute().unload();
                    s.getChallengerBrute().unload();
                    s.netLoadMyBrute();
                    s.netLoadChallengerBrute();
                    isFighting.set(false);
                    try {
                        Parent root;
                        Stage window = new Stage();
                        window.setResizable(false);
                        if(fightTask.getResultProperty().get()){
                            root = FXMLLoader.load(this.getClass().getResource("result/FightResultWin.fxml"));
                            window.setTitle("Victoire !");
                        }
                        else{
                            root = FXMLLoader.load(this.getClass().getResource("result/FightResultLoose.fxml"));
                            window.setTitle("Défaite !");
                        }
                        Scene scene = new Scene(root);
                        window.setScene(scene);
                        setCurrentDialogStage(window);
                        window.show();
                    } catch (IOException ex) {
                        Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else if(newState == Worker.State.FAILED){
                    isFighting.set(false);
                }
            }
        });
        this.isFighting.set(true);
        new Thread(fightTask).start();
    }
    
    @FXML
    private void handleMenuFightWin(ActionEvent e){
        this.doFight(FightTask.FightType.CHEAT_WIN);
    }
    @FXML
    private void handleMenuFightLoose(ActionEvent e){
        this.doFight(FightTask.FightType.CHEAT_LOOSE);
    }
    @FXML
    private void handleMenuFightRandom(ActionEvent e){
        this.doFight(FightTask.FightType.CHEAT_RANDOM);
    }
    @FXML
    private void handleMenuFightRegular(ActionEvent e){
        this.doFight(FightTask.FightType.REGULAR);
    }
    @FXML
    private void handleMenuBruteNew(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("brute/CreateBrute.fxml"));
            Scene scene = new Scene(root);
            Stage window = new Stage();
            window.setScene(scene);
            window.setTitle("Nouvelle brute");
            window.setResizable(false);
            this.setCurrentDialogStage(window);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuBruteUpdate(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("brute/UpdateBrute.fxml"));
            Scene scene = new Scene(root);
            Stage window = new Stage();
            window.setScene(scene);
            window.setTitle("Modifier la brute");
            window.setResizable(false);
            this.setCurrentDialogStage(window);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuBruteDelete(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("brute/DeleteBrute.fxml"));
            Scene scene = new Scene(root);
            Stage window = new Stage();
            window.setScene(scene);
            window.setTitle("Supprimer la brute");
            window.setResizable(false);
            this.setCurrentDialogStage(window);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuDisconnect(ActionEvent e){
        new Thread(){
            @Override
            public void run() {
                try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                    try {
                        connection.sendLogout(ScenesContext.getInstance().getSession().getToken());
                    } catch (InvalidResponseException | ErrorResponseException ex) {
                        Logger.getLogger(FightController.class.getName()).log(Level.WARNING, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
        this.closeCurrentDialogStage();
        ScenesContext.getInstance().showLogin();
    }
    @FXML
    private void handleMenuCredits(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("Credits.fxml"));
            Scene scene = new Scene(root);
            Stage window = new Stage();
            window.setScene(scene);
            window.setTitle("À propos...");
            window.setResizable(false);
            this.setCurrentDialogStage(window);
            window.show();
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setCurrentDialogStage(Stage stage){
        this.closeCurrentDialogStage();
        this.currentDialogStage = stage;
    }
    private void closeCurrentDialogStage(){
        if(this.currentDialogStage != null){
            this.currentDialogStage.close();
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.isFighting = new ReadOnlyBooleanWrapper();
        this.isFighting.set(false);
        
        ObservableBrute me = ScenesContext.getInstance().getSession().getMyBrute();
        ObservableBrute ch = ScenesContext.getInstance().getSession().getChallengerBrute();
        
        this.myBruteStats.visibleProperty().bind(me.isLoadedProperty());
        this.chBruteStats.visibleProperty().bind(ch.isLoadedProperty());
        
        this.myName.textProperty().bind(me.getNameProperty());
        this.myLevel.textProperty().bind(me.getLevelProperty().asString());
        this.myLifes.textProperty().bind(me.getLifeProperty().asString());
        this.myBonusLife.textProperty().bind(me.getBonusLifeProperty().asString());
        this.myStrength.textProperty().bind(me.getStrengthProperty().asString());
        this.myBonusStrength.textProperty().bind(me.getBonusStrengthProperty().asString());
        this.mySpeed.textProperty().bind(me.getSpeedProperty().asString());
        this.myBonusSpeed.textProperty().bind(me.getBonusSpeedProperty().asString());
        this.myBonus1.textProperty().bind(me.getBonus(0).getNameProperty());
        this.myBonus2.textProperty().bind(me.getBonus(1).getNameProperty());
        this.myBonus3.textProperty().bind(me.getBonus(2).getNameProperty());
        
        this.myImage.imageProperty().bind(me.getImageProperty());
        this.myBonus1Image.imageProperty().bind(me.getBonus(0).getImageProperty());
        this.myBonus2Image.imageProperty().bind(me.getBonus(1).getImageProperty());
        this.myBonus3Image.imageProperty().bind(me.getBonus(2).getImageProperty());
        
        this.chName.textProperty().bind(ch.getNameProperty());
        this.chLevel.textProperty().bind(ch.getLevelProperty().asString());
        this.chLifes.textProperty().bind(ch.getLifeProperty().asString());
        this.chBonusLife.textProperty().bind(ch.getBonusLifeProperty().asString());
        this.chStrength.textProperty().bind(ch.getStrengthProperty().asString());
        this.chBonusStrength.textProperty().bind(ch.getBonusStrengthProperty().asString());
        this.chSpeed.textProperty().bind(ch.getSpeedProperty().asString());
        this.chBonusSpeed.textProperty().bind(ch.getBonusSpeedProperty().asString());
        this.chBonus1.textProperty().bind(ch.getBonus(0).getNameProperty());
        this.chBonus2.textProperty().bind(ch.getBonus(1).getNameProperty());
        this.chBonus3.textProperty().bind(ch.getBonus(2).getNameProperty());
        
        this.chImage.imageProperty().bind(ch.getImageProperty());
        this.chBonus1Image.imageProperty().bind(ch.getBonus(0).getImageProperty());
        this.chBonus2Image.imageProperty().bind(ch.getBonus(1).getImageProperty());
        this.chBonus3Image.imageProperty().bind(ch.getBonus(2).getImageProperty());
        
        this.menuFightWin.disableProperty().bind(this.isFighting.getReadOnlyProperty().or(me.isLoadedProperty().not()));
        this.menuFightLoose.disableProperty().bind(this.isFighting.getReadOnlyProperty().or(me.isLoadedProperty().not()));
        this.menuFightRandom.disableProperty().bind(this.isFighting.getReadOnlyProperty().or(me.isLoadedProperty().not()));
        this.menuFightRegular.disableProperty().bind(this.isFighting.getReadOnlyProperty().or(me.isLoadedProperty().not()));
        
        this.menuOptCreate.disableProperty().bind(me.isLoadedProperty());
        this.menuOptRename.disableProperty().bind(me.isLoadedProperty().not());
        this.menuOptDelete.disableProperty().bind(me.isLoadedProperty().not());
        
        ScenesContext.getInstance().getSession().netLoadMyBrute();
        ScenesContext.getInstance().getSession().netLoadChallengerBrute();
    }    
}
