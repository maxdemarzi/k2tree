package com.maxdemarzi;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class K4TreeTest {

    @Test
    public void shouldBasicallyWork() {
        byte height = 6;
        K4Tree k4Tree = new K4Tree(height);

        Random rand = new Random();
        Integer  key1 = rand.nextInt(1000);
        Integer  key2 = rand.nextInt(1000);

        k4Tree.set(key1, key2);

        boolean actual = k4Tree.get(key1, key2);

        Assert.assertTrue(actual);

        int key3 = (key2 + 1) % 1000;
        k4Tree.set(key1, key3);
        Integer actual1 = k4Tree.getByX(key1).get(0);
        Assert.assertEquals(actual1, key2);

        Assert.assertTrue(k4Tree.getByX(key1).size() == 2);
        Assert.assertTrue(k4Tree.getByX(key1).contains(key2));
        Assert.assertTrue(k4Tree.getByX(key1).contains(key3));


        k4Tree.remove(key1, key2);
        actual = k4Tree.get(key1, key2);
        Assert.assertFalse(actual);
    }

    @Test
    public void shouldReallyWork() {
        K4Tree k4Tree = new K4Tree();

        Random rand = new Random();
        HashMap<Integer, Integer> check = new HashMap<>();

        for (int i = 0; i < 10000; i++) {
            Integer  key1 = rand.nextInt(1000000);
            Integer  key2 = rand.nextInt(1000000);
            if (check.containsKey(key1)) {
                continue;
            }
            k4Tree.set(key1, key2);
            check.put(key1, key2);

            boolean actual = k4Tree.get(key1, key2);

            Assert.assertTrue(actual);
        }

        for (Integer key : check.keySet()) {
            Integer expected1 = check.get(key);

            Integer actual1 = k4Tree.getByX(key).get(0);
            ArrayList<Integer> actual2 = k4Tree.getByY(expected1);

            Assert.assertEquals(actual1, expected1);
            Assert.assertTrue(actual2.contains(key));
        }

    }
}
