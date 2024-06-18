import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
public class mpp2 {
    private static final int MATRIX_SIZE = 2048;
    private static int[][] multiply(int[][] a, int[][] b) {
        int[][] result = new int[MATRIX_SIZE][MATRIX_SIZE];
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                for (int k = 0; k < MATRIX_SIZE; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }
    private static void parallelPower(int[][] A, int[][] B, int exponent, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int[][] result = new int[MATRIX_SIZE][MATRIX_SIZE];
        AtomicReference<int[][]> resultRef = new AtomicReference<>(result);
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                final int row = i;
                final int col = j;
                executor.execute(() -> {
                    int[][] localA = A;
                    int[][] localB = B;
                    int[][] localResult = resultRef.get();
                    int temp = 0;
                    for (int k = 0; k < MATRIX_SIZE; k++) {
                        temp += localA[row][k] * localB[k][col];
                    }
                    localResult[row][col] = temp;
                });
            }
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = resultRef.get();
        // Multiply the result with B to compute B^exponent
        for (int i = 1; i < exponent; i++) {
            result = multiply(result, B);
        }
        // Output the result or do something with it
    }
    public static void main(String[] args) {
        int[][] A = new int[MATRIX_SIZE][MATRIX_SIZE];
        int[][] B = new int[MATRIX_SIZE][MATRIX_SIZE];
        // Initialize matrices A and B here
        long startTime, endTime;
        int arr[]={1,2,4,6,8,10,12,14,16};
        for (int i=0;i<9;i++) {
            int numThreads=arr[i];
            System.out.println("Number of threads: " + numThreads);
            startTime = System.currentTimeMillis();
            parallelPower(A, B, 15, numThreads);
            endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + " milliseconds\n");
        }
    }
}

