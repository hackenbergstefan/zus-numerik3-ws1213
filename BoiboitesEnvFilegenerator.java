import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoiboitesEnvFilegenerator {
	
	
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("Error: You have to give exactly one argument, the main tex file, from where the envs should be exported!");
			return;
		}
		
		File infile = new File(args[0]);
		if(!infile.exists()){
			System.out.println("Error: File "+infile.getAbsolutePath()+" does not exist!");
			return;
		}
		
		//Read file content
		
		String content = "";
		try{
			BufferedReader r = new BufferedReader(new FileReader(infile));
			String l = "";
			while ((l = r.readLine()) != null)
				content += l+"\n";
			r.close();			
		}catch(Exception ex){
			System.out.println("Error: "+ex.getMessage());
			return;
		}
		
		//search for environments
		Pattern p = Pattern.compile("\\\\newboxedtheorem[^\\{]*\\{([^\\{]+)\\}");
		Matcher m = p.matcher(content);
		ArrayList<String> envList = new ArrayList<String>();
		while(m.find()){
//			System.out.println(m.group());
//			for(int i=0;i<=m.groupCount();i++)
//				System.out.println(m.group(i));
//			System.out.println();
			envList.add(m.group(1));
		}
		
		//search occurrences for each environment and print them to file
		for(String envName: envList){
			File outfile = new File(infile.getParentFile()+"\\inp_"+envName+".tex");
			FileWriter outWriter = null;
			try{
				if(outfile.exists()) outfile.delete();
				outWriter = new FileWriter(outfile);
			}catch(IOException ex){
				ex.printStackTrace();
			}
			p = Pattern.compile("(?s)\\\\begin\\{"+envName+"\\}.*?\\\\end\\{"+envName+"\\}");
			m = p.matcher(content);
			while(m.find()){
				try{
					outWriter.append(m.group()+"\n\n");
				}catch(IOException ex){ex.printStackTrace();}
			}
			try{
				outWriter.close();
			}catch(IOException ex){ex.printStackTrace();}
		}
	}

}
