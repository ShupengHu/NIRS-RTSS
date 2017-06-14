package virtual_input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by hsp on 2017/6/13.
 */
public class Spectrum_Input {

    private static double[][] spectra;
    private static double[] wavelength;
    static Scanner s;
    private static int numberOfVariables=0;

    public static void readSpectraFromExcel(){

    }
    /**
     * read all absorbance as input from Files and store them into a two-dimensional array
     */
    public static void readSpectraFromFile(String filePath){
        File file=new File(filePath); //12(spectra)*230(variables)
        File[] absorbanceFiles=file.listFiles();

        //confirm the number of variables in each file
        try {
            if (absorbanceFiles != null) {
                s=new Scanner(absorbanceFiles[0]);
            }
            while(s.hasNextDouble()){
                s.nextDouble();
                numberOfVariables++;
            }
        } catch (FileNotFoundException e1) {
            System.out.println("File Not Found");
            e1.printStackTrace();
        }

        //read all absorbance as input and store them into a two-dimensional array
        if (absorbanceFiles != null) {
            spectra=new double[absorbanceFiles.length][numberOfVariables];
        for(int i=0;i<absorbanceFiles.length;i++){
            try {
                s = new Scanner(absorbanceFiles[i]);
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found");
                e.printStackTrace();
            }
            for(int j=0;j<numberOfVariables;j++) {
                    if(s.hasNextDouble()) {
                        spectra[i][j] = s.nextDouble();
                    }
            }
            }
        }
        s.close();
        //System.out.println(spectra[4][0]+"---"+spectra[4][1]);
    }

    /**
     * read all wavelengths as variables
     */
    public static void readWavelength(){
        File file=new File("./Wavelength.txt");
        wavelength=new double[numberOfVariables];
        try {
            s=new Scanner(file);
            int index=0;
            while(s.hasNextDouble()) {
                wavelength[index]=s.nextDouble();
                index++;
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }
    }
    /**
     * get spectra
     * @return a two-dimensional array storing absorbance as input
     */
    public static double[][] getSpectra(){
        return spectra;
    }

    /**
     * get wavelength
     * @return a double arary storing wavelength as variable
     */
    public static double[] getWavelength(){
        return wavelength;
    }

    /**
     * get number of variables
     * @return int
     */
    public static int getNumberOfVariables(){
        return numberOfVariables;
    }
}
