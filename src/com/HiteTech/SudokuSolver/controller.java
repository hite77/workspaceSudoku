package com.HiteTech.SudokuSolver;

import java.util.Vector;

import com.HiteTech.SudokuSolver.board.twoHints;

public class controller {

	private Vector<board> Boards = new Vector<board>();
	//private board UndoBoard = new board();
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
		if (GetBoard().getPossible(i, j).size() == 0) return;
		
		Vector<Integer> possible = new Vector<Integer>(); 
		for (int loop=0; loop<GetBoard().getPossible(i, j).size(); loop++)
			possible.add(GetBoard().getPossible(i, j).get(loop));
		
		GetBoard().setGuess(possible.get(0), i, j);
		// loop through other possibles, starting at 1....
		for (int loop=possible.size()-1; loop > 0; --loop)
		{
			// clone GetBoard..... create new constructor that takes a board and clones...
			board Board = new board(GetBoard());
			// set possible in correct location
			Board.setGuess(possible.get(loop), i, j);
			// add board in after Position in vector....
			Boards.add(Position+1, Board);
		}
	}

	public void Left() {
		Position -= 1;
		if (Position < 0) Position = 0;
	}
	
	public void Right() {
		Position += 1;
		if (Position >= Boards.size()) Position = Boards.size()-1;
	}

	public int Position() {
		return Position+1;
	}

	public boolean LeftEnabled() {
		if (Position > 0) return true;
		return false;
	}

	public boolean RightEnabled() {
		if (Position < Boards.size()-1) 
	    {
			return true; 
	    }
		return false;
	}

	public void Delete() {
		if (Boards.size() > 1)
		{
			Boards.remove(Position);
			if (Position >= Boards.size()) Position = Boards.size()-1;
		}
	}

	public void Undo() {
		// TODO Auto-generated method stub
		
	}

	public void Reset() {
		while (Boards.size() > 1)
		{
			Boards.remove(Position);
			if (Position >= Boards.size()) Position = Boards.size()-1;
		}
		Boards.remove(0);
		AddBoard();
	}

	public void AddBoard() {
		board Board = new board();
		Boards.add(Board);
	}

	public boolean Solve(boolean keepSolving) { // still working on this to make sure it will exhaustively look up solutions...
		ClearOtherBoards();
		//setup variables in while
		boolean keepgoing = true;
		boolean immediatelyQuit = false;
		int numberOfSolutions = 0;
		
		while ((keepSolving) || (keepgoing))
		{
			boolean loopOnce = true;
			while (loopOnce)
			{
				loopOnce = false;
				GetBoard().calculateHints(); 
				if (GetBoard().convertHintToSolution()) // repeat while....
				{
					loopOnce = true;
				}
				if (GetBoard().solutionBoard())  // exit out....
				{
					loopOnce = false;
					keepgoing = false;
					numberOfSolutions++;
					if (numberOfSolutions>1) keepSolving = false;
					if (RightEnabled())
					{
						Right();
					}
					else
					{
						keepSolving = false;
					}
				}
				if (GetBoard().badBoard())
				{
					//try to delete.... if it does not work.... aka last board... exit
					if ((Count() == 1) || (!RightEnabled()))
					{
						if (numberOfSolutions == 0) Reset();
						//exit out...
						loopOnce = false;
						keepgoing = false;
						keepSolving = false;
						immediatelyQuit = true;
					}
					Delete();
				}
			}
		    if (!immediatelyQuit)
		    {
		    	twoHints location = GetBoard().leastHints();
		    	Guess(location.first, location.second);
		    }
		}
		MoveToLeft();
		ClearOtherBoards();
		if (numberOfSolutions==1) return true;
		return false;
	}

	private void MoveToLeft() {
		while (LeftEnabled())
		{
			Left();
		}
	}
	
	private void ClearOtherBoards() {
		while (LeftEnabled())
		{
			Left();
			Delete();
		}
		
		Right();
		
		while (Count() > 1)
		{
			Delete();
		}
	}

	public void Generate() {
		// TODO Auto-generated method stub
		
	}	
}
