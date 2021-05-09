package wooteco.subway.line.model;

public class Line {

    private Long id;
    private String name;
    private String color;

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public boolean isSameName(Line newLine) {
        return this.name.equals(newLine.name);
    }

    public boolean isSameId(Long id) {
        return this.id == id;
    }

    public boolean isSameColor(Line newLine) {
        return this.color.equals(newLine.color);
    }
}