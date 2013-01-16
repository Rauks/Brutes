/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.net;

/**
 *
 * @author Karl
 */
abstract class Protocol {
    //Network datas
    public static int CONNECTION_PORT = 42666;
    
    //Codes des types primitifs
    public static int TYPE_BOOL = 0x01;
    public static int TYPE_CHAR = 0x02;
    public static int TYPE_SHORT = 0x03;
    public static int TYPE_LONG = 0x04;
    public static int TYPE_FLOAT = 0x05;
    public static int TYPE_STRING = 0x06;
    public static int Type_IMG = 0x07;
    
    //Discriminants clients > serveur
    public static int D_LOGIN = 0x80;
    public static int D_LOGOUT = 0x81;
    
    public static int D_CREATE_CHARACTER = 0x90;
    public static int D_UPDATE_CHARACTER = 0x91;
    public static int D_DELETE_CHARACTER = 0x92;
    
    public static int D_GET_MY_CHARACTER = 0xA0;
    public static int D_GET_CHALLENGER = 0xA1;
    
    public static int D_CHEAT_FIGHT_WIN = 0xB0;
    public static int D_CHEAT_FIGHT_LOOSE = 0xB1;
    public static int D_CHEAT_FIGHT_RANDOM = 0xB2;
    public static int D_DO_FIGHT = 0xB3;
    
    public static int D_GET_CHARACTER = 0xC0;
    public static int D_GET_IMAGE = 0xC2;
    public static int D_GET_BONUS = 0xC3;
    
    //Discriminants serveur > client
    public static int R_LOGIN_SUCCESS = 0x01;
    public static int R_LOGOUT_SUCCESS = 0x02;
    
    public static int R_ACTION_SUCCESS = 0x03;
    
    public static int R_CHARACTER = 0x10;
    public static int R_FIGHT_RESULT = 0x11;
    
    public static int R_DATA_CHARACTER = 0x20;
    public static int R_DATA_IMAGE = 0x21;
    public static int R_DATA_BONUS = 0x22;
    
    public static int R_ERROR = 0x40;
    public static int R_ERROR_LOGIN_NOT_FOUND = 0x41;
    public static int R_ERROR_WRONG_PASSWORD = 0x42;
    public static int R_ERROR_CREATE_CHARACTER = 0x43;
    public static int R_ERROR_UPDATE_CHARACTER = 0x44;
    public static int R_ERROR_DELETE_CHARACTER = 0x45;
    public static int R_ERROR_CHARACTER_NOT_FOUND = 0x46;
    public static int R_ERROR_IMAGE_NOT_FOUND = 0x47;
    public static int R_ERROR_BONUS_NOT_FOUND = 0x48;
    public static int R_ERROR_FIGHT = 0x49;
    public static int R_ERROR_TOKEN = 0x4A;
    public static int R_ERROR_SRLY_WTF = 0x4B;
}
