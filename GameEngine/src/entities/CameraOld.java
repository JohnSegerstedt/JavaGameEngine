package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

//Temporary
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class CameraOld {

    private Vector3f position = new Vector3f(60,0,50);
    private float pitch;
    private float yaw;
    private float speed = 0.03f;
    private float jumpTime = 0.5f * 144f;
    private float height = 1;

    private float currentSpeed = speed;
    private float currentJumpTime = jumpTime;


    public CameraOld(){
        position.y = height;

    }


    public void move() {


        if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            currentSpeed = currentSpeed * 1.5f;
        }

        handleYaw();
        handlePitch();
        handleDirection();
        handleMapEnd();


        if(currentSpeed != speed){
            currentSpeed = speed;
        }
    }

    private void handleYaw() {
        yaw =  - (Display.getWidth() - Mouse.getX() / 2);
    }

    private void handlePitch(){
        pitch =  (Display.getHeight() / 2) - Mouse.getY();

        if (pitch >= 90){
            pitch = 90;
        }
        else if (pitch <= -90){
            pitch = -90;
        }
    }

    private void handleDirection(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z += -(float)Math.cos(Math.toRadians(yaw)) * currentSpeed;
            position.x += (float)Math.sin(Math.toRadians(yaw)) * currentSpeed;
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z -= -(float)Math.cos(Math.toRadians(yaw)) * currentSpeed;
            position.x -= (float)Math.sin(Math.toRadians(yaw)) * currentSpeed;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.z += (float)Math.sin(Math.toRadians(yaw)) * currentSpeed;
            position.x += (float)Math.cos(Math.toRadians(yaw)) * currentSpeed;
        }
        else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.z -= (float)Math.sin(Math.toRadians(yaw)) * currentSpeed;
            position.x -= (float)Math.cos(Math.toRadians(yaw)) * currentSpeed;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            if (currentJumpTime > 0) {
                position.y += currentSpeed;
                currentJumpTime--;
            }
        } else {
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                position.y -= currentSpeed;
            }

            if (currentJumpTime < jumpTime) {
                position.y -= currentSpeed;
                currentJumpTime++;
            }

            if (position.y < height) {
                position.y = height;
            }
        }
    }

    private void handleMapEnd(){
        if(position.x > 95){
            position.x = 95;
        }
        if(position.x < 5){
            position.x = 5;
        }
        if(position.z > 95){
            position.z = 95;
        }
        if(position.z < 5){
            position.z = 5;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

}
