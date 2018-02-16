package skybox;


import entities.Camera;
import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import renderEngine.Loader;

public class SkyboxRenderer {

    private static final float SIZE = 500f;

    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };


    //The names of the skybox image files in order: "right", "left", "top", "bottom", "back", "front"
    private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};

    private RawModel cubeModel;
    private int textureID;
    private SkyboxShader skyboxShader;


    public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix){
        cubeModel = loader.loadToVAO(VERTICES, 3);
        textureID = loader.loadCubeMap(TEXTURE_FILES);
        skyboxShader = new SkyboxShader();
        skyboxShader.start();
        skyboxShader.loadProjectionMatrix(projectionMatrix);
        skyboxShader.stop();
    }

    public void render(Camera camera){
        skyboxShader.start();
        skyboxShader.loadViewMatrix(camera);
        GL30.glBindVertexArray(cubeModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cubeModel.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        skyboxShader.stop();
    }

}
