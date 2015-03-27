package stock.source;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinaIndex extends SinaAPI {

	public SinaIndex(String code) {
		this.code = code;
	}

	public double price;
	public double bianh;
	public double bilv;
	public double volume;
	
	public double getNewPrice() {
		try {
			String text = download(INDEX_URL + code);
			this.Parse(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return price;
	}

	// var hq_str_s_sh000001="��ָ֤��,3691.096,9.001,0.24,4089451,50929846";
	public void Parse(String text) {
		Pattern sname  = Pattern.compile("[\\w\\s]+=\"([^,]+),");
		Pattern sprice = Pattern.compile(",(\\d+.\\d+)");
		Pattern svolume = Pattern.compile(",(\\d+)");
		
		Matcher mCells = sname.matcher(text);
		if( mCells.find() ) 
			name = mCells.group(1);
		
		mCells = sprice.matcher(text);
		if( mCells.find() ) 
			price = Double.parseDouble( mCells.group(1) );// ��ǰ����
		if( mCells.find() ) 
			bianh = Double.parseDouble( mCells.group(1) );// �ǵ�
		if( mCells.find() ) 
			bilv = Double.parseDouble( mCells.group(1) ); // �ǵ���

		mCells = svolume.matcher(text);
		// �ɽ������֣���
		if( mCells.find() ) 
			mCells.group(); 
		// �ɽ����Ԫ����
		if( mCells.find() )
			volume = Double.parseDouble( mCells.group() );
	}
}
