// import java.io.IOException;

// import javax.swing.*;

// public class HistogramFrame extends JFrame{

//     /**
//      *
//      */
//     private static final long serialVersionUID = 1L;
//     public HistogramFrame(){
//         JEditorPane editor = new JEditorPane();
//         editor.setEditable(false);
//         String repCourant = System.getProperty("user.dir");
//         try{
//             char[] repC = repCourant.toCharArray();
//             repCourant = "";
//             for (int i = 0; i < repC.length; i++)
//             repCourant += (repC[i] == '\\') ? '/' : repC[i];
//             editor.setPage("file:///" + repCourant + "/histogramme.html");
//         }catch(IOException e){
//             editor.setContentType("Text/html");
//             editor.setText("La page n'a pas pu charger");
//         }
//         JScrollPane scrollPane = new JScrollPane(editor);
//         JFrame f = new JFrame("file:///" + repCourant + "/histogramme.html");
//         f.getContentPane().add(scrollPane);
//         f.setSize(700, 400);
//         f.setVisible(true);
//         f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//         // this.setDefaultCloseOperation(HIDE_ON_CLOSE);
//         // this.setLocationRelativeTo(null);
//         // this.setLayout(new BorderLayout());
//         // this.setBounds(600, 100, 500, 400);
//         // this.setResizable(false);
//         // this.add(histogramPanel, BorderLayout.CENTER);
//         // this.setVisible(true);
//     }
// }
