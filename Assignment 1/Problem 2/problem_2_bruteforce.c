#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <time.h>
#include <sys/time.h>




long long int power(int base, int exponent) {
    long long int result = 1;
    for (int i = 0; i < exponent; i++) {
        result *= base;
    }
    return result;
}


int main(int argc, char *argv[])
{   
    
    struct timeval tv1, tv2;
    struct timezone tz;
	double elapsed; 

    int size=12;

    FILE *file = fopen("12_size_solutions.txt", "a");
    if (file == NULL) {
        printf("Error opening file!\n");
        return 1;
    }

    long long int possibilites=power(size,size);
    printf("solution: %ld\n",possibilites);

    
    for(int threads=1;threads<=16;threads+=2)
    {
        int solutions_count = 0;
        
        omp_set_num_threads(threads);

        gettimeofday(&tv1, &tz);
        
        #pragma omp parallel for
        for(long long int i=0;i<possibilites;i++)
        {   
            long long int pos=i;
            int queen_rows[size];
            for(int j=0;j<size;j++)
            {
                queen_rows[j]=pos%size;
                pos/=size;
            }

            for (int i = 0; i < size; i++)
            {
                for (int j = i + 1; j < size; j++)
                {
                    // two queens in the same row => not a solution!
                    if (queen_rows[i] == queen_rows[j])
                        return 0;

                    // two queens in the same diagonal => not a solution!
                    if (queen_rows[i] - queen_rows[j] == i - j || queen_rows[i] - queen_rows[j] == j - i)
                        return 0;
                }
            }

            if(is_safe(size,queen_rows))
            {
                #pragma omp atomic
                    solutions_count+=1;
                // #pragma omp critical
                // {
                //     fprintf(file,"\n%d: \n",solutions_count);

                //     for(int row=0;row<size;row++){
                //         for(int column=0;column<size;column++){
                //             if(queen_rows[row]==column)
                //                 fprintf(file," 1 ");
                //             else
                //                 fprintf(file," 0 ");
                //         }
                //         fprintf(file,"\n");
                //     }
                // }
            }
        }

        
        gettimeofday(&tv2, &tz);
        elapsed = (double) (tv2.tv_sec-tv1.tv_sec) + (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;
        printf("%f\n", elapsed);

    
    
    
        if(threads==1)
            threads=0;
    
    }


    fclose(file);



}