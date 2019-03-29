package xyz.looveh.wechatpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.looveh.wechatpa.utils.WechatSignUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Administrator
 * @Date 2019/3/29
 * @Desc 微信请求处理的核心类
 */
@RestController
@RequestMapping(value = "/wechat")
public class WeChatCoreController {

    private static final Logger logger = LoggerFactory.getLogger(WeChatCoreController.class);

    @Autowired
    WechatSignUtil wechatSignUtil;

    /**
     * 验证微信服务器
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/access", method = RequestMethod.GET)
    public String wechatInterface(HttpServletRequest request) {

        logger.info("--------------------验证微信服务信息开始--------------------");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        logger.info("signature={},timestamp={},nonce={},echostr={}", signature, timestamp, nonce,
                echostr);
        //验证是否是微信发送的消息
        if (wechatSignUtil.checkSignature(signature, timestamp, nonce)){
            logger.info("--------------------验证微信服务信息结束--------------------");
            return echostr;
        }
        logger.error("--------------------验证微信服务信息结束,请求非法--------------------");
        return "";
    }
}
