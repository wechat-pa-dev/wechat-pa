package xyz.looveh.wechatpa.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.looveh.wechatpa.resp.TextMessage;
import xyz.looveh.wechatpa.service.WeChatCoreService;
import xyz.looveh.wechatpa.utils.HttpUtil;
import xyz.looveh.wechatpa.utils.WechatMessageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    private static final String GET_BY_BOOKNAME_URL = "https://www.xbiquge6.com/search.php?keyword=";

    @Override
    public String weChatMessageHandler(HttpServletRequest request, HttpServletResponse response) {

        logger.info("开始处理微信发过来的消息>>>>>>>>>>>>>>>>>>>>");

        String respMessage = null;

        try {
            Map<String, String> map = WechatMessageUtil.parseXml(request);
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String createTime = map.get("CreateTime");
            String content = map.get("Content");
            String msgId = map.get("MsgId");
            String msgType = map.get("MsgType");

            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setContent(content);
            textMessage.setCreateTime(System.currentTimeMillis());
            textMessage.setMsgType(WechatMessageUtil.RESP_MESSAGE_TYPE_TEXT);

            String key = null;

            if (!StringUtils.isEmpty(content)) {
                for (String keyword : WechatMessageUtil.KEYWORDS) {
                    if (content.startsWith(keyword)) {
                        key = keyword;
                    }
                }
            }
            textMessage = doBiz(map, content, msgType, textMessage, key);
            respMessage = WechatMessageUtil.textMessageToXml(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }

    /**
     * 业务处理
     * @param map
     * @param content
     * @param msgType
     * @param textMessage
     * @param key
     * @throws UnsupportedEncodingException
     */
    private TextMessage doBiz(Map<String, String> map, String content, String msgType,
                       TextMessage textMessage, String key) throws UnsupportedEncodingException {
        //文本内容
        if (WechatMessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
            //没有输入关键词
            if (key == null) {
                textMessage.setContent(WechatMessageUtil.UNIDENTIFIABLE);
            }

            //点歌
            if (key.equals(WechatMessageUtil.KEYWORDS.get(0))) {
                getSong(content, textMessage);
            } else if (key.equals(WechatMessageUtil.KEYWORDS.get(1))) {
                //根据书名查找
                String result = HttpUtil.doGet(GET_BY_BOOKNAME_URL + content.substring(3));
                System.err.println(result);
            } else {

            }
        } else if (WechatMessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
            if (WechatMessageUtil.EVENT_TYPE_SUBSCRIBE.equals(map.get("Event"))) {
                textMessage.setContent(WechatMessageUtil.WELCOME);
                return textMessage;
            }
        } else {
            textMessage.setContent(WechatMessageUtil.UNIDENTIFIABLE);
        }
        return textMessage;
    }

    /**
     * 点歌接口
     *
     * @param content
     * @param textMessage
     * @throws UnsupportedEncodingException
     */
    private void getSong(String content, TextMessage textMessage) throws UnsupportedEncodingException {
        //根据歌名查询
        String result =
                HttpUtil.doGet("https://api.apiopen.top/searchMusic?name=" + URLEncoder.encode(content, "UTF-8"));
        System.out.println(result);
        StringBuffer sb = new StringBuffer();
        if (!StringUtils.isEmpty(result)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
            Integer code = jsonObject.get("code").getAsInt();
            if (code != null && code == 200) {
                JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                if (jsonArray != null) {
                    for (JsonElement jsonElement : jsonArray) {
                        JsonObject asJsonObject = jsonElement.getAsJsonObject();
                        sb.append("《").append("<a href='" + asJsonObject.get("link") +
                                "'>" + asJsonObject.get("title") + "</a>").append("》");
                        sb.append(asJsonObject.get("author") + "\n\n");
                        textMessage.setContent(sb.toString().replace("\"", ""));
                    }
                }
            }
        }
    }
}
