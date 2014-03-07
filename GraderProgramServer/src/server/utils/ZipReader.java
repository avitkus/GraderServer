package server.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Andrew Vitkus
 *
 */
public class ZipReader {
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final int BUFFER_SIZE = 4096;
	
	public static void read(File file, File dest) throws FileNotFoundException, IOException {
		
		try(ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)))) {
			ZipEntry entry;
			System.out.println("Target: " + dest.getCanonicalPath());
			while((entry = zis.getNextEntry()) != null) {
				String name = entry.getName();
				if (FILE_SEPARATOR.equals("\\")) {
					name = name.replace("/", "\\");
				} else if (FILE_SEPARATOR.equals("/")) {
					name = name.replace("\\", "/");
				}
				File out = new File(dest.getAbsolutePath() + FILE_SEPARATOR + name);
				System.out.println(out.getCanonicalPath());
				if (out.exists()) {
					out.delete();
				}
				int folderEnd = out.toPath().toString().lastIndexOf(FILE_SEPARATOR);
				folderEnd = Math.max(folderEnd, out.toPath().toString().lastIndexOf("\\"));
				folderEnd = Math.max(folderEnd, out.toPath().toString().lastIndexOf("/"));
				Files.createDirectories(Paths.get(out.toPath().toString().substring(0, folderEnd)));
				Files.createFile(out.toPath());
				try(BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out))) {
					while(zis.available() > 0) {
						
						byte[] data = new byte[BUFFER_SIZE];
				        int bytesRead = -1;
				        while ((bytesRead = zis.read(data)) != -1) {
				        	bos.write(data, 0, bytesRead);
				        }
						bos.flush();
					}
				}
			}
		}
	}
}
