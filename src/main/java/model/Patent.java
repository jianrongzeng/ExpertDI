package model;

/**
 * @description:
 * @author: Zeng Jianrong
 * @date: 2019/1/3
 */
public class Patent {
    public String patentId;
    public String expertName;
    public String patentName;
    public String unit;
    public String parter;
    public String addDate;

    public String getPatentId() {
        return patentId;
    }

    public void setPatentId(String patentId) {
        this.patentId = patentId;
    }

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    public String getPatentName() {
        return patentName;
    }

    public void setPatentName(String patentName) {
        this.patentName = patentName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getParter() {
        return parter;
    }

    public void setParter(String parter) {
        this.parter = parter;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    @Override
    public String toString() {
        return "Patent{" +
                "patentId='" + patentId + '\'' +
                ", expertName='" + expertName + '\'' +
                ", patentName='" + patentName + '\'' +
                ", unit='" + unit + '\'' +
                ", parter='" + parter + '\'' +
                ", addDate='" + addDate + '\'' +
                '}';
    }
}
