package com.example.flightsofangels;
import java.util.*;
public class ParsingQnA {
//	public static void main(String args[])
//	{
//		String test="Poll title~ question1`a`b`c`d~ q2`a`b`c1`d" ;
//		System.out.println(parseResponse(test));
//	}
	public static String[][] parseResponse(String sent)
	{
		String regex="~";
		String[] questions = sent.split("~");
		
		String[][] votes = new String[questions.length][];
		
		for (int j=0; j<questions.length; j++)
		{
			votes[j] = questions[j].split("`");
		}
		
		String pollTitle = votes[0][0];
		
		
		
//		StringTokenizer st=new StringTokenizer(sent, regex);
//		
//		int len=st.countTokens();
		//return regex;
		return votes;
		
		
	}

}
