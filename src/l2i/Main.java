/*
 * By Fugius, 06/02/2018
 */

package l2i;

import base.ImgList;;

public class Main {

	public static void main(String[] args) {
		ImgList l = new ImgList("res");
		
		if (!l.loadHistos())
			l.createHistogram();
		
		l.compareTo("res/g2.jpg");
	}

}
