package io.openshift.wizard.server;

import org.eclipse.ppp4j.messages.ErroneousParameter;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;

public class Provisionner {
	public ProvisionResult provision(ProvisioningParameters parameters) {
		return new ProvisionResult(null, new ErroneousParameter[0], parameters.location,
				new String[0]);
	}
}
