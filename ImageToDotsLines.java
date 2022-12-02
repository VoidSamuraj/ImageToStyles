/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imagetodots.lines;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Karol
 */
public class ImageToDotsLines {

    public static boolean wasup = false;
    static double luminance=0;
    static  int p, r, g, b;
    static BufferedImage img=null;
    static boolean flag=false;
    static FileWriter fw;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        String s = "D:\\joke.jpg";
        s="D:\\geralt.jpg";
        String s2="D:\\test";

        //drawChecks(s, "D:\\joke.jpg");
       // imageToDOTS(s, s2+"\\itD.jpg", 5);
      //  imageToDOTS2(s, s2+"\\itD2.jpg", 5, false);

      //  imageToLine(s, s2+"\\itl.jpg", 5);

        curvedLines(s, s2+"\\cl.jpg", 4, 8);
       // curvedCircles(s, s2+"\\cc.jpg",4,10);

      //  curvedRound(s, s2+"\\cr2.jpg", 8,1,4,false);

    }

    public static void imageToBurner_G_CODE(String src, String srs, int minSpeed, int maxSpeed, float toolRadius, int liftZ, int scaleImage,float scaleGCode, boolean displayWhite) throws IOException {
        float speedScale = (maxSpeed - minSpeed) ;

        luminance = 0;
        img = null;
        File f = new File(srs);

        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        int imageWidth = img.getWidth();
        int imageHeight = img.getHeight();
        System.out.println("w " + imageWidth + " : h " + imageHeight);

        fw = new FileWriter(f);

        int scaleInt;
        scaleInt = (int) (toolRadius * scaleImage);

        if (scaleInt < 1) {
            scaleInt = 1;
        }
        flag=false;

        //int speed;
        fw.append("G90\nG0 Z0 F500\nG91\n");
        float move= scaleInt*scaleGCode;
        for (int i = 0; i < img.getHeight(); i += scaleInt) //for rows
        {
            System.out.println("nr " + i);
            int scale= img.getWidth()/scaleInt;
            if(!flag)
                for (int j = scale; j >= 0; j --) //for columns
                {
                    inLoop(i,j*scaleInt,scaleInt,speedScale,move,minSpeed,displayWhite);

                }else
                for (int j = 0; j <= scale; j++) //for columns
                {
                    inLoop(i,j*scaleInt,scaleInt,speedScale,move,minSpeed,displayWhite);

                }

            fw.append("G0 Y" + move + " F3000\n");

            flag=!flag;
        }
        fw.append("G91\nG0 Z10 F600\nG90\n");
        fw.append("G28 X0 Y0");
        fw.flush();
        fw.close();

        System.out.println("KOniec");

    }
    private static void inLoop(int i,int j,int scaleInt,float speedScale,float move,int minSpeed, boolean displayWhite) throws IOException{
        luminance = 0;


        for (int y = 0; y < scaleInt; y++) //for elements in box
        {

            for (int x = 0; x < scaleInt; x++) {
                if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {

                    p = img.getRGB(j + x, i + y);

                    r = (p >> 16) & 0xff;
                    g = (p >> 8) & 0xff;
                    b = (p & 0xff);

                    int color=((r+g+b)/3);
                    luminance += color;

                }
            }
        }
        luminance /= (scaleInt * scaleInt);
        System.out.println("Luminance2 "+luminance);

        if(luminance<240||displayWhite){
            int speed = (int) (Math.round((luminance / 255f) * speedScale) + minSpeed);
            if(flag)
                fw.append("G0 X" +(-move) + " F" + speed + "\n");
            else
                fw.append("G0 X" +move + " F" + speed + "\n");
        }
        else{
            if(flag)
                fw.append("G0 X" +(-move) + " F5000\n");
            else
                fw.append("G0 X" +move + " F5000\n");
        }
    }

    public static void curvedLines(String src, String srs, int scalex, int scaley) {
        QuadCurve2D q = new QuadCurve2D.Float();
        int p, r, g, b;
        float luminance = 0;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(srs);

        System.out.println(f.getAbsolutePath());
        if(img==null)
            return;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics2D g2d = bi.createGraphics();

        int max = scalex * scaley;
        int l;
        int iter = 1;
        for (int i = 0; i < img.getHeight(); i += scaley) {
            wasup = false;
            for (int j = 0; j < img.getWidth(); j += scalex) {
                System.out.println("nr " + i + " " + j);
                for (int y = 0; y < scaley; y++) {
                    for (int x = 0; x < scalex; x++) {
                        if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {
                            // try{
                            p = img.getRGB(j + x, i + y);

                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                        }
                    }
                }
                luminance /= max;

                l = Math.round((luminance / 255) * 6);

                switch (l) {
                    case 6 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 1.5));
                    case 5 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 2));
                    case 4 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 3));
                    case 3 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 4));
                    case 2 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 5));
                    case 1 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 6));
                    case 0 -> g2d.draw(plaski(q, j, i, scalex, scaley));
                    default -> {
                    }
                }

            }
            iter = ((iter == 1) ? 2 : 1);
            luminance = 0;
        }
        System.out.println("KOniec");
        try {
            ImageIO.write(bi, "jpg", f);
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void curvedRound(String src, String srs, int l, double skala, int odstep, boolean color) {
        //l=l<8?8:l;
        QuadCurve2D q = new QuadCurve2D.Float();
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(srs);

        System.out.println(f.getAbsolutePath());
        if(img==null)
            return;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics2D g2d = bi.createGraphics();

        //int odstep;
        int przerwa = odstep;
        System.out.println(img.getWidth() + " " + img.getHeight());
        int cenx = img.getWidth() / 2;
        int ceny = img.getHeight() / 2;
        int malapolowa = Math.max(cenx, ceny);
        double dystans = l;
        int lastx = 0;
        int lasty = 0;
        int pointx;
        int pointmx;
        int pointmy;
        int pointy;
        int xpp, ypp, xpk, ypk;
        int p, r, g, b;

        while (dystans < malapolowa) {

            double lokregu = ((2.0 * Math.PI) * dystans);
            odstep = (int) (lokregu / l);
            double stopnie = 360 / odstep;
            dystans += (l * skala + przerwa);
            for (double i = 0; i <= 360; i += stopnie) {
                if (stopnie == 0) {
                    dystans -= (l * skala + przerwa);
                    ++l;//+=0.5;                                               //blokuje przy liczbach <1, 
                    odstep = (int) (lokregu / l);
                    stopnie = 360 / odstep;
                    dystans += (l * skala + przerwa);
                    continue;
                }
                double deg = Math.toRadians((i));
                double deg2 = Math.toRadians((i - stopnie) + stopnie / 2);
                // dystans+=przyrost;
                pointx = (int) (dystans * Math.cos(deg)) + cenx;
                pointy = (int) (dystans * Math.sin(deg)) + ceny;
                pointmx = (int) (dystans * Math.cos(deg2)) + cenx;
                pointmy = (int) (dystans * Math.sin(deg2)) + ceny;

                //pobierz dane pixeli
                xpp = pointmx - l / 2;
                ypp = pointmy - l / 2;
                xpk = pointmx + l / 2;
                ypk = pointmy + l / 2;

                int licznik = 0;
                double luminance = 0;
                if (xpp < 0) {
                    xpp = 0;
                }
                if (ypp < 0) {
                    ypp = 0;
                }
                if (xpk > img.getWidth()) {
                    xpk = img.getWidth();
                }
                if (ypk > img.getHeight()) {
                    ypk = img.getHeight();
                }
                for (int x = xpp; x <= xpk; x++) {
                    for (int y = ypp; y <= ypk; y++) {
                        try {
                            p = img.getRGB(x, y);
                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                            ++licznik;
                        }catch(ArrayIndexOutOfBoundsException ex){

                        }
                    }
                }

                luminance = luminance / licznik;
                luminance = Math.round((luminance / 255) * 8) + 1;

                int dodatek = (int) ((luminance / 8) * l * skala);

                if (wasup) {
                    dodatek = -dodatek;
                    wasup = false;
                } else {
                    wasup = true;
                }
                pointmx = (int) ((dystans + dodatek) * Math.cos(deg2)) + cenx;
                pointmy = (int) ((dystans + dodatek) * Math.sin(deg2)) + ceny;
                if (lastx != 0 && lasty != 0) {
                    g2d.draw(krzywa(q, lastx, lasty, pointmx, pointmy, pointx, pointy));
                }
                lastx = pointx;
                lasty = pointy;
            }
            lastx = lasty = 0;
        }

        System.out.println("KOniec");
        try {
            if (color) {
                ImageIO.write(koloruj(bi, img), "jpg", f);
            } else {
                ImageIO.write(bi, "jpg", f);
            }

        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static BufferedImage koloruj(BufferedImage img, BufferedImage imgwcolor) {
        int p, r, g, b;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                try {
                    p = img.getRGB(x, y);
                    r = (p >> 16) & 0xff;
                    g = (p >> 8) & 0xff;
                    b = p & 0xff;
                    luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                    if ((r != 0 && g != 0 && b != 0)) {
                        bi.setRGB(x, y, imgwcolor.getRGB(x, y));
                    }
                }catch (Exception ex
                ){}
            }
        }

        return bi;
    }

    public static void curvedCircles(String src, String srs, int scalex, int scaley) {
        QuadCurve2D q = new QuadCurve2D.Float();
        int p, r, g, b;
        float luminance = 0;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(srs);

        System.out.println(f.getAbsolutePath());
        if(img==null)
            return;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics2D g2d = bi.createGraphics();

        int max = scalex * scaley;
        int l;
        int iter = 1;
        for (int i = 0; i < img.getHeight(); i += scaley) {
            wasup = false;
            for (int j = 0; j < img.getWidth(); j += scalex) {
                System.out.println("nr " + i + " " + j);
                for (int y = 0; y < scaley; y++) {
                    for (int x = 0; x < scalex; x++) {
                        if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {
                            // try{
                            p = img.getRGB(j + x, i + y);

                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                        }
                    }
                }
                luminance /= max;

                l = Math.round((luminance / 255) * 6);

                switch (l) {
                    case 6 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 1.5));
                    case 5 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 2));
                    case 4 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 3));
                    case 3 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 4));
                    case 2 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 5));
                    case 1 -> g2d.draw(krzywa(q, j, i, scalex, scaley, 6));
                    case 0 -> g2d.draw(plaski(q, j, i, scalex, scaley));
                    default -> {
                    }
                }

            }
            iter = ((iter == 1) ? 2 : 1);
            luminance = 0;
        }
        System.out.println("KOniec");
        try {
            ImageIO.write(bi, "jpg", f);
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static QuadCurve2D krzywa(QuadCurve2D curve, int x, int y, int scalex, int scaley, double dzielnik) {

        int i = scaley;
        if (wasup) {
            i = -scaley;
            wasup = false;
        } else {
            wasup = true;
        }
        curve.setCurve(x, y + ((scaley) / 2), x + ((scalex) / 2), y + ((scaley) / 2) + (i / dzielnik), x + scalex, y + ((scaley) / 2));
        return curve;
    }

    public static QuadCurve2D krzywa(QuadCurve2D curve, int x, int y, int mx, int my, int lx, int ly) {

        curve.setCurve(x, y, mx, my, lx, ly);
        return curve;
    }

    public static QuadCurve2D plaski(QuadCurve2D curve, int x, int y, int scalex, int scaley) {
        curve.setCurve(x, y + ((scaley) / 2), x + ((scalex) / 2), y + ((scaley) / 2), x + scalex, y + ((scaley) / 2));
        return curve;
    }

    public static void imageToDots(String src, String srs, int size) {
        int p, r, g, b;
        float luminance = 0;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(srs);

        System.out.println(f.getAbsolutePath());
        if(img==null)
            return;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics graphics = bi.getGraphics();

        graphics.setColor(java.awt.Color.BLACK);                                                         //tło        
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
        graphics.setColor(java.awt.Color.WHITE);

        int max = size * size;
        int dots;
        Map<Integer, Integer> mapa = new HashMap<>();

        int xpos;
        int ypos;

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

                dots = Math.round((luminance / 255) * max);

                for (int d = 0; d < dots; d++) {
                    xpos = (int) Math.round((size) * Math.random());
                    ypos = (int) Math.round((size) * Math.random());
                    boolean war = true;
                    while (war) {
                        if (mapa.containsKey(xpos)) {
                            if (mapa.get(xpos).equals(ypos)) {
                                xpos = (int) Math.round((size) * Math.random());
                                ypos = (int) Math.round((size) * Math.random());
                            } else {
                                war = false;
                            }
                        } else {
                            war = false;
                        }
                    }
                    mapa.put(xpos, ypos);
                    if (img.getWidth() > (xpos + j) && img.getHeight() > (ypos + i)) {
                        graphics.fillRect(xpos + j, ypos + i, 1, 1);
                    }

                }
                mapa.clear();
            }
            luminance = 0;
        }
        System.out.println("KOniec");
        try {
            ImageIO.write(bi, "jpg", f);
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } catch (NullPointerException np) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, "null", np);
        }

    }

    public static void imageToLine(String src, String srs, int scale) {

        int p, r, g, b;
        float luminance = 0;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(srs);

        System.out.println(f.getAbsolutePath());
        if(img==null)
            return;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics graphics = bi.getGraphics();

        graphics.setColor(java.awt.Color.WHITE);                //tło
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
        graphics.setColor(java.awt.Color.BLACK);

        int size = scale * 2;//8;
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

    public static void imageToDOTS(String src, String srs, int scale) {

        boolean color = false;
        int p, r, g, b;
        float luminance = 0;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(srs);//*src.substring(src.lastIndexOf('.'))*/);

        System.out.println(f.getAbsolutePath());
        if(img==null)
            return;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics graphics = bi.getGraphics();

        if (color) {
            graphics.setColor(java.awt.Color.WHITE);                //tło
        } else {
            graphics.setColor(java.awt.Color.BLACK);
        }
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());

        if (color) {
            graphics.setColor(java.awt.Color.BLACK);
        } else {
            graphics.setColor(java.awt.Color.WHITE);
        }

        int max = scale * scale;
        int l;

        for (int i = 0; i < img.getHeight(); i += scale) {

            for (int j = 0; j < img.getWidth(); j += scale) {
                System.out.println("nr " + i + " " + j);
                for (int y = 0; y < scale; y++) {
                    for (int x = 0; x < scale; x++) {
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
                l = Math.round((luminance / 255) * scale); //12
                graphics.fillOval(j + ((scale - l) / 2), i + ((scale - l) / 2), l, l);
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

    public static void imageToDOTS2(String src, String srs, int scale, boolean color) {

        int p, r, g, b;
        float luminance = 0;
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(src));
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }
        File f = new File(srs);//*src.substring(src.lastIndexOf('.'))*/);

        if(img==null)
            return;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics graphics = bi.getGraphics();

        if(color)
            graphics.setColor(java.awt.Color.WHITE);                //tło
        else
            graphics.setColor(java.awt.Color.BLACK);
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());

        if(color)
            graphics.setColor(java.awt.Color.BLACK);
        else
            graphics.setColor(java.awt.Color.WHITE);

        int max = scale * scale;
        int l;
        int iter = 1;
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

                if (iter == 2) {
                    graphics.fillOval(j + ((scale - l) / 2), i + ((scale - l) / 2), l, l);
                } else if (iter == 1) {
                    graphics.fillOval(j + (((scale - l) / 2) + scale / 2), i + ((scale - l) / 2), l, l);
                }

                //graphics.drawString(""+druk3(luminance), x*j, y*i);                     //moze odwrotnie             
            }
            iter = ((iter == 1) ? 2 : 1);
            luminance = 0;
        }
        System.out.println("KOniec");
        try {
            ImageIO.write(bi, "jpg", f);
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void drawChecks(String src, String srt) throws IOException {
        File f = new File(srt);
        BufferedImage bi = ImageIO.read(new File(src));
        Graphics g = bi.getGraphics();
        int w = bi.getWidth();
        int h = bi.getHeight();
        int s = 40;

        s = s * 2;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        g.setColor(Color.WHITE);

        for (int i = 0; i < w; i += s) {

            g2.draw(new Line2D.Float(i, 0, i, h));
        }

        for (int i = 0; i < h; i += s) {
            g2.draw(new Line2D.Float(0, i, w, i));
        }

        try {
            ImageIO.write(bi, "jpg", f);
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void drawPolygons(String src, String srt) throws IOException {
        File f = new File(srt);
        BufferedImage bi = ImageIO.read(new File(src));
        Graphics g = bi.getGraphics();
        int w = 6;
        int px = 10 * w, py = 8 * w;

        final int[] x1 = {5 * w, 10 * w, 15 * w, 15 * w, 10 * w, 5 * w};
        final int[] x2 = {0, 5 * w, 10 * w, 10 * w, 5 * w, 0};
        int[] tx = {0, 5 * w, 10 * w, 10 * w, 5 * w, 0};
        int[] y = {2 * w, 0, 2 * w, 8 * w, 10 * w, 8 * w};

        for (int i = 0; i < 6; i++) {
            System.out.println(x2[i]);
        }

        int xsize = bi.getWidth();
        int ysize = bi.getHeight();

        g.setColor(Color.gray);
        boolean xv = true, yv = true, v = false;

        do {
            g.drawPolygon(tx, y, 6);
            do {
                for (int i = 0; i < 6; i++) {
                    tx[i] = tx[i] + px;
                }
                g.drawPolygon(tx, y, 6);
                for (int i = 5; i >= 0; i--) {
                    xv = tx[i] <= xsize;
                }
            } while (xv);

            for (int i = 0; i < 6; i++) {
                y[i] = y[i] + py;
            }

            if (v) {
                v = false;

                System.arraycopy(x2, 0, tx, 0, 6);
            } else {
                v = true;
                System.arraycopy(x1, 0, tx, 0, 6);
            }

            xv = true;
            for (int i = 5; i >= 0; i--) {
                yv = y[i] <= ysize;
            }

        } while (yv);
        /*
       for(int yd=0;yd<ysize;yd+=8)
       {
           
       for(int xd=0;xd<xsize;xd+=10)
       {
               g.drawPolygon(tx, y, 6); 
               for(int i=0;i<6;i++)
                    tx[i]=tx[i]+10;
       }
        for(int i=0;i<6;i++)
                    y[i]=y[i]+8;
          v=(v==true)?false:true;
          tx=(v==true)?x1:x2;
           }*/
        ImageIO.write(bi, "jpg", f);

    }

}
