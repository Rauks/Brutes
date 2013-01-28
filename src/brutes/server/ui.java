package brutes.server;

import brutes.server.db.DatasManager;

/**
 *
 * @author Thiktak
 */
public class ui {

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static boolean random() {
        return ui.random(0, 1) == 1;
    }

    public static String getClassPath(Class aClass) {
        String name = aClass.getName();
        String value = name.substring(0, name.lastIndexOf(DatasManager.class.getSimpleName()));
        return value.substring(0, value.length() - 1);
    }

    public static int lengthObjects(Object[] objects) {
        int length = 0;
        for (Object obj : objects) {
            if (obj != null) {
                length++;
            }
        }
        return length;
    }

    public static int randomMiddle(int i, double d) {
        return ui.random((int) (i - d * i), (int) (i + d * i));
    }
}