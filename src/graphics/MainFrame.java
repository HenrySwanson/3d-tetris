package graphics;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	public static final int DROP_DELAY = 1000;

	private ViewingCanvas viewingCanvas = new ViewingCanvas();
	private OptionPanel optionPanel = new OptionPanel(viewingCanvas);
	private Timer timer;

	public static void main(String[] args) {
		new MainFrame();
	}
	
	public MainFrame() {
		setTitle("3D Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(viewingCanvas);
		cp.add(optionPanel, BorderLayout.EAST);
		
		addKeyListener(new TurnListener());
		timer = new Timer(DROP_DELAY, new TimerListener());
		timer.start();
		
		viewingCanvas.setOptionPanel(optionPanel);
		
		pack();
		setVisible(true);
		requestFocusInWindow();
	}

	private class TurnListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_P) {
				viewingCanvas.togglePause();
			}
			viewingCanvas.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

	}

	private class TimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			viewingCanvas.softDrop();
		}

	}
}
