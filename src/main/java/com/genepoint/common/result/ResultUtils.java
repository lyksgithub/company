package com.genepoint.common.result;

public class ResultUtils {
	
	private Integer status;
	
	private String message;
	
	private Object data;

	
	public ResultUtils(Integer status, String message, Object data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	
	public static ResultUtils create(Integer status, String message, Object data) {
		return new ResultUtils(status,message,data);
	}
	
	public static ResultUtils create(Integer status, String message) {
		return new ResultUtils(status,message,null);
	}
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
}
