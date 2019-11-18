package ru.osipov.deploy.utils;

import org.slf4j.Logger;

import java.util.List;
import static org.slf4j.LoggerFactory.getLogger;
public class Paginator {
    private static final Logger logger = getLogger(Paginator.class);
    public Paginator(){ }

    public static <T extends List<R>,R> List<R> getResult(T list,int pages,int size, int np){
        int ls = list.size();
        int s = list.size();
        if(s == 0)
            return list;
        if(pages > s){
            logger.info("Pages are more than result pages: '{}' actual size: '{}'.\n\t >> Set pages are equal to sizeof result.",pages,s);
            pages = list.size();//or generate error
        }
        else if(pages <= 0){
            logger.info("Pages are less than 0: '{}'.\n\t  Set pages are equal to 1",pages);
            pages = 1;
        }
        if(np > pages){
            logger.info("Number cannot be greater than numberOf pages: '{}' but actual '{}'.\n\t >> Set number is equal to pages.",pages,np);
            np = pages;
        }
        if(np <= 0){
            logger.info("Number of the page is less than 0: '{}'.\n\t",">> Set number is equal to 1");
            np = 1;
        }
        int startIdx = (np - 1) * size;
        if(size > s){
            size = s / pages + (s % pages) + 1;
            startIdx = (np - 1) * size;
        }
        while(startIdx >= s){
            size = size - 1;
            startIdx = (np - 1) * size;
        }
        logger.info("Real size of the page: '{}'",size);
        int endIdx = np * size;
        if(endIdx > s)
            endIdx = s;
        return list.subList(startIdx,endIdx);
    }
}