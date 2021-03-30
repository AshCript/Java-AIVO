import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import com.as.func.*;

public class PicFrame extends JFrame{
    /**
     *
     */
    public static Image icon = Toolkit.getDefaultToolkit().getImage("./img/logo/icon.png");
    public static ZoomTest zoomTest;
    public boolean zoomTestOpenOnce = false;
    public File fileBackupPath = new File("backupPath.txt");
    public BufferedReader readBackupPath;
    public FileWriter fwBackupPath;
    public BufferedWriter writeBackupPath;
    public String line;
    public String path;
    private static final long serialVersionUID = 1L;
    public static int memory = 0;
    public static int maxIndex = 0;
    public static int[][][] backupRGB;
    // public static int[][][] backupR;
    // public static int[][][] backupG;
    // public static int[][][] backupB;
    private String fileName;
    private static String[] backupTitle = new String[20];
    private JButton jButtonRefresh = new JButton(" Revenir a l'etat initial de l'image");
    public PicPanel picPanel = new PicPanel();
    public static JMenuBar menuBar = new JMenuBar();
    public static JMenuItem save = new JMenuItem("Enregistrer");
    public static JMenuItem previous = new JMenuItem("Precedent");
    public static JMenuItem next = new JMenuItem("Suivant");
    public static BufferedImage img;
    public static JMenu menu_fichier = new JMenu("Fichier");
    public static JMenu menu_traitement = new JMenu("Traitement");
    public static JMenuItem showHistogram = new JMenuItem("Histogramme");
    public static JMenuItem fichier_findPic = new JMenuItem("Trouver une photo");
    public static JMenuItem fichier_exit = new JMenuItem("Quitter");

    public static JMenu traitement_basic = new JMenu("Basique");
    public static JMenuItem symetrieX = new JMenuItem("Symetrie par rapport a x");
    public static JMenuItem symetrieY = new JMenuItem("Symetrie par rapport a Y");
    public static JMenuItem symetrieO = new JMenuItem("Symetrie par rapport a O");
    public static JMenuItem rotation = new JMenuItem("Rotation");

    public static JMenu traitement_transformation = new JMenu("Transformation");
    public static JMenu transformation_ponctuelle = new JMenu("Ponctuelle");
    public static JMenuItem grayscale = new JMenuItem("Niveau de gris");
    public static JMenuItem inversionGrayscale = new JMenuItem("Inversion de niveau de gris");
    public static JMenuItem inversionColor = new JMenuItem("Inversion de couleur");
    public static JMenu binarisation = new JMenu("Binarisation");
    public static JMenuItem binarisationSeuillage = new JMenuItem("Binarisation par seuillage");
    public static JMenuItem binarisationOtsu = new JMenuItem("Methode d'Otsu");
    public static JMenu operateursFondamentaux = new JMenu("Operateurs fondamentaux");
    public static JMenuItem contour = new JMenuItem("Contour");
    public static JMenuItem etiquetage = new JMenuItem("Etiquetage");
    public static JMenuItem erosion = new JMenuItem("Erosion");
    public static JMenuItem dilatation = new JMenuItem("Dilatation");
    public static JMenuItem ouverture = new JMenuItem("Ouverture");
    public static JMenuItem fermeture = new JMenuItem("Fermeture");
    public static JMenu operateursComplementaires = new JMenu("Operateurs Complementaires");
    public static JMenuItem toutOuRien = new JMenuItem("Tout ou rien");
    public static JMenuItem epaississement = new JMenuItem("Epaississement");
    public static JMenuItem amincissement = new JMenuItem("Amincissement");
    public static JMenuItem topHatOuv = new JMenuItem("Top-hat a l'ouverture");
    public static JMenuItem topHatFer = new JMenuItem("Top-hat a la fermeture");
    public static JMenuItem etalementDynamique = new JMenuItem("Etalement de la dynamique");
    public static JMenu specificationHistogramme = new JMenu("Specification d'histogramme");
    public static JMenuItem egalisationHistogramme = new JMenuItem("Egalisation");
    public static JMenuItem inversionHistogramme = new JMenuItem("Inversion");
    public static JMenu transformation_locale = new JMenu("Locale");
    public static JMenu filtrageLineaire = new JMenu("Filtrage lineaire");
    public static JMenuItem convolutionDiscrete = new JMenuItem("Convolution discrete");
    public static JMenuItem filtreMoyen = new JMenuItem("Lissage par filtre moyen");
    public static JMenuItem filtreGaussien = new JMenuItem("Lissage par filtre gaussien");
    public static JMenu filtrageNonLineaire = new JMenu("Filtrage non lineaire");
    public static JMenuItem lissageConservateur = new JMenuItem("Lissage conservateur");
    public static JMenuItem filtreMedian = new JMenuItem("Filtre Median");
    public static JMenu transformation_globale = new JMenu("Globale");
    // ImageView vue = new ImageView();
    public static BufferedImage image;
    public PicFrame(){
        try {
            try{
                if(!fileBackupPath.exists())
                    fileBackupPath.createNewFile();
                readBackupPath = new BufferedReader(new FileReader(fileBackupPath));
                if((line = readBackupPath.readLine()) != null)
                    path = line;
                else
                    path = System.getProperty("user.home") + "\\Pictures";
            }catch(FileNotFoundException fnf){}
            
            fichier_findPic.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
            fichier_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
            showHistogram.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK));
            save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
            previous.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
            next.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
            
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.setLayout(new BorderLayout());
            this.setTitle("Traitement d'image");

