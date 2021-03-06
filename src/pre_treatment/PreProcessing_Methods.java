package pre_treatment;


import Autoscaling_MATLAB.Autoscaling;
import MSC_MATLAB.MSC;
import MWS_MATLAB.MWS;
import Mean_Centering_Matlab.Mean_Centering;
import Normalization_MATLAB.Normalization;
import SG_MATLAB.SG;
import SNV_MATLAB.SNV_MATLAB;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWComplexity;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Pre-processing methods including:
 * 1.SNV
 * 2.Mean Centering
 * 3.Autoscaling
 * 4.Normalization
 * 5.Moving Window Smoothing
 * 6.SG Smoothing
 * 7.MSC
 * Created by hsp on 2017/6/14.
 */
public class PreProcessing_Methods {
    private Object[] result;          //return of pre-processing methods from MATLAB
    private double[][] SNVResult;
    private double[][] Mean_CenteringResult;
    private double[][] AutoscalingResult;
    private double[][] NormalizationResult;
    private double[][] MovingWindowSmoothingResult;
    private double[][] SGSmoothingResult;
    private double[][] MSCResult;
    private double[] spectrum;

    public PreProcessing_Methods(double[] spectrum){
        this.spectrum=spectrum;
    }

    /**
     * SNV(Standard Normal Variate Transformation)标准正太变量变换
     * function [B]=SNV(A) A is real-time spectrum, B is the spectrum pre-processed by SNV
     */
    public void pre_ProcessingBySNV() throws MWException {
        SNV_MATLAB snv=new SNV_MATLAB();
        /*-----------convert one-dimensional array into two-dimensional array---------*/
        //set size of matrix
        int[] n={1,this.spectrum.length};
        MWNumericArray matrixOfSpectrum=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfSpectrum.set(index,this.spectrum[index[1]-1]);
            }
        }
        //invoke the SNV method generated by MATLAB
        this.result=snv.SNV(1,matrixOfSpectrum);
        //convert the return from MATLAB into two-dimensional double array
        MWNumericArray Result=(MWNumericArray) this.result[0];
        this.SNVResult=(double[][]) Result.toDoubleArray();
        System.out.println("SNV done");
    }

    /**
     * Mean Centering 均值中心化
     * function [mcx] = Mean_Centering_Matlab(x) x is real-time spectrum, mcx is the spectrum pre-processed by mean centering
     * @throws MWException
     */
    public void pre_ProcessingByMean_Centering() throws MWException {
        Mean_Centering mc=new Mean_Centering();
        /*-----------convert one-dimensional array into two-dimensional array---------*/
        //set size of matrix
        int[] n={1,this.spectrum.length};
        MWNumericArray matrixOfSpectrum=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfSpectrum.set(index,this.spectrum[index[1]-1]);
            }
        }
        //invoke the SNV method generated by MATLAB
        this.result=mc.Mean_Centering_Matlab(1,matrixOfSpectrum);
        //convert the return from MATLAB into two-dimensional double array
        MWNumericArray Result=(MWNumericArray) this.result[0];
        this.Mean_CenteringResult= (double[][]) Result.toDoubleArray();
        System.out.println("Mean Centering done");
    }
    /**
     * Autoscaling 标准化
     * function [ax] = Autoscaling_MATLAB(x) x is real-time spectrum, ax is the spectrum pre-processed by Autoscaling
     * @throws MWException
     */
    public void pre_ProcessingByAutoscaling() throws MWException {
        Autoscaling as=new Autoscaling();
        /*-----------convert one-dimensional array into two-dimensional array---------*/
        //set size of matrix
        int[] n={1,this.spectrum.length};
        MWNumericArray matrixOfSpectrum=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfSpectrum.set(index,this.spectrum[index[1]-1]);
            }
        }
        //invoke the SNV method generated by MATLAB
        this.result=as.Autoscaling_MATLAB(1,matrixOfSpectrum);
        //convert the return from MATLAB into two-dimensional double array
        MWNumericArray Result=(MWNumericArray) this.result[0];
        this.AutoscalingResult= (double[][]) Result.toDoubleArray();
        System.out.println("Autoscaling done");
    }
    /**
     * Normalization 归一化
     * function [nx] = Normalization_MATLAB(x) x is real-time spectrum, nx is the spectrum pre-processed by Normalization
     * @throws MWException
     */
    public void pre_ProcessingByNormalization() throws MWException {
        Normalization normalization=new Normalization();
        /*-----------convert one-dimensional array into two-dimensional array---------*/
        //set size of matrix
        int[] n={1,this.spectrum.length};
        MWNumericArray matrixOfSpectrum=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfSpectrum.set(index,this.spectrum[index[1]-1]);
            }
        }
        //invoke the SNV method generated by MATLAB
        this.result=normalization.Normalization_MATLAB(1,matrixOfSpectrum);
        //convert the return from MATLAB into two-dimensional double array
        MWNumericArray Result=(MWNumericArray) this.result[0];
        this.NormalizationResult= (double[][]) Result.toDoubleArray();
        System.out.println("Normalization done");
    }
    /**
     *  Moving Window Smoothing 移动窗口平滑法
     * function [mdata]=Moving_Window_Smoothing_MATLAB(data,window) data is real-time spectrum, window is the width of window (must be odd number),
     * mdata is the spectrum pre-processed by moving window smoothing
     * @throws MWException
     */
    public void pre_ProcessingByMovingWindowSmoothing() throws MWException {
        MWS mws=new MWS();
        /*-----------convert one-dimensional array into two-dimensional array---------*/
        //set size of matrix
        int[] n={1,this.spectrum.length};
        MWNumericArray matrixOfSpectrum=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfSpectrum.set(index,this.spectrum[index[1]-1]);
            }
        }
        //invoke the SNV method generated by MATLAB
        this.result=mws.MWS_MATLAB(1,matrixOfSpectrum);
        //convert the return from MATLAB into two-dimensional double array
        MWNumericArray Result=(MWNumericArray) this.result[0];
        this.MovingWindowSmoothingResult= (double[][]) Result.toDoubleArray();
        System.out.println("Moving Window Smoothing done");
    }
    /**
     * Savitzky-Galoy smoothing
     * function [x_sg]= SG_MATLAB(x,width,order,deriv) x is real-time spectrum, width is the width of window,
     * order is the polynomial order, deriv is the order of derivative (O means that it doesnt do derivative), x_sg is the spectrum pre-processed by SG smoothing
     * @throws MWException
     */
    public void pre_ProcessingBySGSmoothing() throws MWException {
        SG sg =new SG();
        /*-----------convert one-dimensional array into two-dimensional array---------*/
        //set size of matrix
        int[] n={1,this.spectrum.length};
        MWNumericArray matrixOfSpectrum=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfSpectrum.set(index,this.spectrum[index[1]-1]);
            }
        }
        //invoke the SNV method generated by MATLAB
        this.result=sg.SG_MATLAB(1,matrixOfSpectrum);
        //convert the return from MATLAB into two-dimensional double array
        MWNumericArray Result=(MWNumericArray) this.result[0];
        this.SGSmoothingResult= (double[][]) Result.toDoubleArray();
        System.out.println("SG Smoothing done");
    }
    /**
     * MSC multiple scatter correction 多元散射校正
     * function [x_msc]=MSC_MATLAB(x,xref) x is real-time spectrum, xref is the reference spectra from excel, x_msc is the spectrum pre-processed by MSC
     * @throws MWException
     */
    public void pre_ProcessingByMSC() throws MWException {
        MSC msc=new MSC();
        /*-----------convert one-dimensional array into two-dimensional array---------*/
        //set size of matrix
        int[] n={1,this.spectrum.length};
        MWNumericArray matrixOfSpectrum=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfSpectrum.set(index,this.spectrum[index[1]-1]);
            }
        }
        //get reference spectra from excel
        InputStream is = null;
        double[][] xref=null;
        try {
            is = new FileInputStream("./xref for MSC.xlsx");
            XSSFWorkbook excel=new XSSFWorkbook(is);
            //read sheet, index of sheet in excel starts from 1,while in java starts from 0
            XSSFSheet sheet = excel.getSheetAt(0);
            xref=new double[sheet.getLastRowNum()+1][sheet.getRow(0).getLastCellNum()];
            for(int rowNo=0;rowNo<sheet.getLastRowNum()+1;rowNo++){
                XSSFRow row=sheet.getRow(rowNo);
                for(int cellNo=0;cellNo<row.getLastCellNum();cellNo++) {
                    XSSFCell cell=row.getCell(cellNo);
                    xref[rowNo][cellNo]=cell.getNumericCellValue();
                }
                }
                is.close();
        } catch (FileNotFoundException e) {
            System.out.println("Excel not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //invoke the SNV method generated by MATLAB
        this.result=msc.MSC_MATLAB(1,matrixOfSpectrum,xref);
        //convert the return from MATLAB into two-dimensional double array
        MWNumericArray Result=(MWNumericArray) this.result[0];
        this.MSCResult= (double[][]) Result.toDoubleArray();
        System.out.println("MSC done");
    }
    /**
     * get the spectrum pre-processed by SNV
     * @return two-dimensional double array storing the spectrum pre-processed by SNV
     */
    public double[][] getSNVResult(){
        return this.SNVResult;
    }

    /**
     * get the spectrum pre-processed by Mean Centering
     * @return two-dimensional double array storing the spectrum pre-processed by Mean Centering
     */
    public double[][] getMean_CenteringResult(){
        return this.Mean_CenteringResult;
    }

    /**
     * get the spectrum pre-processed by Autoscaling
     * @return two-dimensional double array storing the spectrum pre-processed by Autoscaling
     */
    public double[][] getAutoscalingResult(){
        return this.AutoscalingResult;
    }
    /**
     * get the spectrum pre-processed by Autoscaling
     * @return two-dimensional double array storing the spectrum pre-processed by Autoscaling
     */
    public double[][] getNormalizationResult(){
        return this.NormalizationResult;
    }
    /**
     * get the spectrum pre-processed by moving window smoothing
     * @return two-dimensional double array storing the spectrum pre-processed by moving window smoothing
     */
    public double[][] getMovingWindowSmoothingResult(){
        return this.MovingWindowSmoothingResult;
    }
    /**
     * get the spectrum pre-processed by SG Smoothing
     * @return two-dimensional double array storing the spectrum pre-processed by SG smoothing
     */
    public double[][] getSGSmoothingResult(){
        return this.SGSmoothingResult;
    }
    /**
     * get the spectrum pre-processed by MSC
     * @return two-dimensional double array storing the spectrum pre-processed by MSC
     */
    public double[][] getMSCResult(){
        return this.MSCResult;
    }
}
