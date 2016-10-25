package msa4_assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;



public class matrix {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String csv = "D:/Downloads/music/csv/tags.csv";
		String phototags ="D:/Downloads/music/csv/photos_tags.csv";
		//String csv = "/Volumes/FC/SIT-UOG/semester 1 - yr 2/coursework-image-collection/csv/tags.csv";
		//String phototags = "/Volumes/FC/SIT-UOG/semester 1 - yr 2/coursework-image-collection/csv/photos_tags.csv";
		BufferedReader buff = null;
		String line ="";
		Set<String> set = new LinkedHashSet<String>();
		HashMap<String, HashMap<String, Integer>> basemap = new HashMap<String,HashMap<String, Integer>>();
		ArrayList<Integer> keyList = new ArrayList<Integer>();
		ArrayList<String> valueList = new ArrayList<String>();
			try {
				buff = new BufferedReader(new FileReader(csv));
				while((line = buff.readLine()) != null){
					String [] tags = line.split(",");
					set.add(tags[0]);
					}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
				for(String s : set){
					basemap.put(s, new HashMap<String, Integer>());
				}
				
				for(String name : basemap.keySet()){
					for(String s : set){
						if(name.equals(s)){
							basemap.get(name).put(name, -1);
						}else{
							basemap.get(name).put(s, 0);
						}
					}
				}
			}
			try {
				buff = new BufferedReader(new FileReader(phototags));
				while((line = buff.readLine()) != null){
					String [] item = line.split(",");
					keyList.add(Integer.parseInt(item[0]));
					valueList.add(item[1]);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0; i<keyList.size(); i++){
				
				int j=i+1;
				while(j<keyList.size()){
					if(keyList.get(i).toString().equals(keyList.get(j).toString())){
						basemap.get(valueList.get(i)).put(valueList.get(j), basemap.get(valueList.get(i)).get(valueList.get(j))+1);
						basemap.get(valueList.get(j)).put(valueList.get(i), basemap.get(valueList.get(j)).get(valueList.get(i))+1);
						j++;
					}else{
						break;
					}
				}
			}
			
			try {
				String header = " ,";
				for(String key : basemap.keySet()){
					header += key + ",";
				}
				header.substring(0, header.length()-1);
				FileWriter write = new FileWriter("D:/Downloads/music/csv/coocurrencePhotoTags.csv");
				
				write.append(header);
				
				for(String key : basemap.keySet()){
					write.append("\n");
					write.append(key);
					write.append(",");
					for(String tag : basemap.get(key).keySet()){
						write.append(""+basemap.get(key).get(tag));
						write.append(",");
					}
					
				}
				write.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	}
	
}
