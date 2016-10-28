package msa4_assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
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
		String csv = "tags.csv";
		String phototags ="photos_tags.csv";
		String Coocurrence ="coocurrencePhotoTags.csv";
		BufferedReader buff = null;
		String line ="";
		Set<String> set = new LinkedHashSet<String>();
		HashMap<String, HashMap<String, Double>> basemap = new HashMap<String,HashMap<String, Double>>();
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
					basemap.put(s, new HashMap<String, Double>());
				}
				
				for(String name : basemap.keySet()){
					for(String s : set){
						if(name.equals(s)){
							basemap.get(name).put(name, -1.0);
						}else{
							basemap.get(name).put(s, 0.0);
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
			System.out.println("Part 2");
			LinkedHashMap<String, Double> watermap = (LinkedHashMap<String, Double>) sortByValue(basemap.get("water"));
			LinkedHashMap<String, Double> peoplemap = (LinkedHashMap<String, Double>) sortByValue(basemap.get("people"));
			LinkedHashMap<String, Double> londonmap = (LinkedHashMap<String, Double>) sortByValue(basemap.get("london"));
			System.out.println("WATER: " + getMostFiveItem(watermap));
			System.out.println("People: " + getMostFiveItem(peoplemap));
			System.out.println("London: " +getMostFiveItem(londonmap));
			
			Set<String> uniquePhoto = new HashSet<String>();
			try {
				buff = new BufferedReader(new FileReader(phototags));
				while((line = buff.readLine()) != null){
					String [] item = line.split(",");
					uniquePhoto.add(item[0]);
					}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				System.out.println("Part 3");
				LinkedHashMap<String, Double> waterIdfMap = (LinkedHashMap<String, Double>) sortByValue(Compute(basemap.get("water"),uniquePhoto.size(), "water",csv));
				LinkedHashMap<String, Double> peopleIdfMap = (LinkedHashMap<String, Double>) sortByValue(Compute(basemap.get("people"),uniquePhoto.size(),"people",csv));
				LinkedHashMap<String, Double> londonIdfMap = (LinkedHashMap<String, Double>) sortByValue(Compute(basemap.get("london"),uniquePhoto.size(),"london",csv));
				System.out.println("Water: " +getMostFiveItem(waterIdfMap));
				System.out.println("People: " +getMostFiveItem(peopleIdfMap));
				System.out.println("London: " +getMostFiveItem(londonIdfMap));
				
				System.out.println(Math.log10(uniquePhoto.size()/NT(csv,"water")));
				System.out.println(uniquePhoto.size());
				System.out.println(NT(csv,"water"));
			}
			
	}
	
	public static LinkedHashMap<String, Double> getMostFiveItem(Map<String, Double> map){
		int i = 0;
		LinkedHashMap<String, Double> list = new LinkedHashMap<String, Double>();
		for(String s : map.keySet()){
			if(i < 5){
				list.put(s, map.get(s));
				i++;
			}else{
				break;
			}
		}
		return list;
	}
	
	public static Map<String, Double> Compute(HashMap<String, Double> hashMap, int photsSize, String name, String csv){
		Map<String, Double> resultmap = new HashMap<String, Double>();
		for(String key : hashMap.keySet()){
			if(hashMap.get(key) > 0){
				resultmap.put(key, (Math.log10(photsSize/NT(csv,key)))*hashMap.get(key));
			}
		}
		
		return resultmap;	
	}
	/*
	 * count the number of photo with tags
	 */
	@SuppressWarnings("resource")
	public static Double NT(String tags, String key){
		
		
		BufferedReader buff ;
		String line;
		try {
			buff = new BufferedReader(new FileReader(tags));
			while((line = buff.readLine()) != null){
				String [] item = line.split(",");
				if(item[0].equals(key)){
					return Double.parseDouble(item[1]);
				}
				}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}

	/*
	 * Sort HashMap by value to reverse order
	 */
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
}

