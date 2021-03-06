package todo_manager;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.StyleContext;

import todo_manager.ToDoManager.CommandType;

//@author A0085159W
public class ToDoManagerGUI {
	private static Logic logic;
	private static UserInterface userInterface;
	public static Storage storage;
	private static ToDoManagerGUI toDoManagerGUI;
	static final String HOTKEYS = "Hotkeys:\n" + 
								  "esc: Exit ToDo Manager\n" + 
								  "F1: Display Help Messages\n" + 
								  "F2: Clear Screen\n" + 
								  "F3: Display Hotkeys\n" + 
								  "F4: Undo\n" + 
								  "F5: View All\n" + 
								  "Page_Up: Scroll Up\n" + 
								  "Page_Down: Scroll Down\n" + 
								  "Home: Scroll Up To the Top\n" + 
								  "End: Scroll Down To the Bottom\n";

	private final static String MESSAGE_SPLIT_LINE = "------------------------------------------";
	private final static String MESSAGE_WELCOME = "Welcome to ToDo Manager!";

	private static ToDoManagerGUI guiWindow;
	private JFrame frame;
	private JPanel inputBoxPanel;
	private JScrollPane scrollPane;
	private JTextField inputBox;
	// top display box for ToDo Manager
	private JTextField topTitle;
	// feedback display box below the userinput area
	private JTextField feedbackBox;
	// display box for all tasks
	private static JTextPane displayBox;

