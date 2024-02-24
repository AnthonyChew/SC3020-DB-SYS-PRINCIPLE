package Records;
//Record data [18 bytes | tCosnt][4 bytes | averageRating][4 bytes | numVotes]
public class RecordData {
    private char[] tConst; //18 bytes
    private float averageRating;//4 bytes
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

    public float getAverageRating() {
        return averageRating;
    }

    public int getNumVotes() {
        return numVotes;
    }
}
