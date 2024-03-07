package Utils;

public class ByteUtils {
    /**
     * Sets a bit to 1.
     *
     * @param value    the byte value
     * @param position the position of the bit to set
     * @return the byte value with the specified bit set to 1
     */
    public static byte setBit(byte value, int position) {
        return (byte) (value | (1 << position));
    }

    /**
     * Sets a bit to 0.
     *
     * @param value    the byte value
     * @param position the position of the bit to clear
     * @return the byte value with the specified bit set to 0
     */
    public static byte clearBit(byte value, int position) {
        return (byte) (value & ~(1 << position));
    }

    /**
     * Toggles a bit.
     *
     * @param value    the byte value
     * @param position the position of the bit to toggle
     * @return the byte value with the specified bit toggled
     */
    public static byte toggleBit(byte value, int position) {
        return (byte) (value ^ (1 << position));
    }

    /**
     * Checks if a bit is set to 1.
     *
     * @param value    the byte value
     * @param position the position of the bit to check
     * @return true if the specified bit is set to 1, false otherwise
     */
    public static boolean isBitSet(byte value, int position) {
        return (value & (1 << position)) != 0;
    }

    /**
     * Returns the position of the first bit that is 0.
     *
     * @param value the byte value
     * @return the position of the first 0 bit, or -1 if all bits are set to 1
     */
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

    /**
     * Checks if all bits are set to 1.
     *
     * @param value        the byte value
     * @param numberOfBits the number of bits to check
     * @return true if all bits are set to 1, false otherwise
     */
    public static boolean isBitAllOne(byte value, int numberOfBits) {
        byte mask = (byte) ((1 << numberOfBits) - 1);
        return (value & mask) == mask;
    }
}
