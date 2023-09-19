package com.example.democore.msa.proxy;

import lombok.Getter;
import lombok.Setter;

/**
 * JSON-RPC 요청
 *
 * @author GSITM
 *
 */
@Getter
@Setter
public class JsonRpcRequest {
	private String method;
	private Params params;

	@Getter
	@Setter
	public static class Params {
        private String className;
        private String methodName;
        private String[] argumentClassList;
        private String[] argumentList;

	}

}
