package com.as.func;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.imageio.*;

public class Fonctions {
  static int pixelMax = 256;

  // Binarisation par seuillage
  public static BufferedImage binarisationSeuillage(BufferedImage image, int seuil){
    BufferedImage output = image;
    boolean binary = true;
    /*
     * Binarisation par seuillage : Transformation en noir et blanc
     */
    // Tester si l'image est déjà binaire. Si oui, alors on ne l'expose plus à la
    // methode de binarisation, sinon, on utilise la binarisation par seuillage
    for (int u = 0; u < image.getWidth(); u++) {
      for (int v = 0; v < image.getHeight(); v++) {
        if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
        && (new Color(image.getRGB(u, v)).getGreen() != 0
        && new Color(image.getRGB(u, v)).getGreen() != 255)
        && (new Color(image.getRGB(u, v)).getBlue() != 0
        && new Color(image.getRGB(u, v)).getBlue() != 255)) {
          binary = false;
            break;
        }
      }
      if (!binary)
        break;
    }
    if (!binary) {
      for (int i = 0; i < image.getWidth(); i++) {
        for (int j = 0; j < image.getHeight(); j++) {
          Color pixelColor = new Color(image.getRGB(i, j));
          int r = pixelColor.getRed();
          int g = pixelColor.getGreen();
          int b = pixelColor.getBlue();
          int middleColor = (r + g + b) / 3;
          // int newColor = new Color(r, g, b).getRGB();
          // Exemple : seuil = 100
          if (middleColor < seuil) {
            r = g = b = 0;
            output.setRGB(i, j, new Color(r, g, b).getRGB());
          } else {
            r = g = b = pixelMax - 1;
            output.setRGB(i, j, new Color(r, g, b).getRGB());
          }
        }
  	  }
      return output;
    }
    JOptionPane.showMessageDialog(null, "L'image est deja binarisee", null, JOptionPane.INFORMATION_MESSAGE);
    return output;
  }

  // Binarisation par la methode d'Otsu
  public static BufferedImage binarisationOtsu(BufferedImage image) {
  	BufferedImage output = image;
      boolean binary = true;
      // Tester si l'image est déjà binaire. Si oui, alors on ne l'expose plus à la
      // methode Otsu, sinon, on applique la methode d'Otsu.
      for (int u = 0; u < image.getWidth(); u++) {
        for (int v = 0; v < image.getHeight(); v++) {
          if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
          && (new Color(image.getRGB(u, v)).getGreen() != 0
          && new Color(image.getRGB(u, v)).getGreen() != 255)
          && (new Color(image.getRGB(u, v)).getBlue() != 0
          && new Color(image.getRGB(u, v)).getBlue() != 255)) {
            binary = false;
            break;
          }
        }
        if (!binary)
          break;
    	}
      if (!binary) {
      // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
            // variable pour la récupération initiale et finale
            int[][] initialColor = new int[image.getWidth()][image.getHeight()];
            int[][] finalColor = new int[image.getWidth()][image.getHeight()];

            // Vu que après transformation en niveau de gris, la valeur de Rouge, de Vert et
            // de Bleu de l'un pixel de l'image soit egale,
            // alors on peut utiliser un même histogramme pour toutes ses 3 couleurs.
            int[] histogram = new int[pixelMax];
            double[] varianceInterclasses = new double[pixelMax];
            double moy0, moy1, proba0, proba1;
            int seuil = 0;
            double maxVar = 0;
            /*
             * Binarisation par la methode d'Otsu (Calcul automatique du seuil):
             * Transformation en noir et blanc
             */
            // Transformation de l'image en niveau de gris
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    Color pixelColor = new Color(image.getRGB(i, j));
                    int red = pixelColor.getRed();
                    int green = pixelColor.getGreen();
                    int blue = pixelColor.getBlue();
                    initialColor[i][j] = (red + green + blue) / 3;
                    image.setRGB(i, j, new Color(initialColor[i][j], initialColor[i][j], initialColor[i][j]).getRGB());
                }
            }
            // Calcul de l'histogramme de chaque niveau de gris
            // en niveau de gris, r = g = b, alors, on peut prendre r (le rouge) comme
            // reference
            histogram = histogramme(image, "red");
            // On pose N = la somme de l'histogramme, c'est à dire, le nombre total de
            // d'occurrence d'un niveau de couleur dans l'image

            int N = 0;
            for (int k = 0; k < pixelMax; k++) {
                N += histogram[k];
            }
            // Calcul de la variance inter-classe

            for (int i = 0; i < pixelMax; i++) {
                proba0 = 0;
                moy0 = 0;
                proba1 = 0;
                moy1 = 0;
                // Calcul de la première classe
                for (int j = 0; j <= i; j++) {
                    proba0 += histogram[j];
                    moy0 += j * histogram[j];
                }
                proba0 /= N;
                moy0 /= proba0;

                // Calcul de la deuxième classe
                for (int j = i + 1; j < pixelMax; j++) {
                    proba1 += histogram[j];
                    moy1 += j * histogram[j];
                }
                proba1 /= N;
                moy1 /= proba1;

                varianceInterclasses[i] = proba0 * proba1 * Math.pow((moy1 - moy0), 2);
            }
            // Détermination de la seuil

            // Détermination de la variance inter-classe la plus élevée

            for (int i = 0; i < pixelMax; i++) {
                if (varianceInterclasses[i] > maxVar) {
                    seuil = i;
                    maxVar = varianceInterclasses[i];
                }
            }
            // Transformation finale
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    if (initialColor[i][j] < seuil) {
                        finalColor[i][j] = 0;
                    } else {
                        finalColor[i][j] = pixelMax - 1;
                    }
                    output.setRGB(i, j, new Color(finalColor[i][j], finalColor[i][j], finalColor[i][j]).getRGB());
                }
            }
            return output;
        }
        JOptionPane.showMessageDialog(null, "L'image est deja binarisee", null, JOptionPane.INFORMATION_MESSAGE);
        return output;
    }

    // Retourne le seuil;
    public static int getThresholdOtsu(BufferedImage image) {
        int seuil = 0;
        boolean binary = true;
        // Tester si l'image est déjà binaire. Si oui, alors on ne l'expose plus à la
        // methode Otsu, sinon, on applique la methode d'Otsu.
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255) && (new Color(image.getRGB(u, v)).getGreen() != 0 && new Color(image.getRGB(u, v)).getGreen() != 255) && (new Color(image.getRGB(u, v)).getBlue() != 0 && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
            // variable pour la récupération initiale et finale
            int[][] initialColor = new int[image.getWidth()][image.getHeight()];

            // Vu que après transformation en niveau de gris, la valeur de Rouge, de Vert et
            // de Bleu de l'un pixel de l'image soit egale,
            // alors on peut utiliser un même histogramme pour toutes ses 3 couleurs.
            int[] histogram = new int[pixelMax];
            double[] varianceInterclasses = new double[pixelMax];
            double moy0, moy1, proba0, proba1;
            seuil = 0;
            double maxVar = 0;
            /*
             * Binarisation par la methode d'Otsu (Calcul automatique du seuil):
             * Transformation en noir et blanc
             */
            // Transformation de l'image en niveau de gris
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    Color pixelColor = new Color(image.getRGB(i, j));
                    int red = pixelColor.getRed();
                    int green = pixelColor.getGreen();
                    int blue = pixelColor.getBlue();
                    initialColor[i][j] = (red + green + blue) / 3;
                }
            }
            // en niveau de gris, r = g = b, alors, on peut prendre r (le rouge) comme
            // reference
            histogram = histogramme(initialColor);
            // On pose N = la somme de l'histogramme, c'est à dire, le nombre total de
            // d'occurrence d'un niveau de couleur dans l'image

            int N = 0;
            for (int k = 0; k < pixelMax; k++) {
                N += histogram[k];
            }
            // Calcul de la variance inter-classe

            for (int i = 0; i < pixelMax; i++) {
                proba0 = 0;
                moy0 = 0;
                proba1 = 0;
                moy1 = 0;
                // Calcul de la première classe
                for (int j = 0; j <= i; j++) {
                    proba0 += histogram[j];
                    moy0 += j * histogram[j];
                }
                proba0 /= N;
                moy0 /= proba0;

                // Calcul de la deuxième classe
                for (int j = i + 1; j < pixelMax; j++) {
                    proba1 += histogram[j];
                    moy1 += j * histogram[j];
                }
                proba1 /= N;
                moy1 /= proba1;

                varianceInterclasses[i] = proba0 * proba1 * Math.pow((moy1 - moy0), 2);
            }
            // Détermination de la seuil

            // Détermination de la variance inter-classe la plus élevée

            for (int i = 0; i < pixelMax; i++) {
                if (varianceInterclasses[i] > maxVar) {
                    seuil = i;
                    maxVar = varianceInterclasses[i];
                }
            }
        }
        return seuil;
    }

    public static BufferedImage erosion(BufferedImage image, int origineL, int origineC, int tailleElementStructurant) {
        BufferedImage output = image;

        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        int[][] intermediateColor = new int[image.getWidth()][image.getHeight()];
        int[][] finalColor = new int[image.getWidth()][image.getHeight()];
        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de l'erosion", null,
                    JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }

        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                intermediateColor[i][j] = new Color(output.getRGB(i, j)).getRed();
            }
        }

        /*
         * D E B U T D E L ' A L G O R I T H M E D E L ' E R O S I O N
         */
        
        int[][] elementStructurant = new int[tailleElementStructurant][tailleElementStructurant];
        int[] elements = new int[(int)Math.pow(tailleElementStructurant, 2)];
        String operator = "EROSION";
        int count = 0;

        for (int i = 0 ; i < tailleElementStructurant ; i++) {
            for (int j = 0 ; j < tailleElementStructurant ; j++) {
                elementStructurant[i][j] = 0; // 0 = noir = fond
            }
        }
        for (int ligne = 0; ligne < elementStructurant.length ; ligne++) {
            for (int col = 0; col < elementStructurant[0].length ; col++) {
                elements[count] = elementStructurant[ligne][col];
                count++;
            }
        }
        for (int i = 0; i < intermediateColor.length; i++) {
            for (int j = 0; j < intermediateColor[0].length; j++) {
                int[] voisins = new int[(int)Math.pow(tailleElementStructurant, 2)];
                count = 0;
                for (int ligne = i - origineL, jetonESL = 0 ; jetonESL < tailleElementStructurant ; ligne++, jetonESL++) {
                    for (int col = j - origineC, jetonESC = 0 ; jetonESC < tailleElementStructurant ; col++, jetonESC++) {
                        voisins[count] = (ligne < 0 || ligne > intermediateColor.length - 1 || col < 0 || col > intermediateColor[0].length - 1) ? pixelMax - 1 : intermediateColor[ligne][col];
                        count++;
                    }
                }
                //Comparaison
                finalColor[i][j] = compareVoisinsES(voisins, elements, operator);
            }
        }
        // Transformation finale
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                output.setRGB(i, j, new Color(finalColor[i][j], finalColor[i][j], finalColor[i][j]).getRGB());
            }
        }
        return output;
    }
    public static int compareVoisinsES(int[] voisins, int[] elements, String operator){
        switch(operator){
            case "EROSION" :
                //Comparaison
                for (int k = 0; k < elements.length; k++) {
                    // Si il y a différence, alors, on met la couleur à blanc, pour réduire l'intensité
                    if (elements[k] != voisins[k])
                        return pixelMax - 1;
                }
                return 0;
            case "DILATATION" :
                for (int k = 0; k < elements.length; k++) {
                    // Si il y a différence, alors, on met la couleur à noir, pour augmenter
                    // l'intensité
                    if (elements[k] != voisins[k])
                        return 0;
                }
                return pixelMax - 1; 
        }

        return 0;
    }
    /*
     * D E B U T D E L ' A L G O R I T H M E D E C O N T O U R Cette algorithme
     * consiste à faire la différence entre l'image binaire initiale et cette même
     * image mais érodée. Celà va nous permettre de tracer chaque contour contenu
     * dans l'image. Cette algorithme est similaire à l'algorithme de tortue de
     * papert mais de façon un peu plus simple. J'ai d'abord transformé l'image
     * initiale en image binaire par la méthode d'otsu. Et puis, j'ai érodé l'image
     * binaire obtenue. Enfin, j'ai fait la différence entre l'image binaire obtenu
     * par la méthode d'Otsu et l'image érodée. Celà nous donne l'algorithme
     * suivant.
     */
    public static BufferedImage contourByErosion(BufferedImage image) {
        BufferedImage output = image;

        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        int[][] intermediateColor = new int[image.getWidth()][image.getHeight()];
        int[][] erodedImage = new int[image.getWidth()][image.getHeight()];
        int[][] finalColor = new int[image.getWidth()][image.getHeight()];
        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de l'erosion", null,
                    JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }

        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                intermediateColor[i][j] = new Color(output.getRGB(i, j)).getRed();
            }
        }

        /*
         * D E B U T D E L ' A L G O R I T H M E D E L ' E R O S I O N
         */
        // On va utiliser la matrice 3x3, avec toutes les coordonnées égales à 1
        int[][] elementStructurant = { { 2, 1, 2 }, { 1, 1, 1 }, { 2, 1, 2 } };
        int[] elements = new int[elementStructurant.length * elementStructurant[0].length];
        int count = 0;

        for (int ligne = 0; ligne < elementStructurant.length; ligne++) {
            for (int col = 0; col < elementStructurant[0].length; col++) {
                elements[count] = elementStructurant[ligne][col];
                count++;
            }
        }
        // Transformation de l'intermediateColor en 0 et 1.
        for (int i = 0; i < intermediateColor.length; i++) {
            for (int j = 0; j < intermediateColor[0].length; j++) {
                intermediateColor[i][j] = (intermediateColor[i][j] == 0) ? 1 : 0;
            }
        }
        for (int i = 0; i < intermediateColor.length; i++) {
            for (int j = 0; j < intermediateColor[0].length; j++) {
                int[] voisins = new int[elementStructurant.length * elementStructurant[0].length];
                count = 0;
                for (int ligne = i - 1; ligne <= (i + 1); ligne++) {
                    for (int col = j - 1; col <= (j + 1); col++) {
                        if (ligne < 0 || ligne >= intermediateColor.length || col < 0
                                || col >= intermediateColor[0].length)
                            voisins[count] = 0;
                        else
                            voisins[count] = intermediateColor[ligne][col];
                        count++;
                    }
                }
                // Comparaison
                for(int k = 0; k < elements.length; k++){
                    // Si il y a différence, alors, on met la couleur à blanc, pour réduire
                    // l'intensité
                    if(elements[k] == 2){
                        continue;
                    }else{
                        if (elements[k] != voisins[k]){
                            erodedImage[i][j] = 0;
                            break;
                        }else{
                            erodedImage[i][j] = 1;
                        }
                    }
                }
            }
        }
        // X - erosion(X, B)
        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                // IntermediateColor = l'image précédemment binarisée.
                // ErodedImage = l'image érodée.
                finalColor[i][j] = intermediateColor[i][j] - erodedImage[i][j];
            }
        }
        // Reconversion des (0, 1) en (255, 0)
        for (int i = 0; i < intermediateColor.length; i++) {
            for (int j = 0; j < intermediateColor[0].length; j++) {
                finalColor[i][j] = (finalColor[i][j] == 0) ? 255 : 0;
            }
        }
        // Transformation finale
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                output.setRGB(i, j, new Color(finalColor[i][j], finalColor[i][j], finalColor[i][j]).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage contourByPersonalAlgorithm(BufferedImage image) {
        BufferedImage output = image;

        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        int[][] intermediateColor = new int[image.getWidth()][image.getHeight()];
        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de l'algorithme personnel de contour",
                    null, JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }

        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                intermediateColor[i][j] = new Color(output.getRGB(i, j)).getRed();
            }
        }

        /*
         * D E B U T D E L ' A L G O R I T H M E D U C O N T O U R (Algorithme
         * personnel)
         */

        int[][] contourImage = new int[intermediateColor.length][intermediateColor[0].length];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // Si le pixel est de couleur blanche (fond), on le garde à blanc
                if (intermediateColor[i][j] == pixelMax - 1) {
                    contourImage[i][j] = intermediateColor[i][j];
                    // Si le pixel noir n'est pas au périphérique, alors on le met à fond (blanc)
                } else if (intermediateColor[i][j] == 0 && i - 1 >= 0 && j - 1 >= 0 && i + 1 < intermediateColor.length
                        && j + 1 < intermediateColor[0].length && intermediateColor[i - 1][j] == 0
                        && intermediateColor[i + 1][j] == 0 && intermediateColor[i][j - 1] == 0
                        && intermediateColor[i][j + 1] == 0) {
                    contourImage[i][j] = pixelMax - 1;
                    // Sinon, tous les autres cas de pixel noir sont donc des pixels périphériques,
                    // donc on le met à forme (noir)
                } else {
                    contourImage[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                output.setRGB(i, j, new Color(contourImage[i][j], contourImage[i][j], contourImage[i][j]).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage contourByPapertTurtle(BufferedImage image) {
        BufferedImage output = image;
        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        int[][] intermediateColor = new int[image.getWidth()][image.getHeight()];
        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            output = binarisationOtsu(image);
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de l'algorithme de Tortue de Papert",
                    null, JOptionPane.INFORMATION_MESSAGE);
        }
        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                intermediateColor[i][j] = new Color(output.getRGB(i, j)).getRed();
            }
        }

        /*
         * D E B U T D E L ' A L G O R I T H M E D E L A T O R T U E D E P A P E R T
         */
        int[][] binaryImage = new int[image.getWidth()][image.getHeight()];
        // Récupération de la matrice : transformation des fonds(blancs = 255) en 0 et
        // des objets/formes(noirs = 0) en 1
        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                // On met les fonds à 0 et les formes à 1
                binaryImage[i][j] = (intermediateColor[i][j] == 0) ? 1 : 0;
            }
        }
        int[][] contourImage = new int[binaryImage.length][binaryImage[0].length];
        boolean showTurtle = false;
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[0].length; j++) {
                // Si le point n'est pas périphérique, alors on le met à fond.
                if (binaryImage[i][j] == 1 && (i - 1) >= 0 && (j - 1) >= 0 && (i + 1) < binaryImage.length
                        && (j + 1) < binaryImage[0].length && binaryImage[i - 1][j] == 1 && binaryImage[i][j - 1] == 1
                        && binaryImage[i + 1][j] == 1 && binaryImage[i][j + 1] == 1 && binaryImage[i-1][j-1] == 1 && binaryImage[i+1][j+1] == 1
                        && binaryImage[i-1][j+1] == 1 && binaryImage[i+1][j-1] == 1){
                    // contourImage[i][j] = 0; On peut le négliger puisque le tableau contourImage
                    // est déjà initialisé à 0.
                    continue;
                }
                // Si on rencontre un objet et que cette objet n'est pas encore parcouru par la
                // tortue,
                // alors on place la tortue sur cette objet pour qu'elle puisse commencer son
                // parcours.
                if (binaryImage[i][j] == 1 && contourImage[i][j] != 1) {
                    int previousTurtlePosition_I = i;
                    int previousTurtlePosition_J = j;
                    int depart_I = i;
                    int depart_J = j;
                    showTurtle = true;
                    contourImage[i][j] = 1;
                    int k = i;
                    int l = j;
                    k--;
                    do {
                        // Si la tortue atteint le bord de l'image, on fait en sorte qu'il y a une
                        // bordure imaginaire ne contenant que des fonds (blancs = 255)
                        // et donc, elle va tourner à droite.
                        // Ou si elle ne rencontre pas d'objet, elle tourne à sa droite par rapport à sa
                        // position précédente.
                        if ((k < 0 || l < 0 || k > binaryImage.length - 1 || l > binaryImage[0].length - 1)
                                || (binaryImage[k][l] == 0)) {
                            // On force le le contenu de la coordonnée (k, l) à 0, donc il doit toujours
                            // tourner à droite
                            if (previousTurtlePosition_I == k + 1 && previousTurtlePosition_J == l) {
                                previousTurtlePosition_I = k;
                                previousTurtlePosition_J = l;
                                l++;
                            } else if (previousTurtlePosition_I == k && previousTurtlePosition_J == l - 1) {
                                previousTurtlePosition_I = k;
                                previousTurtlePosition_J = l;
                                k++;
                            } else if (previousTurtlePosition_I == k - 1 && previousTurtlePosition_J == l) {
                                previousTurtlePosition_I = k;
                                previousTurtlePosition_J = l;
                                l--;
                            } else if (previousTurtlePosition_I == k && previousTurtlePosition_J == l + 1) {
                                previousTurtlePosition_I = k;
                                previousTurtlePosition_J = l;
                                k--;
                            }
                        } else if (binaryImage[k][l] == 1) {
                            // Si la tortue rencontre un pixel objet, on marque les coordonnées de la place
                            // de ce pixel et elle tourne
                            // vers sa gauche par rapport à sa position précédente.
                            contourImage[k][l] = 1;
                            if (previousTurtlePosition_I == k - 1 && previousTurtlePosition_J == l) {
                                previousTurtlePosition_I = k;
                                previousTurtlePosition_J = l;
                                l++;
                            } else if (previousTurtlePosition_I == k && previousTurtlePosition_J == l + 1) {
                                previousTurtlePosition_I = k;
                                previousTurtlePosition_J = l;
                                k++;
                            } else if (previousTurtlePosition_I == k + 1 && previousTurtlePosition_J == l) {
                                previousTurtlePosition_I = k;
                                previousTurtlePosition_J = l;
                                l--;
                            } else if (previousTurtlePosition_I == k && previousTurtlePosition_J == l - 1) {
                                previousTurtlePosition_I = k;
                                previousTurtlePosition_J = l;
                                k--;
                            }

                        }

                        // Si elle revient à son point de départ, alors on annule le parcours
                        if (k == depart_I && l == depart_J) {
                            showTurtle = false;
                        }
                    } while (showTurtle);
                }
            }
        }
        // Récupération de la valeur du contourImage : transformation des 0 en fond
        // (blanc = 255) et des 1 en forme (noir = 1)
        // Et affectation de la valeur de contour au buffer d'image de sortie
        for (int i = 0; i < contourImage.length; i++) {
            for (int j = 0; j < contourImage[0].length; j++) {
                contourImage[i][j] = (contourImage[i][j] == 1) ? 0 : (pixelMax - 1);
                output.setRGB(i, j, new Color(contourImage[i][j], contourImage[i][j], contourImage[i][j]).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage dilatation(BufferedImage image, int origineL, int origineC, int tailleElementStructurant) {
        BufferedImage output = image;

        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        int[][] intermediateColor = new int[image.getWidth()][image.getHeight()];
        int[][] finalColor = new int[image.getWidth()][image.getHeight()];
        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de la dilatation", null,
                    JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }

        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                intermediateColor[i][j] = new Color(output.getRGB(i, j)).getRed();
            }
        }
        /*
         * D E B U T D E L ' A L G O R I T H M E D E L A D I L A T A T I O N
         */
        // On va utiliser la matrice 3x3, avec toutes les coordonnées égales à 1
        int[][] elementStructurant = new int[tailleElementStructurant][tailleElementStructurant];
        int[] elements = new int[(int)Math.pow(tailleElementStructurant, 2)];
        String operator = "DILATATION";
        int count = 0;

        for (int i = 0 ; i < tailleElementStructurant ; i++) {
            for (int j = 0 ; j < tailleElementStructurant ; j++) {
                elementStructurant[i][j] = pixelMax - 1;
            }
        }
        for (int ligne = 0; ligne < elementStructurant.length; ligne++) {
            for (int col = 0; col < elementStructurant[0].length; col++) {
                elements[count] = elementStructurant[ligne][col];
                count++;
            }
        }

        for (int i = 0; i < intermediateColor.length; i++) {
            for (int j = 0; j < intermediateColor[0].length; j++) {
                int[] voisins = new int[(int)Math.pow(tailleElementStructurant, 2)];
                count = 0;
                for (int ligne = i - origineL, jetonESL = 0 ; jetonESL < tailleElementStructurant ; ligne++, jetonESL++) {
                    for (int col = j - origineC, jetonESC = 0 ; jetonESC < tailleElementStructurant ; col++, jetonESC++) {
                        voisins[count] = (ligne < 0 || ligne > intermediateColor.length - 1 || col < 0 || col > intermediateColor[0].length - 1) ? pixelMax - 1 : intermediateColor[ligne][col];
                        count++;
                    }
                }
                // Comparaison
                finalColor[i][j] = compareVoisinsES(voisins, elements, operator);
            }
        }
        // Transformation finale
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                output.setRGB(i, j, new Color(finalColor[i][j], finalColor[i][j], finalColor[i][j]).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage ouverture(BufferedImage image, int origineL, int origineC, int tailleElementStructurant) {
        BufferedImage output = image;

        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de l'ouverture (erosion suivi de dilatation)",
                    null, JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }
        /*
         * Ouverture = érosion suivi de dilatation, donc :
         */
        output = dilatation(erosion(output, origineL, origineC, tailleElementStructurant), origineL, origineC, tailleElementStructurant);
        return output;
    }

    public static BufferedImage fermeture(BufferedImage image, int origineL, int origineC, int tailleElementStructurant) {
        BufferedImage output = image;

        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de la fermeture (dilatation suivi d'erosion)",
                    null, JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }
        /*
         * Fermeture = Dilatation suivi de d'érosion, donc :
         */
        output = erosion(dilatation(output, origineL, origineC, tailleElementStructurant), origineL, origineC, tailleElementStructurant);
        return output;
    }

    public static BufferedImage toutOuRien(BufferedImage image, int[][] elementStructurant) {
        BufferedImage output = image;

        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        int[][] intermediateColor = new int[image.getWidth()][image.getHeight()];
        int[][] finalColor = new int[image.getWidth()][image.getHeight()];
        boolean binary = true;
        // L'origine de l'élément structurant 3x3 est toujours fixé au centre, donc
        // origineL = 1, origineC = 1 :
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de l'algorithme de Tout ou Rien",
                    null, JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }

        for (int i = 0; i < output.getWidth(); i++) {
            for (int j = 0; j < output.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                intermediateColor[i][j] = new Color(output.getRGB(i, j)).getRed();
            }
        }

        /*
         * D E B U T D E L ' A L G O R I T H M E D E T O U T O U R I E N
         */
        // On va utiliser la matrice 3x3, avec toutes les coordonnées égales à 1
        int[] elements = new int[elementStructurant.length * elementStructurant[0].length];
        int count = 0;

        // Assignement de la variable elements[] pour la partie comparaison des valeurs.
        for (int i = 0; i < elementStructurant.length; i++) {
            for (int j = 0; j < elementStructurant[0].length; j++) {
                elements[count] = elementStructurant[i][j];
                count++;
            }

        }
        // Transformation temporaire des pixels de l'image : les blancs(255) devient 0
        // et les noirs(0) devient 1.
        for (int i = 0; i < intermediateColor.length; i++) {
            for (int j = 0; j < intermediateColor[0].length; j++) {
                if (intermediateColor[i][j] == 0)
                    intermediateColor[i][j] = 1;
                else
                    intermediateColor[i][j] = 0;
            }
        }
        for (int i = 0; i < intermediateColor.length; i++) {
            for (int j = 0; j < intermediateColor[0].length; j++) {
                int[] voisins = new int[elementStructurant.length * elementStructurant[0].length];
                count = 0;
                for (int ligne = i - 1; ligne <= (i + 1); ligne++) {
                    for (int col = j - 1; col <= (j + 1); col++) {
                        if (ligne < 0 || ligne >= intermediateColor.length || col < 0
                                || col >= intermediateColor[0].length)
                            voisins[count] = 0;
                        else
                            voisins[count] = intermediateColor[ligne][col];
                        count++;
                    }
                }
                // Comparaison
                for (int k = 0; k < elements.length; k++) {
                    // Si la valeur de l'élément structurant est autre que 0 ou 1, alors on
                    // l'ignore, et on continue la boucle
                    if (elements[k] != 0 && elements[k] != 1)
                        continue;
                    // Si il y a différence, alors, on met la couleur à blanc, sinon on le met à
                    // noir
                    if (elements[k] != voisins[k]) {
                        finalColor[i][j] = pixelMax - 1;
                        break;
                    } else {
                        finalColor[i][j] = 0;
                    }
                }
            }
        }
        // Transformation finale
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                output.setRGB(i, j, new Color(finalColor[i][j], finalColor[i][j], finalColor[i][j]).getRGB());
            }
        }

        return output;
    }

    public static BufferedImage epaississement(BufferedImage image, int[][] elementStructurant) {
        BufferedImage output = image;
        BufferedImage imageBinaire = image;
        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        int[][] intermediateColor = new int[image.getWidth()][image.getHeight()];
        int[][] finalColor = new int[image.getWidth()][image.getHeight()];
        boolean binary = true;
        // L'origine de l'élément structurant 3x3 est toujours fixé au centre, donc
        // origineL = 1, origineC = 1 :
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de l'algorithme de Tout ou Rien",
                    null, JOptionPane.INFORMATION_MESSAGE);
            imageBinaire = binarisationOtsu(image);
        }

        for (int i = 0; i < imageBinaire.getWidth(); i++) {
            for (int j = 0; j < imageBinaire.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                intermediateColor[i][j] = new Color(output.getRGB(i, j)).getRed();
            }
        }
        /*
         * ALGORITHME D'EPAISSISSEMENT
         */
        // le tableau intermediateColor contient l'image qui vient d'être transformée en
        // binaire.
        int[][] initialBinaryColor = intermediateColor;
        // le Buffer d'image suivant va contenir l'image transformée avec l'algorithme
        // de tout ou rien :
        BufferedImage imageToutOuRien = toutOuRien(imageBinaire, elementStructurant);
        // On va l'ajouter dans un tableau pour permettre d'obtenir la matrice de
        // l'épaississement.
        int[][] toutOuRienColor = new int[imageToutOuRien.getWidth()][imageToutOuRien.getHeight()];
        for (int i = 0; i < imageToutOuRien.getWidth(); i++) {
            for (int j = 0; j < imageToutOuRien.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                toutOuRienColor[i][j] = new Color(imageToutOuRien.getRGB(i, j)).getRed();
            }
        }
        // Transformation temporaire de l'image initiale et l'image transformée en tout
        // ou rien, en matrice binaire(0 et 1)
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                toutOuRienColor[i][j] = (toutOuRienColor[i][j] == 0) ? 1 : 0;
                initialBinaryColor[i][j] = (initialBinaryColor[i][j] == 0) ? 1 : 0;
            }
        }
        // Implémentation de la formule de l'épaississement : X U toutOuRien(X, B) = X +
        // toutOuRien(X, B) - (X inter toutOuRien(X, B))
        // Càd : somme des deux matrices - intersection des deux matrices.
        int[][] epaississement = new int[image.getWidth()][image.getHeight()];
        int[][] sommeMatrice = new int[image.getWidth()][image.getHeight()];
        int[][] intersectionMatrice = new int[image.getWidth()][image.getHeight()];
        // X + toutOuRien(X, B)
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                if (initialBinaryColor[i][j] == 0 && toutOuRienColor[i][j] == 0)
                    // Si la valeur des 2 matrices à l'indice ij est = 255(blanc), alors, on met le
                    // couleur final à blanc(255)
                    sommeMatrice[i][j] = 0;
                else
                    // Sinon, s'il y a une seule valeur différente de 255 (), alors on la met à
                    // noir(0)
                    sommeMatrice[i][j] = 1;
            }
        }
        // Intersection de X et toutOuRien de X par l'élemenet structurant B
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                if (initialBinaryColor[i][j] == 1 && toutOuRienColor[i][j] == 1)
                    // Si la valeur des 2 matrices à l'indice ij est = 255(blanc), alors, on met le
                    // couleur final à blanc(255)
                    intersectionMatrice[i][j] = 1;
                else
                    // Sinon, s'il y a une seule valeur différente de 255 (), alors on la met à
                    // noir(0)
                    intersectionMatrice[i][j] = 0;
            }
        }
        // Epaississement = XUtor(X,B) = somme(X, tor(X,B)) - intersection(X, tor(X,B))
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                epaississement[i][j] = sommeMatrice[i][j] - intersectionMatrice[i][j];
            }
        }
        // Retransformation de la valeur binaire 0 et 1 en pixel réspectivement, en 255
        // et 0
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R = G = B, alors on peut utiliser R comme reference.
                finalColor[i][j] = (epaississement[i][j] == 0) ? 255 : 0;
            }
        }
        // Transformation finale
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                output.setRGB(i, j, new Color(finalColor[i][j], finalColor[i][j], finalColor[i][j]).getRGB());
            }
        }

        return output;
    }

    public static BufferedImage topHatOuv(BufferedImage image, int origineL, int origineC, int tailleElementStructurant) {
        BufferedImage output = image;
        int[][] imgInit = new int[image.getWidth()][image.getHeight()]; // image avant ouverture
        int[][] imgOuv = new int[image.getWidth()][image.getHeight()]; // image après ouverture
        int[][] topHatO = new int[image.getWidth()][image.getHeight()]; // topHatO = imgInit - imgOuv, càd : X - XoB
        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application du top-hat a l'ouverture ( X - XoB)",
                    null, JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }
        /*
         * Récupération de chaque pixel de l'image binarisée par la methode d'Otsu.
         */
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R=G=B, alors on peut prendre R comme reference
                // Transformation des pixels blancs en 0 et des pixels noirs en 1
                imgInit[i][j] = (new Color(image.getRGB(i, j)).getRed() == pixelMax - 1) ? 0 : 1;
            }
        }
        BufferedImage imageOuverte = ouverture(image, origineL, origineC, tailleElementStructurant);
        /*
         * Récupération de chaque pixel de l'image ouverte
         */
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R=G=B, alors on peut prendre R comme reference
                // Transformation des pixels blancs en 0 et des pixels noirs en 1
                imgOuv[i][j] = (new Color(imageOuverte.getRGB(i, j)).getRed() == pixelMax - 1) ? 0 : 1;
            }
        }
        // Calcul du topHat
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                topHatO[i][j] = imgInit[i][j] - imgOuv[i][j];
            }
        }

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // Transformation des 0 en pixels blancs (255) et des 1 en pixels noirs (0)
                topHatO[i][j] = (topHatO[i][j] == 0) ? pixelMax - 1 : 0;
            }
        }
        // transformation de topHat en binaire
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R = G = B, donc on peut prendre R comme reference
                output.setRGB(i, j, new Color(topHatO[i][j], topHatO[i][j], topHatO[i][j]).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage topHatFer(BufferedImage image, int origineL, int origineC, int tailleElementStructurant) {
        BufferedImage output = image;
        int[][] imgInit = new int[image.getWidth()][image.getHeight()]; // image avant fermeture
        int[][] imgFer = new int[image.getWidth()][image.getHeight()]; // image après fermeture
        int[][] topHatF = new int[image.getWidth()][image.getHeight()]; // topHatF = imgFer - imgInit, càd : X.B - X
        // Puisque toutes les couleurs vont être égales, on n'utilise qu'un seul
        // variable pour la récupération initiale et finale
        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255)
                        && (new Color(image.getRGB(u, v)).getGreen() != 0
                                && new Color(image.getRGB(u, v)).getGreen() != 255)
                        && (new Color(image.getRGB(u, v)).getBlue() != 0
                                && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            JOptionPane.showMessageDialog(null,
                    "Utilisation de la methode d'Otsu pour la binarisation, \n puis application du top-hat a la fermeture ( X.B - X)",
                    null, JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }
        /*
         * Récupération de chaque pixel de l'image binarisée par la methode d'Otsu.
         */
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R=G=B, alors on peut prendre R comme reference
                // Transformation des pixels blancs en 0 et des pixels noirs en 1
                imgInit[i][j] = (new Color(image.getRGB(i, j)).getRed() == pixelMax - 1) ? 0 : 1;
            }
        }
        BufferedImage imageFermee = fermeture(image, origineL, origineC, tailleElementStructurant);
        /*
         * Récupération de chaque pixel de l'image ouverte
         */
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R=G=B, alors on peut prendre R comme reference
                // Transformation des pixels blancs en 0 et des pixels noirs en 1
                imgFer[i][j] = (new Color(imageFermee.getRGB(i, j)).getRed() == pixelMax - 1) ? 0 : 1;
            }
        }
        // Calcul du topHat
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                topHatF[i][j] = imgFer[i][j] - imgInit[i][j];
            }
        }

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // Transformation des 0 en pixels blancs (255) et des 1 en pixels noirs (0)
                topHatF[i][j] = (topHatF[i][j] == 0) ? pixelMax - 1 : 0;
            }
        }
        // transformation de topHat en binaire
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // R = G = B, donc on peut prendre R comme reference
                output.setRGB(i, j, new Color(topHatF[i][j], topHatF[i][j], topHatF[i][j]).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage etiquetage(BufferedImage image) {
        BufferedImage output = image;

        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255) && (new Color(image.getRGB(u, v)).getGreen() != 0 && new Color(image.getRGB(u, v)).getGreen() != 255) && (new Color(image.getRGB(u, v)).getBlue() != 0 && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            // [X-B] = erosion(X) avec utilisation de l'élément structurant B
            JOptionPane.showMessageDialog(null, "Utilisation de la methode d'Otsu pour la binarisation, \n puis application de l'algorithme de l'etiquetage", null, JOptionPane.INFORMATION_MESSAGE);
            output = binarisationOtsu(image);
        }

        /*
         * Debut de l'algorithme de l'etiquetage.
         */

        // On va colorer chaque objet différemment
        // int r = pixelMax - 1, g = pixelMax - 1 , b = pixelMax - 1 - 10;
        // int[][] redValueImage = new int[image.getWidth()][image.getHeight()];
        // int[][] greenValueImage = new int[image.getWidth()][image.getHeight()];
        // int[][] blueValueImage = new int[image.getWidth()][image.getHeight()];

        // Récupération de chaque pixel de l'image dans un tableau 2D
        int[][] binaryImage = new int[image.getWidth()][image.getHeight()];
        int[][] etiqueteImage = new int[image.getWidth()][image.getHeight()];
        int etiqueteCount = 0;
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[0].length; j++) {
                binaryImage[i][j] = new Color(output.getRGB(i, j)).getRed(); // puisque r = g = b, donc j'ai pris red.
            }
        }

        // élimination des bordures (coloration en blanc) pour éviter l'erreur
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[0].length; j++) {
                if (i == 0 || j == 0 || i == binaryImage.length - 1 || j == binaryImage[0].length - 1) {
                    binaryImage[i][j] = pixelMax - 1;
                }
            }
        }
        // étiquetage de chaque objet de l'image.
        //Premier parcours
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[0].length; j++) {
                boolean sortir = false;
                if (binaryImage[i][j] == 0) {
                    for (int l = i - 1; l <= i + 1; l++) {
                        for (int c = j - 1; c <= j + 1; c++) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            //Si le pixel à la coordonnée (l, c) voisin du pixel courant à la coordonnée (i, j) est une forme et est déjà étiquétté, 
                            //alors on lui affecte l'etiquette de son voisin et on passe au pixel suivant
                            if (etiqueteImage[l][c] != 0 && binaryImage[l][c] != pixelMax - 1) {
                                etiqueteImage[i][j] = etiqueteImage[l][c];
                                sortir = true;
                                break;
                            }
                        }
                        if (sortir) {
                            break;
                        }
                    }
                    if (sortir) {
                        continue;
                    }
                    //Si le pixel courant à la coordonnée (i, j) n'a pas de voisin déjà étiquétté, alors on lui donne une nouvelle étiquette
                    etiqueteImage[i][j] = ++etiqueteCount;
                }
            }
        }
        //Deuxième parcours
        for (int i = binaryImage.length - 1; i >= 0; i--) {
            for (int j = binaryImage[0].length - 1; j >= 0; j--) {
                if (binaryImage[i][j] == 0) {
                    for (int l = i + 1; l >= i - 1; l--) {
                        for (int c = j + 1; c >= j - 1; c--) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            if (etiqueteImage[l][c] != etiqueteImage[i][j] && binaryImage[l][c] == 0) {
                                etiqueteImage[l][c] = etiqueteImage[i][j];
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = binaryImage[0].length - 1; j >= 0; j--) {
                if (binaryImage[i][j] == 0) {
                    for (int l = i - 1; l <= i + 1; l++) {
                        for (int c = j - 1; c <= j + 1; c++) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            if (etiqueteImage[l][c] != etiqueteImage[i][j] && binaryImage[l][c] == 0) {
                                etiqueteImage[l][c] = etiqueteImage[i][j];
                            }
                        }
                    }
                }
            }
        }
        for (int i = binaryImage.length - 1; i >= 0; i--) {
            for (int j = binaryImage[0].length - 1; j >= 0; j--) {
                if (binaryImage[i][j] == 0) {
                    for (int l = i + 1; l >= i - 1; l--) {
                        for (int c = j + 1; c >= j - 1; c--) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            if (etiqueteImage[l][c] != etiqueteImage[i][j] && binaryImage[l][c] == 0) {
                                etiqueteImage[l][c] = etiqueteImage[i][j];
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[0].length; j++) {
                if (binaryImage[i][j] == 0) {
                    for (int l = i - 1; l <= i + 1; l++) {
                        for (int c = j - 1; c <= j + 1; c++) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            if (etiqueteImage[l][c] != 0 && binaryImage[l][c] != pixelMax - 1) {
                                etiqueteImage[l][c] = etiqueteImage[i][j];
                            }
                        }
                    }
                }
            }
        }
        // On va re-compter le nombre d'étiquette.
        int countEtq = 0;
        int[] tabEtq = new int[etiqueteCount];
        for (int i = 0; i < etiqueteCount; i++) {
            tabEtq[i] = i + 1;
        }
        for (int k = 1; k <= etiqueteCount; k++) {
            boolean continuer = false;
            for (int i = 0; i < binaryImage.length; i++) {
                for (int j = 0; j < binaryImage[0].length; j++) {
                    if (etiqueteImage[i][j] == k) {
                        countEtq++;
                        continuer = true;
                        break;
                    }
                }
                if (continuer)
                    break;
            }
        }
        // On va colorer les objets étiquétés
        int[] redColorContainer = new int[etiqueteCount + 1];
        int[] greenColorContainer = new int[etiqueteCount + 1];
        int[] blueColorContainer = new int[etiqueteCount + 1];
        redColorContainer[0] = pixelMax - 1;
        greenColorContainer[0] = pixelMax - 1;
        blueColorContainer[0] = pixelMax - 1;
        int changeRed = 50;
        for (int k = 1; k <= etiqueteCount; k++) {
            redColorContainer[k] = changeRed;
            greenColorContainer[k] = greenColorContainer[k - 1];
            blueColorContainer[k] = blueColorContainer[k - 1] - 60;
            if(k%2 == 0)
                redColorContainer[k] = changeRed + 150;
            if (blueColorContainer[k] < 0) {
                blueColorContainer[k] = 200;
                greenColorContainer[k] = greenColorContainer[k - 1] - 100;
                if (greenColorContainer[k] < 0) {
                    greenColorContainer[k] = 200;
                    redColorContainer[k] = redColorContainer[k - 1] + 50;
                    if (redColorContainer[k] > 220) {
                        redColorContainer[k] = 50;
                        changeRed = redColorContainer[k];
                    }
                }
            }
        }
        System.out.println("Nombre d'etiquettes avant le second parcours : " + etiqueteCount);
        System.out.println("Nombre d'etiquEtEs apres le second parcours : " + countEtq);
        // JOptionPane.showMessageDialog(null, "Nombre d'objets : \n Apres le premier parcours : " + etiqueteCount
        //             + "\n Apres le second parcours : " + countEtq, null, JOptionPane.INFORMATION_MESSAGE);
        for (int i = 0; i < etiqueteImage.length; i++) {
            for (int j = 0; j < etiqueteImage[0].length; j++) {
                for (int k = 0; k < etiqueteCount; k++) {
                    if (etiqueteImage[i][j] == k) {
                        output.setRGB(i, j, new Color(redColorContainer[k], greenColorContainer[k], blueColorContainer[k]).getRGB());
                        continue;
                    }
                }
            }
        }

        return output;
    }
    public static int objectsNumber(BufferedImage image){
        BufferedImage output = image;

        boolean binary = true;
        // Vérification de l'image si déjà noir et blanc
        for (int u = 0; u < image.getWidth(); u++) {
            for (int v = 0; v < image.getHeight(); v++) {
                if ((new Color(image.getRGB(u, v)).getRed() != 0 && new Color(image.getRGB(u, v)).getRed() != 255) && (new Color(image.getRGB(u, v)).getGreen() != 0 && new Color(image.getRGB(u, v)).getGreen() != 255) && (new Color(image.getRGB(u, v)).getBlue() != 0 && new Color(image.getRGB(u, v)).getBlue() != 255)) {
                    binary = false;
                    break;
                }
            }
            if (!binary)
                break;
        }
        if (!binary) {
            output = binarisationOtsu(image);
        }

        /*
         * Debut de l'algorithme de l'etiquetage pour récupérer le nombre d'objets
         */

        // Récupération de chaque pixel de l'image dans un tableau 2D
        int[][] binaryImage = new int[image.getWidth()][image.getHeight()];
        int[][] etiqueteImage = new int[image.getWidth()][image.getHeight()];
        int etiqueteCount = 0;
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[0].length; j++) {
                binaryImage[i][j] = new Color(output.getRGB(i, j)).getRed(); // puisque r = g = b, donc j'ai pris red.
            }
        }

        // élimination des bordures (coloration en blanc) pour éviter l'erreur
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[0].length; j++) {
                if (i == 0 || j == 0 || i == binaryImage.length - 1 || j == binaryImage[0].length - 1) {
                    binaryImage[i][j] = pixelMax - 1;
                }
            }
        }
        // étiquetage de chaque objet de l'image.
        //Premier parcours
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[0].length; j++) {
                boolean sortir = false;
                if (binaryImage[i][j] == 0) {
                    for (int l = i - 1; l <= i + 1; l++) {
                        for (int c = j - 1; c <= j + 1; c++) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            //Si le pixel à la coordonnée (l, c) voisin du pixel courant à la coordonnée (i, j) est une forme et est déjà étiquétté, 
                            //alors on lui affecte l'etiquette de son voisin et on passe au pixel suivant
                            if (etiqueteImage[l][c] != 0 && binaryImage[l][c] != pixelMax - 1) {
                                etiqueteImage[i][j] = etiqueteImage[l][c];
                                sortir = true;
                                break;
                            }
                        }
                        if (sortir) {
                            break;
                        }
                    }
                    if (sortir) {
                        continue;
                    }
                    //Si le pixel courant à la coordonnée (i, j) n'a pas de voisin déjà étiquétté, alors on lui donne une nouvelle étiquette
                    etiqueteImage[i][j] = ++etiqueteCount;
                }
            }
        }
        //Deuxième parcours
        for (int i = binaryImage.length - 1; i >= 0; i--) {
            for (int j = binaryImage[0].length - 1; j >= 0; j--) {
                if (binaryImage[i][j] == 0) {
                    for (int l = i + 1; l >= i - 1; l--) {
                        for (int c = j + 1; c >= j - 1; c--) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            if (etiqueteImage[l][c] != etiqueteImage[i][j] && binaryImage[l][c] == 0) {
                                etiqueteImage[l][c] = etiqueteImage[i][j];
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = binaryImage[0].length - 1; j >= 0; j--) {
                if (binaryImage[i][j] == 0) {
                    for (int l = i - 1; l <= i + 1; l++) {
                        for (int c = j - 1; c <= j + 1; c++) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            if (etiqueteImage[l][c] != etiqueteImage[i][j] && binaryImage[l][c] == 0) {
                                etiqueteImage[l][c] = etiqueteImage[i][j];
                            }
                        }
                    }
                }
            }
        }
        for (int i = binaryImage.length - 1; i >= 0; i--) {
            for (int j = binaryImage[0].length - 1; j >= 0; j--) {
                if (binaryImage[i][j] == 0) {
                    for (int l = i + 1; l >= i - 1; l--) {
                        for (int c = j + 1; c >= j - 1; c--) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            if (etiqueteImage[l][c] != etiqueteImage[i][j] && binaryImage[l][c] == 0) {
                                etiqueteImage[l][c] = etiqueteImage[i][j];
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < binaryImage.length; i++) {
            for (int j = 0; j < binaryImage[0].length; j++) {
                if (binaryImage[i][j] == 0) {
                    for (int l = i - 1; l <= i + 1; l++) {
                        for (int c = j - 1; c <= j + 1; c++) {
                            if (l < 0 || c < 0 || l > binaryImage.length - 1 || c > binaryImage[0].length - 1) {
                                continue;
                            }
                            if (etiqueteImage[l][c] != 0 && binaryImage[l][c] != pixelMax - 1) {
                                etiqueteImage[l][c] = etiqueteImage[i][j];
                            }
                        }
                    }
                }
            }
        }

        // On va re-compter le nombre d'étiquette.
        int countEtq = 0;
        int[] tabEtq = new int[etiqueteCount];
        for (int i = 0; i < etiqueteCount; i++) {
            tabEtq[i] = i + 1;
        }
        for (int k = 1; k <= etiqueteCount; k++) {
            boolean continuer = false;
            for (int i = 0; i < binaryImage.length; i++) {
                for (int j = 0; j < binaryImage[0].length; j++) {
                    if (etiqueteImage[i][j] == k) {
                        countEtq++;
                        continuer = true;
                        break;
                    }
                }
                if (continuer)
                    break;
            }
        }
        return countEtq;
    }
    public static BufferedImage etalementDynamique(BufferedImage image, int m, int M) {
        BufferedImage output = image;
        int[][] initialRed = new int[image.getWidth()][image.getHeight()];
        int[][] initialGreen = new int[image.getWidth()][image.getHeight()];
        int[][] initialBlue = new int[image.getWidth()][image.getHeight()];
        int[][] finalRed = new int[image.getWidth()][image.getHeight()];
        int[][] finalGreen = new int[image.getWidth()][image.getHeight()];
        int[][] finalBlue = new int[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color pixelColor = new Color(image.getRGB(i, j));
                initialRed[i][j] = pixelColor.getRed();
                initialGreen[i][j] = pixelColor.getGreen();
                initialBlue[i][j] = pixelColor.getBlue();
            }
        }
        // Etalement
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                for (int k = 0; k < pixelMax; k++) {
                    if (k < m) {
                        if (initialRed[i][j] == k) {
                            finalRed[i][j] = 0;
                        }
                        if (initialGreen[i][j] == k) {
                            finalGreen[i][j] = 0;
                        }
                        if (initialBlue[i][j] == k) {
                            finalBlue[i][j] = 0;
                        }
                    } else if (k > M) {
                        if (initialRed[i][j] == k) {
                            finalRed[i][j] = pixelMax - 1;
                        }
                        if (initialGreen[i][j] == k) {
                            finalGreen[i][j] = pixelMax - 1;
                        }
                        if (initialBlue[i][j] == k) {
                            finalBlue[i][j] = pixelMax - 1;
                        }
                    } else {
                        if (initialRed[i][j] == k) {
                            finalRed[i][j] = (int) ((((double) (pixelMax - 1)) / ((double) (M - m)))
                                    * (double) (initialRed[i][j] - m));
                        }
                        if (initialGreen[i][j] == k) {
                            finalGreen[i][j] = (int) ((((double) (pixelMax - 1)) / ((double) (M - m)))
                                    * (double) (initialGreen[i][j] - m));
                        }
                        if (initialBlue[i][j] == k) {
                            finalBlue[i][j] = (int) ((((double) (pixelMax - 1)) / ((double) (M - m)))
                                    * (double) (initialBlue[i][j] - m));
                        }
                    }
                }
            }
        }
        // Transformation finale
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                output.setRGB(i, j, new Color(finalRed[i][j], finalGreen[i][j], finalBlue[i][j]).getRGB());
            }
        }
        // Rendu
        // for (int i = 0; i < 1; i++) {
        //     for (int j = 0; j < 50; j++) {
        //         System.out.println("Ri[" + i + "][" + j + "] = " + initialRed[i][j] + " Gi[" + i + "][" + j + "] = "
        //                 + initialGreen[i][j] + " Bi[" + i + "][" + j + "] = " + initialBlue[i][j]);
        //         System.out.println("Rf[" + i + "][" + j + "] = " + finalRed[i][j] + " Gf[" + i + "][" + j + "] = "
        //                 + finalGreen[i][j] + " Bf[" + i + "][" + j + "] = " + finalBlue[i][j]);
        //     }
        // }
        System.out.println("Fin du traitement : etalement dynamique");
        return output;
    }

    public static BufferedImage grayscale(BufferedImage image) {
        BufferedImage output = image;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color pixelColor = new Color(image.getRGB(i, j));
                int r = pixelColor.getRed();
                int g = pixelColor.getGreen();
                int b = pixelColor.getBlue();
                int mean = (r + g + b) / 3;
                output.setRGB(i, j, new Color(mean, mean, mean).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage inversionGrayscale(BufferedImage image) {
        BufferedImage output = image;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color pixelColor = new Color(image.getRGB(i, j));
                int r = pixelColor.getRed();
                int g = pixelColor.getGreen();
                int b = pixelColor.getBlue();
                int mean = (r + g + b) / 3;
                int inverse = pixelMax - 1 - mean;
                output.setRGB(i, j, new Color(inverse, inverse, inverse).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage inversionColor(BufferedImage image) {
        BufferedImage output = image;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color pixelColor = new Color(image.getRGB(i, j));
                int r = pixelColor.getRed();
                int g = pixelColor.getGreen();
                int b = pixelColor.getBlue();
                r = pixelMax - 1 - r;
                g = pixelMax - 1 - g;
                b = pixelMax - 1 - b;
                output.setRGB(i, j, new Color(r, g, b).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage symetrieX(BufferedImage image) {
        BufferedImage output = image;

        /*
         * symetrie x'ox
         */
        float axeX = (float) image.getHeight() / 2.0f;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < axeX; j++) {
                int temp = image.getRGB(i, j);
                output.setRGB(i, j, image.getRGB(i, image.getHeight() - 1 - j));
                output.setRGB(i, image.getHeight() - 1 - j, temp);
            }
        }
        return output;
    }

    public static BufferedImage symetrieY(BufferedImage image) {
        BufferedImage output = image;

        /*
         * symetrie y'oy
         */
        float axeY = (float) image.getWidth() / 2.0f;

        for (int i = 0; i < axeY; i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int temp = image.getRGB(i, j);
                output.setRGB(i, j, image.getRGB(image.getWidth() - 1 - i, j));
                output.setRGB(image.getWidth() - 1 - i, j, temp);
            }
        }
        return output;
    }

    public static BufferedImage symetrieO(BufferedImage image) {
        BufferedImage output = image;

        /*
         * symetrie par rapport au centre
         */
        float axeX = (float) image.getHeight() / 2.0f;
        float axeY = (float) image.getWidth() / 2.0f;

        for (int i = 0; i < axeY; i++) {
            for (int j = 0; j < axeX * 2; j++) {
                int temp = image.getRGB(i, j);
                output.setRGB(i, j, image.getRGB((int) axeY * 2 - 1 - i, (int) axeX * 2 - 1 - j));
                output.setRGB((int) axeY * 2 - 1 - i, (int) axeX * 2 - 1 - j, temp);
            }
        }
        return output;
    }

    public static BufferedImage egalisationHistogramme(BufferedImage image) {
        BufferedImage output = image;
        int width = image.getWidth();
        int height = image.getHeight();
        // Histogrammes initiaux (Hi)
        int[] HiRed = new int[pixelMax];
        int[] HiGreen = new int[pixelMax];
        int[] HiBlue = new int[pixelMax];
        // Histogrammes initiaux cumulés (Hci)
        int[] HciRed = new int[pixelMax];
        int[] HciGreen = new int[pixelMax];
        int[] HciBlue = new int[pixelMax];
        // Histogrammes désirés (Hj)= histogrammes égaux
        int[] HjRed = new int[pixelMax];
        int[] HjGreen = new int[pixelMax];
        int[] HjBlue = new int[pixelMax];
        // Histogrammes cumulés désirés (Hcj)
        int[] HcjRed = new int[pixelMax];
        int[] HcjGreen = new int[pixelMax];
        int[] HcjBlue = new int[pixelMax];
        // Transformation ponctuelle : f(i) = j
        int[] jRed = new int[pixelMax];
        int[] jGreen = new int[pixelMax];
        int[] jBlue = new int[pixelMax];
        // Couleurs finales après transformation
        int[][] finalRed = new int[width][height];
        int[][] finalGreen = new int[width][height];
        int[][] finalBlue = new int[width][height];

        HiRed = histogramme(image, "red");
        HiGreen = histogramme(image, "green");
        HiBlue = histogramme(image, "blue");

        HciRed = cumulHistogram(HiRed);
        HciGreen = cumulHistogram(HiGreen);
        HciBlue = cumulHistogram(HiBlue);

        // Egalisation d'histogramme = histogramme désiré (Hj)
        for (int i = 0; i < pixelMax; i++) {
            // On sait que le nombre total de l'histogramme est égal à la dernière valeur de
            // l'histogramme cumulé, donc :
            HjRed[i] = HciRed[pixelMax - 1] / (pixelMax);
            HjGreen[i] = HciGreen[pixelMax - 1] / pixelMax;
            HjBlue[i] = HciBlue[pixelMax - 1] / pixelMax;
        }

        HcjRed = cumulHistogram(HjRed);
        HcjGreen = cumulHistogram(HjGreen);
        HcjBlue = cumulHistogram(HjBlue);

        jRed = nearestPixel(HciRed, HcjRed);
        jGreen = nearestPixel(HciGreen, HcjGreen);
        jBlue = nearestPixel(HciBlue, HcjBlue);

        finalRed = specificationHistogramme(image, "red", jRed);
        finalGreen = specificationHistogramme(image, "green", jGreen);
        finalBlue = specificationHistogramme(image, "blue", jBlue);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                output.setRGB(i, j, new Color(finalRed[i][j], finalGreen[i][j], finalBlue[i][j]).getRGB());
            }
        }
        // Analyse du résultat
        System.out.println(
                "iR\thiR\thciR\thjR\thcjR\tjR\t|\tiG\thiG\thciG\thjG\thcjG\tjG\t|\tiB\thiB\thciB\thjB\thcjB\tjB");
        for (int u = 0; u < pixelMax; u++) {
            System.out.println(u + "\t" + HiRed[u] + "\t" + HciRed[u] + "\t" + HjRed[u] + "\t" + HcjRed[u] + "\t"
                    + jRed[u] + "\t|\t" + u + "\t" + HiGreen[u] + "\t" + HciGreen[u] + "\t" + HjGreen[u] + "\t"
                    + HcjGreen[u] + "\t" + jGreen[u] + "\t|\t " + u + "\t" + HiBlue[u] + "\t" + HciBlue[u] + "\t"
                    + HjBlue[u] + "\t" + HcjBlue[u] + "\t" + jBlue[u]);
        }

        return output;
    }

    public static BufferedImage inversionHistogramme(BufferedImage image) {
        BufferedImage output = image;
        int width = image.getWidth();
        int height = image.getHeight();
        // Histogrammes initiaux (Hi)
        int[] HiRed = new int[pixelMax];
        int[] HiGreen = new int[pixelMax];
        int[] HiBlue = new int[pixelMax];
        // Histogrammes initiaux cumulés (Hci)
        int[] HciRed = new int[pixelMax];
        int[] HciGreen = new int[pixelMax];
        int[] HciBlue = new int[pixelMax];
        // Histogrammes désirés (Hj)= histogrammes inversés
        int[] HjRed = new int[pixelMax];
        int[] HjGreen = new int[pixelMax];
        int[] HjBlue = new int[pixelMax];
        // Histogrammes cumulés désirés (Hcj)
        int[] HcjRed = new int[pixelMax];
        int[] HcjGreen = new int[pixelMax];
        int[] HcjBlue = new int[pixelMax];
        // Transformation ponctuelle : f(i) = j
        int[] jRed = new int[pixelMax];
        int[] jGreen = new int[pixelMax];
        int[] jBlue = new int[pixelMax];
        // Couleurs finales après transformation
        int[][] finalRed = new int[width][height];
        int[][] finalGreen = new int[width][height];
        int[][] finalBlue = new int[width][height];

        HiRed = histogramme(image, "red");
        HiGreen = histogramme(image, "green");
        HiBlue = histogramme(image, "blue");

        HciRed = cumulHistogram(HiRed);
        HciGreen = cumulHistogram(HiGreen);
        HciBlue = cumulHistogram(HiBlue);

        // Inversion d'histogramme = histogramme désiré (Hj)
        // On va faire inverser l'histogramme. Exemple : Red[0] devient Red[255] et
        // Red[255] devient Red[0]
        for (int i = 0; i < pixelMax; i++) {
            HjRed[i] = HiRed[pixelMax - 1 - i];
            HjGreen[i] = HiGreen[pixelMax - 1 - i];
            HjBlue[i] = HiBlue[pixelMax - 1 - i];
        }

        HcjRed = cumulHistogram(HjRed);
        HcjGreen = cumulHistogram(HjGreen);
        HcjBlue = cumulHistogram(HjBlue);

        jRed = nearestPixel(HciRed, HcjRed);
        jGreen = nearestPixel(HciGreen, HcjGreen);
        jBlue = nearestPixel(HciBlue, HcjBlue);

        finalRed = specificationHistogramme(image, "red", jRed);
        finalGreen = specificationHistogramme(image, "green", jGreen);
        finalBlue = specificationHistogramme(image, "blue", jBlue);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                output.setRGB(i, j, new Color(finalRed[i][j], finalGreen[i][j], finalBlue[i][j]).getRGB());
            }
        }
        // Analyse du résultat
        System.out.println(
                "iR\thiR\thciR\thjR\thcjR\tjR\t|\tiG\thiG\thciG\thjG\thcjG\tjG\t|\tiB\thiB\thciB\thjB\thcjB\tjB");
        for (int u = 0; u < pixelMax; u++) {
            System.out.println(u + "\t" + HiRed[u] + "\t" + HciRed[u] + "\t" + HjRed[u] + "\t" + HcjRed[u] + "\t"
                    + jRed[u] + "\t|\t" + u + "\t" + HiGreen[u] + "\t" + HciGreen[u] + "\t" + HjGreen[u] + "\t"
                    + HcjGreen[u] + "\t" + jGreen[u] + "\t|\t " + u + "\t" + HiBlue[u] + "\t" + HciBlue[u] + "\t"
                    + HjBlue[u] + "\t" + HcjBlue[u] + "\t" + jBlue[u]);
        }

        return output;
    }

    /*
     * Les fonctions suivantes sont respectivement nécessaires pour le calcul de la
     * spécifiaction d'histogramme. int [] histogramme(image, couleur), int []
     * cumulHistogram(histogramme), int [] nearestPixel(histo1, histo2), int [][]
     * specificationHistogramme(image, couleur, f(i)) avec f(i) = j
     */
    public static int[] histogramme(BufferedImage image, String color) { // color = "red" or "green" or "blue"
        int[] histo = new int[pixelMax];
        int[][] pixels = new int[image.getWidth()][image.getHeight()];

        // Comptage du nombre d'occurence de chaque pixel
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color rgbPicColor = new Color(image.getRGB(i, j));
                if (color == "red")
                    pixels[i][j] = rgbPicColor.getRed();
                else if (color == "green")
                    pixels[i][j] = rgbPicColor.getGreen();
                else if (color == "blue")
                    pixels[i][j] = rgbPicColor.getBlue();

                // Nombre d'occurence du niveau de couleur
                for (int k = 0; k < pixelMax; k++) {
                    if (pixels[i][j] == k)
                        histo[k]++;
                }
            }
        }

        return histo;
    }
    public static int[] histogramme(int[][] table) { // color = "red" or "green" or "blue"
        int[] histo = new int[pixelMax];
        int[][] pixels = table;

        // Comptage du nombre d'occurence de chaque pixel
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                // Nombre d'occurence du niveau de couleur
                for (int k = 0; k < pixelMax; k++) {
                    if (pixels[i][j] == k)
                        histo[k]++;
                }
            }
        }
        return histo;
    }

    public static int[] cumulHistogram(int[] histogram) {
        int[] cumulHisto = new int[pixelMax];
        int counter = 0;
        for (int i = 0; i < pixelMax; i++) {
            counter += histogram[i];
            cumulHisto[i] = counter;
        }
        return cumulHisto;
    }

    // Comparaison des deux histogrammes cumulées, retourne un tableau contenant les
    // valeurs de pixels transformées : f(i) = j
    public static int[] nearestPixel(int[] histogram1, int[] histogram2) {
        int[] nearestIndex = new int[pixelMax];
        int[][] difference = new int[pixelMax][pixelMax];

        for (int i = 0; i < pixelMax; i++) {
            for (int j = 0; j < pixelMax; j++) {
                difference[i][j] = Math.abs(histogram1[i] - histogram2[j]);
            }
            for (int k = 1; k < pixelMax; k++) {
                if (difference[i][k - 1] < difference[i][k]) {
                    nearestIndex[i] = k - 1;
                    break;
                }
                if (k == pixelMax - 1) {
                    nearestIndex[i] = k;
                }
            }
        }
        return nearestIndex;
    }

    public static int[][] specificationHistogramme(BufferedImage image, String color, int[] jColor) {
        int[][] finalColor = new int[image.getWidth()][image.getHeight()];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color unPixelImage = new Color(image.getRGB(i, j));
                if (color == "red") {
                    finalColor[i][j] = unPixelImage.getRed();
                } else if (color == "green") {
                    finalColor[i][j] = unPixelImage.getGreen();
                } else if (color == "blue") {
                    finalColor[i][j] = unPixelImage.getBlue();
                }
                for (int k = 0; k < pixelMax; k++) {
                    if (finalColor[i][j] == k) {
                        finalColor[i][j] = jColor[k];
                        break;
                    }
                }
            }
        }
        return finalColor;
    }
    /*
     *
     * Fin algorithme de spécification d'histogramme
     *
     */

    public static BufferedImage linearFilter(BufferedImage image, float[][] filter) {
        BufferedImage output = image;

        float[][] filterTransforme = new float[filter.length][filter[0].length];
        // transformation du filtre pour faciliter le calcul
        for (int i = 0; i < filter.length; i++) {
            for (int j = 0; j < filter.length; j++) {
                filterTransforme[filter.length - 1 - i][filter[0].length - 1 - j] = filter[i][j];
            }
        }
        // Récupération de toutes les points de l'image dans 3 tableaux redInitial[][],
        // greenInitial[][], blueInitial[][]
        int[][] initialRed = new int[image.getWidth()][image.getHeight()];
        int[][] initialGreen = new int[image.getWidth()][image.getHeight()];
        int[][] initialBlue = new int[image.getWidth()][image.getHeight()];
        int[][] finalRed = new int[image.getWidth()][image.getHeight()];
        int[][] finalGreen = new int[image.getWidth()][image.getHeight()];
        int[][] finalBlue = new int[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color initialImageColor = new Color(image.getRGB(i, j));
                initialRed[i][j] = initialImageColor.getRed();
                initialGreen[i][j] = initialImageColor.getGreen();
                initialBlue[i][j] = initialImageColor.getBlue();
            }
        }
        // Transformation de l'image
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                for (int ligne = 0; ligne < filter.length; ligne++) {
                    for (int col = 0; col < filter.length; col++) {
                        if (i - 1 + ligne < 0 || j - 1 + col < 0 || i - 1 + ligne > image.getWidth() - 1 || j - 1 + col > image.getHeight() - 1) {
                            continue;
                        } else {
                            finalRed[i][j] += initialRed[i - 1 + ligne][j - 1 + col] * filterTransforme[ligne][col];
                            finalGreen[i][j] += initialGreen[i - 1 + ligne][j - 1 + col] * filterTransforme[ligne][col];
                            finalBlue[i][j] += initialBlue[i - 1 + ligne][j - 1 + col] * filterTransforme[ligne][col];
                        }
                    }
                }
                // Transformation des valeurs négatives en valeurs positives
                finalRed[i][j] = Math.abs(finalRed[i][j]);
                finalGreen[i][j] = Math.abs(finalGreen[i][j]);
                finalBlue[i][j] = Math.abs(finalBlue[i][j]);
                if (finalRed[i][j] > pixelMax - 1) {
                    finalRed[i][j] = pixelMax - 1;
                }
                if (finalGreen[i][j] > pixelMax - 1) {
                    finalGreen[i][j] = pixelMax - 1;
                }
                if (finalBlue[i][j] > pixelMax - 1) {
                    finalBlue[i][j] = pixelMax - 1;
                }
                output.setRGB(i, j, new Color(finalRed[i][j], finalGreen[i][j], finalBlue[i][j]).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage lissageConservateur(BufferedImage image) {
        BufferedImage output = image;

        // Récupération de toutes les points de l'image dans 3 tableaux redInitial[][],
        // greenInitial[][], blueInitial[][]
        int[][] initialRed = new int[image.getWidth()][image.getHeight()];
        int[][] initialGreen = new int[image.getWidth()][image.getHeight()];
        int[][] initialBlue = new int[image.getWidth()][image.getHeight()];
        int[][] finalRed = new int[image.getWidth()][image.getHeight()];
        int[][] finalGreen = new int[image.getWidth()][image.getHeight()];
        int[][] finalBlue = new int[image.getWidth()][image.getHeight()];

        // Enregistrer tous les points dans des tableaux afin de ne pas faire le calcul
        // sur des valeurs érronées de la matrice.
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color initialImageColor = new Color(image.getRGB(i, j));
                initialRed[i][j] = initialImageColor.getRed();
                initialGreen[i][j] = initialImageColor.getGreen();
                initialBlue[i][j] = initialImageColor.getBlue();
            }
        }
        // Transformation de chaque point
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // Insertion de tous les pixels voisins autour du point
                int[] voisinsRouge = new int[8];
                int[] voisinsVert = new int[8];
                int[] voisinsBleu = new int[8];
                int k = 0;
                for (int l = i - 1; l <= i + 1; l++) {
                    for (int c = j - 1; c <= j + 1; c++) {
                        // On ne prend pas le pixel du milieu.
                        if (l == i && c == j) {
                            continue;
                        }
                        if (l < 0 || c < 0 || l > image.getWidth() - 1 || c > image.getHeight() - 1) {
                            voisinsRouge[k] = 0;
                            voisinsVert[k] = 0;
                            voisinsBleu[k] = 0;
                        } else {
                            voisinsRouge[k] = initialRed[l][c];
                            voisinsVert[k] = initialGreen[l][c];
                            voisinsBleu[k] = initialBlue[l][c];
                        }
                        k++;
                    }
                }
                // Triage des points voisins
                int maxRed = 0, maxGreen = 0, maxBlue = 0, minRed = pixelMax - 1, minGreen = pixelMax - 1,
                        minBlue = pixelMax - 1, middleRed = initialRed[i][j], middleGreen = initialGreen[i][j],
                        middleBlue = initialBlue[i][j];
                // Recherche du point minimum
                for (k = 0; k < voisinsRouge.length; k++) {
                    if (voisinsRouge[k] <= minRed) {
                        minRed = voisinsRouge[k];
                    }
                }
                for (k = 0; k < voisinsVert.length; k++) {
                    if (voisinsVert[k] <= minGreen) {
                        minGreen = voisinsVert[k];
                    }
                }
                for (k = 0; k < voisinsBleu.length; k++) {
                    if (voisinsBleu[k] <= minBlue) {
                        minBlue = voisinsBleu[k];
                    }
                }

                // Recherche du point voisin maximum
                for (k = 0; k < voisinsRouge.length; k++) {
                    if (voisinsRouge[k] >= maxRed) {
                        maxRed = voisinsRouge[k];
                    }
                }
                for (k = 0; k < voisinsVert.length; k++) {
                    if (voisinsVert[k] >= maxGreen) {
                        maxGreen = voisinsVert[k];
                    }
                }
                for (k = 0; k < voisinsBleu.length; k++) {
                    if (voisinsBleu[k] >= maxBlue) {
                        maxBlue = voisinsBleu[k];
                    }
                }
                // Si point milieu < minimum => point milieu = minimum
                if (middleRed < minRed) {
                    middleRed = minRed;
                }
                if (middleGreen < minGreen) {
                    middleGreen = minGreen;
                }
                if (middleBlue < minBlue) {
                    middleBlue = minBlue;
                }
                // Si point milieu > maximum => point milieu = maximum
                if (middleRed > maxRed) {
                    middleRed = maxRed;
                }
                if (middleGreen > maxGreen) {
                    middleGreen = maxGreen;
                }
                if (middleBlue > maxBlue) {
                    middleBlue = maxBlue;
                }
                finalRed[i][j] = middleRed;
                finalGreen[i][j] = middleGreen;
                finalBlue[i][j] = middleBlue;
                // Transformation finale
                output.setRGB(i, j, new Color(finalRed[i][j], finalGreen[i][j], finalBlue[i][j]).getRGB());
            }
        }
        return output;
    }

    public static BufferedImage filtreMedian(BufferedImage image) {
        BufferedImage output = image;

        // Récupération de toutes les points de l'image dans 3 tableaux redInitial[][],
        // greenInitial[][], blueInitial[][]
        int[][] initialRed = new int[image.getWidth()][image.getHeight()];
        int[][] initialGreen = new int[image.getWidth()][image.getHeight()];
        int[][] initialBlue = new int[image.getWidth()][image.getHeight()];

        // Enregistrer tous les points dans des tableaux afin de ne pas faire le calcul
        // sur des valeurs érronées de la matrice.
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color initialImageColor = new Color(image.getRGB(i, j));
                initialRed[i][j] = initialImageColor.getRed();
                initialGreen[i][j] = initialImageColor.getGreen();
                initialBlue[i][j] = initialImageColor.getBlue();
            }
        }
        // Transformation de chaque point
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                // Pour commencer la transformation à partir de l'indice [1][1] de l'image,
                // jusqu'à l'indice max-2.
                // Insertion de tous les pixels voisins autour du point
                int taille = 9;
                int[] voisinsEtMilieuRouge = new int[taille];
                int[] voisinsEtMilieuVert = new int[taille];
                int[] voisinsEtMilieuBleu = new int[taille];
                int k = 0;
                for (int l = i - 1; l <= i + 1; l++) {
                    for (int c = j - 1; c <= j + 1; c++) {
                        // On ne prend pas le pixel du milieu.
                        if (l < 0 || c < 0 || l > image.getWidth() - 1 || c > image.getHeight() - 1) {
                            voisinsEtMilieuRouge[k] = 0;
                            voisinsEtMilieuVert[k] = 0;
                            voisinsEtMilieuBleu[k] = 0;
                        } else {
                            voisinsEtMilieuRouge[k] = initialRed[l][c];
                            voisinsEtMilieuVert[k] = initialGreen[l][c];
                            voisinsEtMilieuBleu[k] = initialBlue[l][c];
                        }
                        k++;
                    }
                }
                // On déclare et on initialise les points medianes à 0
                int medianRouge = 0, medianVert = 0, medianBleu = 0;
                // Triage des points voisins du niveau de rouge
                for (int u = 0; u < voisinsEtMilieuRouge.length; u++) {
                    for (int v = u; v < voisinsEtMilieuRouge.length; v++) {
                        if (voisinsEtMilieuRouge[u] > voisinsEtMilieuRouge[v]) {
                            int temp = voisinsEtMilieuRouge[u];
                            voisinsEtMilieuRouge[u] = voisinsEtMilieuRouge[v];
                            voisinsEtMilieuRouge[v] = temp;
                        }
                    }
                }
                // Triage des points voisins du niveau de vert
                for (int u = 0; u < voisinsEtMilieuVert.length; u++) {
                    for (int v = u; v < voisinsEtMilieuVert.length; v++) {
                        if (voisinsEtMilieuVert[u] > voisinsEtMilieuVert[v]) {
                            int temp = voisinsEtMilieuVert[u];
                            voisinsEtMilieuVert[u] = voisinsEtMilieuVert[v];
                            voisinsEtMilieuVert[v] = temp;
                        }
                    }
                }
                // Triage des points voisins du niveau de bleu
                for (int u = 0; u < voisinsEtMilieuBleu.length; u++) {
                    for (int v = u; v < voisinsEtMilieuBleu.length; v++) {
                        if (voisinsEtMilieuBleu[u] > voisinsEtMilieuBleu[v]) {
                            int temp = voisinsEtMilieuBleu[u];
                            voisinsEtMilieuBleu[u] = voisinsEtMilieuBleu[v];
                            voisinsEtMilieuBleu[v] = temp;
                        }
                    }
                }
                // Sélection du milieu(median) des points voisins triés du niveau de rouge
                for (int u = 0; u < voisinsEtMilieuRouge.length; u++) {
                    if (u == ((voisinsEtMilieuRouge.length - 1) / 2)) {
                        medianRouge = voisinsEtMilieuRouge[u];
                    }
                }
                // Sélection du milieu(median) des points voisins triés du niveau de vert
                for (int u = 0; u < voisinsEtMilieuVert.length; u++) {
                    if (u == ((voisinsEtMilieuVert.length - 1) / 2)) {
                        medianVert = voisinsEtMilieuVert[u];
                    }
                }
                // Sélection du milieu(median) des points voisins triés du niveau de bleu
                for (int u = 0; u < voisinsEtMilieuBleu.length; u++) {
                    if (u == ((voisinsEtMilieuBleu.length - 1) / 2)) {
                        medianBleu = voisinsEtMilieuBleu[u];
                    }
                }
                // TransformationFinale
                output.setRGB(i, j, new Color(medianRouge, medianVert, medianBleu).getRGB());
            }
        }
        return output;
    }

    public static void generateHistogramme(BufferedImage image, String title) { // color = "red" or "green" or "blue"
        int[] histoRed = new int[pixelMax];
        int[] histoGreen = new int[pixelMax];
        int[] histoBlue = new int[pixelMax];
        int[][] pixelRed = new int[image.getWidth()][image.getHeight()];
        int[][] pixelGreen = new int[image.getWidth()][image.getHeight()];
        int[][] pixelBlue = new int[image.getWidth()][image.getHeight()];

        // Comptage du nombre d'occurence de chaque pixel
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color rgbPicColor = new Color(image.getRGB(i, j));
                pixelRed[i][j] = rgbPicColor.getRed();
                pixelGreen[i][j] = rgbPicColor.getGreen();
                pixelBlue[i][j] = rgbPicColor.getBlue();

                // Nombre d'occurence du niveau de couleur
                for (int k = 0; k < pixelMax; k++) {
                    if(pixelRed[i][j] == k)
                        histoRed[k]++;
                    if(pixelGreen[i][j] == k)
                        histoGreen[k]++;
                    if(pixelBlue[i][j] == k)
                        histoBlue[k]++;
                }
            }
        }
        try {
            String histoRedString = new String();
            String histoGreenString = new String();
            String histoBlueString = new String();
            for (int k = 0; k < pixelMax; k++) {
                histoRedString += histoRed[k];
                if(k != pixelMax -1)
                    histoRedString += ", ";
                histoGreenString += histoGreen[k];
                if(k != pixelMax -1)
                    histoGreenString += ", ";
                histoBlueString += histoBlue[k];
                if(k != pixelMax -1)
                    histoBlueString += ", ";
            }
            File fileHisto = new File("./assets/histogramme.html");
            FileWriter fwHisto = new FileWriter(fileHisto.getAbsolutePath());
            BufferedWriter writeHisto = new BufferedWriter(fwHisto);
            writeHisto.write("<!DOCTYPE html>");
            writeHisto.write("<head><meta charset=UTF-8><meta name=viewport content=width=device-width, initial-scale=1.0><title>Histogramme : " + title + "</title><script src=Chart.js></script></head>\n");
            writeHisto.write("<body style='min-width : 350px; width: 50%; position: relative; margin: 0 auto;'><h4 style='text-align: center; font-family: sans-serif'>" + title + "</h4><canvas id=Histogramme></canvas><canvas id=HistogrammeRouge></canvas><canvas id=HistogrammeVert></canvas><canvas id=HistogrammeBleu></canvas><script>var ctx = document.getElementById('Histogramme');var ctxRouge = document.getElementById('HistogrammeRouge');var ctxVert = document.getElementById('HistogrammeVert');var ctxBleu = document.getElementById('HistogrammeBleu');var pixel= [];var redLine='rgb(235, 90, 90)';var greenLine='rgb(29, 131, 63)';var blueLine='rgb(52, 195, 214)';var redBackgroundColor = [];var greenBackgroundColor = [];var blueBackgroundColor = [];var redBorderColor = [];var greenBorderColor = [];var blueBorderColor = [];var histogrammeRouge = [];var histogrammeVert = [];var histogrammeBleu = [];for(var i = 0 ; i < 256 ; i++){pixel[i] = i;redBackgroundColor[i] = 'rgb(235, 90, 90)';redBorderColor[i] = 'rgb(235, 90, 90)';greenBackgroundColor[i] = 'rgb(29, 131, 63)';greenBorderColor[i] = 'rgb(29, 131, 63)';blueBackgroundColor[i] = 'rgb(52, 195, 214)';blueBorderColor[i] = 'rgb(52, 195, 214)';}\n");
            writeHisto.write("var myChart = new Chart(ctx, {type: 'line',data: {labels: pixel,datasets: [{label: 'Rouge',data: [" + histoRedString + "],borderColor: redLine,fill: false,pointRadius: 0,borderWidth: 1},{label: 'Vert',data: [" + histoGreenString + "],borderColor: greenLine,fill: false,pointRadius: 0,borderWidth: 1},{label: 'Bleu',data: [" + histoBlueString + "],borderColor: blueLine,fill: false,pointRadius: 0,borderWidth: 1}]},options: {scales: {y: {beginAtZero: true}}}});\n");
            writeHisto.write("myChart = new Chart(ctxRouge, {type: 'bar',data: {labels: pixel,datasets: [{label: 'histogramme rouge',data: [" + histoRedString + "],backgroundColor: redBackgroundColor,borderColor: redBorderColor,borderWidth: 1}]},options: {scales: {y: {beginAtZero: true}}}});\n");
            writeHisto.write("myChart = new Chart(ctxVert, {type: 'bar',data: {labels: pixel,datasets: [{label: 'histogramme vert',data: [" + histoGreenString + "],backgroundColor: greenBackgroundColor,borderColor: greenBorderColor,borderWidth: 1}]},options: {scales: {y: {beginAtZero: true}}}});\n");
            writeHisto.write("myChart = new Chart(ctxBleu, {type: 'bar',data: {labels: pixel,datasets: [{label: 'histogramme bleu',data: [" + histoBlueString + "],backgroundColor: blueBackgroundColor,borderColor: blueBorderColor,borderWidth: 1}]},options: {scales: {y: {beginAtZero: true}}}});</script></body></html>\n");

            writeHisto.close();

             if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                try {
                    String repCourant = System.getProperty("user.dir");
                    char [] repC = repCourant.toCharArray();
                    repCourant = "";
                    for(int i = 0 ; i < repC.length ; i++){
                        if(repC[i] == ' '){
                            repCourant += "%20";
                            continue;
                        }
                        repCourant += (repC[i] == '\\') ? '/' : repC[i];
                    }
             
                    Desktop.getDesktop().browse(new URI("file:///" + repCourant + "/assets/histogramme.html"));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
            }

        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static void generateZoom(BufferedImage image){
        try {
            File destination = new File("./assets/zoomImage.jpg");
            ImageIO.write(image, "jpg", destination);
            File fileHisto = new File("./assets/zoomImage.html");
            FileWriter fwHisto = new FileWriter(fileHisto.getAbsolutePath());
            BufferedWriter writeHisto = new BufferedWriter(fwHisto);
            writeHisto.write("<!DOCTYPE html>");
            writeHisto.write("<head><meta charset=UTF-8><meta name=viewport content=width=device-width, initial-scale=1.0><title>Aperçu</title><script src=Chart.js></script></head>\n");
            writeHisto.write("<body>\n");
            writeHisto.write("<img src=\"zoomImage.jpg\" style='min-width: 800px'></body></html>\n");

            writeHisto.close();

             if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                try {
                    String repCourant = System.getProperty("user.dir");
                    char [] repC = repCourant.toCharArray();
                    repCourant = "";
                    for(int i = 0 ; i < repC.length ; i++){
                        if(repC[i] == ' '){
                            repCourant += "%20";
                            continue;
                        }
                        repCourant += (repC[i] == '\\') ? '/' : repC[i];
                    }
             
                    Desktop.getDesktop().browse(new URI("file:///" + repCourant + "/assets/zoomImage.html"));
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
            }

        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}