package myE4Package;

import java.io.IOException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.geometry.Bounds;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import myE4Package.Pipe;
import javafx.scene.shape.Rectangle;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.geometry.Bounds;

public class Pane extends Application {

	class InternalPipe {
		Pipe p;
		Line head;
		Line tail;
	}
	
	HashMap <String, Line> anchorLines = new HashMap<String, Line>();
	
	int pipeArraySize = 2;
	final int arrayIncrement = 2;
	InternalPipe[] pipeArray = new InternalPipe[pipeArraySize];
	
	int pipeIndex = 0;
	
	@Override
	public void start(Stage primaryStage) {
	GridPane root = null;
	try {
		root = (GridPane) FXMLLoader.load(getClass().getResource("test.fxml"));
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	final Scene scene = new Scene(root, 600, 600);
	primaryStage.setScene(scene);
	primaryStage.show();
	
	root.getChildren().forEach(child -> {
		if (child instanceof Pipe) {
			Pipe p = (Pipe) child;
			pipeArray[pipeIndex] = new InternalPipe();
			pipeArray[pipeIndex].p = p;
			
			String headId = "-pipe-" + p.getHeadColumn() + "-" + p.getHeadHPos() + "-" + p.getHeadRow() + "-" + p.getHeadVPos();
			Line head = anchorLines.get(headId);
			if (head == null) {
				head = new Line();
				head.setId(headId);
				GridPane.setColumnIndex(head, p.getHeadColumn());
				GridPane.setHalignment(head, p.getHeadHPos());
				GridPane.setRowIndex(head, p.getHeadRow());
				GridPane.setValignment(head, p.getHeadVPos());
				anchorLines.put(headId, head);
				head.setStrokeWidth(5);
				head.setStroke(Color.RED);
			} 
			pipeArray[pipeIndex].head = head;

			String tailId = "-pipe-" + p.getTailColumn() + "-" + p.getTailHPos() + "-" + p.getTailRow() + "-" + p.getTailVPos();
			Line tail = anchorLines.get(tailId);
			if (tail == null) {
				tail = new Line();
				tail.setId(tailId );
				GridPane.setColumnIndex(tail, p.getTailColumn());
				GridPane.setHalignment(tail, p.getTailHPos());
				GridPane.setRowIndex(tail, p.getTailRow());
				GridPane.setValignment(tail, p.getTailVPos());
				anchorLines.put(tailId, tail);
				tail.setStrokeWidth(5);
				tail.setStroke(Color.BLUE);
 			}
			pipeArray[pipeIndex].tail = tail;
			
			//handle array resize
			pipeIndex++;
			if (pipeIndex == pipeArraySize) {
				InternalPipe[] tempPipeArray = new InternalPipe[pipeArraySize + arrayIncrement];
				System.arraycopy(pipeArray, 0, tempPipeArray, 0, pipeArraySize);
				pipeArray = tempPipeArray;
				pipeArraySize = pipeArray.length;
			}
	}
	});
	Line lastLine = new Line();
	for (Map.Entry<String, Line> entry : anchorLines.entrySet()) {
		root.getChildren().add(entry.getValue());
		lastLine = entry.getValue();
		System.out.print(entry.getKey());
		System.out.println(" " + entry.getValue().localToScene(entry.getValue().getBoundsInLocal()));
	}
	lastLine.layoutXProperty().addListener(new ChangeListener<Number>() {
		@Override public void changed(ObservableValue<? extends Number> observableValue, Number oldLayoutX, Number newLayoutX) {
			processAnchors();
		}
	});
	
	
	root.widthProperty().addListener(new ChangeListener<Number>() {
	    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
	        System.out.println("Width: " + newSceneWidth);
	        processAnchors();
	    }
	});
	root.heightProperty().addListener(new ChangeListener<Number>() {
	    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
	        System.out.println("Height: " + newSceneHeight);
	        processAnchors();
	    }
	});
	
	}

	void processAnchors(){
		for (int i = 0; i < pipeIndex; i++) {
	        Bounds boundsInSceneTail = pipeArray[i].tail.localToScene(pipeArray[i].tail.getBoundsInLocal());
			Bounds boundsInSceneHead = pipeArray[i].head.localToScene(pipeArray[i].head.getBoundsInLocal());
			pipeArray[i].p.setWidth(boundsInSceneHead.getMinX() - boundsInSceneTail.getMinX());
			if (i == 0) {
				System.out.println(" boundsInSceneTail " + boundsInSceneTail);
				System.out.println(" boundsInSceneHead " + boundsInSceneHead);
			}

		}
}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
