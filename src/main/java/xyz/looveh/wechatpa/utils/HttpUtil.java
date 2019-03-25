package xyz.looveh.wechatpa.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
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
        } catch (Exception e){
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
