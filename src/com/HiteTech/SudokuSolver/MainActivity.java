package com.HiteTech.SudokuSolver;

import com.example.myfirstapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    public board Board = new board();
	private boolean editMode = true;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void oneClicked(View view) {
    	MessageBox("One Was Clicked");
    	DrawView sudoku = (DrawView) findViewById(R.id.drawView1);
        sudoku.SetBoard(Board);
    }
    
    public void twoClicked(View view) {
    	MessageBox("Two Was Clicked");
    }
    
    public void threeClicked(View view) {
    	MessageBox("Three Was Clicked");
    }
    
    public void fourClicked(View view) {
    	MessageBox("Four Was Clicked");
    }
    
    public void fiveClicked(View view) {
    	MessageBox("Five Was Clicked");
    }
    
    public void sixClicked(View view) {
    	MessageBox("Six Was Clicked");
    }
    
    public void sevenClicked(View view) {
    	MessageBox("Seven Was Clicked");
    }
    
    public void eightClicked(View view) {
    	MessageBox("Eight Was Clicked");
    }
    
    public void nineClicked(View view) {
    	MessageBox("Nine Was Clicked");
    }
    
    public void pencilHintClicked(View view) {
    	MessageBox("pencilHint Was Clicked");
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
}
