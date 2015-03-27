package stock.source;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**新浪获取股票当前交易信息的接口.<br>
 * http://www.cnblogs.com/yunzi/p/3213022.html
 * @author zhujiyu
 */
public class SinaAPI {
	public static final String STOCK_URL = "http://hq.sinajs.cn/list=";
	public static final String INDEX_URL = "http://hq.sinajs.cn/list=s_";

	public String name, code;
	
	protected String download(String _url) throws IOException {
		String text = "", temp;
    	HttpURLConnection httpUrl = (HttpURLConnection)new URL(_url).openConnection();
    	
        BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));

        try {
        	while( (temp = br.readLine()) != null ) {
        		text += temp;
        	}
        } catch( IOException ex ) {
        	ex.printStackTrace();
        } finally {
        	br.close();
        	bis.close();
        	httpUrl.disconnect();
        }
        
		return text;
	}

}
