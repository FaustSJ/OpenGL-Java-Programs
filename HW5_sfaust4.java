 /*
 * Coded by Sarah Faust 11/25/15
 * This program makes a wire-frame box with a sphere, tetrahedron, and a line inside 
 * 		that bounce around.
*/

import javax.media.opengl.*;

public class HW5_sfaust4 extends J3_4_Diffuse
{
/* inherits J3_3_Ambient
 * 			J3_2_Emission
 * 			J2_13_TravelSolar
 * 			J2_12_RobotSolar
 * 			J2_11_ConeSolarCollision
 * 			J2_11_ConeSolar
 * 			J2_10_GenSolar
 * 			J2_9_Solar
 * 			J2_8_Robot3d
 * 			J2_7_Sphere
 * 			J2_6_Cylinder
 * 			J2_5_Cone
 * 			J2_4_Robot
 * 			J2_3_Robot2D
 * 			J2_0_2DTransform
 * 			J1_5_Circle
 * 			J1_4_Line
 * 			J1_3_xFont
 * 			J1_3_Triangle
 * 			J1_3_Line
 * 			J1_2_Line
 * 			J1_1_Point
 * 			J1_0_Point
 */
	
	int sphereDepth = 5;
	float sphereRadius = HEIGHT/20; //which = 40
	
	private float sphereXdir;
	private float sphereYdir;
	private float sphereZdir;
	
	private float sphereX = -150;
	private float sphereY = -150;
	private float sphereZ = -150;
	
	private float tetraXdir;
	private float tetraYdir;
	private float tetraZdir;
	
	private float tetraX = 140;;
	private float tetraY = 140;;
	private float tetraZ = 140;;
	
	private float lineXdir;
	private float lineYdir;
	private float lineZdir;
	
	private float lineX = 0;
	private float lineY = 0;
	private float lineZ = 0;
	
	//for lighting
	float white[] = {1, 1, 1, 1f};
	float black[] = {0, 0, 0, 0};
	float whitish[] = {0.8f, 0.8f, 0.8f, 1f};
	float twhite[] = {1f, 1f, 1f, 0.5f};
	float blackish[] = {0.3f, 0.3f, 0.3f, 0.3f};
/*	float redish[] = {.3f, 0, 0, 1};
	float red[] = {1f, 0, 0, 1};
	float greenish[] = {0, .3f, 0, 1};
	float green[] = {0, 1f, 0, 1};
	float blueish[] = {0, 0, .3f, 1};
	float blue[] = {0, 0, 1f, 1};
	float yellish[] = {.7f, .7f, 0.0f, 1};
	float yellow[] = {1f, 1f, 0.0f, 1};*/
	float origin[] = {0f, 0f, 0f, 1};	
	//the constuctor
	public HW5_sfaust4()
	{
		//perspective
		gl.glFrustum(400, -400, 400, -400, 400, -400);
		
		//setting up the fields that track the sphere's translations
		sphereX = -150;
		sphereY = -150;
		sphereZ = -150;
		sphereXdir = 0;
		sphereYdir = 0;
		sphereZdir = 0;
		while((sphereXdir==0)&&(sphereYdir==0)&&(sphereZdir==0))
		{
			sphereXdir = randomDirectionGen();
			sphereYdir = randomDirectionGen();
			sphereZdir = randomDirectionGen();
		}
		
		//setting up the fields that track the tetrahedron's translations
		tetraX = 140;
		tetraY = 140;
		tetraZ = 140;
		tetraXdir = 0;
		tetraYdir = 0;
		tetraZdir = 0;
		while((tetraXdir==0)&&(tetraYdir==0)&&(tetraZdir==0))
		{
			tetraXdir = randomDirectionGen();
			tetraYdir = randomDirectionGen();
			tetraZdir = randomDirectionGen();
		}
		
		//setting up the fields that track the line's translations
		lineX = 0;
		lineY = 0;
		lineZ = 0;
		lineXdir = 0;
		lineYdir = 0;
		lineZdir = 0;
		while((lineXdir==0)&&(lineYdir==0)&&(lineZdir==0))
		{
			lineXdir = randomDirectionGen();
			lineYdir = randomDirectionGen();
			lineZdir = randomDirectionGen();
		}
	}

	
	//sets up the corner spotlight
	public void init(GLAutoDrawable glDrawable) {

	    super.init(glDrawable);

	    gl.glEnable(GL.GL_LIGHTING);
	    gl.glEnable(GL.GL_NORMALIZE);

	    gl.glEnable(GL.GL_LIGHT0);
	    gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, position,0);
	    gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, white,0);

	    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, whitish,0);
	    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, black,0);
	    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, black,0);
	  }
	
	//generates a random direction for a point to move in
    public float randomDirectionGen()
    {
    	int val = Math.round((float)Math.random());
    	int pol = Math.round((float)Math.random());
		 
    	if(pol==0)
    		val = -val;
		 
    	return (float)val;
    }
    
    //allows transparency
    public void myMaterialColor(
    	      float myA[],
    	      float myD[],
    	      float myS[],
    	      float myE[]) {

    	    gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, myA,0);
    	    gl.glMaterialfv(GL.GL_FRONT, GL.GL_DIFFUSE, myD,0);
    	    gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, myS,0);
    	    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, myE,0);
    	  }
