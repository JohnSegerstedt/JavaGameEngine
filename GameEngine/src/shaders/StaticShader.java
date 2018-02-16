package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColor;
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_hasFakeLighting;
    private int location_skyColor;
    private int location_textureOffset;
    private int location_numberOfRows;

    public StaticShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);
    }


    @Override
    protected void bindAttributes(){
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_hasFakeLighting = super.getUniformLocation("hasFakeLighting");
        location_skyColor = super.getUniformLocation("skyColor");
        location_textureOffset = super.getUniformLocation("textureOffset");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
    }


    public void loadNumberOfRows(int numberOfRows){
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    public void loadTextureOffset(float xOffset, float yOffset){
        super.load2DVector(location_textureOffset, new Vector2f(xOffset, yOffset));
    }

    public void loadSkyColor(float r, float g, float b){
        super.loadVector(location_skyColor, new Vector3f(r,g,b));
    }

    public void loadFakeLightingVariable(boolean hasFake){
        super.loadBoolean(location_hasFakeLighting, hasFake);
    }

    public void loadShineVariables(float damper, float reflectivity){
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadLight(Light light){
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColor, light.getColor());
    }

    public void loadTransformationMatrix(Matrix4f transformationMatrix){
        super.loadMatrix(location_transformationMatrix, transformationMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix){
        super.loadMatrix(location_projectionMatrix, projectionMatrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }
}
