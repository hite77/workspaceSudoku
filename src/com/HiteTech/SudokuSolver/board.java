package com.HiteTech.SudokuSolver;

import java.util.Vector;


public class board {

	private cell[][] memory = new cell[9][9];
	private cell[][] hcells = new cell[9][9];
	private cell[][] vcells = new cell[9][9];
	private cell[][] quads = new cell[9][9];

	public board() {
		for (int i=0;i<9; i++)
			for (int j=0;j<9;j++)
				memory[i][j] = new cell();
		// assign cells to cell regions....
		for (int row=0;row<9; row++)
			for (int col=0;col<9;col++)
			{
				hcells[row][col] = memory[col][row];
		        vcells[col][row] = memory[col][row];
			}   
		setQuad(0,2,0,2,0);
		setQuad(3,5,0,2,1);
		setQuad(6,8,0,2,2);
		setQuad(0,2,3,5,3);
		setQuad(3,5,3,5,4);
		setQuad(6,8,3,5,5);
		setQuad(0,2,6,8,6);
		setQuad(3,5,6,8,7);
		setQuad(6,8,6,8,8);		
	}

	public board(board getBoard) {
		for (int i=0;i<9; i++)
			for (int j=0;j<9;j++)
			{
				memory[i][j] = new cell();
				if (getBoard.get(i, j) > 0)
				{
					int val = getBoard.get(i, j);
					memory[i][j].set(val);
				}
				else
				{
					Vector<Integer> vals = getBoard.getPossible(i, j);
					Vector<Integer> newvals = new Vector<Integer>();
					for (int z=0; z<vals.size(); z++)
						newvals.add(vals.get(z));
					memory[i][j].setPossible(newvals);
				}
			}
		// assign cells to cell regions....
		for (int row=0;row<9; row++)
			for (int col=0;col<9;col++)
			{
				hcells[row][col] = memory[col][row];
		        vcells[col][row] = memory[col][row];
			}   
		setQuad(0,2,0,2,0);
		setQuad(3,5,0,2,1);
		setQuad(6,8,0,2,2);
		setQuad(0,2,3,5,3);
		setQuad(3,5,3,5,4);
		setQuad(6,8,3,5,5);
		setQuad(0,2,6,8,6);
		setQuad(3,5,6,8,7);
		setQuad(6,8,6,8,8);
		// clone board....
		
	}

	private void setQuad(int startx, int endx, int starty, int endy, int quad) {
		int count = 0;
		for (int col=startx; col<=endx; col++)
			for (int row=starty; row<=endy; row++)
			{
				quads[quad][count] = memory[col][row];
				count = count + 1;
			}
	}

	public void set(int number, int x, int y) {
		memory[x][y].set(number);
		badBoard();   // remove this from here.  Is calling badBoard every time a cell is set, even in solver.
	}

	public void setGuess(Integer integer, int i, int j) {
		set(integer, i, j);
		memory[i][j].setGuess();
	}
	public int get(int i, int j) {
		return memory[i][j].getSolution();
	}

	public void toggle(int value, int i, int j) {
		memory[i][j].toggle(value);
		badBoard();
	}

	public Vector<Integer> getPossible(int i, int j) {
		return memory[i][j].getPossible();
	}

	public void calculateHints() {
		// loop through all cells... and remove from the three types of cells
		for (int row=0; row<9; row++)
			for (int col=0; col<9; col++)
			{
				int value = memory[col][row].getSolution();
				if (value > 0)
				{
					for (int iter=0; iter<9; iter++)
					{
						hcells[row][iter].remove(value);
						vcells[col][iter].remove(value);
						quads[calcQuad(col,row)][iter].remove(value); // need something instead of iter?  actually here it is ok....
					}
				}
			}
		badBoard();
	}

	private int calcQuad(int col, int row) {
		if (row <= 2)
		{
			if (col <= 2)
				return 0;
			if ((col >= 3) && (col<= 5))
				return 1;
			return 2;
		}
		if ((row >= 3) && (row <= 5))
		{
			if (col <= 2)
				return 3;
			if ((col >= 3) && (col<= 5))
				return 4;
			return 5;
			
		}
		if (row >= 6)
		{
		  if (col <=2)
			  return 6;
		  if ((col>= 3)&&(col<=5))
			  return 7;
		}
		return 8;	
		
	}

