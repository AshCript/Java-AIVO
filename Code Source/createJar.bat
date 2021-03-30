javac ImageEditing.java PicPanel.java com/as/func/*.java
jar -cvfm ImageEditing.jar manifest.txt ImageEditing.class PicFrame.class ZoomTest.class PicPanel.class ImagePanel.class ImageZoom$1.class ImageZoom$2.class ImageZoom.class ./com/as/func/Fonctions.class
java ImageEditing