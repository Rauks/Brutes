package brutes.client.gui;


import brutes.Brutes;
import brutes.client.ScenesContext;
import brutes.client.net.NetworkClient;
import brutes.client.user.Session;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Karl
 */
public class LoginController implements Initializable {
    @FXML
    private Button connexion;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private TextField server;
    @FXML
    private ProgressIndicator loading;
    @FXML
    private Text loginError;
    @FXML
    private Text passwordError;
    @FXML
    private Text serverError;
    
    private void setDisableForm(boolean disable){
        this.loading.setVisible(disable);
        this.login.setDisable(disable);
        this.password.setDisable(disable);
        this.server.setDisable(disable);
        this.connexion.setDisable(disable);
    }
    
    @FXML
    private void handleConnexionAction(ActionEvent e){
        this.login();
    }
    
    @FXML
    private void handleCloseAction(ActionEvent e){
        Brutes.exit();
    }
    
    private synchronized void login(){
        this.setDisableForm(true);
        
        final LoginTask loginTask = new LoginTask(this.server.getText(), this.login.getText(), this.password.getText());
        this.loginError.visibleProperty().bind(loginTask.getLoginErrorProperty());
        this.passwordError.visibleProperty().bind(loginTask.getPasswordErrorProperty());
        this.serverError.visibleProperty().bind(loginTask.getHostErrorProperty());

        loginTask.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State newState) {
                if(newState == Worker.State.SUCCEEDED){
                    loginTask.cancel();
                    ScenesContext.getInstance().showFight();
                    setDisableForm(false);
                }
                else if(newState == Worker.State.FAILED){
                    loginTask.cancel();
                    setDisableForm(false);
                }
            }   
        });
        new Thread(loginTask).start();
    }
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
