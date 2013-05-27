package com.jkp.mp3tag;

/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

 
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.jkp.excel.utils.ReadExcelWriteMp3;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
 
/**
*
* @author Seven
*/
public class JavaFX extends Application {
 
  File file;
 
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
      launch(args);
  }
 
  @SuppressWarnings("unchecked")
@Override
  public void start(Stage primaryStage) {
      primaryStage.setTitle("JKP Kirtan Tagging Software Version 20130523");
     
      final Label labelFile = new Label();
     
      final Label labelDesc = new Label();
      final Label labelOutputDesc = new Label();
      final Label labelStatus = new Label();
      final Label labelStatusMessage = new Label();
      labelStatus.setText("Status : Not started processing");
      
      
      final ProgressBar pb = new ProgressBar();
      pb.setMinHeight(20);
      pb.setMinWidth(300);
 
      labelDesc.setText("Welcome To Mp3Tagging Project. Select a excel file which has mp3 file path and mp3 file title and optional artist to write");
      final Button btn = new Button();
      btn.setText("Choose ExcelFile");
      final Button startBtn = new Button();
      startBtn.setText("Start Process");
      startBtn.setDisable(true);
		final ReadExcelWriteMp3 readExcelWriteMp3Task = new ReadExcelWriteMp3();
		readExcelWriteMp3Task.exceptionProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				labelStatus.setText("Status : Fail");
				labelStatusMessage.setText(readExcelWriteMp3Task.getException().getLocalizedMessage());
				//btn.setDisable(false);
			}
		});
		
				
		readExcelWriteMp3Task.stateProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				labelStatus.setText("Status : "+arg2);
				if(State.SUCCEEDED.equals(arg2)){
					labelStatusMessage.setText( "Review Results in Excel File : "+file.getPath().replace(".xls","_out.xls"));
					//btn.setDisable(false);
				}
			}
		});
		
		pb.progressProperty().bind(readExcelWriteMp3Task.progressProperty());
		final ExecutorService threadExecutor = Executors.newFixedThreadPool(1);
      btn.setOnAction(new EventHandler<ActionEvent>() {
 
          @Override
          public void handle(ActionEvent event) {
              FileChooser fileChooser = new FileChooser();
 
              //Set extension filter
              FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel 97 files (*.xls)", "*.xls");
              fileChooser.getExtensionFilters().add(extFilter);
             
              //Show open file dialog
              file = fileChooser.showOpenDialog(null);
             
              labelFile.setText(" Excel File Choosen : " + file.getPath());
              labelStatus.setText("Status : File Choosen");
              startBtn.setDisable(false);
              btn.setDisable(true);
              
          }
      });
     
      

      startBtn.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
        	  try{
        		  startBtn.setDisable(true);

      			labelStatus.setText("Status : Processing");
      			readExcelWriteMp3Task.setInputFile(file.getAbsolutePath());
        			threadExecutor.execute(readExcelWriteMp3Task);
      			
      			
/*    				labelStatus.setText("Status : Complete");
      			
*/      			}catch(Exception ex){
      				labelStatus.setText("Status : Fail");
      				labelStatusMessage.setText(ex.getLocalizedMessage());
      			}finally{
      				
/*                    
*/      			}
        	  
          }
      });
      
      VBox vBox = new VBox();
      vBox.getChildren().addAll(labelDesc, btn,startBtn,labelFile,labelOutputDesc,labelStatus,labelStatusMessage,pb);
     
      StackPane root = new StackPane();
      root.getChildren().add(vBox);
      primaryStage.setScene(new Scene(root, 600, 250));
      primaryStage.show();
      
  }
}