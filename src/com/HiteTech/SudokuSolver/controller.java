package com.HiteTech.SudokuSolver;

import java.util.Random;
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
		if ((GetBoard().getPossible(i, j).size() <= 1) || (GetBoard().badBoard())) return;
		
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

	public void SetPosition(int p) {
		Position = p-1;
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

	public boolean SolverIteration()
	{
		board Board = GetBoard();
		if (Board.solutionBoard())
		{
			return true;
		}
		
		if (Board.badBoard())
		{
			if (Count() == 1)
			{
				return false;
			}
			Delete();
			return true;
		}
		
		Board.calculateHints();
		if (!Board.convertHintToSolution())
		{
			twoHints locationhints = Board.leastHints();
			Guess(locationhints.first, locationhints.second);
		}
		return true;
	}
	
	public boolean solveMulti() {
		ClearOtherBoards();
		boolean badLastBoard = solverLoop();
		if (badLastBoard) return false;
		
		boolean noMoreBoards = !RightEnabled();
		if (noMoreBoards) return true;
		
		Right();
		
		boolean noOtherSolutions = solverLoop();
		if (noOtherSolutions) return true;
		
		return false;
	}
	
	private boolean solverLoop()
	{
		boolean badLastBoard = false;
		while((!GetBoard().solutionBoard()) && (!badLastBoard))
		{
			badLastBoard = !SolverIteration();
		}
		return badLastBoard;
	}
	
	public boolean solveOne() 
	{
		ClearOtherBoards();
		
		boolean badLastBoard = solverLoop();
		
		ClearOtherBoards();
		return !badLastBoard;
		
//		//setup variables in while
//		boolean keepgoing = true;
//		int numberOfSolutions = 0;
//		
//		while ((keepSolving) || (keepgoing))
//		{
//			boolean helpSolved = false;
//			////////////////if (GetBoard().badBoard())/// in there
//			{
//				//try to delete.... if it does not work.... aka last board... exit
//				if ((Count() == 1) || (!RightEnabled()))
//				{
//					if (numberOfSolutions == 0) Reset();
//					//exit out...
//					keepgoing = false;
//					keepSolving = false;
//				}
//				////////////////////Delete();
//			}
//			else
//			{
//				//////////////////////GetBoard().calculateHints(); 
//				/////////////////////helpSolved = GetBoard().convertHintToSolution(); 
//				
//				if (GetBoard().solutionBoard())  // exit out....
//				{
//					keepgoing = false;
//					numberOfSolutions++;
//					if (numberOfSolutions>1) keepSolving = false;
//					if ((keepSolving) && (RightEnabled()))
//					{
//						Right();
//					}
//					else
//					{
//						keepSolving = false;
//					}
//				}
//				else if(!helpSolved)
//				{
//					twoHints location = GetBoard().leastHints();
//					Guess(location.first, location.second);
//				}
//			}
//		}
//		MoveToLeft();
//		ClearOtherBoards();
//		if (numberOfSolutions==1) return true;
//		return false;
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

	class element {
		public int x;
		public int y;
	}
	
//	public void Generate() {
//		Reset();
//		//first part -- build a board.....
//		board filledBoard = new board();
//		Vector<element> coords = new Vector<element>();
//		
//		for (int i=0; i<9; i++)
//			for (int j=0; j<9;j++)
//			{
//				element newElement = new element();
//				newElement.x = i;
//				newElement.y = j;
//				coords.add(newElement);
//			}
//		
//		Random randomGenerator = new Random();
//		
//		while (coords.size() > 0)
//		{
//			// randomly pick from list.... 0 to size -1.... 
//			int randomInt;
//			if (coords.size() == 1)
//				randomInt = 0;
//			else
//				randomInt = randomGenerator.nextInt(coords.size());
//			//copy hints from selected cell
//			Vector<Integer> possible = filledBoard.getPossible(coords.get(randomInt).x, coords.get(randomInt).y);
//			if (possible.size() == 0)
//			{
//				// this was set already.....
//				coords.remove(randomInt);
//			}
//			else if (possible.size() == 1)
//			{
//				// only one possible.  Convert it to solution.
//				filledBoard.convertHintToSolution();
//				filledBoard.calculateHints();
//			
//				coords.remove(randomInt);
//			}
//			else
//			{
//				// pick from one of the possible and try solving....
//				// if it solves ok with one solution, then keep it, otherwise don't
//				int pick = randomGenerator.nextInt(possible.size());
//				pick = possible.elementAt(pick);
//				board copyBoard = new board(filledBoard);
//				copyBoard.set(pick, coords.get(randomInt).x, coords.get(randomInt).y);
//				filledBoard.toggle(pick, coords.get(randomInt).x, coords.get(randomInt).y);
//				// if it solves on copyBoard then apply.  Otherwise.... don't.
//				
//				Boards.add(Position+1, copyBoard);
//				Right();
//				if (solveOne())
//				{
//					// keep the number
//					filledBoard.set(pick, coords.get(randomInt).x, coords.get(randomInt).y);
//					// filter out other possibilities
//					filledBoard.calculateHints();
//				}
//			}
//		}
//		
//		// have the board now... now to randomly try to hide all 81....
//		for (int i=0; i<9; i++)
//			for (int j=0; j<9;j++)
//			{
//				element newElement = new element();
//				newElement.x = i;
//				newElement.y = j;
//				coords.add(newElement);
//			}
//		// shuffle these boards into new vector....
//		Vector<element> tryHide = new Vector<element>();
//		for (int i=0; i<9; i++)
//			for (int j=0; j<9;j++)
//			{
//				int r = randomGenerator.nextInt(coords.size());
//				element NewElement = new element();
//				NewElement.x = coords.elementAt(r).x;
//				NewElement.y = coords.elementAt(r).y;
//				tryHide.add(NewElement);
//				coords.remove(r);				
//			}
//		// ok now we have random order in tryHide...
//		for (int i=0; i<81; i++)
//		{
//			// create a clone of the board....
//			board clone = new board(filledBoard);
//			// remove the hide element.....
//			clone.toggle(1, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			clone.toggle(2, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			clone.toggle(3, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			clone.toggle(4, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			clone.toggle(5, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			clone.toggle(6, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			clone.toggle(7, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			clone.toggle(8, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			clone.toggle(9, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			Reset();
//			Boards.add(clone);
//			Position += 1;
//			ClearOtherBoards();
//			if (solveMulti())
//			{
//				// make the hide permanent by toggling all pencil hints on
//				filledBoard.toggle(1, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//				filledBoard.toggle(2, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//				filledBoard.toggle(3, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//				filledBoard.toggle(4, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//				filledBoard.toggle(5, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//				filledBoard.toggle(6, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//				filledBoard.toggle(7, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//				filledBoard.toggle(8, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//				filledBoard.toggle(9, tryHide.elementAt(i).x, tryHide.elementAt(i).y);
//			}
//		}
//		
//		// finally show the board with cells hidden
//		Boards.add(filledBoard);
//		Position += 1;
//		ClearOtherBoards();
//	}

	public board stage1()
	{
		return null;
	}
	
	public boolean stage1step(int x, int y, board board)
	{
		// randomly pick from the list of cells
		Vector<Integer> possible = board.getPossible(x, y);
		Random randomGenerator = new Random();
		int randomInt = 0;
		if (possible.size() > 1)
			randomInt = randomGenerator.nextInt(possible.size());
		board.toggle(possible.get(randomInt), x, y);
		applyHintRandomized(x, y, randomInt, board);
		return true;
	}
	
	public boolean applyHintRandomized(int x, int y, int valuePicked, board board) {
		Reset();
		board solveBoard = new board(board);
		Boards.add(0, solveBoard);
		if (!solveOne()) return false;
		
		Vector<Integer> possible = board.getPossible(x, y);
		board.set(possible.get(valuePicked-1), x, y);
		return true;
	}	
}
