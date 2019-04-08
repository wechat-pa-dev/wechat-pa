package xyz.looveh.wechatpa.controller;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author Administrator
 * @Date 2019/4/8
 * @Desc 上传文件
 */
@RestController
public class UploadController {

    private static final String ACCESS_KEY = "";
    private static final String SECRET_KEY = "";
    private static final String BUCKET = "";


    public String getUploadToken() {
        Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        String uploadToken = auth.uploadToken(BUCKET);

        return uploadToken;
    }

    /*public static void main(String[] args) throws QiniuException {
        Response upload = new UploadController().upload();
        System.out.println(upload.bodyString());
    }*/


    public Response upload(MultipartFile file) throws QiniuException {

        Configuration cg = new Configuration(Zone.zone2());

        UploadManager uploadManager = new UploadManager(cg);


        String localPath = "D:\\looveh\\persona_space\\looveh个人博客需求文档.docx";
        String key = file.getOriginalFilename();

        Response put = uploadManager.put(localPath, key, getUploadToken());

        return put;
    }
}