	public boolean badBoard() {
		boolean returnValue = false;
		
		// clear all bad first
		for (int i=0; i<9; i++)
			for (int j=0; j<9; j++)
				memory[i][j].setBad(false);
		
		// 1 check for a blank hint somewhere is bad if this is so, and no solution
		if (blankHintFound())
			returnValue = true;
		// 2 check for two of same solution in same rank
		if (twoDuplicateNumbers())
			returnValue = true;
		// 3 check for three or more sets of same 2 hints.
		if (threeOrMoreDoubleHints())
			returnValue = true;
		return returnValue;
	}

	private boolean twoDuplicateNumbers() {
		boolean returnValue = false;
		for (int i=0; i<9; i++)
		{
			if (lookForSingles(hcells[i])) returnValue = true;
			if (lookForSingles(vcells[i])) returnValue = true;
			if (lookForSingles(quads[i]))  returnValue = true;
		}
		return returnValue;
	}

	private boolean lookForSingles(cell[] cells) {
		// read in values, if in vector, set them to bad....
		boolean returnValue = false;
		Vector<Integer> singles = new Vector<Integer>();
		for (int i=0; i<9; i++)
		{
			int singleValue = pullSingle(cells[i]);
			if ((singleValue != 0) && singles.contains(singleValue))
			{
				cells[i].setBad(true);
				for (int j=0; j<singles.size(); j++)
					if (singleValue == singles.elementAt(j))
						cells[j].setBad(true);
				returnValue = true;
			}
			singles.add(singleValue);
		}
		return returnValue;
	}

	private int pullSingle(cell cell) {
		int returnValue = 0;
		if (cell.getSolution() > 0)
			returnValue = cell.getSolution();
		else if (cell.getPossible().size() == 1)
			returnValue = cell.getPossible().elementAt(0);
		return returnValue;
	}

	private boolean threeOrMoreDoubleHints() { 
		boolean returnValue = false;
		for (int i=0; i<9; i++)
		{
			if (lookForDoubles(hcells[i])) returnValue = true;
			if (lookForDoubles(vcells[i])) returnValue = true;
			if (lookForDoubles(quads[i]))  returnValue = true;
		}
		return returnValue;
	}

	public class twoHints {
		public int first;
		public int second;
	}
	
	private boolean lookForDoubles(cell[] cells) {
				boolean returnValue = false;
				Vector<twoHints> doubles = new Vector<twoHints>();
				
				for (int i=0; i<9; i++)
				{
					twoHints doublesValue = pullDouble(cells[i]);
					
					if (doublesValue.first != 0 
					  && indexOf(doubles, doublesValue) > -1
					  && lastIndexOf(doubles, doublesValue) > -1
					  && indexOf(doubles, doublesValue) != lastIndexOf(doubles, doublesValue))
					{
						cells[i].setBad(true);
						cells[indexOf(doubles, doublesValue)].setBad(true);
						cells[lastIndexOf(doubles, doublesValue)].setBad(true);
						returnValue = true;
					}
					doubles.add(doublesValue);
				}
				return returnValue;
	}

	private int indexOf(Vector<twoHints> doubles, twoHints doublesValue) {
		int returnValue = -1;
		for (int i=0; i<doubles.size(); i++)
			if (  doubles.elementAt(i).first == doublesValue.first
			   && doubles.elementAt(i).second== doublesValue.second) 
			{
				returnValue = i;
			}
		return returnValue;
	}
	
	private int lastIndexOf(Vector<twoHints> doubles, twoHints doublesValue) {
		int returnValue = -1;
		for (int i=doubles.size()-1; i>-1; i--)
			if (  doubles.elementAt(i).first == doublesValue.first
			   && doubles.elementAt(i).second== doublesValue.second) 
			{
				returnValue = i;
			}
		return returnValue;
	}

	private twoHints pullDouble(cell cell) {
		twoHints result = new twoHints();
		result.first = 0;
		result.second = 0;
		
		if (cell.getPossible().size() == 2)
		{
			result.first = cell.getPossible().elementAt(0);
			result.second = cell.getPossible().elementAt(1);
		}
		return result;
	}

	private boolean blankHintFound() {
		boolean returnValue = false;
		for (int i=0; i<9; i++)
			for (int j=0; j<9; j++)
			{
				if (memory[i][j].getPossible().isEmpty() && memory[i][j].getSolution() == 0)
				{
					memory[i][j].setBad(true);
					returnValue = true;
				}
			}
		return returnValue;
	}
	
