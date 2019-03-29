package xyz.looveh.wechatpa.resp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author Administrator
 * @Date 2019/3/29
 * @Desc 图片消息
 */
@Data
@ToString
@EqualsAndHashCode
public class ImageMessage extends BaseMessage{

    private String PicUrl;

    private String mediaId;
}
