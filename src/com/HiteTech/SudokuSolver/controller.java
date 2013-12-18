package com.HiteTech.SudokuSolver;

import java.util.Vector;

public class controller {

	private Vector<board> Boards = new Vector<board>();
	
	public controller() {
		board Board = new board();
		Boards.add(Board);
	}
	
	public board GetBoard() {
		return Boards.get(0);
	}

	public int Count() {
		return Boards.size();
	}

	public void Guess(int i, int j) {
		// research way to keep several boards
		// need to copy from where cursor is at and make N number (N<=9) boards
		// place values into each one.
		for (int easy=0; easy <8; easy++)
		{
			board Board = new board();
			Boards.add(0, Board);
		}
	}
}
