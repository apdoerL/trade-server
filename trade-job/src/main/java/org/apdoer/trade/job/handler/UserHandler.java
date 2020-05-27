package org.apdoer.trade.job.handler;

import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.common.service.util.SecurityContextUtil;
import org.apdoer.trade.common.code.ExceptionCode;
import org.apdoer.trade.common.model.vo.OrderCloseReqVo;
import org.apdoer.trade.common.model.vo.OrderConditionCancelReqVo;
import org.apdoer.trade.common.model.vo.OrderConditionReqVo;
import org.apdoer.trade.common.model.vo.OrderOpenReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserHandler {

    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Value("${trade-server.debug}")
    public boolean debug;

    public ResultVo getOpenUser(OrderOpenReqVo reqVo) {
        if (debug) {
            if (null == reqVo.getUserId()) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_ERROR.getCode(),ExceptionCode.USER_ID_ERROR.getName());
            }
        } else {
            Integer userId = securityContextUtil.getCurrentUserId();
            if (null == userId) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.UNAUTHORIZED_USERS.getCode(),ExceptionCode.UNAUTHORIZED_USERS.getName());
            }
            reqVo.setUserId(userId);
        }
        return ResultVoBuildUtils.buildSuccessResultVo();
    }

    public ResultVo getCloseUser(OrderCloseReqVo reqVo) {
        if (debug) {
            if (null == reqVo.getUserId()) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_ERROR.getCode(),ExceptionCode.USER_ID_ERROR.getName());
            }
        } else {
            Integer userId = securityContextUtil.getCurrentUserId();
            if (null == userId) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.UNAUTHORIZED_USERS.getCode(),ExceptionCode.UNAUTHORIZED_USERS.getName());
            }
            reqVo.setUserId(userId);
        }
        return ResultVoBuildUtils.buildSuccessResultVo();
    }
    
    public ResultVo getUser(OrderConditionReqVo requestVo) {
        if (debug) {
            if (null == requestVo.getUserId()) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_ERROR.getCode(),ExceptionCode.USER_ID_ERROR.getName());
            }
        } else {
            Integer userId = securityContextUtil.getCurrentUserId();
            if (null == userId) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.UNAUTHORIZED_USERS.getCode(),ExceptionCode.UNAUTHORIZED_USERS.getName());
            }
            requestVo.setUserId(userId);
        }
        return ResultVoBuildUtils.buildSuccessResultVo();
    }
    
    public ResultVo getUser(OrderConditionCancelReqVo requestVo) {
        if (debug) {
            if (null == requestVo.getUserId()) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_ERROR.getCode(),ExceptionCode.USER_ID_ERROR.getName());
            }
        } else {
            Integer userId = securityContextUtil.getCurrentUserId();
            if (null == userId) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.UNAUTHORIZED_USERS.getCode(),ExceptionCode.UNAUTHORIZED_USERS.getName());
            }
            requestVo.setUserId(userId);
        }
        return ResultVoBuildUtils.buildSuccessResultVo();
    }
    
    public ResultVo getUser(Integer userId) {
        if (debug) {
            if (null == userId) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.USER_ID_ERROR.getCode(),ExceptionCode.USER_ID_ERROR.getName());
            }
        } else {
        	userId = securityContextUtil.getCurrentUserId();
            if (null == userId) {
                return ResultVoBuildUtils.buildResultVo(ExceptionCode.UNAUTHORIZED_USERS.getCode(),ExceptionCode.UNAUTHORIZED_USERS.getName());
            }
        }
        return ResultVoBuildUtils.buildSuccessResultVo(userId);
    }
}
