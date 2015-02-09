package nio;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

public class FileCreateTest {

	
	public void testAssertThat() {
		assertThat("test", is("test"));
	}
	
	
	public void testNioReadWrite() {
		
		long start = System.currentTimeMillis(); // 시작시간 
		
		try ( FileInputStream fis = new FileInputStream("D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_20150118.csv");
			  FileOutputStream fos = new FileOutputStream("D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_20150118_BULK.csv"); ) {
			
			FileChannel cin = fis.getChannel();		
			FileChannel cout = fos.getChannel();
			
			ByteBuffer buf = ByteBuffer.allocateDirect(10);
			while(cin.read(buf) != -1) {				
				buf.flip();
				cout.write(buf); 
			}
			
		} catch ( IOException ioe ) {			
			ioe.printStackTrace();
		} 
		
		long end = System.currentTimeMillis();  //종료시간
		System.out.println("\n##testNioReadWrite\n" + (end-start)/10 + " seconds");
	}	
	
	
	@Test
	public void testScanner() {

		long start = System.currentTimeMillis(); // 시작시간 

		
		List<String> seedData = new ArrayList<String>();
		File fileObj = new File("D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_20150118.csv");
		try ( Scanner scnr = new Scanner(fileObj); 
			  FileOutputStream fos = new FileOutputStream("D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_20150118_BULK_SCANNER.csv"); 	
				) {

			FileChannel cout = fos.getChannel();
			
			while (scnr.hasNextLine()) {
				String tmp = scnr.nextLine();
				seedData.add(tmp);
				cout.write(ByteBuffer.wrap((tmp+"\r\n").getBytes()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();  //종료시간
		System.out.println("\n##testScanner\n" + (end-start)/10 + " seconds");
	}
	
	@Test
	public void testNioBuffered() {		
				
		long start = System.currentTimeMillis(); // 시작시간
		
		
		Path path = Paths.get("D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_20150118.csv");
		Charset charset = Charset.forName("UTF-8");
		List<String> seedData = new ArrayList<String>();
		try (BufferedReader reader = Files.newBufferedReader(path, charset);
			 FileOutputStream fos = new FileOutputStream("D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_20150118_BULK_BUFFERED.csv");
				) {
			FileChannel cout = fos.getChannel();
			
		    String line = "";
		    while ((line = reader.readLine()) != null) {
		    	seedData.add(line);
		    	cout.write(ByteBuffer.wrap((line+"\r\n").getBytes()  ));
		    }	    
		    
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		
		long end = System.currentTimeMillis();  //종료시간
		System.out.println("\n##testNioBuffered\n" + (end-start)/10 + " seconds");
	}
	
	
	@Test
	public void testNioBufferedMapped() {		
		
		long start = System.currentTimeMillis(); // 시작시간
		
		
		Path path = Paths.get("D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_20150118.csv");
		Charset charset = Charset.forName("UTF-8");
		List<String> seedData = new ArrayList<String>();
		try (BufferedReader reader = Files.newBufferedReader(path, charset);
			 FileChannel rwChannel = new RandomAccessFile("D:/000_pink_factory/01_projects/800_SGS/20_EMS_Platform/성능테스트/TH_DZI_BEMS_TIME_DATA_STORE_20150118_BULK_BUFFEREDMAPPED.csv", "rw").getChannel();				
				) {
						
			String line = "";
			long size = 0;
			while ((line = reader.readLine()) != null) {
				
				seedData.add(line);
				byte[] addedData = (line+"\r\n").getBytes();
				size += addedData.length;
				
			}	    
			
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, size);
			for (int i = 0; i < seedData.size(); i++) {
			    wrBuf.put(seedData.get(i).getBytes());
			}
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		
		long end = System.currentTimeMillis();  //종료시간
		System.out.println("\n##testNioBufferedMapped\n" + (end-start)/10 + " seconds");
	}
	
	
}
