package server.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Andrew Vitkus
 *
 */
public interface IConfigReader {
	public void loadConfigFile(String file) throws FileNotFoundException, IOException;
	public void loadConfigFile(File file) throws FileNotFoundException, IOException;
	
	public String getString(String key);
	public boolean getBoolean(String key);
	public int getInt(String key);
	public double getDouble(String key);
	public float getFloat(String key);
	public long getLong(String key);
	public byte getByte(String key);
	public char getChar(String key);
}
