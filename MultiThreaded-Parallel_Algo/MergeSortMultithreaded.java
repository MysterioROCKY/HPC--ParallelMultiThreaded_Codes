import java.util.Random;

public class MergeSortMultithreaded {
    private static final int MAX_THREADS = 16;
    private static int[] arr;

    public static void main(String[] args) {
        arr = generateRandomArray((int) Math.pow(10, 9), (int) Math.pow(10, 3));

        System.out.println("Number of Threads | Execution Time (ms)");
        for (int i = 1; i <= MAX_THREADS; i++) {
            long startTime = System.currentTimeMillis();
            mergeSortMultithreaded(i);
            long endTime = System.currentTimeMillis();
            System.out.println(i + " | " + (endTime - startTime));
        }
    }

    private static void mergeSortMultithreaded(int numThreads) {
        MergeSortThread[] threads = new MergeSortThread[numThreads];
        int chunkSize = arr.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? arr.length - 1 : (i + 1) * chunkSize - 1;
            threads[i] = new MergeSortThread(start, end);
            threads[i].start();
        }

        for (MergeSortThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mergeChunks(numThreads);
    }

    private static void mergeChunks(int numChunks) {
        int[] tempArray = new int[arr.length];
        int chunkSize = arr.length / numChunks;
        int[][] chunks = new int[numChunks][chunkSize];
        int[] indices = new int[numChunks];

        // Copy chunks to separate arrays
        for (int i = 0; i < numChunks; i++) {
            System.arraycopy(arr, i * chunkSize, chunks[i], 0, chunkSize);
        }

        // Merge chunks
        for (int i = 0; i < arr.length; i++) {
            int minVal = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < numChunks; j++) {
                if (indices[j] < chunkSize && chunks[j][indices[j]] < minVal) {
                    minVal = chunks[j][indices[j]];
                    minIndex = j;
                }
            }
            tempArray[i] = minVal;
            indices[minIndex]++;
        }

        // Copy back to original array
        System.arraycopy(tempArray, 0, arr, 0, arr.length);
    }

    private static int[] generateRandomArray(int size, int range) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(range);
        }
        return array;
    }

    static class MergeSortThread extends Thread {
        private int start, end;

        MergeSortThread(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            mergeSort(start, end);
        }

        private void mergeSort(int start, int end) {
            if (start < end) {
                int mid = (start + end) / 2;
                mergeSort(start, mid);
                mergeSort(mid + 1, end);
                merge(start, mid, end);
            }
        }

        private void merge(int start, int mid, int end) {
            int[] temp = new int[end - start + 1];
            int i = start, j = mid + 1, k = 0;
            while (i <= mid && j <= end) {
                if (arr[i] <= arr[j]) {
                    temp[k++] = arr[i++];
                } else {
                    temp[k++] = arr[j++];
                }
            }
            while (i <= mid) {
                temp[k++] = arr[i++];
            }
            while (j <= end) {
                temp[k++] = arr[j++];
            }
            System.arraycopy(temp, 0, arr, start, temp.length);
        }
    }
}