//////////////////////////////////////////////////////////////////////////////////////////////////////////    
	public void display(GLAutoDrawable glDrawable) {
		// clear both framebuffer and zbuffer
		gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
		
		myMaterialColor(blackish, whitish, white, black);
	    //draws the rotating wire box
		gl.glRotatef(1, 1, 1, 1);
		gl.glPushMatrix();
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_LINE);  //draws an outline of the following shape
		gl.glScalef(2f, 2f, 2f);
		drawBox();
		
		//moves the objects within the box
		moveObjects();
		
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL.GL_FILL); //draws the filled-in sides of the following shapes
		//draws the tetra 
		gl.glTranslatef(tetraX, tetraY, tetraZ);
		drawTetra();
		gl.glTranslatef(-tetraX, -tetraY, -tetraZ); //translate is called again to cancel the previous call
		
		//draws the line
		gl.glTranslatef(lineX, lineY, lineZ);
		drawLine();
		gl.glTranslatef(-lineX, -lineY, -lineZ); //translate is called again to cancel the previous call
		
		//draws the sphere
		gl.glTranslatef(sphereX, sphereY, sphereZ);
		gl.glScalef(sphereRadius, sphereRadius, sphereRadius);
		drawSphere();
		gl.glScalef(-sphereRadius, -sphereRadius, -sphereRadius);
		gl.glTranslatef(-sphereX, -sphereY, -sphereZ);  //translate is called again to cancel the previous call
		gl.glPopMatrix();
		
		//allows transparency
		gl.glEnable(GL.GL_BLEND);
	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

	    //drawing the visible, transparent light-source
		gl.glPushMatrix();
		gl.glScalef(50, 50, 50);
		myMaterialColor(twhite, twhite, twhite, twhite);
		gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, origin,0);
	    drawSphere();
	    gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, black,0);
		gl.glPopMatrix();
		
		try {
			Thread.sleep(10);
		} catch (Exception ignore) {
		}
	}
