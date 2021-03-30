import java.awt.Graphics;
import java.awt.image.*;

import javax.swing.JPanel;

public class PicPanel extends JPanel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
    }
    public BufferedImage getImage(){
        return image;
    }
    public void setImage(BufferedImage image){
        this.image = image;
    }
}