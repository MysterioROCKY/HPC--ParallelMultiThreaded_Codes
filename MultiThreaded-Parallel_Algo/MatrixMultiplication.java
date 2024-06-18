import java.util.Random;

public class MatrixMultiplication {
    static class Worker extends Thread {
        private int[][] a;
        private int[][] b;
        private int[][] c;
        private int startRow;
        private int endRow;

        public Worker(int[][] a, int[][] b, int[][] c, int startRow, int endRow) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public void run() {
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < c[0].length; j++) {
                    for (int k = 0; k < a[0].length; k++) {
                        c[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int n = Integer.parseInt(args[0]); // Matrix size
        int t = Integer.parseInt(args[1]); // Number of threads

        int[][] a = new int[n][n];
        int[][] b = new int[n][n];
        int[][] c = new int[n][n];

        // Initialize matrices A and B with random values
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = rand.nextInt(10); // Random value between 0 and 9
                b[i][j] = rand.nextInt(10); // Random value between 0 and 9
            }
        }

        // Split work among threads
        int chunkSize = (int) Math.ceil((double) n / t);
        Thread[] threads = new Thread[t];
        for (int i = 0; i < t; i++) {
            int startRow = i * chunkSize;
            int endRow = Math.min((i + 1) * chunkSize, n);
            threads[i] = new Worker(a, b, c, startRow, endRow);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        // Print result matrix C
        System.out.println("Matrix C:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(c[i][j] + " ");
            }
            System.out.println();
        }
    }
}

/*
 * import java.util.Random;
 * 
 * public class MatrixMultiplication {
 * static class Worker extends Thread {
 * private int[][] a;
 * private int[][] b;
 * private int[][] c;
 * private int startRow;
 * private int endRow;
 * 
 * public Worker(int[][] a, int[][] b, int[][] c, int startRow, int endRow) {
 * this.a = a;
 * this.b = b;
 * this.c = c;
 * this.startRow = startRow;
 * this.endRow = endRow;
 * }
 * 
 * @Override
 * public void run() {
 * for (int i = startRow; i < endRow; i++) {
 * for (int j = 0; j < c[0].length; j++) {
 * for (int k = 0; k < a[0].length; k++) {
 * c[i][j] += a[i][k] * b[k][j];
 * }
 * }
 * }
 * }
 * }
 * 
 * public static void main(String[] args) throws InterruptedException {
 * int n = Integer.parseInt(args[0]); // Matrix size
 * int t = Integer.parseInt(args[1]); // Number of threads
 * 
 * int[][] a = new int[n][n];
 * int[][] b = new int[n][n];
 * int[][] c = new int[n][n];
 * 
 * // Initialize matrices A and B with random values
 * Random rand = new Random();
 * for (int i = 0; i < n; i++) {
 * for (int j = 0; j < n; j++) {
 * a[i][j] = rand.nextInt(10); // Random value between 0 and 9
 * b[i][j] = rand.nextInt(10); // Random value between 0 and 9
 * }
 * }
 * 
 * // Start time measurement
 * long startTime = System.nanoTime();
 * 
 * // Split work among threads
 * int chunkSize = (int) Math.ceil((double) n / t);
 * Thread[] threads = new Thread[t];
 * for (int i = 0; i < t; i++) {
 * int startRow = i * chunkSize;
 * int endRow = Math.min((i + 1) * chunkSize, n);
 * threads[i] = new Worker(a, b, c, startRow, endRow);
 * threads[i].start();
 * }
 * 
 * // Wait for all threads to finish
 * for (Thread thread : threads) {
 * thread.join();
 * }
 * 
 * // End time measurement
 * long endTime = System.nanoTime();
 * 
 * // Calculate throughput time
 * double elapsedTimeSeconds = (endTime - startTime) / 1e9;
 * 
 * // Print result matrix C
 * System.out.println("Matrix C:");
 * for (int i = 0; i < n; i++) {
 * for (int j = 0; j < n; j++) {
 * System.out.print(c[i][j] + " ");
 * }
 * System.out.println();
 * }
 * 
 * // Display the throughput time
 * System.out.println("Throughput time: " + elapsedTimeSeconds + " seconds");
 * }
 * }
 * 
 */