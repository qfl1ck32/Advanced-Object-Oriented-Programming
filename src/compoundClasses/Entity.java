package compoundClasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Entity <T> {
    protected ArrayList <T> arr;
    protected HashMap <String, T> idMap;
    protected Scanner in;
    
    public Entity() {
        arr = new ArrayList <> ();
        idMap = new HashMap <> ();
        in = new Scanner(System.in);
    }
}
