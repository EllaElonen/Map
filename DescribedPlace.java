public class DescribedPlace extends Place {
	private String description;

	public DescribedPlace(String name, Position position, String category, PicturePanel picturePanel,
			MainWindow mainWindow, String description) {
		super(name, position, category, picturePanel, mainWindow);
		this.description = description;

	}

	@Override
	public String getDescription() {
		return description;
	}

	public String toString() {
		return "Name: " + name + " {" + position + "}" + "\n" + "Description: " + description;
	}

}