            int numPic = (int)(Math.random()*10);
            System.out.println(numPic);
            // Récupération de l'image dans BufferedImage
            image = ImageIO.read(new File("./img/logo/" + numPic + ".jpg"));
            // BufferedImage image2 = ImageIO.read(new File("./" + nomPic));
            menu_traitement.setVisible(false);
            showHistogram.setVisible(false);
            // Ajout de l'image dans le pannel de photo
            picPanel.setImage(image);
            // vue.picPanel.setImage(image2);
            // vue.setVisible(false);
            // Les menus
            menu_fichier.add(fichier_findPic);
            menu_fichier.add(previous);
            menu_fichier.add(next);
            menu_fichier.add(save);
            save.setVisible(false);
            previous.setVisible(false);
            next.setVisible(false);
            menu_fichier.add(showHistogram);
            menu_fichier.add(fichier_exit);
            // Dans traitement basique :
            traitement_basic.add(symetrieX);
            traitement_basic.add(symetrieY);
            traitement_basic.add(symetrieO);
            traitement_basic.add(rotation);
            // dans transformation ponctuelle : couleur vers grayscale, inversion de niveau
            // de gris, etalement de la dynamique, spécification d'histogramme
            specificationHistogramme.add(egalisationHistogramme);
            specificationHistogramme.add(inversionHistogramme);
            transformation_ponctuelle.add(grayscale);
            transformation_ponctuelle.add(inversionGrayscale);
            transformation_ponctuelle.add(inversionColor);
            binarisation.add(binarisationSeuillage);
            binarisation.add(binarisationOtsu);
            binarisation.add(contour);
            binarisation.add(etiquetage);
            operateursFondamentaux.add(erosion);
            operateursFondamentaux.add(dilatation);
            operateursFondamentaux.add(ouverture);
            operateursFondamentaux.add(fermeture);
            operateursComplementaires.add(toutOuRien);
            operateursComplementaires.add(epaississement);
            operateursComplementaires.add(amincissement);
            operateursComplementaires.add(topHatOuv);
            operateursComplementaires.add(topHatFer);
            binarisation.add(operateursFondamentaux);
            binarisation.add(operateursComplementaires);
            transformation_ponctuelle.add(binarisation);
            transformation_ponctuelle.add(etalementDynamique);
            transformation_ponctuelle.add(specificationHistogramme);
            // dans transformation locale : Filtrage linéaire, lissage[par filtrage moyen ou
            // par filtrage gaussien]
            filtrageLineaire.add(convolutionDiscrete);
            filtrageLineaire.add(filtreMoyen);
            filtrageLineaire.add(filtreGaussien);
            filtrageNonLineaire.add(lissageConservateur);
            filtrageNonLineaire.add(filtreMedian);
            transformation_locale.add(filtrageLineaire);
            transformation_locale.add(filtrageNonLineaire);
            traitement_transformation.add(transformation_ponctuelle);
            traitement_transformation.add(transformation_locale);
            traitement_transformation.add(transformation_globale);
            menu_traitement.add(traitement_basic);
            menu_traitement.add(traitement_transformation);
            menuBar.add(menu_fichier);
            menuBar.add(menu_traitement);
            
            menu_fichier.setFont(new Font(Font.DIALOG, 12, 12));
            menu_traitement.setFont(new Font(Font.DIALOG, 12, 12));
            showHistogram.setFont(new Font(Font.DIALOG, 12, 12));
            fichier_findPic.setFont(new Font(Font.DIALOG, 12, 12));
            
