package sample;
import java.util.ArrayList;
import java.util.List;

public class ShapeArray {
    public List<Shape> shapeArray;

    ShapeArray(){
        shapeArray = new ArrayList<>();
        shapeArray.add(new Shape("circle","E:/info/Procesarea Imaginilor/Shapes/circle.png"));
        shapeArray.add(new Shape("rectangle","E:/info/Procesarea Imaginilor/Shapes/rectangle.png"));
        //shapeArray.add(new Shape("romb","E:/info/Procesarea Imaginilor/Shapes/romb.jpg"));
        shapeArray.add(new Shape("square","E:/info/Procesarea Imaginilor/Shapes/square.png"));
        shapeArray.add(new Shape("star","E:/info/Procesarea Imaginilor/Shapes/star.png"));
        shapeArray.add(new Shape("triangle","E:/info/Procesarea Imaginilor/Shapes/triangle.png"));
        shapeArray.add(new Shape("bottle","E:/info/Procesarea Imaginilor/Shapes/bottle.png"));
    }

    public String findMinName(double min){
        for (Shape shape:shapeArray) {
            if(shape.matchValue == min){
                return shape.name;
            }
        }
        return " ";
    }
}
