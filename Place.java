import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

class Place extends JComponent {
	protected String name;
	protected Position position;
	private String category;
	private Triangle triangle;

	private boolean marked = false;
	private boolean shown = false;
	private MainWindow mainWindow;
	private PicturePanel picturePanel;
	private TriangleListener triangleListener = new TriangleListener();

	public Place(String name, Position position, String category, PicturePanel picturePanel, MainWindow mainWindow) {
		this.picturePanel = picturePanel;
		this.name = name;
		this.position = position;
		this.category = category;
		this.mainWindow = mainWindow;
	}

	class TriangleListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {

			triangle(mev);

		}
	}

	public void paintPlace() {
		triangle = new Triangle(this);
		triangleListener = new TriangleListener();
		triangle.addMouseListener(triangleListener); //adds a MouseListener too Triangle
		picturePanel.add(triangle); // adds Triangle to PicturePanel
		picturePanel.validate(); 
		picturePanel.repaint(); 
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public Position getPosition() {
		return position;
	}

	public Color getColor() {

		if (category.equals("Bus")) {
			return Color.RED;
		} else if (category.equals("Underground")) {
			return Color.BLUE;
		} else if (category.equals("Train")) {
			return Color.GREEN;
		} else {
			return Color.BLACK;
		}
	}

	public void mark() {
		marked = true;
		triangle.setMark();

	}

	public void unmark() {
		marked = false;

		triangle.setUnMark();
	}

	public boolean isMarked() {
		return marked;
	}

	public void showPlace() {

		shown = true;
		triangle.setVisability(true);
		triangle.revalidate();
		triangle.repaint();

	}

	public void hidePlace() {
		shown = false;
		triangle.setVisability(false);
		triangle.revalidate();
		triangle.repaint();
	}

	public void removeTriangle() {
		picturePanel.remove(triangle);
	}

	public String getDescription() {
		return null;
	}

	private void triangle(MouseEvent mev) {
		if (SwingUtilities.isRightMouseButton(mev)) {
			Triangle b = (Triangle) mev.getSource();
			JOptionPane.showMessageDialog(Place.this, b.getPlace().toString(), "PlaceInfo",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (SwingUtilities.isLeftMouseButton(mev)) {
			Triangle b = (Triangle) mev.getSource();

			if (!b.getPlace().isMarked()) {

				b.getPlace().mark();

				mainWindow.addToMarkedPlace(Place.this);

			} else {

				b.getPlace().unmark();
				mainWindow.removeMarkedPlace(Place.this);

			}
		}
	}
}


