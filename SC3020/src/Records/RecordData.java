package Records;

public class RecordData {
    private char[] tConst; //18 bytes
    private float averageRating;//1 byte //change to float
    private int numVotes;//4 bytes

    // Constructor
    public RecordData(String tConst, float averageRating, int numVotes) {
        this.tConst = tConst.toCharArray();
        this.averageRating = averageRating;
        this.numVotes = numVotes;
    }

    // Getter
    public char[] gettConst() {
        return tConst;
    }

    public void settConst(char[] tConst) {
        this.tConst = tConst;
    }

    public float getAverageRating() {
        return averageRating;
    }

    public int getNumVotes() {
        return numVotes;
    }
}
