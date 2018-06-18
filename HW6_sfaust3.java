import java.awt.image.*;
import javax.media.opengl.*;
import java.io.*;
import javax.imageio.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class HW6_sfaust3  extends J4_6_Texture {
	// name for texture objects
	final int[] IRIS_TEX = new int[1];
	final int[] EARTH_TEX = new int[1];
	final int[] STARS_TEX = new int[1];

	byte[] img;
	int imgW, imgH, imgType;

	//	  int cnt = 0;
	//	  int depth = 0;


	boolean myCameraView = true;

	float O = 0;
	float A = (float)0.3*WIDTH;
	float B = (float)0.55*WIDTH;
	float C = (float)0.7*WIDTH;

	float PI = (float)Math.PI;

	float cylinderm = 300;
	float tiltAngle = 45;
	float lightAngle = 0;
	float flip = 1;

	//for lighting
	float white[] = {1, 1, 1, 1f};
	float black[] = {0, 0, 0, 0};
	float whitish[] = {0.8f, 0.8f, 0.8f, 1f};
	float blackish[] = {0.3f, 0.3f, 0.3f, 0.3f};
	float red[] = {1f, 0, 0, 1};
	float green[] = {0, 1f, 0, 1};
	float blue[] = {0, 0, 1f, 1};
	float origin[] = {0f, 0f, 0f, 1};	
	float tred[] = {1, 0, 0, 0.8f};
	float tgreen[] = {0, 1, 0, 0.3f};
	float tblue[] = {0, 0, 1, 0.2f};

	float spot_direction[] = {-1, 0, 0, 1};

	float alpha=-40, beta=-40, gama=60, dalpha = 1f, dbeta = 1.2f, dgama = -2f;

	/////////////////////////////////////////////////////////////////////////////////////	  
	void initTexture() 
	{
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // Perspective correction

		// initialize IRIS1 texture obj
		gl.glGenTextures(1, IntBuffer.wrap(IRIS_TEX));
		gl.glBindTexture(GL.GL_TEXTURE_2D, IRIS_TEX[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		readImage("IRIS1.JPG");
		System.out.println("BufferedImage TYPE_3BYTE_BGR 5: " + imgType);
		//TYPE_BYTE_GRAY  10
		//TYPE_3BYTE_BGR 	5

		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, imgW, imgH, 0, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, ByteBuffer.wrap(img));

		// initialize EARTH texture obj
		gl.glGenTextures(1, IntBuffer.wrap(EARTH_TEX));
		gl.glBindTexture(GL.GL_TEXTURE_2D, EARTH_TEX[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		readImage("EARTH2.JPG");
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB8, imgW, imgH, 0, GL.GL_BGR, GL.GL_UNSIGNED_BYTE, ByteBuffer.wrap(img));

		// initialize STARS texture obj
		gl.glGenTextures(1, IntBuffer.wrap(STARS_TEX));
		gl.glBindTexture(GL.GL_TEXTURE_2D, STARS_TEX[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		readImage("STARS.JPG");
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_LUMINANCE, imgW, imgH, 0, GL.GL_LUMINANCE, GL.GL_UNSIGNED_BYTE, ByteBuffer.wrap(img));
	}

	public void display(GLAutoDrawable drawable) 
	{

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		// texture on a quad covering most of the drawing area
		drawTexture(-2.4f*WIDTH, -2.4f*HEIGHT, -1.9f*WIDTH);

		displayView();
	}


	public void displayView() 
	{
		cnt++;
		depth = (1/100)%6;

		// cnt = 220; // for color plates

		if (cnt%60==0) {
			dalpha = -dalpha;
			dbeta = -dbeta;
			dgama = -dgama;
		}
		alpha += dalpha;
		beta += dbeta;
		gama += dgama;

		gl.glPushMatrix();
		// look at the solar system from the moon
		if (cnt%1000<500 || myCameraView) { myCamera(A, B, C, alpha, beta, gama);}

		drawRobot(O, A, B, C, alpha, beta, gama);
		gl.glPopMatrix();

		try {
			Thread.sleep(15);
		} catch (Exception ignore) {}
	}



	void myCamera(float A, float B, float C, float alpha, float beta, float gama) 
	{
		float E = WIDTH/4;
		float e = 2.5f*cnt;
		float M = WIDTH/6;

		//1. camera faces the negative x axis
		gl.glRotatef(-90, 0, 1, 0);

		//2. camera on positive x axis
		gl.glTranslatef(-M*2, 0, 0);

		//3. camera rotates with the cylinder
		gl.glRotatef(-cylinderm, 0, 1, 0);

		// and so on reversing the solar transformation
		gl.glTranslatef(0, -E, 0);
		gl.glRotatef(-tiltAngle, 0, 0, 1); // tilt angle
		// rotating around the "sun"; proceed angle
		gl.glRotatef(-e, 0, 1, 0);

		// and reversing the robot transformation
		gl.glTranslatef(-C+B, 0, 0);
		gl.glRotatef(-gama, 0, 0, 1);
		gl.glTranslatef(-B+A, 0, 0);
		gl.glRotatef(-beta, 0, 0, 1);
		gl.glTranslatef(-A, 0, 0);
		gl.glRotatef(-alpha, 0, 0, 1);
		gl.glRotatef(-cnt, 0, 1, 0);
	}

	public void readImage(String fileName) 
	{
		File f = new File(fileName);
		BufferedImage bufimg;

		try {
			// read the image into BufferredImage structure
			bufimg = ImageIO.read(f);
			imgW = bufimg.getWidth();
			imgH = bufimg.getHeight();
			imgType = bufimg.getType();
			System.out.println(fileName + " -- BufferedImage WIDTH&HEIGHT: " + imgW + ", " + imgH);
			System.out.println("BufferedImage type TYPE_3BYTE_BGR 5; GRAY 10: " + imgType);
			//TYPE_BYTE_GRAY  10
			//TYPE_3BYTE_BGR 	5

			// retrieve the pixel array in raster's databuffer
			Raster raster = bufimg.getData();

			DataBufferByte dataBufByte = (DataBufferByte)raster.getDataBuffer();
			img = dataBufByte.getData();
			System.out.println("Image data's type TYPE_BYTE 0: " + dataBufByte.getDataType());
			// TYPE_BYTE 0

		} catch (IOException ex) {
			System.exit(1);
		}
	}

	public void drawTexture(float x, float y, float z) 
	{

		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);

		gl.glEnable(GL.GL_TEXTURE_2D);
		{
			gl.glBegin(GL.GL_QUADS);
			gl.glTexCoord2f(0.0f, 4.0f);
			gl.glVertex3f(x, y, z);
			gl.glTexCoord2f(4.0f, 4.0f);
			gl.glVertex3f(-x, y, z);
			gl.glTexCoord2f(4.0f, 0.0f);
			gl.glVertex3f(-x, -y, z);
			gl.glTexCoord2f(0.0f, 0.0f);
			gl.glVertex3f(x, -y, z);
			gl.glEnd();
		}
		gl.glDisable(GL.GL_TEXTURE_2D); 
	}

	// normalize a vector to unit vector
	public void normalize(float[] vector) {
		//  Auto-generated method stub
		float d = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);

		if (d == 0) {
			System.err.println("0 length vector: normalize().");
			return;
		}
		vector[0] /= d;
		vector[1] /= d;
		vector[2] /= d;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

	 void drawRobot (float O, float A, float B, float C, float alpha, float beta, float gama) 
	 {

		    // Global coordinates
		    gl.glLineWidth(4);
		    drawColorCoord(WIDTH/8, WIDTH/8, WIDTH/8);

		    gl.glPushMatrix();

		    gl.glRotatef(cnt, 0, 1, 0);
		    gl.glRotatef(alpha, 0, 0, 1);
		    gl.glTranslatef(A, 0, 0);
		    gl.glRotatef(beta, 0, 0, 1);
		    gl.glTranslatef(B-A, 0, 0);
		    gl.glRotatef(gama, 0, 0, 1);

		    // put the solar system at the end of the robot arm
		    gl.glTranslatef(C-B, 0, 0);
		    drawSolar(WIDTH/4, 2.5f*cnt, WIDTH/6, 1.5f*cnt);

		    gl.glPopMatrix();
	}
	
 	  public void drawSolar(float E, float e, float M, float m) {

    gl.glLineWidth(2);
    drawColorCoord(WIDTH/6, WIDTH/6, WIDTH/6);

    gl.glPushMatrix();
    {
      gl.glRotatef(e, 0, 1, 0);
      // rotating around the "sun"; proceed angle
      gl.glRotatef(tiltAngle, 0, 0, 1); // tilt angle
      gl.glTranslated(0, 2*E, 0);

      gl.glPushMatrix();
      gl.glTranslatef(0, 1.5f*E, 0);
      gl.glScalef(E*2, E*1.5f, E*2);
      gl.glRotatef(-(m+e)/4, 0, 1, 0); // self rotation
      drawTetra();
      gl.glPopMatrix();

      gl.glEnable(GL.GL_BLEND);
      gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

      if (lightAngle==10) {
        flip = -1;
      }
      if (lightAngle==-85) {
        flip = 1;
      }
      lightAngle += flip;

      gl.glRotatef(m, 0, 1, 0); // 1st moon
      gl.glDepthMask(false);
      gl.glPushMatrix();
      {
        gl.glTranslated(2.5*M, 0, 0);
        gl.glLineWidth(1);
        drawColorCoord(WIDTH/4, WIDTH/4, WIDTH/4);

        // light source rot up and down on earth center line
        gl.glRotatef(lightAngle, 0, 0, 1);

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, origin,0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPOT_DIRECTION, spot_direction,0);
        gl.glLightf(GL.GL_LIGHT1, GL.GL_SPOT_CUTOFF, 30);
        gl.glPushMatrix();
        myMaterialColor(red, red, red, red);
        gl.glScalef(E/8, E/8, E/8);
        drawTetra(); // light source with cutoff=15
        gl.glPopMatrix();

        // lighting cone corresponds to the light source
        gl.glScaled(2.5*M, 2.5*M*Math.tan(PI*15/180), 2.5*M*Math.tan(PI*15/180));
        gl.glTranslatef(-1, 0, 0);
        gl.glRotatef(90, 0, 1, 0); // orient the cone
        myMaterialColor(tred, tred, tred, tred); // trans.
        drawCone();
      }
      gl.glPopMatrix();

      gl.glRotatef(120, 0, 1, 0); // 2nd moon
      gl.glPushMatrix();
      {
        gl.glTranslated(2.5*M, 0, 0);
        drawColorCoord(WIDTH/4, WIDTH/4, WIDTH/4);
        gl.glRotatef(lightAngle, 0, 0, 1);
        gl.glLightfv(GL.GL_LIGHT2, GL.GL_POSITION, origin,0);
        gl.glLightfv(GL.GL_LIGHT2, GL.GL_SPOT_DIRECTION, spot_direction,0);
        gl.glLightf(GL.GL_LIGHT2, GL.GL_SPOT_CUTOFF, 30f);
        myMaterialColor(green, green, green, green);
        gl.glPushMatrix();
        gl.glScalef(E/8, E/8, E/8);
        drawTetra();
        gl.glPopMatrix();

        gl.glScaled(2.5*M, 2.5*M*Math.tan(PI*15/180), 2.5*M*Math.tan(PI*15/180));
        gl.glTranslatef(-1, 0, 0);
        gl.glRotatef(90, 0, 1, 0); // orient the cone
        myMaterialColor(tgreen, tgreen, tgreen, tgreen);
        drawCone();
      }
      gl.glPopMatrix();

      gl.glRotatef(120, 0, 1, 0); // 3rd moon
      gl.glTranslated(2.5*M, 0, 0);
      gl.glRotatef(lightAngle, 0, 0, 1);
      gl.glLightfv(GL.GL_LIGHT3, GL.GL_POSITION, origin,0);
      gl.glLightfv(GL.GL_LIGHT3, GL.GL_SPOT_DIRECTION, spot_direction,0);
      gl.glLightf(GL.GL_LIGHT3, GL.GL_SPOT_CUTOFF, 40f);
      drawColorCoord(WIDTH/4, WIDTH/4, WIDTH/4);
      myMaterialColor(blue, blue, blue, blue);
      gl.glPushMatrix();
      gl.glScalef(E/8, E/8, E/8);
      drawTetra();
      gl.glPopMatrix();

      gl.glScaled(2.5*M, 2.5*M*Math.tan(PI*20/180), 2.5*M*Math.tan(PI*20/180));
      gl.glTranslatef(-1f, 0f, 0f);
      gl.glRotatef(90, 0f, 1f, 0f); // orient the cone
      myMaterialColor(tblue, tblue, tblue, tblue);
      drawCone();
      gl.glMaterialfv(GL.GL_FRONT, GL.GL_EMISSION, black,0);
    }
    gl.glPopMatrix();
    gl.glDepthMask(true); // turn off emission
    gl.glDisable(GL.GL_BLEND);
    myMaterialColor(blackish, whitish, white, black);
  }
	
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

	  private void subdivideCone(float v1[], float v2[],
                             int depth) {

	  float v11[] = {0, 0, 0};
	  float v22[] = {0, 0, 0};
	  float v00[] = {0, 0, 0};
	  float v12[] = {0, 0, 0};

    if (depth==0) {

      gl.glColor3d(v1[0]*v1[0], v1[1]*v1[1], v1[2]*v1[2]);

      for (int i = 0; i<3; i++) {
        v11[i] = v1[i];
        v22[i] = v2[i];
      }
      drawBottom(v11, v22, v00);
      // bottom cover of the cone

      v00[2] = 1; // height of cone, the tip on z axis
      drawConeSide(v11, v22, v00);
      // side cover of the cone, new normal added

      return;
    }

    for (int i = 0; i<3; i++) {
      v12[i] = v1[i]+v2[i];
    }
    normalize(v12);

    subdivideCone(v1, v12, depth-1);
    subdivideCone(v12, v2, depth-1);
  }

  public void drawCone() {
    // replace original cone drawing, add normals 4 lighting

    subdivideCone(cVdata[0], cVdata[1], depth);
    subdivideCone(cVdata[1], cVdata[2], depth);
    subdivideCone(cVdata[2], cVdata[3], depth);
    subdivideCone(cVdata[3], cVdata[0], depth);
  }
  
   public void drawConeSide(float v1[], float v2[],
		  float v3[]) {
    // add normals to cone side
	  float v11[] = new float[3];
	  float v22[] = new float[3];
	  float v33[] = new float[3];

    for (int i = 0; i<3; i++) {
      v11[i] = v1[i]+v3[i]; // normal for cone vertex 1
      v22[i] = v2[i]+v3[i]; // normal for vertex 2
      v33[i] = v11[i]+v22[i]; // normal for vertex 3
    }

    gl.glBegin(GL.GL_TRIANGLES);
    gl.glNormal3fv(v11,0);
    gl.glVertex3fv(v1,0);
    gl.glNormal3fv(v22,0);
    gl.glVertex3fv(v2,0);
    gl.glNormal3fv(v33,0);
    gl.glVertex3fv(v3,0);
    gl.glEnd();
  }
  
  //creating the tetrahedron
	private void drawTetra()
	{
		gl.glBindTexture(GL.GL_TEXTURE_2D, IRIS_TEX[0]);
		gl.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_MODULATE); 
	    gl.glEnable(GL.GL_TEXTURE_2D);
		

			//gl.glColor3f(0, 0, 1f);
			gl.glBegin(GL.GL_TRIANGLES);
			gl.glTexCoord2f(0f, 1f);
		    gl.glNormal3f(0f, 1f, 0f);
		    gl.glVertex3f(0f, 1f, 0f);
		    gl.glTexCoord2f(0f, 0f);
		    gl.glNormal3f(0f, 0f, 0.6f);
		    gl.glVertex3f(0f, 0f, 0.6f);
		    gl.glTexCoord2f(0.6f, 0f);
		    gl.glNormal3f(0.6f, 0f, -0.6f);
		    gl.glVertex3f(0.6f, 0f, -0.6f);
		    gl.glEnd();
	    
		    
		    //gl.glColor3f(15f, 15f, 1f);
		    gl.glBegin(GL.GL_TRIANGLES);///////////////////
		    gl.glTexCoord2f(0f, 1f);
		    gl.glNormal3f(0f, 1f, 0f);
		    gl.glVertex3f(0f, 1f, 0f);
		    gl.glTexCoord2f(-0.6f, 0f);
		    gl.glNormal3f(-0.6f, 0f, -0.6f);
		    gl.glVertex3f(-0.6f, 0f, -0.6f);
		    gl.glTexCoord2f(0f, 0f);
		    gl.glNormal3f(0f, 0f, 0.6f);
		    gl.glVertex3f(0f, 0f, 0.6f);
		    gl.glEnd();
	    
		    //gl.glColor3f(0.5f, 0.5f, 1f);
		    gl.glBegin(GL.GL_TRIANGLES);
		    gl.glTexCoord2f(0f, 1f);
		    gl.glNormal3f(0f, 1f, 0f);
		    gl.glVertex3f(0f, 1f, 0f);
		    gl.glTexCoord2f(0.6f, 0f);
		    gl.glNormal3f(0.6f, 0f, -0.6f);
		    gl.glVertex3f(0.6f, 0f, -0.6f);
		    gl.glTexCoord2f(-0.6f, 0f);
		    gl.glNormal3f(-0.6f, 0f, -0.6f);
		    gl.glVertex3f(-0.6f, 0f, -0.6f);
		    gl.glEnd();
	    
		    //bottom
		    gl.glBegin(GL.GL_TRIANGLES);
		    gl.glTexCoord2f(0.6f, 0f);
		    gl.glNormal3f(0.6f, 0f, -0.6f);
		    gl.glVertex3f(0.6f, 0f, -0.6f);
		    gl.glTexCoord2f(0f, 0f);
		    gl.glNormal3f(0f, 0f, 0.6f);
		    gl.glVertex3f(0f, 0f, 0.6f);
		    gl.glTexCoord2f(-0.6f, 0f);
		    gl.glNormal3f(-0.6f, 0f, -0.6f);
		    gl.glVertex3f(-0.6f, 0f, -0.6f);
		    gl.glEnd();
	    
	    gl.glDisable(GL.GL_TEXTURE_2D); 
	    
	    // for the background texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, STARS_TEX[0]);
	}
	
	public void drawColorCoord(float xlen, float ylen, float zlen) 
	{
    	boolean enabled = false;

	    gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
	    gl.glHint(GL.GL_LINE_SMOOTH, GL.GL_NICEST);
	
	    if (gl.glIsEnabled(GL.GL_BLEND)) 
	    {
	      enabled = true;
	    } 
	    else 
	    {
	      gl.glEnable(GL.GL_BLEND);
	    }
	    gl.glEnable(GL.GL_LINE_SMOOTH);
	 //   super.drawColorCoord(xlen, ylen, zlen);
	    gl.glDisable(GL.GL_LINE_SMOOTH);
	
	    // blending is only enabled for coordinates
	    if (!enabled) 
	    {
	      gl.glDisable(GL.GL_BLEND);
	    }
  }
	  
///////////////////////////////////////////////////////////////////////////////////////
	  public static void main(String[] args) 
	  {
		 HW6_sfaust3 f = new HW6_sfaust3();

	    f.setTitle("JOGL HW6_sfaust3");
	    f.setSize(WIDTH, HEIGHT);
	    f.setVisible(true);
	  }
	}

