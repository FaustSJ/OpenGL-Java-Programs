/*
 * Coded by Sarah Faust 10/5/15
 * This program makes a dot, line, and triangle whose points bounce between a circle and a rectangle.
*/

import javax.media.opengl.*;

import com.sun.opengl.util.GLUT;

public class HW2_sfaust3 extends J2_0_2DTransform
{
/*
 * inherits J1_5_Circle
 * 			J1_4_Line
 * 			J1_3_xFont
 * 			J1_3_Triangle
 * 			J1_3_Line
 * 			J1_2_Line
 * 			J1_1_Point
 * 			J1_0_Point
 */
     static int depth = 5; // number of subdivisions
     static int cRadius = HEIGHT/16;
     //point = {x, y, xdirection, ydirection}
     double point1[] = new double[4];
     double point2[] = new double[4];
     double point3[] = new double[4];
     double point4[] = new double[4];
     double point5[] = new double[4];
     double point6[] = new double[4];

     // vertex data for the triangles
     static float cVdata[][] = { { 1.0f, 0.0f, 0.0f }, { 0.0f, 1.0f, 0.0f },
          { -1.0f, 0.0f, 0.0f }, { 0.0f, -1.0f, 0.0f } };


     public HW2_sfaust3()
     {
    	 float p1[] = randomDirectionGen();
    	 float p2[] = randomDirectionGen();
    	 float p3[] = randomDirectionGen();
    	 float p4[] = randomDirectionGen();
    	 float p5[] = randomDirectionGen();
    	 float p6[] = randomDirectionGen();
    	 
    	 point1[0] = -150f;
    	 point1[1] = 150f;
    	 point1[2] = p1[0];
    	 point1[3] = p1[1];
    	 point2[0] = -5f;
    	 point2[1] = 150f;
    	 point2[2] = p2[0];
    	 point2[3] = p2[1];
    	 point3[0] = 150f;
    	 point3[1] = 150f;
    	 point3[2] = p3[0];
    	 point3[3] = p3[1];
    	 
    	 point4[0] = -150f;
    	 point4[1] = -150f;
    	 point4[2] = p4[0];
    	 point4[3] = p4[1];
    	 point5[0] = -5f;
    	 point5[1] = -150f;
    	 point5[2] = p5[0];
    	 point5[3] = p5[1];
    	 point6[0] = 150f;
    	 point6[1] = -150f;
    	 point6[2] = p6[0];
    	 point6[3] = p6[1];
     }
     
