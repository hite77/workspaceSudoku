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
	}


	public int get(int i, int j) {
		return memory[i][j].getSolution();
	}

	public void toggle(int value, int i, int j) {
		memory[i][j].toggle(value);
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

}
