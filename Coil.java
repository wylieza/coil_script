import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

/*
Author: Justin Wylie

Description:
    A basic program to produce a script which in turn produces a PCB based coil. Outputs an eagle compatible script.

*/

public class Coil{
    static double trace_clearnace = 0.3, track_width = 0.3, width = 54, height = 64, inner_width = 10;

    public static String dimensions_toString(){
        String msg = "";
        msg += "Trace-Trace -> " + trace_clearnace + " mm\n";
        msg += "Trace-Trace -> " + track_width + " mm\n";
        msg += "Trace-Trace -> " + width + " mm\n";
        msg += "Trace-Trace -> " + height + " mm\n";        

        return msg;
    }

    public static void configure_dimensions(String[] dim){
        boolean configured = false;
        if (dim.length == 5){
            //Attmept to parse dimensions from arguments
            try{
                trace_clearnace = Double.parseDouble(dim[0]);
                track_width = Double.parseDouble(dim[3]);
                width = Double.parseDouble(dim[4]);
                height = Double.parseDouble(dim[5]);
                configured = true;

            }catch (NumberFormatException e){
                System.out.println("Error parsing arguments!");
            }
        }

        if(!configured){
            System.out.println("Current Dimensions:\n" + dimensions_toString());
            System.out.println("Would you like to use default dimensions? [Y/n]");

            Scanner user_in = new Scanner(System.in);
            String input = user_in.nextLine();
            user_in.close();

            if(input.isEmpty() || input.toLowerCase().contains("y")){
                configured = true;
                System.out.println("Using default configuration...");
            }else if(input.toLowerCase().contains("n")){
                //Begin custom configuration
                System.out.println("Custom dimensions not implemented yet, sorry :(");
                System.exit(0);

            }else{
                System.out.println("Invalid option selected, terminating...");
                System.exit(0);
            }
        }

    }

    public static void init_corners(double[][] corners){
        //Bottom Left
        corners[0][0] = -(trace_clearnace + track_width*0.5);
        corners[0][1] = track_width*0.5;

        //Bottom Right
        corners[1][0] = width - track_width*0.5;
        corners[1][1] = track_width*0.5;

        //Top Right
        corners[2][0] = width - track_width*0.5;
        corners[2][1] = height - track_width*0.5;

        //Top Left
        corners[3][0] = track_width*0.5;
        corners[3][1] = height - track_width*0.5;

    }

    public static double inner_width(double[][] corners){
        double length_a = corners[1][0] - corners[0][0];
        double length_b = corners[3][1] - corners[0][1];

        return length_a > length_b ? length_b:length_a; //Return shortest length
    }

    public static void update_corners(double[][] corners){

        //Bottom Left
        corners[0][0] = corners[4][0];
        corners[0][1] = corners[4][1];

        //Next Bottom Left
        corners[4][0] += trace_clearnace + track_width*0.5;
        corners[4][1] += trace_clearnace + track_width*0.5;

        //Bottom Right
        corners[1][0] -= trace_clearnace + track_width*0.5;
        corners[1][1] += trace_clearnace + track_width*0.5;

        //Top Right
        corners[2][0] -= trace_clearnace + track_width*0.5;
        corners[2][1] -= trace_clearnace + track_width*0.5;

        //Top Left
        corners[3][0] += trace_clearnace + track_width*0.5;
        corners[3][1] -= trace_clearnace + track_width*0.5;

    }

    public static void generate_script(){
        //Generate coil here
        double[][] corners = new double[5][2];

        try{
            init_corners(corners);
            FileWriter outFile = new FileWriter("script.txt");

            outFile.write("GRID MM;\n");
            outFile.write("LAYER 1;\n");

            while (inner_width(corners) > inner_width){
                for (int i = 0; i < 4; i++){
                    outFile.write("wire " + track_width + " (" + corners[i][0] + " " + corners[i][1] + ") (" + corners[i+1][0] + " " + corners[i+1][1] + ")\n");
                }

                update_corners(corners);
            }
            
            outFile.close();

        }catch (IOException e){
            System.out.println("Error while writing to script file");
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        if(args.length == 0 || (args.length == 1 && !(args[0].toLowerCase().contains("d")))){
            //d for default
            configure_dimensions(args);
        }else{
            System.out.println("Using default configuration");
        }

        generate_script();

    }


}