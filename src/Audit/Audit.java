package Audit;

import Database.*;

import java.util.Arrays;

public class Audit {
    public static void insertLog(String ID, String log) {
        Database.getInstance().sendQuery("INSERT INTO audit " +
                "VALUES (UNHEX(REPLACE(?, '-', '')), ?, ?);", Arrays.asList(ID, DatabaseUtil.getTimestamp(), log));
    }
}
