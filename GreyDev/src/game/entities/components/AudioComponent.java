package game.entities.components;

import game.Core;
import game.engine.audio.AudioLoader;

public class AudioComponent {
	
	private boolean isLooping;
	private boolean isTicking;
	private boolean group;
	
	private String name;
	private boolean play = false;
	
	private String currSound;
	
	//time keepers
	private double cycleLength_Millis;
	private long elapsedTime_Millis;
	private double sequenceDuration_Millis;
	private int animPeriod_Millis; //for matching animations
	

	public AudioComponent(String name){
		animPeriod_Millis = (int) (Core.animPeriodNano/1000000);
		this.name = name;
	}
	
	public void play(){
		if (currSound != "" && play) {
			if (group)
				AudioLoader.playGrouped(currSound);
			else
				AudioLoader.playSingle(currSound, false);
		}
		tick();
	}
	
	

	public void tick(){
		if(elapsedTime_Millis > cycleLength_Millis){
			play = true;
			elapsedTime_Millis = 0;
		}else
			play = false;
		elapsedTime_Millis += animPeriod_Millis;

		
	}
	
	
	/**Loops a sound at the animation rate for a set period of time
	 * 
	 * @param duration_seconds - length of time to loop in seconds
	 * @param ident - Sounds are loaded as name+ident (Tavish + footstep)
	 * @param group - is it a part of a group of sounds to be chosen randomly
	 */
	public void	loopSound(double duration_seconds, String ident, boolean group){
		if((name+ident).equalsIgnoreCase(currSound))
			return;
		
		currSound = name + ident;
		isLooping = true;
		isTicking = true;
		
		elapsedTime_Millis = 0;
		
		sequenceDuration_Millis = duration_seconds * 1000;
		cycleLength_Millis = sequenceDuration_Millis;
		this.group = group;
	}
	
	/**
	 * Plays a single sound, once, immediately
	 * @param ident - Sounds are loaded as name+ident (Tavish + footstep)
	 * @param group - is it a part of a group of sounds to be chosen randomly
	 */
	public void playSound(String ident, boolean group){
		if((name+ident).equalsIgnoreCase(currSound))
			return;
		
		play = true;
		currSound = name+ident;
		isLooping = false;
		isTicking = false;
		
		elapsedTime_Millis = 0;
		
		cycleLength_Millis = sequenceDuration_Millis;
		this.group = group;
	}
	
	
	
	public void stopRepeat(){
		
	}
	


	
}
