
import javax.swing.*;

public class CoordinateForm extends JPanel {
	private JTextField x = new JTextField(4);
	private JTextField y = new JTextField(4);

	public CoordinateForm() {

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel rad1 = new JPanel();
		add(rad1);
		rad1.add(new JLabel(" x:"));
		rad1.add(x);

		JPanel rad2 = new JPanel();
		add(rad2);
		rad2.add(new JLabel(" y:"));
		rad2.add(y);
	}

	public int coordX() {
		return Integer.parseInt(x.getText());

	}

	public int coordY() {

		return Integer.parseInt(y.getText());
	}
}
