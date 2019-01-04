package model;

public class Foundation {
    public String fund_id;
    public String fund_name;
    public String fund_type;
    public String apply_code;
    public String principal;
    public String principal_title;
    public String principal_unit;
    public String time_limit;
    public String fund_money;
    public String abs_ch;
    public String keyword_ch;
    public String abs_en;
    public String keyword_en;
    public String abs_summary;
    public String add_date;

    public String getFund_id() {
        return fund_id;
    }

    public void setFund_id(String fund_id) {
        this.fund_id = fund_id;
    }

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

    public String getFund_type() {
        return fund_type;
    }

    public void setFund_type(String fund_type) {
        this.fund_type = fund_type;
    }

    public String getApply_code() {
        return apply_code;
    }

    public void setApply_code(String apply_code) {
        this.apply_code = apply_code;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPrincipal_title() {
        return principal_title;
    }

    public void setPrincipal_title(String principal_title) {
        this.principal_title = principal_title;
    }

    public String getPrincipal_unit() {
        return principal_unit;
    }

    public void setPrincipal_unit(String principal_unit) {
        this.principal_unit = principal_unit;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public String getFund_money() {
        return fund_money;
    }

    public void setFund_money(String fund_money) {
        this.fund_money = fund_money;
    }

    public String getAbs_ch() {
        return abs_ch;
    }

    public void setAbs_ch(String abs_ch) {
        this.abs_ch = abs_ch;
    }

    public String getKeyword_ch() {
        return keyword_ch;
    }

    public void setKeyword_ch(String keyword_ch) {
        this.keyword_ch = keyword_ch;
    }

    public String getAbs_en() {
        return abs_en;
    }

    public void setAbs_en(String abs_en) {
        this.abs_en = abs_en;
    }

    public String getKeyword_en() {
        return keyword_en;
    }

    public void setKeyword_en(String keyword_en) {
        this.keyword_en = keyword_en;
    }

    public String getAbs_summary() {
        return abs_summary;
    }

    public void setAbs_summary(String abs_summary) {
        this.abs_summary = abs_summary;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    @Override
    public String toString() {
        return "Foundation{" +
                "fund_id='" + fund_id + '\'' +
                ", fund_name='" + fund_name + '\'' +
                ", fund_type='" + fund_type + '\'' +
                ", apply_code='" + apply_code + '\'' +
                ", principal='" + principal + '\'' +
                ", principal_title='" + principal_title + '\'' +
                ", principal_unit='" + principal_unit + '\'' +
                ", time_limit='" + time_limit + '\'' +
                ", fund_money='" + fund_money + '\'' +
                ", abs_ch='" + abs_ch + '\'' +
                ", keyword_ch='" + keyword_ch + '\'' +
                ", abs_en='" + abs_en + '\'' +
                ", keyword_en='" + keyword_en + '\'' +
                ", abs_summary='" + abs_summary + '\'' +
                ", add_date='" + add_date + '\'' +
                '}';
    }
}
