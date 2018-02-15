package base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import core.Pixels;

public class ImgList {
	
	private ArrayList<Pixels> datas = null;
	
	private boolean files_loaded = false;
	private boolean histo_created = false;
	private boolean histo_loaded = false;
	
	private String folderPath;
	
	public ImgList(String path) {
		
		datas = new ArrayList<Pixels>();
		folderPath = path;

	}
	
	protected boolean allHistosLoaded() {
		//check if all images were in the data file
		File folder = new File(folderPath);
		File[] list = folder.listFiles();
		
		int imgCount = 0;
		
		for (File f : list) {
			int dotIndex = f.getName().lastIndexOf(".");
			String ext = f.getName().substring(dotIndex + 1);
			if (ext.contains("jpg") ||ext.contains("png") || ext.contains("bmp") || ext.contains("gif")) 
				imgCount++;
			
		}		
		
		System.out.println(imgCount + " - " + datas.size());
		
		if (imgCount != datas.size()) {
			return false;
		}
		
		return true;

	}
	
	
	protected void loadFiles() {
		File folder = new File(folderPath);
		File[] list = folder.listFiles();
		
		datas = new ArrayList<Pixels>();
		
		for (File f : list) {
			if(f.isFile()) {
				
				int dotIndex = f.getName().lastIndexOf(".");
				
				if (dotIndex == -1)
					continue;
							
				String ext = f.getName().substring(dotIndex + 1);
				if (ext.contains("jpg") ||ext.contains("png") || ext.contains("bmp") || ext.contains("gif")) {
					datas.add(new Pixels(f.getPath()));
				}
				
			}
			
		}
		
		files_loaded = true;
	}
	
	public void createHistogram() {
		if (datas.isEmpty() || datas == null) {
			System.out.println("Files not loaded !");
			loadFiles();
		}
		
		for (int i = 0; i < datas.size(); i++) {
			datas.get(i).createHistogram();
		}
		
		histo_created = true;
		System.out.println("Histograms created !");
		
		this.saveHistos();

	}
	
	protected void saveHistos() {
		if (!files_loaded || !histo_created) {
			System.out.println("Load files AND create histograms before writing them !");
			return;
		}
		
		try {
			FileWriter writer = new FileWriter(folderPath + "/Histograms");
			
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			for (Pixels p : datas) {
				bufferedWriter.write("[" + p.getPath() + "]");
				
				for (int i : p.getHistogram().getDatas()) {
					bufferedWriter.write(" " + String.valueOf(i));
				}
				
				bufferedWriter.newLine();
				
			}
			bufferedWriter.close();
			
			System.out.println("Histograms saved !");
			
		} catch (Exception e) {
			System.out.println("Error writing files in : " + folderPath );
		}
	}
	
	public boolean loadHistos() {
		
		if (histo_loaded)
			return true;
		
		String line = null;
		
		try {			
			FileReader reader = new FileReader(folderPath + "/Histograms");
			
			BufferedReader bufferedReader = new BufferedReader(reader);
			
			while ((line = bufferedReader.readLine()) != null) {
				Pixels p = new Pixels();
				p.setHistogram(line);
				datas.add(p);
			}
			
			bufferedReader.close();
			
			//check if all images were in the data file
			if (!allHistosLoaded()) {
				System.out.println("Data Files  not up to date ... updating");
				loadFiles();
				createHistogram();
				return true;
			}
			
			histo_created = true;
			files_loaded = true;
			histo_loaded = true;
			
			return true;
			
		} catch (FileNotFoundException e1) {
			System.out.println("File not found " + folderPath + "/Histograms , can't load Histograms from it !");
			return false;
			
		} catch (Exception e) {
			System.out.print("Error reading files in : " + folderPath + "  ! \n " + e + "\n");
			return false;
			
		}
	}
	
	public HashMap<String, Float> compareTo(String path) {
		
		if (!files_loaded || !histo_created) {
			System.out.println("Load files AND create histograms before comparing images !");
			return null;
		}
		
		HashMap<String, Float> tempList = new HashMap<String, Float>();
		
		Pixels b = new Pixels(path);
		b.createHistogram();
		
		for (int i = 0; i < datas.size(); i++) {
			tempList.put(datas.get(i).getPath(), b.getHistogram().compare(datas.get(i).getHistogram()));
		}
		
		ValueComparator vc = new ValueComparator(tempList);
		TreeMap<String, Float> sorted_map = new TreeMap<String, Float>(vc);
		
		sorted_map.putAll(tempList);
		tempList = new HashMap<String, Float>();
		
		for (Entry<String, Float> es : sorted_map.entrySet()) {
			tempList.put(es.getKey(), es.getValue());
			System.out.println(path + " <-> " + es.getKey() + " = " + es.getValue()*100);
		}
		
		return tempList;
	}
}

class ValueComparator implements Comparator<String> {
    Map<String, Float> base;

    public ValueComparator(Map<String, Float> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
