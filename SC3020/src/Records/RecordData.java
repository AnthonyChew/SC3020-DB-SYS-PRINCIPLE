package Records;

//Record data [18 bytes | tCosnt][4 bytes | averageRating][4 bytes | numVotes]
/**
 * The RecordData class represents a record in the database.
 * It contains information about the record's unique identifier, average rating,
 * and number of votes.
 */
public class RecordData {
    private char[] tConst; // 18 bytes
    private float averageRating;// 4 bytes
    private int numVotes;// 4 bytes

    /**
     * Constructs a new RecordData object with the specified parameters.
     *
     * @param tConst        the unique identifier of the record
     * @param averageRating the average rating of the record
     * @param numVotes      the number of votes for the record
     */
    public RecordData(String tConst, float averageRating, int numVotes) {
        this.tConst = tConst.toCharArray();
        this.averageRating = averageRating;
        this.numVotes = numVotes;
    }

    /**
     * Returns the unique identifier of the record.
     *
     * @return the unique identifier of the record
     */
    public char[] gettConst() {
        return tConst;
    }

    /**
     * Returns the average rating of the record.
     *
     * @return the average rating of the record
     */
    public float getAverageRating() {
        return averageRating;
    }

    /**
     * Returns the number of votes for the record.
     *
     * @return the number of votes for the record
     */
    public int getNumVotes() {
        return numVotes;
    }
}
