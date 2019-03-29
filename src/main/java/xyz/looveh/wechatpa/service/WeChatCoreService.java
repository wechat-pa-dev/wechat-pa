package xyz.looveh.wechatpa.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信消息处理service
 */
public interface WeChatCoreService {

    /**
     * 处理微信发过来的消息
     * @param request
     * @param response
     * @return
     */
    String weChatMessageHandler(HttpServletRequest request, HttpServletResponse response);

}
