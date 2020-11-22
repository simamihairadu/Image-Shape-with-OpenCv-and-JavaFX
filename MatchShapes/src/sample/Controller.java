package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static org.opencv.core.Core.bitwise_not;

public class Controller {

    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }
    @FXML
    private Slider thresholdSlider;
    @FXML
    private ImageView mainImageView;
    @FXML
    private TextField filePathTextField;
    @FXML
    private Slider dilateSlider;
    @FXML
    private TextField shapeTextBox;

    Mat image = new Mat();
    Mat grey = new Mat();
    Mat binary = new Mat();
    Mat inverse = new Mat();

    @FXML
    public void onDragThreshold(MouseEvent event){
        Imgproc.cvtColor(image,grey,Imgproc.COLOR_BGR2GRAY);

        thresholdSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Imgproc.threshold(grey,binary,(int)thresholdSlider.getValue(),255,Imgproc.THRESH_BINARY);
                bitwise_not(binary,inverse);
                BufferedImage bufferedImage = (BufferedImage) HighGui.toBufferedImage(inverse);
                mainImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
                image = inverse;
            }
        });
    }

    @FXML
    public void onDragDilate()
    {
        dilateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                Mat temp = new Mat();
                Mat dilate = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size((int)dilateSlider.getValue(),(int)dilateSlider.getValue()));
                Imgproc.dilate(image,temp,dilate);
                BufferedImage bufferedImage = (BufferedImage) HighGui.toBufferedImage(temp);
                mainImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
                image = temp;
            }
        });
    }

    @FXML
    public void onClickFindShape(ActionEvent event){
        Mat hierarchy = new Mat();
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(inverse, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(image,contours,-1,new Scalar(255,0,0),2);

        ShapeArray array = new ShapeArray();
        double min = 5;

        for(int i = 0; i < array.shapeArray.size(); i++){
            Mat shapeHierarchy = new Mat();
            Mat shapeGrey = new Mat();
            Mat shapeBinary = new Mat();
            Mat shapeInverse = new Mat();
            Imgproc.cvtColor(array.shapeArray.get(i).image,shapeGrey,Imgproc.COLOR_BGR2GRAY);
            Imgproc.threshold(shapeGrey,shapeBinary,127,255,Imgproc.THRESH_BINARY);
            bitwise_not(shapeBinary,shapeInverse);
            ArrayList<MatOfPoint> shapeContours = new ArrayList<MatOfPoint>();
            Imgproc.findContours(shapeInverse,shapeContours,shapeHierarchy,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);

            double result = Imgproc.matchShapes(contours.get(0), shapeContours.get(0),1,0.0);
            System.out.println(result);
            array.shapeArray.get(i).matchValue = result;
            if(array.shapeArray.get(i).matchValue < min){
                min = array.shapeArray.get(i).matchValue;
            }
        }
        shapeTextBox.setText(array.findMinName(min));
        System.out.println(array.findMinName(min));
    }

    @FXML
    public void loadImage(ActionEvent event){
        image = Imgcodecs.imread(filePathTextField.getText());
        grey = new Mat();
        binary = new Mat();
        inverse = new Mat();
        BufferedImage bufferedImage = (BufferedImage) HighGui.toBufferedImage(image);
        mainImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
    }
}