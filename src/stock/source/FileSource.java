package stock.source;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class FileSource extends DataSource {

	public FileSource(String _code, String _market) {
		super(_code, _market);
	}

//	public void read(Calendar start, Calendar end) throws IOException {
//		String filename = "data/" + stock_code + ".csv";
//		
//		File file = new File(filename);
//		FileInputStream fis = new FileInputStream(file);
//        BufferedInputStream bis = new BufferedInputStream(fis);
//
//        try {
////        	bis = new BufferedInputStream(new FileInputStream(file));
//        	readCSVData(new InputStreamReader(bis));
////        	bis.close();
//        } catch( IOException ex ) {
//        	ex.printStackTrace();
//        } finally {
////        	if( bis != null )
//        	bis.close();
//        	fis.close();
//        }
//        
//	}
	
	@Override
	public List<List<String>> get(Calendar start, Calendar end)
			throws IOException {
		String filename = "data/" + stock_code + ".csv";
		
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        try {
        	readCSVData(bis);
//        	bis = new BufferedInputStream(new FileInputStream(file));
//        	readCSVData(new InputStreamReader(bis));
//        	bis.close();
        } catch( IOException ex ) {
        	ex.printStackTrace();
        } finally {
//        	if( bis != null )
        	bis.close();
        	fis.close();
        }
        
		return super.price_list;
	}

}
