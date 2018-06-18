//Programmed by Sarah Faust, 9/19/2015
/*
	This program draws a circle from a series of points, then has another point
	of a different color travel around the circle.
*/

import javax.media.opengl.*;

public class HW1_sfaust3 extends J1_1_Point{

	//ponitX and pointY are the coordinates for the point traveling around the circle.
	double pointX;
	double pointY;
	//spot defines the point's location on the circle
	double spot;
	
	//the constructor
	public HW1_sfaust3()
	{
		spot = 0;
		//the coordinates are set upon a location of the circle
		//HEIGHT and WIDTH are defined in J1_0_Point
		pointX = (HEIGHT/4)*Math.cos(spot)+(WIDTH/2);
		pointY = (HEIGHT/4)*Math.sin(spot)+(HEIGHT/2);
	}
	
	//This runs the program, titles it, sizes it, and makes it visible.
	public static void main(String[] args)
	{
		HW1_sfaust3 f = new HW1_sfaust3();
		f.setTitle("JOGL HW1_SFAUST3");
		f.setSize(WIDTH,HEIGHT);
		f.setVisible(true);
	}
	
	//Sets up the buffer to start drawing
	public void init(GLAutoDrawable drawable)
	{
		super.init(drawable);
		
		//There are two drawing buffers, we are only drawing in the back one
		gl.glDrawBuffer(GL.GL_BACK);
		
		//J1_1_Point loops through display for us using animator
	}
	
	//display does the actual drawing
	public void display(GLAutoDrawable drawable)
	{
		//The screen is cleared before any new drawing starts
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		//The color and size of the points making up the circle are defined
		gl.glColor3d(1.0f, 0.0f, 1.0f);
		gl.glPointSize(1);
		
		//drawing the circle
		double degree = 0; //like spot, it tracks which circle point we're on
		double increase = 0.01; //how much degree will change for each loop
		//the coordinates of the points making the circle
		double x = (HEIGHT/4)*Math.cos(degree)+(WIDTH/2);
		double y = (HEIGHT/4)*Math.sin(degree)+(HEIGHT/2);
		//the loop draws a point over and over, in the shape of a circle
		while(degree<2*Math.PI)
		{
			drawPoint(x,y);
			degree += increase;
			x = (HEIGHT/4)*Math.cos(degree)+(WIDTH/2);
			y = (HEIGHT/4)*Math.sin(degree)+(HEIGHT/2);
		}
		
		//draws the point that travels around the circle
		gl.glColor3d(0.0f, 1.0f, 1.0f);
		gl.glPointSize(4);
		drawPoint(pointX, pointY);
		spot += 0.01;
		pointX = (HEIGHT/4)*Math.cos(spot)+(WIDTH/2);
		pointY = (HEIGHT/4)*Math.sin(spot)+(HEIGHT/2);
		
		//slows the drawing down, so the point is easier to visually track
		try{
			Thread.sleep(10);
		}catch(Exception ignore){}
	}
	
}
