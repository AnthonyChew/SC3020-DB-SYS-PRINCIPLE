package Utils;

import java.nio.ByteBuffer;

public class IntConverter {
    public static byte IntToByte(int num) {
        return (byte) num;
    }

    public static byte[] IntToByte(int num, int byteNum) {
        return ByteBuffer.allocate(byteNum).putInt(num).array();
    }

    public static int ByteToInt(byte b) {
        return (int) b;
    }

    public static int ByteToInt(byte[] b) {
        ByteBuffer buffer = ByteBuffer.wrap(b);
        return buffer.getInt();
    }
}
