public class Record {
    //Defining fixed data length for each attribute so record size is fixed
    public char[] tConst = new char[9]; //18 bytes
    public byte[] averageRating = new byte[1];//1 byte
    public byte[] numVotes = new byte[4];//4 bytes
}
