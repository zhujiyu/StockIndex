package stock;

import java.util.Date;

public class PriceBar {

	public static final int OPEN = 1;
	public static final int CLOSE = 2;
	public static final int HIGH = 3;
	public static final int LOW = 4;
	public static final int VOLUME = 5;
	public static final int START = 6;
	public static final int END = 7;
	public static final int MINUTES = 8;
	
	
	protected float open;
	protected float close;
	protected float high;
	protected float low;

	protected float volume;
	
	protected Date  start;
	protected int minutes;

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
	
	public float get(int field) {
		switch(field) {
		case OPEN:
			return open;
		case CLOSE:
			return close;
		case HIGH:
			return high;
		case LOW:
			return low;
		case VOLUME:
			return volume;
//		case START:
//			return close;
//		case END:
//			return close;
		case MINUTES:
			return minutes;
		}
		return 0;
	}
	
	public void set(int field, float value) {
		switch(field) {
		case OPEN:
			open = value;
			break;
		case CLOSE:
			close = value;
			break;
		case HIGH:
			high = value;
			break;
		case LOW:
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
