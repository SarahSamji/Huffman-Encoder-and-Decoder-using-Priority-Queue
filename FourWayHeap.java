import java.util.ArrayList;

public class FourWayHeap {

			
		FourWayHeap(){}
			
			public static ArrayList<Node> heap = new ArrayList<Node>();
			public static ArrayList<Node> huff_tree=new ArrayList<Node>();
				
		 private static class Node implements Comparable<Node>
		 {
		        private final long key;
		        private final int freq;
		        private final Node left, right;

		        Node(long key, int freq, Node left, Node right) {
		            this.key    = key;
		            this.freq  = freq;
		            this.left  = left;
		            this.right = right;
		            
		        }

		       // compare, based on frequency
		        public int compareTo(Node that) {
		            return this.freq - that.freq;
		        }
		        
		
		        @Override
		        public String toString() {
		          //  return "[ key=" + key + ", freq=" + freq + ", left=" + left + ", right="+ right +"]";
		        	return "freq"+freq;
		        }

		
		 }
		 public static void build(int[] freq_table)
		 {
			 for(int i=0;i<=999999;i++)
		 	 {
				 if(freq_table[i]!=0)
				 {
				 Node n=new Node(i,freq_table[i],null,null);
				 heapifyup(n);			 
				 }
		 	 }
			
			 /*for(Node str: heap)
			 		System.out.print(str+" ");
			 System.out.println(" ");
			 	*/	 
			 if(heap.size()==0)
			 {
				System.out.print("Tree is empty");
			 }
			 
			 //building huffman tree
			 while(heap.size()>=2)				
			 {
				 	rem_merge();
			 }
				 		 
			 if(heap.size()==1)
			 {
				 for(Node str: huff_tree)
						System.out.print(str+" ");
				 System.out.println("");
				 heap.clear();
				 huff_tree.clear();
			 }
			 
		 }
		 
		 public static void heapifyup(Node n)
		 {
			 int cur_node = heap.size();
			 heap.add(n);
			 int parent=0;
			
			// System.out.println(cur_node+ " "+ n.freq + " "+n.key);
			 if(heap.size()>0)
			 {     
				 while (cur_node >0)
				 {
					 parent = (cur_node - 1) / 2;
					 
				//	System.out.println("parent" +parent);
					 
		            if ( n.freq >= heap.get(parent).freq) 
		            {
		            	// parent = (cur_node - 1) / 2;
		          //  System.out.println("break" +parent+ " " +cur_node+ " "+heap.get(parent).freq + " "+n.freq);
		                break;
		            }
		             heap.set(cur_node, heap.get(parent));
		            cur_node = parent;
		         //  System.out.println("breakafter" +parent+" "+cur_node);
				 }
				 heap.set(cur_node, n);
			 } 
		 }
		 
		 
		 public static void heapifyup_huff(Node n)
		 {
			 int cur_node = huff_tree.size();
			 huff_tree.add(n);
			 int parent=0;
			
			// System.out.println(cur_node+ " "+ n.freq + " "+n.key);
			 if(huff_tree.size()>0)
			 {     
				 while (cur_node >0)
				 {
					 parent = (cur_node - 1) / 2;
					 
				//	System.out.println("parent" +parent);
					 
		            if ( n.freq >= huff_tree.get(parent).freq) 
		            {
		            	// parent = (cur_node - 1) / 2;
		          //  System.out.println("break" +parent+ " " +cur_node+ " "+heap.get(parent).freq + " "+n.freq);
		                break;
		            }
		            huff_tree.set(cur_node, huff_tree.get(parent));
		            cur_node = parent;
		         //  System.out.println("breakafter" +parent+" "+cur_node);
				 }
				 huff_tree.set(cur_node, n);
			 } 
		 }
		 
		 public static void heapifydown(Node n)
		 {
			 
			 int cur_node=0;
			 int left_child=(2*cur_node)+1;
			int right_child=(2*cur_node)+2;
			
			while(left_child<heap.size())
			{
				if(right_child<heap.size())
				{
					if(heap.get(left_child).freq <= heap.get(right_child).freq )
						
					{
						if(heap.get(left_child).freq < n.freq)
						{ 
							heap.set(cur_node,heap.get(left_child));
							cur_node=left_child;

							//System.out.println(cur_node+ " left  "+heap.get(left_child).freq+ " "+ n.freq + " "+n.key);
						}	
						else
							break;
					}
					else
					{
						if(heap.get(right_child).freq < n.freq)
						{
							heap.set(cur_node,heap.get(right_child));
							cur_node=right_child;

							//System.out.println(cur_node+ " "+ heap.get(right_child).freq+" "+ n.freq + " "+n.key);
						}
						else
							break;
					}
				}	
				else
				{
					if(heap.get(left_child).freq < n.freq)
					{ 
						heap.set(cur_node,heap.get(left_child));
						cur_node=left_child;

						//System.out.println(cur_node+ " left  "+heap.get(left_child).freq+ " "+ n.freq + " "+n.key);
					}	
					else
						break;
				}
				 left_child=(2*cur_node)+1;
				 right_child=(2*cur_node)+2;
			}
			heap.set(cur_node, n);		
			 
		 }
		 public static void remove(int ind)
		 {
			 //System.out.println( "etnered");

			 heap.set(0,heap.get(ind));
			 heap.remove(ind);
			 heapifydown(heap.get(0));
			 
		 }
			
		 public static void rem_merge()
		 {			 
			 Node l=heap.get(0);
			 int f1=l.freq;
			 remove(heap.size()-1);
			 /*for(Node str: heap)
					System.out.print(str+" ");
			 System.out.println("");
			 */
			  Node r=heap.get(0);
			 int f2=r.freq;
			 if(heap.size()>1)				 
				 remove(heap.size()-1);
			 else
				 heap.remove(0);
			 /*for(Node str: heap)
					System.out.print(str+" ");
			 System.out.println("");
			  */
			// System.out.println( "remove "+ l + " "+r);
			 Node n=new Node(-1,f1+f2,l,r);
			 heapifyup_huff(n);
			 //heap.remove(0);
			 //heap.remove(0);		 
			 
			// Collections.sort(heap);
			/* for(Node str: heap)
					System.out.print(str+" ");
			 System.out.println("");*/
			
			   }
		
}