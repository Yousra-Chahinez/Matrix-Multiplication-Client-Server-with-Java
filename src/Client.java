import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException{

		Socket s = null;
		try 
		{
			s = new Socket("localhost",32000);
		
		    InputStreamReader inputStreamReader;
		    PrintWriter pw;
		    BufferedReader bf, br2;
		    
		    /*
		     * the inputStream and output stream objects 
		     * are extracted from the current request's socket object*/
		    
			pw = new PrintWriter(s.getOutputStream(), true);
			inputStreamReader = new InputStreamReader(s.getInputStream());
			bf = new BufferedReader(inputStreamReader);
			Scanner scanner = new Scanner(System.in);
		   
			while(true)
			{
				 //On récupère maintenant un text du serveur 
		         //dans un flux,
			String question = bf.readLine();
			System.out.println("Server : "+question+"\n");
			
			String response = scanner.nextLine();
			
			pw.println(response);
			
			if(response.equals("exit"))
			{
				System.out.println("Closing the connection : "+s);
				s.close();
				System.out.println("Connection closed.");
				System.exit(0);
			}
			
				String received = bf.readLine();
				System.out.println("Server : "+received+"\n");
				
				
			if(response.equals("multi"))
			{
				int i,j;
				int mat1[][],mat2[][];
				
			    //ObjectOutputStream outMat = new ObjectOutputStream(s.getOutputStream());
			    //ObjectInputStream inputMat = new ObjectInputStream(s.getInputStream());
								
				br2 = new BufferedReader(new InputStreamReader(System.in));
			
				String str = br2.readLine();
		
				if(str.matches("[0-9]+"))
				{
					int order = Integer.parseInt(str);
				    pw.println(order);
						
				 mat1 = new int[100][100];
				 mat2 = new int[100][100];
				
				 System.out.println("Values of first matrix:");
				 
				for(i=0;i<order;i++)
				{
					for(j=0;j<order;j++)
					{
						 System.out.print(" [" + (i+1) + "," + (j+1) + "]:") ;
						 int element = Integer.parseInt(scanner.nextLine());
						 mat1[i][j]=element;
					}
				}
				
				 System.out.println("Values of Second matrix:");
				 
				 for(i=0;i<order;i++)
				 {
					 for(j=0;j<order;j++)
					 {
					     System.out.print(" [" + (i+1) + "," + (j+1) + "]:") ;
						 int element = Integer.parseInt(scanner.nextLine());
						 mat2[i][j]=element;
					 }
				 }
				 ObjectOutputStream outMat = new ObjectOutputStream(s.getOutputStream());
				 ObjectInputStream input = new ObjectInputStream(s.getInputStream());
				 outMat.writeObject(mat1);
				 outMat.writeObject(mat2);
				 
				 int matResult[][]= (int[][]) input.readObject();
				 
				 System.out.println("Server : Result of multiplication \n");
				 
				 for(i=0;i<order;i++)
				 {
					 for(j=0;j<order;j++)
					 {
					     System.out.println(matResult[i][j]+" ");
					 }
					 //System.out.println("\n");
				 }	 
			    }
				else
				{
					System.out.println("Invalid Input (Order of Matrix)");
				}
			}
				
			}
		} catch (Exception e)
		{
			System.out.println(e.toString());
		}	
		finally{
	         if(s != null){
	            try {
	               s.close();
	            } catch (IOException e) {
	               System.out.println(e.getMessage());
	            }
	         }
	}

}
}
