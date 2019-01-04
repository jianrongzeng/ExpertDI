package model;

/**
 * @description:
 * @author: Zeng Jianrong
 * @date: 2019/1/3
 */
public class Tutor {
    public String id;
    public String expertId;
    public String studentId;
    public String studentType;
    public String addDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentType() {
        return studentType;
    }

    public void setStudentType(String studentType) {
        this.studentType = studentType;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "id='" + id + '\'' +
                ", expertId='" + expertId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", studentType='" + studentType + '\'' +
                ", addDate='" + addDate + '\'' +
                '}';
    }
}
