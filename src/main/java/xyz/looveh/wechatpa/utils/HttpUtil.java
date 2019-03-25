package xyz.looveh.wechatpa.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xyz.looveh.wechatpa.entity.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Administrator
 * @Date 2019/3/22
 * @desc Jsoup爬虫，爬取笔趣阁小说
 */
public class HttpUtil {
    /**
     * 笔趣阁地址
     */
    private static final String BQG_URL = "https://www.xbiquge6.com/";
    /**
     * 根据书名搜索地址
     */
    private static final String GET_BY_BOOKNAME_URL = "https://www.xbiquge6.com/search" +
            ".php?keyword=";


    public static void main(String[] args) {
        /*try {
            Document doc = get("武炼巅峰");
            Map<String, String> chapter = getChapter(doc, 1);
            System.out.println(chapter);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        List<User> list = new ArrayList<>();
        User u1 = new User();
        u1.setId(1);
        u1.setEname("aaa");
        u1.setName("中文名1");

        User u2 = new User();
        u2.setId(2);
        u2.setEname("bbb");
        u2.setName("中文名2");

        User u3 = new User();
        u3.setId(3);
        u3.setEname("ccc");
        u3.setName("中文名3");

        List<User> list1 = new ArrayList<>();
        System.out.println("之前：" + list1);

        list.forEach(a -> {
            User user = new User();
            user.setId(a.getId());
            user.setName(a.getName());
            user.setEname(a.getEname());
            list1.add(user);
        });

        System.out.println("之后：" + list1);
    }

    /**
     * 根据书名查找书籍
     *
     * @param bookName
     * @return
     * @throws IOException
     */
    public static Document get(String bookName) throws IOException {
        Document doc;
        bookName = URLEncoder.encode(bookName, "UTF-8");
        doc = Jsoup.connect(GET_BY_BOOKNAME_URL + bookName).get();
        Elements elementsByClass = doc.getElementsByClass("result-game-item");
        String attr = elementsByClass.get(0).getElementsByTag("a").get(0).attr("href");
        doc = Jsoup.connect(attr).get();
        return doc;
    }

    /**
     * 查看章节
     * 1007300:武炼巅峰第一章，每次加1以此类推
     *
     * @param chapter 章节
     * @return
     */
    public static Map<String, String> getChapter(Document doc, Integer chapter) throws IOException {
        Elements list = doc.getElementById("list").getElementsByTag("dd");
        //获取第一章的路径
        String href = list.get(0).child(0).attr("href");
        href = href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf("."));
        //chapter=1》》》》href=1007300
        Integer chapter1 = Integer.valueOf(href);
        int i = (chapter1 + chapter) - 1;
        doc = Jsoup.connect(doc.baseUri() + i + ".html").get();
        Map<String, String> map = new HashMap<>(1);
        //章节名称
        String chapterTitle = doc.getElementsByClass("bookName").get(0).child(0).text();
        String content = doc.getElementById("content").text();
        map.put(chapterTitle, content);

        return map;
    }
}
