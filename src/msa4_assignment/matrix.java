package msa4_assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.HashSet;
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
		String csv = "tags.csv";
		String phototags ="photos_tags.csv";
		String photos = "photos.csv";
		String Coocurrence ="coocurrencePhotoTags.csv";
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
				Map<String, Integer> waterIdfMap = sortByValue(Compute(basemap.get("water"),uniquePhoto.size(), "water",phototags));
				Map<String, Integer> peopleIdfMap = sortByValue(Compute(basemap.get("people"),uniquePhoto.size(),"people",phototags));
				Map<String, Integer> londonIdfMap = sortByValue(Compute(basemap.get("london"),uniquePhoto.size(),"london",phototags));
				System.out.println("WATER: " +getMostFiveItem(waterIdfMap));
				System.out.println("People: " +getMostFiveItem(peopleIdfMap));
				System.out.println("London: " +getMostFiveItem(londonIdfMap));
			}
			
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
	 
	public static Map<String, Integer> getMostFiveItem(Map<String, Integer> map){
		int i = 0;
		Map<String, Integer> list = new HashMap<String, Integer>();
		for(String s : map.keySet()){
			System.out.println(s);
			if(i < 5){
				list.put(s, map.get(s));
				i++;
			}else{
				break;
			}
		}
		return list;
	}
	
	public static Map<String, Integer> Compute(Map<String, Integer> map, int photsSize, String name, String csv){
		Map<String, Integer> resultmap = new HashMap<String, Integer>();
		for(String key : map.keySet()){
			if(map.get(key) > 0){
				resultmap.put(key, ((photsSize/NT(csv,key)))*map.get(key));
			}
		}
		
		return resultmap;	
	}
	
	public static Integer NT(String phototags, String key){
		Set<String> uniquePhoto = new HashSet<String>();
		
		BufferedReader buff ;
		String line;
		try {
			buff = new BufferedReader(new FileReader(phototags));
			while((line = buff.readLine()) != null){
				String [] item = line.split(",");
				if(item[1].equals(key)){
					uniquePhoto.add(item[0]);
				}
				}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  uniquePhoto.size();
	}
	public static Integer computeIDF(String key, HashMap<String, HashMap<String, Integer>> map){
		int count =0;
		for(String item : map.keySet()){
			for(String innerItem: map.get(item).keySet()){
				if(innerItem.equals(key)){
					count++;
				}else if(item.equals(key) && !innerItem.equals(key)){
					count++;
				}
			}
		}
		
		return count;
	}

	
	public static Integer CountApprear(String key, String photos_tags){
		int count =0;
		BufferedReader buff;
		String line;
		try {
			buff = new BufferedReader(new FileReader(photos_tags));
			while((line = buff.readLine()) != null){
				String [] tags = line.split(",");
				if(tags[1].equals(key)){
						count++;
					}
				}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}
}
