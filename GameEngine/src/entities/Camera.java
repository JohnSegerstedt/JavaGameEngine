package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;


public class Camera{

    //TODO: Create Camrea settings, ex: sensitivity, invertation, turn player on W after Lmouse
    //TODO: Fix camrea clipping through ground
    //TODO: 1st + 3rd person

    private float distanceFromPlayer = 5;
    private float angleAroundPlayer = 0;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 20;
    private float yaw = 0;
    private float roll;

    private Player player;


    public Camera(Player player){
        this.player = player;
    }


    public void move(){
        calculateZoom();
        calculateAngleAroundPlayer();
        calculatePitch();
        calculateCameraPosition(calculateHorizontalDistance(), calculateVerticalDistance());
        this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
    }

    public Vector3f getPosition(){
        return position;
    }

    public float getPitch(){
        return pitch;
    }

    public float getYaw(){
        return yaw;
    }

    public float getRoll(){
        return roll;
    }


    private void calculateZoom() {
        float newZoom = -Mouse.getDWheel() * 0.01f;
        if (distanceFromPlayer + newZoom > 1) {
            distanceFromPlayer += newZoom;
        }
    }

    private void calculateAngleAroundPlayer(){
        if(Mouse.isButtonDown(0)){
            angleAroundPlayer -= Mouse.getDX() * 0.3f;
            pitch -= Mouse.getDY() * 0.1f;

        }


    }

    private void calculatePitch(){
        if (Mouse.isButtonDown(1) || Mouse.isButtonDown(2)) {
            angleAroundPlayer -= Mouse.getDX() * 0.3f;
            pitch -= Mouse.getDY() * 0.1f;
            player.setRotY(player.getRotY() + angleAroundPlayer);
            angleAroundPlayer = 0;
        }
    }

    private float calculateHorizontalDistance(){
        return (float)(distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float)(distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizontalDistance, float verticalDistance){
        float angle = player.getRotY() + angleAroundPlayer;
        float offsetX = (float)(horizontalDistance * Math.sin(Math.toRadians(angle)));
        float offsetZ = (float)(horizontalDistance * Math.cos(Math.toRadians(angle)));
        position.x = player.getPosition().x - offsetX;
        position.y = player.getPosition().y + verticalDistance + player.height;
        position.z = player.getPosition().z - offsetZ;

    }
}