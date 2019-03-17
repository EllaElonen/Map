import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class PicturePanel extends JPanel {
	private ImageIcon picture;

	public PicturePanel(String filnamn) {
		picture = new ImageIcon(filnamn);
		int w = picture.getIconWidth();
		int h = picture.getIconHeight();
		setPreferredSize(new Dimension(w, h));
		setMaximumSize(new Dimension(w, h));
		setMinimumSize(new Dimension(w, h));
		setLayout(null);

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(picture.getImage(), 0, 0, this);
	}
}