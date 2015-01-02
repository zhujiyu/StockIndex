package stock;

import java.io.IOException;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class StockData extends PriceBar {

	public static final int CODE = 1;
	public static final int MARKET = 2;
	
	private String code = "601857";
	private String market = "ss";
	private DataSource source;
	
	private List<PriceBar> bar_list = new ArrayList<PriceBar>();
	
	public StockData(String code, String market) {
		this.code = code;
		this.market = market;
	}
	
	public void load(Date start, Date end) {
		Calendar start_time = new GregorianCalendar();
		start_time.setTime(start);
		Calendar end_date = new GregorianCalendar();
		end_date.setTime(end);
		source = new WebSource(this.code, this.market);
		
		try {
			List<List<String>> data = source.get(start_time, end_date);
			Parse(data);
		} catch (IOException e) {
			String url = ((WebSource)source).getDataUrl(start_time, end_date);
			System.out.println("download data from " + url + " failed.");
			e.printStackTrace();
		}
	}
	
	public void load(Date start) {
		load(start, new Date());
	}
	
	public void print() {
		source.print();
//		Iterator< Entry<Date, PriceBar> > iter = datamap.entrySet().iterator();
//		while( iter.hasNext() ) {
//			Entry<Date, PriceBar> entry = iter.next();
//			System.out.println(entry.getValue());
//		}
	}
	
	public String getInfo(int field) {
		switch(field) {
		case CODE:
			return this.code;
		case MARKET:
			return this.market;
		}
		return null;
	}
	
	public List<PriceBar> getBarSet() {
		return bar_list;
	}
	
	public int size() {
		return this.bar_list.size();
	}
	
	public void Parse(List<List<String>> _data) {
		int[] fields = null;
		PriceBar bar;
		Iterator< List<String> > rowIter = _data.iterator();
		
		if( rowIter.hasNext() ) {
			List<String> rec = rowIter.next();
			fields = new int[rec.size()];
			Iterator<String> cellIter = rec.iterator();
			
			for(int i = 0; cellIter.hasNext(); i ++ ) {
				String cell = cellIter.next().toUpperCase();
				
				switch(cell) {
				case "OPEN" :
					fields[i] = PriceBar.OPEN;
					break;
				case "CLOSE" :
					fields[i] = PriceBar.CLOSE;
					break;
				case "HIGH" :
					fields[i] = PriceBar.HIGH;
					break;
				case "LOW" :
					fields[i] = PriceBar.LOW;
					break;
				case "VOLUME" :
					fields[i] = PriceBar.VOLUME;
					break;
				case "DATE" :
					fields[i] = PriceBar.START;
					break;
				default:
					break;
				}
			}
		} // end of if
		
		while( rowIter.hasNext() ) {
			List<String> rec = rowIter.next();
			Iterator<String> cellIter = rec.iterator();
			bar = new PriceBar(4*60);
			
			for(int i = 0; cellIter.hasNext(); i ++ ) {
				String cell = cellIter.next();
				try {
					if( fields[i] != PriceBar.START ) {
						float price = Float.parseFloat(cell);
						bar.set(fields[i], price);
					}
					else {
						bar.start = DataSource.DATE_FORMAT.parse(cell);
					}
				}
				catch( ParseException ex ) {
					ex.printStackTrace();
				}
				catch( Exception ex ) {
					ex.printStackTrace();
				}
			}

			bar_list.add(bar);
		} // end of while
		
		Iterator< PriceBar > iter = bar_list.iterator();
		bar = null;
		
		if( iter.hasNext() ) {
			bar = iter.next();
			this.close = bar.close;
			this.high = bar.high;
			this.low = bar.low;
			this.volume = bar.volume;
		}
		
		while( iter.hasNext() ) {
			bar = iter.next();
			this.high = this.high > bar.high ? this.high : bar.high;
			this.low = this.low < bar.low ? this.low : bar.low;
			this.volume += bar.volume;
		}
		
		if( bar != null ) {
			this._open = bar._open;
			this.start = bar.start;
			this.minutes = bar.minutes * bar_list.size();
		}
	}// end of parse
	
}
