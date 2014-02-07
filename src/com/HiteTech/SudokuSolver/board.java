package com.HiteTech.SudokuSolver;

import java.util.Vector;

import android.os.Parcel;
import android.os.Parcelable;


public class board{
	     
	 public int[] writeBoard() 
	 {
		 int counter = 0;
		 int[] out = new int[81];
		 for (int i=0; i<9; i++)
	    		 for (int j=0; j<9; j++)
	    		 {
	    			 out[counter] = memory[i][j].getSolution();
	    			 counter += 1;
	    		 }
		 return out;
	 }
	     
	public void readBoard(int[] in) {
		int counter = 0;
		for (int i=0; i<9; i++)
			for (int j=0; j<9; j++)
			{
				memory[i][j].set(in[counter]);
				counter += 1;
			}
	     }
	
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
				if (getBoard.isGiven(i, j)) memory[i][j].setGiven(true);
				if (getBoard.isGuess(i, j)) memory[i][j].setGuess();
				
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
		if (!memory[x][y].isGiven())
			memory[x][y].set(number);
		badBoard();   // remove this from here.  Is calling badBoard every time a cell is set, even in solver.
	}

	public void setGiven(int integer, int i, int j) {
		memory[i][j].setGiven(false);
		set(integer, i, j);
		memory[i][j].setGiven(true);
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

	public boolean isGiven(int i, int j) {
		return memory[i][j].isGiven();
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

	public void resetCell(int i, int j) {
		memory[i][j].setGiven(false);
		memory[i][j].set(0);
		Vector<Integer> all = new Vector<Integer>();
		all.add(1); all.add(2); all.add(3); all.add(4); all.add(5); all.add(6);
		all.add(7); all.add(8); all.add(9);
		memory[i][j].setPossible(all);
	}		
}
