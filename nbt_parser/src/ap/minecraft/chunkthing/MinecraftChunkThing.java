package ap.minecraft.chunkthing;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import ap.minecraft.chunkthing.file.FileHandler;

public class MinecraftChunkThing implements Listener {
	private ToolBar toolBar;
	private Display display;
	private Shell shell;
	private FileHandler levelFile;
	
	private static Color backgroundColor;
	

	public static void main(String[] args) {
		MinecraftChunkThing app = new MinecraftChunkThing();
		app.run();
	}

	private void run() {
		display = new Display();
		shell = new Shell(display);
		
		backgroundColor = new Color(display, 0xAA, 0xCC, 0xAA);

		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);

		Composite leftSide = new Composite(shell,  SWT.NONE);
		GridData leftSideLayout = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		leftSideLayout.widthHint = 200;// TODO: Magic number
		
		
		Composite rightSide = new Composite(shell,  SWT.NONE);
		rightSide.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		rightSide.setLayout(new FillLayout());
		
		
		toolBar = createToolbar(leftSide);
		final FileDialog dialog = getFileDialog(shell);

		// Add a label for the file name to go into
		final Text text = new Text(rightSide, SWT.LEFT | SWT.TOP);
		text.setText("Level File: ");
		text.setEditable(false);
		text.pack();

				
				
		// Create items for ToolBar
		// Open Level
		createToolBarItem(toolBar, "Open Level", new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// System.out.println("Save to: " + dialog.open());
				String fileName = dialog.open();
				text.setText("Level File: " + fileName);
				text.update();
				levelFile = new FileHandler(fileName, true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				this.widgetSelected(e);
			}
		});

		// Open Region
		createToolBarItem(toolBar, "Open Region", null);

		toolBar.pack();


		
		// System.out.println ("Save to: " + dialog.open ());

		shell.addListener(SWT.Resize, this);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private ToolBar createToolbar(Composite parent) {
		ToolBar toolBar = new ToolBar(parent, SWT.VERTICAL | SWT.BORDER);

		// Set up location and size of toolbar
		Rectangle clientArea = parent.getClientArea();
		toolBar.setLocation(clientArea.x, clientArea.y);
		toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true));

		return toolBar;
	}

	private ToolItem createToolBarItem(ToolBar toolBar, String text, SelectionListener selectionListener) {
		ToolItem item;
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setText(text);
		if (selectionListener != null) {
			item.addSelectionListener(selectionListener);
		}

		return item;
	}

	private FileDialog getFileDialog(Shell shell) {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		String[] filterNames = new String[] { ".dat Files", "All Files (*)" };
		String[] filterExtensions = new String[] { "*.dat", "*" };
		String filterPath = "/";
		String platform = SWT.getPlatform();
		if (platform.equals("win32") || platform.equals("wpf")) {
			filterNames = new String[] { ".dat Files", "All Files (*.*)" };
			filterExtensions = new String[] { "*.dat", "*.*" };
			filterPath = "c:\\";
		}
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(filterPath);
		dialog.setFileName("");

		return dialog;
	}

	private void resize() {
		// TODO: Resize something here
		
	}

	@Override
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.Resize:
			this.resize();
			break;
		default:
			break;
		}
	}
}