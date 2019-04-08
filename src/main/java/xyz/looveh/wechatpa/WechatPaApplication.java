package xyz.looveh.wechatpa;

import org.jsoup.nodes.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.looveh.wechatpa.utils.ReptileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
@RestController
public class WechatPaApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(WechatPaApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WechatPaApplication.class);
    }

    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView m = new ModelAndView();
        m.setViewName("index");
        return m;
    }

    @RequestMapping("seeBook")
    public Map<String,String> seeBook(String bookName, Integer chapter){
        Map<String, String> result = new HashMap<>(1);
        try {
            Document document = ReptileUtil.get(bookName);
            Map<String, String> chapter1 = ReptileUtil.getChapter(document, chapter);
            Set<Map.Entry<String, String>> entries = chapter1.entrySet();
            Iterator<Map.Entry<String, String>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                result.put("title", next.getKey());
                result.put("content", next.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
