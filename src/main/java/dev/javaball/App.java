package dev.javaball;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import dev.javaball.Ball;

public class App extends Application {

    //Which ball is selected
    static Ball ball_selected;
    static Circle circle_selected;
    static Circle circle_select = new Circle(44, Color.LIME);;

    //TextField table, where x is ball and y is field
    static Double[][] ballFields = new Double[4][4];

    //Textfields
    static TextField tf_xPos, tf_yPos, tf_xvel, tf_yvel;

    //Collections
    static ArrayList<Ball> balls = new ArrayList<Ball>();
    static ArrayList<Circle> ballCircles = new ArrayList<Circle>();
    static ArrayList<Circle> trailCircles = new ArrayList<Circle>(); 

    /**
     * Draw balls as circles
     * 
     */
    public static void drawBallCircles() {
        for (int i = 0; i < ballCircles.toArray().length; i++) {
            ballCircles.get(i).setCenterX(balls.get(i).getxPos());
            ballCircles.get(i).setCenterY(balls.get(i).getyPos());
        }
    }

    /**
     * Draw selection ring on ball selected
     * 
     */
    public static void drawSelectionRing() {
        circle_select.setCenterX(ball_selected.getxPos());
        circle_select.setCenterY(ball_selected.getyPos());
    }

    /**
     * Set ballField table values to the values of balls
     * 
     */
    public static void setBallFields() {
        for (int i = 0; i < balls.toArray().length; i++) {
            ballFields[i][0] = balls.get(i).getxPos();
            ballFields[i][1] = balls.get(i).getyPos();
            ballFields[i][2] = balls.get(i).getxVelocity();
            ballFields[i][3] = balls.get(i).getyVelocity();
        }
    }

