package algat.hashtable;

import algat.utils.Util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import static java.lang.Integer.parseInt;

public enum Hasher {
    NAIVE {
        @Override
        public int hash(String key, int modulus) {
            return (key.charAt(0) - 97) % modulus;
        }
    },

    EXTRACTION {
        @Override
        public int hash(String key, int modulus) {
            String binary = Util.toBinary(key);
            int middle = binary.length() / 2;
            return parseInt(binary.subSequence(middle - 4,middle + 4).toString(),2) % modulus;
        }
    },

    XOR {
        @Override
        public int hash(String key, int modulus) {
            int xorEsaminated = 0;
            for(char a : key.toCharArray()){
                xorEsaminated ^= (int)a;
            }
            return xorEsaminated % modulus;
        }
    },

    MULTIPLICATION {
        @Override
        public int hash(String key, int modulus) {
            return 3;
        }
    },

    DIVISION {
        @Override
        public int hash(String key, int modulus) {
            Integer h = 0;
            for (int i = 0; i < key.length(); i++) {
                h = (64 * h + key.charAt(i)) % 383;
            }
            return h % modulus;
        }
    };

    public abstract int hash(String key, int modulus);
}
