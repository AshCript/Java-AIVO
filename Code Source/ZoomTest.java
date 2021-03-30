import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
//import javax.swing.border.*;
import javax.swing.event.*;
import com.as.func.*;

public class ZoomTest{
    public ImagePanel zoomImagePanel = new ImagePanel();
    public static JButton[] btns = new JButton[24];
    public JFrame f;
    public ZoomTest(){
        btns[0] = new JButton(new ImageIcon("./img/btn/conv1.jpg")); // Convolution 1 : filtre de l'exemple
        btns[1] = new JButton(new ImageIcon("./img/btn/conv2.jpg")); // Convolution 2 : filtre de Sobel 1
        btns[2] = new JButton(new ImageIcon("./img/btn/conv3.jpg")); // Convolution 3 : filtre de Sobel 2 :1980
        btns[3] = new JButton(new ImageIcon("./img/btn/conv4.jpg")); // Convolution 4 : filtre perso 1
        btns[4] = new JButton(new ImageIcon("./img/btn/conv5.jpg")); // Convolution 5 : filtre perso 2
        btns[5] = new JButton(new ImageIcon("./img/btn/conv6.jpg")); // Convolution 6 : filtre perso 3
        btns[6] = new JButton(new ImageIcon("./img/btn/conv7.jpg")); // Convolution 7 : filtre de rehaussement
        btns[7] = new JButton(new ImageIcon("./img/btn/conv8.jpg")); // Convolution 8 : filtre d'accentuation
        btns[8] = new JButton(new ImageIcon("./img/btn/conv9.jpg")); // Convolution 9 : filtre Laplacien
        btns[9] = new JButton(new ImageIcon("./img/btn/grayscale.jpg")); // Niveau de gris
        btns[10] = new JButton(new ImageIcon("./img/btn/inversionGrayscale.jpg")); // Inversion de niveau de gris
        btns[11] = new JButton(new ImageIcon("./img/btn/inversionColor.jpg")); // Inversion de couleur
        btns[12] = new JButton(new ImageIcon("./img/btn/etalementDynamique.jpg")); // étalement de la dynamique
        btns[13] = new JButton(new ImageIcon("./img/btn/binOtsu.jpg")); // Binarisation automatique par l'algorithme d'Otsu
        btns[14] = new JButton(new ImageIcon("./img/btn/contour.jpg")); // Contour d'image
        btns[15] = new JButton(new ImageIcon("./img/btn/etiquetage.jpg")); //Etiquetage
        btns[16] = new JButton(new ImageIcon("./img/btn/erosion.jpg")); //Erosion
        btns[17] = new JButton(new ImageIcon("./img/btn/dilatation.jpg")); //Dilatation
        btns[18] = new JButton(new ImageIcon("./img/btn/ouverture.jpg")); //Ouverture
        btns[19] = new JButton(new ImageIcon("./img/btn/fermeture.jpg")); //Fermeture
        btns[20] = new JButton(new ImageIcon("./img/btn/epaississement.jpg")); //Epaississement
        btns[21] = new JButton(new ImageIcon("./img/btn/amincissement.jpg")); //Amincissement
        btns[22] = new JButton(new ImageIcon("./img/btn/topHatOuv.jpg")); //Top-Hat à l'ouverture
        btns[23] = new JButton(new ImageIcon("./img/btn/topHatFer.jpg")); //Top-Hat à la fermeture
        
        ImageZoom.removeOtherBorders();
        ImageZoom zoom = new ImageZoom(zoomImagePanel);
        f = new JFrame();
        f.setIconImage(PicFrame.icon);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        f.setTitle("");
        f.setJMenuBar(PicFrame.menuBar);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(zoom.getUIPanel(), "South");
        f.getContentPane().add(zoom.getButtonPanel(), "West");
        f.getContentPane().add(new JScrollPane(zoomImagePanel));
        f.setBounds(screenSize.width - screenSize.width*3/4, screenSize.height - screenSize.height*4/5, 700, 450);
        f.setVisible(true);


        btns[0].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
                BufferedImage finalImage;
                float [][] filter = {{0.5f, 0.5f, 0.0f }, {0.5f, 0f, -0.5f}, {0f, -0.5f, -1f}}; //Filtre de lexemple donné
                String filterName = "Filtre de l'exemple";
                finalImage = Fonctions.linearFilter(PicFrame.image, filter);     
                PicFrame.memorize(finalImage, "Convolution discrete : " + filterName);
                PicFrame.removeAccelerator();
                PicFrame.showPrevious();
        });
        btns[1].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
                BufferedImage finalImage;
                float [][] filter = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}}; //Filtre de Sobel 1
                String filterName = "Filtre de Sobel 1";
                finalImage = Fonctions.linearFilter(PicFrame.image, filter);     
                PicFrame.memorize(finalImage, "Convolution discrete : " + filterName);
                PicFrame.removeAccelerator();
                PicFrame.showPrevious();
        });
        btns[2].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
                BufferedImage finalImage;
                float [][] filter = {{-1f, 0f, 1f}, {-2f, 0f, 2f}, {-1f, 0f, 1f}}; //Filtre de Sobel 2 : 1980
                String filterName = "Filtre de Sobel 2 (1980)";
                finalImage = Fonctions.linearFilter(PicFrame.image, filter);     
                PicFrame.memorize(finalImage, "Convolution discrete : " + filterName);
                PicFrame.removeAccelerator();
                PicFrame.showPrevious();
        });
        btns[3].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
                BufferedImage finalImage;
                float [][] filter = {{-0.5f, -0.5f, 0.5f}, {-1, 0, 1}, {-0.5f, -0.5f, 0.5f}}; //Filtre de convolution personnel 1
                String filterName = "Filtre personnel 1";
                finalImage = Fonctions.linearFilter(PicFrame.image, filter);     
                PicFrame.memorize(finalImage, "Convolution discrete : " + filterName);
                PicFrame.removeAccelerator();
                PicFrame.showPrevious();
        });
        btns[4].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
                BufferedImage finalImage;
                float [][] filter = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}}; //Filtre de convolution personnel 2
                String filterName = "Filtre personnel 2";
                finalImage = Fonctions.linearFilter(PicFrame.image, filter);     
                PicFrame.memorize(finalImage, "Convolution discrete : " + filterName);
                PicFrame.removeAccelerator();
                PicFrame.showPrevious();
        });
        btns[5].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
                BufferedImage finalImage;
                float [][] filter = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}; //filtre de convolution personnel 3
                String filterName = "Filtre personnel 3";
                finalImage = Fonctions.linearFilter(PicFrame.image, filter);     
                PicFrame.memorize(finalImage, "Convolution discrete : " + filterName);
                PicFrame.removeAccelerator();
                PicFrame.showPrevious();
        });
        btns[6].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
                BufferedImage finalImage;
                float [][] filter = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}}; //Filtre de rehaussement
                String filterName = "Filtre de rehaussement";
                finalImage = Fonctions.linearFilter(PicFrame.image, filter);     
                PicFrame.memorize(finalImage, "Convolution discrete : " + filterName);
                PicFrame.removeAccelerator();
                PicFrame.showPrevious();
        });
        btns[7].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
                BufferedImage finalImage;
                float [][] filter = {{0.0f, -0.5f, 0.0f}, {-0.5f, 3.0f, -0.5f}, {0.0f, -0.5f, 0.0f}}; //Filtre d'accentuation
                String filterName = "Filtre d'accentuation";
                finalImage = Fonctions.linearFilter(PicFrame.image, filter);     
                PicFrame.memorize(finalImage, "Convolution discrete : " + filterName);
                PicFrame.removeAccelerator();
                PicFrame.showPrevious();
        });
        btns[8].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
            BufferedImage finalImage;
            float [][] filter = {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}}; //Filtre Laplacien
            String filterName = "Filtre Laplacien";
            finalImage = Fonctions.linearFilter(PicFrame.image, filter);     
            PicFrame.memorize(finalImage, "Convolution discrete : " + filterName);
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[9].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
            BufferedImage finalImage;
            finalImage = Fonctions.grayscale(PicFrame.image);     
            PicFrame.memorize(finalImage, "Niveau de gris");
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[10].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
            BufferedImage finalImage;
            finalImage = Fonctions.inversionGrayscale(PicFrame.image);     
            PicFrame.memorize(finalImage, "Inversion de niveau de gris");
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[11].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
            BufferedImage finalImage;
            finalImage = Fonctions.inversionColor(PicFrame.image);     
            PicFrame.memorize(finalImage, "Inversion de couleur");
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[12].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[12].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int m, M;
            String message_m = new String(), message_M = new String();
            try{
                do{
                    message_m = JOptionPane.showInputDialog(null, "Inserez la valeur de m (valeur minimum : 0)", null, JOptionPane.QUESTION_MESSAGE);
                    m = Integer.valueOf(message_m).intValue();
                }while(m < 0 || m > 254);
                do{
                    message_M = JOptionPane.showInputDialog(null, "Inserez la valeur de M (valeur maximum : 255), M > " + m, null, JOptionPane.QUESTION_MESSAGE);
                    M = Integer.valueOf(message_M).intValue();
                }while(M <= m || M > 255);
                BufferedImage finalImage = Fonctions.etalementDynamique(PicFrame.image, m, M);
                PicFrame.memorize(finalImage, "Etalement de la dynamique, m=" + m + " M=" + M);
                PicFrame.removeAccelerator();
                PicFrame.showPrevious();
                
            }catch(NumberFormatException ee){
                JOptionPane.showMessageDialog(null, "Oups! Une erreur s'est produite lors de l'insertion de valeur", null, JOptionPane.INFORMATION_MESSAGE);
            }catch(Exception e1){}
        });
        btns[13].addActionListener(e ->{
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            BufferedImage finalImage = Fonctions.binarisationOtsu(PicFrame.image);
            PicFrame.memorize(finalImage, "Methode d'Otsu" + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[14].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[14].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            BufferedImage finalImage = Fonctions.contourByPapertTurtle(PicFrame.image);
            PicFrame.memorize(finalImage, "Contour par l'algorithme de la tortue de Papert" + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[15].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[15].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            int nombreObjets = Fonctions.objectsNumber(PicFrame.image);
            BufferedImage finalImage = Fonctions.etiquetage(PicFrame.image);
            PicFrame.memorize(finalImage, "Etiquetage"  + (seuil == 0 ? "" : ", seuil=" + seuil) + ", Objets : " + nombreObjets);
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[16].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[16].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            int origineL;
            int origineC;
            // On questionne l'utilisateur des coordonnées de l'origine de l'élément
            // structurant.
            JOptionPane.showMessageDialog(null, "L'element structurant qu'on \nutilise ici est une matrice 3x3", null, JOptionPane.INFORMATION_MESSAGE);
            do {
                origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineL < 0 || origineL > 2);
            do {
                origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineC < 0 || origineC > 2);
            BufferedImage finalImage = Fonctions.erosion(PicFrame.image, origineL, origineC, 3);
            PicFrame.memorize(finalImage, "Erosion"  + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[17].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[17].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            int origineL;
            int origineC;
            // On questionne l'utilisateur des coordonnées de l'origine de l'élément
            // structurant.
            JOptionPane.showMessageDialog(null, "L'element structurant qu'on \nutilise ici est une matrice 3x3", null, JOptionPane.INFORMATION_MESSAGE);
            do {
                origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineL < 0 || origineL > 2);
            do {
                origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineC < 0 || origineC > 2);
            BufferedImage finalImage = Fonctions.dilatation(PicFrame.image, origineL, origineC, 3);
            PicFrame.memorize(finalImage, "Dilatation"  + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[18].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[18].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            int origineL;
            int origineC;
            // On questionne l'utilisateur des coordonnées de l'origine de l'élément
            // structurant.
            JOptionPane.showMessageDialog(null, "L'element structurant qu'on \nutilise ici est une matrice 3x3", null, JOptionPane.INFORMATION_MESSAGE);
            do {
                origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineL < 0 || origineL > 2);
            do {
                origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineC < 0 || origineC > 2);
            BufferedImage finalImage = Fonctions.ouverture(PicFrame.image, origineL, origineC, 3);
            PicFrame.memorize(finalImage, "Ouverture"  + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[19].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[19].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            int origineL;
            int origineC;
            // On questionne l'utilisateur des coordonnées de l'origine de l'élément
            // structurant.
            JOptionPane.showMessageDialog(null, "L'element structurant qu'on \nutilise ici est une matrice 3x3", null, JOptionPane.INFORMATION_MESSAGE);
            do {
                origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineL < 0 || origineL > 2);
            do {
                origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineC < 0 || origineC > 2);
            BufferedImage finalImage = Fonctions.fermeture(PicFrame.image, origineL, origineC, 3);
            PicFrame.memorize(finalImage, "Fermeture"  + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[20].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[20].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            /*Element structurant de l'exemple :
                       1                      
                    0  1  1                   
                    0  0                     
                */
            int [][] elementStructurant = {{2, 0, 2}, {1, 0, 0}, {1, 1, 2}}; //elementStructurant[0] = l'exemple donné
            BufferedImage finalImage = Fonctions.epaississement(PicFrame.image, elementStructurant);
            PicFrame.memorize(finalImage, "Epaississement"  + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[21].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[21].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            int [][] elementStructurant = {{2, 1, 2}, {0, 1, 1}, {0, 0, 2}}; //elementStructurant[0] = l'exemple donné
            BufferedImage finalImage = Fonctions.epaississement(PicFrame.image, elementStructurant);
            PicFrame.memorize(finalImage, "Amincissement"  + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[22].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[22].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            int origineL;
            int origineC;
            // On questionne l'utilisateur des coordonnées de l'origine de l'élément
            // structurant.
            JOptionPane.showMessageDialog(null, "L'element structurant qu'on \nutilise ici est une matrice 3x3", null, JOptionPane.INFORMATION_MESSAGE);
            do {
                origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineL < 0 || origineL > 2);
            do {
                origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineC < 0 || origineC > 2);
            BufferedImage finalImage = Fonctions.topHatOuv(PicFrame.image, origineL, origineC, 3);
            PicFrame.memorize(finalImage, "Top-hat a l'ouverture"  + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });
        btns[23].addActionListener(e ->{
            ImageZoom.removeOtherBorders();
            btns[23].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            PicFrame.image = zoomImagePanel.getImage();
            int seuil = Fonctions.getThresholdOtsu(PicFrame.image);
            int origineL;
            int origineC;
            // On questionne l'utilisateur des coordonnées de l'origine de l'élément
            // structurant.
            JOptionPane.showMessageDialog(null, "L'element structurant qu'on \nutilise ici est une matrice 3x3", null, JOptionPane.INFORMATION_MESSAGE);
            do {
                origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineL < 0 || origineL > 2);
            do {
                origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à 2)", null, JOptionPane.QUESTION_MESSAGE)).intValue();
            } while (origineC < 0 || origineC > 2);
            BufferedImage finalImage = Fonctions.topHatFer(PicFrame.image, origineL, origineC, 3);
            PicFrame.memorize(finalImage, "Top-hat a la fermeture"  + (seuil == 0 ? "" : ", seuil=" + seuil));
            PicFrame.removeAccelerator();
            PicFrame.showPrevious();
        });

    }
}
  
class ImagePanel extends JPanel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    BufferedImage image;
    double scale;
  
    public ImagePanel(){
        loadImage(PicFrame.img);
        scale = .5;
        setBackground(Color.decode("0x114b4b"));
    }
  
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int w = getWidth();
        int h = getHeight();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        double x = (w - scale * imageWidth)/2;
        double y = (h - scale * imageHeight)/2;
        AffineTransform at = AffineTransform.getTranslateInstance(x,y);
        at.scale(scale, scale);
        g2.drawRenderedImage(image, at);
    }
  
    /**
     * For the scroll pane.
     */
    public Dimension getPreferredSize(){
        int w = (int)(scale * image.getWidth());
        int h = (int)(scale * image.getHeight());
        return new Dimension(w, h);
    }
  
    public void setScale(double s){
        scale = s;
        revalidate();
        repaint();
    }
  
    public void loadImage(BufferedImage img){
        image = img;
    }
    public BufferedImage getImage(){
        return this.image;
    }
    public void setImage(BufferedImage image){
        this.image = image;
    }
}
  
class ImageZoom{
    ImagePanel imagePanel;
  
    public ImageZoom(ImagePanel ip){
        imagePanel = ip;
    }
  
    public JPanel getUIPanel(){
        SpinnerNumberModel model = new SpinnerNumberModel(.5, 0.1, 50, .02);
        final JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(45, spinner.getPreferredSize().height));
        spinner.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                float scale = ((Double)spinner.getValue()).floatValue();
                imagePanel.setScale(scale);
            }
        });
        JPanel panel = new JPanel();
        JLabel zoooom = new JLabel("Zoom");
        zoooom.setForeground(Color.WHITE);
        panel.add(zoooom);
        panel.add(spinner);
        panel.setBackground(Color.decode("0x193233"));
        return panel;
    }
    public JPanel getButtonPanel(){
        JPanel panel = new JPanel();
        for(int i = 0 ; i < ZoomTest.btns.length ; i++){
            panel.add(uniformizeSize(ZoomTest.btns[i]));
        }
        panel.setBackground(Color.decode("0x193233"));
        panel.setPreferredSize(new Dimension(160, 0));
        return panel;
    }
    public JButton uniformizeSize(JButton btn){
        btn.setPreferredSize(new Dimension(50, 40));
        //btn.setBorder(new RoundBtn(150));
        btn.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e){
                removeOtherBorders();
                btn.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
            }
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
            public void mouseEntered(MouseEvent e){
            }
            public void mouseExited(MouseEvent e)
            {
            }
        });
        return btn;
    }
    public static void removeOtherBorders(){
        for(int i = 0 ; i < ZoomTest.btns.length ; i++){
            ZoomTest.btns[i].setBorder(BorderFactory.createEmptyBorder());
        }
    }
}
// class RoundBtn implements Border {
//     private int r;
//     RoundBtn(int r) {
//         this.r = r;
//     }
//     public Insets getBorderInsets(Component c) {
//         return new Insets(this.r+1, this.r+1, this.r+2, this.r);
//     }
//     public boolean isBorderOpaque() {
//         return true;
//     }
//     public void paintBorder(Component c, Graphics g, int x, int y, 
//     int width, int height) {
//         g.drawRoundRect(x, y, width-1, height-1, r, r);
//     }
// }