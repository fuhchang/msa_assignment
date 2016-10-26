package msa4_assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import java.util.Map;

import java.util.Set;
import java.util.stream.Collectors;



public class matrix {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * 
		 * Part 1 matrix
		 */
		//String csv = "D:/Downloads/music/csv/tags.csv";
		//String phototags ="D:/Downloads/music/csv/photos_tags.csv";
		//String Coocurrence  = "D:/Downloads/music/csv/coocurrencePhotoTags.csv"
		String csv = "/Users/fuhchangloi/git/msa_assignment/tags.csv";
		String phototags ="/Users/fuhchangloi/git/msa_assignment/photos_tags.csv";
		String Coocurrence ="/Users/fuhchangloi/git/msa_assignment/coocurrencePhotoTags.csv";
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
				FileWriter write = new FileWriter(Coocurrence);
				
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
			
			/*
			 * 
			 * part 2
			 */
			Map<String, Integer> watermap = sortByValue(basemap.get("water"));
			Map<String, Integer> peoplemap = sortByValue(basemap.get("people"));
			Map<String, Integer> londonmap = sortByValue(basemap.get("london"));
			ArrayList<String> waterList = new ArrayList<String>();
			ArrayList<String> peopleList = new ArrayList<String>();
			ArrayList<String> londonList = new ArrayList<String>();
			waterList.addAll(getLastFiveItem(watermap));
			peopleList.addAll(getLastFiveItem(peoplemap));
			londonList.addAll(getLastFiveItem(londonmap));
			System.out.println(waterList);
			System.out.println(peopleList);
			System.out.println(londonList);
			int IDF;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}
	 



	
	public static ArrayList<String> getLastFiveItem(Map<String, Integer> map){
		int i = 0;
		ArrayList<String> list = new ArrayList<String>();
		for(String s : map.keySet()){
			if(i < 5){
				list.add(s);
				i++;
			}else{
				break;
			}
		}
		return list;
	}
	
}
