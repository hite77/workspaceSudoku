package com.HiteTech.SudokuSolver;

import java.util.Vector;

public class controller {

	private Vector<board> Boards = new Vector<board>();
	private int Position = 0;
	
	public controller() {
		board Board = new board();
		Boards.add(Board);
	}
	
	public board GetBoard() {
		return Boards.get(Position);
	}

	public int Count() {
		return Boards.size();
	}

	public void Guess(int i, int j) {
		// research way to keep several boards
		// need to copy from where cursor is at and make N number (N<=9) boards
		// place values into each one.
		Vector<Integer> possible = new Vector<Integer>(); 
		for (int loop=0; loop<GetBoard().getPossible(i, j).size(); loop++)
			possible.add(GetBoard().getPossible(i, j).get(loop));
		
		GetBoard().set(possible.get(0), i, j);
		// loop through other possibles, starting at 1....
		for (int loop=1; loop < possible.size(); ++loop)
		{
			// clone GetBoard..... create new constructor that takes a board and clones...
			board Board = new board(GetBoard());
			// set possible in correct location
			Board.set(possible.get(loop), i, j);
			// add board in after Position in vector....
			Boards.add(Position+1, Board);
		}
	}

	public void Right() {
		Position += 1;
		if (Position > Boards.size()) Position = Boards.size();
	}

	public int Position() {
		return Boards.size()+1;
	}
}
