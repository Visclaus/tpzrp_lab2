public class Utils {
    public static int calculateStartIndex(int n, int i, int m) {
        return (int) ((n + Math.pow(2, i)) % m);
    }

    public static int calculateNodeCnt(int m) {
        return (int) Math.pow(2, m);
    }
}
