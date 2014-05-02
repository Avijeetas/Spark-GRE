package org.spark.util;

public interface AbstractRequsetListener<Response> {

	/**
	 * this method is called when the request is complete
	 * 
	 * @param response
	 */
	public void onComplete(Response response);
	
	/**
	 * this method is called when runtime error occurred during the request 
	 * 
	 * @param error
	 */
	public void onError(Error error);
	
	/**
	 * this method is called when runtime exceptions occurred during the request
	 * 
	 * @param exception
	 */
	public void onException(Exception exception);
	
}
