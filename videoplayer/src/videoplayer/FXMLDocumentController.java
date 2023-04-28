package videoplayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {
    private String path;
    private MediaPlayer mediaPlayer;
    
    @FXML
    private MediaView mediaview;
    @FXML
    private Slider progressbar;
    @FXML
    private Slider volumeslider;
    
    //pausing the video   
    @FXML
    void pause(ActionEvent event) {
        mediaPlayer.pause();
    }
    //playing the video
    @FXML
    void play(ActionEvent event) {
        if(mediaPlayer.getStatus()==PLAYING){
            mediaPlayer.stop();
            mediaPlayer.play();
        }else
        mediaPlayer.play();  
    }
    
    //stopping video
    @FXML
    void stop(ActionEvent event) {

        mediaPlayer.stop();
    }
    
    //skipping 20 seconds when video is playing
    @FXML
    void skip20s(ActionEvent event) {
         mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-20)));
    }
    //adding 20 seconds when video is playing
    @FXML
    void add20s(ActionEvent event) {
      
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(20)));
    }
    //choosing the video the user need to play from own machine
    @FXML
    void chooseVid(ActionEvent event) {
        FileChooser filechooser=new FileChooser();
        FileChooser.ExtensionFilter filter=new FileChooser.ExtensionFilter("select a file(*.mp4)","*.mp4");
        filechooser.getExtensionFilters().add(filter);
        
        File file=filechooser.showOpenDialog(null);
        path=file.toURI().toString();
        
        if(path !=null){
            Media media=new Media(path);
            mediaPlayer= new MediaPlayer(media);
            mediaview.setMediaPlayer(mediaPlayer);
            
            DoubleProperty widthProp = mediaview.fitWidthProperty();
            DoubleProperty heightProp = mediaview.fitHeightProperty();
            
            widthProp.bind(Bindings.selectDouble(mediaview.sceneProperty(),"width"));
            heightProp.bind(Bindings.selectDouble(mediaview.sceneProperty(),"height"));
            
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>(){
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue){
                    
                    progressbar.setValue(newValue.toSeconds());
                    
                }
            });
            mediaPlayer.setOnReady(new Runnable(){
                public void run(){
                    Duration total = media.getDuration();
                    progressbar.setMax(total.toSeconds());
                    
                }
            });
            volumeslider.setValue(mediaPlayer.getVolume()*100);
            volumeslider.valueProperty().addListener(new InvalidationListener(){
               
                @Override
                public void invalidated(javafx.beans.Observable observable) {
                    mediaPlayer.setVolume(volumeslider.getValue()/100);
                }
            });            
            mediaPlayer.play();
        }
    }   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
