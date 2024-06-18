// #include <stdio.h>
// #include <stdlib.h>
#include <omp.h>
// #include <sys/time.h>
// #include <time.h>

#include <iostream>
#include <thread> 
#include <algorithm>
#include <sys/time.h>

using namespace std;

long long int *A;

long long int partition(long long int low,long long int high)
{
    // printf("\n%d %d\n",low,high);
    long long int pivot=A[high];
    long long int i=low-1;
    
    for(long long int j=low;j<=high;j++)
    {
      if(A[j]<pivot)
      {
        i++;
        long long int temp=A[i];
        A[i]=A[j];
        A[j]=temp;
      }
    }
    
    long long int temp=A[i+1];
    A[i+1]=A[high];
    A[high]=temp;

    return i+1;
}

void thread_quickSort(long long int low,long long int high,int thread_level,int max_threads)
{
  // when low is less than high
  if(low<high)
  {
     
    
    long long int mid = partition(low,high);

    if(thread_level<(max_threads/2))
    {
        thread thr1(thread_quickSort, low, mid-1, thread_level+1 ,max_threads);
        thread thr2(thread_quickSort, mid+1, high, thread_level+1,max_threads);

        thr1.join();
        thr2.join();
    }

    else
    {
        thread_quickSort(low,mid-1,thread_level,max_threads);
        thread_quickSort(mid+1,high,thread_level,max_threads);
    }

    /*
    #pragma omp parallel sections num_threads(threads)
    {
        #pragma omp section
        {
            quickSort(low,pi-1,threads);
        }

        #pragma omp section
        {
            quickSort(pi+1,high,threads);
        }
    }
    */
  }
}



int main(){
    
    // int threads=atoi(argv[1]);


    struct timeval tv1, tv2;
    struct timezone tz;
	double elapsed;     

    long long int size = 10000000; // 10^9
    // long long int size = 10^9; // 10^9

    
    long long int *original_A;

    // initialize
    A = (long long int *)malloc(size * sizeof(long long int));
    original_A = (long long int *)malloc(size * sizeof(long long int));

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
    

    // for(int i=0;i<size;i++)
        // printf("%d ",original_A[i]);


    

    for(int threads=1;threads<=16;threads+=2)
    {

        omp_set_num_threads(threads);

        #pragma omp parallel for
        for(long long int i=0;i<size;i++)
            A[i]=original_A[i];



        // omp_set_num_threads(threads);
        gettimeofday(&tv1, &tz);

        // QUICK SORT
        thread_quickSort(0,size-1,0,threads);

           
        gettimeofday(&tv2, &tz);
        elapsed = (double) (tv2.tv_sec-tv1.tv_sec) - (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;
        
        printf("%f\n", elapsed);

        // free(B);


        if(threads==1)
            threads=0;
    }


    // for(int i=0;i<size;i++)
        // printf("%d ",A[i]);

    free(A);
    free(original_A);


}