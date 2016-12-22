package com.maxdemarzi;

import java.util.HashMap;

public class K2Trees {
    private static HashMap<String, K2Tree> trees = new HashMap<>();

    public static boolean get(String name, long from, long to) {
        if (trees.containsKey(name)) {
            return trees.get(name).get(((Long) from).intValue(), ((Long) to).intValue());
        } else {
            return false;
        }
    }


    public static void set(String name, long from, long to) {
        if (trees.containsKey(name)) {
            trees.get(name).set(((Long) from).intValue(), ((Long) to).intValue());
        } else {
            K2Tree k2 = new K2Tree();
            k2.set(((Long) from).intValue(), ((Long) to).intValue());
            trees.put(name, k2);
        }
    }

    public static void unset(String name, long from, long to) {
        if (trees.containsKey(name)) {
            trees.get(name).set(((Long) from).intValue(), ((Long) to).intValue());
        } else {
            K2Tree k2 = new K2Tree();
            k2.set(((Long) from).intValue(), ((Long) to).intValue());
            trees.put(name, k2);
        }
    }
}
