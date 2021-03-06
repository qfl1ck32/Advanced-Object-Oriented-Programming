package DTO.Services;

import DTO.Transaction;
import DTO.TransactionItem;
import Database.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Timestamp;
import java.util.*;

import Audit.Audit;
import CurrentUser.CurrentUser;

public class TransactionService implements DTOService <Transaction> {

    private static TransactionService instance;

    private TransactionService() {}

    public static TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }

        return instance;
    }

    public Transaction selectByID(String ID) {
        Database database = Database.getInstance();

        ResultSet transactionInfo = database.sendQuery("SELECT timestamp, status, " +
                "(SELECT sum((SELECT price FROM products WHERE ID = product_ID) * quantity) " +
                "FROM transaction_history " +
                "WHERE ID = UNHEX(?)) as price " +
                "FROM transactions " +
                "WHERE transaction_ID = UNHEX(?);", Arrays.asList(ID, ID));

        String timestamp, status;
        double price;

        try {
            transactionInfo.next();

            timestamp = transactionInfo.getString("timestamp");
            status = transactionInfo.getString("status");
            price = transactionInfo.getDouble("price");
        }

        catch (SQLException exception) {
            throw new RuntimeException("Couldn't load a transaction: " + exception);
        }

        String transactionItemsQuery = "SELECT (SELECT name FROM products WHERE ID = product_ID) AS product_name, " +
                "quantity " +
                "FROM transaction_history " +
                "WHERE ID = UNHEX(?);";

        ResultSet transactionItems = database.sendQuery(transactionItemsQuery, Collections.singletonList(ID));

        List <TransactionItem> items = new ArrayList <> ();

        try {
            while (transactionItems.next()) {
                items.add(new TransactionItem(transactionItems.getString("product_name"),
                        transactionItems.getInt("quantity")));
            }
        }

        catch (SQLException exception) {
            throw new RuntimeException("Couldn't loa a transaction: " + exception);
        }

        return new Transaction(ID, timestamp, status, price, items);
    }

    public List <Transaction> getAllTransactions(String ID) {
        ResultSet transactionsIDs = Database.getInstance().sendQuery("SELECT HEX(transaction_ID) AS ID " +
                "FROM transactions " +
                "WHERE ID = UNHEX(?);", Collections.singletonList(ID));

        List <Transaction> answer = new ArrayList <> ();

        try {
            while (transactionsIDs.next()) {
                answer.add(selectByID(transactionsIDs.getString("ID")));
            }
        }

        catch (SQLException exception) {
            throw new RuntimeException("Couldn't get all transactions: " + exception);
        }

        Audit.insertLog(CurrentUser.getUser().ID(), "Get all the transactions");

        return answer;
    }

    public void placeOrder(String ID) {
        String transactionID = UUID.randomUUID().toString();

        Timestamp timestamp = DatabaseUtil.getTimestamp();

        Database database = Database.getInstance();

        String transactionQuery = "INSERT INTO transactions " +
                "VALUES(UNHEX(?), UNHEX(REPLACE(?, '-', '')), ?, ?);";

        database.sendQuery(transactionQuery, Arrays.asList(ID, transactionID, timestamp, "waiting"));

        String transactionProductsQuery = "INSERT INTO transaction_history " +
                "(SELECT UNHEX(REPLACE(?, '-', '')), product_ID, quantity " +
                "FROM cart " +
                "WHERE ID = UNHEX(?));";

        database.sendQuery(transactionProductsQuery, Arrays.asList(transactionID, ID));

        database.sendQuery("DELETE FROM cart " +
                "WHERE ID = UNHEX(?);", Collections.singletonList(ID));

        Audit.insertLog(CurrentUser.getUser().ID(), "Place order with transactionID " + transactionID);
    }

    public boolean cancelTransaction(String ID) {
        Database database = Database.getInstance();

        database.sendQuery("UPDATE transactions " +
                "SET status = 'canceled' " +
                "WHERE transaction_ID = UNHEX(?) " +
                "AND status = 'waiting';", Collections.singletonList(ID));

        boolean canceled = database.getLastModifiedRowsCount() > 0;

        String userID = CurrentUser.getUser().ID();

        if(canceled) {
            Audit.insertLog(userID, "Cancel transaction with ID " + ID);
        }

        else {
            Audit.insertLog(userID, "Try to cancel transaction with ID " + ID);
        }

        return canceled;
    }
}
