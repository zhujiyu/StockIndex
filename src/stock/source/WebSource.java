package stock.source;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

public class WebSource extends DataSource {

	public WebSource(String _code, String _market) {
		super(_code, _market);
	}

	protected String url = "http://table.finance.yahoo.com/table.csv";
	// http://table.finance.yahoo.com/table.csv?a=0&b=1&c=2012&d=3&e=19&f=2012&s=600000.ss

    public String getDataUrl(Calendar start, Calendar end) {
		String stock_url = String.format("%s?a=%d&b=%d&c=%d&d=%d&e=%d&f=%d&s=%s.%s", 
				url,
				start.get(Calendar.MONTH), start.get(Calendar.DATE), start.get(Calendar.YEAR), 
				end.get(Calendar.MONTH), end.get(Calendar.DATE), end.get(Calendar.YEAR), 
				stock_code, market);
		return stock_url;
    }

	@Override
	public List<List<String>> get(Calendar start, Calendar end)
			throws IOException {
		String _url = getDataUrl(start, end);
    	URL urlfile = new URL(_url);
    	HttpURLConnection httpUrl = (HttpURLConnection)urlfile.openConnection();
    	
        httpUrl.connect();
        InputStream stream = httpUrl.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(stream);

        try {
        	readCSVData(bis);
//        	readCSVData(new InputStreamReader(bis));
        } catch( IOException ex ) {
        	ex.printStackTrace();
        } finally {
        	if( bis != null )
        		bis.close();
        }
        
        stream.close();
    	httpUrl.disconnect();
		
		return price_list;
	}

//  private void download(Calendar start, Calendar end) throws IOException  {
//  	URL urlfile = new URL(getDataUrl(start, end));
//  	HttpURLConnection httpUrl = (HttpURLConnection)urlfile.openConnection();
//      httpUrl.connect();
//      BufferedInputStream bis = null;
//
//      try {
//      	bis = new BufferedInputStream(httpUrl.getInputStream());
//      	readCSVData(new InputStreamReader(bis));
//      	bis.close();
//      } catch( IOException ex ) {
//      	ex.printStackTrace();
//      } finally {
//      	if( bis != null )
//      		bis.close();
//      }
//      
//  	httpUrl.disconnect();
//  }

}
