package stock;

import java.awt.Color;
import java.text.ParseException;

import stock.data.StockData;
import stock.index.StockIndex;
import stock.index.AverageLine;
import stock.index.TurtleIndex;
import stock.source.DataSource;

public class Demo {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) {

		String code = "601390";
		StockData stock = new StockData(code, "ss");
		
		try {
			stock.load(DataSource.DATE_FORMAT.parse("2014-07-01"));
			
			if( stock.size() > 0 ) {
				CandleImage candle = new CandleImage(stock);
				candle.setBarWidth(8);
				StockIndex index = new TurtleIndex(20, StockIndex.DIRECT_BUY);
				candle.AddIndex(index);
				index = new TurtleIndex(5, StockIndex.DIRECT_SELL, Color.red);
				candle.AddIndex(index);
				
				index = new AverageLine(5, Color.white);
//				index.setMove(-5);
				candle.AddIndex(index);
				
				index = new AverageLine(48, Color.blue);
//				index.setMove(5);
				candle.AddIndex(index);

				index = new AverageLine(10, Color.white);
				index.setWindow(StockIndex.WINDOW_BOTTOM);
				index.setDataType(StockIndex.PRICE_VOLUME);
				candle.AddIndex(index);
				
				candle.Save(code + ".jpg");
//				stock.print();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println(code + ".jpg" + " saved.");
		
	}

}
