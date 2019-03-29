package xyz.looveh.wechatpa.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import xyz.looveh.wechatpa.cache.RedisCacheManager;
import xyz.looveh.wechatpa.constant.RedisKeyConstant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @Author Administrator
 * @Date 2019/3/29
 */
@Component
public class WechatSignUtil {

    private static final Logger logger = LoggerFactory.getLogger(WechatSignUtil.class);

    private static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    @Value("${wechat.token}")
    private String token;
    @Value("${wechat.appid}")
    private String appId;
    @Value("${wechat.appsecret}")
    private String secret;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisCacheManager redisCacheManager;

    /**
     * 验签
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        //字典排序
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);

        StringBuilder sb = new StringBuilder();
        for (String str : arr) {
            sb.append(str);
        }
        MessageDigest md;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(sb.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        logger.info("执行微信签名加密认证生成加密串：" + tmpStr);
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    /**
     * byte数组转字符串
     *
     * @param bytes
     * @return
     */
    public String byteToStr(byte[] bytes) {

        String strDigest = "";
        for (byte aByte : bytes) {
            strDigest += byteToHexStr(aByte);
        }

        return strDigest;
    }

    /**
     * byte转16进制字符串
     *
     * @param mByte
     * @return
     */
    public String byteToHexStr(byte mByte) {

        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E'
                , 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

    /**
     * 获取token，有效期2小时，存放在redis，超时重新获取
     * @return
     */
    public String getAccessToken(){
        String accessToken;
        accessToken = (String) redisCacheManager.get(RedisKeyConstant.ACCESS_TOKEN);
        logger.info("redis中accessToken>>>>>>>>>>>>" + accessToken);
        if(StringUtils.isEmpty(accessToken)) {
            String url = GET_ACCESS_TOKEN_URL.replace("APPID", appId).replace("APPSECRET", secret);
            System.out.println(">>>>>>url>>>>>> = " + url);

            accessToken = HttpUtil.post(url);
            System.out.println("请求获取access_token返回值：" + accessToken);
            redisCacheManager.set(RedisKeyConstant.ACCESS_TOKEN, accessToken,7200L);
        }
        return accessToken;
    }

}
