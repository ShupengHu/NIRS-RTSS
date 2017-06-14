package pre_treatment;


import SNV_MATLAB.SNV_MATLAB;
import com.mathworks.toolbox.javabuilder.*;
/**
 * Pre-processing methods including:
 * 1.SNV
 * Created by hsp on 2017/6/14.
 */
public class Pre_Processing {
    private Object[] result;          //return of pre-processing methods from MATLAB
    private double[][] SNVResult;
    private double[] spectrum;

    public Pre_Processing(double[] spectrum){
        this.spectrum=spectrum;
    }

    /**
     * SNV(Standard Normal Variate Transformation)��׼��̫�����任
     * function [B]=SNV(A) A is real-time spectrum, B is the spectrum pre-processed by SNV
     */
    public void pre_ProcessingBySNV() throws MWException {
        SNV_MATLAB snv=new SNV_MATLAB();
        /*-----------onvert one-dimensional array into two-dimensional array---------*/
        //set size of matrix
        int[] n={1,this.spectrum.length};
        MWNumericArray matrixOfSpectrum=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
		/* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n[1]; index[1]++) {
                //MATLAB�о����һ��Ԫ��λ��Ϊ��1,1), Java ��ά�����һ��Ԫ��λ��Ϊ��0,0��
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
     * get the spectrum pre-processed by SNV
     * @return two-dimensional double array storing the spectrum pre-processed by SNV
     */
    public double[][] getSNVResult(){
        return this.SNVResult;
    }
}