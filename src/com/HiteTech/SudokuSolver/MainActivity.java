package com.HiteTech.SudokuSolver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

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
            case R.id.Delete:
            	deleteClicked();
            	return true;
            case R.id.undo:
            	return true;
            case R.id.resetAll:
            	Controller.Reset();
            	Sudoku.SetBoard(Controller.GetBoard());
            	Sudoku.invalidate();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
	}

	private void deleteClicked() {
		Controller.Delete();
		Sudoku.SetBoard(Controller.GetBoard());
		Sudoku.invalidate();
	}

	private controller Controller = new controller();
    
    private boolean editMode = true;
	private DrawView Sudoku;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    	Sudoku = (DrawView) findViewById(R.id.drawView1);
        Sudoku.setParentContext(findViewById(R.id.mainRelativeLayout1));
        Sudoku.setMode(editMode);
        Sudoku.SetBoard(Controller.GetBoard());
        Sudoku.updateButtonColors();
    	
    	// pull persistance and place into controller, or build a new one?
//    	Call openFileInput() and pass it the name of the file to read. This returns a FileInputStream.
//    	Read bytes from the file with read().
//    	Then close the stream with close().
    	
    	
//    	String FILENAME = "hello_file";
//    	String string;
//    	FileInputStream fis = openFileInput(FILENAME);
//    	fis.read();
//    	fis.close();
//    	
    	
    	String filename = "myfile";
        //String outputString = "Hello world!";

//        try {
//            FileOutputStream outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
//            outputStream.write(outputString.getBytes());
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
        try {
            FileInputStream inputStream = openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            inputStream.close();
        } catch (Exception e) {
           // problem with file....  do nothing... should have empty board.
        }
    	
    	
    }
	
	@Override
    protected void onStop(){
       super.onStop();
       // persist stuff here..... from controller.... 
//       String FILENAME = "hello_file";
//       String string = "hello world!";
//
//       FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//       fos.write(string.getBytes());
//       fos.close();
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
    	Sudoku.setMode(editMode);
    	Button button = (Button) findViewById(R.id.editAndPencil);
    	if (editMode)
    	{
    		button.setText("Edit");
    	}
    	else
    	{
    		button.setText("Pencil");
    	}
    	Sudoku.invalidate();
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
