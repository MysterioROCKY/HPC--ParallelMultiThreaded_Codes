#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <math.h>
#include <omp.h>
#include <sys/time.h>

int main(int argc, char *argv[]) 
{
    struct timeval tv1, tv2;
    struct timezone tz;
    double elapsed;

    for(int thread=1; thread<=16; thread+=2)
    {
        gettimeofday(&tv1, &tz);

        // long long int lastNumber = 2199023255552;
        long long int lastNumber = 2199023255;

        omp_set_num_threads(thread);   

        const long long int lastNumberSqrt = (long long int)sqrtl((long double)lastNumber);   
        long long int memorySize = (lastNumber-1)/2;   // initialize   
        long long int* isPrime = malloc((memorySize + 1) * sizeof(long long int));

        #pragma omp parallel for   
        for (long long int i = 0; i <= memorySize; i++)  
        {   
            isPrime[i] = 1;   // find all odd non-primes 
        }

        #pragma omp parallel for schedule(dynamic)   
        for (long long int i = 3; i <= lastNumberSqrt; i += 2)
        {
            if (isPrime[i/2])
            {
                for (long long int j = i*i; j <= lastNumber; j += 2*i)
                {
                    isPrime[j/2] = 0;   // sieve is complete, count primes   
                }
            }
        }

        long long int found = lastNumber >= 2 ? 1 : 0; 
        #pragma omp parallel for reduction(+:found)   
        for (long long int i = 1; i <= memorySize; i++)     
            found += isPrime[i];   

        free(isPrime);

        gettimeofday(&tv2, &tz);
        	
        elapsed = (double) (tv2.tv_sec-tv1.tv_sec) + (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;
        printf("%d %4.2lf \n",thread, elapsed);

        if(thread==1)
        {
            thread=0;
        }
    }

    return(0);
}
