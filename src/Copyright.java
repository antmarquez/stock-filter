import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;


public class Copyright {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

		List<File> javaFilesInFolder = Files.walk(Paths.get("src"))
				.filter(Files::isRegularFile)
				.map(Path::toFile)
				.filter(x -> x.getName().endsWith(".java"))
				.collect(Collectors.toList());

		for (File javaFile : javaFilesInFolder) {
			List<String> allLines = FileUtils.readLines(javaFile);
			if (!allLines.get(allLines.size()-1).contains("// Copyright"))
				FileUtils.write(javaFile, "\n// Copyright Anthony Marquez, 2020", true);
			else
				System.out.println(javaFile.getName() + " already has a copyright.");
		}
	}
}	
// Copyright Anthony Marquez, 2020