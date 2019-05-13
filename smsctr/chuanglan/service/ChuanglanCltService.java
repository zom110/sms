package com.sdhoo.pdloan.smsctr.chuanglan.service;


/**
 * 创蓝客户端服务.
 * @author SD_LJB(LiuJianbin)
 * @data 2018-10-18 20:29:40
 *
 */
public interface ChuanglanCltService {

	
	/**
	 * 批量发送短信
	 * @param chnlAcct TODO
	 * @param chnlPwd TODO
	 * @param tradeNo TODO
	 * @param phones 电话号码,多个用逗号隔开
	 * @param smsCtt
	 * @param extend TODO
	 * @return TODO
	 */
	 String batchSendSms(String chnlAcct , String chnlPwd, String tradeNo, String phones, String smsCtt, String extend );
	 
	 
	
	
}
