package stock.drawing;

import java.awt.Color;
import java.text.ParseException;

import stock.data.StockData;
import stock.index.EnclosureLine;
import stock.index.StockIndex;
import stock.index.AverageLine;
import stock.source.DataSource;

public class Demo {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) {

		String code = "601398";
//		String code = "601390";
//		String code = "600030";
		StockData stock = new StockData(code, "ss");
		
		try {
			stock.load(DataSource.DATE_FORMAT.parse("2014-07-01"), 
					DataSource.SOUREC_FILE);
			
			if( stock.size() > 0 ) {
				CandleImage candle = new CandleImage(stock);
				candle.tranCandle(30);
				candle.setBarWidth(2);
				
//				StockIndex index = new TurtleIndex(20, StockIndex.DIRECT_BUY);
//				candle.AddIndex(index);
//				index = new TurtleIndex(5, StockIndex.DIRECT_SELL, Color.red);
//				candle.AddIndex(index);
				
				StockIndex index = new AverageLine(48, Color.white);
				index.tranIndex(30);
				candle.AddIndex(index);
				
				index = new EnclosureLine(48, Color.red);
				((EnclosureLine)index).setShift(30);
//				index.tranIndex(30);
				candle.AddIndex(index);
				
//				index = new AverageLine(48, Color.blue);
//				index.tranIndex(12);
//				candle.AddIndex(index);

				index = new AverageLine(10, Color.white);
				index.setWindow(StockIndex.WINDOW_BOTTOM);
				index.setDataType(StockIndex.PRICE_VOLUME);
				candle.AddIndex(index);
				
				candle.Save("image/" + code + ".jpg");
//				stock.print();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println(code + ".jpg" + " saved.");
		
	}

}