//////////////////////////////////////////////////////////////////////////
	//moves the objects one pixel each time display() is run
	public void moveObjects()
	{
		sphereX += sphereXdir;
		sphereY += sphereYdir;
		sphereZ += sphereZdir;
		sphereCollisionCheck();
		
		tetraX += tetraXdir;
		tetraY += tetraYdir;
		tetraZ += tetraZdir;
		tetraCollisionCheck();
		
		lineX += lineXdir;
		lineY += lineYdir;
		lineZ += lineZdir;
		lineCollisionCheck();
	}
	
	//checks if the sphere has collided with anything
	private void sphereCollisionCheck()
	{
		if((sphereX==160)||
				(sphereX==-160)||
				(Math.abs(sphereX-tetraX)<=40)||
				(Math.abs(sphereX-lineX)<=40))
		{
			sphereXdir *= -1;
			if(sphereYdir==0)
				sphereYdir = randomDirectionGen();
			else if(sphereZdir==0)
				sphereZdir = randomDirectionGen();
		}
		if((sphereY==160)||
				(sphereY==-160)||
				(Math.abs(sphereY-tetraY)<=40)||
				(Math.abs(sphereY-lineY)<=40))
		{
			sphereYdir *= -1;
			if(sphereXdir==0)
				sphereXdir = randomDirectionGen();
			else if(sphereZdir==0)
				sphereZdir = randomDirectionGen();
		}
		if((sphereZ==160)||
				(sphereZ==-160)||
				(Math.abs(sphereZ-tetraZ)<=40)||
				(Math.abs(sphereZ-lineZ)<=40))
		{
			sphereZdir *= -1;
			if(sphereYdir==0)
				sphereYdir = randomDirectionGen();
			else if(sphereXdir==0)
				sphereXdir = randomDirectionGen();
		}
	}
	
	//checks if the tetrahedron has collided with anything
	private void tetraCollisionCheck()
	{
		if((tetraX==150)||
				(tetraX==-150)||
				(Math.abs(sphereX-tetraX)<=40)||
				(Math.abs(tetraX-lineX)<=40))
		{
			tetraXdir *= -1;
			if(tetraYdir==0)
				tetraYdir = randomDirectionGen();
			else if(tetraZdir==0)
				tetraZdir = randomDirectionGen();
		}
		if((tetraY==150)||
				(tetraY==-150)||
				(Math.abs(sphereY-tetraY)<=40)||
				(Math.abs(tetraY-lineY)<=40))
		{
			tetraYdir *= -1;
			if(tetraXdir==0)
				tetraXdir = randomDirectionGen();
			else if(tetraZdir==0)
				tetraZdir = randomDirectionGen();
		}
		if((tetraZ==150)||
				(tetraZ==-150)||
				(Math.abs(sphereZ-tetraZ)<=40)||
				(Math.abs(tetraZ-lineZ)<=40))
		{
			tetraZdir *= -1;
			if(tetraYdir==0)
				tetraYdir = randomDirectionGen();
			else if(tetraXdir==0)
				tetraXdir = randomDirectionGen();
		}
	}
	
	//checks if the line has collided with anything
	private void lineCollisionCheck()
	{
		if((lineX==200)||
				(lineX==-200)||
				(Math.abs(sphereX-lineX)<=40)||
				(Math.abs(tetraX-lineX)<=40))
		{
			lineXdir *= -1;
			if(lineYdir==0)
				lineYdir = randomDirectionGen();
			else if(lineZdir==0)
				lineZdir = randomDirectionGen();
		}
		if((lineY==200)||
				(lineY==-200)||
				(Math.abs(sphereY-lineY)<=40)||
				(Math.abs(tetraY-lineY)<=40))
		{
			lineYdir *= -1;
			if(lineXdir==0)
				lineXdir = randomDirectionGen();
			else if(lineZdir==0)
				lineZdir = randomDirectionGen();
		}
		if((lineZ==200)||
				(lineZ==-200)||
				(Math.abs(sphereZ-lineZ)<=40)||
				(Math.abs(tetraZ-lineZ)<=40))
		{
			lineZdir *= -1;
			if(lineYdir==0)
				lineYdir = randomDirectionGen();
			else if(lineXdir==0)
				lineXdir = randomDirectionGen();
		}
	}
/////////////////////////////////////////////////////////////////////////	
	//The following methods do the drawing