     //generates a random direction for a point to move in
     public float[] randomDirectionGen()
     {
    	 int xval = 0;
    	 int yval = 0;
    	 while((xval==0)&&(yval==0))
    	 {
    		 xval = Math.round((float)Math.random());
    		 yval = Math.round((float)Math.random());
    	 }
    	 int xpol = Math.round((float)Math.random());
		 int ypol = Math.round((float)Math.random());
		 
		 if(ypol==0)
			 yval = -yval;
		 if(xpol==0)
			 xval = -xval;
		 
		 float coor[] = {(float)xval, (float)yval};
		 return coor;
     }
//////////////////////////////////////////////////////////////////////////////////    
     public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) 
     {
          super.reshape(drawable, x, y, w, h); 
		
          //1. specify drawing into only the back_buffer
          gl.glDrawBuffer(GL.GL_BACK); 

          //2. origin at the center of the drawing area
          gl.glMatrixMode(GL.GL_PROJECTION);
          gl.glLoadIdentity();
          gl.glOrtho(-w / 2, w / 2, -h / 2, h / 2, -1, 1);

          // matrix operation on MODELVIEW matrix
          gl.glMatrixMode(GL.GL_MODELVIEW);
          gl.glLoadIdentity();

          //3. interval to swap buffers to avoid rendering too fast
          gl.setSwapInterval(1);
     }

     // Called for OpenGL rendering every reshape
     public void display(GLAutoDrawable drawable) 
     {
          //clears the screen
          gl.glClear(GL.GL_COLOR_BUFFER_BIT);
          gl.glColor3d(1.0, 1.0, 1.0);
          
        //draws the circle
          drawCircle(cRadius, depth);
          
        // Draws the Rectangle
          gl.glLineWidth(5);
          gl.glBegin(GL.GL_LINE_LOOP);
          gl.glVertex3f(-250f, 200f, 0);
          gl.glVertex3f(250f, 200f, 0);
          gl.glVertex3f(250f, -200f, 0);
          gl.glVertex3f(-250f, -200f, 0);
          gl.glEnd();
        
        //The clipping window
          gl.glBegin(GL.GL_LINE_LOOP);
          gl.glVertex2f(500f, 470f);
          gl.glVertex2f(600f, 470f);
          gl.glVertex2f(600f, 330f);
          gl.glVertex2f(500f, 330f);
          gl.glEnd();
          
        //moves the points
          movePoints();
          
        //makes the points
          gl.glPointSize(5);
          gl.glColor3d(1.0, 1.0, 0.0);
          drawPoint(point6[0], point6[1]);
          //line formed using the line-making method from J1_3_Line
          gl.glColor3d(1.0, 0.0, 0.0);
          bresenhamLine((int)point1[0], (int)point1[1], (int)point2[0], (int)point2[1]);
          //forming the triangle with a method from J1_5_Circle
          gl.glColor3d(1.0, 0.0, 1.0);
          gl.glPointSize(2);
          float vert1[] = {(float)point3[0], (float)point3[1], 0};
          float vert2[] = {(float)point4[0], (float)point4[1], 0};
          float vert3[] = {(float)point5[0], (float)point5[1], 0};
          drawtriangle(vert1, vert2, vert3);
        //labeling the triangle's points (borrowing GLUT from J1_3_xFont)
          gl.glColor3d(1.0, 1.0, 0.0);
          gl.glWindowPos3f((float)point3[0]+300f, (float)point3[1]+360f, 0f);
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "("+String.valueOf((int)point3[0]));
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, ","+String.valueOf((int)point3[1])+")");
          gl.glWindowPos3f((float)point4[0]+300f, (float)point4[1]+360f, 0f);
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "("+String.valueOf((int)point4[0]));
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, ","+String.valueOf((int)point4[1])+")");
          gl.glWindowPos3f((float)point5[0]+300f, (float)point5[1]+360f, 0f);
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "("+String.valueOf((int)point5[0]));
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, ","+String.valueOf((int)point5[1])+")");
        //labeling the line's points
          gl.glWindowPos3f((float)point1[0]+300f, (float)point1[1]+360f, 0f);
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "("+String.valueOf((int)point1[0]));
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, ","+String.valueOf((int)point1[1])+")");
          gl.glWindowPos3f((float)point2[0]+300f, (float)point2[1]+360f, 0f);
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "("+String.valueOf((int)point2[0]));
          glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, ","+String.valueOf((int)point2[1])+")");
     }
     
     public void movePoints()
     {
    	 int newdirec = 0;
    	 
    	//moves the points
         point1[0] += point1[2];
         point1[1] += point1[3];
         point2[0] += point2[2];
         point2[1] += point2[3];
         point3[0] += point3[2];
         point3[1] += point3[3];
         point4[0] += point4[2];
         point4[1] += point4[3];
         point5[0] += point5[2];
         point5[1] += point5[3];
         point6[0] += point6[2];
         point6[1] += point6[3];
         
         //checks collision with the rectangle
         if((point1[0]==245)||(point1[0]==-245))
         {
        	 point1[2] = -point1[2];
        	 if(point1[3]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point1[3] = (float)newdirec;
        	 }
         }
         if((point1[1]==195)||(point1[1]==-195))
         {
        	 point1[3] = -point1[3];
        	 if(point1[2]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point1[2] = (float)newdirec;
        	 }
         }
         ///
         if((point2[0]==245)||(point2[0]==-245))
         {
        	 point2[2] = -point2[2];
        	 if(point2[3]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point2[3] = (float)newdirec;
        	 }
         }
         if((point2[1]==195)||(point2[1]==-195))
         {
        	 point2[3] = -point2[3];
        	 if(point2[2]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point2[2] = (float)newdirec;
        	 }
         }
         ///
         if((point3[0]==245)||(point3[0]==-245))
         {
        	 point3[2] = -point3[2];
        	 if(point3[3]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point3[3] = (float)newdirec;
        	 }
         }
         if((point3[1]==195)||(point3[1]==-195))
         {
        	 point3[3] = -point3[3];
        	 if(point3[2]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point3[2] = (float)newdirec;
        	 }
         }
         ///
         if((point4[0]==245)||(point4[0]==-245))
         {
        	 point4[2] = -point4[2];
        	 if(point4[3]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point4[3] = (float)newdirec;
        	 }
         }
         if((point4[1]==195)||(point4[1]==-195))
         {
        	 point4[3] = -point4[3];
        	 if(point4[2]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point4[2] = (float)newdirec;
        	 }
         }
         ///
         if((point5[0]==245)||(point5[0]==-245))
         {
        	 point5[2] = -point5[2];
        	 if(point5[3]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point5[3] = (float)newdirec;
        	 }
         }
         if((point5[1]==195)||(point5[1]==-195))
         {
        	 point5[3] = -point5[3];
        	 if(point5[2]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point5[2] = (float)newdirec;
        	 }
         }
         ///
         if((point6[0]==245)||(point6[0]==-245))
         {
        	 point6[2] = -point6[2];
        	 if(point6[3]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point6[3] = (float)newdirec;
        	 }
         }
         if((point6[1]==195)||(point6[1]==-195))
         {
        	 point6[3] = -point6[3];
        	 if(point6[2]==0)
        	 {
        		 newdirec = Math.round((float)Math.random());
        		 if (newdirec==0)
        			 newdirec = -1;
        		 point6[2] = (float)newdirec;
        	 }
         }
         
         //checks for collisions with the circle
         if(Math.abs(point1[0])+Math.abs(point1[1])<=55)
         {
        	 point1[2] = -point1[2];
        	 point1[3] = -point1[3];
         }
         if(Math.abs(point2[0])+Math.abs(point2[1])<=55)
         {
        	 point2[2] = -point2[2];
        	 point2[3] = -point2[3];
         }
         if(Math.abs(point3[0])+Math.abs(point3[1])<=55)
         {
        	 point3[2] = -point3[2];
        	 point3[3] = -point3[3];
         }
         if(Math.abs(point4[0])+Math.abs(point4[1])<=55)
         {
        	 point4[2] = -point4[2];
        	 point4[3] = -point4[3];
         }
         if(Math.abs(point5[0])+Math.abs(point5[1])<=55)
         {
        	 point5[2] = -point5[2];
        	 point5[3] = -point5[3];
         }
         if(Math.abs(point6[0])+Math.abs(point6[1])<=55)
         {
        	 point6[2] = -point6[2];
        	 point6[3] = -point6[3];
         }
     }
