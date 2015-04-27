import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException {
        
		//locate csv file in bin
		//get the name of the file 
		String csvFile ;
		csvFile = args[0];

		// = "bin/GasTable.csv";
		BufferedReader br = null;
		String line = "";
		String splitBy = ",";
		
		//create output file
		PrintWriter out = new PrintWriter("output.txt");
		
		//will be from parameters
		double min_sup = Double.parseDouble(args[1]);//= 0.3;
		double min_conf = Double.parseDouble(args[2]);//= 0.90;
		
		DecimalFormat df = new DecimalFormat("#0");
		
		try {
			
			br = new BufferedReader(new FileReader(csvFile));
			Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
			Map<String, Integer> tempmap = new HashMap<String, Integer>();
			Map<List<String>, Integer> tempmap2 = new HashMap<List<String>, Integer>();
			ArrayList<ArrayList<String>> templist = new ArrayList<ArrayList<String>>();
			ArrayList<List<String>> firstpass = new ArrayList<List<String>>();
			Map<List<String>, Double> supResults = new HashMap<List<String>, Double>();
			Map<List<String>, Double> confdataset = new HashMap<List<String>, Double>();
			Map<List<String>, Double> conffirstResults = new HashMap<List<String>, Double>();
			Map<List<String>, Double> confResults = new HashMap<List<String>, Double>();
			Map<ArrayList<ArrayList<String>>, Double> hehetry = new HashMap<ArrayList<ArrayList<String>>, Double>();

			int numRows = 0;
            
			while ((line = br.readLine()) != null){
				
				//keep track of total number of row (to help debug & to calculate support)
				numRows = numRows + 1;
				
				//separate by comma
				String[] naturalGas = line.split(splitBy);
				
				//store into a map to compare variables within a row
				List<String> valueSet = new ArrayList<String>();
				for(int i=0; i<naturalGas.length; i++){
					valueSet.add(naturalGas[i]);
				}
				
				map.put(numRows, valueSet);
			}
			
			//-----This is to keep count of how often variables are used, to calculate minimum support
			//loop every row
			for (Map.Entry<Integer, List<String>> entry : map.entrySet()){
				List<String> valueSet = entry.getValue();
				//loop every column
				for(int i=0; i<valueSet.size(); i++){
					String x = valueSet.get(i);
					Object check = tempmap.get(x);
					//don't consider the "unknown" answers
					if(!x.equals("Unknown")){
						//if it hasn't been accounted for, add to list
						if(check == null){
							tempmap.put(x, 1);
						}else{
							//if it has, increase count by 1
							int count = tempmap.get(x);
							count=count+1;
							tempmap.put(x, count);
						}
					}
				}
			}
			
			//minimum support, first pass
			double support;
			for (Map.Entry<String, Integer> entry : tempmap.entrySet()) {
				int value = entry.getValue();
				support = ((double)value)/((double)numRows);
				if(support>=min_sup){
					//keep track of variables that meet first pass of min support
					List<String> result = new ArrayList<String>();
					result.add(entry.getKey());
					firstpass.add(result);
					supResults.put(result, support);
				}
			}
			
			//----Final/reiterative pass
			//while there are still frequent item sets to be tested...
			while(firstpass.size() != 0){
				for(int i=0; i<firstpass.size(); i++){
					for(int j=i+1; j<firstpass.size(); j++){
						List<String> listi = firstpass.get(i);
						List<String> listj = firstpass.get(j);
						for(int k=0; k<listj.size(); k++){
							boolean check = listi.contains(listj.get(k));
							if(check == false){
								ArrayList<String> group = new ArrayList<String>();
								for(int l=0; l<listi.size(); l++){
									group.add(listi.get(l));
								}
								group.add(listj.get(k));
								boolean contains = templist.contains(group);
								if(contains == false){
									templist.add(group);
								}
							}
						}
					}
				}
				
				//remove duplicates in templist
				for(int i=0; i<templist.size(); i++){
					ArrayList<String> valuesi = templist.get(i);
					for(int j=i+1; j<templist.size(); j++){
						ArrayList<String> valuesj = templist.get(j);
                    outerloop:
						{for(int k=0; k<valuesi.size(); k++){
							boolean repeat = valuesj.contains(valuesi.get(k));
							if(repeat == false && valuesi.size()>1){
								break outerloop;
							}
						}
                            templist.remove(valuesj);
                            j=j-1;}
					}
				}
				
				firstpass.clear();
				
				//loop through groups of variables to check for existence in rows
				for (int i=0; i<templist.size(); i++){
					List<String> list = templist.get(i);
					//loop rows
					for (Map.Entry<Integer, List<String>> entry : map.entrySet()){
						boolean good = true;
						List<String> valueSet = entry.getValue();
                    listloop:
                        for(int j=0; j<list.size(); j++){
                            boolean check = valueSet.contains(list.get(j));
                            //if one of the items does not exist in the row, the group doesn't exist in the row
                            if(check == false){
                                good = false;
                                break listloop;
                            }
                        }
						//if all items are in the row, count the row as an occurrence of the item set
						if(good==true){
							Object check = tempmap2.get(list);
							if(check == null){
								tempmap2.put(list, 1);
							}else{
								int count = tempmap2.get(list);
								count=count+1;
								tempmap2.put(list, count);
							}
						}
					}
				}
                
				for (Map.Entry<List<String>, Integer> entry : tempmap2.entrySet()) {
					List<String> key = entry.getKey();
					int value = entry.getValue();
					support = ((double)value)/((double)numRows);
					if(support>=min_sup){
						//store item sets that meet minimum support and need to be tested again
						firstpass.add(key); //this value will determine if while loop is repeated
						supResults.put(key, support);
					}
				}
				//clear lists to prepare for next pass of while loop
				tempmap2.clear();
				templist.clear();
			}
			
			//sort values in supResults by support
			supResults = MapUtil.sortByValue(supResults);
			
			//print all item sets that meet min_sup
			out.println("==Frequent itemsets (min_sup="+df.format(min_sup*(100))+"%)");
			for (Map.Entry<List<String>, Double> entry : supResults.entrySet()) {
				List<String> keys = entry.getKey();
				out.print("[");
				for(int j=0; j<keys.size(); j++){
					if(j == keys.size()-1){
						out.print(keys.get(j) + "], ");
					}else{
						out.print(keys.get(j) + ", ");
					}
				}
				out.println(df.format(entry.getValue()*100)+"%");
			}
			out.println("==High-confidence association rules (min_conf="+df.format(min_conf*(100))+"%)");
			for (Map.Entry<List<String>, Double> entry : supResults.entrySet()) {
				//print all potential itemsets that may meet the requirement 
				List<String> keys = entry.getKey();
			    
			    	if (keys.size() >1){
			    		
			    		confdataset.put(entry.getKey(),entry.getValue());
             
			    	}
		

			}

			for (Map.Entry<List<String>, Double> entryconf : confdataset.entrySet()) {
				for (Map.Entry<List<String>, Double> entrysup : supResults.entrySet()){
					//find all the confdataset that contain any of the dataset of supresult
					boolean good = true;
					List<String> confvalue = entryconf.getKey();
					List<String> supvalue = entrysup.getKey();
					List<String> confoutvalue = entryconf.getKey();
					listloop:
					for (int j = 0; j < supvalue.size(); j++){
						boolean check = confvalue.contains(supvalue.get(j));
						
						if (check == false){
							good = false;
							break listloop;
						}
					}
					//can not be itself 
					if (good == true && supvalue.size() != confvalue.size()){


						//System.out.println("hehe, can calculate!");
						Double confidence = 0.0;
						confidence = entryconf.getValue()/entrysup.getValue();
						if (confidence >= min_conf){
						ArrayList<String> temconfsup = new ArrayList<String>();
						ArrayList<String> temconfconf = new ArrayList<String>();
						ArrayList<String> temsuppercent = new ArrayList<String>();
						ArrayList<ArrayList<String>> gaga = new ArrayList<ArrayList<String>>();
						//List<ArrayList<String>> tempconf = new List<ArrayList<String>>();
						for(int l=0; l<supvalue.size(); l++){
							temconfsup.add(supvalue.get(l));
						}
						for(int l=0; l<confvalue.size(); l++){
							boolean goodtwo = true;
							for(int i = 0; i < supvalue.size(); i++){
								if (supvalue.get(i) == confvalue.get(l)) goodtwo = false;
								}
								if (goodtwo) temconfconf.add(confvalue.get(l));
						
						}

						for(int l=0; l<1; l++){
							temsuppercent.add(Double.toString(entrysup.getValue()));
						}



						gaga.add(temconfsup);
						gaga.add(temconfconf);
						gaga.add(temsuppercent);

						

						confResults.put(confvalue,confidence);
						conffirstResults.put(supvalue,confidence);
						hehetry.put(gaga,confidence);
					}
					}
				}
			}
			confResults = MapUtil.sortByValue(confResults);
			conffirstResults = MapUtil.sortByValue(conffirstResults);
			hehetry = MapUtil.sortByValue(hehetry);
			//try to see what inside the confresults
			for (Map.Entry<List<String>, Double> entry : confResults.entrySet()) {
				//print all potential itemsets that may meet the requirement 
				List<String> keys = entry.getKey();
			    	
			}
			for (Map.Entry<List<String>, Double> entry : conffirstResults.entrySet()) {
				//print all potential itemsets that may meet the requirement 
				List<String> keys = entry.getKey();
			    	
			}
			for (Map.Entry<ArrayList<ArrayList<String>>, Double> entry : hehetry.entrySet()) {
				ArrayList<ArrayList<String>> keys = entry.getKey();
				Double confidence = entry.getValue();
				int count = 0;
				for (ArrayList<String> sup : keys){
					

					//System.out.print("[");
					if (count%3 != 2){
					for (int i = 0; i< sup.size(); i++){
						if(i == sup.size()-1 && i !=0){
						out.print(sup.get(i)+"]");
					}else{
						if(i == 0 && i == sup.size()-1) out.print("["+ sup.get(i)+"]");
						else out.print("["+ sup.get(i)+",");
					}

					}
				}
					if (count%3 == 0){

					out.print(" => ");
				}
				    if (count%3 ==2){
				    	Double valuesup = Double.parseDouble(sup.get(sup.size()-1));
				    	out.print("(Conf: "+df.format(confidence*100)+"%" + ", Supp:" + df.format(valuesup*100)+"%)");

				    }
				count ++;


				}out.println();

			}
   
			//close out file
			out.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
//class to sort the results by support
class MapUtil
{
    public static <K, V extends Comparable<? super V>> Map<K, V>
    sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        //sort
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
                         {
                             public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
                             {
                                 return (o1.getValue()).compareTo( o2.getValue() );
                             }
                         });
        //now put the list in descending order
        Collections.reverse(list);
        
        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
}