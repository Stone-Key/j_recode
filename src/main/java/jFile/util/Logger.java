package jFile.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
	private static final String LOGFILE = "C:\\Users\\Administrator\\Desktop\\ssh2SpringBootLog";//日志文件
	public static boolean logNow = false;//是否需要立即写入文件。立即写入会增加文件的写入次数，减缓运行速度。但更安全
	
	
	private static File LOG_FILE;
	private static FileWriter fileWriter ;
	private static BufferedWriter bWriter;
	
	static {
		try {
			init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void init() throws IOException{
		//创建日志文件
		LOG_FILE = new File(LOGFILE+System.currentTimeMillis()+".txt");
		LOG_FILE.createNewFile();
		
		if ( !LOG_FILE.getParentFile().exists() || ! LOG_FILE.getParentFile().isDirectory() ) {
			LOG_FILE.getParentFile().mkdirs();
		}
		LOG_FILE.createNewFile();
		
		fileWriter = new FileWriter(LOG_FILE,true);
		bWriter = new BufferedWriter(fileWriter);
	}
	
	public static void appendLog(String log) throws IOException {
		System.out.println("log:"+ new Date() + ":" + log);
		if (logNow) {
			bWriter.write(log);
			bWriter.flush();
		}else {
			bWriter.write(log);
		}
	}
	
	public static void flushLog() throws IOException {
		if (!logNow) {
			System.out.println("log:"+ new Date() + ":刷新日志！" );
		}
		bWriter.flush();
	}
	
	public static void closeLogStream() throws IOException {
		bWriter.close();
		fileWriter.close();
	}
	
	private static Thread flushThread;
	public static void autoFlush() {
		if (flushThread == null || !flushThread.isAlive()) {
			flushThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!logNow) {
						try {
							Thread.sleep(2*60*1000);
							flushLog();
						} catch (Exception e) {
							throw new RuntimeException(e);
						} 
					}
				}
			});
			flushThread.start();
		}
	}
	
}
