package com.maxdemarzi;

import java.util.ArrayList;

public class K4Tree {

    private K4Tree[] _tree;
    private byte _height;
    public int[] Values;

    public K4Tree() {
        this((byte)16);
    }

    public K4Tree(byte height) {
        _height = height;
        if (_height > 0) {
            _tree = new K4Tree[16];
        }
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

        int path = (4 * bit0) + bit1;

        if (_tree[path] == null)
        {
            _tree[path] = new K4Tree((byte)(_height - 1));
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

        int path = 4 * bit0 + bit1;

        if (_tree[path] == null)
        {
            return false;
        }

        return _tree[path].get(x, y);
    }

    public void remove(int x, int y)
    {
        if (_height == 0)
        {
            // // TODO: 12/21/16 Implement cleanup
            Values = null;
            return;
        }

        int bit0 = getBit(x);
        int bit1 = getBit(y);

        int path = 4 * bit0 + bit1;

        _tree[path].remove(x, y);
    }

    public ArrayList<Integer> getByX(int x)
    {
        if (_height == 0)
        {
            return new ArrayList<Integer>() {{ add(Values[1]); } };
        }

        int bit0 = getBit(x);

        int path1 = 4 * bit0;
        int path2 = 4 * bit0 + 1;
        int path3 = 4 * bit0 + 2;
        int path4 = 4 * bit0 + 3;

        ArrayList<Integer> res = new ArrayList<>();

        if (_tree[path1] != null)
        {
            res.addAll(_tree[path1].getByX(x));
        }
        if (_tree[path2] != null)
        {
            res.addAll(_tree[path2].getByX(x));
        }
        if (_tree[path3] != null)
        {
            res.addAll(_tree[path3].getByX(x));
        }
        if (_tree[path4] != null)
        {
            res.addAll(_tree[path4].getByX(x));
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

        int path1 = bit1;
        int path2 = 4 + bit1;
        int path3 = 8 + bit1;
        int path4 = 12 + bit1;

        ArrayList<Integer> res = new ArrayList<>();

        if (_tree[path1] != null)
        {
            res.addAll(_tree[path1].getByY(y));
        }
        if (_tree[path2] != null)
        {
            res.addAll(_tree[path2].getByY(y));
        }
        if (_tree[path3] != null)
        {
            res.addAll(_tree[path3].getByY(y));
        }
        if (_tree[path4] != null)
        {
            res.addAll(_tree[path4].getByY(y));
        }

        return res;
    }

    private int getBit(int val)
    {
        return (val / (int)Math.pow(4, _height - 1)) % 4;
    }
}
