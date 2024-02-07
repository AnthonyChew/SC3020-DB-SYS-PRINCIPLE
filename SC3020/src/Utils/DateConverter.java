package Utils;

import java.util.Calendar;
import java.util.Date;

public class DateConverter {
    private static final int YearBase = 2000;

    public static byte[] DateToByte3(Date uDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(uDate);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        year -= YearBase;

        // Encode date into an integer
        int date = (year << 10) | (month << 6) | day;

        // Convert to 3-byte array
        byte[] dateBytes = new byte[3];
        dateBytes[0] = (byte) ((date >> 16) & 0xFF);
        dateBytes[1] = (byte) ((date >> 8) & 0xFF);
        dateBytes[2] = (byte) (date & 0xFF);

        return dateBytes;
    }

    public static Date DateFromByte3(byte[] uDate) {
        int decodedDate = ((uDate[0] & 0xFF) << 16) | ((uDate[1] & 0xFF) << 8) | (uDate[2] & 0xFF);
        int decodedYear = (decodedDate >> 10) + YearBase;
        int decodedMonth = (decodedDate >> 6) & 0x0F;
        int decodedDay = decodedDate & 0x3F;

        Calendar calendar = Calendar.getInstance();
        calendar.set(decodedYear, decodedMonth, decodedDay);

        return calendar.getTime();
    }
}
