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

        String s = "D:\\Pobrane\\pobrany plik.png";
        s = "D:\\Pobrane\\x";
        String s2;
        s = "C:\\Users\\Karol\\Desktop\\rys\\sky.jpg";
        int b=2;    //lepiej 3x4 niż 4x3 nakjpepszy wsp 1x4
        int sc=4;
        /*
        float i=0.0f;
        System.out.print("y:");
        int h=0;
        while(i<42)
        {
            h++;
            i=2.1f*h;
            System.out.print(", "+i);
        }
        i=0.0f;
        
        System.out.print("\nx:");
        int w=0;
        while(i<29.5)
        {   
            w++;
            i=2.2f*w;
            System.out.print(", "+i);
        }*/
        // curvedRound(s, "C:\\Users\\Karol\\Desktop\\mask1.jpg", 6,1,1);
        // drawChecks(s, "C:\\Users\\Karol\\Desktop\\rys.jpg");
        //imageToDOTS2(s, "C:\\Users\\Karol\\Desktop\\mask4.jpg",4,false);
        /*
         s="D:\\Pobrane\\x";
        File f=new File(s);
        for(File x:f.listFiles()){
            if(x.isFile()){
           // System.out.println(x.getParent());
           curvedRound(x.getAbsolutePath(), x.getParent()+"\\l\\"+x.getName()+".png", 12,1.2,4);
          // curvedLines(x.getAbsolutePath(), x.getParent()+"\\l\\"+x.getName()+".png", b,b*sc);
          // curvedLines(x.getAbsolutePath(),  x.getParent()+"\\l\\"+x.getName()+".png",b,b*sc);
          // imageToLine(x.getAbsolutePath(),  "C:\\Users\\Karol\\Desktop\\xd\\l\\"+x.getName()+".png",7);
           //imageToDOTS2(x.getAbsolutePath(), "C:\\Users\\Karol\\Desktop\\xd\\k\\"+x.getName()+".png",10,true);
        }
        }
         */

     
    /*    //curvedLines(s, s2+"\\cl1.jpg",6, 8);
         curvedRound(s, s2+"\\cr1.jpg",
                 2,
                 2,
                 2,
                 false);*/
          //curvedCircles(s, s2+"\\cc1.jpg", sc,sc);
        //720 1200
       imageToBurner_G_CODE(
                s,      //source image
                s2,     //out file
                250,
               // 100,      //min speed
                1500,  //max speed 1700
                /*5000*/  
              //  2500,
                0.078f,
             //   0.08f,   //tool radius
                5,      //lift z
             30,
                //  30,
                //150,    //scale image
            0.215f,
           //  0.15f,
               // 0.04f,  //scale gcode
               true    //display white
        );
        //   imageToDOTS(s, s2+"\\itD1.jpg", sc);
        //  imageToDOTS2(s, s2+"\\itD21.jpg", sc, false);
        //  imageToDots(s, s2+"\\itdm1.jpg", sc);
        // imageToLine(s, s2+"\\itl1.jpg", sc);
        //   curvedLines(s, s2+"\\cl2.jpg", 4, 8);
        //  curvedRound(s, s2+"\\cr2.jpg", 8,6,10,false);
        //  curvedCircles(s, s2+"\\cc2.jpg", sc,sc);
        // imageToDOTS(s, s2+"\\itD2.jpg", sc);
        // imageToDOTS2(s, s2+"\\itD22.jpg", sc, true);
        //  imageToDots(s, s2+"\\itdm2.jpg", 4);
        //  imageToLine(s, s2+"\\itl2.jpg", 4);
    }

    public static void imageToBurner_G_CODE(String src, String srs, int minSpeed, int maxSpeed, float toolRadius, int liftZ, int scaleImage,float scaleGCode, boolean displayWhite) throws IOException {
        float speedScale = (maxSpeed - minSpeed) ;
        float a4w = 11.6979166f * scaleImage;
        float a4h = 8.2638888f * scaleImage;
      //  boolean isUp=false;

       
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
        /* double scale=imageWidth/((float)a4w);
         if(scale<1)
             scale=((float)a4w)/imageWidth;
         if(scale*imageHeight>a4h){
             scale=imageHeight/((float)a4h);
             if(scale<1)
           scale=((float)a4h)/imageHeight;
         
         }*/
        //int scale =2;
        fw = new FileWriter(f);

        // int max=(int)(scale*scale);
        int scaleInt;/*=(int)(toolRadius*toolRadius);
            if(scaleInt<1)
                scaleInt=1;*/
        //  scaleInt*=scale;        
        scaleInt = (int) (toolRadius * scaleImage);

        if (scaleInt < 1) {
            scaleInt = 1;
        }
        flag=false;

        //   int move=(int)(scaleInt*scale);
        //int speed;
        fw.append("G90\nG0 Z0 F500\nG91\n");
        float move= scaleInt*scaleGCode;
        for (int i = 0; i < img.getHeight(); i += scaleInt) //for rows
        {
            System.out.println("nr " + i);
            int scale= (int)(img.getWidth()/scaleInt);
            if(!flag)
                for (int j = scale; j >= 0; j --) //for columns
            {
                inLoop(i,j*scaleInt,scaleInt,speedScale,move,minSpeed,displayWhite);
                 
            }else
            for (int j = 0; j <= scale; j++) //for columns
            {
                inLoop(i,j*scaleInt,scaleInt,speedScale,move,minSpeed,displayWhite);
                 
            }
          /*  if(!isUp)
                fw.append("G0 Z" + liftZ + " F600\n");*/
            // fw.append("G0 Y"+scaleInt+" F3000\nG90\n");
            fw.append("G0 Y" + move + " F3000\n");
          //  fw.append("G90\nG0 X0 F5000\nG91\n");
          /*  if(!isUp)
                fw.append("G0 Z" + (-liftZ) + " F600\nG91\n");
            */
            flag=!flag;
        }
       // fw.append("G91\nG0 Z" + liftZ + " F600\nG90\n");
         fw.append("G91\nG0 Z10 F600\nG90\n");
        fw.append("G28 X0 Y0");
        fw.flush();
        fw.close();

        System.out.println("KOniec");

    }
    private static void inLoop(int i,int j,int scaleInt,float speedScale,float move,int minSpeed, boolean displayWhite) throws IOException{
     // System.out.println("nr "+i+" "+j);
                luminance = 0;
               
          
                    for (int y = 0; y < scaleInt; y++) //for elements in box
                    {   

                        for (int x = 0; x < scaleInt; x++) {
                            if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {
                                // try{
                                p = img.getRGB(j + x, i + y);
                                Color c= new Color(p);

                              //  System.out.println("Rgb "+p);    

                                r = (p >> 16) & 0xff;
                                g = (p >> 8) & 0xff;
                                b = (p & 0xff);
                                /*
                                r = (int) Math.round(c.getRed()*0.299);
                                g = (int) Math.round(c.getGreen()*0.587);
                                b = (int) Math.round(c.getBlue()*0.114);
                                */
                                //System.out.println("Rgb "+r+" "+" "+g+" "+b);    
                              //  if(r>100||g>100||b>100)
                                //System.out.println("RGB "+r+" "+g+" "+b);    
                                int color=((r+g+b)/3);
                               // c=new Color(color,color,color);
                                luminance += color;//(r * 0.299 + g * 0.587 + b * 0.114);      
                                //luminance +=(r * 0.2126 + g * 0.7152 + b * 0.0722);                         

                               // luminance += c.getRGB();

                            }
                        }
                }
                luminance /= (scaleInt * scaleInt);
                System.out.println("Luminance2 "+luminance);    

                if(luminance<240||displayWhite){
               /*     if(isUp){
                        fw.append("G91\nG0 Z" + (-liftZ) + " F600\n");
                        isUp=false;
                    }*/
                    int speed = (int) (Math.round((luminance / 255f) * speedScale) + minSpeed);
                    if(flag)
                        fw.append("G0 X" +(-move) + " F" + speed + "\n");
                    else  
                        fw.append("G0 X" +move + " F" + speed + "\n");
                }
                else{
                /*    if(!isUp){
                        fw.append("G91\nG0 Z" + liftZ + " F600\n");
                        isUp=true;
                    }*/
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
        File f = new File(srs);//*src.substring(src.lastIndexOf('.'))*/);

        System.out.println(f.getAbsolutePath());
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        // Graphics graphics = bi.getGraphics();    
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
                    case 6:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 1.5));
                        break;
                    case 5:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 2));
                        break;
                    case 4:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 3));
                        break;
                    case 3:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 4));
                        break;
                    case 2:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 5));
                        break;
                    case 1:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 6));
                        break;
                    case 0:
                        g2d.draw(plaski(q, j, i, scalex, scaley));
                        break;
                    default:
                        break;

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
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics2D g2d = bi.createGraphics();

        double skalaodstepu = skala;
        //int odstep;
        int przerwa = odstep;
        System.out.println(img.getWidth() + " " + img.getHeight());
        int cenx = img.getWidth() / 2;
        int ceny = img.getHeight() / 2;
        int malapolowa = (cenx < ceny) ? ceny : cenx;
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
            dystans += (l * skalaodstepu + przerwa);
            for (double i = 0; i <= 360; i += stopnie) {
                if (stopnie == 0) {
                    dystans -= (l * skalaodstepu + przerwa);
                    ++l;//+=0.5;                                               //blokuje przy liczbach <1, 
                    odstep = (int) (lokregu / l);
                    stopnie = 360 / odstep;
                    dystans += (l * skalaodstepu + przerwa);
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
                //if(xpp<xpk&&ypp<xpk)
                for (int x = xpp; x <= xpk; x++) {
                    for (int y = ypp; y <= ypk; y++) {
                        try {
                            //   System.out.println(x+" "+y);                            //1026 918
                            p = img.getRGB(x, y);
                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                        } catch (Exception e) {
                            //System.err.println(x+" "+y);
                        }
                        ++licznik;

                    }
                }

                luminance = luminance / licznik;
                luminance = Math.round((luminance / 255) * 8) + 1;

                int dodatek = (int) ((luminance / 8) * l * skalaodstepu);

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
        int p, r, g, b, luminance = 0;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                p = img.getRGB(x, y);
                r = (p >> 16) & 0xff;
                g = (p >> 8) & 0xff;
                b = p & 0xff;
                luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                if ((r != 0 && g != 0 && b != 0)) {
                    bi.setRGB(x, y, imgwcolor.getRGB(x, y));
                }

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
        File f = new File(srs);//*src.substring(src.lastIndexOf('.'))*/);

        System.out.println(f.getAbsolutePath());
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        // Graphics graphics = bi.getGraphics();    
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
                    case 6:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 1.5));
                        break;
                    case 5:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 2));
                        break;
                    case 4:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 3));
                        break;
                    case 3:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 4));
                        break;
                    case 2:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 5));
                        break;
                    case 1:
                        g2d.draw(krzywa(q, j, i, scalex, scaley, 6));
                        break;
                    case 0:
                        g2d.draw(plaski(q, j, i, scalex, scaley));
                        break;
                    default:
                        break;

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
        File f = new File(srs);//*src.substring(src.lastIndexOf('.'))*/);

        System.out.println(f.getAbsolutePath());
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics graphics = bi.getGraphics();

        graphics.setColor(java.awt.Color.BLACK);                                                         //tło        
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
        graphics.setColor(java.awt.Color.WHITE);

        int max = size * size;
        int dots;
        Map<Integer, Integer> mapa = new HashMap<Integer, Integer>();

        int xpos;
        int ypos;

        for (int i = 0; i < img.getHeight(); i += size) {

            for (int j = 0; j < img.getWidth(); j += size) {
                System.out.println("nr " + i + " " + j);
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {
                            // try{
                            p = img.getRGB(j + x, i + y);

                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                            //    }catch(Exception e){}
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
                        //System.out.println("\n\nMapa:\n"+mapa.toString()+"\nWartosc: "+xpos+" "+mapa.get(xpos));
                        if (mapa.containsKey(xpos)) {
                            if (mapa.get(xpos).equals(ypos)) {
                                xpos = (int) Math.round((size) * Math.random());
                                ypos = (int) Math.round((size) * Math.random());
                                //System.err.println("liczę");
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
                //graphics.drawString(""+druk3(luminance), x*j, y*i);                     //moze odwrotnie             
            }
            luminance = 0;
        }
        System.out.println("KOniec");
        try {
            ImageIO.write(bi, "jpg", f);
        } catch (IOException ex) {
            Logger.getLogger(ImageToDotsLines.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException np) {
        }

    }

    public static void imageToLine(String src, String srs, int scale) {

        boolean color = true;
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

        int size = scale * 2;//8;
        int max = size * size;
        int lines;
        int pl = scale;//4;
        for (int i = 0; i < img.getHeight(); i += size) {

            for (int j = 0; j < img.getWidth(); j += size) {
                System.out.println("nr " + i + " " + j);
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {
                            // try{
                            p = img.getRGB(j + x, i + y);

                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                            //    }catch(Exception e){}
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
                    //graphics.fillRect(j, i, size, size);break;
                    case 2:
                        graphics.drawLine(j + pl + 1, i, j + pl - 1, i + size - 1);
                    case 3:
                        graphics.drawLine(j, i + pl + 1, j + size - 1, i + pl - 1);
                    case 4:
                        graphics.drawLine(j + pl - 1, i, j + pl + 1, i + size - 1);
                    case 5:
                        graphics.drawLine(j, i + pl - 1, j + size - 1, i + pl + 1);
                    case 6:
                        graphics.drawLine(j + size - 2, i, j + 1, i + size - 1);
                    case 7:
                        graphics.drawLine(j, i + 1, j + size - 1, i + size - 2);
                    case 8:
                        graphics.drawLine(j + pl, i, j + pl, i + size - 1);
                    case 9:
                        graphics.drawLine(j, i + pl, j + size - 1, i + pl);
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

                //graphics.drawString(""+druk3(luminance), x*j, y*i);                     //moze odwrotnie             
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

        int size = scale;//8;
        int max = size * size;
        int l;

        for (int i = 0; i < img.getHeight(); i += size) {

            for (int j = 0; j < img.getWidth(); j += size) {
                System.out.println("nr " + i + " " + j);
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {
                            // try{
                            p = img.getRGB(j + x, i + y);

                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                            //    }catch(Exception e){}
                        }
                    }
                }
                luminance /= max;

                l = Math.round((luminance / 255) * scale); //12

                graphics.fillOval(j + ((scale - l) / 2), i + ((scale - l) / 2), l, l);

                //graphics.drawString(""+druk3(luminance), x*j, y*i);                     //moze odwrotnie             
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

        System.out.println(f.getAbsolutePath());
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics graphics = bi.getGraphics();

        /*if(color)
            graphics.setColor(java.awt.Color.WHITE);                //tło
            else */
        graphics.setColor(java.awt.Color.BLACK);
        graphics.fillRect(0, 0, img.getWidth(), img.getHeight());

        /*if(color)
            graphics.setColor(java.awt.Color.BLACK);    
            else  */
        graphics.setColor(java.awt.Color.WHITE);

        int size = scale;//8;
        int max = size * size;
        int l;
        int iter = 1;
        for (int i = 0; i < img.getHeight(); i += size) {
            for (int j = 0; j < img.getWidth(); j += size) {
                System.out.println("nr " + i + " " + j);
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        if (img.getHeight() > (y + i) && img.getWidth() > (x + j)) {
                            // try{
                            p = img.getRGB(j + x, i + y);
                            if (color) {
                                graphics.setColor(new Color(p));
                            }

                            r = (p >> 16) & 0xff;
                            g = (p >> 8) & 0xff;
                            b = p & 0xff;
                            luminance += (r * 0.3f + g * 0.59f + b * 0.11f);
                            //    }catch(Exception e){}
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
        int lw = 0, lh = 0;
        /*
       g.setColor(Color.cyan);
       for(int i=0; i<w;i+=s)
       {
        g.drawLine(i, 0, i, h);
       }
       for(int i=0; i<h;i+=s)
       {
        g.drawLine(0, i, w, i);
       }*/
        s = s * 2;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));

        g.setColor(Color.WHITE);

        for (int i = 0; i < w; i += s) {

            g2.draw(new Line2D.Float(i, 0, i, h));
            lw++;
            // g.drawLine(i, 0, i, h);
        }

        for (int i = 0; i < h; i += s) {
            lh++;
            g2.draw(new Line2D.Float(0, i, w, i));

            // g.drawLine(0, i, w, i);
        }
        /* g.setColor(Color.blue);
       g2.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,42));
       //g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,12));
       g2.drawString("X:"+lw+" Y:"+lh, 1, 1);
         */
        System.out.println("\nX:" + lw + " Y:" + lh);
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
        int z = 1;
        //  g.drawPolygon(x, y, 6);

        g.setColor(Color.gray);
        boolean xv = true, yv = true, v = false;

        do {
            g.drawPolygon(tx, y, 6);
            do {
                // System.out.println("X"+z);
                for (int i = 0; i < 6; i++) {
                    tx[i] = tx[i] + px;

                    // System.out.print(x2[i]+", ");
                }
                // System.out.println();
                g.drawPolygon(tx, y, 6);
                for (int i = 5; i >= 0; i--) {
                    xv = tx[i] > xsize ? false : true;
                }

            } while (xv);
            z++;

            for (int i = 0; i < 6; i++) {
                y[i] = y[i] + py;
            }

            /*  v=(v)?false:true;
          tx=(v)?x1:x2;*/
            if (v) {
                v = false;

                for (int i = 0; i < 6; i++) {
                    tx[i] = x2[i];
                    // System.out.println(x2[i]);
                }
            } else {
                v = true;
                for (int i = 0; i < 6; i++) {
                    tx[i] = x1[i];
                }
            }

            xv = true;
            for (int i = 5; i >= 0; i--) {
                yv = y[i] > ysize ? false : true;
            }
            // System.out.println("Y");

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
