package textures;

public class ModelTexture {

    private int textureID;
    private float shineDamper = 1;
    private float reflectivity = 0;

    private boolean hasTransparency = false;
    private boolean hasFakeLighting = false;

    private int numberOfRows = 1;




    public ModelTexture(int id){
        this.textureID = id;
    }


    public int getID(){
        return this.textureID;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public boolean getHasTransparency(){return hasTransparency;}

    public boolean getHasFakeLighting(){return hasFakeLighting;}

    public int getNumberOfRows() {return numberOfRows;}

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public void setHasTransparency(boolean hasTransparency){this.hasTransparency = hasTransparency;}

    public void setHasFakeLighting(boolean hasFakeLighting){this.hasFakeLighting = hasFakeLighting;}

    public void setNumberOfRows(int numberOfRows) {this.numberOfRows = numberOfRows;}


}
