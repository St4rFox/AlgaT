package algat.hashtable;

public enum Hasher {
    NAIVE {
        @Override
        public int hash(String key, int modulus) {
            return (key.toLowerCase().charAt(0) - 97) % modulus;
        }

        @Override
        public String toString() {
            return "Naive";
        }
    },

    EXTRACTION {
        @Override
        public int hash(String key, int modulus) {
            return 1;
        }

        @Override
        public String toString() {
            return "Extraction";
        }
    },

    XOR {
        @Override
        public int hash(String key, int modulus) {
            return 2;
        }

        @Override
        public String toString() {
            return "Xor";
        }
    },

    MULTIPLICATION {
        @Override
        public int hash(String key, int modulus) {
            return 3;
        }

        @Override
        public String toString() {
            return "Multiplication";
        }
    },

    DIVISION {
        @Override
        public int hash(String key, int modulus) {
            return 4;
        }

        @Override
        public String toString() {
            return "Division";
        }
    };

    public abstract int hash(String key, int modulus);
}
