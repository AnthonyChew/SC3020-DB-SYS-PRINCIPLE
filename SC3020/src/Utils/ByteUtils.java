package Utils;

public class ByteUtils {
    // Sets a bit to 1
    public static byte setBit(byte value, int position) {
        return (byte) (value | (1 << position));
    }

    // Set a bit to 0
    public static byte clearBit(byte value, int position) {
        return (byte) (value & ~(1 << position));
    }

    // Toggles a bit
    public static byte toggleBit(byte value, int position) {
        return (byte) (value ^ (1 << position));
    }

    // Checks if a bit is 1
    public static boolean isBitSet(byte value, int position) {
        return (value & (1 << position)) != 0;
    }

    // Returns the position of the first bit that is 0
    public static int findFirstZeroBitPosition(byte value) {
        for (int i = 0; i < 8; i++) {
            if ((value & (1 << i)) == 0) {
                // Return the position as soon as a 0 bit is found
                return i;
            }
        }
        // Return -1 if all bits are set to 1
        return -1;
    }

    // Return true if all bit is 1
    public static boolean isBitAllOne(byte value, int numberOfBits) {
        byte mask = (byte)((1 << numberOfBits) - 1);
        return (value & mask) == mask;
    }
}
