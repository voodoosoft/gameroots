package de.voodoosoft.gameroots.examples.colorgrading;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LookUpTableGenerator {

    static int[] dimensions = {16, 16, 16};
    static String outputFilePath = "core/assets/colorGradeTable.png";

    public static void main(String[] args) {
        if (args != null && args.length > 3){
            for (int i = 0; i < 3; i++) {
                try {
                    dimensions[i] = Integer.parseInt(args[i]);
                } catch (NumberFormatException e){
                    throw new IllegalArgumentException("Argument " + args[i] + "is not a valid integer.");
                }
            }
        }

        if (args != null){
            if (args.length == 1)
                outputFilePath = args[0];
            else if (args.length > 3)
                outputFilePath = args[3];
        }

        int layerWidth = dimensions[0]; //red
        int height = dimensions[1]; //green
        int layers = dimensions[2]; //blue
        int totalWidth = layerWidth * layers;

        BufferedImage image = new BufferedImage(totalWidth, height, BufferedImage.TYPE_3BYTE_BGR);

        for (int r = 0; r < layerWidth; r++) {
            for (int g = 0; g < height; g++) {
                for (int b = 0; b < layers; b++) {
                    int red = (int)(r / (float)(layerWidth - 1) * 255);
                    int green = (int)(g / (float)(height - 1) * 255);
                    int blue = (int)(b / (float)(layers - 1) * 255);
                    int argb = (red << 16) | (green << 8) | blue;
                    image.setRGB(r + b * layerWidth, g, argb);
                }
            }
        }

        try {
            File outputfile = new File(outputFilePath);
            ImageIO.write(image, "png", outputfile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(
                String.format("Look up table saved at: %s \nDimensions: %d, %d, %d", outputFilePath, layerWidth, height, layers));
    }

}
