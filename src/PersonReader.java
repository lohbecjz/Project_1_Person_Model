import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import static java.nio.file.StandardOpenOption.CREATE;

/**
 * @author lohbecjz@mail.uc.edu
 */

public class PersonReader
{
    static Scanner console = new Scanner(System.in);
    static String name = "";
    static boolean confirm = false;
    static JFileChooser chooser = new JFileChooser();
    static File selectedFile;
    static String record = "";
    static ArrayList<String> lines = new ArrayList<>();
    static int line = 0;
    static final int FIELDS_LENGTH = 5;

    static String id, firstName, lastName, title, yob;


    public static void main(String[] args)
    {
        final String menu = "[O] Open - [V] View - [Q] Quit ";
        boolean done = false;
        String cmd = "";

        do
        {
            // display the menu options
            // get a menu choice
            cmd = SafeInput.getRegExString(console, menu, "[OoVvQq]");
            cmd = cmd.toUpperCase();

            // execute the choice
            switch(cmd)
            {
                case "O":
                    openFile();
                    break;
                case "V":
                    displayList();
                    break;
                case "Q":
                    quit();
                    break;
            }

        }
        while(!done);

    }

    // open a list file from disk
    // if there is a list present
    // prompt user to save
    // clear items before loading new list
    private static void openFile()
    {
        try
        {
            File workingDirectory = new File(System.getProperty("user.dir"));

            chooser.setCurrentDirectory(workingDirectory);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                selectedFile = chooser.getSelectedFile();
                Path file = selectedFile.toPath();

                name = chooser.getSelectedFile().getName();
                System.out.println("You chose to open this file: " + name);

                InputStream in = new BufferedInputStream(Files.newInputStream(file, CREATE));
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while(reader.ready())
                {
                    record = reader.readLine();
                    lines.add(record);
                    line++;
                    System.out.printf("\nLine %4d %-60s ", line, record);
                }

                reader.close();
                System.out.println("\n\nData File read!");
                System.out.println("");
            }
            else
            {
                System.out.println("Failed to choose a file to process");
                System.out.println("Run the program again!");
                System.exit(0);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found!!!");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    // view the list in the console
    private static void displayList()
    {
        System.out.printf("\n%-8s%-25s%-25s%-6s%6s","ID#", "Firstname", "LastName", "Title", "YOB");
        System.out.println("");
        System.out.println("======================================================================");
        String[] fields;
        for(String l:lines)
        {
            fields = l.split(","); // Split the record into the fields

            if (fields.length == FIELDS_LENGTH)
            {
                id = fields[0].trim();
                firstName = fields[1].trim();
                lastName = fields[2].trim();
                title = fields[3].trim();
                yob = fields[4].trim();
                System.out.printf("\n%-8s%-25s%-25s%-6s%6s", id, firstName, lastName, title, yob);
            }
            else
            {
                System.out.println("Found a record that may be corrupt");
                System.out.println(l);
            }
        }
        System.out.println("");
    }

    // quit the program
    // verify if they want to quit
    private static void quit()
    {
        // ask are you sure?
        confirm = SafeInput.getYNConfirm(console, "Are you sure you want to quit? Enter [Y] for Yes, [N] for No: ");
        System.out.println("");
        if (confirm)
        {
            System.exit(0);
        }
    }
}