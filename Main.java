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
		if(!input.contains("/quit")){
			String start = input.get(0);
			String end = input.get(1);
			ArrayList<String> ladder = getWordLadderDFS(start, end);
			printLadder(ladder);
		}
		
		/*for testing in linux only
		  *ArrayList<String> ladder = getWordLadderBFS("stone", "money");
		  *printLadder(ladder);
		  *ladder = getWordLadderBFS("aldol", "drawl");
		  *printLadder(ladder);
		  *ladder = getWordLadderBFS("smart", "money");
		  *printLadder(ladder);
		  *ladder = getWordLadderBFS("hello", "cells");
		  *printLadder(ladder);
		 */
		
		// TODO methods to read in words, output ladder
	}
	
	public static void initialize() { //create adjacency list once for dictionary provided
		AdjacencyList = CreateAdjacencyList(makeDictionary());

	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
	ArrayList<String> in = new ArrayList<String>();
	in.add(keyboard.next());
	if(in.contains("/quit")){
		return in;
	}
	in.add(keyboard.next());
	return in;

	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		
		Node begin = new Node(start, parent);	//starting position
		explored.add(begin.word);	//adding to explored list set
		
		if(begin.word.equals(end)){	//found end value
			ArrayList<String> dfsLadder = new ArrayList<String>();
			dfsLadder.add(end);
			begin = begin.parent;
			while(begin != null){
				dfsLadder.add(begin.word);
				begin = begin.parent;
			}
			return dfsLadder;
			
		}else{
			ArrayList<String> dfsNeighbors = AdjacencyList.get(begin.word);
			for(String j : dfsNeighbors){
				if(!explored.contains(j)){
					parent = begin;
					return getWordLadderDFS(j, end);
					
				}
				
			}
			ArrayList<String> dfsLadder = new ArrayList<String>();
			dfsLadder.add(start);
			dfsLadder.add(end);
			return dfsLadder;
			
		}
		
		
		
		// TODO some code
		/*Set<String> dict = makeDictionary();
		Set<String> explored = new HashSet<String>();
		Queue<Node> dfsq = new LinkedList<Node>();
		Node begin = new Node(start, null);
		dfsq.add(begin);
		
		while(!dfsq.isEmpty()){
			Node currentdfs = dfsq.remove();
			if(currentdfs.word.equals(end)){
				ArrayList<String> dfsladder = new ArrayList<String>();
				dfsladder.add(currentdfs.word);
				currentdfs = currentdfs.parent;
				while(currentdfs != null){
					dfsladder.add(currentdfs.word);
					currentdfs = currentdfs.parent;
				}
				return dfsladder;
			}
			//explored.add??
			ArrayList<String> dfsNeighbors = AdjacencyList.get(currentdfs.word);
			for(String s : dfsNeighbors){
				if(!explored.contains(s)){
					
					getWordLadderDFS(s, end); // is this right?
					explored.add(s); //check
					
				}
				
			}
		}
		
		
		// TODO more code
		ArrayList<String> dfsladder = new ArrayList<String>();
		dfsladder.add(start);
		dfsladder.add(end);
		return dfsladder;*/
		
		//return null; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	Set<String> visited = new HashSet<String>();
		Queue<Node> qe = new LinkedList<Node>();
		Node first = new Node(start, null);
		qe.add(first);
		while(!qe.isEmpty()){
			Node current = qe.remove();
			if(current.word.equals(end)){
				//create ArrayList and return;
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
			for(String n : neighbors){
				if(!visited.contains(n)){
					visited.add(n);
					Node next = new Node(n, current);
					qe.add(next);
				}
			}
		}
		
		ArrayList<String> ladder = new ArrayList<String>();
		ladder.add(start);
		ladder.add(end);
		return ladder;
	}
    
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			//infile = new Scanner (new File("five_letter_words.txt"));
			infile = new Scanner (new File("five_letter_words.txt"));

		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next()/*.toUpperCase()*/); //fix to work for upper case 
		}
		return words;
	}
	
	public static void printLadder(ArrayList<String> ladder) {
		if(ladder.size() == 2){ //if no ladder found, the ladder array will only contain the start and end word
			System.out.println("no word ladder can be found between " + ladder.get(0) + " and " + ladder.get(1) + ".");
		}else{
			System.out.println("a " + (ladder.size()-2) + "-rung word ladder exists between " + ladder.get(ladder.size()-1) + " and " + ladder.get(0) + ".");
			for(int i = ladder.size()-1; i>=0; i--){
				System.out.println(ladder.get(i));
			}
		}
	
	}
	
	// TODO
	// Other private static methods here
	
	
	/*creates adjacency list for each word in given dictionary
	* Each key to the Map is a word from the dictionary and each
	* 	Map value is list of words that differ by one letter
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