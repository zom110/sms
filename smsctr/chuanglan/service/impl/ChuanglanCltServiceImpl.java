package com.sdhoo.pdloan.smsctr.chuanglan.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.common.base.util.WebOptUtils;
import com.sdhoo.common.base.util.WebOptUtils.WebRequest;
import com.sdhoo.common.base.util.WebOptUtils.WebResp;
import com.sdhoo.pdloan.smsctr.chuanglan.service.ChuanglanCltService;

/**
 * 创蓝客户端服务
 * @author SD_LJB(LiuJianbin)
 * @data 2018-10-18 20:51:03
 *
 */
@Service
public class ChuanglanCltServiceImpl implements ChuanglanCltService {
    
    private static final String CLT_SERVICE_NAME="创蓝短信接口客户端";
	
	/**
	 * 日志.
	 */
	private static final Logger logger = LoggerFactory.getLogger(ChuanglanCltServiceImpl.class); 
	
	@Value(value="${sms.chuanglan.server_url}")
	private String serverUrl ; 

//	@Value(value="${sms.chuanglan.account}")
//	private String account ;
//
//	@Value(value="${sms.chuanglan.passwd}")
//	private String passwd ; 

	/**
	 * 
	 */
	private static SimpleDateFormat sdfmt = new SimpleDateFormat("yyyyMMddHHmm");

	@Override
	public String batchSendSms(String chnlAcct, String chnlPwd, String tradeNo, String phones, String smsCtt, String extend) {
		String logPre =  CLT_SERVICE_NAME + "(" + (System.currentTimeMillis()) + ")-->";
		String rspStr = null ;
		Map<String,Object> dataMap = new HashMap<>(16);
		Date curTime = new Date();
		String sendtime= sdfmt.format(curTime); 
		dataMap.put("uid", tradeNo);
		dataMap.put("account", chnlAcct);
		dataMap.put("password", chnlPwd);
		dataMap.put("msg", smsCtt);
		dataMap.put("phone", phones);
		dataMap.put("sendtime", sendtime);
		dataMap.put("report", true);
		if(StringUtils.isEmpty(extend)) {
		    extend = "000";
		}
		dataMap.put("extend", extend );
		String jsonStr = JSONObject.toJSONString(dataMap);

		WebRequest webRequest = new WebRequest();
		Map<String, String> headMap = new HashMap<>();
		headMap.put("Content-Type", "application/json");
		webRequest.setHeadMap(headMap );
		webRequest.setUrl(serverUrl);
		webRequest.setContentBytes(jsonStr.getBytes()); 
		try {
			WebResp resp = WebOptUtils.doPost(webRequest );
			if(resp.isSuccess()) {
			    rspStr = resp.getResponseStr();
			}else {
			    logger.error(logPre + "调用失败了,响应信息:" + resp.getResponseStr() );
			}
		} catch (IOException e) {
			logger.error(logPre+"调用异常了..",e);
		}
		return rspStr ;
	}

	
//	public static void main(String[] args) throws Exception {
//		
//		String serverUrl = "http://smssh1.253.com/msg/send/json";
//		String account = "N4666060";
//		String passwd = "lkMcn6EgiQab55";
//
//		String tradeNo="18101821230001"; 
//		String smsCtt = "【福建盛典】您的验证码是:9874"; 
//		String phones="15980261863"; 
//		Date curTime = new Date();
//		String sendtime= sdfmt.format(curTime); 
//
//		Map<String,Object> dataMap = new HashMap<>(16);
//
//		dataMap.put("uid", tradeNo);
//		dataMap.put("account", account);
//		dataMap.put("password", passwd);
//		dataMap.put("msg", smsCtt);
//		dataMap.put("phone", phones);
//		dataMap.put("sendtime", sendtime);
//		dataMap.put("report", true);
//		dataMap.put("extend", "123");
//
//		String jsonStr = JSONObject.toJSONString(dataMap);
//		
//		WebRequest webRequest = new WebRequest();
//		Map<String, String> headMap = new HashMap<>();
//		headMap.put("Content-Type", "application/json");
//		webRequest.setHeadMap(headMap );
//		webRequest.setUrl(serverUrl);
//		webRequest.setContentBytes(jsonStr.getBytes()); 
//		WebResp resp = WebOptUtils.doPost(webRequest ); 
//
//		String responseStr = resp.getResponseStr();
//		System.err.println(responseStr);
//		
//		// {"code":"0","msgId":"18101822473127723","time":"20181018224731","errorMsg":""} 
//
//	}

}
