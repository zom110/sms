package com.sdhoo.pdloan.smsctr.service;

import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.pdloan.bcrud.model.DtSmsChnlCfgInf;
import com.sdhoo.pdloan.bcrud.model.DtSmsTpltInf;

import java.util.List;
import java.util.Map;

/**
 * 短信渠道服务
 * @author SD_LJB(LiuJianbin)
 * @data 2018-10-18 19:43:03
 *
 */
public interface SmsChnlService {

	
	
	/**
	 * 根据模板批量发送短信
	 * @param cfgInf 配置信息
	 * @param tranNo 交易号,
	 * @param phoneList 电话号码列表
	 * @param tpltInf 模板信息 
	 * @param params 参数信息
	 * @param initUtype 用户类型
	 * @param initUid 用户id
	 * @param initUname 用户名称
	 * @throws BaseServiceException 
	 */
	void batchSendSms(DtSmsChnlCfgInf cfgInf , String tranNo , List<String> phoneList, DtSmsTpltInf tpltInf, Map<String,Object> params, Integer initUtype, Long initUid, String initUname  ) throws BaseServiceException; 
	
	/**
	 * 单条发送短信
	 * @param cfgInf 配置信息
	 * @param tranNo 交易号,
	 * @param phone 电话号码 
	 * @param tpltInf 模板信息
	 * @param params 参数信息
	 * @throws BaseServiceException 
	 */
	void sendSms(DtSmsChnlCfgInf cfgInf , String tranNo , String phone, DtSmsTpltInf tpltInf, Map<String,Object> params  ) throws BaseServiceException;
	
	
	
	
	
	
	
	
}
