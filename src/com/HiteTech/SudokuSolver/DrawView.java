package com.HiteTech.SudokuSolver;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
	
	
	private board Board;
	private boolean initialized = false;
	
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
//				if (Board.get(i, j) > 0)
//				{
//					// should output solution for square
//				}
				// will need to iterate over the vector, or check for each number.....
				
				canvas.drawText("1", start_x+(float)2.0, start_y+(float)12.0, paint);
				canvas.drawText("2", start_x+(cell_width/(float)2.0)-(float)2.0, 
						             start_y+(float)12.0, paint);
				canvas.drawText("3", start_x+cell_width-(float)8.0, 
						             start_y+(float)12.0, paint);
				
				canvas.drawText("4", start_x+(float)2.0, 
						             start_y+(cell_height/(float)2.0)+(float)(4.0), paint);
				canvas.drawText("5", start_x+(cell_width/(float)2.0)-(float)2.0,
						             start_y+(cell_height/(float)2.0)+(float)(4.0), paint);
				canvas.drawText("6", start_x+cell_width-(float)8.0,
			                         start_y+(cell_height/(float)2.0)+(float)(4.0), paint);
	
				
				canvas.drawText("7", start_x+(float)2.0, 
						             start_y+cell_height-(float)2.0, paint);
				canvas.drawText("8", start_x+(cell_width/(float)2.0)-(float)2.0, 
			                         start_y+cell_height-(float)2.0, paint);
				canvas.drawText("9", start_x+cell_width-(float)8.0, 
			                         start_y+cell_height-(float)2.0, paint);

				
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
	public boolean onTouch(View view, MotionEvent event)
	{
		Point point = new Point();
		point.x = event.getX();
		point.y = event.getY();
		points.add(point);
		invalidate();
		return true;
	}
}
