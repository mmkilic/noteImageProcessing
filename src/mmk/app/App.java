package mmk.app;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

public class App {
	private static final String WD = System.getProperty("user.dir");
	public static void main(String[] args) {
		
		pdf2Jpeg("cover-1");
		pdf2Jpeg("cover-2");
		imageComparing("cover-1-1.jpg", "cover-2-1.jpg");
		
	}
	private static void pdf2Jpeg(String fileName) {
		try {
			System.out.println(fileName + " has been started.");
		    PDDocument document = PDDocument.load(new File(WD + "\\resources\\pdf\\" + fileName + ".pdf"));
		    PDFRenderer pdfRenderer = new PDFRenderer(document);
		    for (int page = 0; page < document.getNumberOfPages(); ++page) {
		        BufferedImage bim = pdfRenderer.renderImageWithDPI(
		          page, 600, ImageType.RGB);
		        ImageIOUtil.writeImage(
		          bim, "resources/image/" + fileName + "-" + (page + 1) + ".jpg", 600);
		    }
		    document.close();
		    System.out.println("it is completed.");
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	private static void imageComparing(String FirstImage, String SecondImage) {
		try {
			BufferedImage img1 = ImageIO.read(new File("resources/image/" + FirstImage));
			BufferedImage img2 = ImageIO.read(new File("resources/image/" + SecondImage));
			
			int w1 = img1.getWidth();
			int w2 = img2.getWidth();
			int h1 = img1.getHeight();
			int h2 = img2.getHeight();

			if(w1 != w2 || h1 != h2) {
				System.out.println("They are not the similar images");
				return;
			}
			
			BufferedImage imgf = new BufferedImage(w1, h1, img1.getType());
			
			for (int j = 0; j < h1; j++) {
				for (int i = 0; i < w1; i++) {
					int pixel1 = img1.getRGB(i, j);
					int pixel2 = img2.getRGB(i, j);
					   
					if(pixel1 == pixel2)
						imgf.setRGB(i, j, pixel1);
					else {
						if(pixel2 == Color.WHITE.getRGB()) {
							// blue for removed lines
							Color c = new Color(pixel1);
							imgf.setRGB(i, j, new Color(c.getRed(), c.getGreen(), 255).getRGB());
						}
						else {
							//red for added lines
							Color c = new Color(pixel2);
							imgf.setRGB(i, j, new Color(255, c.getGreen(), c.getBlue()).getRGB());
						}
					}
	            }
			}
			System.out.println(ImageIO.write(imgf, "jpg", new File("resources/image/compared.jpg")));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
