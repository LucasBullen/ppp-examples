/*********************************************************************
 * Copyright (c) 2018 Red Hat Inc. and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Lucas Bullen (Red Hat Inc.) - Initial implementation
 *******************************************************************************/
package io.openshift.wizard.server;

import org.eclipse.ppp4j.messages.ComponentVersion;
import org.eclipse.ppp4j.messages.ComponentVersionSelection;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.Template;
import org.eclipse.ppp4j.messages.TemplateSelection;
import org.eclipse.ppp4j.messages.Version;

public class Initializer {
	public static ComponentVersion mission = new ComponentVersion("mission", "Mission",
			"A specification that describes what your application will do.", new Version[] {
					new Version("cache", "Cache", "Use a cache to improve the response time of applications"),
					new Version("externalized_configuration", "Externalized Configuration",
							"Use a ConfigMap to externalize configuration. ConfigMap is an object used by OpenShift to inject configuration data as simple key and value pairs into one or more Linux containers while keeping the containers independent of OpenShift"),
					new Version("circuit_breaker", "Circuit Breaker",
							"Report the failure of a service and then limit access to the failed service until it becomes available to handle requests"),
					new Version("crud", "CRUD",
							"Expand on the REST API Level 0 to perform CRUD operations on a PostgreSQL database using a simple HTTP REST API"),
					new Version("health_check", "Health Check", "Monitor an application's ability to service requests"),
					new Version("istio_distributed_tracing", "Istio - Distributed Tracing",
							"The Istio Distributed Tracing mission demonstrates how simple distributed tracing can be integrated with an application running on an Istio-enabled cluster."),
					new Version("istio_circuit_breaker", "Istio - Circuit Breaker",
							"The Istio Circuit Breaker mission demonstrates limiting access to a degraded service until it becomes available to handle requests. This helps prevent cascading failures in other services that depend on the failed services for functionality."),
					new Version("rest_api", "REST API Level 0",
							"Map business operations to a remote procedure call endpoint over HTTP using a REST framework"),
					new Version("secured", "Secured",
							"Expands on REST API Level 0 to secure the REST endpoint using Red Hat SSO which adds security to your applications while centralizing the security configuration"),
					new Version("istio_security", "Istio - Security",
							"Ths Istio Security mission demonstrates how Istio secures communication between microservices with Mutual TLS, and how services can be allowed or denied access to other services using ACL."),
					new Version("istio_routing", "Istio - Routing",
							"The Istio Routing mission demonstrates how Istio can be used to route traffic to/from services, including load balancing traffic to different versions of the same service. This can be used to set up A/B tests, canary deployments, or simply do rolling deployments during a production release.") });

	private static Version[] node_versions = new Version[] { new Version("10.x.rhoar", "10.x (RHOAR)", null),
			new Version("8.x.rhoar", "8.x (RHOAR)", null), new Version("10.x.community", "10.x (Community)", null),
			new Version("8.x.community", "8.x (Community)", null) };
	private static Version[] fuse_versions = new Version[] { new Version("7.0.0.rhf", "7.0.0 (Red Hat Fuse)", null),
			new Version("2.21.0.community", "2.21.0 (Community)", null) };
	private static Version[] vertx_versions = new Version[] {
			new Version("3.5.0.final.community", "3.5.0.Final (Community)", null),
			new Version("3.5.1.redhat-004", "3.5.1.redhat-004 (RHOAR)", null) };
	private static Version[] spring_versions = new Version[] {
			new Version("1.4.13.release.rhoar", "1.4.13.RELEASE (RHOAR)", null),
			new Version("1.4.13.release.community", "1.4.13.RELEASE (Community)", null) };
	private static Version[] wildfly_versions = new Version[] {
			new Version("7.1.0.redhat-77.rhoar", "7.1.0.redhat-77 (RHOAR)", null),
			new Version("2018.5.0.community", "2018.5.0 (Community)", null) };

	public static Template[] templates = new Template[] { new Template("nodejs", "Node.js",
			"A JavaScript runtime built on Chrome's V8 JavaScript engine, using an event-driven, non-blocking I/O model for lightweight efficiency.",
			new ComponentVersion[] { new ComponentVersion("version", "Version", null, node_versions) }),
			new Template("fuse", "Fuse",
					"The Fuse runtime enables you to deploy Spring Boot applications on OpenShift while leveraging the Fuse technology stack for middleware integration. The core technology provided by Fuse is Apache Camel for application integration including Spring Boot starters for the Camel components. The technology stack also provides transactions, Web services, security, management, and messaging clients.",
					new ComponentVersion[] { new ComponentVersion("version", "Version", null, fuse_versions) }),
			new Template("vertx", "Eclipse Vert.x", "A tool-kit for building reactive applications on the JVM.",
					new ComponentVersion[] { new ComponentVersion("version", "Version", null, vertx_versions) }),
			new Template("spring", "Spring Boot",
					"Create stand-alone, production-grade Spring based Applications that you can \"just run\".",
					new ComponentVersion[] { new ComponentVersion("version", "Version", null, spring_versions) }),
			new Template("wildfly", "WildFly Swarm",
					"An innovative approach to packaging and running Java EE applications, packaging them with just enough of the server runtime to \"java -jar\" your application.",
					new ComponentVersion[] { new ComponentVersion("version", "Version", null, wildfly_versions) }) };

	public static ComponentVersion cluster = new ComponentVersion("cluster", "OpenShift Cluster",
			"Choose the environment where the application will be deployed.",
			new Version[] { new Version("local", "Run Locally", "Download as ZIP"),
					new Version("pro-asia", "Pro: Asia Pacific", "Sydney"),
					new Version("pro-eu-west", "Pro: EU West", "Ireland"),
					new Version("pro-us-east", "Pro: US East", "N. Virginia"),
					new Version("starter-canada", "Starter: Canada", "Central"),
					new Version("starter-us-east", "Starter: US East", "Virginia"),
					new Version("starter-us-west-1", "Starter: US West", "California"),
					new Version("starter-us-west-2", "Starter: US West", "Oregon") });

	public InitializeResult initialize() {

		TemplateSelection selection = new TemplateSelection("nodejs", new ComponentVersionSelection[] {
				new ComponentVersionSelection("version", "10.x.rhoar")
		});
		InitializeResult result = new InitializeResult(true, true, true, templates,
				new ComponentVersion[] { mission, cluster },
				new ProvisioningParameters("newOSProject", "/tmp/newOSProject", "1.0.0-SNAPSHOT", selection,
						new ComponentVersionSelection[] { new ComponentVersionSelection("cluster", "local"),
								new ComponentVersionSelection("mission", "cache") }));
		return result;
	}
}