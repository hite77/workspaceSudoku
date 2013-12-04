Here is the way my own SuDoKu program does it:

1) start with a complete, valid board (filled with 81 numbers)

2) make a list of all 81 cell positions and shuffle it randomly

3) As long as the list is not empty, take the next position from the list and 
remove the number from the related cell

4) test uniqueness using a fast solver (with backtracking if needed). My solver could count all solutions, but it stops when it found more than 1 solution.

5) If the current board has just one solution, goto step 3) and repeat.

6) If the current board has more than one solution, undo the last removal 
(step 3), and continue step 3 with the next position from the list

7) stop when you have tested all 81 positions.

This gives you not only unique boards, but boards where you cannot remove any 
more numbers without destroying the uniqueness of the solution.

Of course, this is only the second half of the algorithm. The first half is to 
find a complete valid board first. It works very similar, but "in the other 
direction":

1) start with an empty board

2) add a random number at one of the free cells (randomly chosen)

3) Use the solver to check if the current board has at least one valid solution. If not, undo step 2 and repeat with another number and cell

4) Repeat until the board is completely filled with numbers

Of course, in step 2 the numbers to be used are not completely chosen randomly: for each cell, the program maintains a list of numbers that are valid for this 
cell according to the SuDoKu rules. Only numbers from that list are taken into 
account.
