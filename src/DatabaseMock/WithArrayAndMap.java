package DatabaseMock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class WithArrayAndMap<T> {
    protected ArrayList<T> itemsArray;
    protected HashMap<String, T> itemsMap;
    protected Scanner scanner;

    public WithArrayAndMap() {
        itemsArray = new ArrayList<>();
        itemsMap = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public ArrayList<T> getItemsArray() {
        return new ArrayList <T> (itemsArray);
    }

    public HashMap<String, T> getItemsMap() {
        return new HashMap <String, T> (itemsMap);
    }
}
