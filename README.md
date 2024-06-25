# HPC - High Performance Computing Repository

Welcome to the HPC (High Performance Computing) repository. This repository contains various assignments and projects related to parallel and multithreaded computing using **C (using OpenMP) and Java (using built-in concurrent libraries)**.

## Repository Structure

The repository is structured as follows:

This repository contains assignments and solutions for the High-Performance Computing (HPC) course. The assignments are focused on multi-threaded parallel execution and are implemented in C, C++, and Java. Each assignment folder includes the problem statements, source code, and performance analysis graphs.

### Assignment 1
- **.vscode**: Configuration files for the Visual Studio Code editor.
- **Problem 1**
  - `1-2048(block).pdf`: PDF with block execution results for 2048 elements.
  - `1-2048.pdf`: PDF with results for 2048 elements.
  - `1-4096(block).pdf`: PDF with block execution results for 4096 elements.
  - `1-4096.pdf`: PDF with results for 4096 elements.
  - `problem_1 block.c`: C program with block execution for Problem 1.
  - `problem_1 ordinary.c`: C program with ordinary execution for Problem 1.
- **Problem 2**
  - `pro2.c`: Main C program for Problem 2.
  - `problem_2.pdf`: Problem statement for Problem 2.
  - `problem_2_backtracking.c`: C program using backtracking for Problem 2.
  - `problem_2_bruteforce.c`: C program using brute force for Problem 2.
- **Problem 3**
  - `problem_3.c`: C program for Problem 3.
  - `problem_3.pdf`: Problem statement for Problem 3.
- **Problem 4**
  - `problem_4_bruteforce.c`: C program using brute force for Problem 4.
  - `problem_4_prime_eratosthenes.c`: C program using the Sieve of Eratosthenes for Problem 4.
  - `problem_4_prime_sundaram.c`: C program using the Sieve of Sundaram for Problem 4.
- **Problem 5**
  - `problem_5.c`: C program for Problem 5.
  - `problem_5.pdf`: Problem statement for Problem 5.
- `HPC_Assignment1_Questions.pdf`: PDF containing the questions for Assignment 1.
- `Team 26 HPC Assignment 1.xlsx`: Excel sheet with performance data for Assignment 1.

### Assignment 2
- **.vscode**: Configuration files for the Visual Studio Code editor.
- **Question 1**
  - `Reading1.jpg`: Image file with reading 1.
  - `Reading2.jpg`: Image file with reading 2.
  - `graph.sh`: Shell script to generate graphs.
  - `mpp2.java`: Java program for Question 1.
  - `mpp2.pdf`: Problem statement for Question 1.
  - `plotme.dat`: Data file for plotting.
- **Question 2**
  - `ListQ2.java`: Java program for listing operations in Question 2.
  - `Q2.pdf`: Problem statement for Question 2.
  - `TestQ2.java`: Java test program for Question 2.
  - `ThreadID.java`: Java class for thread identification.
  - `file.dat`: Data file used in Question 2.
- **Question 3**
  - **Count Sort**
    - `Count Sort.txt`: Text file with details about Count Sort.
    - `FinalCountSort.c`: Final implementation of Count Sort in C.
  - **Merge Sort**
    - `a.out`: Executable file for Merge Sort.
    - `merge.c++`: C++ program for Merge Sort.
    - `script.sh`: Shell script to run Merge Sort.
  - **Quick Sort**
    - `QuickSort.c++`: C++ program for Quick Sort.
    - `QuickSort.txt`: Text file with details about Quick Sort.
  - `count_sort.dat`: Data file for Count Sort.
  - `graph.sh`: Shell script to generate graphs.
  - `graph1.pdf`: PDF with graph results for various sorts.
  - `merge_sort.dat`: Data file for Merge Sort.
  - `quick_sort.dat`: Data file for Quick Sort.
- **Question 4**
  - `a.out`: Executable file for Question 4.
  - `data.dat`: Data file used in Question 4.
  - `graph.pdf`: PDF with graph results for Question 4.
  - `graph.sh`: Shell script to generate graphs.
  - `main.c`: Main C program for Question 4.
- **Question 5**
  - `graph.pdf`: PDF with graph results for Question 5.
  - `graph.sh`: Shell script to generate graphs.
  - `median.c`: C program for calculating median in Question 5.
  - `q5.dat`: Data file used in Question 5.
- `HPC_Assignment2_Questions.pdf`: PDF containing the questions for Assignment 2.

### Assignment 3
- `CHL.pdf`: PDF with details about CLH lock.
- `CLHLock.java`: Java implementation of CLH lock.
- `Cohort.pdf`: PDF with details about Cohort lock.
- `CohortLock.java`: Java implementation of Cohort lock.
- `CohortLockList.java`: Java implementation of a list using Cohort lock.
- `Filter.pdf`: PDF with details about Filter lock.
- `FineList.java`: Java implementation of a fine-grained list.
- `HPC_Assignment3_Questions.jpg`: Image file with the questions for Assignment 3.
- `ListBackoff.pdf`: PDF with details about List with backoff.
- `ListWithBackoff.java`: Java implementation of a list with backoff.
- `List_Test.java`: Java test program for list implementations.
- `MCS.pdf`: PDF with details about MCS lock.
- `MCS_Lock.java`: Java implementation of MCS lock.
- `Team 26 HPC Assignment 3.xlsx`: Excel sheet with performance data for Assignment 3.
- `script.sh`: Shell script for running tests.

### Assignment 4
- **avl**
  - `ConcurrentAVLTree.java`: Java implementation of a concurrent AVL tree.
  - `List_Test.java`: Java test program for AVL tree.
  - `ThreadID.java`: Java class for thread identification.
  - `output.txt`: Output file for AVL tree operations.
  - `script.sh`: Shell script for AVL tree operations.
- **bst**
  - `ConcurrentBinarySearchTree.java`: Java implementation of a concurrent binary search tree.
  - `List_Test.java`: Java test program for binary search tree.
  - `ThreadID.java`: Java class for thread identification.
  - `output.txt`: Output file for binary search tree operations.
  - `script.sh`: Shell script for binary search tree operations.
- **ref hash set**
  - `List_Test.java`: Java test program for refinable hash set.
  - `RefinableHashSet.java`: Java implementation of a refinable hash set.
  - `ThreadID.java`: Java class for thread identification.
  - `output.txt`: Output file for refinable hash set operations.
  - `script.sh`: Shell script for refinable hash set operations.
- **skiplist**
  - `LazySkipList.java`: Java implementation of a lazy skip list.
  - `List_Test.java`: Java test program for lazy skip list.
  - `ThreadID.java`: Java class for thread identification.
  - `output.txt`: Output file for lazy skip list operations.
  - `script.sh`: Shell script for lazy skip list operations.
- **striped hashset**
  - `ConcurrentStripedHashSet.java`: Java implementation of a concurrent striped hash set.
  - `List_Test.java`: Java test program for striped hash set.
  - `ThreadID.java`: Java class for thread identification.
- `HPC_Assignment4_Questions.jpg`: Image file with the questions for Assignment 4.
- `Team 26 HPC Assignment 4.xlsx`: Excel sheet with performance data for Assignment 4.

## Contents
- **Source Code**: The main implementations of the problems in C, C++, and Java.
- **Problem Statements**: PDFs containing the problem statements for each assignment.
- **Performance Analysis**: Excel sheets and graphs showing the time complexity and performance with increasing number of threads.
- **Scripts**: Various shell scripts used for generating graphs and other analyses.

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
   
