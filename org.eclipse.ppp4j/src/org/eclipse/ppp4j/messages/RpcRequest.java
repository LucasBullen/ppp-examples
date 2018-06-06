package org.eclipse.ppp4j.messages;

public class RpcRequest {
	final public String jsonrcp = "2.0";
	public String id;
	public String method;
	public Object params;

	public RpcRequest(String id, String method, Object params) {
		this.id = id;
		this.method = method;
		this.params = params;
	}
}
