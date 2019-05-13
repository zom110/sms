package com.sdhoo.pdloan.smsctr.zhutongkeji.service;

public interface ZhutongkejiCltService {

    /**
     * 助通科技发短信
     *
     * @param chnlAcct  用户名
     * @param chnlPwd   密码
     * @param productid 产品id
     * @param phones    电话
     * @param smsCtt    内容+【签名】
     * @return
     */
    String sendSms(String chnlAcct, String chnlPwd, String productid, String phones, String smsCtt);

}
