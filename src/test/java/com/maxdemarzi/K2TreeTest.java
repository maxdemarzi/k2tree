package com.maxdemarzi;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class K2TreeTest {

    @Test
    public void shouldBasicallyWork() {
        byte height = 32;
        K2Tree k2Tree = new K2Tree(height);

        Random rand = new Random();
        Integer  key1 = rand.nextInt(1000);
        Integer  key2 = rand.nextInt(1000);

        k2Tree.set(key1, key2);

        boolean actual = k2Tree.get(key1, key2);

        Assert.assertTrue(actual);

        k2Tree.remove(key1, key2);
        actual = k2Tree.get(key1, key2);
        Assert.assertFalse(actual);
    }

    @Test
    public void shouldReallyWork() {
        byte height = 32;
        K2Tree k2Tree = new K2Tree(height);

        Random rand = new Random();
        HashMap<Integer, Integer> check = new HashMap<>();

        for (int i = 0; i < 10000; i++) {
            Integer  key1 = rand.nextInt(1000000);
            Integer  key2 = rand.nextInt(1000000);
            if (check.containsKey(key1)) {
                continue;
            }
            k2Tree.set(key1, key2);
            check.put(key1, key2);

            boolean actual = k2Tree.get(key1, key2);

            Assert.assertTrue(actual);
        }

        for (Integer key : check.keySet()) {
            Integer expected1 = check.get(key);

            Integer actual1 = k2Tree.getByX(key).get(0);
            ArrayList<Integer> actual2 = k2Tree.getByY(expected1);

            Assert.assertEquals(actual1, expected1);
            Assert.assertTrue(actual2.contains(key));
        }

    }

}
