package game.engine.audio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

/**AudioLoader.java
 * @author Jeremy Barnes March 30, 2013
 *
 * UNLESS YOU HATE YOURSELF, DON'T READ THIS CLASS. READ THIS COMMENT, AND THEN TAKE IT ON FAITH THAT THE
 * SYSTEM WORKS. IT DOES. TRUST ME. IT'S NOT IMPORTANT TO GAME LOGIC.
 *
 *Used to load in audio files
 */

public class AudioLoader {
	
	private static String filePath;
	private static HashMap sounds = new HashMap();
	private static SoundClip lastPlayed;
	
	/**
	 * Calls readFile on the text file provided to load sounds into memory from disk.
	 * IMPORTANT NOTE: Do not provide individual files or call this constructor for multiple sounds.
	 * Each clip uses the same AudioLoader, they all call getter methods within this.
	 * 
	 * @param filepath - the filepath of the text file with sound names in
	 */
	public static void init(String filepath) {
		filePath = filepath;
	
		readFile(filePath);
	}
	
	/*
	 * ********************** LOADING METHODS *********************************
	 * These methods are used to get sounds (.wav, .ogg, etc) into memory off the HDD
	 */
		
		/**
		 * Opens and parses a text file that indicates what needs to be opened.
		 * Calls other loader methods as indicated.
		 * 
		 * @param filePath - The path of the text file with the audio information in it.
		 * 
		 * 
		 */
		private static void readFile(String filePath){
			try{
				
				File file = new File("Audio/"+filePath);
				BufferedReader br = new BufferedReader(new FileReader(file));

				while(br.ready()){
					String currLine = br.readLine();
					if(currLine.length() == 0)//blank line
						continue;
					if(currLine.startsWith("//")) //comment
						continue;
					if(currLine.startsWith("  ")) //nothing
						continue;
					if(currLine.startsWith("S")) //single file
						loadSingle(currLine);
					if(currLine.startsWith("G")) //group of sounds
						loadGroup(currLine);
				}

				br.close();
				br = null;
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		
		/**
		 * Loads in a single file, places it into the hashmap.
		 * 
		 * @param lineData - the line of the text file with info - 
		 * 					 S Name,FilePath
		 */
		private static void loadSingle(String lineData) {
			lineData = lineData.substring(2); //skip the line label (S)

			StringTokenizer st = new StringTokenizer(lineData, ",");
			String soundName = st.nextToken(); //the name of the sound, how we'll find it in the hash
			
			sounds.put(soundName, new SoundClip(st.nextToken()));//inset into hashmap
			System.out.println(soundName + "loaded");
		}

		/**
		 * Loads in a group of sounds, puts into a set, puts the set into the HashMap of other sounds
		 * 
		 * @param lineData - the corresponding text file line-
		 * 					 G Name,Fnm1,Fnm2...
		 */
		private static void loadGroup(String lineData){
			lineData = lineData.substring(2); //skip line label (g)

			StringTokenizer st = new StringTokenizer(lineData, ",");
			String name = st.nextToken(); //the name of the group

			//ArrayList that will actually be in the Hashmap with the sounds inside it.
			ArrayList soundSet = new ArrayList();

			//get the files indicated by the current line
			while(st.hasMoreTokens()){
				//and put them into the arraylist
				soundSet.add(new SoundClip(st.nextToken()));
			}
			
			//putting the sound list into the set of all audio files.
			sounds.put(name, soundSet);
		}
		
/* 
 * ********************** ACCESSING METHODS *******************************
 * These methods are for internal use by Entites, etc. The entity does not interface directly with the SoundClip, instead they
 * specify which one they want played, and the loader plays it after retrieving it.
 */
		
	public static void playSingle(String name, boolean toLoop)
	// play (perhaps loop) the specified clip
	{
		SoundClip ci = (SoundClip) sounds.get(name);
		if (ci == null)
			System.out.println("Error: " + name + "not stored");
		else if(!ci.isPlaying())
			ci.play(toLoop);
	} // end of play()

	public static void stopSingle(String name)
	// stop the clip, resetting it to the beginning
	{
		SoundClip ci = (SoundClip) sounds.get(name);
		if (ci == null)
			System.out.println("Error: " + name + "not stored");
		else
			ci.stop();
	} // end of stop()
	
	public static void playGrouped(String name){
		ArrayList soundGroup = (ArrayList) sounds.get(name);
		if (soundGroup == null)
			System.out.println("Error: " + name + "not stored");
		else{
			Random rand = new Random();
			int i = rand.nextInt(soundGroup.size());
			SoundClip ci = (SoundClip) soundGroup.get(i);
			if(ci != lastPlayed){
				ci.play(false);
				lastPlayed = ci;
			}else
				playGrouped(name);
				
		}
	}

}
