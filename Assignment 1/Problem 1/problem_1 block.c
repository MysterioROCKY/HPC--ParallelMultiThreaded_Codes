#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>
#include <sys/time.h>

int main(int argc, char *argv[])
{   
    // int size=2048;
    int size=4096;
    int block_size=4;

    int *A, *B, *C;

    struct timeval tv1, tv2;
    struct timezone tz;
	double elapsed; 

    // initialize
    A = (int *)malloc(size * size * sizeof(int));
    B = (int *)malloc(size * size * sizeof(int));
    C = (int *)malloc(size * size * sizeof(int));

    int threads = 15;

    // assign random value
    omp_set_num_threads(threads);
    #pragma omp parallel
    {
        unsigned int seed = time(NULL) ^ omp_get_thread_num();
    
        #pragma omp for
        for (int i = 0; i < size*size; i++)
        {
            A[i] = rand_r(&seed) % 3 - 1;
            B[i] = rand_r(&seed) % 3 - 1;

        }
    }

    for(int block_size=4;block_size<=64;block_size*=2)
    {
        printf("%d\n",block_size);
        for(int loop=0;loop<5;loop++)
        {
            for (int threads = 1; threads <= 16; threads += 2)
            {

                omp_set_num_threads(10);
                #pragma omp parallel for
                for (int j = 0; j < size*size; j++)
                {
                    C[j]=0;
                }

                omp_set_num_threads(threads);

                int i,j,k;
                gettimeofday(&tv1, &tz);
                #pragma omp parallel for collapse(3) private(j, k)
                for (int jj = 0; jj < size; jj += block_size) {
                    for (int kk = 0; kk < size; kk += block_size) {
                        for (i = 0; i < size; i++) {
                            for (j = jj; j < jj + block_size && j < size; j++) {
                                int temp = 0;
                                for (k = kk; k < kk + block_size && k < size; k++) {
                                    temp += A[i*size+k] * B[k*size+j];
                                }
                                C[i*size+j] += temp;
                            }
                        }
                    }
                }
                gettimeofday(&tv2, &tz);
                elapsed = (double) (tv2.tv_sec-tv1.tv_sec) + (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;
                printf("%f\n", elapsed);
                
                if (threads == 1)
                threads = 0;
            
            }

            printf("\n \n");
            
        }
    }


    free(A);
    free(B);
    free(C);
}