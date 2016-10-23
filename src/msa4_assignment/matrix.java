package msa4_assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class matrix {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String csv = "D:/Downloads/music/csv/tags.csv";
		String phototags ="D:/Downloads/music/csv/photos_tags.csv";
		BufferedReader buff = null;
		String line ="";
		Set<String> set = new LinkedHashSet<String>();
		HashMap<String, HashMap<String, Integer>> basemap = new HashMap<String,HashMap<String, Integer>>();
		
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
					int id = Integer.parseInt(item[0]);
					
					if(buff.readLine() != null){
						String next = buff.readLine();
						String [] nextItem = next.split(",");
						int nextid = Integer.parseInt(nextItem[0]);
						if(id == nextid){
							basemap.get(item[1]).put(nextItem[1], basemap.get(item[1]).get(nextItem[1]).intValue() + 1);
							basemap.get(nextItem[1]).put(item[1], basemap.get(nextItem[1]).get(item[1]).intValue() + 1);
						}
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		for(String name : basemap.keySet()){
			String key = name.toString();
			//System.out.println(key + " " + basemap.get(name));
		}	
	}

}
