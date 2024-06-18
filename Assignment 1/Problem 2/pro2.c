#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <omp.h>
#include <sys/time.h>

#define MAX_N 20

int solutions[MAX_N][MAX_N]; // Array to store the solutions
int num_solutions = 0;       // Number of solutions found

bool isSafe(int board[MAX_N][MAX_N], int row, int col, int n)
{
    // Check if there is a queen in the same column
    for (int i = 0; i < row; i++)
    {
        if (board[i][col] == 1)
        {
            return false;
        }
    }

    // Check if there is a queen in the same diagonal (left-upper to right-bottom)
    for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
    {
        if (board[i][j] == 1)
        {
            return false;
        }
    }

    // Check if there is a queen in the same diagonal (right-upper to left-bottom)
    for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++)
    {
        if (board[i][j] == 1)
        {
            return false;
        }
    }

    return true;
}

void solveNQueens(int board[MAX_N][MAX_N], int row, int n)
{
    // If all queens are placed, record the solution
    if (row == n)
    {
#pragma omp critical
        {
            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    solutions[num_solutions][i * n + j] = board[i][j];
                }
            }
            num_solutions++;
        }
        return;
    }

// Try placing the queen in each column of the current row
#pragma omp parallel for
    for (int col = 0; col < n; col++)
    {
        if (isSafe(board, row, col, n))
        {
            board[row][col] = 1;
            solveNQueens(board, row + 1, n);
            board[row][col] = 0;
        }
    }
}

int main()
{

    struct timeval tv1,
        tv2;
    struct timezone tz;
    double elapsed;

    int threads[] = {16, 14, 12, 10, 8, 6, 4, 2, 1};

    freopen("NQueens.dat", "a", stdout);

    for (int n = 10; n <= 16; n += 2)
    {
        printf("%d ", n);

        for (int th = 0; th < 9; th++)
        {
            omp_set_num_threads(threads[th]);
            // Create an empty chess board
            int board[MAX_N][MAX_N] = {0};
            num_solutions = 0;
            solutions[MAX_N][MAX_N];
            gettimeofday(&tv1, &tz);
#pragma omp parallel
            {
                solveNQueens(board, 0, n);
            }
            gettimeofday(&tv2, &tz);
            // printf("Found %d solutions:\n", num_solutions);
            elapsed = (double)(tv2.tv_sec - tv1.tv_sec) + (double)(tv2.tv_usec - tv1.tv_usec) * 1.e-6;
            printf("%f ", elapsed);
        }
        printf("\n");
    }
}