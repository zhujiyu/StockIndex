package stock.data;

import java.util.Date;

import stock.source.DataSource;

public class PriceBar {

	public static final int PRICE_OPEN = 0xf0;
	public static final int PRICE_CLOSE = 0xf1;
	public static final int PRICE_HIGH = 0xf2;
	public static final int PRICE_LOW = 0xf3;
	public static final int VOLUME = 0xf4;

	public static final int PRICE_MIDDLE = 0xf5;  ///< (HIGH + LOW) / 2
	public static final int PRICE_AVERAGE = 0xf6; ///< (HIGH + LOW + OPEN + CLOSE) / 4
	public static final int PRICE_VOLUME = VOLUME;
	
	public static final int START = 0xf7;
	public static final int END = 0xf8;
	public static final int MINUTES = 0xf9;

	
	public double open;
	public double close;
	public double high;
	public double low;
	public double volume;
	
	public Date  start;
	public int minutes;

	public PriceBar() {
	}
	
	public PriceBar(int minutes) {
		this.minutes = minutes;
	}
	
	public PriceBar(Date start, int minutes) {
		this.start = start;
		this.minutes = minutes;
	}
	
	public String toString() {
		return DataSource.DATE_FORMAT.format(this.start) + "," + 
				this.open + "," + this.high + "," + this.low + "," + this.close;
	}
	
	public double get(int field) {
		switch(field) {
		case PRICE_OPEN:
			return open;
		case PRICE_CLOSE:
			return close;
		case PRICE_HIGH:
			return high;
		case PRICE_LOW:
			return low;
		case VOLUME:
			return volume;
		case PRICE_MIDDLE:
			return (high + low) / 2;
		case PRICE_AVERAGE:
			return (open + close + high + low) / 4;
		case MINUTES:
			return minutes;
		default:
			break;
		}
		return 0;
	}
	
	public void set(int field, double value) {
		switch(field) {
		case PRICE_OPEN:
			open = value;
			break;
		case PRICE_CLOSE:
			close = value;
			break;
		case PRICE_HIGH:
			high = value;
			break;
		case PRICE_LOW:
			low = value;
			break;
		case VOLUME:
			volume = value;
			break;
		default:
			break;
		}
	}
	
	public Date getDate() {
		return this.start;
	}
	
	public void setDate(Date date) {
		this.start = date;
	}
}
