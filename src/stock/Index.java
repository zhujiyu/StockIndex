package stock;

import java.text.ParseException;

public class Index {

	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) {

//		String file = "d:\\candle.jpeg";
//		int width = 800, height = 600;
//		BufferedImage bmp = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//		Graphics g = bmp.getGraphics();
//		
//		g.setColor(Color.white);
//		g.fillRect(0, 0, width, height);
//		g.setColor(Color.green);
//		g.drawRect(200, 100, 200, 100);
//
//		g.dispose();
//		try {
//			ImageIO.write(bmp, "jpeg", new File(file));
//			System.out.println("save file succeed.");
//		}
//		catch( IOException ex ) {
//			ex.printStackTrace();
//		}

		String file = "candle.jpg";
		StockData stock = new StockData("601390", "ss");
		
		try {
			stock.load(DataSource.DATE_FORMAT.parse("2014-11-01"));
			if( stock.size() > 0 ) {
				CandleImage candle = new CandleImage(stock);
				candle.Save(file);
				stock.print();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println(file + " saved.");
		
	}

}
