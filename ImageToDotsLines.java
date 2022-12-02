
package imagetodots.lines;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Karol Robak
 */
public class ImageToDotsLines {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println(args[0]+" "+args[1]);
        imageToDOTS(args[0],args[1]+"\\itd1.jpg",5,false,true);
        imageToDOTS(args[0],args[1]+"\\itd2.jpg",5,false,false);
        imageToDOTS(args[0],args[1]+"\\itd3.jpg",5,true,true);
        imageToLine(args[0], args[1]+"\\itl.jpg", 5);

    }

    public static void imageToLine(String src, String srs, int scale) {

        int p, r, g, b;
        float luminance = 0;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            System.err.println("CANNOT FIND INPUT FILE");
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(srs);

        if(img==null){
            System.err.println("CANNOT READ FILE");
            return;
        }
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics graphics = bi.getGraphics();

        graphics.setColor(java.awt.Color.WHITE);                //tło
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
        graphics.setColor(java.awt.Color.BLACK);

        int size = scale * 2;
        int max = size * size;
        int lines;
        for (int i = 0; i < img.getHeight(); i += size) {

            for (int j = 0; j < img.getWidth(); j += size) {
                System.out.println("nr " + i + " " + j);
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {
                            p = img.getRGB(j + x, i + y);

                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                        }
                    }
                }
                luminance /= max;

                lines = Math.round((luminance / 255) * 12);

                switch (lines) {
                    case 0:
                        graphics.drawLine(j + size - 1, i + 1, j, i + size - 2);
                    case 1:
                        graphics.drawLine(j, i - 1, j + size - 1, i + size - 2);
                    case 2:
                        graphics.drawLine(j + scale + 1, i, j + scale - 1, i + size - 1);
                    case 3:
                        graphics.drawLine(j, i + scale + 1, j + size - 1, i + scale - 1);
                    case 4:
                        graphics.drawLine(j + scale - 1, i, j + scale + 1, i + size - 1);
                    case 5:
                        graphics.drawLine(j, i + scale - 1, j + size - 1, i + scale + 1);
                    case 6:
                        graphics.drawLine(j + size - 2, i, j + 1, i + size - 1);
                    case 7:
                        graphics.drawLine(j, i + 1, j + size - 1, i + size - 2);
                    case 8:
                        graphics.drawLine(j + scale, i, j + scale, i + size - 1);
                    case 9:
                        graphics.drawLine(j, i + scale, j + size - 1, i + scale);
                    case 10:
                        graphics.drawLine(j, i, j + size - 1, i + size - 1);
                    case 11:
                        graphics.drawLine(j + size - 1, i, j, i + size - 1);
                    case 12:
                        break;
                    default:
                        System.out.println("nie tak" + lines);
                        break;
                }

            }
            luminance = 0;
        }
        System.out.println("KOniec");
        try {
            ImageIO.write(bi, "jpg", f);
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    public static void imageToDOTS(String src, String srs, int scale, boolean color,boolean shift) {

        int p, r, g, b;
        float luminance = 0;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            System.err.println("CANNOT FIND INPUT FILE");
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(srs);

        if(img==null)
            return;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics graphics = bi.getGraphics();

        graphics.setColor(java.awt.Color.BLACK);
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());

            graphics.setColor(java.awt.Color.WHITE);

        int max = scale * scale;
        int l;
        boolean iter = true;
        for (int i = 0; i < img.getHeight(); i += scale) {
            for (int j = 0; j < img.getWidth(); j += scale) {
                System.out.println("nr " + i + " " + j);
                for (int y = 0; y < scale; y++) {
                    for (int x = 0; x < scale; x++) {
                        if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {
                            p = img.getRGB(j + x, i + y);
                            if (color) {
                                graphics.setColor(new Color(p));
                            }

                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                        }
                    }
                }
                luminance /= max;

                l = Math.round((luminance / 255) * scale); //12
                if(shift){
                    if (iter) {
                        graphics.fillOval(j + ((scale - l) / 2), i + ((scale - l) / 2), l, l);
                    } else {
                        graphics.fillOval(j + (((scale - l) / 2) + scale / 2), i + ((scale - l) / 2), l, l);
                    }
                }else
                    graphics.fillOval(j + ((scale - l) / 2), i + ((scale - l) / 2), l, l);
      }
            iter = !iter;
            luminance = 0;
        }
        try {
            ImageIO.write(bi, "jpg", f);
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
