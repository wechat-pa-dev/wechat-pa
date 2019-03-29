package xyz.looveh.wechatpa.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author Administrator
 * @Date 2019/3/29
 */
@Data
@ToString
@EqualsAndHashCode
public class BaseMessage implements Serializable {

    private static final long serialVersionUID = 3242765769180679511L;

    private String ToUserName;

    private String FromUserName;

    private long CreateTime;

    private String MsgType;

    private String MsgId;
}
