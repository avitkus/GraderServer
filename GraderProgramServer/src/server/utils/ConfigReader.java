package server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * 
 * @author Andrew Vitkus
 *
 */
public class ConfigReader implements IConfigReader {

	private HashMap<String,String> config;
	
	public ConfigReader() {
		config = new HashMap<>();
	}
	
	public ConfigReader(String file) throws FileNotFoundException, IOException {
		this();
		loadConfigFile(file);
	}
	
	public ConfigReader(File file) throws FileNotFoundException, IOException {
		this();
		loadConfigFile(file);
	}
	
	@Override
	public void loadConfigFile(String file) throws FileNotFoundException, IOException {
		loadConfigFile(new File(file));
	}
	
	@Override
	public void loadConfigFile(File file) throws FileNotFoundException, IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			while(br.ready()) {
				String line = br.readLine();
				line = line.trim();
				line = line.split("#")[0];
				if (!line.isEmpty()) {
					String[] property = line.split("[ ]*=[ ]*");
					config.put(property[0], property[1]);
				}
			}
		}
	}
	
	@Override
	public String getString(String key) {
		return config.get(key);
	}

	@Override
	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}

	@Override
	public int getInt(String key) {
		return Integer.parseInt(getString(key));
	}

	@Override
	public double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}

	@Override
	public float getFloat(String key) {
		return Float.parseFloat(getString(key));
	}

	@Override
	public long getLong(String key) {
		return Long.parseLong(getString(key));
	}

	@Override
	public byte getByte(String key) {
		return Byte.parseByte(getString(key));
	}

	@Override
	public char getChar(String key) {
		return getString(key).charAt(0);
	}
}
