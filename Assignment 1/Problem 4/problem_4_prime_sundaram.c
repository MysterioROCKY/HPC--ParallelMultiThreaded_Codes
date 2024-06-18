#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <omp.h>
#include <sys/time.h>

// Implement using Sieve of Atkin algorithm

int main(int argc, char *argv[]) {

    struct timeval start_time, end_time;
    struct timezone tz;
    double elapsed;

    // long long int end = 2199023255552;
    long long int end = 21990232;

    long long int nNew = (end - 1) / 2;
    printf("%ld",nNew);
    
    
    
    for(int thread=1;thread<=1;thread+=2)
    {   
        gettimeofday(&start_time, &tz);
        
        bool *sieve; 
        sieve = (bool *)malloc((nNew + 1) * sizeof(bool));

        omp_set_num_threads(16);
        long long int i,j;
        #pragma omp parallel for 
        for (i=1; i<=nNew; i++)
            for (j=i; (i + j + 2*i*j) <= nNew; j++)
                    sieve[i + j + 2*i*j] = true;


        gettimeofday(&end_time, &tz);
        elapsed = (end_time.tv_sec - start_time.tv_sec) + (end_time.tv_usec - start_time.tv_usec) / 1000000.0;
        printf("%.8f",elapsed);

        int ans=0;
        for (long long int i=pow(2, 40); i<=nNew; i++)
            if (sieve[i] == false)
                printf("%d\n",2*i+1);

 
        // if(thread==0)
        //     thread=2;
    }
    

    return 0;
}
