package com.whiteblue.tools;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.whiteblue.model.News;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WhiteBlue on 15/2/28.
 */
public class NewsUtil {
    public static List<News> getNews(String url){
        List<News> list=new ArrayList<News>();
        try {
            URL feedSource = new URL(url);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed= input.build(new XmlReader(feedSource));
            List<SyndEntry> syndEntries = (List<SyndEntry>)feed.getEntries();
            int i=0;
            for (SyndEntry syndEntry : syndEntries) {
                if(i>3)
                    break;
                String entryTitle = syndEntry.getTitle();
                String entryLink = syndEntry.getLink();
                SyndContent entryDescription = syndEntry.getDescription();
                String content = entryDescription.getValue();
                list.add(new News(entryTitle,entryLink,content));
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}

