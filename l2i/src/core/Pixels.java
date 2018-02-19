package core;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import core.Histogram;

public class Pixels {
	
	private BufferedImage img = null;
	private Histogram histo = null;
	
	private String ipath;
	
	private int bin1, bin2, bin3;
	
	public Pixels() {
		bin1 = 16;
		bin2 = 16;
		bin3 = 8;
	}
	
	public Pixels(String path) {
		this.loadFromFile(path);
		
		bin1 = 16;
		bin2 = 16;
		bin3 = 8;
	}
	
	public void loadFromFile(String path) {
		File f = null;
		
		//read img
		try {
			f = new File(path);
			img = ImageIO.read(f);
			
			ipath = f.getPath();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	
	//need to regen the histograms after
	public void setBinsSize(int b1, int b2, int b3) {
		bin1 = b1;
		bin2 = b2;
		bin3 = b3;
	}
	
	public String getPath() {
		return ipath;
	}
	
	public Histogram createHistogram() {
		//Is the img loaded ?
		if (img == null) {
			System.out.println("Load img before");
			return null;
		}
		
		if (histo != null) {
			System.out.println("Histogram already loaded !");
			return null;
		}
		
		//instantiate histogram
		histo = new Histogram();
		
		//histogram [rg][by][wb]
		int [][][] histogram = new int[bin1][bin2][bin3];
		int [] histogram_l = new int[bin1*bin2*bin3];
		
		//get pic size
		int width = img.getWidth();
		int height = img.getHeight();
		
		//get histogram following s&b method
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				
				int r = (img.getRGB(i, j) >> 16) & 0xff;
				int g = (img.getRGB(i, j) >> 8) & 0xff;
				int b = img.getRGB(i, j) & 0xff;
				
				int rg = r - g + 255; // shift by 255 to get positive numbers
				int by = 2 * b - r - g + 510; // shift by 510 to get positive numbers
				int wb = r + g + b;
				
				rg /= (510 / (bin1 - 1)); //rg ∈ [0; 510]
				by /= (1020 / (bin2 - 1)); // by ∈ [0;1020]
				wb /= (765 / (bin3 - 1)); // wb ∈ [0; 765]
				
				histogram[rg][by][wb]++; 
			}
		}
		
		int l = 0;
		
		//make the raw histogram linear
		for (int i = 0; i < bin1; i++) {
			for (int j = 0; j < bin2; j++) {
				for(int k = 0; k < bin3; k++) {
					histogram_l[l] = histogram[i][j][k];
					l++;
				}
			}
		}
		
		histo.setDatas(histogram_l);
		return histo;
		
	}
	
	public void setHistogram(String s) {
		String hs = s.split("]")[s.split("]").length - 1];
		
		String[] rawString = hs.split(" ");
		
		int[] rawDatas = new int[rawString.length - 1];
						
		for (int i = 0; i < rawDatas.length; i++) {
			try {
				if (!rawString[i].isEmpty())
					rawDatas[i] = Integer.parseInt(rawString[i + 1]);
			} catch (NumberFormatException nfe) {
				System.out.println("Format Error getting Histogram from saves file ! " + nfe.getMessage() + "\n");
			} catch (Exception e) {
				System.out.println("Thrown exception loading histogram from file : " + e.getMessage());
			}
		}
		
		histo = new Histogram();
		histo.setDatas(rawDatas);
				
		ipath = s.split("]")[0];
		ipath = ipath.split("\\[")[1];
		
	}
	
	public Histogram getHistogram() {
		return histo;
	}
}
