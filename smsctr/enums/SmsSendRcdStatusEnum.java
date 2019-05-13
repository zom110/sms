package com.sdhoo.pdloan.smsctr.enums;


/**
 * 短信记录状态
 * @author SD_LJB(LiuJianbin)
 * @data 2018-10-19 17:24:04
 *
 */
public enum SmsSendRcdStatusEnum {

	CREATE(0,"创建"),
	FOR_CHECK(3,"待核对"),
	SUCCESS(1,"发送成功"),
	FAIL(2,"发送失败"),
	;

	private int code ;
	private String name ;

	public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    SmsSendRcdStatusEnum(int code , String name ){
		this.code = code ;
		this.name = name;
	}


	/**
	 * 根据编码获取枚举
	 * @param code
	 * @return
	 */
	public static SmsSendRcdStatusEnum getByCode(int code){
		for(SmsSendRcdStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode()== (code) ){
				return tmpEnum ;
			}
		}
		return null ;
	}
	
	/**
	 * CODE是否符合
	 * @param code
	 * @return
	 */
	public static boolean isCodeValid(int code){
		for(SmsSendRcdStatusEnum tmpEnum : values() ){
			if(tmpEnum.getCode() == (code) ){
				return true ;
			}
		}
		return false ;
	}
	
}
