set logscale x 2
set term pdf size 4,3
set output "mpp2.pdf"
set xtics font "Bold, 8"
set ytics font "Bond, 8"
set xlabel "Number of Threads" font "Bold, 9"
set ylabel "Execution Time(milliseconds)" font "Bold, 9"
set title "Matrix Multiplication (mpp2)"
plot "plotme.dat" using 1:2 title "Matrix Size = 2048" with linespoint lw 2 ps 1
