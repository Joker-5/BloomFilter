package com.joker;

import java.util.BitSet;

public class BloomFilter<T> {
    // 默认容量
    private static final int DEFAULT_SIZE = 1 << 30;

    // hash随机种子
    private static final int[] SEEDS = {3, 13, 46, 71, 91, 134};

    private BitSet bitSet;

    // hash func
    private HashFunc<T>[] func;

    public BloomFilter() {
        bitSet = new BitSet(DEFAULT_SIZE);
        func = new HashFunc[SEEDS.length];
        for (int i = 0; i < func.length; i++) {
            func[i] = new HashFunc(SEEDS[i], DEFAULT_SIZE);
        }
    }

    // 添加元素
    public void add(T value) {
        for (HashFunc<T> f : func) {
            bitSet.set(f.hash(value), true);
        }
    }

    // 判断元素是否在filter中
    public boolean contains(T value) {
        for (HashFunc<T> f : func) {
            if (!bitSet.get(f.hash(value))) {
                return false;
            }
        }
        return true;
    }

    private static class HashFunc<T> {
        private final int seed;
        private final int capacity;

        public HashFunc(int seed, int capacity) {
            this.seed = seed;
            this.capacity = capacity;
        }

        public int hash(T value) {
            int h;
            return (value == null) ? 0 : Math.abs(seed * (capacity - 1) & ((h = value.hashCode())) ^ (h >>> 16));
        }
    }

}

