package rssHandler;

import models.Mannschaft;

public class DataHelper {
	Mannschaft mh;
	Mannschaft mg;
	Byte th;
	Byte tg;
	
	public DataHelper(){
		
	}
	
	public DataHelper(Mannschaft mh, Mannschaft mg, Byte th, Byte tg){
    	this.mh=mh;
    	this.mg=mg;
    	this.th=th;
    	this.tg=tg;
    }
}
