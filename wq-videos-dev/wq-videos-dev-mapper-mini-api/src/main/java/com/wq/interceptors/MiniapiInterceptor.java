package com.wq.interceptors;

import com.wq.utils.JSONResult;
import com.wq.utils.JsonUtils;
import com.wq.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by qwu on 2018/8/1.
 */
public class MiniapiInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisOperator redis;
    public static final String USER_REDIS_SESSION="user-redis-session";

    /**
     * 拦截请求，在controller调用之前
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String userToken = request.getHeader("userToken");
        String userId=request.getHeader("userId");
        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(userToken)){
            String redisUserToken=redis.get(USER_REDIS_SESSION+":"+userId);
            if(StringUtils.isEmpty(redisUserToken)&&StringUtils.isBlank(redisUserToken)){
                System.out.println("请登录...");
                returnErrorResponse(response, new JSONResult().errorTokenMsg("请登录..."));
                return false;
            }else {
                if (!redisUserToken.equals(userToken)) {
                    System.out.println("账号被挤出...");
                    returnErrorResponse(response, new JSONResult().errorTokenMsg("账号被挤出..."));
                    return false;
                }
            }
        }else {
            System.out.println("请登录..");
            returnErrorResponse(response, new JSONResult().errorTokenMsg("请登录..."));
            return false;
        }
        return true;
    }

    /**
     * 请求controller 渲染页面之前
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求controller之后 页面渲染之后
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public void returnErrorResponse(HttpServletResponse response, JSONResult result)
            throws IOException, UnsupportedEncodingException {
        OutputStream out=null;
        try{
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } finally{
            if(out!=null){
                out.close();
            }
        }
    }

}
