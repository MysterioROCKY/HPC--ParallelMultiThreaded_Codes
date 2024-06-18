// Due to memory constraints(16gb ram is not sufficient for verification) unable to verify solution but verified for 10^7 and it works!

#include <omp.h>

#include <iostream>
#include <algorithm>
#include <sys/time.h>
#include <pthread.h>


using namespace std;

long long int *A;
long long int part = 0;
long long int thread=1;
long long int total_size = 1000000000; 



// merge function for merging two parts
void merge(int low, int mid, int high)
{
	
    long long int mx = max(A[mid], A[high]) + 1;
 
    int i = low, j = mid + 1, k = low;
    while (i <= mid && j <= high && k <= high) 
    {
        // recover back original element to compare
        
        long long int e1 = A[i] % mx;
        long long int e2 = A[j] % mx;
        
        if (e1 <= e2) 
        {
            A[k] += (e1 * mx);
            i++;
            k++;
        }
        else 
        {
            A[k] += (e2 * mx);
            j++;
            k++;
        }
    }
 
    // process those elements which are left in the array
    long long int el;
    while (i <= mid) {
        el = A[i] % mx;
        A[k] += (el * mx);
        i++;
        k++;
    }
 
    while (j <= high) {
        el = A[j] % mx;
        A[k] += (el * mx);
        j++;
        k++;
    }
 
    // finally update elements by dividing with maximum
    // element
    for (int i = low; i <= high; i++)
        A[i] /= mx;

}







// merge sort function
void merge_sort(long long int low, long long int high)
{
	// calculating mid point of array
	long long int mid = low + (high - low) / 2;
	if (low < high) {

		// calling first half
		merge_sort(low, mid);

		// calling second half
		merge_sort(mid + 1, high);

		// merging the two halves
		merge(low, mid, high);
	}
}





// thread function for multi-threading
void* merge_sort(void* arg)
{
	// which part out of 4 parts
	long long int thread_part = part++;

	// calculating low and high
    long long int low = thread_part * (total_size / thread);
	int high = (thread_part + 1) * (total_size / thread) - 1;

	// evaluating mid point
	long long int mid = low + (high - low) / 2;
	if (low < high) {
		merge_sort(low, mid);
		merge_sort(mid + 1, high);
		merge(low, mid, high);
	}

	return NULL;
}


// merge sort function
void sorted_merge(long long int low,long long  int high)
{
	// calculating mid point of array
	long long int mid = low + (high - low) / 2;
	if (low < high) {
		// merging the two halves
		merge(low, mid, high);
	}
}

// thread function for multi-threading
void* sorted_merge(void* arg)
{
	// which part out of 4 parts
	int thread_part = part++;

	// calculating low and high
	int low = thread_part * (total_size / thread);
	int high = (thread_part + 1) * (total_size / thread) - 1;

	// evaluating mid point
	int mid = low + (high - low) / 2;
	if (low < high) {
		merge(low, mid, high);
	}

	return NULL;
}



int main(int argc,char *argv[]){
    
    int g_thread=atoi(argv[1]);


    struct timeval tv1, tv2;
    struct timezone tz;
	double elapsed;     

    // long long int total_size = 10^9; // 10^9

    
    long long int *original_A;
    // long long int *seq_sorted_A;


    // initialize
    A = (long long int *)malloc(total_size * sizeof(long long int));
    original_A = (long long int *)malloc(total_size * sizeof(long long int));
    // seq_sorted_A = (long long int *)malloc(total_size * sizeof(long long int));


    // assign random value
    omp_set_num_threads(6);    
    #pragma omp parallel
    {
        unsigned int seed=time(NULL) ^ omp_get_thread_num();;
        #pragma omp for
        for(long long int i = 0; i < total_size; i++){
            original_A[i] = rand_r(&seed) % 1001;
        }
    }
    
    // for(int i=0;i<total_size;i++)
    // {
    //     cout<<original_A[i]<<" ";
    // }

    // for(int g_thread=16;g_thread<=16;g_thread+=2)
    // {


        omp_set_num_threads(16);
        #pragma omp parallel for
        for(long long int i=0;i<total_size;i++)
            A[i]=original_A[i];

        thread=g_thread;

        gettimeofday(&tv1, &tz);
        //sequential
        // if(g_thread==1)
        // {
            // merge_sort(0,total_size-1);
            // #pragma omp parallel for
            // for(long long int i=0;i<total_size;i++)
                // seq_sorted_A[i]=A[i];

        // }

        // else
        // {
            part=0;
    	    pthread_t threads[thread];

            // creating variable thread number of threads
    	    for (int i = 0; i < thread; i++)
    	    	pthread_create(&threads[i], NULL, merge_sort,(void*)NULL);

            	// joining all 4 threads
    	    for (int i = 0; i < thread; i++)
    	    	pthread_join(threads[i], NULL);


            // from here we directly merge
            for(int i=thread/2;i>0;i/=2)
            {
                thread=i;
                pthread_t threads[thread];

                part=0;
                for (int j = 0; j < thread; j++)
    	    	    pthread_create(&threads[j], NULL, sorted_merge,(void*)NULL);

                for (int j = 0; j < thread; j++)
    	    	    pthread_join(threads[j], NULL);

            }
        // }

        

        //end
        gettimeofday(&tv2, &tz);
        elapsed = (double) (tv2.tv_sec-tv1.tv_sec) - (double) (tv2.tv_usec-tv1.tv_usec) * 1.e-6;

        // int flag=0;
        // for(int i=0;i<total_size;i++)
        // {
        //     if(A[i]!=seq_sorted_A[i])
        //     {
        //         flag=1;
        //         break;
        //     }    
        // }

        // if(flag==0)
        //     cout<<"a"<<elapsed;

        cout<<elapsed<<"\n";


        if(g_thread==1)
            thread=0;

    // }

    free(A);
    free(original_A);
    // free(seq_sorted_A);


}