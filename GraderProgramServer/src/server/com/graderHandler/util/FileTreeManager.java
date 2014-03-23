package server.com.graderHandler.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;


public class FileTreeManager {
    private static final Path root = Paths.get("graderProgram", "data");
    
    public static void purgeSubmission(Path submission) throws IOException {
        purge(submission);
    }
    
    public static void checkPurgeRoot() throws IOException {
        if (doPurgeRoot()) {
            purge(root);
        }
    }
    
    private static boolean doPurgeRoot() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return root.resolve(Integer.toString(year - 1)).toFile().exists();
    }
    
    private static void purge(Path p) throws IOException {
        if (!p.toFile().exists()) {
            return;
        }
        Files.walkFileTree(p, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!file.toFile().getName().endsWith(".bak")) {
                    Files.delete(file);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                if (e == null) {
                    try {
                        Files.delete(dir);
                    } catch (IOException ex) {
                        if (!(ex instanceof DirectoryNotEmptyException || ex instanceof FileNotFoundException)) {
                            throw ex;
                        }
                    }
                    return FileVisitResult.CONTINUE;
                } else {
                    // directory iteration failed
                    throw e;
                }
            }
        });
    }
    
    public static void backup(Path file, Path copy) throws FileNotFoundException, IOException {
        if (copy.toFile().exists()) {
            Files.delete(copy);
        }
        Files.createFile(copy);
        Files.copy(file, new FileOutputStream(copy.toFile()));
    }

    private FileTreeManager() {
    }
}
