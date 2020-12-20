package newt;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * Class handles the OpenGL events to render graphics.
 *
 */
public class JOGL2Renderer implements GLEventListener {
    private double theta = 0.0f;  // rotational angle

    /** 
     * Called back by the drawable to render OpenGL graphics 
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();   // get the OpenGL graphics context

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);    // clear background
        gl.glLoadIdentity();                   // reset the model-view matrix    

          // Rendering code - draw a triangle
        float sine = (float)Math.sin(theta);
        float cosine = (float)Math.cos(theta);
        gl.glBegin(GL.GL_TRIANGLES);
        gl.glColor3f(1, 0, 0);
        gl.glVertex2d(-cosine, -cosine);
        gl.glColor3f(0, 1, 0);
        gl.glVertex2d(0, cosine);
        gl.glColor3f(0, 0, 1);
        gl.glVertex2d(sine, -sine);
        gl.glEnd();

        update();
    }

    /** 
     * Update the rotation angle after each frame refresh 
     */
    private void update() {
        theta += 0.01;
    }

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

    /*... Other methods leave blank ...*/
}