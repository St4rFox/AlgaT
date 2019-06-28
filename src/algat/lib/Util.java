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

    // Returns a shuffled range of type 0..<ofLength
    public static int[] getShuffledRange(int ofLength) {
        Random random = new Random();
        int[] ints = IntStream.range(0, ofLength).toArray();

        for (int i = ofLength - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int tmp = ints[index];
            ints[index] = ints[i];
            ints[i] = tmp;
        }

        return ints;
    }
}
