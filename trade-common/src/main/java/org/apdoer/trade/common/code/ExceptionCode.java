package org.apdoer.trade.common.code;

import java.util.*;

/**
 * 异常错误码枚举   0400-0499：服务响应 0500-0699：请求参数验证 0700-0899：业务验证
 */
public enum ExceptionCode {
    /**
     * 0400-0499：服务响应
     */
    /*成功*/
    SUCCESS(0, "success"),

    /*失败*/
    FIAL(101010400, "fail"),

    /*请求超时*/
    REQUEST_TIMEOUT(101010401, "request timeout"),

    /*未知异常*/
    UNKNOWN_EXCEPTION_CODE(101010402, "unknown_exception_code"),

    /*渠道服务发送失败*/
    CHANNEL_SERVICE_DELIVERY_FAILURE(101010403, "channel org.apdoer.common.service.service delivery failure"),

    /*无权限用户*/
    UNAUTHORIZED_USERS(101010404, "unauthorized users"),

    /**
     * 0700-0899：业务验证
     */

    /*query org.apdoer.common info org.apdoer.common.service.exception*/
    GET_COMMON_INFO_EXCEPTION_CODE(101010701, "query org.apdoer.common info org.apdoer.common.service.exception"),

    /*query all currency org.apdoer.common.service.exception*/
    GET_ALL_CURRENCY_EXCEPTION_CODE(101010702, "query all currency org.apdoer.common.service.exception"),

    /*query recharge currency org.apdoer.common.service.exception*/
    QUERY_RECHARGE_CURRENCY_EXCEPTION(101010703, "query recharge currency org.apdoer.common.service.exception"),

    /*query all contract org.apdoer.common.service.exception*/
    QUERY_ALL_CONTRACT_EXCEPTION(101010704, "query all contract org.apdoer.common.service.exception"),

    /*query quoted currency org.apdoer.common.service.exception*/
    QUERY_QUOTED_CURRENCY_EXCEPTION(101010705, "query quoted currency org.apdoer.common.service.exception"),

    /*query currency org.apdoer.common.service.exception*/
    QUERY_CURRENCY_EXCEPTION(101010706, "query currency org.apdoer.common.service.exception"),

    /*查询用户信息异常*/
    QUERY_USER_INFO_EXCEPTION_CODE(101010707, "query user info org.apdoer.common.service.exception org.apdoer.code"),

    /*更新用户信息异常*/
    UPDATE_USER_EXCEPTION_CODE(101010708, "update user org.apdoer.common.service.exception org.apdoer.code"),

    /*用户注册异常*/
    REGISTER_USER_EXCEPTION_CODE(101010709, "register user org.apdoer.common.service.exception org.apdoer.code"),

    /*更新用户密码信息异常*/
    UPDATE_USER_PASSWORD_EXCEPTION_CODE(101010710, "update user password org.apdoer.common.service.exception org.apdoer.code"),

    /*查询用户权限异常*/
    QUERY_USER_PERMISSION_LIST_EXCEPTION_CODE(101010711, "query user permission list org.apdoer.common.service.exception org.apdoer.code"),

    /*查询账号记录异常*/
    GET_ACCOUNT_RECORD_EXCEPTION_CODE(101010712, "get account record org.apdoer.common.service.exception org.apdoer.code"),

    /*查询用户的认证等级异常*/
    QUERY_USER_CERTIFICATIONGRADE_EXCEPTION_CODE(101010713, "query user certificationgrade org.apdoer.common.service.exception org.apdoer.code"),

    /*保存用户登陆记录异常*/
    SAVE_USER_LOGIN_RECORD_EXCEPTION(101010714, "save user login record org.apdoer.common.service.exception"),




    /*用户id异常*/
    USER_ID_ERROR(109000700, "USER_ID_ERROR"),
    /*平仓失败*/
    CLOSE_POSI_FAILED(109000802, "CLOSE_POSI_FAILED"),

    /*仓位不存在*/
    POSI_NOT_EXIST(109000812, "POSI_NOT_EXIST"),










    ;


    private int code;

    private String name;

    private static Map<Integer, String> map = new HashMap<Integer, String>();

    private static List<org.apdoer.common.service.code.ExceptionCode> list = new ArrayList<org.apdoer.common.service.code.ExceptionCode>();

    static {
        for (org.apdoer.common.service.code.ExceptionCode status : org.apdoer.common.service.code.ExceptionCode.values()) {
            map.put(status.getCode(), status.getName());
        }
        list.addAll(Arrays.asList(org.apdoer.common.service.code.ExceptionCode.values()));
    }

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

    private ExceptionCode(int codeNo, String codeName) {
        this.code = codeNo;
        this.name = codeName;
    }

    /**
     * 返回map类型形式
     *
     * @return
     */
    public static Map<Integer, String> getMap() {
        return map;
    }

    /**
     * 返回list类型形式
     *
     * @return
     */
    public static List<org.apdoer.common.service.code.ExceptionCode> getList() {
        return list;
    }

    /**
     * 根据code获取枚举类型
     *
     * @param codeNo
     * @return
     */
    public static org.apdoer.common.service.code.ExceptionCode getCategory(int codeNo) {
        for (org.apdoer.common.service.code.ExceptionCode status : list) {
            if (status.getCode() == codeNo) {
                return status;
            }
        }
        return null;
    }

    /**
     * 根据code获取枚举类型中文名称
     *
     * @param codeNo
     * @return
     */
    public static String getName(int codeNo) {
        for (org.apdoer.common.service.code.ExceptionCode status : list) {
            if (status.getCode() == codeNo) {
                return status.getName();
            }
        }
        return null;
    }
}
