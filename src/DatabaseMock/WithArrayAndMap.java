package DatabaseMock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class WithArrayAndMap<T> {
    protected ArrayList<T> arr;
    protected HashMap<String, T> map;
    protected Scanner in;

    public WithArrayAndMap() {
        arr = new ArrayList<>();
        map = new HashMap<>();
        in = new Scanner(System.in);
    }
}
