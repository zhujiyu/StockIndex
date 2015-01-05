package stock.index;

import java.awt.Color;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import stock.data.PriceBar;

public class TurtleIndex extends StockIndex {

	public TurtleIndex(int terms, int direct) {
		super(terms, Color.yellow);
		this.direct = direct;
		
		if( direct == StockIndex.DIRECT_BUY )
			this.datatype = StockIndex.PRICE_HIGH;
		else
			this.datatype = StockIndex.PRICE_LOW;
	}

	public TurtleIndex(int terms, int direct, Color color) {
		super(terms, color);
		this.direct = direct;
		
		if( direct == StockIndex.DIRECT_BUY )
			this.datatype = StockIndex.PRICE_HIGH;
		else
			this.datatype = StockIndex.PRICE_LOW;
	}

	@Override
	public void calcIndex(List<PriceBar> bars) {
		
//		PriceBar[] bars = (PriceBar[]) bl.toArray();
		LinkedList<Float> prices = new LinkedList<Float>();
		
		for( int i = bars.size() - 1; i >= 0; --i ) {
			if( prices.size() >= this.terms )
				prices.poll();
			PriceBar bar = bars.get(i);
			
			float v = bar.get(this.datatype), iv = v;
			Iterator<Float> iter = prices.iterator();
			
			if( this.direct == DIRECT_BUY ) {
//				v = bar.high;
//				iv = v;
//				Iterator<Float> iter = prices.iterator();
				while( iter.hasNext() ) 
					iv = Math.max(iv, iter.next());
			}
			else {
//				v = bar.get(this.datatype);
//				iv = v;
//				Iterator<Float> iter = prices.iterator();
				while( iter.hasNext() ) 
					iv = Math.min(iv, iter.next());
			}
			
			indexes.put(bar.start, iv);
			prices.offer(v);
		}
		
	}

}
