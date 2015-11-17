package aiproj.checkers.game;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mknutsen on 11/17/15.
 */
public class Test {

    public static int numInRange(List<Integer> item, int low, int high) {
        if (item.isEmpty()) {
            return 0;
        } else {
            int removed = item.remove(0);
            if (removed >= low && removed <= high) {
                return numInRange(item, low, high) + 1;
            } else {
                return numInRange(item, low, high);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(numInRange(Arrays.asList(1, 2, 3, 4, 5, 6), 2, 4));
    }
}
