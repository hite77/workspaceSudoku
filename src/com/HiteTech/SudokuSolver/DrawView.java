package com.HiteTech.SudokuSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.HiteTech.SudokuSolver.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class DrawView extends View implements OnTouchListener {
	
	public int selection_x = 0; int selection_y = 0;
	
	private Button Btn1 = null;
	private Button Btn2 = null;
	private Button Btn3 = null;
	private Button Btn4 = null;
	private Button Btn5 = null;
	private Button Btn6 = null;
	private Button Btn7 = null;
	private Button Btn8 = null;
	private Button Btn9 = null;
	
	
	private boolean Given = true;
	private boolean EditMode = true;
	private board Board;
	private boolean initialized = false;
	private View ParentContext;
	
	public void setMode(boolean editMode)
	{
		EditMode = editMode;
	}
	
	public void setParentContext(View findViewById) {
		ParentContext = findViewById;
		
		if (ParentContext != null)
		{
			Btn1 = (Button) ParentContext.findViewById(R.id.oneButton);
			Btn2 = (Button) ParentContext.findViewById(R.id.twoButton);
			Btn3 = (Button) ParentContext.findViewById(R.id.threeButton);
			Btn4 = (Button) ParentContext.findViewById(R.id.fourButton);
			Btn5 = (Button) ParentContext.findViewById(R.id.fiveButton);
			Btn6 = (Button) ParentContext.findViewById(R.id.sixButton);
			Btn7 = (Button) ParentContext.findViewById(R.id.sevenButton);
			Btn8 = (Button) ParentContext.findViewById(R.id.eightButton);
			Btn9 = (Button) ParentContext.findViewById(R.id.nineButton);
		}
	}
	
	public void SetBoard(board inputBoard)
	{
		initialized  = true;
		Board = inputBoard;
		invalidate();
	}
	
	List<Point> points = new ArrayList<Point>();
	Paint paint = new Paint();
	
	public DrawView(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
	}
	
	public DrawView(Context context, AttributeSet attrs) { 
		super( context, attrs );
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
	}
		 
	public DrawView(Context context, AttributeSet attrs, int defStyle) {	 
		super( context, attrs, defStyle );
		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
	}
	
	private void updateButtonColor(Button btn, int number)
	{
		
    Vector <Integer> possible = Board.getPossible(selection_x, selection_y);
    
	if (((EditMode ) && (Board.get(selection_x, selection_y) == number)) || 
		(!EditMode ) && (possible.contains(number)))
		{
			if (btn != null)
			{
				if (Given)
					btn.setBackgroundColor(Color.GREEN);
				else
					btn.setBackgroundColor(Color.CYAN);
			}
		}
		else
		{
			if (btn != null)
			{
				btn.setBackgroundColor(Color.LTGRAY);
			}
		}
	}
	
	public void updateButtonColors()
	{
		updateButtonColor(Btn1, 1);
		updateButtonColor(Btn2, 2);
		updateButtonColor(Btn3, 3);
		updateButtonColor(Btn4, 4);
		updateButtonColor(Btn5, 5);
		updateButtonColor(Btn6, 6);
		updateButtonColor(Btn7, 7);
		updateButtonColor(Btn8, 8);
		updateButtonColor(Btn9, 9);
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth((float) 0.0);
		//top line
		canvas.drawLine(0, 0, (float)canvas.getWidth(), 0, paint);
		//right line
		canvas.drawLine((float)canvas.getWidth(), 0, (float)canvas.getWidth(), (float)canvas.getHeight(), paint);
		// bottom line
		canvas.drawLine((float)canvas.getWidth(), (float)canvas.getHeight(), 
				         0, (float)canvas.getHeight(), paint);
		// left line
		canvas.drawLine(0, (float)canvas.getHeight(), 0, 0, paint);
		
		// draw grid....
		// _________  left, right, top, bottom.....
		// |       |  0-width / 9.....
		// |       |  0-height / 9....
		// |       |
		// |       |
		// _________
		
		float cell_width = (float)canvas.getWidth() / (float)9.0;
		float cell_height = (float)canvas.getHeight() / (float)9.0;
		float start_x, start_y;
		
		// draws grid...
		for (int i=0; i<9; i++)
		{
			for (int j=0; j<9; j++)
			{
				start_x = (float)i * cell_width;
				start_y = (float)j * cell_height;
				
				
				if (initialized)
				{
				if (Board.isGuess(i, j))
				{
					fillWithColor(canvas, cell_width, cell_height, start_x,
							start_y, Color.YELLOW);
				}
				if (Board.isGiven(i, j))
				{
					fillWithColor(canvas, cell_width, cell_height, start_x,
							start_y, Color.GREEN);
				}
				if (Board.getBad(i, j))
				{
					fillWithColor(canvas, cell_width, cell_height, start_x,
							start_y, Color.RED);
				}
				}
				// points... first entry... if it exists fill with blue....
				if (points.size() == 1)
				{
					float x = points.get(0).x;
					float y = points.get(0).y;
					
					if ((x > start_x) && (x < start_x+cell_width))
						if ((y > start_y) && (y < start_y+cell_height))
						{
							// set selection
							selection_x = i; selection_y = j;
							
							fillWithColor(canvas, cell_width, cell_height, start_x,
									start_y, Color.CYAN);
							
							updateButtonColors();
						}
				}
				
				
				canvas.drawLine(start_x, start_y, 
						        start_x+cell_width, start_y, paint); // top
				canvas.drawLine(start_x+cell_width, start_y, 
						        start_x+cell_width, start_y+cell_height, paint); // right
				canvas.drawLine(start_x+cell_width, start_y+cell_height, 
						        start_x, start_y+cell_height, paint); // bottom
				canvas.drawLine(start_x, start_y+cell_height, 
						        start_x, start_y, paint); // left
				
				if (initialized)
				{
				if (Board.get(i, j) > 0)
				{
					// should output solution for square
					float original = paint.getTextSize();
					
					paint.setTextSize((float) (original*1.8));
					canvas.drawText(Integer.toString(Board.get(i,j)), start_x+(cell_width/(float)2.0)-(float)2.0,
				             start_y+(cell_height/(float)2.0)+(float)(4.0), paint);
					paint.setTextSize(original);
				}
				
				
				// will need to iterate over the vector, or check for each number.....
				Vector <Integer> possible = Board.getPossible(i, j);
				
				if (possible.contains(1))
				{
					canvas.drawText("1", start_x+(float)2.0, start_y+(float)12.0, paint);
				}
				
				if (possible.contains(2))
				{
					canvas.drawText("2", start_x+(cell_width/(float)2.0)-(float)2.0, 
				             start_y+(float)12.0, paint);
				
				}
				
				if (possible.contains(3))
				{
					canvas.drawText("3", start_x+cell_width-(float)8.0, 
				             start_y+(float)12.0, paint);
					
				}
				
				if (possible.contains(4))
				{
					canvas.drawText("4", start_x+(float)2.0, 
				             start_y+(cell_height/(float)2.0)+(float)(4.0), paint);
					
				}
				
				if (possible.contains(5))
				{
					canvas.drawText("5", start_x+(cell_width/(float)2.0)-(float)2.0,
				             start_y+(cell_height/(float)2.0)+(float)(4.0), paint);
					
				}
				
				if (possible.contains(6))
				{
					canvas.drawText("6", start_x+cell_width-(float)8.0,
	                         start_y+(cell_height/(float)2.0)+(float)(4.0), paint);
					
				}
				
				if (possible.contains(7))
				{
					canvas.drawText("7", start_x+(float)2.0, 
				             start_y+cell_height-(float)2.0, paint);
					
				}
				
				if (possible.contains(8))
				{
					canvas.drawText("8", start_x+(cell_width/(float)2.0)-(float)2.0, 
	                         start_y+cell_height-(float)2.0, paint);
					
				}
				
				if (possible.contains(9))
				{
					canvas.drawText("9", start_x+cell_width-(float)8.0, 
	                         start_y+cell_height-(float)2.0, paint);
					
				}				
	
				// need to separate draws to another class.
				// and store coordinates
				// so we can tell when each is clicked on....
				// also need to find coordinates for the big numbers for solution numbers
				}
				
			}
		}
		// draw darker lines...
		paint.setStrokeWidth((float) 2.0);
		cell_width = (float)3.0*cell_width;
		cell_height = (float)3.0*cell_height;
		for (int i=0; i<3; i++)
		{
			for (int j=0; j<3; j++)
			{
				start_x = (float)i * cell_width;
				start_y = (float)j * cell_height;
				
				canvas.drawLine(start_x, start_y, 
						        start_x+cell_width, start_y, paint); // top
				canvas.drawLine(start_x+cell_width, start_y, 
						        start_x+cell_width, start_y+cell_height, paint); // right
				canvas.drawLine(start_x+cell_width, start_y+cell_height, 
						        start_x, start_y+cell_height, paint); // bottom
				canvas.drawLine(start_x, start_y+cell_height, 
						        start_x, start_y, paint); // left
			}
		}
		
		//String text = String.format("Get Stroke Width:%f", paint.getStrokeWidth());
		//canvas.drawText(text, (float)50.0, (float)50.0, paint);
		
	}

	private void fillWithColor(Canvas canvas, float cell_width,
			float cell_height, float start_x, float start_y, int color) {
		// set color and fill
		paint.setColor(color);
		canvas.drawRect(start_x, start_y, start_x+cell_width, 
				start_y+cell_height, paint);
		// set color back
		paint.setColor(Color.BLACK);
	}
	
	public boolean onTouch(View view, MotionEvent event)
	{
		Point point = new Point();
		point.x = event.getX();
		point.y = event.getY();
		points.clear();
		points.add(point);
		invalidate();
		return true;
	}
}
