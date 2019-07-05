package algat.lib.hashtable;

import algat.lib.Util;

import static java.lang.Integer.parseInt;
import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

public enum Hasher {
    NAIVE {
        @Override
        public int hash(String key, int modulus) {
            return key.charAt(0) % modulus;
        }

        @Override
        public String toString() {
            return "Naive";
        }
    },

    EXTRACTION {
        @Override
        public int hash(String key, int modulus) {
            String binary = Util.toBinary(key);
            int middle = binary.length() / 2;
            return parseInt(binary.subSequence(middle - 4,middle + 4).toString(),2) % modulus;
        }

        @Override
        public String toString() {
            return "Extraction";
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

        @Override
        public String toString() {
            return "Xor";
        }
    },

    MULTIPLICATION {
        @Override
        public int hash(String key, int modulus) {
            Integer i = 0;
            for (int j = 0; j < key.length(); j++) {
                i = (64 * i + key.charAt(j));
            }
            return (int)floor(256*(i*((sqrt(5)-1)/2) - floor(i*((sqrt(5)-1)/2))));
        }

        @Override
        public String toString() {
            return "Multiplication";
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

        @Override
        public String toString() {
            return "Division";
        }
    };

    public abstract int hash(String key, int modulus);
}
