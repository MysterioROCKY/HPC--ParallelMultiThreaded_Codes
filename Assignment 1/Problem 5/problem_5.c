#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <sys/time.h>
#include <time.h>


int main(int argc, char* argv[]){
    
    int threads=atoi(argv[1]);


    struct timeval tv1, tv2;
    struct timezone tz;
	double elapsed;     

    // long long int size = 1000000000; // 10^9
    long long int size = 10; // 10^9

    long long int dot_product = 0;
    int *A,*B;

    // initialize
    A = (int *)malloc(size * sizeof(int));
    B = (int *)malloc(size * sizeof(int));

    // assign random value
    omp_set_num_threads(6);     
    #pragma omp parallel
    {
        unsigned int seed=time(NULL) ^ omp_get_thread_num();;
        #pragma omp for
        for(int i = 0; i < size; i++){
            A[i] = rand_r(&seed) % 3 - 1;
            B[i] = rand_r(&seed) % 3 - 1;
        }
    }
    

    //sequential answer
    int sequential_dot_product=0;
    for(int i=0;i<size;i++){
        sequential_dot_product+=A[i]*B[i];
    }

    for(int i=1;i<=16;i+=2)
    {
        omp_set_num_threads(i);
        

        
        // calculate dot product
        dot_product=0;
        gettimeofday(&tv1, &tz);
        #pragma omp parallel for reduction(+:dot_product)
        for(int i=0;i<size;i++){
            dot_product+=A[i]*B[i];
        }
       
        gettimeofday(&tv2, &tz);
        elapsed = (double) (tv2.tv_sec-tv1.tv_sec) + (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;
        if(dot_product!=sequential_dot_product)
            exit(0);
        
        printf("%f\n", elapsed);



        if(i==1)
            i=0;
    }


    free(A);
    free(B);

}