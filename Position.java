
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;

public class Position {
	private int x;
	private int y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		return x + "," + y;

	}

	@Override
	public int hashCode() {
		return 3 * x + y;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Position) {
			Position position = (Position) other; // Cast from Object to Position
			return (position.x == this.x && position.y == this.y);
		}

		return false;

	}
}