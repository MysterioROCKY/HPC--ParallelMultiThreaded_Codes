#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <sys/time.h>
#include <time.h>


long long int *A;
long long int *original_A;
long long int *seq_sorted_A;


// Parallel count sort 
void parallelCountSort(long long int n) 
{ 
	long long int max_value = 0;
	long long int index = 0;

    // Get Max Value
    #pragma omp parallel for reduction(max: max_value)
    for (long long int i = 0; i < n; i++) {
        if(A[i]>max_value)
            max_value=A[i];
    }
    long long int *count;
    count = (long long int *)calloc(max_value+1 , sizeof(long long int));


    #pragma omp parallel for
    for (long long int i = 0; i < n; i++) 
    {
        #pragma omp atomic
    		count[A[i]]+=1;
    }

	for (int i = 0; i <= max_value; i++) {
		for (int j = 1; j <= count[i]; j++) {
			A[index++] = i;
		}
	}

    free(count);
} 

// Driver program to test above functions 
int main() 
{ 
	// vector<int> arr = { 170, 45, 75, 90, 802, 24, 2, 66 }; 

    struct timeval tv1, tv2;
    struct timezone tz;
	double elapsed;     

    long long int size = 1000000000;

    A = (long long int *)calloc(size , sizeof(long long int));
    original_A = (long long int *)calloc(size , sizeof(long long int));
    // seq_sorted_A = (long long int *)calloc(size , sizeof(long long int));

    // assign random value
    omp_set_num_threads(6);    
    #pragma omp parallel
    {
        unsigned int seed=time(NULL) ^ omp_get_thread_num();;
        #pragma omp for
        for(long long int i = 0; i < size; i++){
            original_A[i] = rand_r(&seed) % 1001;
        }
    }

    for(int threads=1;threads<=16;threads+=2)
    {
        omp_set_num_threads(threads);    
        #pragma omp parallel for
        for(long long int i=0;i<size;i++)
            A[i]=original_A[i];

        omp_set_num_threads(threads);    
            
        gettimeofday(&tv1, &tz);
        parallelCountSort(size); 
        gettimeofday(&tv2, &tz);

        elapsed = (double) (tv2.tv_sec-tv1.tv_sec) - (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;        

        int flag=0;
        if(threads==1)
        {
            threads=0;

            // #pragma omp parallel for
            // for(long long int i=0;i<size;i++)
                // seq_sorted_A[i]=A[i];
        }

        // else
        // {
            // for(long long int i=0;i<size;i++)
            // {
                // if(seq_sorted_A[i]!=A[i])
                // {
                    // flag=1;
                    // break;
                // }
            // }
        // }
// 
        // if(flag!=1)
        printf("%d %f\n", threads,elapsed);



    }

    free(A);
    free(original_A);
    // free(seq_sorted_A);

	return 0; 
}
