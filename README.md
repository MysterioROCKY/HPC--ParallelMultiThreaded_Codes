# HPC - High Performance Computing

Welcome to the HPC (High Performance Computing) repository for the academic year 2023-24. This repository contains various assignments and projects related to parallel and multithreaded computing using **C (using OpenMP) and Java (using built-in concurrent libraries)**.

## Repository Structure

The repository is structured as follows:

### Assignments

Each assignment folder contains multiple questions with both parallel multithreaded and sequential codes. The folders also include Excel sheets with performance readings (1, 2, 4, 8, 12, 16 threads) and PDF files showing complexity graphs.

All folder contains multithreaded and parallel algorithms implemented in **C (using OpenMP)** and **Java (using built-in concurrent libraries)**.

- **Assignment 1**
  - Compute A² of a matrix using ordinary and block matrix multiplication.
  - Solve the N-Queens problem.
  - Find the minimum, maximum, and mean of a large array.
  - Generate prime numbers in a specified range.
  - Calculate the dot product of large vectors.

- **Assignment 2**
  - Compute X¹⁵ of a complex matrix.
  - Create and manipulate a sorted linked list with various workloads.
  - Implement multithreaded sorting algorithms (Merge Sort, Quick Sort, Counting Sort).
  - Sort and merge large subsequences.
  - Find the median of a large dataset.

- **Assignment 3**
  - Implement and measure the performance of various synchronization techniques on a sorted linked list.
  - Analyze the performance of different locking mechanisms.

- **Assignment 4**
  - Develop concurrent data structures (Binary Search Tree, AVL Tree, Skiplist, Hashsets) and measure their performance with various workloads.

### MultiThreaded-Parallel_Algo

This folder contains additional multithreaded and parallel algorithms implemented in **C (using OpenMP)** and **Java (using built-in concurrent libraries)**.

- **Locks (TAS-TTAS-Backoff)**
  - `Backoff.java`
  - `Counter_test.java`
  - `TASLock.java`
  - `TTASLock.java`

- **C Programs**
  - `Floyd-Warshall.c`
  - `In-Place_MatrixTranspose.c`

- **Java Programs**
  - `LinkedListWorkloadSimulation.java`
  - `LockTest.java`
  - `MatrixMultiplication.java`
  - `MergeSortMultithreaded.java`

## Instructions

1. **Clone the Repository**

   ```sh
   git clone https://github.com/MysterioROCKY/HPC-HighPerformanceComputing.git
   cd HPC-HighPerformanceComputing

2. **Compile and Run the Programs**
   
- For C programs, use a compiler like `gcc`:
   
  ```sh
  gcc -fopenmp program_name.c -o program_name
  ./program_name

- For Java programs, use javac and java:

  ```sh
  javac ProgramName.java
  java ProgramName



3. **View Performance Data**
   
   - Check the Excel sheets in each assignment folder to see the performance readings with varying thread counts. The PDF files contain graphs and analyses of the 
     computational complexity and speedup achieved.
   
