package game.engine.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;

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
			InputStream url = SoundClip.class.getClassLoader().getResourceAsStream("Audio/" + file);
			InputStream buff = new BufferedInputStream(url);
			audioInputStream = AudioSystem.getAudioInputStream(buff);

			af = audioInputStream.getFormat();
			size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
			audio = new byte[size];
			info = new DataLine.Info(Clip.class, af, size);
			audioInputStream.read(audio, 0, size);
		//	buff.close();
		//	url.close();
			
		} catch (Exception e) {
			System.out.println(file + " had an issue" );
			e.printStackTrace();
		}
	}

}
