package pre_treatment;

import Correlation_MATLAB.Correlation;
import PCA_Score.PCAScore;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWComplexity;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class Assessments {
    private double[][] preProcessedResult;
    private double[][] x1;
    private double[] x2;
    private Object[] PCAScoreresult;
    private Object[] Correlationresult;
    private double[][] scores;
    private double[][] VCR;
    private double[][] pc_number;
    private double[][] coefficient;
    private double[][] Rsquared;

    public Assessments(){

    }

    /**
     * PCA-score PCA得分
     * function [score_pc1,VCR_pc1,pc_number]=PCA_Score(x)
     * x is the preprocessed spectra matrix
     * score_pc1 is the PCA-score values on the first principal component(PC)
     * VCR_pc1 is the variance contribution rate of the first PC
     * pc_number is the number of PCs when their accumulated variance contribution rate is more than 95%
     */
    public void assessedByPCAScore(double[][] preProcessedResult) throws MWException {
        this.preProcessedResult=preProcessedResult;
        PCAScore pcas=new PCAScore();
        //set size of matrix
        int[] n={this.preProcessedResult.length,this.preProcessedResult[0].length};
        MWNumericArray matrixOfpreProcessedResult=MWNumericArray.newInstance(n, MWClassID.DOUBLE, MWComplexity.REAL);
        /* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfpreProcessedResult.set(index,this.preProcessedResult[index[0]-1][index[1]-1]);
            }
        }
        this.PCAScoreresult=pcas.PCA_Score(3,matrixOfpreProcessedResult);
        MWNumericArray Result1=(MWNumericArray) this.PCAScoreresult[0];
        //返回的得分矩阵是列向量矩阵，将此矩阵转置
        double[][] scoreMatrix=(double[][]) Result1.toDoubleArray();
        //System.out.println(scoreMatrix[0][0]);
        this.scores=new double[1][scoreMatrix.length];
        for(int i=0;i<scoreMatrix.length;i++){
            this.scores[0][i]=scoreMatrix[i][0];
        }
        //System.out.println(this.scores[0][0]);
        MWNumericArray Result2=(MWNumericArray) this.PCAScoreresult[1];
        this.VCR=(double[][]) Result2.toDoubleArray();
        //System.out.println(this.VCR[0][0]);
        MWNumericArray Result3=(MWNumericArray) this.PCAScoreresult[2];
        this.pc_number=(double[][])Result3.toDoubleArray();
        //System.out.println(this.pc_number[0][0]);
    }

    public void assessedByCoefficient(double[][] x1, double[] x2) throws MWException {
        this.x1=x1;
        this.x2=x2;
        Correlation c=new Correlation();

        //set size of matrix
        int[] n1={this.x1.length,this.x1[0].length};
        MWNumericArray matrixOfx1=MWNumericArray.newInstance(n1, MWClassID.DOUBLE, MWComplexity.REAL);
        /* Set matrix values */
        int[] index = {1, 1};
        for (index[0]= 1; index[0] <= n1[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n1[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfx1.set(index,this.x1[index[0]-1][index[1]-1]);
            }
        }
        int[] n2={1,this.x2.length};
        MWNumericArray matrixOfx2=MWNumericArray.newInstance(n2, MWClassID.DOUBLE, MWComplexity.REAL);
        /* Set matrix values */
        for (index[0]= 1; index[0] <= n2[0]; index[0]++) {
            for (index[1] = 1; index[1] <= n2[1]; index[1]++) {
                //MATLAB中矩阵第一个元素位置为（1,1), Java 二维数组第一个元素位置为（0,0）
                matrixOfx2.set(index,this.x2[index[1]-1]);
            }
        }

        this.Correlationresult=c.Correlation_MATLAB(2,matrixOfx1,matrixOfx2);
        MWNumericArray Result1=(MWNumericArray) this.PCAScoreresult[0];
        this.coefficient=(double[][]) Result1.toDoubleArray();
        MWNumericArray Result2=(MWNumericArray) this.PCAScoreresult[1];
        this.Rsquared=(double[][]) Result1.toDoubleArray();
    }

    /**
     *
     * @return get the PCA-score values on the first principal component(PC)
     */
    public double[][] getScores(){
        return this.scores;
    }

    /**
     *
     * @return get the variance contribution rate of the first PC
     */
    public double[][] getVCR(){
        return this.VCR;
    }

    /**
     *
     * @return get the variance contribution rate of the first PC
     */
    public double[][] getNumberofPCs(){
        return this.pc_number;
    }

    /**
     *
     * @return get the correlation coefficient
     */
    public double[][] getCoefficient(){
        return this.coefficient;
    }

    /**
     *
     * @return get the coefficient of determination
     */
    public double[][] getRsquared(){
        return this.Rsquared;
    }
}
