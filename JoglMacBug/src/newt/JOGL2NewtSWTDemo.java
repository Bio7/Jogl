package newt;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.newt.swt.NewtCanvasSWT;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * A program that draws with JOGL in a NEWT GLWindow.
 * 
 */
public class JOGL2NewtSWTDemo {

	private static final int FPS = 60; // animator's target frames per second
	private static CTabFolder tabFolder;
	private static CTabItem tabItem1;
	private static CTabItem tabItem2;
	private static NewtCanvasSWT canvas;

	static {
		GLProfile.initSingleton(); // The method allows JOGL to prepare some Linux-specific locking optimizations
	}

	/**
	 * The entry main() method.
	 */
	public static void main(String[] args) {

		Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setText("OneTriangle SWT");
		shell.setLayout(new FillLayout());
		shell.setSize(640, 480);

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());
		tabFolder = new CTabFolder(composite, SWT.TOP);
		tabFolder.setBorderVisible(true);
		tabFolder.setLayoutData(new FillLayout());
		tabItem1 = new CTabItem(tabFolder, SWT.NONE, 0);
		tabItem1.setText("GLTab");
		tabItem2 = new CTabItem(tabFolder, SWT.NONE, 1);
		tabItem2.setText("Tab");

		// Get the default OpenGL profile, reflecting the best for your running platform
		GLProfile glp = GLProfile.getDefault();
		// Specifies a set of OpenGL capabilities, based on your profile.
		GLCapabilities caps = new GLCapabilities(glp);
		// Create the OpenGL rendering canvas
		GLWindow window = GLWindow.create(caps);

		// Create a animator that drives canvas' display() at the specified FPS.
		final FPSAnimator animator = new FPSAnimator(window, FPS, true);

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowDestroyNotify(WindowEvent arg0) {
				// Use a dedicate thread to run the stop() to ensure that the
				// animator stops before program exits.
				new Thread() {
					@Override
					public void run() {
						if (animator.isStarted())
							animator.stop(); // stop the animator loop
						System.exit(0);
					}
				}.start();
			}
		});

		window.addGLEventListener(new JOGL2Renderer());
		// window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		// window.setTitle(TITLE);
		// window.setVisible(true); 
		
		canvas = new NewtCanvasSWT(tabFolder, SWT.NO_BACKGROUND, window);
		// canvas.setSize(tabFolder.getClientArea().width,
		// tabFolder.getClientArea().height);
		tabItem1.setControl(canvas);
		canvas.setFocus();
		animator.start(); // start the animator loop
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		canvas.dispose();
		display.dispose();

	}
}