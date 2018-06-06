package org.eclipse.ppp4j.messages;

public class RpcResponse {
	final public String jsonrcp = "2.0";
	public String id;
	public Object result;

	public RpcResponse(String id, String result) {
		this.id = id;
		this.result = result;
	}

}
