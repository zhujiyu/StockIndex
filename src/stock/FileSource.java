package stock;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

public class FileSource extends DataSource {

	public FileSource(String _code, String _market) {
		super(_code, _market);
	}

	public void read(Calendar start, Calendar end) throws IOException {
		String filename = "data/" + stock_code + ".csv";
		File file = new File(filename);
        BufferedInputStream bis = null;

        try {
        	bis = new BufferedInputStream(new FileInputStream(file));
        	readCSVData(new InputStreamReader(bis));
        	bis.close();
        } catch( IOException ex ) {
        	ex.printStackTrace();
        } finally {
        	if( bis != null )
        		bis.close();
        }
        
	}
	
	@Override
	public List<List<String>> get(Calendar start, Calendar end)
			throws IOException {
		read(start, end);
		return super.price_list;
	}

}
