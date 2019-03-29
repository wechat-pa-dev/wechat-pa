package xyz.looveh.wechatpa.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xyz.looveh.wechatpa.resp.TextMessage;
import xyz.looveh.wechatpa.service.WeChatCoreService;
import xyz.looveh.wechatpa.utils.WechatMessageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Author Administrator
 * @Date 2019/3/29
 */
@Service
public class WeChatCoreServiceImpl implements WeChatCoreService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatCoreServiceImpl.class);

    @Override
    public String weChatMessageHandler(HttpServletRequest request, HttpServletResponse response) {

        logger.info("开始处理微信发过来的消息>>>>>>>>>>>>>>>>>>>>");

        String respMessage = null;

        try {
            Map<String, String> map = WechatMessageUtil.parseXml(request);
            String fromUserName = map.get("FromUserName");
            System.out.println(fromUserName);
            String toUserName = map.get("ToUserName");
            System.out.println(toUserName);
            String createTime = map.get("CreateTime");
            System.out.println(createTime);
            String content = map.get("Content");
            System.out.println(content);
            String msgId = map.get("MsgId");
            System.out.println(msgId);
            String msgType = map.get("MsgType");
            System.out.println(msgType);

            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setContent(content);
            textMessage.setCreateTime(System.currentTimeMillis());
            textMessage.setMsgType(WechatMessageUtil.RESP_MESSAGE_TYPE_TEXT);

//            Long aLong = Long.valueOf(createTime) * 1000L;
//            String formatDate = new SimpleDateFormat().format(new Date(aLong));
//            textMessage.setCreateTime(formatDate);

            if(WechatMessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)){
                respMessage = WechatMessageUtil.textMessageToXml(textMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }
}
