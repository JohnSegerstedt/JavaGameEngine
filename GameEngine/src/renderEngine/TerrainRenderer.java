package renderEngine;

import models.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

import java.util.List;

public class TerrainRenderer {

    private TerrainShader terrainShader;


    public  TerrainRenderer(TerrainShader terrainShader, Matrix4f projectionMatrix){
        this.terrainShader = terrainShader;
        terrainShader.start();
        terrainShader.loadProjectionMatrix(projectionMatrix);
        terrainShader.connectTextureUnits();
        terrainShader.stop();
    }


    public void render(List<Terrain> terrainList){
        for(Terrain terrain : terrainList){
            prepareTerrainModel(terrain);
            loadTerrainModelMatrix(terrain);
                GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTerrainModel();
        }
    }

    private void prepareTerrainModel(Terrain terrain){
        RawModel rawModel = terrain.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        bindTextures(terrain);
        terrainShader.loadShineVariables(1, 0);

    }

    private void bindTextures(Terrain terrain){
        TerrainTexturePack texturePack = terrain.getTerrainTexturePack();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBackgroundTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getRTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getGTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturePack.getBTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());
    }

    private void unbindTerrainModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadTerrainModelMatrix(Terrain terrain){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
        terrainShader.loadTransformationMatrix(transformationMatrix);
    }
}
