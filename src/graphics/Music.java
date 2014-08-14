package graphics;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
	
	private Clip data;
	
	public Music() {
		try {
			AudioInputStream audioIn = AudioSystem
					.getAudioInputStream(new File("music/theme.wav"));
			data = AudioSystem.getClip();
			data.open(audioIn);
			data.loop(Clip.LOOP_CONTINUOUSLY);
		} catch(LineUnavailableException e) {
			e.printStackTrace();
		} catch(UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pause() {
		data.stop();
	}
	
	public void unpause() {
		data.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void restart() {
		data.setFramePosition(0);
	}
}
