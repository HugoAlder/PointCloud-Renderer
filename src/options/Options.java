package options;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JColorChooser;
import javax.swing.JFrame;

import gl.Viewer;

@SuppressWarnings("serial")
public class Options extends JFrame {

	private JColorChooser colorChooser;

	public Options() {
		super();
		setTitle("Options");
		colorChooser = new JColorChooser();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(600, 400);
		setLocationRelativeTo(null);
		getContentPane().add(colorChooser);
		setVisible(true);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				Color c = colorChooser.getColor();
				Viewer.forceColor(c);

			}
		});
	}
}