package stock.index;

import java.awt.Color;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
	public static final int PRICE_MIDDLE = StockData.PRICE_MIDDLE;  ///< (HIGH + LOW) / 2
	public static final int PRICE_AVERAGE = StockData.PRICE_AVERAGE; ///< (HIGH + LOW + OPEN + CLOSE) / 4
	public static final int PRICE_VOLUME = StockData.PRICE_VOLUME;
	
	protected HashMap<Date, Float> indexes = new HashMap<Date, Float>();
	public int terms = 10;
	protected int direct = DIRECT_BUY;
	
	public int move = 0;
	public Color color = Color.yellow;
	public int window_index = WINDOW_TOP;
	public int datatype = PRICE_AVERAGE;
	
	public StockIndex(int terms, Color color) {
		this.terms = terms;
		this.color = color;
	}
	
	public void setDirect(int direct) {
		this.direct = direct;
	}
	
	public void setMove(int move) {
		this.move = move;
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
	
	public float get(Date date) {
		if( indexes == null )
			return 0;
		return indexes.get(date);
	}
	
	abstract public void calcIndex(List<PriceBar> bars);
}
