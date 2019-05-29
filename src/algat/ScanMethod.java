package algat;


public enum ScanMethod {
    LINEARE {
        @Override
        public int scan() {
            return 1;
        }

        @Override
        public String toString() {
            return "Scansione Lineare";
        }

    },

    QUADRATICA {
        @Override
        public int scan() {
            //TODO
            return 2;
        }

        @Override
        public String toString() {
            return "Scansione Quadratica";
        }

    },

    PSEUDOCASUALE {
        @Override
        public int scan() {
            //TODO
            return 3;
        }

        @Override
        public String toString() {
            return "Scansione Pseudocasuale";
        }

    },

     HASHING_DOPPIO {
         @Override
         public int scan() {
             //TODO
             return 4;
         }

         @Override
         public String toString() {
             return "Hashing Doppio";
         }

    };

    public abstract int scan();
}
