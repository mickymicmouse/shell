package shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.*;
import java.nio.file.Paths;
import java.util.*;

public class SimpleShell {

	public static void main (String[] args) throws java.io.IOException {
		String commandLine; //define commandLine as it is String value
		BufferedReader console = new BufferedReader //define console as it is bufferedReader
				(new InputStreamReader(System.in));
		ArrayList<String> history = new ArrayList<String>(); //define new Array list history
		ProcessBuilder pb = new ProcessBuilder(); //pb is surname of ProcessBuilder 
		
		
	     File home_dir = new File(System.getProperty("user.home")); //home_dir get /home/user directory path
	     
	     String cur_path = System.getProperty("user.dir"); //cur_path String get working directory path
	     File cur_dir = new File(cur_path); //cur_dir File contains working directory path
	     pb.directory(cur_dir); // working directory is /home/user
	   /*  String path = System.getProperty("user.dir");
	     File f = new File(path);*/
	     
		
		
		//we break out with <control><C>
		while(true) {
			//read what the user entered
			System.out.print("shell>");
			commandLine = console.readLine();
			
			String [] command = commandLine.split(" "); //(1)split the command by space
			ArrayList<String> list = new ArrayList<String>(); //make new arraylist name is list
			
		
			for (int i = 0; i < command.length; i++) {
				//System.out.println(command[i]);
				
				list.add(command[i]); // save the command in the list
			
				
			}
			System.out.println(list);
			history.add(commandLine); //save all list in my list
			//System.out.println(history);
			pb.command(list);
		try {
			
			// exit and quit command execute exit program
			if (list.get(list.size()-1).equals("exit") | list.get(list.size()-1).equals("quit")) { //list contain same string exit or quit
				
				System.out.println("Goodbye");
				System.exit(0); //quit the system
			}
			

			if (list.get(list.size()-1).equals("!!")) { //if last commend contain "!!" in the list 
				if(history.size()==1) { //no input history except input commend now
					System.out.println("no history");//express error
				} else {
				
				String prev_com = history.get(history.size()-2); //prev_com String will get preview commend in the history
				command = prev_com.split(" ");//split by space
				
				pb.command(command); //execute again
				
				}
				history.remove("!!");//remove !! commend in the history
			}	else if (list.get(list.size()-1).contains("!")) { //if last commend in the list contain "!"
					int a = Character.getNumericValue(list.get(list.size()-1).charAt(1));
					//charAt mean how many times order from front side
					//0 is !, 1 is number which i enter.
					//list.get(list.size()-1) is last commend in the list
					//character.getnumericValue means value of character change to integer value
					String n_com = history.get(a);//n_com String get a(th) history value
					command = n_com.split(" ");//split the a(th)command in the history by space
					
					if(a<=history.size()) {
					pb.command(command); //if a value if smaller than size of history, execute again
					} else {
						System.out.println("default number"); //or not express error
					}
			history.remove("!"+a); //remove the !+a in the history
			} 
			

			
			//history command execute to show list of my list
			if (list.get(list.size()-1).equals("history")) {//it means that last commend is history
				
				if(history.size()==1) { //history is no value just include commend history at the first time.
					
					System.out.println("no history"); 
					continue;
				} else {
				for (int i = 0; i < history.size()-1; i++) { //represent all commend that we write by repeating except wrting commend now
					System.out.println(i + "  " + history.get(i));
				}
				}
				history.remove("history");
			}
			
			/* command = in linux cat, ls, ps => in window type [filename], dir, tasklist
			in linux cd => in Window cd */
			
		
			
			if(list.contains("cd")) {
				
		
				if(list.get(list.size()-1).equals("cd")) { //last string is cd than go to /home/user directory
					cur_dir = home_dir; //cur_dir is working directory, home directory is address of home.
					System.out.println(cur_dir);
					pb.directory(cur_dir);
				} 
				else if(list.get(list.size()-1).equals("..")) { //input "cd .." go to previous directory
					File prev_dir = new File(pb.directory().getParent());
					//prev_dir is new file which is parent of working directory, getparent execute to get address of upper directory
					System.out.println(prev_dir);
					pb.directory(prev_dir);
					continue;
					
				}
			
						
				else {
				
		
					String dir = list.get(1); //get String in the input which is next cd
					File go_dir = new File(dir); // make new File get upper value
					File new_dir = new File(pb.directory() + File.separator + go_dir);
					//new_dir is new file which is working file plus input directory

					
					
					if(go_dir.isAbsolute()) { // if my input directory path is absolute path
						if (list.get(list.size()-1).contains(pb.directory().getName())) {// if input directory include working directory add child directory
							String re_path = new File(pb.directory().getParentFile()+File.separator + dir).getAbsolutePath(); //get path from parent of working directory
							File re_dir = new File(re_path); // make new File follow upper path 
							if (!re_dir.exists()) { //error check
								System.out.println("wrong path");
								continue; //if error is exist, do again, don't go next step
							}
							pb.directory(re_dir);
							System.out.println(re_dir);
						} else {
						cur_path = go_dir.getAbsolutePath(); //cur_path get absolute path of input directory
						cur_dir = new File(cur_path); // cur_dir is new file include cur_path
						
						
							if (!cur_dir.exists()) { //if file don't exist, express error comment
							System.out.println("hey it is not correct Absolute path plz check again!!");
							continue;
							}
						pb.directory(cur_dir); //processBuildr work to move follow cur_path
						System.out.println(cur_dir); 
						}
					}	
						 
					else { //if my input directory path is relative path
						if (pb.directory().getName().contains(list.get(list.size()-1))) {//if input file just working file than stay working directory
							pb.directory(); //working directory
							System.out.println(pb.directory());
						}
						else {
							cur_path = new_dir.getAbsolutePath(); 
						//get total path of new_dir which include path of working directory+input directory
						cur_dir = new File(cur_path);
						
						if (!cur_dir.exists()) {
							System.out.println("hey it is not correct Relative path plz check again!!");
							continue;	
						}
						pb.directory(cur_dir);
						System.out.println(cur_dir);
							}
						} //same code with upper absolute path code
					
				}
				
				
			}
		
            if (command.length == 0) { //OSProcess
    			System.err.println("Usage: java OSProcess <command>");
    			System.exit(0);
    		}
			
			//list is the command that is run in a separate process
           
			Process process = pb.start(); //start ProcessBuilder
			
			//obtain the input stream
			InputStream is = process.getInputStream(); //3stage of inputing case first process get input data and send to is Inputstream
			//and is get and send. and isr get input data. finally br BufferedReader read and save data in buffer
			
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			//read the output of the process
			String line;
			while((line = br.readLine())!= null) //read and print the command
			System.out.println(line);
	
		
			br.close();
			
			
		     
	         //if the user entered a return, just loop again
			if (commandLine.equals(""))
				list.remove(" ");
				continue;
				
			} 	catch (IOException e){
				//System.out.println("ERROR");
			}
		}
	}
}
