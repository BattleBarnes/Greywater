/**
 * @author Jeremy Barnes
 * Jan 10, 2013
 * 
 * This class is for loading in images. It is exactly what it says on the tin.
 * 
 * UNLESS YOU HATE YOURSELF, DON'T READ THIS CLASS. READ THIS COMMENT, AND THEN TAKE IT ON FAITH THAT THE
 * SYSTEM WORKS. IT DOES. TRUST ME. IT'S NOT IMPORTANT TO GAME LOGIC.
 * 
 * A word on the system of image loading:
 * 
 * The image loader loads images specified by a text file in the "Images" folder in the working path.
 * The name of the text file is user-specified, but the format is not.
 * 
 * The loader uses a string parser to tell what images to load and what to do with them, it can handle individual images,
 * groups of images that are meant to be used together, a single image comprised of multiple sub images, and 
 * sprite sheets.
 * The format must be as follows:
 * 
 * S - single images 
 * G - groups of images with incremental names (g1 g2 g3)
 * L - a line of images in one file
 * T - a sprite sheet (2d)
 * A - sprite sheet where each cell is a unique set
 * 
 * S format: S Name,filepath
 * G format: G SeriesName,Fnm1,Fnm2,Fnm3...
 * L format: L SeriesName,Filepath,Number of images on line
 * T format: T Filepath,number of rows, number of columns,RowName1, RowName2, RowName3, RowName4,.....  
 * A format: A Filepath,number of rows, number of columns, (1,1) name, (1,2) name........
 * 
 * HERE ARE SOME EXAMPLES
 * 
 * G TavishWalkSouth,Tavish_Walk_South_1.png,Tavish_Walk_South_2.png,Tavish_Walk_South_3.png,Tavish_Walk_South_4.png,Tavish_Walk_South_5.png,Tavish_Walk_South_6.png
 * S TavishStandSouth,Tavish_Stand_South_1.png
 * L TestImage,TestImage.png,6
 * T SprSh1.png,5,5,SprSh_Up,SprSh_Left,SprSh_Right,SprSh_Down,SprSh_Attack
 * A SprSh2.png,2,2,Up,Down,Left,Right
 */
package game.engine;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	private static String filePath;
	private static HashMap images = new HashMap();
	BufferedImage[] indivFiles;
	
	/**
	 * Constructor, calls readFile on the text file provided to load images into memory from disk.
	 * IMPORTANT NOTE: Do not provide individual files or call this constructor for multiple sprites.
	 * Each sprite uses the same ImageLoader, they all call getter methods within this.
	 * 
	 * @param filepath - the filepath of the text file with image names in
	 */
	public static void init(String filepath) {
		filePath = filepath;
	
		readFile(filePath);
	}

