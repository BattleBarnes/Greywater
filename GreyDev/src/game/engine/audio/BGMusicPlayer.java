package game.engine.audio;
/**
 * 
 * @author Barnes
 * Help from http://codeidol.com/java/swing/Audio/Play-Non-Trivial-Audio/ and
 * also Dr Mailler, who was kind enough to read the Oracle docs to me, because I
 * can't.
 * Code sections from http://docs.oracle.com/javase/tutorial/sound/playing.html
 * 
 * This plays the background .wav by using a DataLine instead of a clip
 */


import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class BGMusicPlayer implements Runnable {

	//the audio file to be played and its various helper pieces
	File file;
	AudioInputStream stream;
	SourceDataLine line;
	int frameSize;
	byte[] buffer = new byte [32 * 1024]; 
	Thread player;
	boolean playing = false;
	boolean fileNotOver = true;
	
	/**
	 * Constructor! Sets up the DataLine that will pipe out audio and the thread
	 * to allow it to play at the same time as the game.
	 * @param inputFile -- the music file to be played
	 */

	public BGMusicPlayer (File inputFile){
		try{
			file = inputFile;
			stream = AudioSystem.getAudioInputStream (inputFile);
			AudioFormat format = stream.getFormat();
			
			frameSize = format.getFrameSize(); 
			
			DataLine.Info info =new DataLine.Info (SourceDataLine.class, format); 
			line = (SourceDataLine) AudioSystem.getLine (info);
			
			line.open(); 
			
			player = new Thread (this);       
		}
		catch(Exception e){
			System.out.println("That is not a valid file.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Actually plays the music
	 */
	public void run() {
		int readPoint = 0;
		int bytesRead = 0;
		player.setPriority(Thread.MIN_PRIORITY); //Because games > music
		
		while (fileNotOver) {
			if (playing) {
				try {
					bytesRead = stream.read (buffer, readPoint, 10); //10 is arbitrary, but functional
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (bytesRead == -1) { 
					fileNotOver = false; 
					break;
				}


				// send to line
				line.write (buffer, readPoint, bytesRead);
				Thread.yield(); //let the game go for a bit
				}
			}
		stop();
		
	} 
	
	/**
	 * Starts the music
	 */
	public void start() {
		playing = true;
		if(!player.isAlive())
		player.start();
		line.start();
	}
	
	/**
	 * Stops the music
	 */
	public void stop() {
		playing = false;
		line.stop();
	}
	
	public boolean playing(){
		return playing;
	}

	
	public void newSong(File inputFile){
		try{
			
			if(player != null){
				line.stop();
				line.flush();
				line.close();
			//	player.join();
				playing = false;
			}
			
			file = inputFile;
			stream = AudioSystem.getAudioInputStream (inputFile);
			AudioFormat format = stream.getFormat();
			
			frameSize = format.getFrameSize(); 
			
			DataLine.Info info =new DataLine.Info (SourceDataLine.class, format); 
			line = (SourceDataLine) AudioSystem.getLine (info);
			
			line.open(); 
			start();
			//player = new Thread(this);       
		}
		catch(Exception e){
			System.out.println("That is not a valid file.");
			e.printStackTrace();
		}
	}
	
}

