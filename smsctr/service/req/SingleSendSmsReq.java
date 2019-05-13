package com.sdhoo.pdloan.smsctr.service.req;

import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * 单条发送短信请求
 * @author SD_LJB(LiuJianbin)
 * @data 2018-11-07 17:16:00
 *
 */
public class SingleSendSmsReq {
    
    

    /**
     * 交易号 
     */
    @NotNull(message="交易号不能为空")
    private String tranCode ;
    

    /**
     * 发送电话号码
     */
    @NotNull(message="电话号码不能为空")
    private String phone ;
    
    /**
     * 模板KEY
     */
    @NotNull(message="指定模板不能为空")
    private String tpltKey ;
    
    /**
     * 模板参数 
     */
    private Map<String,Object> paramsMap ;
    
    
    /**
     * 用户类型
     */
    private Integer initUtype ;
    
    /**
     * 用户ID
     */
    private Long initUid ;
    
    /**
     * 用户名称
     */
    private String initUname ;

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTpltKey() {
        return tpltKey;
    }

    public void setTpltKey(String tpltKey) {
        this.tpltKey = tpltKey;
    }

    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, Object> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public Integer getInitUtype() {
        return initUtype;
    }

    public void setInitUtype(Integer initUtype) {
        this.initUtype = initUtype;
    }

    public Long getInitUid() {
        return initUid;
    }

    public void setInitUid(Long initUid) {
        this.initUid = initUid;
    }

    public String getInitUname() {
        return initUname;
    }

    public void setInitUname(String initUname) {
        this.initUname = initUname;
    }

    
}
