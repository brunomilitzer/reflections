package section5.lesson1.api;

public class ClothingProduct extends Product {
    private Size size;
    private String color;

    // Getters
    public Size getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    // Setters
    public void setSize(Size size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
