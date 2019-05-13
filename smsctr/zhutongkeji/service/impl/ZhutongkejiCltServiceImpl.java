package com.sdhoo.pdloan.smsctr.zhutongkeji.service.impl;

import com.sdhoo.pdloan.smsctr.zhutongkeji.service.ZhutongkejiCltService;
import com.sdhoo.pdloan.smsctr.zhutongkeji.util.MD5Gen;
import com.sdhoo.pdloan.util.DateFormatUtil;
import com.sdhoo.pdloan.util.URLConnectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ZhutongkejiCltServiceImpl implements ZhutongkejiCltService {

    private static final String CLT_SERVICE_NAME = "助通科技短信接口客户端";
    private static final Logger logger = LoggerFactory.getLogger(ZhutongkejiCltServiceImpl.class);


    @Value("${sms.zhutongkeji.server_url}")
    private String serverUrl;

    @Override
    public String sendSms(String chnlAcct, String chnlPwd, String productid, String phones, String smsCtt) {
        String rspStr = null;
        String logPre = CLT_SERVICE_NAME + "(" + (System.currentTimeMillis()) + ")-->";
        try {
            String tkey = DateFormatUtil.formatByPattern(new Date(), "yyyyMMddHHmmss");
            Map<String, String> params = new HashMap<>();
            params.put("username", chnlAcct);
            params.put("password", MD5Gen.getMD5(MD5Gen.getMD5(chnlPwd) + tkey));
            params.put("tkey", tkey);
            params.put("mobile", phones);
            params.put("content", smsCtt);
            params.put("productid", productid);
            params.put("xh", "");
            String post = URLConnectionUtils.doRequest(serverUrl, "POST", params);
            if (StringUtils.isNotBlank(post)) {
                rspStr = post;
            } else {
                logger.error(logPre + "调用失败了,响应信息:" + post);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(rspStr + "调用助通科技发短信异常了..", e);
        }
        return rspStr;
    }


}
