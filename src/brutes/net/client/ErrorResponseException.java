/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net.client;

/**
 *
 * @author Karl
 */
public class ErrorResponseException extends Exception{
    public static final String LOGIN_NOT_FOUND = "Login invalide";
    public static final String WRONG_PASSWORD = "Password invalide";
    public static final String CREATE_CHARACTER = "Erreur de création du personnage";
    public static final String UPDATE_CHARACTER = "Erreur de mise à jour du personnage";
    public static final String DELETE_CHARACTER = "Erreur de supression du personnage";
    public static final String CHARACTER_NOT_FOUND = "Personnage non trouvé";
    public static final String IMAGE_NOT_FOUND = "Image non trouvée";
    public static final String BONUS_NOT_FOUND = "Bonus non trouvé";
    public static final String FIGHT = "Erreur lors du combat";
    public static final String TOKEN = "Identifiant de session invalide";
    public static final String SRLY_WTF = "Le serveur n'a pas compris la demande";
    
    public ErrorResponseException(String message){
        super(message);
    }
}
