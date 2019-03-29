package xyz.looveh.wechatpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.looveh.wechatpa.service.WeChatCoreService;
import xyz.looveh.wechatpa.utils.WechatSignUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

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

    @Autowired
    WeChatCoreService weChatCoreService;

    /**
     * 验证微信服务器
     *
     * @param request
     * @return
     */
    /*@RequestMapping(value = "/access", method = RequestMethod.GET)
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
    }*/

    @RequestMapping(value = "/access")
    public String getWeChatMessage(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
        logger.info("--------------------开始处理微信发过来的消息--------------------");

        //设置请求响应消息编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String respXml = weChatCoreService.weChatMessageHandler(request, response);
        if(null != respXml){
            logger.info("<<<<<<<<<<<<<<<<处理微信消息成功\n" + respXml);
        }
        return respXml;
    }
}
