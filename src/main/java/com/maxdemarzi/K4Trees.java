package com.maxdemarzi;

import java.util.ArrayList;
import java.util.HashMap;

public class K4Trees {
    private static HashMap<String, K4Tree> trees = new HashMap<>();

    public static boolean get(String name, long from, long to) {
        if (trees.containsKey(name)) {
            return trees.get(name).get(((Long) from).intValue(), ((Long) to).intValue());
        } else {
            return false;
        }
    }

    public static ArrayList<Integer> getByX(String name, long from) {
        if (trees.containsKey(name)) {
            return trees.get(name).getByX(((Long) from).intValue());
        } else {
            return new ArrayList<>();
        }
    }

    public static ArrayList<Integer> getByY(String name, long to) {
        if (trees.containsKey(name)) {
            return trees.get(name).getByY(((Long) to).intValue());
        } else {
            return new ArrayList<>();
        }
    }

    public static void set(String name, long from, long to) {
        if (trees.containsKey(name)) {
            trees.get(name).set(((Long) from).intValue(), ((Long) to).intValue());
        } else {
            K4Tree k2 = new K4Tree();
            k2.set(((Long) from).intValue(), ((Long) to).intValue());
            trees.put(name, k2);
        }
    }

    public static void unset(String name, long from, long to) {
        if (trees.containsKey(name)) {
            trees.get(name).set(((Long) from).intValue(), ((Long) to).intValue());
        } else {
            K4Tree k2 = new K4Tree();
            k2.set(((Long) from).intValue(), ((Long) to).intValue());
            trees.put(name, k2);
        }
    }
}