            menuBar.setBackground(Color.decode("0x114b4b"));
            menu_fichier.setForeground(Color.decode("0x4ddbee"));
            menu_traitement.setForeground(Color.decode("0x4ddbee"));
            jButtonRefresh.setBackground(Color.decode("0x21aabd"));
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int appWidth = screenSize.width;
            int appHeight = screenSize.height;
            this.add(picPanel, BorderLayout.CENTER);
            this.setIconImage(icon);
            this.setBounds(appWidth - appWidth*3/4, appHeight - appHeight*4/5, 700, 450);
            this.setResizable(false);
            this.setJMenuBar(menuBar);
            this.setVisible(true);
            // if(Desktop.getDesktop().isSupported(Desktop.Action.OPEN)){
            //     try {
            //         Desktop.getDesktop().open(new File(System.getProperty("user.home")));
            //     } catch (Exception e) {
            //     }
            // }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(path));
            fichier_findPic.addActionListener(e -> {
                try {
                    memory = 0;
                    fileChooser.setCurrentDirectory(fileChooser.getCurrentDirectory());
                    fwBackupPath = new FileWriter(fileBackupPath.getAbsolutePath());
                    writeBackupPath = new BufferedWriter(fwBackupPath);
                    save.setVisible(true);
                    save.setEnabled(false);
                    if(fileChooser.showOpenDialog(PicFrame.this) == JFileChooser.APPROVE_OPTION){
                        // String fc = fileChooser.getSelectedFile() + "";
                        // String fileExtension = fc.substring(fc.lastIndexOf(".") + 1);
                        // if(fileExtension.toUpperCase() != ".JPG" || fileExtension.toUpperCase() != ".JPEG" || fileExtension.toUpperCase() != ".PNG"){
                        //     throw new Exception();
                        // }
                        image = ImageIO.read(fileChooser.getSelectedFile());
                        fileName = fileChooser.getSelectedFile().getName();
                        System.out.println(image.getWidth() + " x " + image.getHeight());
                        BufferedImage imageFinal = ImageIO.read(fileChooser.getSelectedFile());
                        img = imageFinal;
                        imageFinal.setRGB(0, 0, 0);
                        if(image.getWidth() * image.getHeight() > 2048 * 1535){
                            JOptionPane.showMessageDialog(null, "La taille de l'image est tres grande (" + image.getWidth() + " x " + image.getHeight() + "). \n Le logiciel risque de mal fonctionner.", null, JOptionPane.INFORMATION_MESSAGE);
                            backupRGB = new int[5][image.getWidth()][image.getHeight()];
                        }else{
                            backupRGB = new int[20][image.getWidth()][image.getHeight()];
                        }
                        this.setVisible(false);
                        // backupR = new int[20][image.getWidth()][image.getHeight()];
                        // backupG = new int[20][image.getWidth()][image.getHeight()];
                        // backupB = new int[20][image.getWidth()][image.getHeight()];

                        menu_traitement.setVisible(true);
                        showHistogram.setVisible(true);
                            
                        save.setVisible(true);
                        save.setEnabled(false);
                        previous.setVisible(false);
                        next.setVisible(false);
                        
                        this.setTitle(fileName + "(" + image.getWidth() + " x " + image.getHeight()  + ")");
                    }
                    try{
                        writeBackupPath.write("" + fileChooser.getCurrentDirectory());
                        writeBackupPath.close();
                    }catch(IOException ioe){
                        System.out.println("error while writing backupPath.txt");
                    }
                    ImageZoom.removeOtherBorders();
                } catch (Exception e0) {}
                this.setVisible(false);
                if(!zoomTestOpenOnce)
                    zoomTest = new ZoomTest();
                zoomTestOpenOnce = true;
                PicFrame.menu_traitement.setVisible(true);
                zoomTest.f.setTitle(fileName + " (" + image.getWidth() + " x " + image.getHeight()  + ")");
                zoomTest.f.setJMenuBar(menuBar);
                zoomTest.zoomImagePanel.loadImage(img);
                zoomTest.zoomImagePanel.repaint();
                backupTitle[memory] = zoomTest.f.getTitle();
                for (int i = 0; i < image.getWidth(); i++) {
                    for (int j = 0; j < image.getHeight(); j++) {
                        backupRGB[memory][i][j] = zoomTest.zoomImagePanel.getImage().getRGB(i, j);
                    }
                }
                memory++;
            });
            save.addActionListener(e -> {
                BufferedImage image = zoomTest.zoomImagePanel.getImage();
                try {
                    if(fileChooser.showSaveDialog(PicFrame.this) == JFileChooser.APPROVE_OPTION){
                        File destination = new File(fileChooser.getSelectedFile() + "");
                        //String s = destination + "";
                        //destination = s.indexOf('.') != -1 ? new File(s.substring(0, s.indexOf('.')) + ".jpg") : destination;
                        System.out.println(destination);
                        ImageIO.write(image, "jpg", destination);
                        JOptionPane.showMessageDialog(null, "L'image est sauvegardee avec succes!\n" + destination, null, JOptionPane.DEFAULT_OPTION);
                    }
                } catch (Exception e1) {}
            });
            previous.addActionListener(e -> {
                if (memory != 0) {
                    memory--;
                    int current = memory;
                    int prev = current - 1;

                    BufferedImage previousImage = image;
                    for (int i = 0; i < image.getWidth(); i++) {
                        for (int j = 0; j < image.getHeight(); j++) {
                            previousImage.setRGB(i, j, backupRGB[prev][i][j]);
                        }
                    }
                    zoomTest.zoomImagePanel.setImage(previousImage);
                    zoomTest.zoomImagePanel.repaint();
                    next.setEnabled(true);
                    if (memory == 1) {
                        previous.setEnabled(false);
                        save.setEnabled(false);
                    }
                    zoomTest.f.setTitle(backupTitle[prev]);
                }
            });
            next.addActionListener(e -> {
                if (memory <= maxIndex) {
                    int current = memory;
                    int nxt = current;
                    memory++;
                    BufferedImage nextImage = zoomTest.zoomImagePanel.getImage();
                    for (int i = 0; i < image.getWidth(); i++) {
                        for (int j = 0; j < image.getHeight(); j++) {
                            nextImage.setRGB(i, j, backupRGB[nxt][i][j]);
                        }
                        zoomTest.zoomImagePanel.setImage(nextImage);
                        zoomTest.zoomImagePanel.repaint();
                    }
                    previous.setEnabled(true);
                    save.setEnabled(true);
                    if (memory > maxIndex) {
                        next.setEnabled(false);
                    }
                    zoomTest.f.setTitle(backupTitle[nxt]);
                }
            });
            fichier_exit.addActionListener(e ->{
                System.exit(0);
            });
            showHistogram.addActionListener(e ->{
                //JOptionPane.showMessageDialog(null, "Lancement du creation de l'histogramme", null, JOptionPane.INFORMATION_MESSAGE);
                Fonctions.generateHistogramme(image, backupTitle[memory-1]);
                //new HistogramFrame();
                //JOptionPane.showMessageDialog(null, "Creation de l'histogramme terminee!", null, JOptionPane.INFORMATION_MESSAGE);
            });
            binarisationSeuillage.addActionListener(e -> {
                try{
                    image = zoomTest.zoomImagePanel.getImage();
                    int seuil;
                    do{
                        String message_seuil = JOptionPane.showInputDialog(null, "Inerez le seuil (0 ... 255)", null,
                                JOptionPane.QUESTION_MESSAGE);
                        seuil = Integer.valueOf(message_seuil).intValue();
                    }while(seuil < 0 || seuil > 255);
    
                    BufferedImage finalImage = Fonctions.binarisationSeuillage(image, seuil);
                    memorize(finalImage, "Binarisation par seuillage, seuil=" + seuil);
                    removeAccelerator();
                    binarisationSeuillage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                    showPrevious();
                }catch(NumberFormatException ee){
                    JOptionPane.showMessageDialog(null, "Oups! Une erreur s'est produite lors de l'insertion de valeur", null,
                                JOptionPane.INFORMATION_MESSAGE);
                }catch(Exception ee){}
            });
            binarisationOtsu.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                int seuil = Fonctions.getThresholdOtsu(image);
                BufferedImage finalImage = Fonctions.binarisationOtsu(image);
                memorize(finalImage, "Methode d'Otsu" + (seuil == 0 ? "" : ", seuil=" + seuil));
                removeAccelerator();
                binarisationOtsu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            
            contour.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = image;
                int seuil = Fonctions.getThresholdOtsu(image);
                String[] contourType = {"Contour en utilisant l'erosion", "Algorithme de contour personnel", "Algorithme de tortue de Papert"};
                String message = "Quel algorithme de contour voulez-vous utiliser?";
                for(int i = 0 ; i < contourType.length ; i++){
                    message += "\n" + (i+1) + "- " + contourType[i];
                }
                int contourUsed = 0;
                try{
                    do{
                        contourUsed = Integer.valueOf(JOptionPane.showInputDialog(null, message, null,
                    JOptionPane.QUESTION_MESSAGE)).intValue();
                    }while(contourUsed > contourType.length - 1 && contourUsed <= 0);
                // On fixe l'élément structurant au centre pour le calcul automatique de l'érosion et donc du contour.
                }catch(Exception e5){}
                if(contourUsed == 1)
                    finalImage = Fonctions.contourByErosion(image);
                else if(contourUsed == 2)
                    finalImage = Fonctions.contourByPersonalAlgorithm(image);
                else if(contourUsed == 3)
                    finalImage = Fonctions.contourByPapertTurtle(image);
                memorize(finalImage, contourType[contourUsed-1] + (seuil == 0 ? "" : ", seuil=" + seuil));
                removeAccelerator();
                contour.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            etiquetage.addActionListener(e ->{
                image = zoomTest.zoomImagePanel.getImage();

                int seuil = Fonctions.getThresholdOtsu(image);
                // On fixe l'élément structurant au centre pour le calcul automatique de l'érosion et donc du contour.
                int nombreObjets = Fonctions.objectsNumber(image);
                BufferedImage finalImage = Fonctions.etiquetage(image);
                memorize(finalImage, "Etiquetage"  + (seuil == 0 ? "" : ", seuil=" + seuil) + ", Objets : " + nombreObjets);
                removeAccelerator();
                etiquetage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            erosion.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                int tailleElementStructurant;
                int origineL;
                int origineC;
                // On questionne l'utilisateur sur la taille et les coordonnées de l'origine de l'élément
                // structurant.
                do {
                    tailleElementStructurant = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la taille de l'element structurant", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (tailleElementStructurant == 0);
                do {
                    origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 a "+ tailleElementStructurant + ")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineL < 0 || origineL >= tailleElementStructurant);
                do {
                    origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 a "+ tailleElementStructurant + ")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineC < 0 || origineC >= tailleElementStructurant);
                
                int seuil = Fonctions.getThresholdOtsu(image);
                BufferedImage finalImage = Fonctions.erosion(image, origineL, origineC, tailleElementStructurant);
                memorize(finalImage, "Erosion" + (seuil == 0 ? "" : ", seuil=" + seuil));
                removeAccelerator();
                erosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            dilatation.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                int tailleElementStructurant;
                int origineL;
                int origineC;

                // On questionne l'utilisateur des coordonnées de l'origine de l'élément
                // structurant.
                do {
                    tailleElementStructurant = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la taille de l'element structurant", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (tailleElementStructurant == 0);
                do {
                    origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 a " + tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineL < 0 || origineL >= tailleElementStructurant);
                do {
                    origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à "+ tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineC < 0 || origineC >= tailleElementStructurant);
                
                int seuil = Fonctions.getThresholdOtsu(image);
                BufferedImage finalImage = Fonctions.dilatation(image, origineL, origineC, tailleElementStructurant);
                memorize(finalImage, "Dilatation" + (seuil == 0 ? "" : ", seuil=" + seuil));
                removeAccelerator();
                dilatation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            ouverture.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                int tailleElementStructurant;
                int origineL;
                int origineC;
                // On questionne l'utilisateur des coordonnées de l'origine de l'élément
                // structurant.
                do {
                    tailleElementStructurant = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la taille de l'element structurant", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (tailleElementStructurant == 0);
                do {
                    origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 a " + tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineL < 0 || origineL >= tailleElementStructurant);
                do {
                    origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à "+ tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineC < 0 || origineC >= tailleElementStructurant);
                int seuil = Fonctions.getThresholdOtsu(image);
                BufferedImage finalImage = Fonctions.ouverture(image, origineL, origineC, tailleElementStructurant);
                memorize(finalImage, "Ouverture" + (seuil == 0 ? "" : ", seuil=" + seuil));
                removeAccelerator();
                ouverture.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            fermeture.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                int tailleElementStructurant;
                int origineL;
                int origineC;
                // On questionne l'utilisateur des coordonnées de l'origine de l'élément
                do {
                    tailleElementStructurant = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la taille de l'element structurant", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (tailleElementStructurant == 0);
                do {
                    origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 a " + tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineL < 0 || origineL >= tailleElementStructurant);
                do {
                    origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à "+ tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineC < 0 || origineC >= tailleElementStructurant);
                int seuil = Fonctions.getThresholdOtsu(image);
                BufferedImage finalImage = Fonctions.fermeture(image, origineL, origineC, tailleElementStructurant);
                memorize(finalImage, "Fermeture" + (seuil == 0 ? "" : ", seuil=" + seuil));
                removeAccelerator();
                fermeture.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            toutOuRien.addActionListener(e -> {
                /*
                 * TRANSFORMATION EN NOIR ET BLANC DE L'IMAGE, EN UTILISANT LA METHODE D'OTSU
                 */
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage;
                /*Element structurant de l'exemple :
                       1                      
                    0  1  1                   
                    0  0                     
                */
                int [][][] elementStructurant = {
                    {{2, 0, 2}, {1, 0, 0}, {1, 1, 2}}, //elementStructurant[0] = l'exemple donné.
                };
                finalImage = Fonctions.toutOuRien(image, elementStructurant[0]);
                memorize(finalImage, "Tout ou Rien");
                removeAccelerator();
                toutOuRien.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            epaississement.addActionListener(e -> {
                /*
                 * TRANSFORMATION EN NOIR ET BLANC DE L'IMAGE, EN UTILISANT LA METHODE D'OTSU
                 */
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage;
                /*Element structurant de l'exemple :
                       1                      
                    0  1  1                   
                    0  0                     
                */
                int [][] elementStructurant = {{2, 0, 2}, {1, 0, 0}, {1, 1, 2}}; //elementStructurant[0] = l'exemple donné
                finalImage = Fonctions.epaississement(image, elementStructurant);
                memorize(finalImage, "Epaississement");
                removeAccelerator();
                epaississement.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            amincissement.addActionListener(e -> {
                /*
                 * TRANSFORMATION EN NOIR ET BLANC DE L'IMAGE, EN UTILISANT LA METHODE D'OTSU
                 */
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage;

                int [][][] elementStructurant = {
                    {{2, 1, 2}, {0, 1, 1}, {0, 0, 2}}, //elementStructurant[0] = l'exemple donné
                };
                finalImage = Fonctions.epaississement(image, elementStructurant[0]);
                memorize(finalImage, "Amincissement");
                removeAccelerator();
                amincissement.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            topHatOuv.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                int tailleElementStructurant;
                int origineL;
                int origineC;
                // On questionne l'utilisateur des coordonnées de l'origine de l'élément
                // structurant.
                do {
                    tailleElementStructurant = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la taille de l'element structurant", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (tailleElementStructurant == 0);
                do {
                    origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 a " + tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineL < 0 || origineL >= tailleElementStructurant);
                do {
                    origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à "+ tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineC < 0 || origineC >= tailleElementStructurant);
                BufferedImage finalImage = Fonctions.topHatOuv(image, origineL, origineC, tailleElementStructurant);
                memorize(finalImage, "Top-hat a l'ouverture");
                removeAccelerator();
                topHatOuv.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            topHatFer.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                int tailleElementStructurant;
                int origineL;
                int origineC;
                // On questionne l'utilisateur des coordonnées de l'origine de l'élément
                // structurant.
                do {
                    tailleElementStructurant = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la taille de l'element structurant", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (tailleElementStructurant == 0);
                do {
                    origineL = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee x de \nl'origine de l'element structurant (0 a " + tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineL < 0 || origineL >= tailleElementStructurant);
                do {
                    origineC = Integer.valueOf(JOptionPane.showInputDialog(null, "Indiques la coordonnee Y de \nl'origine de l'element structurant (0 à "+ tailleElementStructurant +")", null, JOptionPane.QUESTION_MESSAGE)).intValue();
                } while (origineC < 0 || origineC >= tailleElementStructurant);
                BufferedImage finalImage = Fonctions.topHatFer(image, origineL, origineC, tailleElementStructurant);
                memorize(finalImage, "Top-hat a la fermeture");
                removeAccelerator();
                topHatFer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            etalementDynamique.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
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
                    BufferedImage finalImage = Fonctions.etalementDynamique(image, m, M);
                    memorize(finalImage, "Etalement de la dynamique, m=" + m + " M=" + M);
                    removeAccelerator();
                    etalementDynamique.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                    showPrevious();
                }catch(NumberFormatException ee){
                    JOptionPane.showMessageDialog(null, "Oups! Une erreur s'est produite lors de l'insertion de valeur", null,
                                JOptionPane.INFORMATION_MESSAGE);
                }catch(Exception e1){}
            });
            grayscale.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.grayscale(image);
                memorize(finalImage, "Niveau de gris");
                removeAccelerator();
                grayscale.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            inversionGrayscale.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.inversionGrayscale(image);
                memorize(finalImage, "Inversion de niveau de gris");
                removeAccelerator();
                inversionGrayscale.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            inversionColor.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.inversionColor(image);
                memorize(finalImage, "Image inversee");
                removeAccelerator();
                inversionColor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            symetrieY.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.symetrieY(image);
                memorize(finalImage, "Symetrie par rapport a Y'OY");
                removeAccelerator();
                symetrieY.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            symetrieX.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.symetrieX(image);
                memorize(finalImage, "Symetrie par rapport a X'OX");
                removeAccelerator();
                symetrieX.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            symetrieO.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.symetrieO(image);
                memorize(finalImage, "Symetrie par rapport a O");
                removeAccelerator();
                symetrieO.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            egalisationHistogramme.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.egalisationHistogramme(image);
                memorize(finalImage, "Egalisation d'histogramme");
                removeAccelerator();
                egalisationHistogramme.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            inversionHistogramme.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.inversionHistogramme(image);
                memorize(finalImage, "Inversion d'histogramme");
                removeAccelerator();
                inversionHistogramme.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            convolutionDiscrete.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage;
                /*Filtre de l'exemple :         Filtre de Sobel 1 :          Filtre de Rehaussement          Filtre d'accentuation
                    0.5  0.5  0.0                   -1.0  0.0  1.0                  0.0 -1.0  0.0                   0.0 -0.5  0.0
                    0.5  0.0 -0.5                   -2.0  0.0  2.0                 -1.0  5.0 -1.0                  -0.5  3.0 -0.5
                    0.0 -0.5 -1.0                   -1.0  0.0  1.0                  0.0 -1.0  0.0                   0.0 -0.5  0.0
                    
                    Filtre Laplacien :         Filtre de Sobel 2 (1980) :
                    0.0  1.0  0.0                   -1.0 -2.0 -1.0
                    1.0 -4.0  1.0                    0.0  0.0  0.0
                    0.0  1.0  0.0                    1.0  2.0  1.0
                */
                float [][][] filters = {
                    {{0.5f, 0.5f, 0.0f }, {0.5f, 0f, -0.5f}, {0f, -0.5f, -1f}}, //filters[0] = l'exemple donné
                    {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}}, //filters[1] = Filtre de Sobel 1
                    {{-1f, 0f, 1f}, {-2f, 0f, 2f}, {-1f, 0f, 1f}}, //filters[2] = Filtre de Sobel 1980
                    {{-0.5f, -0.5f, 0.5f}, {-1, 0, 1}, {-0.5f, -0.5f, 0.5f}}, //filters[3] = Filtre de convolution perso 1
                    {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}}, //filters[4] = Filtre perso 2
                    {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, //filters[5] = Filtre perso 3
                    {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}}, //filters[6] = Filtre de Rehaussement
                    {{0.0f, -0.5f, 0.0f}, {-0.5f, 3.0f, -0.5f}, {0.0f, -0.5f, 0.0f}}, //filters[7] = Filtre d'accentuation
                    {{0, 1, 0}, {1, -4, 1}, {0, 1, 0}} //filters[8] = Filtre Laplacien

                };
                String[] filterName = {"Filtre de l'exemple", "Filtre de Sobel 1", "Filtre de Sobel 1980", "Filtre perso", "Filtre perso 2", "Filtre perso 3", "Filtre de Rehaussement", "Filtre d'accentuation", "Filtre Laplacien"};
                String message = "Quel filtre voulez-vous utiliser?";
                for(int i = 0 ; i < filterName.length ; i++){
                    message += "\n" + (i+1) + "- " + filterName[i];
                }
                int filterUsed;
                try{
                    do{
                        filterUsed = Integer.valueOf(JOptionPane.showInputDialog(null, message, null,
                    JOptionPane.QUESTION_MESSAGE)).intValue();
                    }while(filterUsed > filterName.length - 1 && filterUsed <= 0);
                    finalImage = Fonctions.linearFilter(image, filters[filterUsed - 1]);     
                    memorize(finalImage, "Convolution discrete : " + filterName[filterUsed - 1]);
                    removeAccelerator();
                    convolutionDiscrete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                    showPrevious();
                }catch(Exception e2){}
            });
            filtreMoyen.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                /*Filtre moyen 3x3:
                          ( 1   1   1 )
                    1/9 x ( 1   1   1 )
                          ( 1   1   1 )
                */
                float[][] meanFilter = new float[3][3];
                for (int i = 0; i < meanFilter.length; i++) {
                    for (int j = 0; j < meanFilter[0].length; j++) {
                        meanFilter[i][j] = 1f / ((float) Math.pow((float) meanFilter.length, 2));
                    }
                }
                BufferedImage finalImage = Fonctions.linearFilter(image, meanFilter);
                memorize(finalImage, "Filtre moyen");
                removeAccelerator();
                filtreMoyen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            filtreGaussien.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                /*Filtre Gaussien 3x3 :
                          ( 1   2   1 )
                   1/16 x ( 2   4   2 )
                          ( 1   2   1 )
                */
                float[][] gaussianFilter = {{1f, 2f, 1f}, {2f, 4f, 2f}, {1f, 2f, 1f}};
                float sommeGauss = 0;
                for (int i = 0; i < gaussianFilter.length; i++) {
                    for (int j = 0; j < gaussianFilter.length; j++) {
                        sommeGauss += gaussianFilter[i][j];
                    }
                }
                for (int i = 0; i < gaussianFilter.length; i++) {
                    for (int j = 0; j < gaussianFilter.length; j++) {
                        gaussianFilter[i][j] /= sommeGauss;
                    }
                }
                BufferedImage finalImage = Fonctions.linearFilter(image, gaussianFilter);
                memorize(finalImage, "Filtre Gaussien");
                removeAccelerator();
                filtreGaussien.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            lissageConservateur.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.lissageConservateur(image);
                showPrevious();
                memorize(finalImage, "Lissage Conservateur");
                removeAccelerator();
                lissageConservateur.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
            filtreMedian.addActionListener(e -> {
                image = zoomTest.zoomImagePanel.getImage();
                BufferedImage finalImage = Fonctions.filtreMedian(image);
                memorize(finalImage, "Filtre Median");
                removeAccelerator();
                filtreMedian.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
                showPrevious();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void showPrevious(){
        previous.setVisible(true);
        previous.setEnabled(true);
        save.setEnabled(true);
        next.setVisible(true);
        next.setEnabled(false);
    }
    public static void memorize(BufferedImage image, String title) {
        zoomTest.zoomImagePanel.setImage(image);
        zoomTest.zoomImagePanel.repaint();
        zoomTest.f.setTitle(title);
        backupTitle[memory] = title;
        if (memory <= 19) {
            img = image;
            if (memory == 19) {
                for (int i = 0; i < memory; i++) {
                    backupRGB[i] = backupRGB[i+1];
                    // backupR[i] = backupR[i + 1];
                    // backupG[i] = backupG[i + 1];
                    // backupB[i] = backupB[i + 1];
                    backupTitle[i] = backupTitle[i + 1];
                }
            }
            for(int i = 0 ; i < image.getWidth() ; i++){
                for(int j = 0 ; j < image.getHeight() ; j++){
                    backupRGB[memory][i][j] = img.getRGB(i, j);
                    // backupR[memory][i][j] = new Color(image.getRGB(i, j)).getRed();
                    // backupG[memory][i][j] = new Color(image.getRGB(i, j)).getGreen();
                    // backupB[memory][i][j] = new Color(image.getRGB(i, j)).getBlue();
                }
            }
            
            if(memory != 19){   
                maxIndex = memory;
                memory++;
            }
        }
    }
    public static void removeAccelerator(){
        symetrieX.setAccelerator(null);
        symetrieY.setAccelerator(null);
        symetrieO.setAccelerator(null);
        rotation.setAccelerator(null);
        grayscale.setAccelerator(null);
        inversionGrayscale.setAccelerator(null);
        inversionColor.setAccelerator(null);
        binarisationSeuillage.setAccelerator(null);
        binarisationOtsu.setAccelerator(null);
        contour.setAccelerator(null);
        etiquetage.setAccelerator(null);
        erosion.setAccelerator(null);
        dilatation.setAccelerator(null);
        ouverture.setAccelerator(null);
        fermeture.setAccelerator(null);
        toutOuRien.setAccelerator(null);
        epaississement.setAccelerator(null);
        amincissement.setAccelerator(null);
        topHatOuv.setAccelerator(null);
        topHatFer.setAccelerator(null);
        etalementDynamique.setAccelerator(null);
        egalisationHistogramme.setAccelerator(null);
        inversionHistogramme.setAccelerator(null);
        convolutionDiscrete.setAccelerator(null);
        filtreMoyen.setAccelerator(null);
        filtreGaussien.setAccelerator(null);
        lissageConservateur.setAccelerator(null);
        filtreMedian.setAccelerator(null);
    }
}