package core;

public class Histogram {
	
	private int[] data = null;
	
	public Histogram() {
		
	}
	
	public void setDatas(int[] h) {
		data = new int[h.length];
		data = h.clone();
	}
	
	public int[] getDatas() {
		return data;
	}
	
	
	//compare the target with me ... i.e : i am the model
	public float compare(Histogram b) {
		
		if (data != null && b.getDatas() == null) {
			System.out.println("Initialize histograms before");
		}
		
		int [] hb = b.getDatas().clone();
		
		int mins = 0;
		int modelTotal = 0;
		
		for (int i = 0; i < data.length; i++) {
			mins += (data[i] > hb[i]) ? hb[i] : data[i];
			modelTotal += data[i];
		}
				
		return (float) mins / (float) modelTotal;
	}
}