/////////////////////////////////////////////////////////////////////////
	public void drawBox() {	  
		gl.glColor3f(1f, 1f, 1f);
		gl.glLineWidth(5);
		
		//front
		gl.glBegin(GL.GL_POLYGON);
		gl.glNormal3f(200f, 200f, 200f);
		gl.glVertex3f(200f, 200f, 200f);
		gl.glNormal3f(-200f, 200f, 200f);
		gl.glVertex3f(-200f, 200f, 200f);
		gl.glNormal3f(-200f, -200f, 200f);
		gl.glVertex3f(-200f, -200f, 200f);
		gl.glNormal3f(200f, -200f, 200f);
		gl.glVertex3f(200f, -200f, 200f);
		gl.glEnd();
		
		//back
		gl.glBegin(GL.GL_POLYGON);
		gl.glNormal3f(200f, 200f, -200f);
		gl.glVertex3f(200f, 200f, -200f);
		gl.glNormal3f(-200f, 200f, -200f);
		gl.glVertex3f(-200f, 200f, -200f);
		gl.glNormal3f(-200f, -200f, -200f);
		gl.glVertex3f(-200f, -200f, -200f);
		gl.glNormal3f(200f, -200f, -200f);
		gl.glVertex3f(200f, -200f, -200f);
		gl.glEnd();	    
		
		//right
		gl.glBegin(GL.GL_POLYGON);
		gl.glNormal3f(200f, 200f, -200f);
		gl.glVertex3f(200f, 200f, -200f);
		gl.glNormal3f(200f, 200f, 200f);
		gl.glVertex3f(200f, 200f, 200f);
		gl.glNormal3f(200f, -200f, 200f);
		gl.glVertex3f(200f, -200f, 200f);
		gl.glNormal3f(200f, -200f, -200f);
		gl.glVertex3f(200f, -200f, -200f);
		gl.glEnd();	   
		
		//left
		gl.glBegin(GL.GL_POLYGON);
		gl.glNormal3f(-200f, 200f, -200f);
		gl.glVertex3f(-200f, 200f, -200f);
		gl.glNormal3f(-200f, 200f, 200f);
		gl.glVertex3f(-200f, 200f, 200f);
		gl.glNormal3f(-200f, -200f, 200f);
		gl.glVertex3f(-200f, -200f, 200f);
		gl.glNormal3f(-200f, -200f, -200f);
		gl.glVertex3f(-200f, -200f, -200f);
		gl.glEnd();	 
		
		gl.glLineWidth(1);
		//drawing top and bottom would be redundant
	}
	
	
	//creating the tetrahedron
	private void drawTetra()
	{
		gl.glColor3f(0, 0, 1f);
		gl.glBegin(GL.GL_TRIANGLES);
	    gl.glNormal3f(0f, 80f, 0f);
	    gl.glVertex3f(0f, 80f, 0f);
	    gl.glNormal3f(0f, 0f, 50f);
	    gl.glVertex3f(0f, 0f, 50f);
	    gl.glNormal3f(50f, 0f, -50f);
	    gl.glVertex3f(50f, 0f, -50f);
	    gl.glEnd();
	    
	    gl.glColor3f(0.25f, 0.25f, 1f);
	    gl.glBegin(GL.GL_TRIANGLES);
	    gl.glNormal3f(0f, 80f, 0f);
	    gl.glVertex3f(0f, 80f, 0f);
	    gl.glNormal3f(0f, 0f, 50f);
	    gl.glVertex3f(0f, 0f, 50f);
	    gl.glNormal3f(-50f, 0f, -50f);
	    gl.glVertex3f(-50f, 0f, -50f);
	    gl.glEnd();
	    
	    gl.glColor3f(0.5f, 0.5f, 1f);
	    gl.glBegin(GL.GL_TRIANGLES);
	    gl.glNormal3f(0f, 80f, 0f);
	    gl.glVertex3f(0f, 80f, 0f);
	    gl.glNormal3f(50f, 0f, -50f);
	    gl.glVertex3f(50f, 0f, -50f);
	    gl.glNormal3f(-50f, 0f, -50f);
	    gl.glVertex3f(-50f, 0f, -50f);
	    gl.glEnd();
	    
	    gl.glColor3f(0.75f, 0.75f, 1f);
	    gl.glBegin(GL.GL_TRIANGLES);
	    gl.glNormal3f(50f, 0f, -50f);
	    gl.glVertex3f(50f, 0f, -50f);
	    gl.glNormal3f(0f, 0f, 50f);
	    gl.glVertex3f(0f, 0f, 50f);
	    gl.glNormal3f(-50f, 0f, -50f);
	    gl.glVertex3f(-50f, 0f, -50f);
	    gl.glEnd();
	}
	
	//creating the line
	private void drawLine()
	{
		//drawn as a thin rectangle so it's easier for the programmer to handle
		gl.glLineWidth(3);
		gl.glColor3f(1f, 0.20f, 1f);
		gl.glBegin(GL.GL_POLYGON);
		gl.glNormal3f(50f, 0f, -50f);
		gl.glVertex3f(50f, 0f, -50f);
		gl.glNormal3f(52f, 0f, -50f);
		gl.glVertex3f(52f, 0f, -50f);
		gl.glNormal3f(0f, 50f, 50f);
		gl.glVertex3f(0f, 50f, 50f);
		gl.glNormal3f(0f, 52f, 50f);
		gl.glVertex3f(0f, 52f, 50f);
		gl.glEnd();
		gl.glLineWidth(1);
	}
	
	//creating the sphere 
	public void subdivideSphere(float v1[], float v2[], float v3[], long depth) 
	{
		float v12[] = new float[3];
		float v23[] = new float[3];
		float v31[] = new float[3];
		int i;

		if (depth==0) 
		{
			gl.glColor3f(v1[0]*v1[0], v2[1]*v2[1], v3[2]*v3[2]);
			drawSphereTriangle(v1, v2, v3);
			return;
		}
		for (i = 0; i<3; i++) {
			v12[i] = v1[i]+v2[i];
			v23[i] = v2[i]+v3[i];
			v31[i] = v3[i]+v1[i];
		}
		normalize(v12);
		normalize(v23);
		normalize(v31);
		subdivideSphere(v1, v12, v31, depth-1);
		subdivideSphere(v2, v23, v12, depth-1);
		subdivideSphere(v3, v31, v23, depth-1);
		subdivideSphere(v12, v23, v31,depth-1);
	}

	//sphereData values borrowed from J2_7_Sphere
	public void drawSphere() 
	{
		float sphereData[][] = { {1.0f, 0.0f, 0.0f}
	    , {0.0f, 1.0f, 0.0f}
	    , {0.0f, 0.0f, 1.0f}
	    , {-1.0f, 0.0f, 0.0f}
	    , {0.0f, -1.0f, 0.0f}
	    , {0.0f, 0.0f, -1.0f}
		};
		
		subdivideSphere(sphereData[0], sphereData[1], sphereData[2], sphereDepth);
		subdivideSphere(sphereData[0], sphereData[2], sphereData[4], sphereDepth);
		subdivideSphere(sphereData[0], sphereData[4], sphereData[5], sphereDepth);
		subdivideSphere(sphereData[0], sphereData[5], sphereData[1], sphereDepth);
		
		subdivideSphere(sphereData[3], sphereData[1], sphereData[5], sphereDepth);
		subdivideSphere(sphereData[3], sphereData[5], sphereData[4], sphereDepth);
		subdivideSphere(sphereData[3], sphereData[4], sphereData[2], sphereDepth);
		subdivideSphere(sphereData[3], sphereData[2], sphereData[1], sphereDepth);
	}

/////////////////////////////////////////////////////////////////////////////
     public static void main(String[] args) 
     {
     	HW5_sfaust4 f = new HW5_sfaust4();

     	f.setTitle("JOGL J1_5_Circle");
     	f.setSize(WIDTH, HEIGHT);
     	f.setVisible(true);
     }
}