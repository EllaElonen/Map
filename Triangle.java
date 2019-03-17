import java.awt.Color;
import java.awt.*;
import javax.swing.*;

class Triangle extends JComponent {

	private int[] xs = { 0, 50, 25 };
	private int[] ys = { 0, 0, 50 };
	private Place place;
	private boolean visibility = true;

	public Triangle(Place place) {
		this.place = place;

		setBounds(place.getPosition().getX() - 25, place.getPosition().getY() - 50, 50, 50);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (visibility) {

			g.setColor(place.getColor());
			g.fillPolygon(xs, ys, 3);
		}

	}

	public void setVisability(boolean visably) {
		this.visibility = visably;
		setVisible(visably);

	}

	public Place getPlace() {
		return place;
	}

	public void setMark() {

		setBorder(BorderFactory.createLineBorder(Color.red, 4));
	}

	public void setUnMark() {
		this.setBorder(null);
	}

}