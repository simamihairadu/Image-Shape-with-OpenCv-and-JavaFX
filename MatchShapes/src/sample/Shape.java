package sample;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

public class Shape {
    public String name;
    public Mat image;
    public Double matchValue;

    Shape(String name,String filePath){
        matchValue = 0.0 ;
        image = new Mat();
        this.name = name;
        image = Imgcodecs.imread(filePath);
    }
    Shape(){}

    public void showImage(){
        HighGui.imshow(" ",image);
    }
}
