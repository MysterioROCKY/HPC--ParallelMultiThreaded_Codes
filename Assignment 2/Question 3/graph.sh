set logscale x 2
unset label
#set key above
set term pdf size 16,3
set output "graph1.pdf"
set xtics(1,2,4,6,8,12,14,16) font "Bold,7" offset 0,graph 0.05
set ytics font "Bold,7"
set xlabel "Number of Threads" font "Bold,6" offset 0,0.5
set ylabel "Execution Time" font "Bold,6" offset 2,0
set key at screen 0.80,0.90 font ",7" vertical sample 0.12 spacing 0.1 width 30 height 1.0 maxrows 1
set multiplot layout 1,3


set title "Merge Sort" font "Bold,7" offset -1.0,-2.0
set size 0.22,0.9
plot "merge_sort.dat" using 1:2 title "" with linespoint lw 2 ps 0.25 linecolor rgb "red",\

set title "Quick Sort" font "Bold,7" offset -1.0,-2.0
set size 0.22,0.9
plot "quick_sort.dat" using 1:2 notitle "" with linespoint lw 2 ps 0.25 linecolor rgb "red",\

set title "Count Sort" font "Bold,7" offset -1.0,-2.0
set size 0.22,0.9
plot "count_sort.dat" using 1:2 notitle "" with linespoint lw 2 ps 0.25 linecolor rgb "red",\



