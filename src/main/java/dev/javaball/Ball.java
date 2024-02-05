package dev.javaball;

import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Ball {
    private String name;
    private double xPos, yPos, xVelocity, yVelocity, yCheck, gravity; //Position
    private Color color;
    private int radius, time;
    private double bounce, friction; //Energy loss

    /**
     * Constructor of the Ball class
     *
     * @param xPos x position
     * @param yPos y position
     * @param xVelocity velocity on x axis
     * @param yVelocity velocity on y axis
     *
     */
    public Ball(double xPos, double yPos, double xVelocity, double yVelocity) {
        this.name = "RED";
        this.xPos = xPos;
        this.yPos = yPos;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.yCheck = yVelocity;
        this.color = Color.RED;
        this.radius = 40;
        this.bounce = -0.5;
        this.friction = 0.8;
        this.gravity = 0.05;
        this.time = 1;
    }

    /**
     * Constructor of the Ball class copying another ball
     *
     * @param ball ball to be copied
     *
     */
    public Ball(Ball ball) {
        this.name = ball.name;
        this.xPos = ball.xPos;
        this.yPos = ball.yPos;
        this.xVelocity = ball.xVelocity;
        this.yVelocity = ball.yVelocity;
        this.yCheck = ball.yCheck;
        this.color = ball.color;
        this.radius = ball.radius;
        this.bounce = ball.bounce;
        this.friction = ball.friction;
        this.gravity = ball.gravity;
        this.time = ball.time;
    }

    //Field getters

    public String getName() {
        return name;
    }

    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public double getxVelocity() {
        return xVelocity;
    }

    public double getyVelocity() {
        return yVelocity;
    }

    public Color getColor() {
        return color;
    }

    public int getRadius() {
        return radius;
    }

    public double getGravity() {
        return gravity;
    }

    //Field Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public void setyVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void setxVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    /**
     * Ball into javafx circle
     * @return Circle
     *
     */
    public Circle toCircle() {
        Circle circle = new Circle(radius, color);
        circle.setCenterX(xPos); circle.setCenterY(yPos);
        return circle;
    }

    /**
     * Check for collision with a frame and lose speed if hit
     *
     */
    public void checkCollisionFrame(final double WIDTH, final double HEIGHT) {
        if (xPos < radius) {
            xVelocity = xVelocity*bounce;
            xPos = radius;
            yVelocity *= friction;
        } else if (xPos + radius > WIDTH) {
            xVelocity = xVelocity*bounce;
            xPos = WIDTH - radius;
            yVelocity *= friction;
        }

        if (yPos < radius) {
            yVelocity = yVelocity*bounce;
            xVelocity *= friction;
            yPos = radius;
        } else if (yPos + radius > HEIGHT) {
            yVelocity = yVelocity*bounce;
            xVelocity *= friction;
            yPos = HEIGHT - radius;
        }
    }

    /**
     * Check for collision with another ball
     *
     */
    public void checkCollisionBall(Ball ball) {
        if (getLength(ball.getxPos(), ball.getyPos()) < radius+ball.getRadius()) {
            setVelocityDir((getVelocity()*-bounce/2+0.2)+(ball.getVelocity()*-bounce/2+0.2),
                    getDir(ball.getxPos(), ball.getyPos())-180);
        }
    }

    /**
     * Start adding velocity and gravity to position
     *
     */
    public void start() {
        time++;
        xPos += xVelocity;
        yPos += yVelocity;
        //Gravity
        yVelocity += gravity*time;
        if (signChange() || isStop()) {
            time = 1;
        }
        yCheck = yVelocity;
    }

    /**
     * Add velocity to direction
     *
     * @param velocity to add
     * @param angle in degrees
     *
     */
    public void addVelocityDir(double velocity, double angle) {
        double angleRad = (Math.PI/180)*angle;
        xVelocity += velocity*Math.cos(angleRad);
        yVelocity += velocity*Math.sin(angleRad);
    }

    /**
     * Set velocity to direction
     *
     * @param velocity to set
     * @param angle in degrees
     *
     */
    public void setVelocityDir(double velocity, double angle) {
        double angleRad = (Math.PI/180)*angle;
        xVelocity = 0; yVelocity = 0;
        xVelocity += velocity*Math.cos(angleRad);
        yVelocity += velocity*Math.sin(angleRad);
    }

    /**
     * Set position in direction
     *
     * @param distance from center
     * @param angle in degrees
     *
     */
    public void setPosDir(double distance, double angle) {
        double angleRad = (Math.PI/180)*angle;
        xPos +=  distance*Math.cos(angleRad);
        yPos +=  distance*Math.sin(angleRad);
    }

    /**
     * Get angle of velocity
     * @return Double
     *
     */
    public double getVelocityDir() {
        return getDir(xPos+xVelocity, yPos+yVelocity);
    }

    /**
     * Get total velocity asserted on ball
     * @return Double
     *
     */
    public double getVelocity() {
        return Math.sqrt(Math.pow(xVelocity,2)+Math.pow(yVelocity, 2));
    }

    /**
     * Get length between ball and a point
     * @param x of point
     * @param y of point
     * @return Double
     *
     */
    public double getLength(double x,  double y) {
        return Math.sqrt(Math.pow(x-xPos, 2)+Math.pow(y-yPos, 2));
    }

    /**
     * Get angle between ball and a point
     * @param x of point
     * @param y of point
     * @return Double
     *
     */
    public double getDir(double x, double y) {
        double angle = (double) Math.toDegrees(Math.atan2(y-yPos, x-xPos));

        if(angle < 0) {
            angle += 360;
        }
        return angle;
    }

    /**
     * Check if yVelocity sign has changed
     * @return Boolean
     *
     */
    private Boolean signChange() {
        if (yCheck*yVelocity < 0) {
            return true;
        } else return false;
    }

    /**
     * Check if ball has zero velocity
     * @return Boolean
     *
     */
    private Boolean isStop() {
        if (Math.round(xVelocity) == 0 && Math.round(yVelocity) == 0){
            return true;
        } else return false;
    }
}

/*
Resources used:

https://en.wikipedia.org/wiki/Bouncing_ball

*/
