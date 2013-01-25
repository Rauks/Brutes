package brutes.server;

/**
 *
 * @author Thiktak
 */
public class ui {
    public static int random(int min, int max) {
        return (int) ( Math.random() * (max-min+1) + min );
    }
}
