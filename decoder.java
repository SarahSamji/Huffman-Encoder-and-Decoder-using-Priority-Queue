import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;



public class decoder {
	
	static Node root=null;
	static Node next=null;
	static boolean flag=false;
	public static class Node		
	{
        public int key;
        public final String code;
        public Node left;
		public Node right;

        Node(int key, String code, Node left, Node right) {
            this.key    = key;
            this.code  = code;
            this.left  = left;
            this.right = right;
            
        }

               @Override
        public String toString() {
          return "[ key=" + key + ", code=" + code + ", left=" + left + ", right="+ right +"]";
       
        }


	}
	
	
	public static void main(String args[]) throws IOException
	{
		
		//long startTime = System.currentTimeMillis();
		BufferedReader br = null;
		FileReader fr = null;
		try 
		{		
			fr = new FileReader(args[1]);
			br = new BufferedReader(fr);
			String cur_line;
			
			while((cur_line= br.readLine()) != null && !cur_line.isEmpty())		//reading code_table.txt
			{
				 
				String[] key_code = cur_line.split(" ");  
				int key=Integer.parseInt(key_code[0]);
				insert(key,key_code[1]);			
									
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

		decode(args[0]);
		//System.out.println(System.currentTimeMillis()-startTime);
					
	}
	
	public static void insert(int key, String code)			//each key-code pair is inserted into the decoder tree
	{
		Node temp=root;
		Node prev=null;
		next=null;
		int l=code.length();
	
		for(int i=0;i<l;i++)
		{
			if(root==null)
			{
				temp =new Node(-1,"-1",null,null);
				root=temp;
				flag=true;
				
			}
			if(code.charAt(i)=='0')
			{		if(temp.left!=null)
						{
						temp=temp.left;
						}
					else{
										
						next = new Node(-1,"-1",null,null);
						
						temp.left=next;
						temp=next;
						
					}
			}
			else
			{
				if(temp.right!=null){
					temp=temp.right;
					
				}
				else{
					
					next = new Node(-1,"-1",null,null);
					
					temp.right=next;
					temp=next;
					
					
				}
			}
		}
	
		temp.key = key;
		
	}
	
	private static void decode(String args)throws IOException {			// produces decoded.txt

		
		File decoded_file = new File("decoded.txt");
		BufferedWriter output = new BufferedWriter(new FileWriter(decoded_file));
		File encodedFile = new File(args);
		String inputFile =encodedFile.getAbsolutePath();
		long fileSize = new File(inputFile).length();
		byte[] data = new byte[(int) fileSize];
		FileInputStream input = new FileInputStream(inputFile);
		next=root;
		Node rootTemp=root;
		while ((input.read(data)) != -1)
		{
			
			for(int i = 0; i < data.length; i++){
				for(int j=0; j<8;j++){
					if((data[i] & (1 << (7 - (j)))) > 0)			//Reading values at bit position 
						{
						rootTemp=rootTemp.right;
						
						}
					else
					{
						
						rootTemp=rootTemp.left;
					}
		    		if(rootTemp.left == null && rootTemp.right == null){
		        		output.write(rootTemp.key+"\n");
		        		rootTemp=root;
		        	}
				}
		        }
			}
		
		output.close();
		input.close();
		
	}

}