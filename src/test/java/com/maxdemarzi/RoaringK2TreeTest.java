package com.maxdemarzi;

import org.junit.Assert;
import org.junit.Test;

public class RoaringK2TreeTest {

    @Test
    public void shouldBasicallyWork() {
        RoaringK2Tree rk2Tree = new RoaringK2Tree();
        rk2Tree.set(1, 6);
        rk2Tree.set(2, 5);

        Assert.assertTrue(rk2Tree.get(1, 6));
        Assert.assertTrue(rk2Tree.get(2, 5));
        Assert.assertFalse(rk2Tree.get(1, 5));
    }
}
