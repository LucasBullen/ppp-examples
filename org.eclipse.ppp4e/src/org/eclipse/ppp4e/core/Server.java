package org.eclipse.ppp4e.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import org.eclipse.ppp4e.ProvisioningPlugin;
import org.eclipse.ppp4j.messages.Initialize;
import org.eclipse.ppp4j.messages.InitializeResult;
import org.eclipse.ppp4j.messages.ProvisionResult;
import org.eclipse.ppp4j.messages.ProvisioningParameters;
import org.eclipse.ppp4j.messages.RpcRequest;
import org.eclipse.ppp4j.messages.RpcResponse;

import com.google.gson.Gson;

public class Server {
	boolean isInitialized;
	private String baseMethod = "projectProvisioning/";
	StreamConnectionProvider streamConnectionProvider;
	Map<Integer, Semaphore> messageResponseSemaphores = new HashMap<>();
	Map<Integer, Object> messageResponses = new HashMap<>();
	private Integer nextMessageId = 0;

	public Server(StreamConnectionProvider streamConnectionProvider) {
		this.streamConnectionProvider = streamConnectionProvider;
		try {
			streamConnectionProvider.start();
			listenForMessages();
		} catch (IOException e) {
			ProvisioningPlugin.logError(e);
		}
	}

	// TODO: replace with actual calls to a server
	public CompletableFuture<InitializeResult> Initalize() {
		return sendMessage("initalize", new Initialize(true, true));
		//		isInitialized = true;
		//		return CompletableFuture.completedFuture(
		//				new InitializeResult(false, false, false, null, null, new ProvisioningParameters("new_rust_project",
		//						"/tmp/new_rust_project", null, null, new ComponentVersionSelection[0])));
	}

	public CompletableFuture<ProvisionResult> Provision(ProvisioningParameters parameters) {
		return sendMessage("provision", parameters);
		//		return CompletableFuture.completedFuture(
		//				new ProvisionResult(null, new ErroneousParameter[0], new String[] { "test" }, new String[] { "test" }));
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	private <T> CompletableFuture<T> sendMessage(String method, Object params) {
		try {
			streamConnectionProvider.start();
			final int id = nextMessageId;
			nextMessageId++;
			RpcRequest request = new RpcRequest(String.valueOf(id), baseMethod + method, params);
			try (PrintWriter p = new PrintWriter(streamConnectionProvider.getOutputStream())) {
				Gson gson = new Gson();
				p.println(gson.toJson(request));
			}
			Semaphore responSemaphore = new Semaphore(0);
			messageResponseSemaphores.put(id, responSemaphore);
			@SuppressWarnings("unchecked")
			CompletableFuture<T> completableFuture = CompletableFuture.supplyAsync(() -> {
				try {
					responSemaphore.acquire();
					return (T) messageResponses.get(id);
				} catch (InterruptedException e) {
					ProvisioningPlugin.logError(e);
					return null;
				}
			});
			return completableFuture;
		} catch (IOException e) {
			ProvisioningPlugin.logError(e);
			return CompletableFuture.completedFuture(null);
		}
	}

	private void listenForMessages() {
		CompletableFuture.runAsync(() -> {
			try (BufferedReader in = new BufferedReader(
					new InputStreamReader(streamConnectionProvider.getInputStream()))) {
				String line = in.readLine();
				while (line != null) {
					Gson gson = new Gson();
					RpcResponse response = gson.fromJson(line, RpcResponse.class);
					Integer id = Integer.getInteger(response.id);
					messageResponses.put(id, response.result);
					messageResponseSemaphores.get(id).release();
					line = in.readLine();
				}
			} catch (IOException e) {
				ProvisioningPlugin.logError(e);
			}
			System.out.println("no longer listening");
		});
	}
}
