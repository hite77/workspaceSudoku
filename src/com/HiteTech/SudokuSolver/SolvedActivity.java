package com.HiteTech.SudokuSolver;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SolvedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solved);
		
		board Board = new board();
		int[] array = getIntent().getExtras().getIntArray("Board");
		Board.readBoard(array);
		setBoard(Board);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.solved, menu);
		return true;
	}
	
	public void setBoard(board solvedBoard)
	{
		DrawView drawView = (DrawView) findViewById(R.id.drawViewSolved);
		drawView.SetBoard(solvedBoard);
		drawView.invalidate();
	}
	
	public void resetClicked(View view) 
	{
		// return from intent and cause reset to happen....
		Intent data = new Intent();
		if (getParent() == null) {
		    setResult(Activity.RESULT_OK, data);
		} else {
		    getParent().setResult(Activity.RESULT_OK, data);
		}
		finish();
	}
}
