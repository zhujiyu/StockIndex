package stock;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class FileSource extends DataSource {

	public FileSource(String _code, String _market) {
		super(_code, _market);
	}

	@Override
	public List<List<String>> get(Calendar start, Calendar end)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
