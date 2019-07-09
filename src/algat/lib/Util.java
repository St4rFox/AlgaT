package algat.lib;

import java.util.Random;
import java.util.stream.IntStream;

public class Util {

    public static String toBinary(String key) {
        byte[] bytes = key.getBytes();
        StringBuilder binary = new StringBuilder();

        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }
}
