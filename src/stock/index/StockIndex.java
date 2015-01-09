package stock.index;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import stock.data.PriceBar;
import stock.data.StockData;

public abstract class StockIndex {

	public static final int DIRECT_BUY = 0;
	public static final int DIRECT_SELL = 1;
	
	public static final int WINDOW_TOP = 2;
	public static final int WINDOW_BOTTOM = 3;
	public static final int WINDOW_MIDDLE = 4;

	public static final int PRICE_OPEN  = StockData.PRICE_OPEN;
	public static final int PRICE_CLOSE = StockData.PRICE_CLOSE;
	public static final int PRICE_HIGH  = StockData.PRICE_HIGH;
	public static final int PRICE_LOW   = StockData.PRICE_LOW;
	public static final int PRICE_MIDDLE = StockData.PRICE_MIDDLE;   ///< (HIGH + LOW) / 2
	public static final int PRICE_AVERAGE = StockData.PRICE_AVERAGE; ///< (HIGH + LOW + OPEN + CLOSE) / 4
	public static final int PRICE_VOLUME = StockData.PRICE_VOLUME;
	
//	private TreeMap<Date, Double> indexes = new TreeMap<Date, Double>();
//	protected Object[] datas = null;

	private ArrayList< Double > datas = new ArrayList<Double>();
	protected int terms = 10;
	protected int direct = DIRECT_BUY;
	
	protected int tran = 0;  ///< transform index to right
	protected Color color = Color.yellow;
	protected int window_index = WINDOW_TOP;
	protected int datatype = PRICE_AVERAGE;
	
	public StockIndex(int terms, Color color) {
		this.terms = terms;
		this.color = color;
	}
	
	abstract public void calcIndex(List<PriceBar> bars);
	
	public int getWindowIndex() {
		return window_index;
	}
	
	public void setDirect(int direct) {
		this.direct = direct;
	}
	
	public void tranIndex(int _tran) {
		this.tran = _tran;
	}
	
	public void setColor(Color c) {
		this.color = c;
	}
	
	public void setWindow(int windowIndex) {
		this.window_index = windowIndex;
	}
	
	public void setDataType(int type) {
		this.datatype = type;
	}
	
	public void add(double value, Date time) {
//		indexes.put(time, value);
		datas.add(value);
//		Entry r = new Entry<Date, Double>();
//		datas.add(new Entry<Date, Double>(time, value));
	}
	
//	public double get(Date date) {
//		if( indexes == null )
//			return 0;
//		if( indexes.containsKey(date) )
//			return indexes.get(date);
//		else
//			return 0;
//	}
	
	public double get(int idx) {
		if( datas != null ) {
			idx += tran;
			if( idx >= 0 && idx < datas.size() ) {
				return datas.get(datas.size() - idx - 1);
			}
		}
		return 0;
//		if( idx >= 0 && idx < datas.size() ) {
//			if( datas != null ) {
//				
//				return datas.get(idx);
//			}
//			
////			if( datas == null )
////				datas = indexes.values().toArray();
////			return (double)datas[idx];
////			return (double) indexes.values().toArray()[idx];
//		}
	}
	
	public void drawIndex(Graphics g, int first, int count, int step, int right, 
			double scale, double baseValue) { 
		
		if( first < 0 || count > datas.size() )
			return;
		g.setColor(this.color);
		
		count = Math.min(datas.size(), count + tran);
		right += this.tran * step; // 不管是否越界，向右移动tran个单位，这样下标就不用移动了
		first = datas.size() - first - 1;
		
		double prev = datas.get(first), curr = 0;
		for( right -= step, count --; count > 0;
				count --, first --, right -= step ) {
			curr = datas.get(first);

//			int py = (int) Math.round( (prev - baseValue) * scale );
//			int cy = (int) Math.round( (curr - baseValue) * scale );
//			g.drawLine(middle + step, height - py, middle, height - cy);

			int py = (int)Math.round( prev * scale + baseValue);
			int cy = (int)Math.round( curr * scale + baseValue);
			g.drawLine(right + step, py, right, cy);
//			g.drawLine(right + step, 
//					(int)Math.round( prev * scale - baseValue),
////					height - (int)Math.round( prev * scale - baseValue * scale ), 
//					right, 
//					(int)Math.round( prev * scale - baseValue));
////					height - (int)Math.round( curr * scale - baseValue * scale ));
			
			prev = curr;
		}
	}

//	public void drawIndex(Graphics g, PriceBar[] bars, int step,
//			Rectangle rect, double deltaValue, double baseValue, double zoom) {
//
//		if( bars.length == 0 )
//			return;
//		g.setColor(this.color);
//		
//		int middle = rect.width - step / 2 + this.tran * step, 
//				height = rect.y + rect.height;
//		double hp = rect.height * zoom / deltaValue;
//		PriceBar prev = null, curr = null;
//
//		prev = bars[0];
//		middle -= step;
//
//		for( int i = 1; i < bars.length; i ++ ) {
//			curr = bars[i];
//
//			double p = this.get(prev.start), c = this.get(curr.start);
//			int py = (int) Math.round( (p - baseValue) * hp );
//			int cy = (int) Math.round( (c - baseValue) * hp );
//			
//			g.drawLine(middle + step, height - py, middle, height - cy);
//			
//			prev = curr;
//			middle -= step;
//		}
//	}
}
