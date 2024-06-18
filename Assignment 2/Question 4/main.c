#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <sys/time.h>
#include <time.h>

#define ll long long
#define nl "\n"
#define SUBSEQUENCE_SIZE 1000000
#define TOTAL_SIZE 1000000000
#define NUM_SUBSEQUENCES 1000
#define NUM_THREADS_MAX 16



// merge function for merging two parts
void merge(int arr[],int low, int mid, int high)
{
	
    long long int mx ;
    
    if(arr[mid]>=arr[high])
        mx=arr[mid]+1;
    else
        mx=arr[high]+1;

    int i = low, j = mid + 1, k = low;
    while (i <= mid && j <= high && k <= high) 
    {
        // recover back original element to compare
        
        long long int e1 = arr[i] % mx;
        long long int e2 = arr[j] % mx;
        
        if (e1 <= e2) 
        {
            arr[k] += (e1 * mx);
            i++;
            k++;
        }
        else 
        {
            arr[k] += (e2 * mx);
            j++;
            k++;
        }
    }
 
    // process those elements which are left in the array
    long long int el;
    while (i <= mid) {
        el = arr[i] % mx;
        arr[k] += (el * mx);
        i++;
        k++;
    }
 
    while (j <= high) {
        el = arr[j] % mx;
        arr[k] += (el * mx);
        j++;
        k++;
    }
 
    // finally update elements by dividing with maximum
    // element
    for (int i = low; i <= high; i++)
        arr[i] /= mx;

}




void mergeSortSubsequence(int arr[], int l, int r) {
    if (r - l > 0) {
        int m = l + (r - l) / 2;
        mergeSortSubsequence(arr, l, m);
        mergeSortSubsequence(arr, m + 1, r);
        merge(arr, l, m, r);
    }
}

int main(void) {
    int* inputSequence = (int *)malloc(TOTAL_SIZE * sizeof(int));
    struct timeval tv1, tv2;

    // assign random value
    omp_set_num_threads(6);    
    #pragma omp parallel for
    for(long long int i = 0; i < TOTAL_SIZE; i+=SUBSEQUENCE_SIZE)
    {
        unsigned int seed=time(NULL) ^ omp_get_thread_num();
        inputSequence[i]=rand_r(&seed) % 1001;
        int sum=0;
        for(long long int j = i+1; j < SUBSEQUENCE_SIZE; j+=1)
        {
            inputSequence[j] = inputSequence[i]+sum;
            sum+=inputSequence[i];
        }
    }


    for (int numThreads = 1; numThreads <= NUM_THREADS_MAX; numThreads+=2) {
        omp_set_num_threads(numThreads);

        gettimeofday(&tv1, NULL);

        #pragma omp parallel for
        for (int i = 0; i < NUM_SUBSEQUENCES; i++) {
            for (int j = 0; j < SUBSEQUENCE_SIZE; j++) {
                inputSequence[i * SUBSEQUENCE_SIZE + j] = rand();
            }
            mergeSortSubsequence(inputSequence + i * SUBSEQUENCE_SIZE, 0, SUBSEQUENCE_SIZE - 1);
        }

        #pragma omp parallel for
        for (int i = 1; i < NUM_SUBSEQUENCES; i++) {
            merge(inputSequence, 0, i * SUBSEQUENCE_SIZE - 1, (i + 1) * SUBSEQUENCE_SIZE - 1);
        }

        gettimeofday(&tv2, NULL);
        double elapsed_time = (double)(tv2.tv_sec - tv1.tv_sec) + (double)(tv2.tv_usec - tv1.tv_usec) * 1.e-6;
        printf("Time taken for sorting and merging with %d threads: %f seconds\n", numThreads, elapsed_time);

        if(numThreads==1)
            numThreads=0;
    
    }

    free(inputSequence);

    return 0;
}

