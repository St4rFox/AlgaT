package algat.hashtable;

public enum Hasher {
    NAIVE {
        @Override
        public int hash(String key, int modulus) {
            return (key.toLowerCase().charAt(0) - 97) % modulus;
        }
    },

    EXTRACTION {
        @Override
        public int hash(String key, int modulus) {
            return 1;
        }
    },

    XOR {
        @Override
        public int hash(String key, int modulus) {
            return 2;
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
            return 4;
        }
    };

    public abstract int hash(String key, int modulus);
}
