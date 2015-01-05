package stock;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import stock.data.PriceBar;
import stock.data.StockData;
import stock.index.StockIndex;

public class CandleImage {

//	public static final int MIN_WIDTH = 16;
//	public static final int MAX_WIDTH = 64;
//	public static final int DEFAULT_WIDTH = 12;
	
	private int img_width = 800;
	private int img_height = 600;
//	private StockData data;
	private List<PriceBar> bar_list;
	private List<StockIndex> indexes = new ArrayList<StockIndex>();
	
	private int step = 8;
	private int trans = 10;
	private int moving = 0;
	private float zoom = 0.6f;
	
	public CandleImage(StockData data) {
		this.bar_list = data.getBarSet();
	}
	
	public void AddIndex(StockIndex index) {
		index.calcIndex(bar_list);
		indexes.add(index);
	}

	public void setBarWidth(int width) {
		this.step = width;
	}
	
	public void Save(String file) {
		BufferedImage bmp = new BufferedImage(img_width, img_height, 
				BufferedImage.TYPE_3BYTE_BGR);
		
		if( bar_list.size() > 0 ) {
			Graphics g = bmp.getGraphics();
			this.display(g, new Rectangle(1, 1, img_width - 2, 
					img_height - 2));
			g.dispose();
		}
		
		try {
			ImageIO.write(bmp, "jpeg", new File(file));
		}
		catch( IOException ex ) {
			ex.printStackTrace();
		}
	}
	
	public void display(Graphics g, Rectangle rect) {
		
		int divid = Math.round(rect.height * 0.8f);
		float b = divid * 0.5f, s = b * zoom;
		Rectangle topWindow = new Rectangle(rect.x, rect.y + Math.round(b - s),
				rect.width, Math.round(2 * s)); 
//				rect.width, rect.y + Math.round(b + s));
		Rectangle btmWindow = new Rectangle(rect.x, divid + 10, rect.width, 
				rect.height - divid - 10);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, rect.width, rect.height);

		g.setColor(Color.red);
		g.drawRect(rect.x, rect.y, rect.width, divid);
		g.drawRect(btmWindow.x, btmWindow.y, btmWindow.width, btmWindow.height);

//		float temp = rect.height * y_zoom;
//		rect.y = Math.round( (rect.height - temp) * 0.5f );
//		rect.height = Math.round(temp);

		topWindow.width = btmWindow.width = Math.min(rect.width, 
				rect.width - trans * step + moving * step);
		int count = Math.min(topWindow.width / step + 1, bar_list.size());
		float high = 0, low = 0, maxVolume = 0;
		
		Iterator<PriceBar> iter = bar_list.listIterator(Math.max(0, 
				moving - trans));
		PriceBar[] bars = new PriceBar[count];

		if( iter.hasNext() ) {
			bars[0] = iter.next();
			high = bars[0].high;
			low = bars[0].low;
			maxVolume = bars[0].volume;
		}
		
		for( int i = 1; i < count && iter.hasNext(); i ++ ) {
			bars[i] = iter.next();
			high = Math.max(bars[i].get(PriceBar.PRICE_HIGH), high);
			low = Math.min(bars[i].get(PriceBar.PRICE_LOW), low);
			maxVolume = Math.max(bars[i].volume, maxVolume);
		}

