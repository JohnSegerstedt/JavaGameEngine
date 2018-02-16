package textures;

import java.nio.ByteBuffer;

public class TextureData {

    private int width;
    private int height;
    private ByteBuffer byteBuffer;


    public TextureData(int width, int height, ByteBuffer byteBuffer) {
        this.width = width;
        this.height = height;
        this.byteBuffer = byteBuffer;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

}
