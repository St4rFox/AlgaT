package algat.model;

import java.util.List;

public class Lesson {
    private String title;           //NB necessaria corrispondenza 1:1 con i campi del file Yaml
    private String fileName;
    private List<Question> questions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "title='" + title + '\'' +
                ", fileName='" + fileName + '\'' +
                ", questions=" + questions +
                '}';
    }
}
