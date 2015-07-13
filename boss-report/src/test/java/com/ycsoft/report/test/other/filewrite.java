package com.ycsoft.report.test.other;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class filewrite {
	public static void main(String[] args) throws Exception   
    {       
		StringBuilder sbc = new java.lang.StringBuilder();  
        for(int j = 0; j < 1; ++j)   
        {   
            for(int k = 0; k < 1024; ++k)   
            {   
            	sbc.append("a");   
            }   
            sbc.append("\n");   
        }   
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("E:/performace.txt"));   
        long start = System.currentTimeMillis() ;   
        for(int i = 0; i < 100*1024; ++i)   
        {   
          
            bufferedWriter.write(sbc.toString());  // 积累1M数据，才写一次   
        }   
        bufferedWriter.flush();   
        bufferedWriter.close();   
        long end = System.currentTimeMillis() ;   
        System.out.println((end - start) + "ms elapsed."); 

        //读文件
        LineNumberReader linereader=new LineNumberReader(new FileReader("E:/performace.txt"));
        String aa="";
        while((aa=linereader.readLine())!=null){
        	if(linereader.getLineNumber()==10000)
        		System.out.println("aa");
        }
         start = System.currentTimeMillis() ;   
         System.out.println((start-end  ) + "ms elapsed."); 
         linereader.close();
         
         BufferedReader br=new BufferedReader(new FileReader("E:/performace.txt"));
         char cbuf[]=new char[sbc.length()];
         System.out.println(
         br.read(cbuf, 0+sbc.length()*87*1024, sbc.length()*88*1024-100));
         
         //测试对象流
         ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("E:/tt.txt") );
         
         
         start = System.currentTimeMillis() ; 
         StringBuilder sb = new java.lang.StringBuilder();  
         for(int i = 0; i < 100; ++i)   
         {   
        	 List<String> list=new ArrayList<String>(30);
             for(int j = 0; j < 1024; ++j)   
             {   
            	
            	 
                     for(int k = 0; k < 30; ++k)   
                     {   
                    	 sb.delete(0, sb.length());
                    	 list.add(sb.append("a").append(i).append(j).append(k).toString());
                     }   
                     
                 
             }  
             oos.writeObject(list);
             oos.flush();
        	 oos.reset();
         }
         oos.flush();
         oos.close();
         end=System.currentTimeMillis() ;   
         System.out.println((end - start) + "ms elapsed."); 
         
         
       //测试对象流
          oos = new ObjectOutputStream(new FileOutputStream("E:/tt1.txt") );
         
         
         start = System.currentTimeMillis() ; 
         
         for(int i = 0; i < 100; ++i)   
         {   
        	 String[][] aa1=new String[1024][30];  
             for(int j = 0; j < 1024; ++j)   
             {   
            	     
                     for(int k = 0; k < 30; ++k)   
                     {   
                    	 sb.delete(0, sb.length());
                    	
                    	 aa1[j][k]= sb.append("a").append(i).append(j).append(k).toString();
                     }   
                
                 
             }  
             oos.writeObject(aa1);
             oos.flush();
        	 oos.reset();
         }
         oos.flush();
         oos.close();
         end=System.currentTimeMillis() ;   
         System.out.println((end - start) + "ms elapsed."); 
         System.gc();
         end=System.currentTimeMillis() ;   
         ObjectInputStream ois = new ObjectInputStream( new FileInputStream("E:/tt.txt"));
         Object list1=null;
         int b=0;
         try{
         while((list1= ois.readObject())!=null) b++;
         }catch(java.io.EOFException e){
        	// e.printStackTrace();
         }catch(Exception e){
        	 e.printStackTrace();
         }
         System.out.println(b);
         ois.close();
         start=System.currentTimeMillis() ;   
         System.out.println((start - end) + "ms elapsed."); 
         System.gc();
         end=System.currentTimeMillis() ;   
        
        end=System.currentTimeMillis() ; 
          ois = new ObjectInputStream( new FileInputStream("E:/tt1.txt"));
          list1=null;
          b=0;
         try{
         while((list1= ois.readObject())!=null) b++;
         }catch(java.io.EOFException e){
        	// e.printStackTrace();
         }catch(Exception e){
        	 e.printStackTrace();
         }
         System.out.println(b);
         ois.close();
         start=System.currentTimeMillis() ;   
         System.out.println((start - end) + "ms elapsed."); 
    }   
}
