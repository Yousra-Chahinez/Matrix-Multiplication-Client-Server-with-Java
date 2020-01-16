import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

	public static void main(String[] args) throws IOException
	{
		/*Running infinite loop for getting client request.*/
		ServerSocket ss = new ServerSocket(32000);
		while(true)
		{
		try {
			/*ServerSocket --> Waiting for incoming client connection requests.
			  No socket for server initially.*/
			
			System.out.println("Waiting for client requests...");
			
			//accept Attends une demande de connexion
			//Lors de la réception d’une création d’une socket
			
			Socket clientSocket = ss.accept();
			System.out.println("A new client is connected: "+clientSocket);
			
			
			System.out.println("Assigning new thread for this client");
			
			Thread t = new ClientHandler(clientSocket);
						
			t.start();
			
		} catch(Exception e)
		{
			ss.close();
			System.out.println(e.toString());
		}	
		
		}
	}			
	
	static class ClientHandler extends Thread
	{
	    Socket clientSocket;
		
		public ClientHandler(Socket clientSocket)
		{
			this.clientSocket=clientSocket;
		}
		
		@Override
		public void run() {
			InputStreamReader input=null;
			PrintWriter pw=null;
			BufferedReader bf;
			ObjectInputStream inputObj;
			ObjectOutputStream outputObj;
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			while(true)
			{
				try {
					pw = new PrintWriter(clientSocket.getOutputStream(), true);
				    input = new InputStreamReader(clientSocket.getInputStream());
					bf = new BufferedReader(input);
					
					//Ask client what he wants.
					pw.println("What do you want ? [date | multi]  "
							 + "Type Exit to terminate connection.");
					pw.flush();
					
	                //Read the answer from client.

					String response = bf.readLine();
					//System.out.println(response+"\n");
					
					 if(response.equals("exit")) 
		                {  
		                    System.out.println("Client " + clientSocket + " sends exit..."); 
		                    System.out.println("Closing this connection."); 
		                    clientSocket.close(); 
		                    System.out.println("Connection closed"); 
		                    break;
		                } 
					
	                //Receive the answer from client.
					
					switch (response)
					{
					case "date":
						pw.println(simpleDateFormat.format(date));
						break;
					case "multi":
						int i,j,k;
						
						pw.println("Enter order of matrices: ");
						int order = Integer.parseInt(bf.readLine());
						
						//pw.println("Enter elements of first matrix: ");
						
						inputObj = new ObjectInputStream(clientSocket.getInputStream());
				        outputObj = new ObjectOutputStream(clientSocket.getOutputStream());
					
				        int mat1[][] = (int[][]) inputObj.readObject();
						int mat2[][] = (int[][]) inputObj.readObject();
						int[][] result = new int[mat1.length][mat2.length];
						
						System.out.println("Matrix 1:");
						 for(i=0;i<order;i++)
						 {
							 for(j=0;j<order;j++)
							 {
							     System.out.print(mat1[i][j]+" ");
							 }
							 System.out.println("\n");
						 }
				
						 System.out.println("Matrix 2:");
						 for(i=0;i<order;i++)
						 {
							 for(j=0;j<order;j++)
							 {
							     System.out.print(mat2[i][j]+" ");
							 }
							 System.out.println("\n");
						 }
						 
						 System.out.println("Matrix 1 * Matrix 2= ");
							for(i=0;i<order;i++)
							{
								for(j=0;j<order;j++)
								{
									result[i][j]=0;
									for(k=0;k<order;k++)
									{
										result[i][j]=result[i][j]+mat1[i][k]*mat2[k][j];
									}
									System.out.println(result[i][j]+" ");
								}
								System.out.println("\n");
							}
						
							outputObj.writeObject(result);
							
						break;	
						default:
							pw.println("Invalid input.");
							break;
					}
					
					
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}	




