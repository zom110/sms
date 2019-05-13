package com.sdhoo.pdloan.smsctr.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sdhoo.pdloan.bcrud.model.DtSmsChnlCfgInf;
import com.sdhoo.pdloan.bcrud.model.DtSmsTpltInf;
import com.sdhoo.pdloan.bcrud.service.DtSmsChnlCfgInfService;
import com.sdhoo.pdloan.bcrud.service.DtSmsTpltInfService;
import com.sdhoo.pdloan.smsctr.SmsctrConstants;
import com.sdhoo.pdloan.smsctr.service.SmsChnlService;
import com.sdhoo.pdloan.smsctr.service.SmsSendService;
import com.sdhoo.pdloan.smsctr.service.req.BatchSendSmsReq;
import com.sdhoo.pdloan.smsctr.service.req.SingleSendSmsReq;
import com.sdhoo.common.base.exception.BaseServiceException;
import com.sdhoo.common.base.service.BaseBytesCacheService;

/**
 * 短信发送服务实现类
 *
 * @author SD_LJB(LiuJianbin)
 * @data 2018-10-18 19:32:33
 */
@Service
public class SmsSendServiceImpl implements SmsSendService {

    private static final String CACHEKEY_SMS_TRAN_CODE_PRE = "sms_trancode:";

    private static final int CACHEKEY_SMS_TRAN_CODE_TIMESEC = 3600;

    Logger logger = LoggerFactory.getLogger(SmsSendServiceImpl.class);


    @Autowired
    private BaseBytesCacheService baseBytesCacheService;

    @Autowired
    private DtSmsTpltInfService smsTpltInfService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DtSmsChnlCfgInfService smsChnlCfgInfService;

    private Map<String, SmsChnlService> smsChnlServiceMap;

    @PostConstruct
    public void init() {
        smsChnlServiceMap = applicationContext.getBeansOfType(SmsChnlService.class);
    }

    private SmsChnlService getChnlServiceByCfgId(Integer chnlId) {
        String serviceName = SmsctrConstants.SMS_CHNL_SERVICE_NAMPRE + chnlId;
        SmsChnlService smsChnlService = smsChnlServiceMap.get(serviceName);

        if (smsChnlService == null) {
            throw new RuntimeException("指定渠道(" + chnlId + ")短信实现类不存在");
        }
        return smsChnlService;
    }

    private static SimpleDateFormat tradeCodeTimeFmt = new SimpleDateFormat("yyMMddHHmmssSSS");

    @Override
    public String genSmsTranCode() {
        String code = tradeCodeTimeFmt.format(new Date());
        String cacheKey = CACHEKEY_SMS_TRAN_CODE_PRE + code;
        baseBytesCacheService.putAndExpireByteArray(cacheKey, code.getBytes(), CACHEKEY_SMS_TRAN_CODE_TIMESEC);

        return code;
    }

    @Override
    public List<String> batchGenSmsTranCode(int genCnt) {
        List<String> rtList = new ArrayList<>(genCnt);
        for (int i = 0; i < genCnt; i++) {
            String genCode = genSmsTranCode();
            rtList.add(genCode);
        }
        return rtList;
    }

    /**
     * 方法淘汰,改用指定参数类的方法进行发送
     */
    @Deprecated
    @Override
    public void sendSms(String tranCode, String phone, String tpltKey, Map<String, Object> paramsMap) throws BaseServiceException {

        List<String> phoneList = new ArrayList<>(1);
        phoneList.add(phone);
        batchSendSms(tranCode, phoneList, tpltKey, paramsMap);
    }

    /**
     * 方法淘汰,改用指定参数类的方法进行发送
     */
    @Deprecated
    @Override
    public void batchSendSms(String tranCode, List<String> phoneList, String tpltKey, Map<String, Object> paramsMap) throws BaseServiceException {

        Map<String, Object> tpltQmap = new HashMap<String, Object>(2);
        tpltQmap.put("stiKey", tpltKey);
        DtSmsTpltInf tpltInf;
        List<DtSmsTpltInf> tpltList = smsTpltInfService.selectByCriteria(tpltQmap, 0, 2);
        if (tpltList.size() != 1) {
            throw new BaseServiceException("短信模板不存在");
        }
        tpltInf = tpltList.get(0);
        Long dftCfgId = tpltInf.getDftCfgId();

        DtSmsChnlCfgInf cfgInf = smsChnlCfgInfService.getByPrimKey(dftCfgId, null);
        Integer chnlId = cfgInf.getChnlId();
        SmsChnlService chnlService = getChnlServiceByCfgId(chnlId);
        chnlService.batchSendSms(cfgInf, tranCode, phoneList, tpltInf, paramsMap, null, null, null);
    }


    @Override
    public void sendSms(SingleSendSmsReq sendReq) throws BaseServiceException {
        String tranCode = sendReq.getTranCode();
        String tpltKey = sendReq.getTpltKey();
        Map<String, Object> paramsMap = sendReq.getParamsMap();
        String phone = sendReq.getPhone();
        Integer initUtype = sendReq.getInitUtype();
        Long initUid = sendReq.getInitUid();
        String initUname = sendReq.getInitUname();
        List<String> phoneList = new ArrayList<>();
        phoneList.add(phone);

        BatchSendSmsReq batchReq = new BatchSendSmsReq();

        batchReq.setTranCode(tranCode);
        batchReq.setTpltKey(tpltKey);
        batchReq.setParamsMap(paramsMap);
        batchReq.setPhoneList(phoneList);
        batchReq.setInitUtype(initUtype);
        batchReq.setInitUid(initUid);
        batchReq.setInitUname(initUname);

        batchSendSms(batchReq);

    }

    @Override
    public void batchSendSms(BatchSendSmsReq sendReq) throws BaseServiceException {

        String tranCode = sendReq.getTranCode();
        String tpltKey = sendReq.getTpltKey();
        Map<String, Object> paramsMap = sendReq.getParamsMap();
        List<String> phoneList = sendReq.getPhoneList();
        Integer initUtype = sendReq.getInitUtype();
        Long initUid = sendReq.getInitUid();
        String initUname = sendReq.getInitUname();

        Map<String, Object> tpltQmap = new HashMap<String, Object>(2);
        tpltQmap.put("stiKey", tpltKey);
        DtSmsTpltInf tpltInf;
        List<DtSmsTpltInf> tpltList = smsTpltInfService.selectByCriteria(tpltQmap, 0, 2);
        if (tpltList.size() != 1) {
            throw new BaseServiceException("短信模板不存在");
        }
        tpltInf = tpltList.get(0);
        Long dftCfgId = tpltInf.getDftCfgId();

        DtSmsChnlCfgInf cfgInf = smsChnlCfgInfService.getByPrimKey(dftCfgId, null);
        logger.info("cfgInf:" + cfgInf);
        Integer chnlId = cfgInf.getChnlId();
        SmsChnlService chnlService = getChnlServiceByCfgId(chnlId);
        chnlService.batchSendSms(cfgInf, tranCode, phoneList, tpltInf, paramsMap, initUtype, initUid, initUname);

    }

}
