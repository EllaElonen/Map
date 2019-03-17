
public class NamedPlace extends Place {

	public NamedPlace(String name, Position position, String category, PicturePanel picturePanel,
			MainWindow mainWindov) {
		super(name, position, category, picturePanel, mainWindov);

	}

	public String toString() {
		return name + "{" + position + "}";
	}

}