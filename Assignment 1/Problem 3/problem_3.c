#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>
#include <sys/time.h>

#define SIZE (1 << 28)

void generate_array(int* arr, int size) {
    for (int i = 0; i < size; i++) {
        arr[i] = rand() % 1000000000;  // Generate random numbers between 0 and 999
    }
}



int main() {
    struct timeval tv1, tv2;
    struct timezone tz;
	double elapsed;    

    srand(time(NULL));

    int* arr = (int*)malloc(SIZE * sizeof(int));
    generate_array(arr, SIZE);

    int num_threads[] = {1, 2, 4, 6, 8, 10, 12, 14, 16};
    int num_threads_size = sizeof(num_threads) / sizeof(num_threads[0]);

    double start_time, end_time;

    long long int seq_max=arr[0],seq_min=arr[0],seq_avg=0;
    
    for(long long int i=0;i<SIZE;i++){
        if (arr[i] < seq_min) {
                seq_min = arr[i];
        }
        if (arr[i] > seq_max) {
                seq_max = arr[i];
        }
        seq_avg+=arr[i];
    }

    seq_avg/=SIZE;

    for (int i = 0; i < num_threads_size; i++) {
        int num_thread = num_threads[i];

        omp_set_num_threads(num_thread);

        // start_time = omp_get_wtime();
        gettimeofday(&tv1, &tz);


        // Find minimum
        int min_val = arr[0];
#pragma omp parallel for reduction(min : min_val)
        for (int j = 0; j < SIZE; j++) {
            if (arr[j] < min_val) {
                min_val = arr[j];
            }
        }

        // Find maximum
        int max_val = arr[0];
#pragma omp parallel for reduction(max : max_val)
        for (int j = 0; j < SIZE; j++) {
            if (arr[j] > max_val) {
                max_val = arr[j];
            }
        }

        // Find mean
        long long mean_val = 0;
#pragma omp parallel for reduction(+:mean_val)
        for (int j = 0; j < SIZE; j++) {
            mean_val += arr[j];
        }
        mean_val /= SIZE;

        // end_time = omp_get_wtime();
        gettimeofday(&tv2, &tz);
        elapsed = (double) (tv2.tv_sec-tv1.tv_sec) + (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;

        if(seq_max==max_val && seq_min==min_val && seq_avg==mean_val)
        {
            printf("%f\n", elapsed);
        }

        
    }

    free(arr);

    return 0;
}
