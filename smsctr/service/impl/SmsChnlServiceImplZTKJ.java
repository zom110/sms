package com.sdhoo.pdloan.smsctr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sdhoo.common.SebaseConstants;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.util.StringUtils;
import com.sdhoo.pdloan.bcrud.model.DtSmsChnlCfgInf;
import com.sdhoo.pdloan.bcrud.model.DtSmsSendRcd;
import com.sdhoo.pdloan.bcrud.model.DtSmsTpltInf;
import com.sdhoo.pdloan.bcrud.service.DtSmsSendRcdService;
import com.sdhoo.pdloan.smsctr.SmsctrConstants;
import com.sdhoo.pdloan.smsctr.enums.SmsSendRcdStatusEnum;
import com.sdhoo.pdloan.smsctr.service.SmsChnlService;
import com.sdhoo.pdloan.smsctr.zhutongkeji.service.ZhutongkejiCltService;
import com.sdhoo.pdloan.tools.service.VelocityContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(value = (SmsctrConstants.SMS_CHNL_SERVICE_NAMPRE + SmsctrConstants.SMS_CHNL_ID_ZTKJ))
public class SmsChnlServiceImplZTKJ implements SmsChnlService {

    private static final Logger logger = LoggerFactory.getLogger(SmsChnlServiceImplCl253.class);

    private static final String SERVICE_NAME = "短信渠道服务(助通科技)";

    @Autowired
    private ZhutongkejiCltService zhutongkejiCltService;

    /**
     * 短信发送记录表
     */
    @Autowired
    private DtSmsSendRcdService smsSendRcdService;

    @Autowired
    private VelocityContentService velocityContentService;

    @Override
    public void batchSendSms(DtSmsChnlCfgInf cfgInf, String tranNo, List<String> phoneList, DtSmsTpltInf tpltInf, Map<String, Object> params, Integer initUtype, Long initUid, String initUname) throws BaseServiceException {

        if (cfgInf == null || tranNo == null || phoneList == null || phoneList.size() < 1 || tpltInf == null) {
            throw new BaseServiceException(SebaseConstants.ERROR_CODE_PARAMS_ILLEGAL, "入参不合法");
        }
        String logPre = SERVICE_NAME + "调用(" + System.currentTimeMillis() + ")-->";
        Long cfgId = cfgInf.getScciId(); // 配置ID
        Integer chnlId = cfgInf.getChnlId(); // 渠道ID
        Long stiId = tpltInf.getStiId(); // 配置ID
        String msgTplt = tpltInf.getMsgTplt(); // 模板
        String msgSign = tpltInf.getMsgSign(); // 签名信息
        String smsCtt = "";
        String genCtt = velocityContentService.generateVelocityContent(msgTplt, params);
        smsCtt = msgSign + genCtt;
        String paramJsn = "{}";
        if (params != null) {
            paramJsn = JSONObject.toJSONString(params);
        }
        String tgtPhoneJsnAry = JSONObject.toJSONString(phoneList);
        int smsLength = smsCtt.length();
        Integer useCnt = BigDecimal.valueOf(Double.valueOf("" + smsLength) / 70).setScale(0, BigDecimal.ROUND_UP).intValue();

        // 记录创建
        Date curTime = new Date();
        DtSmsSendRcd sendRcd = new DtSmsSendRcd();
        sendRcd.setTradeNo(tranNo);
        sendRcd.setCfgId(cfgId);
        sendRcd.setStiId(stiId);
        sendRcd.setChnlId(chnlId);
        sendRcd.setTgtPhoneJsnary(tgtPhoneJsnAry);
        sendRcd.setMsgCtt(smsCtt);
        sendRcd.setUseCnt(useCnt);
        sendRcd.setParamJsn(paramJsn);
        sendRcd.setStepStatus(SmsSendRcdStatusEnum.CREATE.getCode());
        sendRcd.setInitUtype(initUtype);
        sendRcd.setInitUid(initUid);
        sendRcd.setInitUname(initUname);
        sendRcd.setSsrCtime(curTime);
        sendRcd.setSsrMtime(curTime);
        // 数据入库
        smsSendRcdService.insertSelective(sendRcd);

        Long ssrId = sendRcd.getSsrId();

        SmsSendRcdStatusEnum rstStatusEnum = null;
        // 调用接口,
        StringBuffer phonesSbf = new StringBuffer();
        int idx = 0;
        for (String phone : phoneList) {
            if (idx > 0) {
                phonesSbf.append(",");
            }
            phonesSbf.append(phone);
            idx++;
        }
        String phones = phonesSbf.toString();
        String chnlAccount = cfgInf.getAppKey();
        String chnlPwd = cfgInf.getAppPwd();
        String productid = cfgInf.getAppOtherParamOne();//助通科技产品id
        String rspStr = zhutongkejiCltService.sendSms(chnlAccount, chnlPwd, productid, phones, smsCtt);
        String chnlTradeNo = null;
        String rstMsg = "";
        if (StringUtils.isNotEmpty(rspStr)) {
            String[] split = rspStr.split(",");
            if (split.length > 1 && split[0].equals("1")) {
                chnlTradeNo = split[1];
                rstStatusEnum = SmsSendRcdStatusEnum.FOR_CHECK;
            } else {
                rstMsg = rspStr;
                rstStatusEnum = SmsSendRcdStatusEnum.FAIL;
                logger.warn(logPre + "发送失败,响应报文:" + rspStr);
            }
        } else {
            rstStatusEnum = SmsSendRcdStatusEnum.FAIL;
            rstMsg = "响应报文无效";
            logger.warn(logPre + "发送失败,响应报文无效:" + rspStr);
        }
        // 变更短信状态,
        if (rstStatusEnum != null) {
            if (rstMsg.length() > 64) {
                rstMsg = rstMsg.substring(0, 63);
            }
            DtSmsSendRcd mdfRcd = new DtSmsSendRcd();
            mdfRcd.setSsrId(ssrId);
            mdfRcd.setChnlTradeNo(chnlTradeNo);
            mdfRcd.setStepStatus(rstStatusEnum.getCode());
            mdfRcd.setStepMsg(rstMsg);
            smsSendRcdService.updateByPrimKeySelective(mdfRcd);
        }
    }

    @Override
    public void sendSms(DtSmsChnlCfgInf cfgInf, String tranNo, String phone, DtSmsTpltInf tpltInf, Map<String, Object> params) throws BaseServiceException {
        List<String> phoneList = new ArrayList<>(1);
        phoneList.add(phone);
        this.batchSendSms(cfgInf, tranNo, phoneList, tpltInf, params, null, null, null);
    }
}
