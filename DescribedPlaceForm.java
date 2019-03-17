import javax.swing.*;

public class DescribedPlaceForm extends JPanel {
	private JTextField name = new JTextField(8);
	private JTextField description = new JTextField(14);

	public DescribedPlaceForm() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel rad1 = new JPanel();
		add(rad1);
		rad1.add(new JLabel("Namn:"));
		rad1.add(name);

		JPanel rad2 = new JPanel();
		add(rad2);
		rad2.add(new JLabel("Description"));
		rad2.add(description);

	}

	public String getName() {
		return name.getText();
	}

	public String getDescription() {
		return description.getText();
	}
}