/*
 * ********************** LOADING METHODS *********************************
 * These methods are used to get images (.pngs) into memory off the HDD
 */
	
	/**
	 * Opens and parses a text file that indicates what needs to be opened.
	 * Calls other loader methods as indicated.
	 * 
	 * @param filePath - The path of the text file with the image information in it.
	 * 
	 * 
	 */
	private static void readFile(String filePath){
		try{
			
			File file = new File("Images/"+filePath);
			BufferedReader br = new BufferedReader(new FileReader(file));

			while(br.ready()){
				String currLine = br.readLine();
				if(currLine.length() == 0)//blank line
					continue;
				if(currLine.startsWith("//")) //comment
					continue;
				if(currLine.startsWith("  ")) //nothing
					continue;
				if(currLine.startsWith("S")) //single Image
					loadSingle(currLine);
				if(currLine.startsWith("G")) //group of individual images
					loadGroup(currLine);
				if(currLine.startsWith("L"))
					loadLine(currLine);
				if(currLine.startsWith("T"))
					loadSheet(currLine);
				if(currLine.startsWith("A"))
					loadLonerSheet(currLine);
			}

			br.close();
			br = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads in a single image, places it into the hashmap.
	 * 
	 * @param lineData - the line of the text file with info - 
	 * 					 S Name,FilePath
	 */
	private static void loadSingle(String lineData) {
		lineData = lineData.substring(2); //skip the line label (S)

		StringTokenizer st = new StringTokenizer(lineData, ",");
		String imgName = st.nextToken(); //the name of the image, how we'll find it in the hash
		
		images.put(imgName, loadImage(st.nextToken()));//inset into hashmap
	}

	/**
	 * Loads in a group of images, puts into a set, puts the set into the HashMap of other Images
	 * 
	 * @param lineData - the corresponding text file line-
	 * 					 G Name,Fnm1,Fnm2...
	 */
	private static void loadGroup(String lineData){
		lineData = lineData.substring(2); //skip line label (g)

		StringTokenizer st = new StringTokenizer(lineData, ",");
		String name = st.nextToken(); //the name of the group

		//ArrayList that will actually be in the Hashmap with the images inside it.
		ArrayList imageSet = new ArrayList();

		//get the images indicated by the current line
		while(st.hasMoreTokens()){
			//and put them into the arraylist
			imageSet.add(loadImage(st.nextToken()));
		}
		
		//putting the image list into the set of all images.
		images.put(name, imageSet);
	}

	/**
	 * Loads in a group of images all contained with in an individual file (one PNG for instance)
	 * so long as they are in a straight line, evenly spaced.
	 * 
	 * @param lineData - the corresponding text file line-
	 * 					 L Name,FilePath,Cols
	 */
	private static void loadLine(String lineData){
		lineData = lineData.substring(2); //skip line label (L)

		StringTokenizer st = new StringTokenizer(lineData, ",");
		String name = st.nextToken(); //the name of the group
		BufferedImage line = loadImage(st.nextToken()); //get the file
		int width = line.getWidth();
		int height = line.getHeight();
		
		//and divvy it up
		ArrayList imageSet = new ArrayList();
		
		int frames = Integer.parseInt(st.nextToken());
		int frameWidth = width/frames;
		
		for(int i = 0; i < frames; i++){
			imageSet.add(line.getSubimage(i*frameWidth, 0, frameWidth, height));
		}
		images.put(name,imageSet);
	}
	
	/**
	 * Loads in a group of images contained in a 2D sprite sheet (single file)
	 * 
	 * @param lineData - the corresponding text file line- 
	 * 					 T Filepath,number of rows, number of columns,RowName1, RowName2,...
	 */
	private static void loadSheet(String lineData){
		lineData = lineData.substring(2); //skip line label (T)

		StringTokenizer st = new StringTokenizer(lineData, ",");
		BufferedImage sheet = loadImage(st.nextToken()); //get the file
		
		//get the file data
		int width = sheet.getWidth();
		int height = sheet.getHeight();
		int rows = Integer.parseInt(st.nextToken());
		int framesPerRow = Integer.parseInt(st.nextToken());
		int frameWidth = width/framesPerRow;
		int frameHeight = height/rows;
		
		//and divvy it up		
		for(int i = 0; i < rows; i++){
			ArrayList imageSet = new ArrayList();
			for(int j = 0; j < framesPerRow; j++){
				imageSet.add(sheet.getSubimage(j*frameWidth, i*frameHeight, frameWidth, frameHeight));
			}
			String name = st.nextToken();
			System.out.println(name + " sheet loaded");
			images.put(name,imageSet);
		}
	}
	
	/**
	 * Loads in a group of images contained in a 2D sprite sheet (single file) that are
	 * stored separately and have no relation to each other. (Like tiles!)
	 * 
	 * @param lineData - the corresponding text file line- 
	 * 					 A Filepath,number of rows, number of columns,Name1, Name2,...
	 */
	private static void loadLonerSheet(String lineData){
		lineData = lineData.substring(2); //skip line label (T)

		StringTokenizer st = new StringTokenizer(lineData, ",");
		BufferedImage sheet = loadImage(st.nextToken()); //get the file
		
		//get the file data
		int width = sheet.getWidth();
		int height = sheet.getHeight();
		int rows = Integer.parseInt(st.nextToken());
		int framesPerRow = Integer.parseInt(st.nextToken());
		int frameWidth = width/framesPerRow;
		int frameHeight = height/rows;
		//and divvy it up		
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < framesPerRow; j++){
				String name = st.nextToken();
				images.put(name,sheet.getSubimage(j*frameWidth, i*frameHeight, frameWidth, frameHeight));
				System.out.println(name);

			}
		}
	}

	/**
	 * Loads in image files with a given filepath. Used for the actual disk -> memory conversion.
	 * Could also be used for evil if imgPath is called by outsiders using a hardcoded path.
	 * 
	 * @param imgPath - Filepath of the Image
	 * @return Returns the drawable BufferedImage
	 * @throws IOException if the Image isn't there, fuck you
	 */
	public static BufferedImage loadImage(String imgPath){
		BufferedImage I = null;
		try {
			I = ImageIO.read(new File("Images/"+imgPath));
		} catch (IOException e) {e.printStackTrace();}
		
		return I;
	}
	
/* 
 * ********************** ACCESSING METHODS *******************************
 * These methods are for internal use by Sprites, etc. They get images out of the image map
 * For display purposes, or give information about the image map for animating purposes.
 */
	
	/**
	 * 
	 * Gets an individual (S) image out for display purposes.
	 * 
	 * @param name - the name of the image. First value on the images.txt line
	 * @return The image as a BufferedImage or null if the image isn't there
	 */
	public static BufferedImage getSingleImage(String name){
		BufferedImage im;
		
		try{//find the image
			im = (BufferedImage) images.get(name);
		}
		catch(Exception e){ //if it doesn't exist, fuck you, nulls!!
			System.out.println("No images exist for " + name + " !!!!");
			im = null;
		}
		return im;
	}
	
	/**
	 * Gets a single image out of a set of images grouped together.
	 * For animating shit.
	 * 
	 * @param name - the name of the set. First value of the line of the text file.
	 * @param position - which image in the set to display
	 * @return the image
	 */
	public static BufferedImage getGroupedImage(String name, int position){
		BufferedImage im; //the image to be returned;

		try{//get the image set out of the Hashmap
			ArrayList imgSet = (ArrayList) images.get(name);
			im = (BufferedImage) imgSet.get(position);
		}
		catch(Exception e){//if it isn't there, fuck you!
			System.out.println("That image doesn't exist! Image name: " + name + " and position: " + position);
			im = null;
		}
		return im;
	}
	
	 public static int getSeriesCount(String name){
	    ArrayList imgSet = (ArrayList) images.get(name);
	    if (imgSet == null) {
	      System.out.println("404 " + name + " not found! (ImageLoader.java)");  
	      return 0;
	    }
	    return imgSet.size();
	  } // end of numImages()
	
}
