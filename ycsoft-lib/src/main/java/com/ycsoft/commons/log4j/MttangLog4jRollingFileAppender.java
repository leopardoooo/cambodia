package com.ycsoft.commons.log4j;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

//继承log4j的RollingFileAppender类
public class MttangLog4jRollingFileAppender extends RollingFileAppender {

	private long nextRollover = 0;
	private static Map<String, BeginFileData> fileMaps = new HashMap<String, BeginFileData>();
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd");

	// synchronization not necessary since doAppend is alreasy synched
	public void rollOver() {

		File target;
		File file;
		int maxBackupIndexLeng = String.valueOf(maxBackupIndex).length();
		if (qw != null) {
			long size = ((CountingQuietWriter) qw).getCount();
			LogLog.debug("rolling over count=" + size);
			// if operation fails, do not roll again until
			// maxFileSize more bytes are written
			nextRollover = size + maxFileSize;
		}
		LogLog.debug("maxBackupIndex=" + maxBackupIndex);

		String nowDateString = sdf.format(new Date());
		String newFileName = (fileName.indexOf(".") != -1 ? fileName.substring(
				0, fileName.lastIndexOf(".")) : fileName);

		boolean renameSucceeded = true;
		// If maxBackups <= 0, then there is no file renaming to be done.
		if (maxBackupIndex > 0) {

			// Delete the oldest file, to keep Windows happy.
			file = new File(newFileName + '.' + nowDateString + '.'
					+ getIndex(maxBackupIndex, maxBackupIndexLeng));

			if (file.exists()) {
				renameSucceeded = file.delete();
			}
			// Map {(maxBackupIndex - 1), ..., 2, 1} to {maxBackupIndex, ..., 3,
			// 2}
			for (int i = maxBackupIndex - 1; (i >= 1 && renameSucceeded); i--) {
				file = new File(newFileName + '.' + nowDateString + '.'
						+ getIndex(i, maxBackupIndexLeng));
				if (file.exists()) {
					target = new File(newFileName + '.' + nowDateString + '.'
							+ getIndex(i + 1, maxBackupIndexLeng));
					LogLog.debug("Renaming file " + file + " to " + target);
					renameSucceeded = file.renameTo(target);
				}
			}

			if (renameSucceeded) {
				BeginFileData beginFileData = fileMaps.get(fileName);
				System.out.println("fileName= " + fileName
						+ "\t beginFileData=" + beginFileData);
				// 在每天一个日志目录的方式下，检测日期是否变更了，如果变更了就要把变更后的日志文件拷贝到变更后的日期目录下。
				if (newFileName.indexOf(nowDateString) == -1
						&& beginFileData.getFileName().indexOf("yyyy-MM-dd") != -1) {
					newFileName = beginFileData.getFileName().replace(
							"yyyy-MM-dd", nowDateString);
					newFileName = (newFileName.indexOf(".") != -1 ? newFileName
							.substring(0, newFileName.lastIndexOf("."))
							: newFileName);
				}
				target = new File(newFileName + '.' + nowDateString + '.'
						+ getIndex(1, maxBackupIndexLeng));
				this.closeFile();
				file = new File(fileName);
				LogLog.debug("Renaming file " + file + " to " + target);

				renameSucceeded = file.renameTo(target);
				//
				// if file rename failed, reopen file with append = true
				//
				if (!renameSucceeded) {
					try {
						this.setFile(fileName, true, bufferedIO, bufferSize);
					} catch (IOException e) {
						LogLog.error("setFile(" + fileName
								+ ", true) call failed.", e);
					}
				}
			}
		}
		//
		// if all renames were successful, then
		//
		if (renameSucceeded) {

			try {
				// This will also close the file. This is OK since multiple
				// close operations are safe.

				this.setFile(fileName, false, bufferedIO, bufferSize);
				nextRollover = 0;
			} catch (IOException e) {
				LogLog
						.error("setFile(" + fileName + ", false) call failed.",
								e);
			}
		}
	}

	/**
	 * 文件个数的长度补零，如果文件个数为10那么文件的个数长度就是2位，第一个文件就是01，02，03....
	 * 
	 * @param i
	 * @param maxBackupIndexLeng
	 * @return
	 */
	private String getIndex(int i, int maxBackupIndexLeng) {
		String index = String.valueOf(i);
		int len = index.length();
		for (int j = len; j < maxBackupIndexLeng; j++) {
			index = "0" + index;
		}
		return index + ".log";
	}

	/**
	 * This method differentiates RollingFileAppender from its super class.
	 * 
	 * @since 0.9.0
	 */
	protected void subAppend(LoggingEvent event) {
		super.subAppend(event);
		if (fileName != null && qw != null) {

			String nowDate = sdf.format(new Date());
			// 检测日期是否已经变更了，如果变更了就要重创建日期目录
			if (!fileMaps.get(fileName).getDate().equals(nowDate)) {
				rollOver();
				return;
			}

			long size = ((CountingQuietWriter) qw).getCount();
			if (size >= maxFileSize && size >= nextRollover) {
				rollOver();
			}
		}
	}

	@Override
	public synchronized void setFile(String fileName, boolean append,
			boolean bufferedIO, int bufferSize) throws IOException {

		String nowDate = sdf.format(new Date());
		// 如果文件路径包含了“yyyy-MM-dd”就是每天一个日志目录的方式记录日志(第一次的时候)
		if (fileName.indexOf("yyyy-MM-dd") != -1) {
			String beginFileName = fileName;
			fileName = fileName.replace("yyyy-MM-dd", nowDate);
			fileMaps.put(fileName, new BeginFileData(beginFileName, nowDate));
		}
		BeginFileData beginFileData = fileMaps.get(fileName);
		// 检测日期是否已经变更了，如果变更了就要把原始的字符串给fileName变量，把变更后的日期做为开始日期
		if (!beginFileData.getDate().equals(nowDate)) {
			// 获取出第一次的文件名
			beginFileData.setDate(nowDate);
			fileName = beginFileData.getFileName().replace("yyyy-MM-dd",
					nowDate);
			fileMaps.put(fileName, beginFileData);
		}

		// D:/data/test/yyyy-MM-dd/test.log 替换yyyy-MM-dd为当前日期。

		super.setFile(fileName, append, this.bufferedIO, this.bufferSize);
	}

	class BeginFileData {

		public BeginFileData(String fileName, String date) {
			super();
			this.fileName = fileName;
			this.date = date;
		}

		private String fileName;
		private String date;

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
	}
}