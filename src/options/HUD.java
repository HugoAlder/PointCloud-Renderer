package options;

import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class HUD extends JPanel {

	public HUD() {
		setLayout(null);
		setFocusable(false);

		JLabel options = new JLabel("Options");
		options.setBounds(20, 20, 100, 100);
		options.setFocusable(false);
		options.setIcon(new ImageIcon("res/gear.png"));

		options.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				System.out.println("CLICK");
			}
		});

		add(options);

		setOpaque(false);
	}

}
