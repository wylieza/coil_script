import java.util.Scanner;

/*
Author: Justin Wylie

Description:
    A basic program to produce a PCB coil. Outputs an eagle compatible script.

*/

public class Coil{
    static double trace_trace = 0.3, trace_outline = 0.3, track_width = 0.3, pc_width = 54, pc_height = 64;

    public static String dimensions_toString(){
        String msg = "";
        msg += "Trace-Trace -> " + trace_trace + " mm\n";
        msg += "Trace-Trace -> " + trace_outline + " mm\n";
        msg += "Trace-Trace -> " + track_width + " mm\n";
        msg += "Trace-Trace -> " + pc_width + " mm\n";
        msg += "Trace-Trace -> " + pc_height + " mm\n";        

        return msg;
    }

    public static void configure_dimensions(String[] dim){
        boolean configured = false;
        if (dim.length == 5){
            //Attmept to parse dimensions from arguments
            try{
                trace_trace = Double.parseDouble(dim[0]);
                trace_outline = Double.parseDouble(dim[2]);
                track_width = Double.parseDouble(dim[3]);
                pc_width = Double.parseDouble(dim[4]);
                pc_height = Double.parseDouble(dim[5]);
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

    public static void generate_script(){
        //Generate coil here
    }

    public static void main(String[] args){
        if(args.length == 0 || (args.length == 1 && !(args[0].toLowerCase().contains("d")))){
            //d for default
            configure_dimensions(args);
        }else{
            System.out.println("Using default configuration");
        }



    }


}