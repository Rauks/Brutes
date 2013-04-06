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
import javafx.animation.Animation.Status;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransitionBuilder;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Karl
 */
public class FightController implements Initializable {
    private Stage currentDialogStage;
    private ReadOnlyBooleanWrapper isFighting;
    private ScaleTransition centerVsshadowScaleTransition;
    
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
    private Group VS;
    @FXML
    private ProgressIndicator loadingVS;
    @FXML
    private Circle centerVS;
    @FXML
    private Circle centerVSshadow;
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
    @FXML
    private MenuItem menuDisconnect;
    @FXML
    private MenuItem menuCredits;
    @FXML
    private Menu menuBrute;
    @FXML
    private Group arrowBruteMenu;
    @FXML
    private Group arrowBruteNew;
    
    private void doFight(FightTask.FightType type){
        final FightTask fightTask = new FightTask(type);
        fightTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newState) {
                if(newState == Worker.State.SUCCEEDED){
                    isFighting.set(false);
                    try {
                        Parent root;
                        Stage window = new Stage();
                        window.setResizable(false);
                        if(fightTask.getResultProperty().get()){
                            root = FXMLLoader.load(this.getClass().getResource("result/FightResultWin.fxml"));
                            setCurrentDialogStage(new Scene(root), "Victoire !");
                        }
                        else{
                            root = FXMLLoader.load(this.getClass().getResource("result/FightResultLoose.fxml"));
                            setCurrentDialogStage(new Scene(root), "Défaite !");
                        }
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
    private void handleMenuFightWin(Event e){
        this.doFight(FightTask.FightType.CHEAT_WIN);
    }
    @FXML
    private void handleMenuFightLoose(Event e){
        this.doFight(FightTask.FightType.CHEAT_LOOSE);
    }
    @FXML
    private void handleMenuFightRandom(Event e){
        this.doFight(FightTask.FightType.CHEAT_RANDOM);
    }
    @FXML
    private void handleMenuFightRegular(Event e){
        this.doFight(FightTask.FightType.REGULAR);
    }
    @FXML
    private void handleMenuBruteNew(Event e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("brute/CreateBrute.fxml"));
            this.setCurrentDialogStage(new Scene(root), "Nouvelle brute");
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuBruteUpdate(Event e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("brute/UpdateBrute.fxml"));
            this.setCurrentDialogStage(new Scene(root), "Modifier la brute");
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuBruteDelete(Event e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("brute/DeleteBrute.fxml"));
            this.setCurrentDialogStage(new Scene(root), "Supprimer la brute");
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void handleMenuDisconnect(Event e){
        final String token = ScenesContext.getInstance().getSession().getToken();
        final String host = ScenesContext.getInstance().getSession().getServer();
        new Thread(){
            @Override
            public void run() {
                try (NetworkClient connection = new NetworkClient(new Socket(host, Protocol.CONNECTION_PORT))) {
                    try {
                        connection.sendLogout(token);
                    } catch (InvalidResponseException | ErrorResponseException ex) {
                        Logger.getLogger(FightController.class.getName()).log(Level.WARNING, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
        this.closeCurrentDialogStage();
        ScenesContext.getInstance().destroySession();
        ScenesContext.getInstance().showLogin();
    }
    @FXML
    private void handleMenuCredits(Event e){
        try {
            Parent root = FXMLLoader.load(this.getClass().getResource("Credits.fxml"));
            this.setCurrentDialogStage(new Scene(root), "À propos...");
        } catch (IOException ex) {
            Logger.getLogger(FightController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setCurrentDialogStage(Scene scene, String title){
        this.closeCurrentDialogStage();
        this.currentDialogStage.setScene(scene);
        this.currentDialogStage.setTitle(title);
        this.currentDialogStage.show();
    }
    private void closeCurrentDialogStage(){
        this.currentDialogStage.hide();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ScaleTransitionBuilder.create()
                .node(this.centerVSshadow)
                .duration(Duration.seconds(1))
                .fromX(1)
                .fromY(1)
                .toX(1.1)
                .toY(1.1)
                .autoReverse(true)
                .cycleCount(Timeline.INDEFINITE)
                .build().play();
        TranslateTransitionBuilder.create()
                .node(this.arrowBruteNew)
                .duration(Duration.millis(500))
                .fromX(0)
                .fromY(0)
                .toX(18)
                .toY(0)
                .autoReverse(true)
                .cycleCount(Timeline.INDEFINITE)
                .build().play();
        TranslateTransitionBuilder.create()
                .node(this.arrowBruteMenu)
                .duration(Duration.millis(500))
                .fromX(0)
                .fromY(0)
                .toX(10)
                .toY(16)
                .autoReverse(true)
                .cycleCount(Timeline.INDEFINITE)
                .build().play();
        
        this.menuFightRegular.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
        this.menuFightWin.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        this.menuFightLoose.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        this.menuFightRandom.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        this.menuOptCreate.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        this.menuOptRename.setAccelerator(KeyCombination.keyCombination("Ctrl+U"));
        this.menuOptDelete.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        this.menuDisconnect.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        this.menuCredits.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));

        this.currentDialogStage = new Stage();
        this.currentDialogStage.setResizable(false);
        
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
        
        this.VS.visibleProperty().bind(me.isLoadedProperty());
        this.loadingVS.visibleProperty().bind(this.isFighting.getReadOnlyProperty());
        this.centerVS.setCursor(Cursor.HAND);
        this.centerVS.disableProperty().bind(this.isFighting.getReadOnlyProperty());
        
        this.arrowBruteMenu.visibleProperty().bind(me.isLoadedProperty().not().and(this.menuBrute.showingProperty().not()).and(this.currentDialogStage.showingProperty().not()));
        this.arrowBruteNew.visibleProperty().bind(me.isLoadedProperty().not().and(this.menuBrute.showingProperty()).and(this.currentDialogStage.showingProperty().not()));
        
        this.menuFightWin.disableProperty().bind(this.isFighting.getReadOnlyProperty().or(me.isLoadedProperty().not()));
        this.menuFightLoose.disableProperty().bind(this.isFighting.getReadOnlyProperty().or(me.isLoadedProperty().not()));
        this.menuFightRandom.disableProperty().bind(this.isFighting.getReadOnlyProperty().or(me.isLoadedProperty().not()));
        this.menuFightRegular.disableProperty().bind(this.isFighting.getReadOnlyProperty().or(me.isLoadedProperty().not()));
        
        this.menuOptCreate.disableProperty().bind(me.isLoadedProperty());
        this.menuOptRename.disableProperty().bind(me.isLoadedProperty().not());
        this.menuOptDelete.disableProperty().bind(me.isLoadedProperty().not());
    }    
}
