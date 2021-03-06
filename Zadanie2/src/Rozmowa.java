import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Tekst{
	
	Lock lock=new ReentrantLock(); 
	Condition txtNapisany = lock.newCondition();  
	Condition txtPobrany = lock.newCondition();
	boolean newTxt = false;

	public String pobierzTekst() {
		String Line;
		String d="";
		lock.lock(); 
		try {	
			while(newTxt == false) {
				txtPobrany.await(); 
			}
			try {
				FileInputStream fileInputStream = new FileInputStream("rozmowa.txt");
				DataInputStream dataInputStream = new DataInputStream(fileInputStream);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
				String temp=null;
				try {
					while((Line = bufferedReader.readLine()) != null) {
						temp=Line;
					}
						d += temp;
					dataInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}		
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			newTxt=false; 
			txtNapisany.signal(); 
			return d;					
		}catch(InterruptedException e) {			
				e.printStackTrace();
		}finally {
			lock.unlock(); 
		}
			return d;
	}
	

	public void ustawTekst(String str) {
		lock.lock(); 
		try {
				while(newTxt == true) {		 
					txtNapisany.await(); 
				}
				FileWriter fileWriter;
				try {
					fileWriter = new FileWriter("rozmowa.txt", true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					bufferedWriter.write(str);
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			newTxt=true; 
			txtPobrany.signal(); 
		}catch(InterruptedException e) { 
			
		}finally {
			lock.unlock(); 
		}	
	}
}

class Author implements Runnable{

	Tekst obszar;
	Scanner scanner = new Scanner(System.in);
	
	public Author(Tekst tekst) {
		obszar = tekst;
	}

	public void run() {
		
		String u;
		while(true) {
			u = (scanner.nextLine());
			if(!u.equals("exit")) {
				obszar.ustawTekst(u + "\n");
			}else {
				obszar.ustawTekst(u + "\n");
			}
		}
		
	}
	
}

class Writer implements Runnable{

	Tekst obszar;
	
	public Writer(Tekst tekst) {
		obszar = tekst;
	}

	public void run() {
		
		String tekscik = obszar.pobierzTekst();
		while(!tekscik.equals("exit")) 	// zatrzymanie
		{
			System.out.println(tekscik);
			tekscik = obszar.pobierzTekst();
		}
		
	}
	
}


public class Rozmowa {

	public static void main(String[] args) {
	
		Tekst tekst = new Tekst();
		Author author = new Author(tekst);
		Writer writer = new Writer(tekst);
		Thread thread1 = new Thread(author);
		Thread thread2 = new Thread(writer);
		thread1.start();
		thread2.start();

	}

}



