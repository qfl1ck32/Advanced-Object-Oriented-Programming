package Database;

import java.sql.Timestamp;
import java.util.Date;

public class DatabaseUtil {
    public static Timestamp getTimestamp() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }
}
