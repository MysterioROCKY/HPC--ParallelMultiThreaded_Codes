#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <omp.h>
#include <sys/time.h>
#include <time.h>

#define NUM_THREADS 8

void swap(int *x, int *y) {
    int temp = *x;
    *x = *y;
    *y = temp;
}

int partition(int array[], int left, int right) {
    int pivot = array[right];
    int i = left - 1;
    for (int j = left; j <= right - 1; ++j) {
        if (array[j] <= pivot) {
            ++i;
            swap(&array[i], &array[j]);
        }
    }
    swap(&array[i + 1], &array[right]);
    return i + 1;
}

void quickSort(int array[], int left, int right) {
    if (left < right) {
        int pivotIndex = partition(array, left, right);
        #pragma omp task
        quickSort(array, left, pivotIndex - 1);
        #pragma omp task
        quickSort(array, pivotIndex + 1, right);
    }
}

int findMedian(int array[], int size) {
    int numGroups = (size + 4) / 5;
    int *medians = (int *)malloc(numGroups * sizeof(int));
    int i, medianIndex = 0;
    
    #pragma omp parallel for num_threads(NUM_THREADS)
    for (i = 0; i < size; i += 5) {
        int groupSize = (i + 5 < size) ? 5 : (size - i);
        quickSort(array + i, 0, groupSize - 1);
        medians[medianIndex++] = array[i + groupSize / 2];
    }
    
    int medianOfMedians = (numGroups == 1) ? medians[0] : findMedian(medians, numGroups);
    free(medians);
    return medianOfMedians;
}

int findMedianOfMedians(int array[], int left, int right, int k) {
    if (k > 0 && k <= right - left + 1) {
        int size = right - left + 1;
        int median = findMedian(array + left, size);
        int pivotPosition = partition(array, left, right);
        
        if (pivotPosition - left == k - 1)
            return array[pivotPosition];
        if (pivotPosition - left > k - 1)
            return findMedianOfMedians(array, left, pivotPosition - 1, k);
        return findMedianOfMedians(array, pivotPosition + 1, right, k - pivotPosition + left - 1);
    }
    return INT_MAX;
}

int main() {
    int size = 10000;
    int *array = (int *)malloc(size * sizeof(int));
    
    #pragma omp parallel for num_threads(NUM_THREADS)
    for (int i = 0; i < size; ++i)
        array[i] =(int) rand() % 100;
    
    struct timeval startTime, endTime;
    struct timezone tz;
    gettimeofday(&startTime, &tz);
    int median = findMedianOfMedians(array, 0, size - 1, size / 2 + 1);
    
    printf("Median of the array is: %d\n", median);
    
    gettimeofday(&endTime, &tz);
    double elapsedTime;
    elapsedTime = (endTime.tv_sec - startTime.tv_sec);
    elapsedTime = (elapsedTime + (endTime.tv_usec - startTime.tv_usec)) * 1e-6;
    printf("Elapsed time: %lf seconds\n", elapsedTime);

    free(array);
    return 0;
}