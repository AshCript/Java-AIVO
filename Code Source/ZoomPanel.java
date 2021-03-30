import java.awt.Graphics;
import java.awt.image.*;

import javax.swing.JPanel;

public class ZoomPanel extends JPanel{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    public Graphics g;
    public int picWidth = 0;
    public int picHeight = 0;
    @Override
    protected void paintComponent(Graphics g) {
        this.g = g;
        picWidth = image.getWidth();
        picHeight = image.getHeight();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
    }
    public void zoomComponent() {
        picWidth += 10;
        picHeight += 10;
        g.drawImage(image, 0, 0, picWidth, picHeight, null);
    }
    public BufferedImage getImage(){
        return image;
    }
    public void setImage(BufferedImage image){
        this.image = image;
    }
}