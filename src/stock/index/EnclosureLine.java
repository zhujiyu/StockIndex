package stock.index;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import stock.data.PriceBar;

public class EnclosureLine extends AverageLine {

	protected List< Double > diffs = new ArrayList<Double>();
	protected List< Double > upline = new ArrayList<Double>();
	protected List< Double > dnline = new ArrayList<Double>();
	
	protected double rate = 0.01;
	protected Color upcolor = Color.blue;
	protected Color dncolor = Color.blue;
	
	public EnclosureLine(int terms, int shift, double rate) {
		super(terms, Color.red);
		this.shift = shift;
		this.rate = rate;
	}
	
	public EnclosureLine(int terms, Color color) {
		super(terms, color);
	}

	public void setShift(int s) {
		this.shift = s;
	}

	@Override
	public void drawIndex(Graphics g, int first, int count, int step, int right, 
			double scale, double baseValue) { 
		super.drawIndex(g, first, count, step, right, scale, baseValue);
		
		if( first < 0 || count > upline.size() )
			return;
		g.setColor(this.upcolor);
		
		draw(upline, g, first, count, step, right, scale, baseValue);
		draw(dnline, g, first, count, step, right, scale, baseValue);
	}
	
	private void draw(List<Double> index, Graphics g, int first, int count, int step, int right, 
			double scale, double baseValue) {
		ListIterator<Double> iter = index.listIterator(index.size() - first);
		double prev = 0, curr = 0;
		
		if( iter.hasPrevious() && count -- > 0 ) {
			prev = iter.previous();
			right -= step;
		}
		
		while ( iter.hasPrevious() && count -- > 0 ) {
			curr = iter.previous();
			g.drawLine(right + step, (int)Math.round( prev * scale + baseValue), 
					right, (int)Math.round( curr * scale + baseValue));
			prev = curr;
			right -= step;
		}
	}

	@Override
	public void calcIndex(List<PriceBar> bars) {
		super.calcIndex(bars);

		double r1 = rate, r2 = 1 - r1;
		int start = 0;
		for( int i = start + shift; i > 0; i -- )
			diffs.add(0.0);
		
		ListIterator<PriceBar> iter = bars.listIterator(bars.size() - start - shift);
		PriceBar bar = iter.previous();
		
		double ma = datas.get(start++);
		double typic = (bar.high + bar.low + bar.close) / 3;
		double diff = Math.abs(typic - ma);
		
		diffs.add(diff);
		upline.add(ma + diff);
		dnline.add(ma - diff);
		
		while( iter.hasPrevious() ) {
			bar = iter.previous();
			typic = (bar.high + bar.low + bar.close) / 3;
			ma = datas.get(start);
			diff = Math.abs(typic - ma) * r1 + diff * r2;
			upline.add(ma + diff);
			dnline.add(ma - diff);
			start++;
		}
		
//		LinkedList<Double> prices = new LinkedList<Double>();
//		double[] averages = new double[bars.size()];
//		
//		for( int i = bars.size() - 1; i >= 0; --i ) {
//			if( prices.size() >= this.terms )
//				prices.poll();
//			PriceBar bar = bars.get(i);
//			double v = bar.get(datatype), ave = v;
//
//			Iterator<Double> iter = prices.iterator();
//			while( iter.hasNext() )
//				ave += iter.next();
//			ave /= (prices.size() + 1);
//			
//			averages[i] = ave;
//			prices.offer(v);
//		}

//		
//		int limit = bars.size() - 1 - terms - shift;
//		PriceBar _bar = bars.get(limit);
//		double ma = datas.get(terms);
//		double typic = (_bar.high + _bar.low + _bar.close) / 3;
//		double diff = Math.abs(typic - ma);
//		
////		double ma = averages[limit + shift];
////		double diff = Math.abs(_bar.get(datatype) - ma);
//		this.add(ma + diff, _bar.start);
////		indexes.put(_bar.start, ma + diff);
//
//		System.out.println(limit);
//		for( int i = limit - 1; i >= 0; --i ) {
//			ma = averages[i + shift];
//			PriceBar bar = bars.get(i);
//			diff = Math.abs(bar.get(datatype) - ma) * 0.125f + diff * 0.875f;
//			this.add(ma + diff, bar.start);
////			indexes.put(bar.start, ma + diff);
//		}
//		
	}

}
