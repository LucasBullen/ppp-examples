package org.eclipse.rustprovisioningserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;

public class Provisionner {
	public ProvisionResult provision(ProvisioningParameters parameters) {
		File location = new File(parameters.location);
		String projectName = parameters.name;

		Boolean makeLocation = !location.exists();
		if (makeLocation) {
			location.mkdirs();
		}

		List<String> commandLine = new ArrayList<>();
		commandLine.add(getCargoPath());
		commandLine.add("init");

		commandLine.add("--name");
		commandLine.add(projectName);

		commandLine.add("--bin");

		ProcessBuilder processBuilder = new ProcessBuilder(commandLine);
		processBuilder.directory(location);

		String errorMessage = "";
		try {
			Process process = processBuilder.start();
			if (process.waitFor() == 0) {
				return applyTemplate(parameters);
			} else {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
					String errorLine;
					while ((errorLine = in.readLine()) != null) {
						errorMessage += errorLine + '\n';
					}
				}
				if (makeLocation) {
					location.delete();
				}
				return createErrorResult(errorMessage);
			}
		} catch (InterruptedException | IOException e) {
			if (makeLocation) {
				location.delete();
			}
			return createErrorResult(e.getLocalizedMessage());
		}

	}

	private ProvisionResult createErrorResult(String errorMessage) {
		return new ProvisionResult(errorMessage, new ErroneousParameter[0], null, new String[0]);
	}

	private ProvisionResult applyTemplate(ProvisioningParameters parameters) {
		switch (parameters.templateSelection.id) {
		case "crate_example":
			try {
				Files.write(Paths.get(parameters.location + "/Cargo.toml"),
						("time = \"" + parameters.templateSelection.getComponentVersion("crate_version") + "\"")
								.getBytes(),
						StandardOpenOption.APPEND);
				InputStream in = getClass().getResourceAsStream("/crate_example/main.rs");
				Files.copy(in, new File(parameters.location + "/src/main.rs").toPath(),
						StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception e) {
				return createErrorResult(e.getLocalizedMessage());
			}
			return new ProvisionResult(null, new ErroneousParameter[0], parameters.location,
					new String[] { "src/main.rs", "Cargo.toml" });
		case "hello_world":
		default:
			return new ProvisionResult(null, new ErroneousParameter[0], parameters.location,
					new String[] { "src/main.rs" });
		}
	}

	private String getCargoPath() {
		String command = findCommandPath("cargo");
		if (command.isEmpty()) {
			File possibleCommandFile = new File(System.getProperty("user.home") + "/.cargo/bin/cargo");
			if (possibleCommandFile.exists() && possibleCommandFile.isFile() && possibleCommandFile.canExecute()) {
				return possibleCommandFile.getAbsolutePath();
			}
		}
		return command;
	}

	private String findCommandPath(String command) {
		try {
			ProcessBuilder builder = new ProcessBuilder("which", command);
			Process process = builder.start();

			if (process.waitFor() == 0) {
				try (BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					return in.readLine();
				}
			}
		} catch (IOException | InterruptedException e) {
			// Errors caught with empty return
		}
		return "";
	}
}
