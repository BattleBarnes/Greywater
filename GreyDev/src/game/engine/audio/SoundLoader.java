package game.engine.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 * AudioLoader.java
 * 
 * @author Jeremy Barnes March 30, 2013
 * 
 * UNLESS YOU HATE YOURSELF, DON'T READ THIS CLASS. READ THIS COMMENT, AND THEN TAKE IT ON FAITH THAT THE SYSTEM WORKS. IT DOES. TRUST ME. IT'S NOT
 * IMPORTANT TO GAME LOGIC.
 * 
 * Used to load in audio files
 */
public class SoundLoader {
	private static String filePath;
	private static HashMap sounds = new HashMap();
	private static ArrayList<Clip> clipSet = new ArrayList<Clip>();
	/**
	 * Calls readFile on the text file provided to load sounds into memory from disk. IMPORTANT NOTE: Do not provide individual files or call this
	 * constructor for multiple sounds. Each clip uses the same AudioLoader, they all call getter methods within this.
	 * 
	 * @param filepath - the filepath of the text file with sound names in
	 */
	public static void init(String filepath) {
		filePath = filepath;
		
		readFile(filePath);
	}
	/*
	 * ********************** LOADING METHODS ********************************* 
	 * These methods are used to get sounds (.wav, .ogg, etc) into memory off
	 * the HDD
	 */
	/**
	 * Opens and parses a text file that indicates what needs to be opened. Calls other loader methods as indicated.
	 * 
	 * @param filePath - The path of the text file with the audio information in it.
	 * 
	 * 
	 */
	private static void readFile(String filePath) {
		try {
			InputStream url = SoundLoader.class.getClassLoader().getResourceAsStream("Audio/" + filePath);
			InputStreamReader isr = new InputStreamReader(url);
			BufferedReader br = new BufferedReader(isr);
			while (br.ready()) {
				String currLine = br.readLine();
				if (currLine.length() == 0)// blank line
					continue;
				if (currLine.startsWith("//")) // comment
					continue;
				if (currLine.startsWith("  ")) // nothing
					continue;
				if (currLine.startsWith("S")) // single file
					loadSingle(currLine);
			}
			br.close();
			url.close();
			isr.close();
			br = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Loads in a single file, places it into the hashmap.
	 * 
	 * @param lineData - the line of the text file with info - S Name,FilePath
	 */
	private static void loadSingle(String lineData) {
		lineData = lineData.substring(2); // skip the line label (S)
		StringTokenizer st = new StringTokenizer(lineData, ",");
		String soundName = st.nextToken(); // the name of the sound, how we'll find it in the hash
		sounds.put(soundName, new Sound(st.nextToken()));// inset into hashmap
		System.out.println(soundName + "loaded");
	}
	/*
	 * ********************** ACCESSING METHODS ******************************* These methods are for internal use by Entites, etc. The entity does
	 * not interface directly with the SoundClip, instead they specify which one they want played, and the loader plays it after retrieving it.
	 */
	public static void playSingle(String name)
	// play (perhaps loop) the specified clip
	{
		ArrayList remList = new ArrayList();
		for(Clip c: clipSet){
			if(!c.isActive()){
				c.stop();
				c.close();
				remList.add(c);
				//clipSet.remove(c);
			}
		}
		for(Object r: remList){
			clipSet.remove((Clip)r);
		}
		try {
			Sound csound = (Sound) sounds.get(name.toLowerCase());
			//System.out.println(csound.af);
			Clip clip = (Clip) AudioSystem.getLine(csound.info);
			clip.open(csound.af, csound.audio, 0, csound.size);
			clip.start();
			clipSet.add(clip);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Sound had issue in Soundloader.java line 109 " + name );
		}
	} // end of play()
}