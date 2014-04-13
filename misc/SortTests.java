import java.util.Arrays;

public class SortTests {
    public static void LsdIntSort(int[] a, int w) {
        int radix = 2;
        int n = a.length;
        int[] aux = new int[n];
        
        int bit;
        for (int d = 0; d < w; d++) {         
            int[] count = new int[radix + 1];
            for (int i = 0; i < n; i++) {           
                bit = (a[i] & (1 << d)) == 0 ? 0 : 1;           
                count[bit + 1]++;
            }
            
            for (int r = 0; r < radix; r++) {
                count[r + 1] += count[r];
            }
            
            for (int i = 0; i < n; i++) {
                bit = (a[i] & (1 << d)) == 0 ? 0 : 1;           
                aux[count[bit]++] = a[i];
            }
            
            for (int i = 0; i < n; i++) {
                a[i] = aux[i];
            }
        }
    }
    
    public static void LsdIntSort2(int[] a, int w, int bits) {
        int radix = (int) Math.pow(2, bits);
        int mask = radix - 1;
        int n = a.length;
        int[] aux = new int[n];
        
        int bit;
        for (int d = 0; d < w; d+=bits) {         
            int[] count = new int[radix+1];
            for (int i = 0; i < n; i++) {           
                bit = (a[i] >> d) & mask;
                count[bit + 1]++;
            }
            
            for (int r = 0; r < radix; r++) {
                count[r + 1] += count[r];
            }
            
            for (int i = 0; i < n; i++) {
                bit = (a[i] >> d) & mask;
                aux[count[bit]++] = a[i];
            }
            
            for (int i = 0; i < n; i++) {
                a[i] = aux[i];
            }
        }
    }
    
    public static void main (String[] args) {
        int n = 1000000;
        int[] a1 = new int[n];
        int[] a2 = new int[n];
        int[] a3 = new int[n];
        
        for (int i = 0; i < n; i++) {
            int r = StdRandom.uniform(Integer.MAX_VALUE);
            a1[i] = r;
            a2[i] = r;
            a3[i] = r;
        }
        
        long startTime = System.nanoTime();
        Arrays.sort(a1);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        StdOut.println("Arrays.sort: " + duration);
        
        startTime = System.nanoTime();
        LsdIntSort(a2, 32);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        StdOut.println("LsdIntSort: " + duration);
        
        startTime = System.nanoTime();
        LsdIntSort2(a3, 32, 16);
        endTime = System.nanoTime();
        duration = endTime - startTime;
        StdOut.println("LsdIntSort2: " + duration);
        
        assert Arrays.equals(a1, a2);
        assert Arrays.equals(a1, a3);
    }
}