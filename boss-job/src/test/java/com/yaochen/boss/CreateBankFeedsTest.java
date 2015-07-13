package com.yaochen.boss;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class CreateBankFeedsTest {

	public static void main(String[] args) throws Exception{

		List<String> lines = new ArrayList<String>();
		lines.add("hello");
		lines.add("hello");
		lines.add("hello");
		lines.add("hello");
		lines.add("hell你好o");
		
		FileOutputStream fos = new FileOutputStream(new File("E:\\快捷图标\\软件\\aa.txt"), true);
		IOUtils.writeLines(lines, IOUtils.LINE_SEPARATOR, fos, "GBK");

	}

}
