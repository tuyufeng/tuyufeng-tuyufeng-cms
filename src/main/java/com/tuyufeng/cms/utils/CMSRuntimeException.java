package com.tuyufeng.cms.utils;

/**
 * 
 * @ClassName: CMSRuntimeException 
 * @Description:  CMS 系统的统一异常
 * @author: charles
 * @date: 2019年7月18日 上午10:48:45
 */
public class CMSRuntimeException extends RuntimeException {
	
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	public CMSRuntimeException() {
		super();
	}

     public CMSRuntimeException(String message ) {
		super(message);
	}

}
