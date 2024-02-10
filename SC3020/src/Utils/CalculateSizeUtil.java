package Utils;

import java.util.Date;

public class CalculateSizeUtil {
    public static int getSize(Object obj) {
        int size = 0;
        if (obj instanceof Byte) {
            size = Byte.SIZE/8;
        } else if (obj instanceof Short) {
            size = Short.SIZE/8;
        } else if (obj instanceof Integer) {
            size = Integer.SIZE/8;
        } else if (obj instanceof Long) {
            size = Long.SIZE/8;
        } else if (obj instanceof Character) {
            size = Character.SIZE/8;
        } else if (obj instanceof Float) {
            size = Float.SIZE/8;
        } else if (obj instanceof Double) {
            size = Double.SIZE/8;
        }
        return size;
    }
}
