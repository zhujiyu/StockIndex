package stock.source;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class DataSource {

	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	protected String stock_code = "601857";
	protected String market = "ss";
	protected List< List<String> > price_list = new ArrayList< List<String> >();
	
	public DataSource(String _code, String _market) {
		stock_code = _code;
		market = _market;
	}

//	abstract public List< List<String> > get(Date start) throws IOException;
	abstract public List< List<String> > get(Calendar start, Calendar end) 
			throws IOException;
	
	/**
	 * ����csv�ļ� ��һ��list�� ÿ����Ԫ��Ϊһ��String���ͼ�¼��
	 * ÿһ��Ϊһ��list�� �ٽ����е��зŵ�һ����list��
	 * @throws IOException 
	 */
	public List< List<String> > readCSVData(InputStreamReader fr) 
			throws IOException {
		BufferedReader br = new BufferedReader(fr);
		String rec = null;// һ��
		String str;// һ����Ԫ��
//		List<List<String>> listFile = new ArrayList< List<String> >();

		Iterator< List<String> > iter = price_list.iterator();
		while( iter.hasNext() ) {
			iter.next().clear();
		}
		price_list.clear();
		
		try {
			// ��ȡһ��
			while ((rec = br.readLine()) != null) {
				Pattern pCells = Pattern
						.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
				Matcher mCells = pCells.matcher(rec);
				List<String> cells = new ArrayList<String>();// ÿ�м�¼һ��list
				// ��ȡÿ����Ԫ��
				while (mCells.find()) {
					str = mCells.group();
					str = str.replaceAll(
							"(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
					str = str.replaceAll("(?sm)(\"(\"))", "$2");
					cells.add(str);
				}
				
				price_list.add(cells);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (br != null) {
				br.close();
			}
			if (fr != null) {
				fr.close();
			}
		}
		
		return price_list;
	}

//	public void clear() {
//		Iterator< List<String> > iter = price_list.iterator();
//		while( iter.hasNext() ) {
//			iter.next().clear();
//		}
//		price_list.clear();
//	}
	
	public void print() {
		Iterator< List<String> > iter = price_list.iterator();
		while( iter.hasNext() ) {
			List<String> _data = iter.next();
			System.out.println(_data);
		}
	}

	/**  
     * ����Զ���ļ������浽����  
     * @param remoteFilePath Զ���ļ�·��   
     * @param localFilePath �����ļ�·��  
     */
    public void downloadData(String remoteFilePath, String localFilePath)
    {
        URL urlfile = null;
        HttpURLConnection httpUrl = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        File f = new File(localFilePath);
        
        try
        {
            urlfile = new URL(remoteFilePath);
            httpUrl = (HttpURLConnection)urlfile.openConnection();
            httpUrl.connect();
            bis = new BufferedInputStream(httpUrl.getInputStream());
            
            bos = new BufferedOutputStream(new FileOutputStream(f));
            int len = 2048;
            byte[] b = new byte[len];
            
            while ((len = bis.read(b)) != -1)
            {
                bos.write(b, 0, len);
            }
            bos.flush();
            bis.close();
            httpUrl.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                bis.close();
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
	
}