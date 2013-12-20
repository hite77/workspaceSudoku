package com.HiteTech.SudokuSolver;

import com.example.myfirstapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection
        switch (item.getItemId()) {
            case R.id.guessMenu:
                guessClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
	}

	private controller Controller = new controller();
    
    private boolean editMode = true;
	private DrawView Sudoku;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	Sudoku = (DrawView) findViewById(R.id.drawView1);
        Sudoku.SetBoard(Controller.GetBoard());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void HandleClick(int i) {
    	if (Sudoku.selection_x == -1) return;
    	if (editMode)
    	{
    		Controller.GetBoard().set(i, Sudoku.selection_x, Sudoku.selection_y);
    	}
    	else
    	{
    		Controller.GetBoard().toggle(i, Sudoku.selection_x, Sudoku.selection_y);
    	}
    	Sudoku.invalidate();
  }
    
    public void oneClicked(View view) {
    	HandleClick(1);
    }
    
	public void twoClicked(View view) {
		HandleClick(2);
    }
    
    public void threeClicked(View view) {
    	HandleClick(3);
    }
    
    public void fourClicked(View view) {
    	HandleClick(4);
    }
    
    public void fiveClicked(View view) {
    	HandleClick(5);
    }
    
    public void sixClicked(View view) {
    	HandleClick(6);
    }
    
    public void sevenClicked(View view) {
    	HandleClick(7);
    }
    
    public void eightClicked(View view) {
    	HandleClick(8);
    }
    
    public void nineClicked(View view) {
    	HandleClick(9);
    }
    
    public void pencilHintClicked(View view) {
    	MessageBox("pencilHint Was Clicked");
    	Controller.GetBoard().calculateHints();
    	Sudoku.invalidate();
    }
    
    public void MessageBox(String message)
    {
       Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    public void editPencilClicked(View view) {
    	editMode  = !editMode;
    	Button button = (Button) findViewById(R.id.editAndPencil);
    	if (editMode)
    	{
    		button.setText("Edit");
    	}
    	else
    	{
    		button.setText("Pencil");
    	}
    }   
    
    public void leftClicked(View view) {
    	Controller.Left();
    	Sudoku.SetBoard(Controller.GetBoard());
    	Sudoku.invalidate();
    }
    
    public void rightClicked(View view) {
    	Controller.Right();
    	Sudoku.SetBoard(Controller.GetBoard());
    	Sudoku.invalidate();
    }	    
    
    public void guessClicked() {
    	if (Sudoku.selection_x > -1)
    	{
    		Controller.Guess(Sudoku.selection_x, Sudoku.selection_y);
    		Sudoku.invalidate();
    	}
    }
}
