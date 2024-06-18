#!/bin/bash

./a.out 1
# Loop through numbers from 1 to 16 with a step of 2
for i in {2..16..2}; do
    # Run the executable with the current number as argument
    ./a.out $i
    echo "\n"


done
