package com.maxdemarzi;

import java.util.ArrayList;

public class K2Tree {

    private K2Tree[] _tree;
    private byte _height;
    public int[] Values;

    public K2Tree(byte height) {
        _height = height;
    }

    public void set(int x, int y) {
        if (_height == 0)
        {
            Values = new int[2];
            Values[0] = x;
            Values[1] = y;
            return;
        }

        int bit0 = getBit(x);
        int bit1 = getBit(y);

        int path = 2 * bit0 + bit1;
        if (_tree == null)
        {
            _tree = new K2Tree[4];
        }
        if (_tree[path] == null)
        {
            _tree[path] = new K2Tree((byte)(_height - 1));
        }

        _tree[path].set(x, y);
    }

    public boolean get(int x, int y)
    {
        if (_height == 0)
        {
            return (Values != null);
        }

        int bit0 = getBit(x);
        int bit1 = getBit(y);

        int path = 2 * bit0 + bit1;

        if (_tree[path] == null)
        {
            return false;
        }

        return _tree[path].get(x, y);
    }

    public ArrayList<Integer> getByX(int x)
    {
        if (_height == 0)
        {
            return new ArrayList<Integer>() {{ add(Values[1]); } };
        }

        int bit0 = getBit(x);

        int path1 = 2 * bit0;
        int path2 = 2 * bit0 + 1;


        ArrayList<Integer> res = new ArrayList<>();

        if (_tree[path1] != null)
        {
            res.addAll(_tree[path1].getByX(x));
        }
        if (_tree[path2] != null)
        {
            res.addAll(_tree[path2].getByX(x));
        }

        return res;
    }

    public ArrayList<Integer> getByY(int y)
    {
        if (_height == 0)
        {
            return new ArrayList<Integer>() {{ add(Values[0]); } };
        }

        int bit1 = getBit(y);

        int path1 = 2 * 0 + bit1;
        int path2 = 2 * 1 + bit1;

        ArrayList<Integer> res = new ArrayList<>();

        if (_tree[path1] != null)
        {
            res.addAll(_tree[path1].getByY(y));
        }
        if (_tree[path2] != null)
        {
            res.addAll(_tree[path2].getByY(y));
        }

        return res;
    }

    private int getBit(int val)
    {
        return (val / (int)Math.pow(2, _height - 1)) % 2;
    }

}
