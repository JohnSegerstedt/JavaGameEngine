package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Player extends Entity{

    private float pitch;
    private float yaw;
    protected float height = 1;
    private float speed;

    private static final float WALK_SPEED = 10;
    private static final float RUN_SPEED = 50;
    private static final float TURN_SPEED = 120;
    private static final float STRAFE_SPEED = WALK_SPEED * 0.5f;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 15;

    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;
    private float currentSideWaySpeed = 0;

    private boolean isInAir = false;
    private boolean turnMidAir = false;

    private static final int MOVE_FORWARDS = Keyboard.KEY_W;
    private static final int MOVE_BACKWARDS = Keyboard.KEY_S;
    private static final int TURN_LEFT = Keyboard.KEY_A;
    private static final int TURN_RIGHT = Keyboard.KEY_D;
    private static final int STRAFE_LEFT = Keyboard.KEY_Q;
    private static final int STRAFE_RIGHT = Keyboard.KEY_E;
    private static final int SPRINT = Keyboard.KEY_LSHIFT;
    //private static final int MOVE_CAMERA = ???
    //private static final int TURN_CHARACTER = ???
    //private static final int TURN_CHARACTER_AND_MOVE_FORWARDS = ???

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(Terrain terrain){
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getFrameTimeSeconds(), 0);
        float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float distanceSideways = currentSideWaySpeed * DisplayManager.getFrameTimeSeconds();
        float dx = (float)(distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float)(distance * Math.cos(Math.toRadians(super.getRotY())));
        dx += (float)(distanceSideways * Math.cos(Math.toRadians(super.getRotY())));
        dz += (float)(distanceSideways * -Math.sin(Math.toRadians(super.getRotY())));
        upwardSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();

        float resultSpeedVector = (float)Math.sqrt((dx*dx) + (dz*dz));

        /*
        if(resultSpeedVector > WALK_SPEED){
            dx = dx/resultSpeedVector;
            dz = dz/resultSpeedVector;
        }
        */

        super.increasePosition(dx, upwardSpeed * DisplayManager.getFrameTimeSeconds(), dz);

        float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
        if(super.getPosition().y<terrainHeight){
            upwardSpeed = 0;
            isInAir = false;
            super.getPosition().y = terrainHeight;
        }
    }

    private void jump(){
        if(!isInAir){
            this.upwardSpeed = JUMP_POWER;
            isInAir = true;
        }
    }


    public void checkInputs(){
        speed = WALK_SPEED;
        float sideWaySpeed = STRAFE_SPEED;
        if(Keyboard.isKeyDown(SPRINT)){
            speed = RUN_SPEED;}

        if (Keyboard.isKeyDown(MOVE_FORWARDS) || Mouse.isButtonDown(2)){
            this.currentSpeed = speed;}
        else if (Keyboard.isKeyDown(MOVE_BACKWARDS)){
            this.currentSpeed = -speed;}
        else{
            this.currentSpeed = 0;}

        if (Keyboard.isKeyDown(STRAFE_RIGHT)){
            this.currentSideWaySpeed = -sideWaySpeed;}
        else if (Keyboard.isKeyDown(STRAFE_LEFT)){
            this.currentSideWaySpeed = sideWaySpeed;}
        else{
            this.currentSideWaySpeed = 0;}

        if (Keyboard.isKeyDown(TURN_RIGHT)){
            this.currentTurnSpeed = -TURN_SPEED;}
        else if (Keyboard.isKeyDown(TURN_LEFT)) {
            this.currentTurnSpeed = TURN_SPEED;}
        else{
            this.currentTurnSpeed = 0;}


        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            jump();
        }
    }

}
