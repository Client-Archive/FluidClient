package com.fluid.client.util.render.font;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class TTFFontRenderer {

    private final boolean antiAlias;

    /**
     * The font to be drawn.
     */
    private final Font font;

    /**
     * If fractional metrics should be used in the font renderer.
     */
    private final boolean fractionalMetrics;

    /**
     * All the character data information (regular).
     */
    private CharacterData[] regularData;

    /**
     * The margin on each texture.
     */
    private static final int MARGIN = 4;

    public TTFFontRenderer(ExecutorService executorService, ConcurrentLinkedQueue<TextureData> textureQueue, Font font) {
        this(executorService, textureQueue, font, 256);
    }

    public TTFFontRenderer(ExecutorService executorService, ConcurrentLinkedQueue<TextureData> textureQueue, Font font, int characterCount) {
        this(executorService, textureQueue, font, characterCount, true);
    }

    public TTFFontRenderer(ExecutorService executorService, ConcurrentLinkedQueue<TextureData> textureQueue, Font font, int characterCount, boolean antiAlias) {
        super();
        this.font = font;
        this.fractionalMetrics = true;
        this.antiAlias = antiAlias;

        // Generates all the character textures.
        int[] regularTexturesIds = new int[characterCount];
        for (int i = 0; i < characterCount; i++) {
            regularTexturesIds[i] = GL11.glGenTextures();
        }

        executorService.execute(() -> this.regularData = setup(new CharacterData[characterCount], regularTexturesIds, textureQueue));
    }

    /**
     * Sets up the character data and textures.
     *
     * @param characterData The array of character data that should be filled.
     */
    private CharacterData[] setup(CharacterData[] characterData, int[] texturesIds, ConcurrentLinkedQueue<TextureData> textureQueue) {
        // Changes the type of the font to the given type.
        Font font = this.font.deriveFont(Font.PLAIN);

        // An image just to get font data.
        BufferedImage utilityImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        // The graphics of the utility image.
        Graphics2D utilityGraphics = (Graphics2D) utilityImage.getGraphics();

        // Sets the font of the utility image to the font.
        utilityGraphics.setFont(font);

        // The font metrics of the utility image.
        FontMetrics fontMetrics = utilityGraphics.getFontMetrics();

        // Iterates through all the characters in the character set of the font renderer.
        for (int index = 0; index < characterData.length; index++) {
            // The character at the current index.
            char character = (char) index;

            // The width and height of the character according to the font.
            Rectangle2D characterBounds = fontMetrics.getStringBounds(character + "", utilityGraphics);

            // The width of the character texture.
            double width = characterBounds.getWidth() + (2 * MARGIN);

            // The height of the character texture.
            double height = characterBounds.getHeight();

            // The image that the character will be rendered to.
            BufferedImage characterImage = new BufferedImage(ceiling_double_int(width), ceiling_double_int(height), BufferedImage.TYPE_INT_ARGB);

            // The graphics of the character image.
            Graphics2D graphics = (Graphics2D) characterImage.getGraphics();

            // Sets the font to the input font/
            graphics.setFont(font);

            // Sets the color to white with no alpha.
            graphics.setColor(new Color(255, 255, 255, 0));

            // Fills the entire image with the color above, makes it transparent.
            graphics.fillRect(0, 0, characterImage.getWidth(), characterImage.getHeight());

            // Sets the color to white to draw the character.
            graphics.setColor(Color.WHITE);

            // Enables anti-aliasing
            if (antiAlias) {
                graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, this.fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            }

            // Draws the character.
            graphics.drawString(character + "", MARGIN, fontMetrics.getAscent());

            // Generates a new texture id.
            int textureId = texturesIds[index];

            // Allocates the texture in opengl.
            createTexture(textureId, characterImage, textureQueue);

            // Initiates the character data and stores it in the data array.
            characterData[index] = new CharacterData(character, characterImage.getWidth(), characterImage.getHeight(), textureId);
        }

        // Returns the filled character data array.
        return characterData;
    }

    private static int ceiling_double_int(double value) {
        int i = (int) value;
        return value > (double) i ? i + 1 : i;
    }

    /**
     * Uploads the opengl texture.
     *
     * @param textureId The texture id to upload to.
     * @param image     The image to upload.
     */
    private void createTexture(int textureId, BufferedImage image, ConcurrentLinkedQueue<TextureData> textureQueue) {
        // Array of all the colors in the image.
        int[] pixels = new int[image.getWidth() * image.getHeight()];

        // Fetches all the colors in the image.
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        // Buffer that will store the texture data.
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        // Puts all the pixel data into the buffer.
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                // The pixel in the image.
                int pixel = pixels[y * image.getWidth() + x];

                // Puts the data into the byte buffer.
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        // Flips the byte buffer, not sure why this is needed.
        buffer.flip();

        textureQueue.add(new TextureData(textureId, image.getWidth(), image.getHeight(), buffer));
    }

    /**
     * Renders the given string.
     *
     * @param text  The text to be rendered.
     * @param x     The x position of the text.
     * @param y     The y position of the text.
     * @param color The color of the text.
     */
    public void drawString(String text, float x, float y, int color) {
        // fixes graphical glitches if the position is uneven
        float tempX = (int) x / 2f;
        x = tempX * 2;

        float tempY = (int) y / 2f;
        y = tempY * 2;

        renderString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - getWidth(text) / 2f, y, color);
    }

    /**
     * Renders the given string.
     *
     * @param text  The text to be rendered.
     * @param x     The x position of the text.
     * @param y     The y position of the text.
     * @param color The color of the text.
     */
    private void renderString(String text, float x, float y, int color) {
        // Returns if the text is empty.
        if (text.length() == 0) return;

        // Pushes the matrix to store gl values.
        GlStateManager.pushMatrix();

        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GlStateManager.scale(0.5f, 0.5f, 1);

        // Removes half the margin to render in the right spot.
        x -= MARGIN / 2f;
        y -= MARGIN / 2f;

        x *= 2;
        y *= 2;

        y += 2;

        float startingX = x;

        // The character texture set to be used. (Regular by default)
        CharacterData[] characterData = regularData;

        // The length of the text used for the draw loop.
        int length = text.length();

        // The multiplier.
        float multiplier = 255f;

        Color c = new Color(color);

        // Sets the color.
        GlStateManager.color(c.getRed() / multiplier, c.getGreen() / multiplier, c.getBlue() / multiplier, (float) ((color >> 24 & 0xFF) / 255d));

        // Loops through the text.
        for (int i = 0; i < length; i++) {
            // The character at the index of 'i'.
            char character = text.charAt(i);

            if (character == '\n') {
                y += font.getSize();
                x = startingX;
            }

            // Continues to not crash!
            if (character > 255) continue;

            // Draws the character.
            drawChar(character, characterData, x, y);

            // The character data for the given character.
            CharacterData charData = characterData[character];

            // Adds to the offset.
            x += charData.width - (2 * MARGIN);
        }

        GlStateManager.bindTexture(0);
        // Sets the color back to white so no odd rendering problems happen.
        GlStateManager.color(1, 1, 1, 1);
        // Restores previous values.
        GlStateManager.popMatrix();
    }

    /**
     * Gets the width of the given text.
     *
     * @param text The text to get the width of.
     * @return The width of the given text.
     */
    public float getWidth(String text) {
        // The width of the string.
        float width = 0;

        // The character texture set to be used. (Regular by default)
        CharacterData[] characterData = regularData;

        // The length of the text.
        int length = text.length();

        // Loops through the text.
        for (int i = 0; i < length; i++) {
            // The character at the index of 'i'.
            char character = text.charAt(i);

            // Continues to not crash!
            if (character > 255) continue;

            // The character data for the given character.
            CharacterData charData = characterData[character];

            // Adds to the offset.
            width += (charData.width - (2 * MARGIN)) / 2f;
        }

        // Returns the width.
        return width + MARGIN / 2f;
    }

    /**
     * Gets the height of the given text.
     *
     * @param text The text to get the height of.
     * @return The height of the given text.
     */
    public float getHeight(String text) {
        // The height of the string.
        float height = 0;

        // The character texture set to be used. (Regular by default)
        CharacterData[] characterData = regularData;

        // The length of the text.
        int length = text.length();

        // Loops through the text.
        for (int i = 0; i < length; i++) {
            // The character at the index of 'i'.
            char character = text.charAt(i);

            // Continues to not crash!
            if (character > 255) continue;

            // The character data for the given character.
            CharacterData charData = characterData[character];

            // Sets the height if it's bigger.
            height = (float) Math.max(height, charData.height);
        }

        // Returns the height.
        return height / 2 - MARGIN / 2f;
    }

    /**
     * Draws the character.
     *
     * @param character     The character to be drawn.
     * @param characterData The character texture set to be used.
     */
    private void drawChar(char character, CharacterData[] characterData, float x, float y) {
        // The char data that stores the character data.
        CharacterData charData = characterData[character];

        // Binds the character data texture.
        charData.bind();

        // Begins drawing the quad.
        GL11.glBegin(GL11.GL_QUADS);
        {
            // Maps out where the texture should be drawn.
            GL11.glTexCoord2f(0, 0);
            GL11.glVertex2d(x, y);
            GL11.glTexCoord2f(0, 1);
            GL11.glVertex2d(x, y + charData.height);
            GL11.glTexCoord2f(1, 1);
            GL11.glVertex2d(x + charData.width, y + charData.height);
            GL11.glTexCoord2f(1, 0);
            GL11.glVertex2d(x + charData.width, y);
        }
        // Ends the quad.
        GL11.glEnd();
    }

    /**
     * Class that holds the data for each character.
     */
    static class CharacterData {

        /**
         * The character the data belongs to.
         */
        public char character;

        /**
         * The width of the character.
         */
        public float width;

        /**
         * The height of the character.
         */
        public float height;

        /**
         * The id of the character texture.
         */
        private final int textureId;

        public CharacterData(char character, float width, float height, int textureId) {
            this.character = character;
            this.width = width;
            this.height = height;
            this.textureId = textureId;
        }

        /**
         * Binds the texture.
         */
        public void bind() {
            // Binds the opengl texture by the texture id.
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        }

    }

}