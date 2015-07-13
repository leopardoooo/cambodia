package com.yaochen.boss.commons;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * FTP 处理类 
 * 
 * @author Tom
 */
public class FtpUtil{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private FTPClient ftpClient;
	private String encoding = "GBK";
	private final int BUFFER_SIZE = 1024 * 10;
	private final int TIMEOUT = 60 * 1000; 
	
	// --------------------------------------------------------------------------------
	//  require to inject attributes
	// --------------------------------------------------------------------------------
	private String username;
	private String password;
	private String hostname;
	private int port;
	

	public FtpUtil(){}
	
	public FtpUtil(String hostname, int port, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.username = username;
		this.password = password;
		this.ftpClient = new FTPClient();
	}

	/**
	 * Login to the FTP Server
	 */
	public boolean login() {
		FTPClientConfig fcc = new FTPClientConfig();
		fcc.setServerTimeZoneId(TimeZone.getDefault().getID());
		ftpClient.setControlEncoding(encoding);
		ftpClient.configure(fcc);
		try {
			if (port > 0) {
				ftpClient.connect(hostname, port);
			} else {
				ftpClient.connect(hostname);
			}
			// check reply code
			int code = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(code)) {
				ftpClient.disconnect();
				logger.error("Login FTP Server is failure!");
				return false;
			}
			
			if(ftpClient.login(username, password)){
				// setting
				this.ftpClient.enterLocalPassiveMode();
				this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				this.ftpClient.setBufferSize(BUFFER_SIZE);
				this.ftpClient.setDataTimeout(TIMEOUT);
//				logger.info(username + " successfully logined to the FTP server.");
				return true;
			}else{
				throw new Exception("Please check your username and password.");
			}
		} catch (Exception e) {
			logger.error(username + " failed to login to the FTP server", e);
		}
		return false;
	}

	/**
	 * 上传文件至FTP服务器
	 * 
	 * @param file 需要上传的文件, 包括文件目录及名称的完整目录
	 * @param remotePath FTP服务器的路径
	 */
	public boolean uploadFile(String file, String remotePath) {
		return uploadFile(new File(file), remotePath);
	}
	
	/**
	 * 上传文件至FTP服务器
	 * 
	 * @param file 需要上传的文件, 包括文件目录及名称的完整目录
	 * @param remotePath FTP服务器的路径
	 */
	public boolean uploadFile(File srcFile, String remotePath) {
		BufferedInputStream bis = null;
		String filePath = srcFile.getPath();
		try {
			String fileName = srcFile.getName();
			ftpClient.changeWorkingDirectory(remotePath);
			bis = new BufferedInputStream(new FileInputStream(srcFile));
			
			// store file
			if (this.ftpClient.storeFile(fileName, bis)) {
				logger.info( fileName + " 上传成功");
				return true;
			}
		} catch (FileNotFoundException e) {
			logger.error(filePath + " 未找到", e);
		} catch (IOException e) {
			logger.error(filePath + " 上传出错", e);
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}
	
	/**
	 * 将文件夹上传至FTP服务器
	 * 
	 * @param localPath 本地需要上传的文件夹路径
	 * @param remotePath FTP服务器路径
	 */
	public boolean uploadDirectory(String localPath, String remotePath) {
		return uploadDirectory(new File(localPath), remotePath);
	}
	
	/**
	 * 将文件夹上传至FTP服务器
	 * 
	 * @param dir 本地需要上传的文件夹路径
	 * @param remotePath FTP服务器路径
	 */
	public boolean uploadDirectory(File dir, String remotePath) {
		try {
			ftpClient.makeDirectory(remotePath);
		} catch (IOException e) {
			logger.error(remotePath + "目录创建失败", e);
		}
		File[] files = dir.listFiles();
		
		// 上传文件
		for (File file : files) {
			if(!file.isDirectory()){
				uploadFile(file, remotePath);
			}
		}
		
		// 找出文件夹，递归上传
		for (File subDir : files) {
			if(subDir.isDirectory()){
				String remoteSubDir = remotePath + File.separator + subDir.getName();
				uploadDirectory(subDir, remoteSubDir);
			}
		}
		return true;
	}
	
	/**
	 * download single file
	 * 
	 * @param remotefileName 远程文件
	 * @param remotePath 远程目录
	 * @param localPath 本地目录
	 * @param delete 下载完成是否删除
	 */
	public boolean downloadFile(FTPFile remotefile, String remotePath, String localPath, boolean delete) {
		String localFileName = localPath + File.separator + remotefile.getName();
		BufferedOutputStream bos = null;
		try {
			File localFile = new File(localFileName);
			if(!localFile.exists()){
				localFile.createNewFile();
			}
			
			ftpClient.changeWorkingDirectory(remotePath);
			bos = new BufferedOutputStream(new FileOutputStream(localFile));
			
			if (ftpClient.retrieveFile(remotefile.getName(), bos)) {
				localFile.setLastModified(remotefile.getTimestamp().getTime().getTime());
				logger.info(remotefile.getName() + " successfully downloaded to the " + localFileName);
				if(delete && ftpClient.deleteFile(remotefile.getName())){
					logger.info(remotefile.getName() + " successfully removed.");
				}
				System.out.println(localFile.lastModified());
				return true;
			}
		} catch (Exception e) {
			logger.error(remotefile.getName() + " download failure", e);
		} finally {
			if (null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
				}
			}
		}
		return false;
	}
	
	/**
	 * download directory
	 * 
	 * @param localPath 本地路径
	 * @param remotePath 远程文件夹
	 * @param delete 下载完毕是否删除文件
	 */
	public boolean downLoadDirectory(String localPath, String remotePath, boolean delete) {
		try {
			File localFile = new File(localPath);
			localFile.mkdirs();
			
			FTPFile[] files = this.ftpClient.listFiles(remotePath);
			
			// download single file
			for (FTPFile file : files) {
				if(file.isFile()){
					downloadFile(file, remotePath, localPath, delete);
				}
			}
			
			// download directory
			for (FTPFile file : files) {
				String subDir = file.getName();
				if(file.isDirectory() && !".".equals(subDir) && !"..".equals(subDir)){
					downLoadDirectory(localPath + File.separator + subDir, 
							remotePath + File.separator + subDir, delete);
				}
			}
		} catch (IOException e) {
			logger.error("download directory failure", e);
			return false;
		}
		return true;
	}

	/**
	 * 从FTP服务器删除文件
	 * 
	 * @param remotefile
	 * @return 
	 * @throws Exception
	 */
	public boolean delete(String remoteFile)throws Exception{
		return ftpClient.deleteFile(remoteFile);
	}
	
	/**
	 * logout from the FTP Server and release connect
	 */
	public void logout() {
		if (null == ftpClient || !ftpClient.isConnected()) {
			return ;
		}
		try {
			// logout from the FTP Server
			if (ftpClient.logout()) {
//				logger.info("logout from the FTP Server.");
			}
		} catch (IOException e) {
			logger.error("failed to logout to the FTP server!", e);
		} finally {
			try {
				// release connect
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		FtpUtil ftp = new FtpUtil("ljq.vicp.net", 6000, "test", "yc");
		ftp.login();
		
		ftp.uploadFile("C:\\Users\\Tom\\Desktop\\银行扣费测试\\25D301.0113748295", "\\test\\boss");
		
		ftp.logout();
	}
}
