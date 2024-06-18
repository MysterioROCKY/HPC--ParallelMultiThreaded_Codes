set logscale x 2
set term pdf size 4,3
set output "CHL.pdf"
set xtics font "Bold, 8"
set ytics font "Bold, 8"
set xlabel "Thread" font "Bold, 9"
set ylabel "Time" font "Bold, 9"
set title "Time vs. Thread"
plot "CHL.dat" using 1:2 title "" with linespoint lw 2 ps 1