package com.sdhoo.pdloan.smsctr.chuanglan.dto;

/**
 * 批量发送短信响应信息
 * @author SD_LJB(LiuJianbin)
 * @data 2018-10-19 17:39:07
 *
 */
public class ChuanglanBathsendRsp {

	// {"code":"0","msgId":"18101822473127723","time":"20181018224731","errorMsg":""}  
	
	/**
	 * 编号,"0"为成功,其它值失败
	 */
	String code ;
	
	/**
	 * 消息ID
	 */
	String msgId ;
	
	/**
	 * 时间,精确到秒,yyyyMMddHHmmss
	 */
	String time ;
	
	/**
	 * 错误信息
	 */
	String errorMsg ;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean checkSuccess() {
		return "0".equals(this.getCode()); 
	}
	
}
