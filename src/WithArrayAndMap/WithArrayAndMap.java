package WithArrayAndMap;

import java.util.ArrayList;
import java.util.HashMap;

public class WithArrayAndMap<T> {
    protected ArrayList<T> itemsArray;
    protected HashMap<String, T> itemsMap;

    public WithArrayAndMap() {
        itemsArray = new ArrayList<>();
        itemsMap = new HashMap<>();
    }
}
