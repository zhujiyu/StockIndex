package stock.source;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import stock.data.PriceBar;

public class SinaStock extends SinaAPI {
	
	public SinaStock(String code) {
		this.code = code;
	}
	
	public PriceBar getNewPrice() {
		PriceBar bar = new PriceBar();

		try {
			String text = download(STOCK_URL + code);
			this.Parse(text, bar);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bar;
	}

	// var hq_str_sh601006="大秦铁路,10.95,10.94,10.87,10.97,10.73,10.86,10.87,120600235,1309055023,55317,10.86,935799,10.85,264244,10.84,175700,10.83,250569,10.82,47469,10.87,88316,10.88,338500,10.89,633740,10.90,243200,10.91,2015-03-27,15:03:04,00";
	public void Parse(String text, PriceBar bar) {
		Pattern sname  = Pattern.compile("[\\w\\s]+=\"([^,]*),");
		Pattern sprice = Pattern.compile(",(\\d+.\\d+)");
		Pattern volume = Pattern.compile(",(\\d+)");
		
		Matcher mCells = sname.matcher(text);
		if( mCells.find() ) 
			name = mCells.group(1);
		
		mCells = sprice.matcher(text);
		if( mCells.find() ) 
			bar.open = Double.parseDouble( mCells.group(1) );
		if( mCells.find() ) 
			mCells.group(); // 昨收
		
		if( mCells.find() ) 
			bar.close = Double.parseDouble( mCells.group(1) );
		if( mCells.find() ) 
			bar.high = Double.parseDouble( mCells.group(1) );
		if( mCells.find() ) 
			bar.low = Double.parseDouble( mCells.group(1) );

		if( mCells.find() ) 
			mCells.group(); // 竞买价，即“买一”报价
		if( mCells.find() ) 
			mCells.group(); // 竞卖价，即“卖一”报价

		mCells = volume.matcher(text);
		// 成交的股票数，由于股票交易以一百股为基本单位，所以在使用时，通常把该值除以一百
		if( mCells.find() ) 
			mCells.group(); 
		// 成交金额，单位为“元”，为了一目了然，通常以“万元”为成交金额的单位，所以通常把该值除以一万；
		if( mCells.find() )
			bar.volume = Double.parseDouble( mCells.group() ) / 10000;
	}
}
