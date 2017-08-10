import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;



public class encoder 
{
	static public int[] freq_table=new int[1000000]; 	 
	static int i=0;
	public static ArrayList<Node> heap = new ArrayList<Node>();
	static String[] code_table=new String[1000000];
	static String code="";	
	static int fileflag=0;
	long avgt=0;
	static Node root=null;
	
	
										
	public static void main(String args[]) throws IOException
	{
		//long startTime = System.currentTimeMillis();
		File file = new File("code_table.txt");	
		FileWriter fw1  = new FileWriter(file);	
		BufferedWriter fw=new BufferedWriter(fw1);
		frequency_table(args[0]);
		priority_queue(freq_table);
		huffman_tree();		
		code_generation(heap.get(0),code,fw);
		encoder_file(args[0]);		
		//System.out.println(System.currentTimeMillis()-startTime);
		fw.close();		
		
}
	private static class Node implements Comparable<Node>
	{
        private final int key;
        private final int freq;
        private final Node left, right;

        Node(int key, int freq, Node left, Node right) {
            this.key    = key;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
            
        }

       // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
        

        @Override											//print format of node
        public String toString() {
          return "[ key=" + key + ", freq=" + freq + ", left=" + left + ", right="+ right +"]";
     
        }


	}
	public static void frequency_table(String args)			//build's the frequnecy table
	{
		BufferedReader br = null;
		FileReader fr = null;
		try 
		{

			fr = new FileReader(args);
			br = new BufferedReader(fr);	
			
			String cur_line;
			
			while((cur_line= br.readLine()) != null && !cur_line.isEmpty())
			{
				
				String[] key_freq = cur_line.split(" ");  				
				int key=Integer.parseInt(key_freq[0]);
				freq_table[key]+=1;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();

		}
		finally 
		{

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

				} 
			catch (IOException ex) 
				{
					ex.printStackTrace();

				}

		}
	}
	
		
	
		public static void priority_queue(int[] freq_table)			//building the min heap.
		{
			for(int i=0;i<=999999;i++)
			{
				if(freq_table[i]!=0)
				{
						Node n=new Node(i,freq_table[i],null,null);			//each element in freq_table is given a node and heapify is called
						heapifyup(n);			 
				}
			}
		
			try
			{
			if( heap.size()==0)
				throw new IOException();
			}
			catch(IOException e)
			{
				System.out.print("Tree is empty");
				System.exit(1);
			}
		}
		 
		
		public static void heapifyup(Node n)				//heapify upon insertion
		{
			int cur_node = heap.size();
			heap.add(n);
			int parent=0;
		
		
			if( heap.size()>0)
			{     
				while (cur_node >0)
				{
					parent = (cur_node - 1) / 2;
				 
			
					if ( n.freq >heap.get(parent).freq) 			//comparing with parent node and swapping if current node is smaller
					{
	            	     break;
					}
					heap.set(cur_node, heap.get(parent));
					cur_node = parent;
	       
				}
				heap.set(cur_node, n);
			}		 
		}
	
	 
		public static void heapifydown(Node n)			//heapify incase of deletion
		{
		 int cur_node=0;
		 int left_child=(2*cur_node)+1;
		 int right_child=(2*cur_node)+2;
		
		 while(left_child< heap.size())
		 {
			if(right_child< heap.size())
			{
				if( heap.get(left_child).freq <=  heap.get(right_child).freq )			//comparing child nodes to find the smaller node
					
				{
					if( heap.get(left_child).freq < n.freq)					//swap smaller child with current node
					{ 
						 heap.set(cur_node, heap.get(left_child));
						cur_node=left_child;

						
					}	
					else
						break;
				}
				else
				{
					if( heap.get(right_child).freq < n.freq)
					{
						 heap.set(cur_node, heap.get(right_child));
						cur_node=right_child;

						
					}
					else
						break;
				}
			}	
			else
			{
				if( heap.get(left_child).freq < n.freq)
				{ 
					 heap.set(cur_node, heap.get(left_child));
					cur_node=left_child;

					
				}	
				else
					break;
			}
			 left_child=(2*cur_node)+1;			//updating pointers
			 right_child=(2*cur_node)+2;
		}
		 heap.set(cur_node, n);		
		
	 }
	 
		public static void remove(int ind)				//replaces min node with rightmost element upon remove min operation
		{
		
		   heap.set(0, heap.get(ind));
		   heap.remove(ind);
		   heapifydown( heap.get(0));
		 
		}
		
	 	public static void huffman_tree()				//builds the huffman tree
	 	{			 
	 		while( heap.size()>=2)						
	 		{
	 			Node l= heap.get(0);			//remove first min node
	 			int f1=l.freq;
	 			remove( heap.size()-1);
		 
	
	 			Node r= heap.get(0);			//remove second min node
	 			int f2=r.freq;
	 			if( heap.size()>1)				 
	 				remove( heap.size()-1);
	 			else
	 				heap.remove(0);
		 
	 			Node n=new Node(-1,f1+f2,l,r);		//insert new node with combined frequency
	 			heapifyup(n);
	 		}
	 		
		 }
	
	 //recursive function to generate code of keys from huffman tree
	 public static void code_generation(Node n, String code, BufferedWriter fw) throws IOException
		{
		 		 
		 if(n.left!=null)
		 {
			  code_generation(n.left, code+"0",fw);
		 }
		  if(n.right!=null)
		 {
			  code_generation(n.right,code+"1",fw);
	   	}
		 if(n.right==null &&n.left==null)
		 {
			code_table[n.key]=code;			
			 String content = n.key+ " "+code+ "\n";
			 fw.write(content);				//writing to file
			 
				 
		 }
		 
		}
	 
	 //Uses ouputstream to write binary encoded data to the file
	 public static void encoder_file(String args)
	 {
		// String bin_code="";
		 BufferedReader br = null;
			FileReader fr = null;
			try 
			{

				fr = new FileReader(args);
				br = new BufferedReader(fr);
				File file = new File("encoded.bin");
				FileOutputStream op1 = new FileOutputStream(file);;
				BufferedOutputStream fop = new BufferedOutputStream(op1); 
				br = new BufferedReader(new FileReader(args));
				StringBuilder str=new StringBuilder();
			
				String cur_line;
				
				while((cur_line= br.readLine()) != null && !cur_line.isEmpty())
				{
					String[] key_freq = cur_line.split(" "); 
					int a=Integer.parseInt(cur_line);
					
					int byte_arr=0;
					String s=code_table[a];
					str.append(s);
					while(str.length()>=8)		//if code length is greater than 8 then read first 8 bits and append the rest to next set of data
					{
						for(i=0;i<8;i++)			
						{
							if(str.charAt(i)=='1')
								byte_arr+=1<<(7-i);			//placing bits into individual bit position of bytearray
						}
						
						fop.write(byte_arr);
						byte_arr=0;
						str = new StringBuilder(str.substring(8));			//appending remaining string to str
						}
						
					
				}
				fop.close();

			}
			catch (IOException e)
			{
				e.printStackTrace();

			}
			
		 
	 }
	
	
}