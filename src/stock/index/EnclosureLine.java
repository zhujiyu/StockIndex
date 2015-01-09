package stock.index;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import stock.data.PriceBar;

//import stock.data.PriceBar;
//import stock.index.StockIndex;

public class EnclosureLine extends StockIndex {

	protected int shift = 30;
	
	public EnclosureLine(int terms, Color color) {
		super(terms, color);
	}

	public void setShift(int s) {
		this.shift = s;
	}
	
	@Override
	public void calcIndex(List<PriceBar> bars) {

		LinkedList<Double> prices = new LinkedList<Double>();
		double[] averages = new double[bars.size()];
		
		for( int i = bars.size() - 1; i >= 0; --i ) {
			if( prices.size() >= this.terms )
				prices.poll();
			PriceBar bar = bars.get(i);
			double v = bar.get(datatype), ave = v;

			Iterator<Double> iter = prices.iterator();
			while( iter.hasNext() )
				ave += iter.next();
			ave /= (prices.size() + 1);
			
			averages[i] = ave;
			prices.offer(v);
		}
		
		int limit = bars.size() - terms - shift - 1;
		PriceBar _bar = bars.get(limit);
		double ma = averages[limit + shift];
		double diff = Math.abs(_bar.get(datatype) - ma);
		this.add(ma + diff, _bar.start);
//		indexes.put(_bar.start, ma + diff);

		System.out.println(limit);
		for( int i = limit - 1; i >= 0; --i ) {
			ma = averages[i + shift];
			PriceBar bar = bars.get(i);
			diff = Math.abs(bar.get(datatype) - ma) * 0.125f + diff * 0.875f;
			this.add(ma + diff, bar.start);
//			indexes.put(bar.start, ma + diff);
		}
		
	}

}
