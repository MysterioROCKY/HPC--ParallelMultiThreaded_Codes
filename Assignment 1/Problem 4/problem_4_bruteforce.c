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

    long long start = 0;
    long long end = 30;


    for(int thread=1;thread<=1;thread+=2)
    {
        // 0 false
        // 1 true

        int sieve[end + 1];
        for (int i = 0; i <= end; i++)
            sieve[i] = 0;
        
        omp_set_num_threads(thread);
        gettimeofday(&start_time, &tz);

        if (end > 2)
            sieve[2] = 1;
        if (end > 3)
            sieve[3] = 1;

        long long int x;
        long long int y;
        #pragma omp parallel for private(x,y) 
        for (x=1;x*x<=end;x++) 
        {
            for (y=1;y*y<=end;y++)
            {
                // Condition 1
                long long int n = (4*x*x)+(y*y);
                if (n <= end && (n % 12 == 1 || n % 12 == 5))
                    sieve[n] = 1;
    
                // Condition 2
                n = (3*x*x)+(y*y);
                if (n <= end && n % 12 == 7)
                    sieve[n] = 1;
    
                // Condition 3
                n = (3*x*x)-(y*y);
                if (x > y && n <= end && n % 12 == 11)
                    sieve[n] = 1;

            }
        }
        
        long long int r,i;
        #pragma omp parallel for private(r,i)
        for (r = 5; r*r <= end; r++) 
        {
            if (sieve[r]) 
            {
                for (i = r * r; i <= end; i += r * r)
                    sieve[i] = 0;
            }
        }

        for (long long int a = start; a <= end; a++)
            if (sieve[a])
                printf("%lld\n",a);
            


        gettimeofday(&end_time, &tz);
        elapsed = (end_time.tv_sec - start_time.tv_sec) + (end_time.tv_usec - start_time.tv_usec) / 1000000.0;
        printf("%.8f",elapsed);

        if(thread==0)
            thread=2;
    }

    return 0;
}
