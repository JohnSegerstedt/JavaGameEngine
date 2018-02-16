package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GUIRenderer;
import guis.GUITexture;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main(String[] args){

        DisplayManager.createDisplay();

        Loader loader = new Loader();
        MasterRenderer masterRenderer = new MasterRenderer(loader);
        GUIRenderer guiRenderer = new GUIRenderer(loader);
        Random rand = new Random();

        List<Entity> entities = new ArrayList<>();
        List<Entity> treeWall = new ArrayList<>();
        List<GUITexture> guis = new ArrayList<>();




        //*************      PLAYERS    *****************

        ModelData playerModelData = OBJFileLoader.loadOBJModel("blob_man_joined");
        RawModel playerRawModel = loader.loadToVAO(playerModelData.getVertices(), playerModelData.getTextureCoords(),
                playerModelData.getNormals(), playerModelData.getIndices());
        TexturedModel playerTexturedModel = new TexturedModel(playerRawModel, new ModelTexture(loader.loadTexture("white")));
        ModelTexture playerModelTexture = playerTexturedModel.getTexture();
        playerModelTexture.setShineDamper(10);
        playerModelTexture.setReflectivity(1);
        Player player = new Player(playerTexturedModel, new Vector3f(32, 0, 80), 0, 140, 0, 0.2f);
        Camera camera = new Camera(player);

        //***********************************************


        GUITexture guiTexture = new GUITexture(loader.loadTexture("testGUI"), new Vector2f(0.75f, 0.75f), new Vector2f(0.15f, 0.15f));
        guis.add(guiTexture);


        //************* TERRAIN TEXTURE *****************

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("terrainGrassLight"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrainClay"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrainFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrainPath"));
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap1"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);

        //************************************************

        Light light = new Light(new Vector3f(0, 0, 0), new Vector3f(1,1,1));
        Terrain terrain = new Terrain(500, 0, 0, loader, texturePack, blendMap, "heightMap2");


        ModelData treeModelData = OBJFileLoader.loadOBJModel("tree");
        RawModel treeRawModel = loader.loadToVAO(treeModelData.getVertices(), treeModelData.getTextureCoords(), 
                treeModelData.getNormals(), treeModelData.getIndices());
        TexturedModel treeStaticModel = new TexturedModel(treeRawModel, new ModelTexture(loader.loadTexture("tree")));
        ModelTexture treeTexture = treeStaticModel.getTexture();
            treeTexture.setShineDamper(99);
            treeTexture.setReflectivity(0);
        for (int i = 0; i < 5000; i++) {
            float x = rand.nextFloat() * (Terrain.getSize());
            float z = rand.nextFloat() * (Terrain.getSize());
            float y = terrain.getHeightOfTerrain(x, z);
            if (!collision(x, z, entities)) {
                entities.add(new Entity(treeStaticModel, new Vector3f(x, y, z), 0, 0, 0, 1.5f));
            } else {
                i--;
            }
        }

        for (int i = 0; i < Terrain.getSize()/2; i++) {
            float z = rand.nextFloat() * (5);
            treeWall.add(new Entity(treeStaticModel, new Vector3f(i*2, 0, z), 0, 0, 0, 3f));
            treeWall.add(new Entity(treeStaticModel, new Vector3f(i*2, 0, Terrain.getSize()-z), 0, 0, 0, 3f));
        }
        for (int i = 0; i < Terrain.getSize()/2; i++) {
            float x = rand.nextFloat() * (5);
            treeWall.add(new Entity(treeStaticModel, new Vector3f(x, 0, i*2), 0, 0, 0, 3f));
            treeWall.add(new Entity(treeStaticModel, new Vector3f(Terrain.getSize()-x, 0, i*2), 0, 0, 0, 3f));
        }



        ModelData dragonModelData = OBJFileLoader.loadOBJModel("dragon");
        RawModel dragonRawModel = loader.loadToVAO(dragonModelData.getVertices(), dragonModelData.getTextureCoords(),
                dragonModelData.getNormals(), dragonModelData.getIndices());
        TexturedModel dragonStaticModel = new TexturedModel(dragonRawModel, new ModelTexture(loader.loadTexture("white")));
        ModelTexture dragonTexture = dragonStaticModel.getTexture();
            dragonTexture.setShineDamper(10);
            dragonTexture.setReflectivity(1);
        Entity dragon = new Entity(dragonStaticModel, new Vector3f(50,0,20),0,0,0,1);


        ModelData fernModelData = OBJFileLoader.loadOBJModel("fern");
        RawModel fernRawModel = loader.loadToVAO(fernModelData.getVertices(), fernModelData.getTextureCoords(),
                fernModelData.getNormals(), fernModelData.getIndices());
        TexturedModel fernStaticModel = new TexturedModel(fernRawModel, new ModelTexture(loader.loadTexture("fern")));
        ModelTexture fernTexture = fernStaticModel.getTexture();
        fernTexture.setNumberOfRows(2);
        fernTexture.setHasTransparency(true);
        fernTexture.setHasFakeLighting(true);
        fernTexture.setShineDamper(5);
        fernTexture.setReflectivity(0.2f);
        for(int i=0; i<1000; i++){
            float x = rand.nextFloat()*(Terrain.getSize());
            float z= rand.nextFloat()*(Terrain.getSize());
            float y = terrain.getHeightOfTerrain(x, z);
            float roty = rand.nextFloat()*(360);
            if (!collision(x, z, entities)) {
                entities.add(new Entity(fernStaticModel, new Vector3f(x, y, z), rand.nextInt(4),0, roty, 0, 0.2f));
            } else {
                i--;
            }
        }


        ModelData grassModelData = OBJFileLoader.loadOBJModel("grass");
        RawModel grassRawModel = loader.loadToVAO(grassModelData.getVertices(), grassModelData.getTextureCoords(),
                grassModelData.getNormals(), grassModelData.getIndices());
        TexturedModel grassStaticModel = new TexturedModel(grassRawModel, new ModelTexture(loader.loadTexture("grass")));
        ModelTexture grassTexture = grassStaticModel.getTexture();
        grassTexture.setHasTransparency(true);
        grassTexture.setShineDamper(10);
        grassTexture.setReflectivity(1);
        for(int i=0; i<10000; i++){
            float x = rand.nextFloat()*(Terrain.getSize()-15)+5;
            float z= rand.nextFloat()*(Terrain.getSize()-15)+5;
            float y = terrain.getHeightOfTerrain(x, z);
            float roty = rand.nextFloat()*(360);
            entities.add(new Entity(grassStaticModel, new Vector3f(x, y, z), 0, roty, 0, 0.3f));
        }






        while(!Display.isCloseRequested()){
            camera.move();
            player.move(terrain);
            masterRenderer.processEntity(player);
            for(Entity entity:entities){
                masterRenderer.processEntity(entity);}
            for(Entity tree:treeWall){
                masterRenderer.processEntity(tree);}
            masterRenderer.processEntity(dragon);
            masterRenderer.processTerrain(terrain);
            masterRenderer.render(light, camera);
            guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }
        masterRenderer.cleanUp();
        guiRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    private static boolean collision(float x, float z, List<Entity> entities) {
        float accuracy = 2;
        for (Entity entity : entities) {
            if (entity.getPosition().x > x - accuracy && entity.getPosition().x < x + accuracy) {
                if (entity.getPosition().z > z - accuracy && entity.getPosition().z < z + accuracy) {
                    return true;
                }
            }
        }
        return false;
    }

}
