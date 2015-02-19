import java.io.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.nodes.*;

class JavaManual{
	//program
	private BufferedReader reader;
	private Scanner scanner;
	private String option;
	private String[] cmdArgs = new String[4];
	private String docPath = "C:/javaDocs/docs/api/java";
	//colors
	private String RESET= "\u001B[0m";
	private String GREEN = "\u001B[32m";
	private String PURPLE = "\u001B[35m";
	private String YELLOW = "\u001B[33m";
	private String RED = "\u001B[31m";

	JavaManual(){
		clearScreen();
		System.out.println(PURPLE + "Java Docs" + RESET);

		while(true){
			scanner = new Scanner(System.in);
			System.out.println(GREEN + "options[packages][show][exit]" + RESET);
			option = scanner.nextLine();
			parseOption();

			switch(cmdArgs[0]){
				case"packages":
					listPackages();
				break;

				case"show":
					show();
				break;

				case"exit":
					exit();
				break;

				default:
					error("cmd");
				break;
			}
		}
	}

	void clearScreen(){
		try{
			Process p = Runtime.getRuntime().exec("clear");
			p.waitFor();
			BufferedReader br = new BufferedReader
			(new InputStreamReader(p.getInputStream()));
			String line = "";
			while((line = br.readLine()) != null){
				System.out.println(line);
			}
		}catch(Exception e){
			// could not clear screen
		}
	}

	void usage(String type){
		switch(type){
			case"show":
				System.out.println(YELLOW + "Usage: show [PATH]" + RESET);
			break;
		}
	}

	void listPackages(){
		File parentDir = new File(docPath);
		String[]subDirs = parentDir.list();

		for(String d : subDirs){
			System.out.println(YELLOW + d + RESET);
		}
	}

	void show(){
		try{
			File selection = new File(docPath + "/" + cmdArgs[1]);

			if(selection.isDirectory()){
				String[]subDirs = selection.list();

				for(String d : subDirs){
					if(new File(selection + "/" + d).isDirectory()){
						System.out.print(RED + "DIRECTORY -- > ");
					}
					String[] df = new String[4];
					df = d.split("\\.");
					System.out.println(YELLOW + df[0] + RESET);
				}
			}else{
				selection = new File(docPath + "/" + cmdArgs[1] + ".html");
				//System.out.println(selection);
				readFile(selection);
			}
		}catch(ArrayIndexOutOfBoundsException aex){
			usage("show");
			error("args");
		}catch(IOException iex){
			error("file");
		}
	}

	void readFile(File file)throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = "";
 		String str = "";

		while((line = reader.readLine()) != null){
			str += line + "\n";
		}
		Document doc = Jsoup.parse(str);
		// Title
		String title = doc.title();
		System.out.println(GREEN + "\n" + title + "\n" + RESET);
		// Description
		System.out.println(YELLOW + "Description: \n" + RESET);

		for(Element e : doc.getElementsByClass("description")){
			System.out.println(PURPLE + e.text() + "\n\n" + RESET);
		}
		// Methods
		System.out.println(YELLOW + "Method Summary: \n" + RESET);
		ArrayList<Element>methodNames = new ArrayList<>();
		ArrayList<Element>methodReturns = new ArrayList<>();
		ArrayList<Element>methodDesc = new ArrayList<>();

		for(Element ee : doc.select("td.colFirst")){  // return type
			methodReturns.add(ee);
		}
		for(Element f : doc.select("td.colLast code")){ // name
			methodNames.add(f);
		}
		for(Element g : doc.select("td.colLast .block")){  // description
			methodDesc.add(g);
		}

		try{
			for(int i = 0; i < methodNames.size(); i++){
				System.out.print(GREEN + methodReturns.get(i).text() + " " + RESET);
				System.out.println(PURPLE + methodNames.get(i).text() + RESET);
				System.out.println(YELLOW + methodDesc.get(i).text() + RESET + "\n");
			}
		}catch(IndexOutOfBoundsException iex){
			System.out.println(RED + "end\n" + RESET);
		}
	}

	void exit(){
		System.exit(0);
	}

	void parseOption(){
		resetArgs();
		cmdArgs = option.split("\\s+");
	}

	void resetArgs(){
		for(int i = 0; i < cmdArgs.length; i++){
			cmdArgs[i] = null;
		}
	}

	void error(String type){
		System.out.println(RED);

		switch(type){
			case"cmd":
				System.out.println("command not found");
			break;

			case"args":
				System.out.println("invalid arguments");
			break;

			case"file":
				System.out.println("no such file or directory");
			break;
		}
		System.out.println(RESET);
	}

	public static void main(String[] args){
		new JavaManual();
	}
}
