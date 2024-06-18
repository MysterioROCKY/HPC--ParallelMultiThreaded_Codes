#!/bin/bash

# Define the output file
output_file="output.txt"

# Clear the output file if it exists
> $output_file

# Define the list of values for i
values=(1 2 4 6 8 10 12 14 16)
val=(1 2)
for j in "${val[@]}"
do
  echo "$j" >> $output_file
  # Loop through each value of i and execute the Java command
  for i in "${values[@]}"
  do
    #echo "Executing: java List_Test $i 1000000 10000 50 30 35" >> $output_file
    java List_Test $i 1000000 10000 50 100 0 >> $output_file
    echo -e "\n" >> $output_file
  done
done
echo "Execution completed. Output saved in $output_file"

