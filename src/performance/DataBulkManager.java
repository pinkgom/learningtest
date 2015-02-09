package performance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.chrono.GregorianChronology;

public class DataBulkManager {	
	
	private String seedFile = "D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_SEED_20150131.csv";
	//private String seedFile = "D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_DAY_DATA_STORE_SEED_20150131.csv";
	private String bulkFile = "D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_BULK_YEAR_951_1095.csv";
	//private String bulkFile = "D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_DAY_DATA_STORE_BULK_YEAR_1_730.csv";
	
	public static void main(String[] args) {
		DataBulkManager bulkManager = new DataBulkManager();

		long startTimeMili =0, endTimeMili = 0;
		/*		
		startTimeMili = System.currentTimeMillis(); // 시작시간 
		
		bulkManager.makeBulkDataUsingCommonIO();

		endTimeMili = System.currentTimeMillis();  //종료시간
		System.out.println("\n" + (endTimeMili-startTimeMili)/1000 + " seconds");
		*/
		
		startTimeMili = System.currentTimeMillis(); // 시작시간
		bulkManager.makeBulkDataUsingNIO(951, 1095);		
		endTimeMili = System.currentTimeMillis();  //종료시간
		System.out.println("\n" + (endTimeMili-startTimeMili)/1000 + " seconds");
		
		/*
		startTimeMili = System.currentTimeMillis(); // 시작시간
		//bulkManager.makeBulkDataUsingMap(1);		
		endTimeMili = System.currentTimeMillis();  //종료시간
		System.out.println("\n" + (endTimeMili-startTimeMili)/1000 + " seconds");
		*/
	}
	
	public void makeBulkDataUsingCommonIO() {
		File readFile = new File(seedFile);
		File monthFile = new File(bulkFile);
		
		try {
		    List<String> lines = FileUtils.readLines(readFile);
		    
		    for ( int dInx=0; dInx<=1; dInx++ ) {
		    	Iterator<String> it = lines.iterator();
		    	
		    	long rowNumber = 0;
		    	while(it.hasNext()) {
		    		rowNumber++;
		    		String rowString = it.next().replaceAll("NULL", "") + "\r\n";		    	
		    		String estimateDate =  rowString.substring(rowString.indexOf(',')+1, rowString.indexOf(',')+9);
		    		
		    		
		    		Chronology chrono = GregorianChronology.getInstance();
		    		LocalDate theDay = new LocalDate(Integer.valueOf(estimateDate.substring(0, 4)), 
		    				Integer.valueOf(estimateDate.substring(4, 6)),
		    				Integer.valueOf(estimateDate.substring(6, 8)), chrono);
		    		
		    		String modifiedDay = theDay.minusDays(dInx).toString("yyyyMMdd");
		    		String modifiedRow = rowString.substring(0, rowString.indexOf(',')+1) + modifiedDay + rowString.substring(rowString.indexOf(',')+9);
		    		
		    		FileUtils.writeStringToFile(monthFile, modifiedRow, true);
		    		
		    		if(rowNumber%10000 == 0) {
		    			System.out.print(".");
		    		}
		    	}		    	
		    	System.out.println("");		    	
		    }
		    
		    
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
	}
	
	public void makeBulkDataUsingNIO(int startCount, int endCount) {
		
		Path readPath = Paths.get(seedFile);
		
		try (BufferedReader reader = Files.newBufferedReader(readPath, Charset.forName("UTF-8"));
			 FileOutputStream writeFos 
			 	= new FileOutputStream(bulkFile);
			 FileChannel cout = writeFos.getChannel();) {			
			
			List<String> seedData = new ArrayList<String>();
		    String tempRow = "";
		    while ((tempRow = reader.readLine()) != null) {
		    	seedData.add(tempRow+"\r\n");
		    }	    
		    
		    for ( int dInx=startCount; dInx<=endCount; dInx++ ) {		    	
		    		    	
		    	for( int rInx=0; rInx<seedData.size(); rInx++ ) {		    		
		    				    	
		    		String estimateDate = seedData.get(rInx).substring(seedData.get(rInx).indexOf(',')+1, seedData.get(rInx).indexOf(',')+9);		    		
		    		
		    		Chronology chrono = GregorianChronology.getInstance();
		    		LocalDate theDay = new LocalDate(Integer.valueOf(estimateDate.substring(0, 4)), 
		    										 Integer.valueOf(estimateDate.substring(4, 6)),
		    										 Integer.valueOf(estimateDate.substring(6, 8)), chrono);
		    		
		    		String modifiedDay = theDay.minusDays(dInx).toString("yyyyMMdd");
		    		String modifiedRow = seedData.get(rInx).substring(0, seedData.get(rInx).indexOf(',')+1) 
		    				           + modifiedDay + seedData.get(rInx).substring(seedData.get(rInx).indexOf(',')+9);
		    		
		    		//FileUtils.writeStringToFile(monthFile, modifiedRow, true);
		    		cout.write( ByteBuffer.wrap( modifiedRow.getBytes() ) );
		    		
		    		if(rInx%10000 == 0) {
		    			System.out.print(".");
		    		}
		    	}
		    	System.out.println("[" + dInx + "/" + endCount + "]");
		    }
		    
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	public void makeBulkDataUsingMap(int startCount, int endCount) {
		
		Path readPath = Paths.get(seedFile);
		
		try (BufferedReader reader = Files.newBufferedReader(readPath, Charset.forName("UTF-8"));
			 FileChannel rwChannel = new RandomAccessFile(bulkFile, "rw").getChannel(); ) {			
			
			List<String> seedData = new ArrayList<String>();
			String tempRow = "";
			long size = 0;
			while ((tempRow = reader.readLine()) != null) {
				seedData.add(tempRow+"\r\n");				
				size += (tempRow+"\r\n").getBytes().length;
			}	    
			
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, size*(endCount-startCount));
			
			for ( int dInx=startCount; dInx<=endCount; dInx++ ) {
				
				for( int rInx=0; rInx<seedData.size(); rInx++ ) {		    		
					
					String estimateDate = seedData.get(rInx).substring(seedData.get(rInx).indexOf(',')+1, seedData.get(rInx).indexOf(',')+9);		    		
					
					Chronology chrono = GregorianChronology.getInstance();
					LocalDate theDay = new LocalDate(Integer.valueOf(estimateDate.substring(0, 4)), 
							Integer.valueOf(estimateDate.substring(4, 6)),
							Integer.valueOf(estimateDate.substring(6, 8)), chrono);
					
					String modifiedDay = theDay.minusDays(dInx).toString("yyyyMMdd");
					String modifiedRow = seedData.get(rInx).substring(0, seedData.get(rInx).indexOf(',')+1) 
							+ modifiedDay + seedData.get(rInx).substring(seedData.get(rInx).indexOf(',')+9);
					

					wrBuf.put(modifiedRow.getBytes());
					
					if(rInx%10000 == 0) {
						System.out.print(".");
					}
				}
				System.out.println("");
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}

}