		drawVolume(g, bars, btmWindow, maxVolume);
		drawCandle(g, bars, topWindow, high, low);
		Iterator<StockIndex> _it = indexes.iterator();
		while( _it.hasNext() ) {
			StockIndex index = _it.next();
			if( index.window_index == StockIndex.WINDOW_TOP )
				drawIndex(g, index, bars, topWindow, high - low, low, 1);
			else if( index.window_index == StockIndex.WINDOW_BOTTOM )
				drawIndex(g, index, bars, btmWindow, maxVolume, 0, 0.8f);
		}
	}
	
	private void drawCandle(Graphics g, PriceBar[] bars, 
			Rectangle rect, float high, float low) {

		int delta = step / 8, bar_width = step * 3 / 4;
		int x = rect.width - step + delta, height = rect.y + rect.height,
				middle = rect.width - step / 2;
		float bhp = rect.height / (high - low);
		
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(2));
		
		for( int i = 0; i < bars.length; i ++ ) {
			PriceBar curr = bars[i];
			
			if( curr.get(PriceBar.PRICE_CLOSE) < curr.get(PriceBar.PRICE_OPEN) ) {
//				top = curr.get(PriceBar.OPEN);
//				btm = curr.get(PriceBar.CLOSE);

				int y = Math.round( (curr.open - low) * bhp );
				int h = Math.round( (curr.close - low) * bhp );

				g.setColor(Color.green);
				g.fillRect(x, height - y, bar_width, y - h);
			}
			else {
//				btm = curr.get(PriceBar.OPEN);
//				top = curr.get(PriceBar.CLOSE);

				int y = Math.round( (curr.close - low) * bhp );
				int h = Math.round( (curr.open - low) * bhp );
				
				g.setColor(Color.red);
				g.drawRect(x, height - y, bar_width, y - h);
			}
			
			int y1 = Math.round( (curr.get(PriceBar.PRICE_HIGH) - low) * bhp );
			int y2 = Math.round( (curr.get(PriceBar.PRICE_LOW) - low) * bhp );
			g.drawLine(middle, height - y1, middle, height - y2);
			
			x -= step;
			middle -= step;
		}
	}
	
	private void drawVolume(Graphics g, PriceBar[] bars, Rectangle rect, float maxv) {

		int delta = step / 8, bar_width = step * 3 / 4;
		int x = rect.width - step + delta, bottom = rect.y + rect.height;
		float vhp = rect.height / maxv * 0.8f;
		
		((Graphics2D)g).setStroke(new BasicStroke(1));
		
		for( int i = 0; i < bars.length; i ++ ) {
			PriceBar curr = bars[i];
			
			if( curr.close > curr.open )
				g.setColor(Color.red);
			else
				g.setColor(Color.green);

			int vh =  Math.round( curr.volume * vhp );
			g.fillRect(x, bottom - vh, bar_width, vh);

			x -= step;
		}
	}
	
	private void drawIndex(Graphics g, StockIndex index, PriceBar[] bars, 
			Rectangle rect, float deltaValue, float baseValue, float zoom) {

		if( bars.length == 0 )
			return;
		g.setColor(index.color);
		
		int middle = rect.width - step / 2 + index.move * step, 
				height = rect.y + rect.height;
		float hp = rect.height * zoom / deltaValue;
		PriceBar prev = null, curr = null;

		prev = bars[0];
		middle -= step;

		for( int i = 1; i < bars.length; i ++ ) {
			curr = bars[i];

			float p = index.get(prev.start), c = index.get(curr.start);
			int py = Math.round( (p - baseValue) * hp );
			int cy = Math.round( (c - baseValue) * hp );
			
			g.drawLine(middle + step, height - py, middle, height - cy);
			
			prev = curr;
			middle -= step;
		}
	}
	
//	private void _display(Graphics g, Rectangle rect, PriceBar[] bars, int step, 
//			float high, float low) {
//
//		int x = rect.width - step, x1 = x + step / 2, y, h;
//		int delta = step / 8, width = step * 3 / 4, 
//				base = Math.round(rect.height * 1.0f) + rect.y;
//		float hp = rect.height * 1.0f / (high - low), top, btm;
//		PriceBar prev = null, curr = null;
//		
//		Graphics2D g2d = (Graphics2D)g;
//		g2d.setStroke(new BasicStroke(2));
//		
//		for( int i = 0; i < bars.length; i ++ ) {
//			curr = bars[i];
//			
//			if( curr.get(PriceBar.CLOSE) < curr.get(PriceBar.OPEN) ) {
//				g.setColor(Color.green);
//				top = curr.get(PriceBar.OPEN);
//				btm = curr.get(PriceBar.CLOSE);
//
//				y = Math.round( (top - low) * hp );
//				h = y - Math.round( (btm - low) * hp );
//				g.fillRect(x + delta, base - y, width, h);
//			}
//			else {
//				g.setColor(Color.red);
//				btm = curr.get(PriceBar.OPEN);
//				top = curr.get(PriceBar.CLOSE);
//
//				y = Math.round( (top - low) * hp );
//				h = y - Math.round( (btm - low) * hp );
//				g.drawRect(x + delta, base - y, width, h);
//			}
//			
//			int y1 = Math.round( (curr.get(PriceBar.HIGH) - low) * hp );
//			int y2 = Math.round( (curr.get(PriceBar.LOW) - low) * hp );
//			g.drawLine(x1, base - y1, x1, base - y2);
//			
//			if( prev != null ) {
//				Iterator<StockIndex> _it = indexes.iterator();
//				while( _it.hasNext() ) {
//					StockIndex index = _it.next();
//					
//					float p = index.get(prev.start), c = index.get(curr.start);
//					int py = Math.round( (p - low) * hp );
//					int cy = Math.round( (c - low) * hp );
//					
//					g.setColor(index.color);
//					int x2 = x1 + index.move * step;
//					g.drawLine(x2 + step, base - py, x2, base - cy);
////					if( x2 < rect.width )
////						g.drawLine(x2 + step, base - py, x2, base - cy);
//				}
//			}
//			
//			prev = curr;
//			x -= step;
//			x1 -= step;
//		}
//	}
	
