package com.zeeveener.zport.cmd.warplist;

import java.util.List;

import com.zeeveener.zport.objects.Warp;

/**
 * Sorts objects using Insertion sort.
 * There is a tiny chance that there will be enough items to slow it down so no need for Merge Sort
 * Theoretically, It would take >10,000 warps to significantly slow down the algorithm
 * 
 * @author zee
 */
public class Sort {

	/**
	 * Sorts the input array into Descending Order by their Name
	 * @param in - Array of Warps
	 */
	public static void sortByNameDescending(List<Warp> in){
		for(int i = 1; i < in.size(); i++){
			for(int j = i; j < in.size(); j++){
				if(!earlierThan(in.get(j).getName(), in.get(j-1).getName())){
					Warp t = in.get(j);
					in.set(j, in.get(j-1));
					in.set(j-1, t);
				}else{
					break;
				}
			}
		}
	}
	
	/**
	 * Sorts the input array into Ascending Order by their Name
	 * @param in - Array of Warps
	 */
	public static void sortByNameAscending(List<Warp> in){
		for(int i = 1; i < in.size(); i++){
			for(int j = i; j < in.size(); j++){
				if(earlierThan(in.get(j).getName(), in.get(j-1).getName())){
					Warp t = in.get(j);
					in.set(j, in.get(j-1));
					in.set(j-1, t);
				}else{
					break;
				}
			}
		}
	}
	
	/**
	 * Compares two strings for alphabetical order ignoring case
	 * @param a - First String
	 * @param b - Second String
	 * @return True if First String is earlier than Second String. False otherwise.
	 * 			<br>Theoretically, shorter words will return true. (Ex: "true" < "truefalse")
	 */
	private static boolean earlierThan(String a, String b){
		byte[] x = a.toLowerCase().getBytes();
		byte[] y = b.toLowerCase().getBytes();
		int xi = 0, yi = 0;
		while(true){
			if(xi >= x.length || yi >= y.length){
				break;
			}
			if(x[xi] < y[yi]){
				return true;
			}else if(x[xi] == y[yi]){
				xi++;
				yi++;
			}else{
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Sorts the input array into Ascending Order by their Age
	 * @param in - Array of Warps
	 */
	public static void sortByAgeAscending(List<Warp> in){
		for(int i = 1; i < in.size(); i++){
			for(int j = i; j < in.size(); j++){
				if(in.get(j).getCreated() < in.get(j-1).getCreated()){
					Warp t = in.get(j);
					in.set(j, in.get(j-1));
					in.set(j-1, t);
				}else{
					break;
				}
			}
		}
	}
	
	/**
	 * Sorts the input array into Descending Order by their Age
	 * @param in - Array of Warps
	 */
	public static void sortByAgeDescending(List<Warp> in){
		for(int i = 1; i < in.size(); i++){
			for(int j = i; j < in.size(); j++){
				if(in.get(j).getCreated() > in.get(j-1).getCreated()){
					Warp t = in.get(j);
					in.set(j, in.get(j-1));
					in.set(j-1, t);
				}else{
					break;
				}
			}
		}
	}
	
	/**
	 * Sorts the input array into Ascending Order by their number of Uses
	 * @param in - Array of Warps
	 */
	public static void sortByUsesAscending(List<Warp> in){
		for(int i = 1; i < in.size(); i++){
			for(int j = i; j < in.size(); j++){
				if(in.get(j).getUses() < in.get(j-1).getUses()){
					Warp t = in.get(j);
					in.set(j, in.get(j-1));
					in.set(j-1, t);
				}else{
					break;
				}
			}
		}
	}
	
	/**
	 * Sorts the input array into Descending Order by their number of Uses
	 * @param in - Array of Warps
	 */
	public static void sortByUsesDescending(List<Warp> in){
		for(int i = 1; i < in.size(); i++){
			for(int j = i; j < in.size(); j++){
				if(in.get(j).getUses() > in.get(j-1).getUses()){
					Warp t = in.get(j);
					in.set(j, in.get(j-1));
					in.set(j-1, t);
				}else{
					break;
				}
			}
		}
	}
}
