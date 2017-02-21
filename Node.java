/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Jared Cormier
 * jcc4969
 * 16238
 * Daniel John
 * dcj597
 * 16238
 * Slip days used: 0
 * Git URL: https://github.com/danielcjohn/EE422C_Project3
 * Spring 2017
 */
package assignment3;

public class Node {
	String word = null;
	Node parent = null;
	
	public Node(String w, Node p){
		this.word = w;
		this.parent = p;
	}
}
