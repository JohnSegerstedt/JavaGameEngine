package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

public class EntityRenderer {




    private StaticShader staticShader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.staticShader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }


    public void render(Map<TexturedModel, List<Entity>> entities){
        for(TexturedModel texturedModel: entities.keySet()){
            prepareTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);
            for(Entity entity: batch){
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, texturedModel.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModel(TexturedModel texturedModel){
        RawModel rawModel = texturedModel.getRawModel();
        ModelTexture modelTexture = texturedModel.getTexture();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        staticShader.loadNumberOfRows(modelTexture.getNumberOfRows());
        if(modelTexture.getHasTransparency()){
            MasterRenderer.disableCulling();
        }

        staticShader.loadFakeLightingVariable(modelTexture.getHasFakeLighting());
        staticShader.loadShineVariables(modelTexture.getShineDamper(), modelTexture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
    }

    private void unbindTexturedModel(){
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        staticShader.loadTransformationMatrix(transformationMatrix);
        staticShader.loadTextureOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }




}
