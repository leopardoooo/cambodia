
import java.util.Date;

import com.ycsoft.commons.helper.DateHelper;

public class L {
	public static void main(String[] args) throws Exception {

		Date date = DateHelper.parseDate("", "yyyy-MM-dd");
		System.out.println(date);
	}

}
