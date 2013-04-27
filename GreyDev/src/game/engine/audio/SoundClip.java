package game.engine.audio;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundClip implements LineListener {

	private String name, filename;
	private Clip clip = null;
	private boolean isLooping = false;
	
	public SoundClip(String fnm) {
		filename = "Audio/" + fnm;

		loadClip(filename);
	} // end of SoundClip()

	private void loadClip(String fnm) {
		try {
			// link an audio stream to the sound clip's file
			AudioInputStream stream = AudioSystem.getAudioInputStream(new File(
					fnm));

			AudioFormat format = stream.getFormat();
			// convert ULAW/ALAW formats to PCM format
			if ((format.getEncoding() == AudioFormat.Encoding.ULAW)
					|| (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
				AudioFormat newFormat = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED,
						format.getSampleRate(),
						format.getSampleSizeInBits() * 2, format.getChannels(),
						format.getFrameSize() * 2, format.getFrameRate(), true); // big

				// update stream and format details
				stream = AudioSystem.getAudioInputStream(newFormat, stream);
				System.out.println("Converted Audio format: " + newFormat);
				format = newFormat;
			}

			DataLine.Info info = new DataLine.Info(Clip.class, format);
			// make sure sound system supports data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Unsupported Clip File: " + fnm);
				return;
			}

			// get clip line resource
			clip = (Clip) AudioSystem.getLine(info);
			// listen to clip for events
			clip.addLineListener(this);

			clip.open(stream); // open the sound file as a clip
			stream.close(); // we're done with the input stream

		} // end of try block

		catch (UnsupportedAudioFileException audioException) {
			System.out.println("Unsupported audio file: " + fnm);
		} catch (LineUnavailableException noLineException) {
			System.out.println("No audio line available for : " + fnm);
		} catch (IOException ioException) {
			System.out.println("Could not read: " + fnm);
		} catch (Exception e) {
			System.out.println("Problem with " + fnm);
		}
	} // end of loadClip()

	public void update(LineEvent lineEvent)
	/*
	 * Called when the clip's line detects open, close, start, or stop events.
	 * The watcher (if one exists) is notified.
	 */
	{
		// when clip is stopped / reaches its end
		if (lineEvent.getType() == LineEvent.Type.STOP) {
			// System.out.println("update() STOP for " + name);
			clip.stop();
			clip.setFramePosition(0); // NEW
			if (!isLooping) { // it isn't looping
			} else { // else play it again
				clip.start();
			}
		}
	} // end of update()

	public void close() {
		if (clip != null) {
			clip.stop();
			clip.close();
		}
	}

	public void play(boolean toLoop) {
		if (clip != null) {
			isLooping = toLoop;
			clip.start(); // start playing
		}
	}
	
	public boolean isPlaying(){
		if(clip!=null)
			return clip.isRunning();
		else
			return false;
	}

	public void stop()
	// stop and reset clip to its start
	{
		if (clip != null) {
			isLooping = false;
			clip.stop();
			clip.setFramePosition(0);
		}
	}

	public void pause()
	// stop the clip at its current playing position
	{
		if (clip != null)
			clip.stop();
	}

	public void resume() {
		if (clip != null)
			clip.start();
	}

	public String getName() {
		return name;
	}

} // end of SoundClip class