#include <omp.h>
#include <stdio.h>
#include <stdlib.h>

#define INF 99999

void printMatrix(int **dist, int N);
void floydWarshall(int **graph, int N);

int main() {
    int N, i, j;

    // Ask the user for the number of nodes
    printf("Enter the number of nodes (vertices): ");
    scanf("%d", &N);

    // Allocate memory for the graph matrix
    int *graph = (int)malloc(N * sizeof(int));
    for (i = 0; i < N; i++) {
        graph[i] = (int*)malloc(N * sizeof(int));
    }

    // Ask the user to input the graph matrix
    printf("Enter the graph adjacency matrix (use %d for infinity):\n", INF);
    for (i = 0; i < N; i++) {
        for (j = 0; j < N; j++) {
            scanf("%d", &graph[i][j]);
            if (graph[i][j] == 0 && i != j) {
                graph[i][j] = INF;  // Ensure non-diagonal zeros are treated as INF
            }
        }
    }

    // Perform the Floyd-Warshall algorithm on the user input graph
    floydWarshall(graph, N);

    // Free allocated memory
    for (i = 0; i < N; i++) {
        free(graph[i]);
    }
    free(graph);

    return 0;
}

void floydWarshall(int **graph, int N) {
    int *dist = (int *)malloc(N * sizeof(int *));
    int i, j, k;

    // Initialize distance array
    for (i = 0; i < N; i++) {
        dist[i] = (int *)malloc(N * sizeof(int));
        for (j = 0; j < N; j++) {
            dist[i][j] = graph[i][j];
        }
    }

    // Floyd-Warshall with OpenMP
    for (k = 0; k < N; k++) {
        #pragma omp parallel for private(i, j) shared(dist, N)
        for (i = 0; i < N; i++) {
            for (j = 0; j < N; j++) {
                if (dist[i][k] + dist[k][j] < dist[i][j]) {
                    dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }
    }

    // Print the shortest distances matrix
    printMatrix(dist, N);

    // Free distance array
    for (i = 0; i < N; i++) {
        free(dist[i]);
    }
    free(dist);
}

void printMatrix(int **dist, int N) {
    printf("Shortest distances between every pair of vertices:\n");
    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            if (dist[i][j] == INF)
                printf("%7s", "INF");
            else
                printf("%7d", dist[i][j]);
        }
        printf("\n");
    }
}