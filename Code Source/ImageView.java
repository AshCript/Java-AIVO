
// import java.awt.BorderLayout;
// import java.awt.image.BufferedImage;
// import java.io.File;

// import javax.imageio.ImageIO;
// import javax.swing.JFrame;
// import javax.swing.JMenu;
// import javax.swing.JMenuBar;
// import javax.swing.JMenuItem;
// import javax.swing.JOptionPane;

// public class ImageView extends JFrame {
//     /**
//      *
//      */
//     private static final long serialVersionUID = 1L;
//     PicPanel picPanel = new PicPanel();
//     private JMenuBar menubar = new JMenuBar();

//     public ImageView() {
//         this.setTitle("Image Finale apres transformation");
//         JMenu menu = new JMenu("Fichier");
//         JMenuItem save = new JMenuItem("Enregistrer");
//         menu.add(save);
//         menubar.add(menu);
//         // this.setJMenuBar(menubar);
//         this.add(picPanel, BorderLayout.CENTER);
//         this.setDefaultCloseOperation(HIDE_ON_CLOSE);
//         this.setJMenuBar(menubar);
//         this.setLocationRelativeTo(null);
//         this.setLayout(new BorderLayout());
//         this.add(picPanel, BorderLayout.CENTER);
//         this.setBounds(600, 100, 500, 400);
//         this.setResizable(false);
//         save.addActionListener(e -> {
//             BufferedImage image = picPanel.getImage();
//             String nom_image = new String();
//             try {
//                 do{
//                     nom_image = JOptionPane.showInputDialog(null, "Nom de l'image", null, JOptionPane.QUESTION_MESSAGE);
//                 }while(nom_image.isEmpty());
//                 File destination = new File("../pics/" + nom_image + ".jpg");
//                 ImageIO.write(image, "jpg", destination);
//                 JOptionPane.showMessageDialog(null, "L'image \""+nom_image+".jpg\" est sauvegarde avec succes!", null, JOptionPane.DEFAULT_OPTION);
//             } catch (Exception e1) {
//             }
//         });
//     }
// }
