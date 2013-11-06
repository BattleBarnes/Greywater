package game.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Logger extends PrintStream {

	private static final String newLine = System.getProperty("line.separator");

	private final StringBuffer sb = new StringBuffer();
	private final PrintStream original;

	public Logger(File file) throws FileNotFoundException {
		super(file);

		this.original = this;
	}

	public void print(double d) {
		//sb.append(d);
		super.print(d);
	}

	public void print(String s) {
	//	sb.append(s);
		super.print(s);
	}

	public void println(String s) {
		//sb.append(s).append(newLine);
		super.println(s);
	}

	public void println() {
	//	sb.append(newLine);
		super.println();
	}

	public PrintStream printf(String s, Object... args) {
		//sb.append(String.format(s, args));
		super.printf(s, args);
		return this;
	}

	// .....
	// the same for ALL the public methods in PrintStream....
	// (your IDE should help you easily create delegates for the `original` methods.)

	public String getAllWrittenText() {
		return sb.toString();
	}

}