	// @author A0085159W
	public static void main(String[] args) {
		toDoManagerGUI = new ToDoManagerGUI();
		toDoManagerGUI.setup();
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				guiWindow = new ToDoManagerGUI();
				guiWindow.frame.setVisible(true);
			}
		});

	}

	// @author A0085159W
	public void setup() {
		logic = Logic.getInstance();
		userInterface = new UserInterface();
		storage = new Storage();
		UserInterface.setup();
		logic.setupGUI(this); // creation and filling out of linked lists
	}

	// @author A0085159W
	public ToDoManagerGUI() {
		initialize();
		start();
	}

	// @author A0085159W
	private void initialize() {
		setupFrame();
		setupTopTitle();
		setupFeedbackBox();
		setupTextDisplayBox();
		setupTextInputBox();
		setupLayout();
	}

	// @author A0085159W
	private void start() {
		displayWelcomeMessage();
		guiFrameListener();
		inputBoxListener();
		displayBoxListener();
	}

	// @author A0085159W
	// set up the application frame
	private void setupFrame() {
		frame = new JFrame();

		frame.setSize(400, 500);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ToDoManager");
		frame.setResizable(false);
		// frame.setUndecorated(true);
	}

	// @author A0085159W
	// set up top title
	private void setupTopTitle() {
		topTitle = new JTextField();
		topTitle.setBackground(Color.BLACK);
		topTitle.setForeground(new Color(0xFFD700));
		topTitle.setBorder(null);
		topTitle.setHorizontalAlignment(SwingConstants.CENTER);
		topTitle.setPreferredSize(new Dimension(400, 50));
		topTitle.setFont(new Font("Aller", Font.BOLD, 20));
		topTitle.setText("ToDo Manager");
	}

	// @author A0085159W
	// add a text input box
	private void setupTextInputBox() {
		inputBox = new HintTextField("/cmd:");

		inputBoxPanel = new JPanel();
		inputBoxPanel.add(inputBox);

		inputBoxPanel.setBackground(new Color(0x474747));
		inputBoxPanel.setBorder(new RoundedCornerBorder());
		inputBoxPanel.setPreferredSize(new Dimension(350, 35));

		inputBox.requestFocus();
		inputBox.setBorder(null);
		inputBox.setBackground(new Color(0x474747));
		inputBox.setForeground(new Color(0xF0E68C));
		inputBox.setFont(new Font("Aller", Font.PLAIN, 13));
		inputBox.setHorizontalAlignment(SwingConstants.LEFT);
		inputBox.setPreferredSize(new Dimension(315, 20));
	}

	// @author A0085159W
	// set up feedback display box
	private void setupFeedbackBox() {
		feedbackBox = new JTextField();
		feedbackBox.setBackground(Color.BLACK);
		feedbackBox.setForeground(new Color(0xF0E68C));
		feedbackBox.setFont(new Font("Aller", Font.PLAIN, 12));
		feedbackBox.setBorder(null);
		feedbackBox.setHorizontalAlignment(SwingConstants.CENTER);
		feedbackBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		feedbackBox.setPreferredSize(new Dimension(315, 30));

		feedbackBox.setText("Command Feedback:");
	}

	// @author A0085159W
	// text display box
	private void setupTextDisplayBox() {
		displayBox = new JTextPane();
		scrollPane = new JScrollPane(displayBox);

		displayBox.setBackground(new Color(0x6A6A6A));
		displayBox.setForeground(new Color(0xF0E68C));
		displayBox.setBorder(null);
		displayBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		displayBox.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		displayBox.setFont(new Font("Aller", Font.PLAIN, 14));
		// displayBox.setMargin(new Insets(20,20,20,20));

		// set up the scrollable pane
		scrollPane.setBorder(null);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setPreferredSize(new Dimension(400, 450));
		scrollPane.setWheelScrollingEnabled(true);
	}

	// @author A0085159W
	// setup frame layout
	private void setupLayout() {
		frame.getContentPane().setLayout(
				new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.getContentPane().add(topTitle, Component.CENTER_ALIGNMENT);
		frame.getContentPane().add(inputBoxPanel);
		frame.getContentPane().add(feedbackBox);
		frame.getContentPane().add(scrollPane, Component.CENTER_ALIGNMENT);
	}

	// @author A0085159W
	// clear display box
	private void clearDisplayBox() {
		displayBox.setText("");
	}

	// @author A0085159W
	// display a string on the display box
	private void displayMessage(String message) {
		StyleContext style = StyleContext.getDefaultStyleContext();

		System.out.println(displayBox);
		int length = displayBox.getDocument().getLength();
		System.out.println("Length:" + length);
		displayBox.setCaretPosition(length);
		displayBox.replaceSelection(message + "\n");
	}

	// @author A0085159W
	// read userinput
	private String readUserInput() {
		String userInput = null;
		if (!inputBox.equals(null)) {
			userInput = inputBox.getText();
		}
		return userInput;
	}

	// @author A0085159W
	// displays the latest messages
	private void appendToDisplayBox(String message) {
		displayBox.setText(message);
	}

	// @author A0085159W
	// GUI frame Listener
	private void guiFrameListener() {
		frame.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				inputBox.requestFocusInWindow();
			}
		});

		frame.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				inputBox.requestFocusInWindow();
			}
		});
	}

	// @author A0085159W
	// Listener for display box
	private void displayBoxListener() {
		displayBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				inputBox.requestFocus();
			}
		});
		displayBox.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateScrollBar();
			}
		});
	}

	// @author A0085159W
	// Pushes scroll bar to display the latest
	private void updateScrollBar() {
		displayBox.setCaretPosition(displayBox.getDocument().getLength());
		DefaultCaret caret = (DefaultCaret) displayBox.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	// @author A0085159W
	// display welcome meesge
	private void displayWelcomeMessage() {
		displayBox.setText(MESSAGE_WELCOME + "\nToday's Date: "
				+ getTodayDate() + "\n" + MESSAGE_SPLIT_LINE + "\n" + HOTKEYS);
	}

	// @author A0085159W
	// get system date
	private String getTodayDate() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar date = Calendar.getInstance();
		return dateFormatter.format(date.getTime());
	}

	// @author A0085159W
	// Listener for input box
	private void inputBoxListener() {
		inputBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateScrollBar();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!inputBox.getText().trim().equals("")) {
						clearDisplayBox();
						feedbackBox.setText("");
						String userCommand = readUserInput();
						Object displayObj;
						displayObj = toDoManagerGUI.logic
								.actOnUserInput(userCommand);

						displayResult(displayObj);
						inputBox.setText("");
					}
				}

				else if (e.getKeyCode() == KeyEvent.VK_F1) {
					displayBox.setText(Logic.HELP_NO_KEYWORD);
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP
						|| e.getKeyCode() == KeyEvent.VK_UP) {
					scrollPane.getVerticalScrollBar().setValue(
							scrollPane.getVerticalScrollBar().getValue() - 25);
				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN
						|| e.getKeyCode() == KeyEvent.VK_DOWN) {
					scrollPane.getVerticalScrollBar().setValue(
							scrollPane.getVerticalScrollBar().getValue() + 25);
				} else if (e.getKeyCode() == KeyEvent.VK_HOME) {
					scrollPane.getVerticalScrollBar().setValue(0);
				} else if (e.getKeyCode() == KeyEvent.VK_END) {
					scrollPane.getVerticalScrollBar().setValue(
							scrollPane.getVerticalScrollBar().getMaximum());
				} else if (e.getKeyCode() == KeyEvent.VK_F2) {
					displayBox.setText("");
				} else if (e.getKeyCode() == KeyEvent.VK_F3) {
					displayBox.setText(HOTKEYS);
				} else if (e.getKeyCode() == KeyEvent.VK_F4) {
					Object action;
					action = toDoManagerGUI.logic.actOnUserInput("/undo");
					displayResult(action);
				} else if (e.getKeyCode() == KeyEvent.VK_F5) {
					Object action;
					action = toDoManagerGUI.logic.actOnUserInput("/view");
					displayResult(action);
				}

			}
		});
	}

	// @author A0085159W
	// display tasks and feedback to displaybox and feedbackbox
	private void displayResult(Object displayObj) {
		if (displayObj == null) {
			feedbackBox.setText(ToDoManager.MESSAGE_ERROR_GENERIC);
			return;
		} else if (displayObj instanceof String) { // print it if string
			String displayString = (String) displayObj;
			if (displayString.split(" ")[0].equals("Help")) {
				displayBox.setText(displayString);
			} else {
				feedbackBox.setText(displayString);
			}

		} else if (displayObj instanceof LinkedList) { // send to entrylist
														// printer
			LinkedList<?> displayList = (LinkedList<?>) displayObj;
			displayLists(displayList);
		} else if (displayObj instanceof Result) {
			CommandType command = ((Result) displayObj).getCommandType();
			switch (command) {
				case CMD_ADD:
					feedbackBox.setText(((Result) displayObj).getFeedback());
					displayLists(((Result) displayObj).getDisplayList());
					break;
				case CMD_CLEAR:
					feedbackBox.setText(((Result) displayObj).getFeedback());
					break;
				case CMD_DELETE:
					feedbackBox.setText(((Result) displayObj).getFeedback());
					displayLists(((Result) displayObj).getDisplayList());
					break;
				case CMD_DISPLAY:
					feedbackBox.setText(((Result) displayObj).getFeedback());
					displayLists(((Result) displayObj).getDisplayList());
					break;
				case CMD_DONE:
					feedbackBox.setText(((Result) displayObj).getFeedback());
					displayLists(((Result) displayObj).getDisplayList());
					break;
				case CMD_UNDONE:
					feedbackBox.setText(((Result) displayObj).getFeedback());
					displayLists(((Result) displayObj).getDisplayList());
					break;
				case CMD_EDIT:
					feedbackBox.setText(((Result) displayObj).getFeedback());
					displayLists(((Result) displayObj).getDisplayList());
					break;
				case CMD_UNDO:
					feedbackBox.setText(((Result) displayObj).getFeedback());
					displayLists(((Result) displayObj).getDisplayList());
					break;
				case CMD_SEARCHFREEDAY:
					feedbackBox.setText("Next Free Day @" + prettyDate(((Result) displayObj).getFeedback()));
					displayLists(((Result) displayObj).getDisplayList());
					break;
				default:
					break;
			}
		}

	}

	// @author A0085159W
	// display the display list on displaybox
	private void displayLists(LinkedList<?> displayList) {
		int count = 1;
		String entryString;
		String displayString = "";
		Entry e = new Entry();
		for (Object o : displayList) {
			// ensure type safety
			if (o instanceof Entry) {
				e = (Entry) o;
			} else {
				feedbackBox.setText(ToDoManager.MESSAGE_ERROR_GENERIC);
			}

			entryString = count + ". " + e.getName();
			
			if (e.getDoneness() == true) {
				entryString += "    (Done)";
			}
			
			if (e.getStartingDate() != null && e.getEndingDate() != null &&
					e.getStartingDate().equals(e.getEndingDate())){// 1-day event
				entryString += "\n" + "     on : " + prettyDate(e.getStartingDate());
				if (e.getEndingTime() != null && e.getStartingTime() != null && 
						e.getEndingTime().equals(e.getStartingTime())){ // single-time event
					entryString += "     at : " + prettyTime(e.getEndingTime());
				} else { // 1-day event, range of time
					boolean first = true;
					
					if (e.getStartingTime() != null && !e.getStartingTime().equals("")) {
						first = false;
						entryString += "\n";
						entryString += "     from " + prettyTime(e.getStartingTime());
					}
					if (e.getEndingTime() != null && !e.getEndingTime().equals("0000")) {
						if (first) {
							entryString += "\n";
						}
						entryString += "     til " + prettyTime(e.getEndingTime());
					}
				}
			} else {
				if (e.getStartingDate() != null && !e.getStartingDate().equals("") ) {
					entryString += "\n" + "     start : " + prettyDate(e.getStartingDate());
					if (e.getStartingTime() != null && !e.getStartingTime().equals("") ) {
						entryString += "     " + prettyTime(e.getStartingTime());
					}
				}
				if (e.getEndingDate() != null && !e.getEndingDate().equals("999999")){
					entryString += "\n" + "     end : " + prettyDate(e.getEndingDate());
					if (e.getEndingTime() != null && !e.getEndingTime().equals("0000")) {
						entryString += "     " + prettyTime(e.getEndingTime());
					}
				}
			}

			count++;
			entryString += "\n";
			displayString += entryString;
		}
		if (displayList.size() == 0) {
			feedbackBox.setText("no entry found!");
		} else {
			displayBox.setText(displayString);
		}
	}
	
	private static String prettyTime(String uglyTime) {
		if (uglyTime == null || uglyTime.equals("0000")){
			return "";
		}
		uglyTime = uglyTime.trim();
		String niceTime = "";
		if (!uglyTime.equals("")) {
			int hour = Integer.parseInt(uglyTime.substring(0, 2));
			if (hour >= 13) {
				hour -= 12;
			}
			niceTime = hour + ":" + uglyTime.substring(2);
		}
		return niceTime;
	}
	
	//@author A0098735M
	private static String prettyDate(String uglyDate) {
		if (uglyDate == null || uglyDate.equals("999999")){
			return "";
		}
		String myFormat;

		int thisYear = Calendar.getInstance().get(Calendar.YEAR) % 100;
		int givenYear = (Integer.parseInt(uglyDate)) % 100;

		if (thisYear == givenYear) {
			myFormat = "dd MMM EEE";
		} else {
			myFormat = "dd MMM EEE yyyy";
		}

		Date uglyDateObj;
		try {
			uglyDateObj = new SimpleDateFormat(Interpreter.DATE_FORMAT).parse(uglyDate);
		} catch (ParseException e) {
			return "ERROR";
		}
		String niceDate = new SimpleDateFormat(myFormat).format(uglyDateObj);
		return niceDate;
	}

	//@author A0085159W
	// setup a prompt message
	class HintTextField extends JTextField implements FocusListener {

		private final String hint;
		private boolean showingHint;

		public HintTextField(final String hint) {
			super(hint);
			this.hint = hint;
			this.showingHint = true;
			super.addFocusListener(this);
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (this.getText().isEmpty()) {
				super.setText("");
				showingHint = false;
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (this.getText().isEmpty()) {
				super.setText(hint);
				showingHint = true;
			}
		}

		@Override
		public String getText() {
			return showingHint ? "" : super.getText();
		}
	}

	/*
	 * // rounded JTextField public class RoundJTextField extends JTextField {
	 * private Shape shape;
	 * 
	 * public RoundJTextField(int size) { super(size); setOpaque(false); // As
	 * suggested by @AVD in comment. }
	 * 
	 * protected void paintComponent(Graphics g) { g.setColor(getBackground());
	 * g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
	 * super.paintComponent(g); }
	 * 
	 * protected void paintBorder(Graphics g) { g.setColor(getForeground());
	 * g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15); }
	 * 
	 * public boolean contains(int x, int y) { if (shape == null ||
	 * !shape.getBounds().equals(getBounds())) { shape = new
	 * RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15); }
	 * return shape.contains(x, y); } }
	 */

	class RoundedCornerBorder extends AbstractBorder {
		@Override
		public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			int r = height - 1;
			RoundRectangle2D round = new RoundRectangle2D.Float(x + 25, y,
					width - 50, height - 1, r, r);
			Container parent = c.getParent();
			if (parent != null) {
				g2.setColor(parent.getBackground());
				Area corner = new Area(new Rectangle2D.Float(x, y, width,
						height));
				corner.subtract(new Area(round));
				g2.fill(corner);
			}
			g2.setColor(Color.GRAY);
			g2.draw(round);
			g2.dispose();
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(4, 8, 4, 8);
		}

		@Override
		public Insets getBorderInsets(Component c, Insets insets) {
			insets.left = insets.right = 8;
			insets.top = insets.bottom = 4;
			return insets;
		}
	}
}
