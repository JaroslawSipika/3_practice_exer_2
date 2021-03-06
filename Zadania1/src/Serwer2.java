import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Serwer2 {
	
		static int liczbaKlientów = 0;

		public static void main(String[] args) throws IOException  
	    { 
	        ServerSocket ss = new ServerSocket(5055); 
	          
	        while (true)  
	        { 
	        	
	            Socket s = null; 
	              
	            try 
	            { 
		            s = ss.accept(); 
	           
	                System.out.println("A new client is connected : " + s); 
	                  
	 
	                DataInputStream dis = new DataInputStream(s.getInputStream()); 
	                DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
	                  
	                System.out.println("Assigning new thread for this client"); 
	  
	            
	                if (liczbaKlientów < 250) {
	                liczbaKlientów++;
	                Thread t = new ClientHandler(s, dis, dos); 
	                t.start(); 
	                liczbaKlientów--;
	                
	                } else {
	                	System.out.println("Serwer Przepełniony");
	                	s.close();
	                }
	                
	            } 
	            catch (Exception e){ 
	                s.close(); 
	                e.printStackTrace(); 
	            } 
	        }  
	    } 
	} 
	  

	class ClientHandler extends Thread  
	{ 
	   
	    final DataInputStream dis; 
	    final DataOutputStream dos; 
	    final Socket s;
	    ArrayList<Pytanie> listaPytan;
		int m = 0;
		int t = 0;
		int c = 0;
		int liczbaPytan = 0;
	
	    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos)  
	    { 
	    	wczytajPytania();
	    	this.s = s; 
	        this.dis = dis; 
	        this.dos = dos;
	    } 
	    
		public class Pytanie{
			
			public String wiersz1;
			
			public Pytanie(String a) {
				{
					wiersz1 = a;
				}
			
		}
		}
	
		public void wczytajPytania() {
			
			listaPytan = new ArrayList<Pytanie>();
			try {
				File mojPlik = new File("bazaPytan.txt");
				FileReader czytelnikF = new FileReader(mojPlik);
				BufferedReader czytelnikK = new BufferedReader(czytelnikF);
				
				String wiersz = null;
				
				while((wiersz = czytelnikK.readLine()) != null) {
		
					Pytanie pytanko = new Pytanie(wiersz);
					listaPytan.add(pytanko);
				}
				liczbaPytan = listaPytan.size();
				czytelnikK.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	    
		
	    @Override
	    public void run()  
	    { 
	        String received; 
	        String toreturn; 
	        String[] odpowiedzi;
	        ArrayList listaOdpowiedzi = new ArrayList();
			
	        
	        while (true)  
	        { 
	            try { 
	            
	            		dos.writeUTF(listaPytan.get(m).wiersz1);
	        			m++;
		            	received = dis.readUTF(); 
	               
	
	            
	                if(received.equals("Exit")) 
	                {  
	                    System.out.println("Client " + this.s + " sends exit..."); 
	                    System.out.println("Closing this connection."); 
	                    this.s.close(); 
	                    System.out.println("Connection closed"); 
	                    break; 
	                } 
	                  
	                listaOdpowiedzi.add(received);          
  
	              
	               if(t == 0 && received.equals("a"))
	               {
	            	   c++;
	            	    
	               }
	               if(t == 1 && received.equals("a"))
	               {

	            	   c++;
	               }
	               if(t == 2 && received.equals("c")) 
	               {

	            	   c++;
	               }
	               if(t == 3 && received.equals("c"))
	               {
	            	   c++;
	               }
	           

	               	String punkty = Integer.toString(c);
	               	t++;
	                
	             
	                switch (received) { 
	                  
	                    case "a" : 
	                        toreturn = "Zapisane i sprawdzone. Masz " + punkty + " punktów"; 
	                        dos.writeUTF(toreturn); 
	                        break; 
	                          
	                   case "b" : 
	                        toreturn = "Zapisane i sprawdzone. Masz " + punkty + " punktów"; 
	                        dos.writeUTF(toreturn); 
	                        break; 
	                   
	                   case "c" : 
	                        toreturn = "Zapisane i sprawdzone. Masz " + punkty + " punktów"; 
	                        dos.writeUTF(toreturn); 
	                        break;
	                        
	                   case "d" : 
	                        toreturn = "Zapisane i sprawdzone. Masz " + punkty + " punktów";  
	                        dos.writeUTF(toreturn); 
	                        break;  
	                        
	                               
	                    default: 
	                        dos.writeUTF("Invalid input. Masz: " + punkty + " punktów"); 
	                        break; 
	                } 
	                
	               
	                if(t==liczbaPytan) {
	                dos.writeUTF("Podaj imie i nazwisko, Twoje punkty to: " + punkty); 
	                String imie = dis.readUTF();
	                String lkl = "odpowiedzi " + imie + ".txt";
	                FileWriter writerodp = new FileWriter(lkl);
	                
		               for(int i = 0; i<liczbaPytan; i++) {
		                writerodp.write((String)listaOdpowiedzi.get(i));
		                }
	                writerodp.close();
	                }

	             
	            } catch (IOException e) { 
	                e.printStackTrace(); 
	            }
	            
	        }
	       
	        try
	        { 
	            
	            this.dis.close(); 
	            this.dos.close(); 
	            this.s.close();
	              
	        }catch(IOException e){ 
	            e.printStackTrace(); 
	            
	        } 
	       
	    }
}

