package terrains;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {

    private static float SIZE = 100;
    private static final float MAX_HEIGHT = 35;
    private static final float MAX_PIXEL_COLOR = 256*256*256;

    private float x;
    private float z;
    private RawModel rawModel;
    private TerrainTexturePack terrainTexturePack;
    private TerrainTexture blendMap;

    private float[][] vertexHeights;


    public Terrain(float size, int gridX, int gridZ, Loader loader, TerrainTexturePack terrainTexturePack, TerrainTexture blendMap, String heightMap){
        SIZE = size;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.rawModel = generateTerrain(loader, heightMap);
        this.terrainTexturePack = terrainTexturePack;
        this.blendMap = blendMap;
    }


    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TerrainTexturePack getTerrainTexturePack() {
        return terrainTexturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }

    public float getHeightOfTerrain(float worldX, float worldZ){
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / (float)(vertexHeights.length - 1);
        int gridX = (int)Math.floor(terrainX / gridSquareSize);
        int gridZ = (int)Math.floor(terrainZ / gridSquareSize);
        if(gridX >= vertexHeights.length - 1 || gridZ >= vertexHeights.length - 1 || gridX < 0 || gridZ < 0){
            return 0;
        }
        float xCoordinate = (terrainX % gridSquareSize)/gridSquareSize;
        float zCoordinate = (terrainZ % gridSquareSize)/gridSquareSize;

        if(xCoordinate <= (1-zCoordinate)){
            return Maths.barryCentric(new Vector3f(0, vertexHeights[gridX][gridZ], 0), new Vector3f(1,
                    vertexHeights[gridX + 1][gridZ], 0), new Vector3f(0,
                    vertexHeights[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));

        }else{
            return Maths.barryCentric(new Vector3f(1, vertexHeights[gridX + 1][gridZ], 0), new Vector3f(1,
                    vertexHeights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                    vertexHeights[gridX][gridZ + 1], 1), new Vector2f(xCoordinate, zCoordinate));
        }
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setRawModel(RawModel rawModel) {
        this.rawModel = rawModel;
    }

    private RawModel generateTerrain(Loader loader, String heightMap){

        BufferedImage image = null;
        int vertexCount = 0;

        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        }catch(IOException e){
            e.printStackTrace();
        }
        try {
            vertexCount = image.getHeight();
        }catch(NullPointerException e){
            e.printStackTrace();

        }
        vertexHeights = new float[vertexCount][vertexCount];
        int count = vertexCount * vertexCount;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(vertexCount-1)*(vertexCount-1)];
        int vertexPointer = 0;
        for(int i=0;i<vertexCount;i++){
            for(int j=0;j<vertexCount;j++){
                vertices[vertexPointer*3] = (float)j/((float)vertexCount - 1) * SIZE;
                float height = getHeight(j, i, image);
                vertexHeights[j][i] = height;
                vertices[vertexPointer*3+1] = height;
                vertices[vertexPointer*3+2] = (float)i/((float)vertexCount - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)vertexCount - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)vertexCount - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<vertexCount-1;gz++){
            for(int gx=0;gx<vertexCount-1;gx++){
                int topLeft = (gz*vertexCount)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*vertexCount)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }
        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image){
        float heightU = getHeight(x, z+1, image);
        float heightR = getHeight(x+1, z, image);
        float heightD = getHeight(x, z-1, image);
        float heightL = getHeight(x-1, z, image);
        Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD-heightU);
        normal.normalise();
        return normal;
    }

    private float getHeight(int x, int z, BufferedImage image){
        if(x<0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()){
            return 0;
        }
        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOR/2f;
        height /= MAX_PIXEL_COLOR/2f;
        height *= MAX_HEIGHT;
        return height;
    }

    public static float getSize(){
        return SIZE;
    }
}