//	private void _display(Graphics g, int step, Rectangle rect) {
//		//, float maxPrice, float minPrice
////		int count = Math.min(candle_width / DEFAULT_WIDTH, data.size());
////		int step = Math.min(MAX_WIDTH, candle_width / count);
////		Rectangle rect = new Rectangle(0, 0, candle_width, candle_height);
//		
//		g.setColor(Color.BLACK);
//		g.fillRect(0, 0, rect.width, rect.height);
//		
//		int count = rect.width / step;
//		float minp = 0, maxp = 0;
//		Iterator<PriceBar> iter = bar_list.iterator();
//		
//		if( iter.hasNext() ) {
//			PriceBar bar = iter.next();
//			maxp = bar.get(PriceBar.HIGH);
//			minp = bar.get(PriceBar.LOW);
//		}
//		
//		for( int i = 0; i < count && iter.hasNext(); i ++ ) {
//			PriceBar bar = iter.next();
//			maxp = Math.max(bar.get(PriceBar.HIGH), maxp);
//			minp = Math.min(bar.get(PriceBar.LOW), minp);
//		}
//
//		int delta = step / 8, width = step * 3 / 4, 
//				base = Math.round(rect.height * 1.0f);
//		float hp = rect.height * 1.0f / (maxp - minp), top, btm;
//		int x = rect.width - step, x1 = x + step / 2, y, h;
//		iter = bar_list.iterator();
//		PriceBar prev = null, curr = null;
//		
//		for( int i = 0; i < count && iter.hasNext(); i ++ ) {
//			curr = iter.next();
//			
//			if( curr.get(PriceBar.CLOSE) < curr.get(PriceBar.OPEN) ) {
//				g.setColor(Color.green);
//				top = curr.get(PriceBar.OPEN);
//				btm = curr.get(PriceBar.CLOSE);
//
//				y = Math.round( (top - minp) * hp );
//				h = y - Math.round( (btm - minp) * hp );
//				g.fillRect(x + delta, base - y, width, h);
//			}
//			else {
//				g.setColor(Color.red);
//				btm = curr.get(PriceBar.OPEN);
//				top = curr.get(PriceBar.CLOSE);
//
//				y = Math.round( (top - minp) * hp );
//				h = y - Math.round( (btm - minp) * hp );
//				g.drawRect(x + delta, base - y, width, h);
//			}
//			
//			int y1 = Math.round( (curr.get(PriceBar.HIGH) - minp) * hp );
//			int y2 = Math.round( (curr.get(PriceBar.LOW) - minp) * hp );
//			g.drawLine(x1, base - y1, x1, base - y2);
//			
//			if( prev != null ) {
//				Iterator<StockIndex> _it = indexes.iterator();
//				while( _it.hasNext() ) {
//					StockIndex index = _it.next();
//					
//					float p = index.get(prev.start), c = index.get(curr.start);
//					int py = Math.round( (p - minp) * hp );
//					int cy = Math.round( (c - minp) * hp );
//					
//					g.setColor(index.color);
//					g.drawLine(x1 + step, base - py, x1, base - cy);
//				}
//			}
//			
//			prev = curr;
//			x -= step;
//			x1 -= step;
//		}
//	}
	
}
