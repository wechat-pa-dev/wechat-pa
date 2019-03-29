package xyz.looveh.wechatpa.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Administrator
 * @Date 2019/3/22
 * @desc Jsoup爬虫，爬取笔趣阁小说
 */
public class ReptileUtil {
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
/*
        String unicode = "\\u53cc\\u8282\\u671f\\u95f4\\u5feb\\u9012\\u9ad8\\u5cf0\\uff0c\\u4e3a" +
                "\\u907f\\u514d\\u8d27\\u7269\\u4e2d\\u9014\\u4e22\\u5931\\uff0c\\u6211\\u516c" +
                "\\u53f8\\u5c06\\u4e8e\\u31\\u30\\u6708\\u31\\u65e5\\u8d77\\u505c\\u6b62\\u66" +
                "\\u61\\u68\\u75\\u6f\n";

        String cn = "双节期间快递高峰，为避免货物中途丢失，我公司将于10月1日起停止发货";
        String s = cnToUnicode(cn);
        System.out.println(s);
        String s1 = unicodeToCn(s);
        System.out.println(s1);*/
        BigDecimal divide = new BigDecimal(0).divide(new BigDecimal(1000));
        System.out.println(divide.setScale(2, RoundingMode.HALF_DOWN));
    }

    /**
     * 中文转Unicode
     *
     * @param cn
     * @return
     */
    public static String cnToUnicode(String cn) {
        StringBuilder sb = new StringBuilder();
        char[] chars = cn.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            sb.append("\\u" + Integer.toString(chars[i], 16));
        }
        return sb.toString();
    }

    /**
     * Unicode转中文
     *
     * @param unicode
     * @return
     */
    public static String unicodeToCn(String unicode) {
        String result = "";
        String[] split = unicode.split("\\\\u");
        for (int i = 1; i < split.length; i++) {
            Integer integer = Integer.valueOf(split[i], 16);
            result += (char) integer.intValue();
        }
        return result;
    }

    /**
     * 根据书名查找书籍
     *
     * @param bookName
     * @return
     * @throws IOException
     */
    public static Document get(String bookName) throws IOException {
        Document doc = null;
        bookName = URLEncoder.encode(bookName, "UTF-8");
        try {
            doc = Jsoup.connect(GET_BY_BOOKNAME_URL + bookName).get();
            Elements elementsByClass = doc.getElementsByClass("result-game-item");
            String attr = elementsByClass.get(0).getElementsByTag("a").get(0).attr("href");
            doc = Jsoup.connect(attr).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Map<String, String> map = null;
        try {
            Elements list = doc.getElementById("list").getElementsByTag("dd");
            //获取第一章的路径
            String href = list.get(0).child(0).attr("href");
            href = href.substring(href.lastIndexOf("/") + 1, href.lastIndexOf("."));
            //chapter=1》》》》href=1007300
            Integer chapter1 = Integer.valueOf(href);
            int i = (chapter1 + chapter) - 1;
            doc = Jsoup.connect(doc.baseUri() + i + ".html").get();
            map = new HashMap<>(1);
            //章节名称
            String chapterTitle = doc.getElementsByClass("bookName").get(0).child(0).text();
            String content = doc.getElementById("content").text();
            map.put(chapterTitle, content);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}
