/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.client.gui.result;

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
    private final String[] sentences = {
        "La victoire aime l'effort.", 
        "Ce n'est pas la trempe des armes, mais celle du cœur qui donne la victoire.", 
        "Tout soldat est grand dans un jour de victoire.", 
        "À bien patienter consiste la victoire.", 
        "La victoire est à qui tient un quart d'heure de plus.", 
        "Qui veut vaincre est déjà bien près de la victoire.", 
        "Qui sait se vaincre dans la victoire est deux fois vainqueur.", 
        "Tout soldat s'ennoblit le jour d'une victoire.", 
        "La victoire ne veut point de rivalité.",
        "Chaque pas est une victoire."
    };
    
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
        this.text.setText(this.sentences[(int)(Math.random() * this.sentences.length)]);
    }    
}
