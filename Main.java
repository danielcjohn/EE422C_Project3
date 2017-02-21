/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Spring 2017
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	public static Map<String, ArrayList<String>> AdjacencyList;
	public static Set<String> explored = new HashSet<String>();
	public static Node parent = null;
	
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		
		
		ArrayList<String> input = parse(kb);
		while(!input.contains("/quit")){ //if /quit is one of two words entered, terminate program
			String start = input.get(0);
			String end = input.get(1);
			ArrayList<String> ladder = getWordLadderDFS(start, end);
			printLadder(ladder);
			input = parse(kb);
		}

	}
	
	public static void initialize() { 
		AdjacencyList = CreateAdjacencyList(makeDictionary()); //create adjacency list once for dictionary provided
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
	ArrayList<String> in = new ArrayList<String>();
	in.add(keyboard.next());
	if(in.contains("/quit")){ //if /quit is first word entered, terminate program
		return in;
	}
	in.add(keyboard.next());
	return in;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		start = start.toUpperCase();
    	end = end.toUpperCase();
		Node first = new Node(start, null);	//starting position
		Set<String> visited = new HashSet<String>();
		Node found = findDFS(first, end, visited);
		if(!(found==null)){
			ArrayList<String> dfsLadder = new ArrayList<String>();
			dfsLadder.add(end);
			found = found.parent;
			while(found != null){
				dfsLadder.add(found.word);
				found = found.parent;
			}
			return dfsLadder;
			}
		else{
			ArrayList<String> ladder = new ArrayList<String>();
			ladder.add(start);
			ladder.add(end);
			return ladder;
		}
	}
		
	public static Node findDFS(Node n, String end, Set<String> visited){
		if(n==null){
			return null;
		}
		visited.add(n.word);
		if(n.word.equals(end)){
			return n;
		}else{
			ArrayList<String> dfsNeighbors = AdjacencyList.get(n.word);
			for(String j : dfsNeighbors){
				if(!visited.contains(j)){
					Node current = new Node(j, n);
					Node found = findDFS(current, end, visited);
					if(!(found==null)){
						return found;
					}
					}
				}
			return null;
			}
		}
		
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	start = start.toUpperCase();
    	end = end.toUpperCase();
    	if(start.equals(end)){
    		//if start and end word are same, return list with both words and print "no ladder found"
    		ArrayList<String> ladder = new ArrayList<String>();
    		ladder.add(start);
    		ladder.add(end);
    		return ladder;
    	}
    	Set<String> visited = new HashSet<String>(); //keep track of visited words
		Queue<Node> qe = new LinkedList<Node>(); 
		Node first = new Node(start, null);
		qe.add(first); 								//add starting node to queue
		while(!qe.isEmpty()){
			Node current = qe.remove();
			if(current.word.equals(end)){ 			//if end word found, create word ladder and return;
				ArrayList<String> ladder = new ArrayList<String>();
				ladder.add(current.word);
				current = current.parent;
				while(current != null){
					ladder.add(current.word);
					current = current.parent;
				}
				return ladder;
			}
			ArrayList<String> neighbors = AdjacencyList.get(current.word);	
			for(String n : neighbors){										//for each neighbor of current head
				if(!visited.contains(n)){										//if neighbor has not been visited
					visited.add(n);													//mark neighbor as visited
					Node next = new Node(n, current);								//mark neighbors parent to be head
					qe.add(next);													//add neighbor to queue
				}
			}
		}
		
		//if no word ladder is found, return list with only start and end word
		ArrayList<String> ladder = new ArrayList<String>();
		ladder.add(start);
		ladder.add(end);
		return ladder;
	}
    
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	public static void printLadder(ArrayList<String> ladder) {
		if(ladder.size() == 2){ //if no ladder found, the ladder array will only contain the start and end word
			System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + " and " + ladder.get(1).toLowerCase() + ".");
		}else{
			System.out.println("a " + (ladder.size()-2) + "-rung word ladder exists between " + ladder.get(ladder.size()-1).toLowerCase() + " and " + ladder.get(0).toLowerCase() + ".");
			for(int i = ladder.size()-1; i>=0; i--){
				System.out.println(ladder.get(i).toLowerCase());
			}
		}
	
	}
	
	// TODO
	// Other private static methods here
	
	
	/*creates adjacency list for each word in given dictionary
	* Each key to the Map is a word from the dictionary and each
	* 	Map value is list of words that differ from key by one letter
	*/
	public static Map<String, ArrayList<String>> CreateAdjacencyList(Set<String> dict){
	Map<String, ArrayList<String>> AList = new HashMap<>();
	for(String n : dict){
		Iterator<String> it = dict.iterator();
		ArrayList<String> edges = new ArrayList<>();
		while(it.hasNext()){
			String word = it.next();
			if(word!=n){
				if(differByOne(word, n)){
					edges.add(word);
				}
			}
		}
		AList.put(n, edges);
		}
	return AList;
}
	
	/* returns true of the two strings differ by one letter
	 * used to create the adjacency list
	 */
	private static boolean differByOne(String s1, String s2) {
		if (s1.length() != s2.length())
			return false;

		int diff = 0;
		for (int i = 0; i < s1.length(); i++) {
			if (s1.charAt(i) != s2.charAt(i)){
				diff++;
			}
		}
		if(diff > 1){return false;}
		return true;
	}
}
