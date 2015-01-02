package stock;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;

public class CandleImage {

	public static final int MIN_WIDTH = 16;
	public static final int MAX_WIDTH = 64;
	
	private int width = 2400;
	private int height = 1800;
	private StockData data;
	
	public CandleImage(StockData data) {
		this.data = data;
	}
	
	public void display(Graphics g, int length, float maxPrice, float minPrice) {
		
		int step = Math.min(MAX_WIDTH, width / length);
		int x = width - step, y, h;
		int bar_width = step / 2, base = Math.round(height * 0.8f);
		float hp = height * 0.6f / (maxPrice - minPrice), top, btm;
		Iterator<PriceBar> iter = data.getBarSet().iterator();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		
		for( int i = 0; i < length && iter.hasNext(); i ++ ) {
			PriceBar bar = iter.next();
			
			if( bar.get(PriceBar.CLOSE) < bar.get(PriceBar.OPEN) ) {
				g.setColor(Color.green);
				top = bar.get(PriceBar.OPEN);
				btm = bar.get(PriceBar.CLOSE);
			}
			else {
				g.setColor(Color.red);
				btm = bar.get(PriceBar.OPEN);
				top = bar.get(PriceBar.CLOSE);
			}
			
			y = Math.round( (top - minPrice) * hp );
			h = y - Math.round( (btm - minPrice) * hp );
//			g.drawRect(x, y, bar_width, h);
			g.drawRect(x, base - y, step, h);
			
			int x1 = x + bar_width / 2;
			int y1 = Math.round( (bar.get(PriceBar.HIGH) - minPrice) * hp );
			int y2 = Math.round( (bar.get(PriceBar.LOW) - minPrice) * hp );
//			g.drawLine(x1, y1, x1, y2);
			g.drawLine(x1, base - y1, x1, base - y2);
			
			x -= step;
		}
	}
	
	public void Save(String file) {
		BufferedImage bmp = new BufferedImage(width, height, 
				BufferedImage.TYPE_3BYTE_BGR);
		
		if( data.size() > 0 ) {
			Graphics g = bmp.getGraphics();
			
			this.display(g, Math.min(width / MIN_WIDTH, data.size()), 
					data.get(PriceBar.HIGH), data.get(PriceBar.LOW));
//			this.display(g, 10, 
//					data.get(PriceBar.HIGH), data.get(PriceBar.LOW));
			g.dispose();
		}
		
		try {
			ImageIO.write(bmp, "jpeg", new File(file));
		}
		catch( IOException ex ) {
			ex.printStackTrace();
		}
	}
	
//	public Image getImage() {
//		Image bmp = new BufferedImage(2400, 1800, BufferedImage.TYPE_4BYTE_ABGR, null);
//		Graphics g = bmp.getGraphics();
//		g.setColor(Color.white);
//		
//		return bmp;
//	}
//	
}
