 import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.util.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

public class MainWindow extends JFrame {
	private JFileChooser jfc = new JFileChooser();
	private PicturePanel picturePanel = null;
	private JScrollPane scroll = null;
	private JRadioButton named;
	private JRadioButton described;
	private NewPlaceListener newPlaceListener = new NewPlaceListener();
	private JButton newBtn;
	private JList<String> categoryList = null;
	private JTextField search = null;
	private Place place;
	private boolean modified;
	private JFileChooser fileChooser;
	private final String[] categoryArray = { "Bus", "Underground", "Train" };
	private Map<Position, Place> places = new HashMap<Position, Place>();
	private ArrayList<Place> markedPlaces = new ArrayList<Place>();
	private Map<String, ArrayList<Place>> placeByCategory = new HashMap<String, ArrayList<Place>>();
	private Map<String, ArrayList<Place>> positionByName = new HashMap<>();

	MainWindow() {

		JMenuBar mb = new JMenuBar();
		JMenu archive = new JMenu("Archive");
		JMenuItem newMap = new JMenuItem("New Map");
		newMap.addActionListener(new FileListener());
		JMenuItem place = new JMenuItem("Load Places");
		place.addActionListener(new LoadListen());
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new SaveLyss());
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ExitBtnListener());
		setJMenuBar(mb);
		mb.add(archive);
		archive.add(newMap);
		archive.add(place);
		archive.add(save);
		archive.add(exit);

		JPanel borderL = new JPanel();
		add(borderL, BorderLayout.NORTH);

		newBtn = new JButton("New");
		borderL.add(newBtn);
		newBtn.addActionListener(new NewButtonAction());

		JPanel borderLG = new JPanel(new GridLayout(2, 3));
		borderL.add(borderLG);
		ButtonGroup group = new ButtonGroup();
		named = new JRadioButton("Named");
		borderLG.add(named);
		group.add(named);

		described = new JRadioButton("Described");
		borderLG.add(described);
		group.add(described);

		search = new JTextField("Search", 10);
		borderL.add(search);

		JButton searchBtn = new JButton("Search");
		borderL.add(searchBtn);
		searchBtn.addActionListener(new SearchBtnListener());

		JButton hideBtn = new JButton("Hide");
		borderL.add(hideBtn);
		hideBtn.addActionListener(new HideBtnListener());

		JButton removeBtn = new JButton("Remove");
		borderL.add(removeBtn);
		removeBtn.addActionListener(new RemoveBtnListener());

		JButton coordinatesBtn = new JButton("Coordinates");
		borderL.add(coordinatesBtn);
		coordinatesBtn.addActionListener(new CoordBtnListener());

		JPanel contents = new JPanel();
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		add(contents, BorderLayout.EAST);
		JLabel category = new JLabel("Categories");
		contents.add(category);

		categoryList = new JList<String>(categoryArray);
		contents.add(new JScrollPane(categoryList));
		categoryList.addMouseListener(new CategoryListener());

		JButton hideCategoryBtn = new JButton("Hide category");
		contents.add(hideCategoryBtn);
		hideCategoryBtn.addActionListener(new HideCategoryBtnListener());

		setTitle("Inlupp 2");
		pack();
		setLocationRelativeTo(null);

		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				saveBeforeExit();
				return;
			}
		});

		if (this != null)
			setVisible(true);

	}

	public class HideBtnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Iterator<Place> it = markedPlaces.iterator();
			Place place = null;
			while (it.hasNext()) {

				place = it.next();
				place.hidePlace();
				place.unmark();
				it.remove();

			}
			picturePanel.validate();
			picturePanel.repaint();
			modified = true;
		}
	}

	public class HideCategoryBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			Position position = null;
			String selected = categoryList.getSelectedValue();
			Iterator<Place> it = placeByCategory.get(selected).iterator();
			ArrayList<Place> toRemove = new ArrayList<Place>();
			while (it.hasNext()) {
				Place currentPlace = (Place) it.next();

				currentPlace.unmark();
				currentPlace.hidePlace();

				picturePanel.validate();
				picturePanel.repaint();
				modified = true;

			}

		}
	}

	public class CategoryListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {

			String selected = categoryList.getSelectedValue();

			ArrayList<Place> tempPlace = placeByCategory.get(selected);
			if (tempPlace != null) {
				for (Place p : tempPlace) {
					categoryList.add(p);

					p.unmark();
					p.showPlace();

				}
				picturePanel.validate();
				picturePanel.repaint();
				modified = true;

			}

		}
	}

	public class RemoveBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			for (Place place : markedPlaces) {

				places.remove(place.getPosition());
				positionByName.get(place.getName()).remove(place);
				placeByCategory.get(place.getCategory()).remove(place);
				place.removeTriangle();

			}
			markedPlaces.clear();
			picturePanel.validate();
			picturePanel.repaint();

		}
	}

	class NewPlaceListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent mev) {

			int x = mev.getX();
			int y = mev.getY();
			Position pos = new Position(x, y);
			Place place = null;
			String category = categoryList.getSelectedValue();
			if (category == null) {
				category = "None";
			}

			if (described.isSelected()) {
				DescribedPlaceForm form = new DescribedPlaceForm();
				int inputFromUser = JOptionPane.showConfirmDialog(MainWindow.this, form, "PlaceInfo",
						JOptionPane.OK_CANCEL_OPTION);

				if (inputFromUser != JOptionPane.OK_OPTION)
					return;
				String name = form.getName();
				String description = form.getDescription();
				if (name.equals("") || description.equals("")) {
					JOptionPane.showMessageDialog(MainWindow.this, "You haven't provided a name or description", "Fel",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				place = new DescribedPlace(name, pos, category, picturePanel, MainWindow.this, description);

			} else {
				String name = JOptionPane.showInputDialog("Name");
				place = new NamedPlace(name, pos, category, picturePanel, MainWindow.this);
				if (name.equals("")) {
					JOptionPane.showMessageDialog(MainWindow.this, "You haven't provided a name", "Fel",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

			}

			doStuff(place);
		}
	}

	class FileListener implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			if (picturePanel != null) {
				if (modified == true) {
					JLabel warning = new JLabel(
							"Changes have been made. Do you want to save these changes before proceeding?");
					int input = JOptionPane.showConfirmDialog(MainWindow.this, warning, "Warning",
							JOptionPane.YES_NO_CANCEL_OPTION);

					if (input == JOptionPane.YES_OPTION) {
						save();
					} else if ((input == JOptionPane.CANCEL_OPTION) || (input == JOptionPane.CLOSED_OPTION)) {
						return;
					}
				}
			}
			int svar = jfc.showOpenDialog(MainWindow.this);
			if (svar != JFileChooser.APPROVE_OPTION)
				return;
			File fil = jfc.getSelectedFile();
			String path = fil.getAbsolutePath();
			if (picturePanel != null)
				remove(scroll);
			picturePanel = new PicturePanel(path);
			scroll = new JScrollPane(picturePanel);
			add(scroll, BorderLayout.CENTER);
			pack();
			validate();
			repaint();
			modified = true;

		}
	}

	class NewButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ave) {

			picturePanel.addMouseListener(newPlaceListener);
			newBtn.setEnabled(false);
			picturePanel.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}

	class CoordBtnListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ave) {
			try {
				CoordinateForm cordForm = new CoordinateForm();
				int inputFromUser = JOptionPane.showConfirmDialog(MainWindow.this, cordForm, "Input Coordinates:",
						JOptionPane.OK_CANCEL_OPTION);

				if (inputFromUser != JOptionPane.OK_OPTION)
					return;

				int x = cordForm.coordX();
				int y = cordForm.coordY();

				Position position = new Position(x, y);

				if (places.containsKey(position)) {
					markedPlaces.clear();
					places.get(position).mark();
				} else if (!(places.containsKey(position))) {
					JOptionPane.showMessageDialog(MainWindow.this, "No place exists here", " ",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(MainWindow.this, "Wrong input!");
			}

		}
	}

	class SearchBtnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Iterator<Place> it = markedPlaces.iterator();
			Place place = null;
			while (it.hasNext()) {

				place = it.next();
				place.unmark();
				it.remove();
				picturePanel.validate();
				picturePanel.repaint();
			}

			String namn = search.getText();
			ArrayList<Place> sammaNamn = positionByName.get(namn);
			if (sammaNamn != null && !sammaNamn.isEmpty()) {
				for (Place p : sammaNamn) {

					p.mark();
					markedPlaces.add(p);
					p.showPlace();
					picturePanel.validate();
					picturePanel.repaint();
				}
			} else {
				JOptionPane.showMessageDialog(MainWindow.this, "There are no places with specified names", "Warning",
						JOptionPane.ERROR_MESSAGE);
				return;

			}
		}
	}

	private class ExitBtnListener implements ActionListener {
		private void doIt() {
			if (modified == true) {
				int svar = JOptionPane.showConfirmDialog(MainWindow.this,
						"File is not saved,do you really whant to exit ?  ", "Warning", JOptionPane.OK_CANCEL_OPTION);
				if (svar == JOptionPane.OK_OPTION)
					System.exit(0);
			} else {
				return;

			}
		}

		public void windowClosing(WindowEvent wev) {
			doIt();
		}

		public void actionPerformed(ActionEvent ave) {
			doIt();
		}
	}

	class SaveLyss implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			save();
		}
	}

	public void saveBeforeExit() {
		if (modified == true) {
			JLabel changeMsg = new JLabel("Changes have been made. Do you want to save these changes before exiting?");
			int input = JOptionPane.showConfirmDialog(MainWindow.this, changeMsg, "Warning",
					JOptionPane.YES_NO_CANCEL_OPTION);

			if (input == JOptionPane.YES_OPTION) {
				save();
				System.exit(0);
			} else if (input == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (input == JOptionPane.NO_OPTION) {
				System.exit(0);
			}
		} else if (modified == false) {
			System.exit(0);
		}
	}

	private void save() {
		int answer = jfc.showSaveDialog(MainWindow.this);

		if (answer != JFileChooser.APPROVE_OPTION) {
			return;
		} else {

			File fToSave = jfc.getSelectedFile();

			try {

				FileWriter utfil = new FileWriter(fToSave.getAbsolutePath() + ".places.txt");
				PrintWriter out = new PrintWriter(utfil);

				for (Place p : places.values())

					if (p instanceof DescribedPlace) {
						out.println("Described," + p.getCategory() + "," + p.getPosition() + "," + p.getName() + ","
								+ p.getDescription());
					} else {
						out.println("Named," + p.getCategory() + "," + p.getPosition() + "," + p.getName());
					}

				out.close();
				utfil.close();
				modified = false;
				JOptionPane.showMessageDialog(MainWindow.this, "File has been saved!");
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(MainWindow.this, "File can't be opened");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(MainWindow.this, "Error");
			}
		}
	}

	class LoadListen implements ActionListener {
		public void actionPerformed(ActionEvent ave) {
			if (picturePanel == null) {
				JOptionPane.showMessageDialog(MainWindow.this,
						"You need to create a new map to create existing places.", "ErrorWindow",
						JOptionPane.ERROR_MESSAGE);
			} else {
				if (modified == true) {
					JLabel warning = new JLabel(
							"You have made changes. Do you want to save these changes before proceeding?");
					int input = JOptionPane.showConfirmDialog(MainWindow.this, warning, "Warning",
							JOptionPane.YES_NO_OPTION);
					if (input == JOptionPane.YES_OPTION) {
						save();
					} else if ((input == JOptionPane.CLOSED_OPTION)) {
						return;
					} else if (input == JOptionPane.NO_OPTION) {
					}

				}

				try {
					JFileChooser saveChooser = new JFileChooser();
					int input = saveChooser.showOpenDialog(MainWindow.this);

					if (input != JFileChooser.APPROVE_OPTION) {
						return;
					} else {
						File loadFile = saveChooser.getSelectedFile();
						String str = loadFile.getAbsolutePath();

						/*
						 * for (Place place : places.values()) {
						 * place.showPlace(); place.unmark(); }
						 */
						for (Place place : places.values()) {

							ArrayList<Place> toRemove = new ArrayList<Place>();
							toRemove.add(place);
							toRemove.remove(place);
							positionByName.get(place.getName()).remove(place);
							placeByCategory.get(place.getCategory()).remove(place);
							place.removeTriangle();
						}

						placeByCategory.clear();
						places.clear();
						positionByName.clear();
						markedPlaces.clear();
						picturePanel.validate();
						picturePanel.repaint();

						FileReader inpFile = new FileReader(loadFile);
						BufferedReader in = new BufferedReader(inpFile);
						String line;
						while ((line = in.readLine()) != null) {
							String[] tokens = line.split(",");

							String placeType = tokens[0];
							if (placeType.equals("Named")) {
								String category = tokens[1];
								int x = Integer.parseInt(tokens[2]);
								int y = Integer.parseInt(tokens[3]);
								String name = tokens[4];
								Position pos = new Position(x, y);
								Place place = new NamedPlace(name, pos, category, picturePanel, MainWindow.this);
								doStuff(place);

							} else if (placeType.equals("Described")) {
								String category = tokens[1];
								int x = Integer.parseInt(tokens[2]);
								int y = Integer.parseInt(tokens[3]);
								String name = tokens[4];
								String description = tokens[5];
								Position pos = new Position(x, y);
								Place place = new DescribedPlace(name, pos, category, picturePanel, MainWindow.this,
										description);
								doStuff(place);

							}
						}

						in.close();
						inpFile.close();
						picturePanel.repaint();
						picturePanel.validate();

					}
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(MainWindow.this, "File can't be opened!");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(MainWindow.this, "Error " + e.getMessage());
				}
			}
		}
	}

	public void addToMarkedPlace(Place place) {
		markedPlaces.add(place);
	}

	public void removeMarkedPlace(Place place) {
		markedPlaces.remove(place);
	}

	private void doStuff(Place place) {
		if (places.containsKey(place.getPosition())) {
			JOptionPane.showMessageDialog(MainWindow.this, "Error! Place exists already ");
			return;
		}

		places.put(place.getPosition(), place);

		if (!positionByName.containsKey(place.getName())) {
			positionByName.put(place.getName(), new ArrayList<Place>());
		}
		positionByName.get(place.getName()).add(place);

		ArrayList<Place> categoryList = placeByCategory.get(place.getCategory());
		if (categoryList == null) {
			categoryList = new ArrayList<Place>();
			placeByCategory.put(place.getCategory(), categoryList);

		}

		categoryList.add(place);
		picturePanel.removeMouseListener(newPlaceListener);
		place.paintPlace();
		newBtn.setEnabled(true);
		picturePanel.setCursor(Cursor.getDefaultCursor());
		setVisible(true);
		modified = true;
	}

	public static void main(String[] args) {
		new MainWindow();
	}
}