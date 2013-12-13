package com.HiteTech.SudokuSolver;

import java.util.Vector;


public class cell {

	Vector<Integer> possible = new Vector<Integer>();
	
	public cell()
	{
		possible.add(1);
		possible.add(2);
		possible.add(3);
		possible.add(4);
		possible.add(5);
		possible.add(6);
		possible.add(7);
		possible.add(8);
		possible.add(9);
	}
	
	private int solution = 0;
	
	public int getSolution() {
		return solution;
	}
	
	public void remove(int i) {
		if (possible.indexOf(i) >= 0)
			possible.remove(possible.indexOf(i));
		
//		if (possible.size() == 1)
//		{
//			solution = possible.get(0);
//		}
	}

	public void set(int i) {
		possible.clear();
		solution = i;
	}

	public void toggle(int value) {
		// need this to do something, and add test to cell....
		if (possible.contains(value))
		{
			possible.remove(possible.indexOf(value));
		}
		else
		{
			possible.add(value);
		}
	}

	public Vector<Integer> getPossible() {
		return possible;
	}
}
