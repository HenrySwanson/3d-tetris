package graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import engine.CheatCodes;
import engine.pieces.*;

@SuppressWarnings("serial")
public class OptionPanel extends JPanel { //TODO mute button
	
	public static String helpText = "<html> 3D Tetris <br />" +
			"Version: 1.0 <br /><br />" +
			"Goal: Do not let the pieces reach the top! <br /><br />" +
			"Controls: <ul>" +
			"<li> Movement: a - Left, d - Right, w - Away, s - Toward </li>" +
			"<li> Rotation: Shift+a - Left, Shift+d - Right, Shift+w - Up, Shift+d - Down, Shift+q - Counterclockwise, Shift+e - Clockwise </li>" +
			"<li> Viewpoint: Arrow Keys </li>" +
			"<li> Other: Space - Soft Drop, Enter - Hard Drop, p - Pause </li>" +
			"</ul>" +
			"Made by: Henry Swanson";
	
	private ViewingCanvas viewingCanvas;
	private JLabel nextPiece = new JLabel("Next Piece");
	private JLabel scoreText = new JLabel("Score");
	private JLabel scoreNumber = new JLabel("000000");
	private JButton cheatCode = new JButton("Cheat Code");
	private JButton pause = new JButton("Pause");
	private JButton newGame = new JButton("New Game");
	private JButton help = new JButton("Help / About");

	public OptionPanel(ViewingCanvas v) {
		//for spacing
		setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
		
		viewingCanvas = v;
		//otherwise the viewingcanvas can lose focus
		cheatCode.setFocusable(false);
		pause.setFocusable(false);
		newGame.setFocusable(false);
		help.setFocusable(false);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignments();

		nextPiece.setIcon(new ImageIcon("images/Blank.png"));
		scoreNumber.setBorder(BorderFactory.createLoweredBevelBorder());

		ButtonListener buttonListener = new ButtonListener();
		cheatCode.addActionListener(buttonListener);
		pause.addActionListener(buttonListener);
		newGame.addActionListener(buttonListener);
		help.addActionListener(buttonListener);

		addComponents();
		update();
	}

	private void setAlignments() {
		nextPiece.setAlignmentX(CENTER_ALIGNMENT);
		scoreText.setAlignmentX(CENTER_ALIGNMENT);
		scoreNumber.setAlignmentX(CENTER_ALIGNMENT);
		cheatCode.setAlignmentX(CENTER_ALIGNMENT);
		pause.setAlignmentX(CENTER_ALIGNMENT);
		newGame.setAlignmentX(CENTER_ALIGNMENT);
		help.setAlignmentX(CENTER_ALIGNMENT);

		nextPiece.setHorizontalAlignment(SwingConstants.CENTER);
		nextPiece.setHorizontalTextPosition(SwingConstants.CENTER);
		nextPiece.setVerticalTextPosition(SwingConstants.BOTTOM);
		scoreText.setHorizontalAlignment(SwingConstants.CENTER);
		scoreNumber.setHorizontalAlignment(SwingConstants.RIGHT);
	}

	private void addComponents() {
		add(nextPiece);
		add(Box.createVerticalStrut(10));
		add(scoreText);
		add(scoreNumber);
		add(Box.createVerticalGlue());
		add(help);
		add(Box.createVerticalStrut(5));
		add(cheatCode);
		add(Box.createVerticalStrut(5));
		add(pause);
		add(Box.createVerticalStrut(5));
		add(newGame);
	}
	
	//updates next piece and score
	public void update() {
		Piece p = viewingCanvas.getNextPiece();
		if(p instanceof Corner) {
			nextPiece.setIcon(new ImageIcon("images/Corner.png"));
		} else if(p instanceof Hook_L) {
			nextPiece.setIcon(new ImageIcon("images/Hook_L.png"));
		} else if(p instanceof Hook_R) {
			nextPiece.setIcon(new ImageIcon("images/Hook_R.png"));
		} else if(p instanceof L) {
			nextPiece.setIcon(new ImageIcon("images/L.png"));
		} else if(p instanceof Line) {
			nextPiece.setIcon(new ImageIcon("images/Line.png"));
		} else if(p instanceof Square) {
			nextPiece.setIcon(new ImageIcon("images/Square.png"));
		} else if(p instanceof Squiggly) {
			nextPiece.setIcon(new ImageIcon("images/Squiggly.png"));
		} else if(p instanceof T) {
			nextPiece.setIcon(new ImageIcon("images/T.png"));
		}

		long score = viewingCanvas.getScore();
		scoreNumber.setText(String.format("%06d", score));
		if(CheatCodes.OVER9000) scoreNumber.setText("009001");
	}

	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == pause)
				viewingCanvas.togglePause();
			if(e.getSource() == help) {
				viewingCanvas.pause();
				JOptionPane.showMessageDialog(null, helpText, "Help", JOptionPane.INFORMATION_MESSAGE);
				viewingCanvas.unpause();
			}
			if(e.getSource() == newGame) {
				viewingCanvas.newGame();
				viewingCanvas.unpause();
				viewingCanvas.update();
				OptionPanel.this.update();
			}
			if(e.getSource() == cheatCode) {
				viewingCanvas.pause();
				String code = JOptionPane.showInputDialog(null, "Enter code",
						"Cheat Code", JOptionPane.QUESTION_MESSAGE);
				viewingCanvas.unpause();
				if(code == null) return;
				if(code.equals("QEUPDOWN"))
					CheatCodes.QEUPDOWN = !CheatCodes.QEUPDOWN;
				if(code.equals("OVER9000"))
					CheatCodes.OVER9000 = !CheatCodes.OVER9000;
				if(code.equals("LOCKENTER"))
					CheatCodes.LOCKENTER = !CheatCodes.LOCKENTER;
				if(code.equals("GRAYVIEW"))
					CheatCodes.GRAYVIEW = !CheatCodes.GRAYVIEW;
				update();
			}
		}

	}

}
