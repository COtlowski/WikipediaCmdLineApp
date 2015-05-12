import java.io.*;
import java.net.*;
import javax.swing.text.html.HTMLEditorKit;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

/**
 * Created by Chris on 5/9/2015.
 * The main class of the command-line application
 */
public class WCLA_Main {
    public static void main(String[] args){
        System.out.println("Welcome the Wikipedia Command Line app.\r\n");

        /** Input string for program loop. */
        String input = new String();
        /** HTML document to parse through. */
        Document doc;

        /** Scanner to read in user input. */
        Scanner scan = new Scanner(System.in);

        // If we have command line arguments
        if(args.length > 0){
            // Concatenate the arguments into a query
            for(int i = 0; i < args.length; i++)
                input += (args[i] + " ");
        } else {
            // Prompt the user for the search topic
            System.out.println("Please enter a topic or type in exit.");
            input = scan.nextLine();
        }


        // While the input is not equal to the word exit.
        while(!input.equalsIgnoreCase("exit")) {

            System.out.println("Searching for " + input);

            try {
                // Replace the whitespace in the input with underscores.
                String formattedInput = input.replace(" ", "_");

                // Get the HTML document from the wikipedia page specified by the input.
                doc = Jsoup.connect("http://en.wikipedia.org/wiki/" + formattedInput).get();

                // Get the element id'd as mw-content-text, which holds the majority of the content
                // in a Wikipedia page.
                Element contentText = doc.getElementById("mw-content-text");
                // Get the child elements of the content text, which could be paragraphs, pictures, etc.
                Elements contentTextChildElements = contentText.children();

                // Print the first paragraph element that is a direct child of the content text
                printFirstByTag(contentTextChildElements, Tag.valueOf("p"));
            } catch (Exception e) {
                // Print that we haven't found the article
                System.out.println("Not Found");
            }

            // Scan for the next line
            System.out.println("\r\nSearch completed. Enter another query or type exit.");
            input = scan.nextLine();
        }
    }

    /**
     * Prints the text of the first element of a particular tag
     * Example: printFirstByTag(elements, Tag.valueOf("div"))
     *          The program would output the text of the first occurance of div
     * @param pageElements      The page elements to search throught
     * @param tag               The HTML tag whose first element in the is to be printed
     */
    public static void printFirstByTag(Elements pageElements, Tag tag){
        /** Counter through the elements */
        int i = 0;
        /** Boolean flag to notify loop when an element of tag has been found */
        boolean foundFirst = false;

        // While we haven't found the first element of tag and we haven't checked all elements
        while(!foundFirst && i < pageElements.size()){
            // If the tag of the current element is equal to the specified tag
            if(pageElements.get(i).tag().equals(tag)){
                // Print out the text of the element and flag the while loop
                System.out.println(pageElements.get(i).text());
                foundFirst = true;
            }
            // Increment the counter
            i++;
        }

        // Just in case an article may not have an entry paragraph
        if(!foundFirst){
            System.out.println("Article is empty.");
        }
    }
}
