package stock.source;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private void download(Calendar start, Calendar end) throws IOException  {
    	URL urlfile = new URL(getDataUrl(start, end));
    	HttpURLConnection httpUrl = (HttpURLConnection)urlfile.openConnection();
        httpUrl.connect();
        BufferedInputStream bis = null;

        try {
        	bis = new BufferedInputStream(httpUrl.getInputStream());
        	readCSVData(new InputStreamReader(bis));
        	bis.close();
        } catch( IOException ex ) {
        	ex.printStackTrace();
        } finally {
        	if( bis != null )
        		bis.close();
        }
        
    	httpUrl.disconnect();
    }

	@Override
	public List<List<String>> get(Calendar start, Calendar end)
			throws IOException {
		download(start, end);
		return super.price_list;
	}
    
//	@Override
//	public List< List<String> > get(Date start) throws IOException {
//		Calendar start_time = new GregorianCalendar();
//		start_time.setTime(start);
//		Calendar end_date = new GregorianCalendar();
//
//		super.clear();
//		download(start_time, end_date);
//		return data;
//	}
//
//	@Override
//	public List<List<String>> get(Date start, Date end) throws IOException {
//		Calendar start_time = new GregorianCalendar();
//		start_time.setTime(start);
//		Calendar end_date = new GregorianCalendar();
//		end_date.setTime(end);
//		
//		super.clear();
//		download(start_time, end_date);
//		return data;
//	}

//	public StockData get(Calendar start, Calendar end) throws IOException {
//		URL urlfile = null;
//        HttpURLConnection httpUrl = null;
//        BufferedInputStream bis = null;
//        StockData map = new StockData(this.stock_code, this.market);
//
//    	urlfile = new URL(getDataUrl(start, end));
//        httpUrl = (HttpURLConnection)urlfile.openConnection();
//        httpUrl.connect();
//
//        bis = new BufferedInputStream(httpUrl.getInputStream());
//        data = readCSVFile(new InputStreamReader(bis));
//        map.Parse(data);
//
//    	httpUrl.disconnect();
//        bis.close();
//        
////        try
////        {
////        }
////        catch (Exception e)
////        {
////            e.printStackTrace();
////        }
////        finally
////        {
////        	httpUrl.disconnect();
////            try
////            {
////                bis.close();
////            }
////            catch (IOException e)
////            {
////                e.printStackTrace();
////            }
////        }
//
//        return map;
//	}

}
