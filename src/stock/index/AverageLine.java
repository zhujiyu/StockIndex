package stock.index;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

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

		Queue<Double> prices = new LinkedList<Double>();
		double sump = 0;
		ListIterator<PriceBar> iter = bars.listIterator(bars.size());
		
		while( iter.hasPrevious() ) {
			if( prices.size() >= this.terms ) 
				sump -= prices.poll();

			PriceBar bar = iter.previous();
			double v = bar.get(datatype);
			prices.offer(v);
			
			sump += v;
			this.add(sump / prices.size(), bar.start);			
		}
	}

//	PriceBar[] arr = bars.toArray(new PriceBar[1]);
//	for( int i = arr.length - 1; i >= 0; --i ) {
//		double v = arr[i].get(datatype);
//
//		if( prices.size() >= this.terms ) 
//			sump -= prices.poll();
//		prices.offer(v);
//		
//		sump += v;
//		this.add(sump / prices.size(), arr[i].start);
//	}
//	
//	for( int i = bars.size() - 1; i >= 0; --i ) {
//		PriceBar bar = bars.get(i);
//		double v = bar.get(datatype);
//		
//		if( prices.size() >= this.terms ) 
//			sump -= prices.poll();
//		prices.offer(v);
//		
//		sump += v;
//		this.add(sump / prices.size(), bar.start);
//
////		Iterator<Double> iter = prices.iterator();
////		while( iter.hasNext() )
////			ave += iter.next();
////		ave /= (prices.size() + 1);
////		prices.offer(v);
////		indexes.put(bar.start, ave);
//	}
	
}
