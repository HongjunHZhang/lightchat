package com.zhj.entity.ret;

import com.zhj.entity.Journal;

/**
 * @author 789
 */
public class RetJournal {
    private int pageNum;
    private Journal journal;
    private String formatTime;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }
}
