package com.sdhoo.pdloan.smsctr.service;

import java.util.List;
import java.util.Map;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.smsctr.service.req.BatchSendSmsReq;
import com.sdhoo.pdloan.smsctr.service.req.SingleSendSmsReq;

public interface SmsSendService {

	/**
	 * 创建短信交易号.
	 */
	String genSmsTranCode(); 

	/**
	 * 批量创建交易号
	 * @param genCnt 要创建的数量
	 * @return
	 */
	List<String> batchGenSmsTranCode(int genCnt );

	/**
	 * 发送短信
	 * @param tranCode 交易号
	 * @param phone
	 * @param tpltKey 模板KEY 
	 * @param paramsMap
	 * @throws BaseServiceException 
	 */
    @Deprecated
	void sendSms(String tranCode , String phone , String tpltKey, Map<String,Object>paramsMap ) throws BaseServiceException ;

	/**
	 * 批量发送短信.
	 * @param phoneList 交易号
	 * @param tpltKey 模板KEY 
	 * @param paramsMap
	 * @throws BaseServiceException 
	 */
    @Deprecated
	void batchSendSms(String tranCode , List<String> phoneList ,String tpltKey , Map<String,Object> paramsMap ) throws BaseServiceException;


	/**
	 * 发送单条短信
	 * @param sendReq
	 */
	void sendSms(SingleSendSmsReq sendReq) throws BaseServiceException; 
	
	/**
	 * 发送多条短信
	 * @param sendReq
	 * @throws BaseServiceException 
	 */
	void batchSendSms(BatchSendSmsReq sendReq) throws BaseServiceException; 
	
	
	
	
	
}
