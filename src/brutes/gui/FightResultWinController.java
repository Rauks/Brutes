/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Karl
 */
public class FightResultWinController implements Initializable {
    @FXML
    private Text text;
    
    @FXML
    private void handleContinueAction(ActionEvent e){
        Node source = (Node) e.getSource(); 
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
