package game.engine.audio;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Sound {

	DataLine.Info info;
	AudioFormat af;
	byte[] audio;
	int size;
	
	public Sound(String file) {
		AudioInputStream audioInputStream;
		try {
			System.out.println();
			audioInputStream = AudioSystem.getAudioInputStream(new File("Audio/" + file));

			af = audioInputStream.getFormat();
			size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);
		} catch (Exception e) {
			System.out.println(file + " had an issue" );
			e.printStackTrace();
		}
	}

}
