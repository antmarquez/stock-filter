
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class AlphaVantage {
	public static final List<String> symbols = Arrays.asList("AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE",
			"AMZN", "CRAY", "CSCO", "SNE", "GOOG", "INTC", "INTU", "MSFT", "ORCL", "TIBX", "VRSN", "YHOO");

	final static String API_KEY = "LQ4GET4Z207CIA71";

	@SuppressWarnings("deprecation")
	private static Double getPriceLive(String symbol) throws IOException {

		String rootURL = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE";
		rootURL += "&symbol=" + URLEncoder.encode(symbol, "UTF-8");
		rootURL += "&apikey=" + API_KEY;
		
		URL request = new URL(rootURL);
		String response = IOUtils.toString(request.openStream());

		JSONObject root = new JSONObject(response);

		JSONObject globalQuote;
		try {
			globalQuote = (JSONObject) root.get("Global Quote");
		} catch (JSONException e) {
			System.out.println("Wrong symbol entered: " + symbol);
			return 0.0;
		}

		String priceAsString = (String) globalQuote.get("05. price");
		double priceAsDouble = Double.parseDouble(priceAsString);

		return priceAsDouble;
	}

	
	public static void main(String[] args) throws IOException {
		
		// 1. Print these symbols using a Java 8 for-each and lambdas
		symbols.stream().forEach(System.out::println);

		// 2. Use the StockUtil class to print the price of Bitcoin
		System.out.println("Price of Bitcoin: " + StockUtil.getPrice("BTC-USD"));
		System.out.println("The live price of Bitcoin is: " + getPriceLive("BTC-USD"));

		// 3. Create a new List of StockInfo that includes the stock price
		List<StockInfo> listStockInfo = StockUtil.prices.entrySet()
				.stream()
				.map(stock -> new StockInfo(stock.getKey(), stock.getValue()))
				.collect(Collectors.toList());
		listStockInfo.forEach(System.out::println);
		Map<String, Double> randomFourPrices = new HashMap<String, Double>();
		Object[] keys = StockUtil.prices.keySet().toArray();
		for (int i = 0; i < 5; i++) {
			Object key = keys[new Random().nextInt(keys.length)];
			randomFourPrices.put((String) key, StockUtil.prices.get(key));
		}

		List<StockInfo> listStockLive = randomFourPrices.entrySet()
				.stream()
				.map(stock -> {
					try {
						return new StockInfo(stock.getKey(), getPriceLive(stock.getKey()));
					} catch (IOException e) {
						System.out.println("Error!");
						return null;
						}
					})
				.collect(Collectors.toList());
		listStockLive.forEach(System.out::println);

		// 4. Find the highest-priced stock under $500
		StockInfo highestPricedStock = listStockInfo
				.stream()
				.filter(StockUtil.isPriceLessThan(500))
				.reduce(StockUtil::pickHigh)
				.get();
		System.out.println("The highest-priced stock under $500 is: " + highestPricedStock);

	}

}

// Copyright Anthony Marquez, 2020