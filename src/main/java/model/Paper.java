package model;

/**
 * Created by Zeng Jianrong on 2018/12/25
 */
public class Paper {
    public String paper_id;
    public String papername_ch;
    public String papername_en;
    public String abs_ch;
    public String abs_en;
    public String keyword_ch;
    public String keyword_en;
    public String author_ch;
    public String auchor_en;
    public String author_unit;
    public String author_unit_en;
    public String mails;
    public String cite_num;
    public String journal_ch;
    public String juornal_en;
    public String tutor;
    public String type;
    public String url;
    public String juornal_time;
    public String column;
    public String class_num;
    public String publish_time;
    public String fund;
    public String yy;
    public String mm;
    public String dd;

    public String getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(String paper_id) {
        this.paper_id = paper_id;
    }

    public String getPapername_ch() {
        return papername_ch;
    }

    public void setPapername_ch(String papername_ch) {
        this.papername_ch = papername_ch;
    }

    public String getPapername_en() {
        return papername_en;
    }

    public void setPapername_en(String papername_en) {
        this.papername_en = papername_en;
    }

    public String getAbs_ch() {
        return abs_ch;
    }

    public void setAbs_ch(String abs_ch) {
        this.abs_ch = abs_ch;
    }

    public String getAbs_en() {
        return abs_en;
    }

    public void setAbs_en(String abs_en) {
        this.abs_en = abs_en;
    }

    public String getKeyword_ch() {
        return keyword_ch;
    }

    public void setKeyword_ch(String keyword_ch) {
        this.keyword_ch = keyword_ch;
    }

    public String getKeyword_en() {
        return keyword_en;
    }

    public void setKeyword_en(String keyword_en) {
        this.keyword_en = keyword_en;
    }

    public String getAuthor_ch() {
        return author_ch;
    }

    public void setAuthor_ch(String author_ch) {
        this.author_ch = author_ch;
    }

    public String getAuchor_en() {
        return auchor_en;
    }

    public void setAuchor_en(String auchor_en) {
        this.auchor_en = auchor_en;
    }

    public String getAuthor_unit() {
        return author_unit;
    }

    public void setAuthor_unit(String author_unit) {
        this.author_unit = author_unit;
    }

    public String getAuthor_unit_en() {
        return author_unit_en;
    }

    public void setAuthor_unit_en(String author_unit_en) {
        this.author_unit_en = author_unit_en;
    }

    public String getMails() {
        return mails;
    }

    public void setMails(String mails) {
        this.mails = mails;
    }

    public String getCite_num() {
        return cite_num;
    }

    public void setCite_num(String cite_num) {
        this.cite_num = cite_num;
    }

    public String getJournal_ch() {
        return journal_ch;
    }

    public void setJournal_ch(String journal_ch) {
        this.journal_ch = journal_ch;
    }

    public String getJuornal_en() {
        return juornal_en;
    }

    public void setJuornal_en(String juornal_en) {
        this.juornal_en = juornal_en;
    }

    public String getTutor() {
        return tutor;
    }

    public void setTutor(String tutor) {
        this.tutor = tutor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJuornal_time() {
        return juornal_time;
    }

    public void setJuornal_time(String juornal_time) {
        this.juornal_time = juornal_time;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getClass_num() {
        return class_num;
    }

    public void setClass_num(String class_num) {
        this.class_num = class_num;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }

    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    @Override
    public String toString() {
        return "Paper{" +
                "paper_id='" + paper_id + '\'' +
                ", papername_ch='" + papername_ch + '\'' +
                ", papername_en='" + papername_en + '\'' +
                ", abs_ch='" + abs_ch + '\'' +
                ", abs_en='" + abs_en + '\'' +
                ", keyword_ch='" + keyword_ch + '\'' +
                ", keyword_en='" + keyword_en + '\'' +
                ", author_ch='" + author_ch + '\'' +
                ", auchor_en='" + auchor_en + '\'' +
                ", author_unit='" + author_unit + '\'' +
                ", author_unit_en='" + author_unit_en + '\'' +
                ", mails='" + mails + '\'' +
                ", cite_num='" + cite_num + '\'' +
                ", journal_ch='" + journal_ch + '\'' +
                ", juornal_en='" + juornal_en + '\'' +
                ", tutor='" + tutor + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", juornal_time='" + juornal_time + '\'' +
                ", column='" + column + '\'' +
                ", class_num='" + class_num + '\'' +
                ", publish_time='" + publish_time + '\'' +
                ", fund='" + fund + '\'' +
                ", yy='" + yy + '\'' +
                ", mm='" + mm + '\'' +
                ", dd='" + dd + '\'' +
                '}';
    }
}
