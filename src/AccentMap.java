import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

class AccentMap extends JFrame {

	boolean isStarted = false;
	ImageIcon mapImage = new ImageIcon(this.getClass().getResource("map.jpg"));
	int screenWidth = mapImage.getIconWidth();
	int screenHeight = mapImage.getIconHeight();
	JDesktopPane pane = new JDesktopPane();
	JLabel map = new JLabel(mapImage);
	JPanel correctPanel = new JPanel();
	JPanel buttonPanel = new JPanel();

	String names[] = { "Denmark", "USAGeorgia", "Lithuania", "Bahamas", "AustraliaQueensland" };
	int correct = 0;

	public AccentMap() {
		map.setBounds(0, 0, screenWidth, screenHeight);
		buttonPanel.setOpaque(false);
		buttonPanel.setBounds(0, 0, screenWidth, screenHeight);
		buttonPanel.setLayout(null);

		JButton lithuania = new JButton();
		addButton("Lithuania", lithuania, 700, 238, 3500);
		JButton usageorgia = new JButton();
		addButton("USAGeorgia", usageorgia, 325, 345, 3700);
		JButton denmark = new JButton();
		addButton("Denmark", denmark, 652, 238, 2500);
		JButton bahamas = new JButton();
		addButton("Bahamas", bahamas, 348, 370, 2200);
		JButton australiaqueensland = new JButton();
		addButton("AustraliaQueensland", australiaqueensland, 1160, 550, 3000);
		startButton();

		JButton lithuaniaAnswer = new JButton("Lithuania");
		addGameButton(lithuaniaAnswer, 0, 500);
		JButton usageorgiaAnswer = new JButton("USA: Georgia");
		addGameButton(usageorgiaAnswer, 0, 520);
		JButton bahamasAnswer = new JButton("Bahamas");
		addGameButton(bahamasAnswer, 0, 540);
		JButton australiaqueenslandAnswer = new JButton("Australia: Queensland");
		addGameButton(australiaqueenslandAnswer, 0, 560);
		JButton denmarkAnswer = new JButton("Denmark");
		addGameButton(denmarkAnswer, 0, 580);

		// more buttons
		pane.add(map, new Integer(1));
		pane.add(buttonPanel, new Integer(2));
		pane.add(correctPanel, new Integer(2));

		setLayeredPane(pane);
	}

	public void playSound(String soundName) {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			;
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();

		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}

	public void createParagraph(String accentname, int audioLength) {
		ImageIcon paragraphImage = new ImageIcon(this.getClass().getResource(accentname + ".png"));
		JLabel paragraphPanel = new JLabel(paragraphImage);
		paragraphPanel.setOpaque(true);
		paragraphPanel.setBounds(200, 300, paragraphImage.getIconWidth(), paragraphImage.getIconHeight());
		paragraphPanel.setLayout(null);
		pane.add(paragraphPanel, new Integer(3));

		int delay = audioLength;
		ActionListener removeParagraph = new ActionListener() {
			public void actionPerformed(ActionEvent remove) {
				paragraphPanel.setOpaque(false);
				paragraphPanel.setVisible(false);
				pane.remove(paragraphPanel);
			}
		};
		new Timer(delay, removeParagraph).start();
	}

	public void addButton(String accentname, JButton accentName, int X, int Y, int audioLength) {
		buttonPanel.add(accentName);
		Insets insets = pane.getInsets();
		accentName.setPreferredSize(new Dimension(18, 18));
		Dimension size = accentName.getPreferredSize();
		accentName.setBounds(X + insets.left, Y + insets.top, size.width, size.height);
		accentName.setOpaque(false);
		accentName.setContentAreaFilled(false);
		accentName.setBorderPainted(false);
		accentName.setMultiClickThreshhold(audioLength);

		accentName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playSound(accentname + ".wav");
				createParagraph(accentname, audioLength);
			}
		});

		accentName.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {

				try {
					ImageIcon buttonImage = new ImageIcon(this.getClass().getResource("button.jpg"));
					accentName.setIcon(buttonImage);
				} catch (Exception ex) {
					System.out.println(ex);
				}

				accentName.setOpaque(true);
				accentName.setContentAreaFilled(true);
				accentName.setBorderPainted(true);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {

				try {
					accentName.setIcon(null);
				} catch (Exception ex) {
					System.out.println(ex);
				}
				accentName.setOpaque(false);
				accentName.setContentAreaFilled(false);
				accentName.setBorderPainted(false);
			}
		});
	}
	
	int answerDelay;
	public void startButton() {
		answerDelay = 12000;
		Insets insets = pane.getInsets();
		JButton startButton = new JButton();
		ImageIcon buttonImage = new ImageIcon(this.getClass().getResource("gamebutton.png"));
		startButton.setIcon(buttonImage);
		Dimension size = startButton.getPreferredSize();
		startButton.setPreferredSize(new Dimension(buttonImage.getIconWidth(), buttonImage.getIconHeight()));
		startButton.setBounds(550 + insets.left, insets.top, size.width, size.height);
		startButton.setOpaque(false);
		startButton.setContentAreaFilled(true);
		startButton.setBorderPainted(false);
		startButton.setMultiClickThreshhold(answerDelay);
		buttonPanel.add(startButton);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isStarted = true;
				playGame();
			}
		});
	}

	public void playGame() {
		if (isStarted != false) {
			Random random = new Random();
			String accentname = names[random.nextInt(names.length)];
			playSound(accentname + ".wav");
			createParagraph(accentname, 12000);
		}
	}

	public void checkCorrect(JButton accentName) {
		if (accentName.getModel().isPressed() && isStarted == true) {
			correct++;
			playGame();
			correctPanel.setOpaque(true);
			correctPanel.setLayout(null);
			correctPanel.setBounds(0, 300, 30, 30);
			JLabel correct = new JLabel();
			correctPanel.add(correct);
			correct.setText(String.valueOf(correct));
		}
	}

	public void addGameButton(JButton accentName, int X, int Y) {
		buttonPanel.add(accentName);
		Insets insets = pane.getInsets();
		accentName.setPreferredSize(new Dimension(200, 20));
		Dimension size = accentName.getPreferredSize();
		accentName.setBounds(X + insets.left, Y + insets.top, size.width, size.height);
		accentName.setOpaque(true);
		accentName.setContentAreaFilled(true);
		accentName.setBorderPainted(true);
		accentName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkCorrect(accentName);
			}
		});

	}

	public static void main(String[] args) {
		AccentMap window = new AccentMap();
		window.setSize(1270, 720);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