    /**
     * Set ball variables from BallFields table
     * 
     */
    public static void setBallsFromBallFields() {
        for (int i = 0; i < balls.toArray().length; i++) {
            balls.get(i).setxPos(ballFields[i][0]);
            balls.get(i).setyPos(ballFields[i][1]);
            balls.get(i).setxVelocity(ballFields[i][2]);
            balls.get(i).setyVelocity(ballFields[i][3]);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("javaball");

        //Window width and height
        final double WIDTH = 800;
        final double HEIGHT = 800;

        //Ball0
        Ball ball0 = new Ball(50, 200, 10, -20);
        ball0.setColor(Color.RED);
        balls.add(ball0);
        Circle circle0 = ball0.toCircle();
        ballCircles.add(circle0);

        //Ball1
        Ball ball1 = new Ball(250, 300, -10, -3);
        ball1.setColor(Color.BLUE);
        ball1.setName("BLUE");
        balls.add(ball1);
        Circle circle1 = ball1.toCircle();
        ballCircles.add(circle1);

        //Ball2
        Ball ball2 = new Ball(450, 500, -20, 2);
        ball2.setColor(Color.CYAN);
        ball2.setName("CYAN");
        balls.add(ball2);
        Circle circle2 = ball2.toCircle();
        ballCircles.add(circle2);

        //Ball3
        Ball ball3 = new Ball(650, 600, -10, -4);
        ball3.setColor(Color.YELLOW);
        ball3.setName("YELLOW");
        balls.add(ball3);
        Circle circle3 = ball3.toCircle();
        ballCircles.add(circle3);

        //First ball selected
        ball_selected = ball0;

        //First circle selected
        circle_selected = circle0;

        drawSelectionRing();

        //Text
        Text txt_ballname = new Text("Currently selected: "+ball_selected.getName());

        Text txt_xPos = new Text("X Position: ");
        Text txt_yPos = new Text("Y Position: ");
        Text txt_xvel = new Text("X velocity: ");
        Text txt_yvel = new Text("Y velocity: ");

        //Checkbox
        CheckBox cb_trail = new CheckBox();
        cb_trail.setText("Draw trail");

        setBallFields();

        //Visible textfields
        tf_xPos = new TextField(Double.toString(ball_selected.getxPos()));
        tf_yPos = new TextField(Double.toString(ball_selected.getyPos()));
        tf_xvel = new TextField(Double.toString(ball_selected.getxVelocity()));
        tf_yvel = new TextField(Double.toString(ball_selected.getyVelocity()));

        //Button
        Button btn_start = new Button("Start simulation");
        Button btn_set = new Button("Set");
        Button btn_save = new Button("Save setup");
        Button btn_load = new Button("Load setup");

        //Gridpane gui
        GridPane gp_right = new GridPane();
        gp_right.setHgap(4);
        gp_right.setVgap(10);
        gp_right.add(txt_ballname, 1, 0);
        gp_right.add(tf_xPos, 1, 2);
        gp_right.add(txt_xPos, 0, 2);
        gp_right.add(tf_yPos, 1, 3);
        gp_right.add(txt_yPos, 0, 3);
        gp_right.add(tf_xvel, 1, 4);
        gp_right.add(txt_xvel, 0, 4);
        gp_right.add(tf_yvel, 1, 5);
        gp_right.add(txt_yvel, 0, 5);
        gp_right.add(btn_set, 1, 6);

        GridPane gp_left = new GridPane();
        gp_left.setVgap(10);
        gp_left.add(btn_start, 0, 0);
        gp_left.add(btn_save, 0, 1);
        gp_left.add(btn_load, 0, 2);
        gp_left.add(cb_trail, 0, 3);

        //Create 100x100 grid
        GridPane grid = new GridPane();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rect = new Rectangle(0, 0, 100, 100);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.LIGHTGRAY);
                rect.setStrokeWidth(1);
                grid.add(rect, i, j);
            }
        }

        //BorderPane
        BorderPane bpane = new BorderPane();
        bpane.getChildren().addAll(grid, circle_select, circle0, circle1, circle2, circle3);
        bpane.setRight(gp_right);
        bpane.setLeft(gp_left);

        Scene scene = new Scene(bpane, WIDTH, HEIGHT);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.02), (ActionEvent e) ->
        {
            for (Ball b : balls) {

                //Start balls
                b.start();

                //Check Collision with window
                b.checkCollisionFrame(WIDTH, HEIGHT);

                //Check collision with balls
                for (Ball b2 : balls) {
                    if (b != b2) {
                        b.checkCollisionBall(b2);
                    }
                }

                //Draw ball trails
                if (cb_trail.isSelected()) {
                    Circle c = new Circle(b.getxPos(),b.getyPos(), 1);
                    trailCircles.add(c);
                    bpane.getChildren().add(6, c);
                }
            }

            drawSelectionRing();
            drawBallCircles();

            //TextField update
            tf_xPos.setText(Double.toString(Math.round(ball_selected.getxPos()))); 
            tf_yPos.setText(Double.toString(Math.round(ball_selected.getyPos()))); 
            tf_xvel.setText(Double.toString(Math.round(ball_selected.getxVelocity()))); 
            tf_yvel.setText(Double.toString(Math.round(ball_selected.getyVelocity()))); 

            //Make fields uneditable
            tf_xPos.setEditable(false);
            tf_yPos.setEditable(false);
            tf_xvel.setEditable(false);
            tf_yvel.setEditable(false);

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        //Event handling
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.PRIMARY)
                {
                    //Change ball selected
                    for (int i = 0; i < ballCircles.toArray().length; i++) {
                        if (ballCircles.get(i).contains(new Point2D(e.getX(), e.getY()))) {
                            //Update selected ball
                            ball_selected = balls.get(i);
                            circle_selected = ballCircles.get(i);
                            txt_ballname.setText("Currently selected: "+balls.get(i).getName());

                            drawSelectionRing();

                            //Set TextField text from BallFields table
                            tf_xPos.setText(Double.toString(ballFields[i][0]));
                            tf_yPos.setText(Double.toString(ballFields[i][1]));
                            tf_xvel.setText(Double.toString(ballFields[i][2]));
                            tf_yvel.setText(Double.toString(ballFields[i][3]));
                        }
                    }

                    //If outside circle radius add velocity
                    if (ball_selected.getLength(e.getX(), e.getY()) > ball_selected.getRadius()) {
                        ball_selected.setVelocityDir(20, ball_selected.getDir(e.getX(), e.getY()));
                    }
                }
            }
        });

        //Start the simulation
        btn_start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                //Set TextField text from BallFields table
                for (int i = 0; i < balls.toArray().length; i++) {
                    tf_xPos.setText(Double.toString(ballFields[i][0]));
                    tf_yPos.setText(Double.toString(ballFields[i][1]));
                    tf_xvel.setText(Double.toString(ballFields[i][2]));
                    tf_yvel.setText(Double.toString(ballFields[i][3]));
                }

                setBallsFromBallFields();

                //Remove trailcircles
                bpane.getChildren().removeAll(trailCircles);

                if (timeline.getStatus() == Status.STOPPED) {

                    //Start timeline
                    timeline.play();
                    btn_start.setText("Stop simulation");
                }
                else 
                {
                    if (timeline.getStatus() == Status.RUNNING) {

                        //Update selected ball
                        ball_selected = ball0;
                        circle_selected = circle0;
                        txt_ballname.setText("Currently selected: "+ball_selected.getName());

                        setBallsFromBallFields();

                        drawSelectionRing();
                        drawBallCircles();

                        //Make fields editable
                        tf_xPos.setEditable(true);
                        tf_yPos.setEditable(true);
                        tf_xvel.setEditable(true);
                        tf_yvel.setEditable(true);

                        //Give textfields the values of ball selected
                        tf_xPos.setText(Double.toString(ball_selected.getxPos()));
                        tf_yPos.setText(Double.toString(ball_selected.getyPos()));
                        tf_xvel.setText(Double.toString(ball_selected.getxVelocity()));
                        tf_yvel.setText(Double.toString(ball_selected.getyVelocity()));

                        //Stop timeline
                        timeline.stop();
                        btn_start.setText("Start simulation");
                    }
                }
            }
        });

        //Set ball with new properties and save them to a file
        btn_set.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                Double x = Double.parseDouble(tf_xPos.getText());
                Double y = Double.parseDouble(tf_yPos.getText());
                Double xv = Double.parseDouble(tf_xvel.getText());
                Double yv = Double.parseDouble(tf_yvel.getText());

                //Set variables to selected ball
                ball_selected.setxPos(x);
                ball_selected.setyPos(y);
                ball_selected.setxVelocity(xv);
                ball_selected.setyVelocity(yv);
                circle_selected.setCenterX(x);
                circle_selected.setCenterY(y);
                circle_select.setCenterX(x);
                circle_select.setCenterY(y);

                setBallFields();
            }
        });

        //Write ball field table to file
        btn_save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {

                    FileOutputStream f = new FileOutputStream(new File("fields.txt"));
                    ObjectOutputStream o = new ObjectOutputStream(f);
                    o.writeObject(ballFields);
                    o.close();
                    f.close();
                    System.out.println("Setup saved! ");
                    
                } catch (FileNotFoundException fe) {
                    System.out.println("File not found");
                } catch (IOException ie) {
                    System.out.println("Error initializing stream");
                }
            }
        });

        //Read ball field table from file
        btn_load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {

                    FileInputStream fi = new FileInputStream(new File("fields.txt"));
                    ObjectInputStream oi = new ObjectInputStream(fi);
                    ballFields = (Double[][]) oi.readObject();
                    oi.close();
                    fi.close();

                    setBallsFromBallFields();

                    drawSelectionRing();
                    drawBallCircles();

                    //Remove trailcircles
                    bpane.getChildren().removeAll(trailCircles);

                    timeline.stop();
                    btn_start.setText("Start simulation");

                    System.out.println("Setup loaded! ");

                } catch (FileNotFoundException fe) {
                    System.out.println("File not found");
                } catch (IOException ie) {
                    System.out.println("Error initializing stream");
                } catch (ClassNotFoundException ce) {
                    ce.printStackTrace();
                }
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}