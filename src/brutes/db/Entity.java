/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brutes.db;

import brutes.Brutes;
import brutes.db.SQL;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thiktak
 */
abstract public class Entity {

    public String tableName;

    protected ArrayList<Field> getAnnotedFields(Class c) {
        ArrayList<Field> fields = new ArrayList<>();

        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(SQL.class)) {
                fields.add(field);
            }
        }

        return fields;
    }

    abstract public int getId();

    public void save() throws Exception {
        Class current = this.getClass().asSubclass(Entity.class);

        Logger.getLogger(Entity.class.getName()).log(Level.INFO, "{0}::save()", current.getName());

        ArrayList<Field> fields = this.getAnnotedFields(current);
        int nbFields = fields.size();

        String tableName = (String) current.getField("tableName").get(this); // @TODO
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName != null ? tableName : (current.getSimpleName() + "s")).append(" SET");

        int f = 0;

        for (Field field : fields) {
            field.setAccessible(true);
            SQL info = field.getAnnotation(SQL.class);

            sql.append(" " + info.name() + " = ?");

            if (++f < nbFields) {
                sql.append(", ");
            }
        }

        sql.append(" WHERE id = ?");

        System.out.println(sql.toString());
        PreparedStatement prepare = DatasManager.prepare(sql.toString());

        f = 1;
        for (Field field : fields) {
            field.setAccessible(true);
            SQL info = field.getAnnotation(SQL.class);

            switch (info.type()) {
                case "int":
                    if (field.get(this) instanceof Entity) {
                        prepare.setInt(f++, ((Entity) field.get(this)).getId());
                        System.out.println("int#id: " + ((Entity) field.get(this)).getId());
                    } else {
                        prepare.setInt(f++, field.getInt(this));
                        System.out.println("int: " + (field.getInt(this)));
                    }
                    break;
                case "varchar":
                case "text":
                    prepare.setString(f++, (String) field.get(this));
                    System.out.println("varchar: " + ((String) field.get(this)));
                    break;
                default:
                    throw new Exception("Not yet supported");
            }
        }


        prepare.setInt(f++, this.getId());
        //System.out.println("int: " + (idField.getInt(this)));
        prepare.executeUpdate(); // TODO Bug return int ?
    }

    private String getTableName() {
        return tableName;
    }
}
