package com.HiteTech.SudokuSolver;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import com.example.myfirstapp.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
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
    	
    	String filename = "sudokuBoards";
    	board Board = Controller.GetBoard();
    	
    	try {
            FileInputStream inputStream = openFileInput(filename);
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            
            int i=0;
            int j=0;
            
            
            while ((line = r.readLine()) != null) {	
            	if (i==9)
            	{
            		// reset count
            		i=0; 
            		j=0;
            		//build new board
            		Controller.AddBoard();
            		Controller.Right();
            		Board = Controller.GetBoard();
            	}
            
            	// logic....	
            	if (line.substring(0, 1).compareTo("S") == 0)
            	{
            		Board.set(Integer.parseInt(line.substring(1, 2)), i, j);	
            	}
            	else if (line.substring(0, 1).compareTo("H") == 0)
            	{
            		for (int loop=1; loop< line.length(); loop++)
            			Board.toggle(Integer.parseInt(line.substring(loop, loop+1)), i, j);
            	}
            	// increment....
            	j++;
            
            	if (j==9)
            	{
            		j=0;
            		i=i+1;
            	}    
            }
            MessageBox("Done loading Sudoku");
            Sudoku.invalidate();
            
            r.close();
            inputStream.close();
       
        } catch (Exception e) {
           // problem with file....  do nothing... should have empty board.
        }
    	
    	while (Controller.LeftEnabled())
   	 	{
    		Controller.Left();
   	 	} 
        Sudoku.SetBoard(Controller.GetBoard());
    }
	
	private void persist()
	{
		String filename = "sudokuBoards";

	     FileOutputStream outputStream = null;
		
	     try 
	     {
	    	 outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
		 
	    	 // go to first board....
	    	 while (Controller.LeftEnabled())
	    	 {
	    		 Controller.Left();
	    	 } 
	         
	    	 board currentBoard = Controller.GetBoard();
	    	 OutputBoard(currentBoard, outputStream);
	         
	    	 // if right enabled... then do the others
	    	 while ((Controller.RightEnabled()))
	    	 {
	    		 Controller.Right();
	    		 currentBoard = Controller.GetBoard();
	    		 OutputBoard(currentBoard, outputStream);
	    	 }	 
			outputStream.close();
		} 
		catch (IOException e) 
		{
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		persist();
	}

	@Override
    protected void onStop(){
       super.onStop();
       persist();  
     }

    private void OutputBoard(board currentBoard, FileOutputStream outputStream) {
		
    	String output = "";
    	
    	for (int i=0; i<9; i++)
       	 for (int j=0; j<9; j++)
       	 {
       		 int cellSolution = currentBoard.get(i,j);
       		 if (cellSolution > 0)
       		 {
       			 output = "S" + Integer.toString(cellSolution);
       		 }
       		 else
       		 {
       			 output = "H";
       			 Vector<Integer> cellPossible = currentBoard.getPossible(i, j);
       			 if (!cellPossible.contains(1))
       				 output = output + "1";
       			 if (!cellPossible.contains(2))
       				 output = output + "2";
       			 if (!cellPossible.contains(3))
       				 output = output + "3";
       			 if (!cellPossible.contains(4))
       				 output = output + "4";
       			 if (!cellPossible.contains(5))
       				 output = output + "5";
       			 if (!cellPossible.contains(6))
       				 output = output + "6";
       			 if (!cellPossible.contains(7))
       				 output = output + "7";
       			 if (!cellPossible.contains(8))
       				 output = output + "8";
       			 if (!cellPossible.contains(9))
       				 output = output + "9";
       		 }
       		 try {
				outputStream.write(output.getBytes());
				outputStream.write(System.getProperty("line.separator").getBytes());
			  } 
       		 catch (IOException e) 
       		 {
			 }
       		 
       	 }
       
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
    
    public void solveClicked(View view) {
    	Controller.Solve(false);
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
