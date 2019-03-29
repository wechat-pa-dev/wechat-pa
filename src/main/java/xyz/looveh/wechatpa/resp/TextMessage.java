package xyz.looveh.wechatpa.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author Administrator
 * @Date 2019/3/29
 * @Desc 文本消息
 */
@Data
@ToString
@EqualsAndHashCode
public class TextMessage extends BaseMessage{

    private String Content;

}
