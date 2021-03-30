// import java.awt.*;
// import java.awt.event.*;
// import java.awt.image.*;
// import javax.swing.*;

// public class ZoomFrame extends JFrame{
//     /**
//      *
//      */
//     private static final long serialVersionUID = 1L;

//     public ZoomFrame(BufferedImage image){
//         ZoomPanel panel = new ZoomPanel();
//         JButton zoomIn = new JButton("+");
//         //JButton zoomOut = new JButton("-");

//         panel.setImage(image);
        
//         this.setDefaultCloseOperation(HIDE_ON_CLOSE);
//         this.setLocationRelativeTo(null);
//         this.setLayout(new BorderLayout());
//         this.setTitle("Zoom de l'image");
//         Image icon = Toolkit.getDefaultToolkit().getImage("./img/logo/icon.png");
//         this.setIconImage(icon);
//         this.setBounds(100, 100, 500, 500);
//         this.setResizable(false);
//         this.add(panel, BorderLayout.CENTER);
//         // this.add(zoomIn, BorderLayout.NORTH);
//         // this.add(zoomOut, BorderLayout.NORTH);
//         this.setVisible(true);
//         zoomIn.addActionListener(new ActionListener(){
//             public void actionPerformed(ActionEvent e){
//                 panel.zoomComponent();
//             }
//         });
//     }
// }
