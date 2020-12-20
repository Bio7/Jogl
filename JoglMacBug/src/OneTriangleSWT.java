/*Example from: https://jogamp.org/wiki/index.php/Using_JOGL_in_AWT_SWT_and_Swing*/
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLDrawableFactory;
import com.jogamp.opengl.GLProfile;

/**
 * A minimal program that draws with JOGL in an SWT Composite.
 *
 * @author Wade Walker
 */
public class OneTriangleSWT {

    private static CTabFolder tabFolder;
	private static CTabItem tabItem1;
	private static CTabItem tabItem2;
	

	public static void main(String [] args) {
        Display display = new Display();
        final Shell shell = new Shell( display );
        shell.setText( "OneTriangle SWT" );
        shell.setLayout( new FillLayout() );
        shell.setSize( 640, 480 );

        final Composite composite = new Composite( shell, SWT.NONE );
        composite.setLayout(new FillLayout()  );
        tabFolder = new CTabFolder(composite, SWT.TOP);
        tabFolder.setBorderVisible(true);
        tabFolder.setLayoutData(new FillLayout() );
        tabItem1=new CTabItem(tabFolder, SWT.NONE, 0);
        tabItem1.setText("GLTab");
        tabItem2=new CTabItem(tabFolder, SWT.NONE, 1);
        tabItem2.setText("Tab");

        GLData gldata = new GLData();
        gldata.doubleBuffer = true;
        // need SWT.NO_BACKGROUND to prevent SWT from clearing the window
        // at the wrong times (we use glClear for this instead)
        final GLCanvas glcanvas = new GLCanvas( tabFolder, SWT.NO_BACKGROUND, gldata );
        tabItem1.setControl(glcanvas);
        glcanvas.setCurrent();
        GLProfile glprofile = GLProfile.getDefault();
        final GLContext glcontext = GLDrawableFactory.getFactory( glprofile ).createExternalGLContext();

        // fix the viewport when the user resizes the window
        glcanvas.addListener( SWT.Resize, new Listener() {
            public void handleEvent(Event event) {
                Rectangle rectangle = glcanvas.getClientArea();
                glcanvas.setCurrent();
                glcontext.makeCurrent();
                OneTriangle.setup( glcontext.getGL().getGL2(), rectangle.width, rectangle.height );
                glcontext.release();        
            }
        });

        // draw the triangle when the OS tells us that any part of the window needs drawing
        glcanvas.addPaintListener( new PaintListener() {
            public void paintControl( PaintEvent paintevent ) {
                Rectangle rectangle = glcanvas.getClientArea();
                glcanvas.setCurrent();
                glcontext.makeCurrent();
                OneTriangle.render(glcontext.getGL().getGL2(), rectangle.width, rectangle.height);
                glcanvas.swapBuffers();
                glcontext.release();        
            }
        });

        shell.open();

        while( !shell.isDisposed() ) {
            if( !display.readAndDispatch() )
                display.sleep();
        }

        glcanvas.dispose();
        display.dispose();
    }
}