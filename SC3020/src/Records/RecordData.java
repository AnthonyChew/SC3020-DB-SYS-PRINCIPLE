package Records;

import Utils.IntConverter;

import java.nio.ByteBuffer;

public class RecordData {
<<<<<<< Updated upstream
    private char[] tConst; // 9 characters x 2 bytes -> 18 bytes
    private byte averageRating; // 1 byte
    private byte[] numVotes; // 4 bytes
=======
    private char[] tConst; //18 bytes
    private byte averageRating;//1 byte //change to float
    private byte[] numVotes;//4 bytes
>>>>>>> Stashed changes

    // Getter
    public char[] gettConst() {
        return tConst;
    }

    public void settConst(char[] tConst) {
        this.tConst = tConst;
    }

    public byte getAverageRating() {
        return averageRating;
    }

    public byte[] getNumVotes() {
        return numVotes;
    }

    // Constructor
    public RecordData(String tConst, float averageRating, int numVotes) {
        this.tConst = tConst.toCharArray();
        this.averageRating = (byte) ((int) (averageRating * 10));
        this.numVotes = IntConverter.IntToByte(numVotes, 4);
    }
}