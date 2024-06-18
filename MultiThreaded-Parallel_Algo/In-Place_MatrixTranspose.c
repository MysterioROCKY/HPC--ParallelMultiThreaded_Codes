#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define BASE_CASE_THRESHOLD 64

// Struct to pass parameters to the thread function
typedef struct {
    int** matrix;
    int n;
    int start_row;
    int end_row;
} ThreadArgs;

// Function prototypes
void in_place_transpose(int** matrix, int n, int num_threads);
void* in_place_transpose_tile(void* arg);
void print_matrix(int** matrix, int n);

// Main function
int main(int argc, char* argv[]) {
    if (argc != 2) {
        printf("Usage: %s <num_threads>\n", argv[0]);
        return 1;
    }
    int num_threads = atoi(argv[1]); // Number of threads from command-line argument
    if (num_threads <= 0) {
        printf("Invalid number of threads\n");
        return 1;
    }
    
    int n;
    printf("Enter the size of the matrix: ");
    scanf("%d", &n);
    srand(time(NULL)); // Seed for random number generation

    // Allocate memory for the matrix
    int** matrix = (int**)malloc(n * sizeof(int*));
    for (int i = 0; i < n; ++i) {
        matrix[i] = (int*)malloc(n * sizeof(int));
        for (int j = 0; j < n; ++j) {
            // Initialize matrix elements randomly
            matrix[i][j] = rand() % 100; // Random numbers between 0 and 99
        }
    }

    // Print the original matrix
    printf("Original Matrix:\n");
    print_matrix(matrix, n);

    // Transpose the matrix in place
    in_place_transpose(matrix, n, num_threads);

    // Print the transposed matrix
    printf("\nTransposed Matrix:\n");
    print_matrix(matrix, n);

    // Free allocated memory
    for (int i = 0; i < n; ++i) {
        free(matrix[i]);
    }
    free(matrix);

    return 0;
}

// Function to perform in-place transpose using the provided algorithm
void in_place_transpose(int** matrix, int n, int num_threads) {
    if (n <= 1 || num_threads <= 0) {
        // Handle invalid input
        return;
    }
    if (n <= BASE_CASE_THRESHOLD) {
        // Base case: Sequential transpose for small matrices
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                // Swap matrix elements
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        return;
    }

    // Divide the matrix into tiles for efficient thread access
    int tile_size = n / (num_threads * 2); // Adjust for better granularity
    pthread_t threads[num_threads];
    ThreadArgs args[num_threads];
    for (int tid = 0; tid < num_threads; ++tid) {
        int start_row = tid * tile_size;
        int end_row = (tid + 1) * tile_size;
        if (tid == num_threads - 1) {
            end_row = n;
        }
        args[tid].matrix = matrix;
        args[tid].n = n;
        args[tid].start_row = start_row;
        args[tid].end_row = end_row;
        pthread_create(&threads[tid], NULL, in_place_transpose_tile, &args[tid]);
    }

    // Wait for all threads to finish
    for (int tid = 0; tid < num_threads; ++tid) {
        pthread_join(threads[tid], NULL);
    }
}

// Thread function to transpose a tile of the matrix
void* in_place_transpose_tile(void* arg) {
    ThreadArgs* args = (ThreadArgs*)arg;
    int** matrix = args->matrix;
    int n = args->n;
    int start_row = args->start_row;
    int end_row = args->end_row;
    for (int i = start_row; i < end_row; ++i) {
        for (int j = i + 1; j < n; ++j) {
            // Swap matrix elements
            int temp = matrix[i][j];
            matrix[i][j] = matrix[j][i];
            matrix[j][i] = temp;
        }
    }
    pthread_exit(NULL);
}

// Function to print the matrix
void print_matrix(int** matrix, int n) {
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            printf("%d ", matrix[i][j]);
        }
        printf("\n");
    }
}
