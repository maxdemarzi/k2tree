package com.maxdemarzi;

/*
    An Attempt at creating a k^2 Tree (where k = 4) that can handle Java.maxint nodes using
    roaring bitmaps as our base.
 */

import org.roaringbitmap.RoaringBitmap;

public class RoaringK2Tree {
    private int _k;
    private int _height;
    private RoaringBitmap _tree;
    private int size;
    private int[] offsets =  {0, 4, 20, 84, 340, 1364, 5460, 21844, 87380, 349524, 1398100, 5592404, 22369620, 89478484, 357913940, 1431655764};

    public RoaringK2Tree() {
        this(16);
    }

    public RoaringK2Tree(int _height) {
        this._k = 2;
        this._height = _height;
        this._tree = new RoaringBitmap();
        this.size = (int) Math.pow(_height, _k);
    }

    public boolean get(int x, int y) {
        int[] positionX = new int[_height];
        int[] positionY = new int[_height];
        int[] path = new int[_height];
        for(int i=0; i < _height; i++) {
            positionX[i] = getBit(x, _height - i);
            positionY[i] = getBit(y, _height - i);
            if (i == 0) {
                path[i] = offsets[i] + 2 * positionX[i] + positionY[i] + 1;
            } else {
                path[i] = offsets[i] + ((int) (Math.pow(_height, i)) * (2 * positionX[i - 1] + positionY[i - 1])) + 2 * positionX[i] + positionY[i] + 1;
            }
            if (!_tree.contains(path[i])) {
                return false;
            }
        }
        return  true;
    }

    public void set(int x, int y) {
        int[] positionX = new int[_height];
        int[] positionY = new int[_height];
        int[] path = new int[_height];
        for(int i=0; i < _height; i++) {
            positionX[i] = getBit(x, _height - i);
            positionY[i] = getBit(y, _height - i);
            if (i == 0) {
                path[i] = offsets[i]  + 2* positionX[i] + positionY[i] + 1;
            } else {
                path[i] = offsets[i] + ( (int)(Math.pow(_height, i)) * (2* positionX[i-1] + positionY[i-1]))  + 2* positionX[i] + positionY[i] + 1;
            }
            _tree.add(path[i]);
            //System.out.println(path[i]);
        }


       /* System.out.println("Values:");
        for(int i=0; i < _height; i++) {
            System.out.println(positionX[i] + "-" + positionY[i]);
        }

        System.out.println("Path:");
        for(int i=0; i < _height; i++) {
            System.out.println(2 * positionX[i] + positionY[i]);
        }
        */
    }

    private int getBit(int val, int height)
    {
        return (val / (int)Math.pow(2, height - 1)) % 2;
    }

}