	public boolean getBad(int i, int j) {
		return memory[i][j].getBad();
	}

	// note ... refactor todo .... write getCell method, and replace getGuess, getBad... here 
	public boolean isGuess(int i, int j) {
		return memory[i][j].isGuess();
	}

	public boolean solutionBoard() {
		for (int i=0; i<9; i++)
			for (int j=0; j<9; j++)
				if (memory[i][j].getSolution() == 0) return false;
		if (badBoard()) return false;
		return true;
	}

	public boolean convertHintToSolution() {
		boolean returnValue = false;
		for (int i=0; i<9; i++)
			for (int j=0; j<9; j++)
				if (memory[i][j].getPossible().size()==1) 
					{
					memory[i][j].set(memory[i][j].getPossible().firstElement());
					returnValue = true;
					}
		return returnValue;
	}

	public twoHints leastHints() {
		twoHints location = new twoHints();
		location.first = 0; location.second=0;
		
		int countOfHints=9;
		
		for (int i=0; i<9; i++)
			for (int j=0; j<9; j++)
			{
				int size = memory[i][j].getPossible().size();
				if ((size < countOfHints) && (size > 1))
				{
					countOfHints = size;
					location.first = i;
					location.second = j;
				}
			}
		
		return location;
	}

	public boolean BruteForce() {
		// returns false if no solution.....
		
		// need to track cells as we go, and if solution already in there don't set / backtrack to it
		
		//int i=0; int j=0; //work x first, then y.... need to stop at 8.
		// either figure out a recursive way and/or straight forward way to backtrack...
		
		// find first cell without solution....
//		while((memory[i][j].getSolution() != 0) && (j < 9))
//		{
//			i++;
//			if(i==9)
//			{
//				i=0;
//				j++;
//			}
//		}
//		
//		if (j==9)
//		{
//			if (solutionBoard()) return true; // is already a solution to start with
//			return false; // is not a valid solution to start with
//		}
//		return (calculateCell(i, j, 1) && solutionBoard()); // start recursive algorithm
		return false;
	}
	public boolean calculateCell (int i, int j, int value)
	{
		if (i==9) return true;
		boolean isSolutionCell = true;
		if (memory[i][j].getSolution() == 0)
		{
			isSolutionCell = false;
			memory[i][j].set(value);
			while (twoDuplicateNumbers())
			{
				value++;
				if (value == 10)
				{
					memory[i][j].set(0);
					return false;
				}
				memory[i][j].set(value);
			}
		}
		if (!calculateCell (i+1, j, 1))
		{
			if (!isSolutionCell) memory[i][j].set(0);
			if (value+1 == 10)
			{
				return false;
			}
			if(!calculateCell(i, j, value+1))
				return false;
		}
		return true;
		// cycle thru cells with no solution....
		// try and place 1's, then 2's...etc... up to 9....
		// if none work... i.e. 9, then backtrack, and move that number up...

		// let’s see…. set i,j to value
//		int originali = i;
//		int originalj = j;
//		memory[i][j].set(value);
		// see if valid…… (no dups)
		// invalid…. try next higher number
//		while(twoDuplicateNumbers())
//		{
//			value++;
//			if (value==10)
//			{
//				memory[i][j].set(0); // backtracking
//				return false;
//			}
//			memory[i][j].set(value);
//		}
		// valid… call next i, j value….
		//   note call calculate cell for next value...
		//   skip over cells with a solution….
//		while((j<9) && (memory[i][j].getSolution() != 0))
//		{
//			i++;
//			if (i>8)
//			{
//				i=0;
//				j++;
//			}
//		}
//		if (j==9)
//		{
			// stepped off end of board
//			return true; // this will be the last call.....
//		}
		//  if next calculateCell failed…..
//		value = 1;
//		while((value != 10) && !calculateCell(i, j, value))
//		{
//			value++;
//		}
//		if(value !=10)
//			return true;
		// need to increment this cell... and repeat all this logic.... so call calculateCell
//		value = memory[originali][originalj].getSolution();
//		if (value==9) 
//			{
//			memory[originali][originalj].set(0); // backtracking
//			return false;
//			}
//		return calculateCell(originali, originalj, value+1);
	}
		
}
