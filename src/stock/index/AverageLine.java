package stock.index;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import stock.data.PriceBar;

public class AverageLine extends StockIndex {

	public AverageLine(int terms) {
		super(terms, Color.yellow);
	}

	public AverageLine(int terms, Color color) {
		super(terms, color);
	}

	@Override
	public void calcIndex(List<PriceBar> bars) {

		LinkedList<Float> prices = new LinkedList<Float>();
		for( int i = bars.size() - 1; i >= 0; --i ) {
			if( prices.size() >= this.terms )
				prices.poll();
			PriceBar bar = bars.get(i);
			float v = bar.get(datatype), ave = v;

			Iterator<Float> iter = prices.iterator();
			while( iter.hasNext() )
				ave += iter.next();
			ave /= (prices.size() + 1);
			
			indexes.put(bar.start, ave);
			prices.offer(v);
		}
	}

}
