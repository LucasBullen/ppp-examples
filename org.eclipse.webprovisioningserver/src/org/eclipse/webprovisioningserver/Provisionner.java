package org.eclipse.webprovisioningserver;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;

public class Provisionner {
	public ProvisionResult provision(ProvisioningParameters parameters) {
		File location = new File(parameters.location);
		Boolean makeLocation = !location.exists();
		if (makeLocation) {
			location.mkdirs();
		}
		return applyTemplate(parameters);
	}

	private ProvisionResult createErrorResult(String errorMessage) {
		return new ProvisionResult(errorMessage, new ErroneousParameter[0], null, new String[0]);
	}

	private ProvisionResult applyTemplate(ProvisioningParameters parameters) {
		String[] paths = new String[0];
		switch (parameters.templateSelection.id) {
		case "hello_world":
			paths = new String[] { "/HelloWorld/index.html" };
			break;
		case "form":
			paths = new String[] { "/Form/index.html", "/Form/result.html" };
			break;
		case "advanced_js":
			paths = new String[] { "/AdvancedJavaScript/index.html", "/AdvancedJavaScript/first.js",
			"/AdvancedJavaScript/second.js" };
			break;
		case "advanced_css":
			paths = new String[] { "/AdvancedCSS/index.html", "/AdvancedCSS/first.css", "/AdvancedCSS/second.css" };
			break;
		}
		for (String path : paths) {
			try {
				InputStream in = getClass().getResourceAsStream(path);
				int index = path.lastIndexOf('/');
				Files.copy(in, new File(parameters.location + (index == -1 ? path : path.substring(index))).toPath());
			} catch (Exception e) {
				return createErrorResult(e.getMessage());
			}
		}
		return new ProvisionResult(null, new ErroneousParameter[0], parameters.location,
				new String[] { "index.html" });
	}
}