//--------------------------------------------------------------------------
     // draw a circle with center at the origin in xy plane
     public void drawCircle(int cRadius, int depth) 
     {
          subdivideCircle(cRadius, cVdata[0], cVdata[1], depth);
          subdivideCircle(cRadius, cVdata[1], cVdata[2], depth);
          subdivideCircle(cRadius, cVdata[2], cVdata[3], depth);
          subdivideCircle(cRadius, cVdata[3], cVdata[0], depth);
     }

     // subdivide a triangle recursively, and draw them
     private void subdivideCircle(int radius, float[] v1, float[] v2, int depth) 
     {
    	float v11[] = new float[3];
     	float v22[] = new float[3];
     	float v00[] = { 0, 0, 0 };
     	float v12[] = new float[3];

     	//stops the recursion
     	if (depth == 0)
     	{
     		for (int i = 0; i < 3; i++) 
     		{
     			v11[i] = v1[i] * radius;
     			v22[i] = v2[i] * radius;
     		} 
     		drawtriangle(v11, v22, v00);
     		return;
     	}

     	v12[0] = v1[0] + v2[0];
     	v12[1] = v1[1] + v2[1];
     	v12[2] = v1[2] + v2[2];

     	normalize(v12);

     	// subdivide a triangle recursively, and draws them
    	subdivideCircle(radius, v1, v12, depth - 1);
     	subdivideCircle(radius, v12, v2, depth - 1); 
     }

     public void drawtriangle(float[] v1, float[] v2, float[] v3) 
     {
     	gl.glBegin(GL.GL_TRIANGLES);
     	gl.glVertex3fv(v1, 0);
     	gl.glVertex3fv(v2, 0);
     	gl.glVertex3fv(v3, 0);
     	gl.glEnd();
     }	
/////////////////////////////////////////////////////////////////////////////
     public static void main(String[] args) 
     {
     	HW2_sfaust3 f = new HW2_sfaust3();

     	f.setTitle("JOGL J1_5_Circle");
     	f.setSize(WIDTH, HEIGHT);
     	f.setVisible(true);
     }
}