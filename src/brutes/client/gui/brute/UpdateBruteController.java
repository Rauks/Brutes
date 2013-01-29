package brutes.client.gui.brute;

import brutes.client.ScenesContext;
import brutes.client.gui.FightController;
import brutes.client.gui.LoginController;
import brutes.client.net.ErrorResponseException;
import brutes.client.net.InvalidResponseException;
import brutes.client.net.NetworkClient;
import brutes.net.Protocol;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karl
 */
public class UpdateBruteController implements Initializable {
    @FXML
    private TextField bruteName;
    
    @FXML
    private void handleCancelAction(ActionEvent e){
        this.closeStage(e);
    }
    
    @FXML
    private void handleSubmitAction(ActionEvent e){
        new Thread(){
            @Override
            public void run() {
                try (NetworkClient connection = new NetworkClient(new Socket(ScenesContext.getInstance().getSession().getServer(), Protocol.CONNECTION_PORT))) {
                    try {
                        connection.sendUpdateBrute(ScenesContext.getInstance().getSession().getToken(), bruteName.getText());
                    } catch (InvalidResponseException | ErrorResponseException ex) {
                        Logger.getLogger(FightController.class.getName()).log(Level.WARNING, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                ScenesContext.getInstance().getSession().netLoadMyBrute();
            }
        }.start();
        this.closeStage(e);
    }
    
    private void closeStage(Event e){
        Node source = (Node) e.getSource(); 
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.bruteName.setText(ScenesContext.getInstance().getSession().getMyBrute().getNameProperty().getValue());
    }    
}
