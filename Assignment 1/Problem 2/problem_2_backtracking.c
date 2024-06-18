#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <sys/time.h>
#include <time.h>


#define N 16

int board[N][N];
int solution=0;

// Function to check if a queen can be placed on board[row][col]
int isSafe(int row, int col) {
    int i, j;

    // Check this row on left side
    for (i = 0; i < col; i++)
        if (board[row][i])
            return 0;

    // Check upper diagonal on left side
    for (i = row, j = col; i >= 0 && j >= 0; i--, j--)
        if (board[i][j])
            return 0;

    // Check lower diagonal on left side
    for (i = row, j = col; j >= 0 && i < N; i++, j--)
        if (board[i][j])
            return 0;

    return 1;
}

// A recursive utility function to solve N Queen problem
int solveNQueensUtil(int col) {
    // If all queens are placed then print the solution
    if (col >= N) {
        #pragma omp critical
        {
            solution+=1;
            // printf("Solution: %d\n",solution);
            // for (int i = 0; i < N; i++) {
            //     for (int j = 0; j < N; j++) {
            //         printf("%d ", board[i][j]);
            //     }
            //     printf("\n");
            // }
            // printf("\n");
        }
        return 1;
    }

    // Initialize solution found flag
    int found = 0;

    // Consider this column and try placing this queen in all rows one by one
    for (int i = 0; i < N; i++) {
        // Check if the queen can be placed on board[i][col]
        if (isSafe(i, col)) {
            // Place this queen in board[i][col]
            board[i][col] = 1;

            // Recur to place rest of the queens
            found |= solveNQueensUtil(col + 1);

            // If placing queen in board[i][col] doesn't lead to a solution, then remove queen from board[i][col]
            board[i][col] = 0; // BACKTRACK
        }
    }

    // Return if solution is found in this column
    return found;
}

// Function to solve the N Queen problem
void solveNQueens() {
    #pragma omp parallel for 
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            board[i][j] = 0;

    int found = solveNQueensUtil(0);

    if (!found)
        printf("No solution found\n");
}

int main() {

    struct timeval tv1, tv2;
    struct timezone tz;
	double elapsed;     

    
    for(int thread=1;thread<=16;thread+=2)
    {
        solution=0;
        omp_set_num_threads(thread);
        gettimeofday(&tv1, &tz);
        solveNQueens();
        gettimeofday(&tv2, &tz);
        
        elapsed = (double) (tv2.tv_sec-tv1.tv_sec) + (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;
        printf("%d %d %f\n", thread,solution,elapsed);



        if(thread==1)
            thread=0;
    }

    double start_time = omp_get_wtime();
    solveNQueens();
    double end_time = omp_get_wtime();
    printf("Elapsed time: %f seconds\n", end_time - start_time);
    return 0;
}
