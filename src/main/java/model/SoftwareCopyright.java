package model;

/**
 * @description:
 * @author: Zeng Jianrong
 * @date: 2019/1/3
 */
public class SoftwareCopyright {
    public String softcopyId;
    public String expertName;
    public String softcopyName;
    public String unit;
    public String parter;
    public String addDate;

    public String getSoftcopyId() {
        return softcopyId;
    }

    public void setSoftcopyId(String softcopyId) {
        this.softcopyId = softcopyId;
    }

    public String getExpertName() {
        return expertName;
    }

    public void setExpertName(String expertName) {
        this.expertName = expertName;
    }

    public String getSoftcopyName() {
        return softcopyName;
    }

    public void setSoftcopyName(String softcopyName) {
        this.softcopyName = softcopyName;
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
        return "SoftwareCopyright{" +
                "softcopyId='" + softcopyId + '\'' +
                ", expertName='" + expertName + '\'' +
                ", softcopyName='" + softcopyName + '\'' +
                ", unit='" + unit + '\'' +
                ", parter='" + parter + '\'' +
                ", addDate='" + addDate + '\'' +
                '}';
    }
}
