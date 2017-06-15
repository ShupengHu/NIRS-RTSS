package user_interface;

import MWS_MATLAB.MWS;
import com.mathworks.toolbox.javabuilder.MWException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import multivariate_calibration.PLS_Algorithm;
import pre_treatment.Pre_Processing;
import virtual_input.Spectrum_Input;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    @FXML
    private AnchorPane pane;
    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu fileMenu;

    @FXML
    private Menu editMenu;

    @FXML
    private Menu helpMenu;

    @FXML
    private ToolBar toolbar;

    @FXML
    private Button turnOnButton;

    @FXML
    private Button runButton;

    @FXML
    private Button stopButton;

    @FXML
    private LineChart spectrumChart;

    @FXML
    private NumberAxis spectrumXAxis;

    @FXML
    private NumberAxis spectrumYAxis;

    @FXML
    private LineChart spectraChart;

    @FXML
    private NumberAxis spectraXAxis;

    @FXML
    private NumberAxis spectraYAxis;

    @FXML
    private LineChart pretreatedSpectrumChart;

    @FXML
    private NumberAxis pretreatedSpectrumXAxis;

    @FXML
    private NumberAxis pretreatedSpectrumYAxis;
    @FXML
    private ScatterChart concentrationChart;

    @FXML
    private NumberAxis concentrationXAxis;

    @FXML
    private NumberAxis concentrationYAxis;

    private double[][] spectra;
    private double[] wavelength;
    private int numberOfVariables=0;
    private int indexOfSpectra=0;
    private XYChart.Series[] setOfSeriesForSpectrum;
    private XYChart.Series[] setOfSeriesForSpectra; //series cannot be reused. so have to create new instances of series
    private XYChart.Series seriesBySNV;
    private XYChart.Series seriesByMeanCentering;
    private XYChart.Series seriesByAutoscaling;
    private XYChart.Series seriesByNormalization;
    private XYChart.Series seriesByMWS;
    private XYChart.Series seriesBySG;
    private XYChart.Series seriesByMSC;
    private XYChart.Series seriesBySNV_PLS;
    private XYChart.Series seriesByMeanCentering_PLS;
    private XYChart.Series seriesByAutoscaling_PLS;
    private XYChart.Series seriesByNormalization_PLS;
    private XYChart.Series seriesByMWS_PLS;
    private XYChart.Series seriesBySG_PLS;
    private XYChart.Series seriesByMSC_PLS;
    private XYChart.Series seriesByNone_PLS;
    private boolean isStopped;
    private double[][] noPre_ProcesingResult;
    private double[][] SNVResult;
    private double[][] MeanCenteringResult;
    private double[][] AutoscalingResult;
    private double[][] NormalizationResult;
    private double[][] MWSResult;
    private double[][] SGResult;
    private double[][] MSCResult;
    private double[][] realTimeConcentration;
    private double[][] historicalConcentrationBySNV;
    private double[][] historicalConcentrationByMeanCentering;
    private double[][] historicalConcentrationByAutoscaling;
    private double[][] historicalConcentrationByNormalization;
    private double[][] historicalConcentrationByMWS;
    private double[][] historicalConcentrationBySG;
    private double[][] historicalConcentrationByMSC;
    private double[][] historicalConcentrationByNone;

    @FXML
    /**
     * Initialize components of user interface
     */
    public void initialize(){
        //read absorbance and wavelength
        Spectrum_Input.readSpectraFromFile("./Absorbance/File/");
        Spectrum_Input.readWavelength();
        this.spectra=Spectrum_Input.getSpectra();
        this.wavelength=Spectrum_Input.getWavelength();
        this.numberOfVariables=Spectrum_Input.getNumberOfVariables();
        //System.out.println(this.wavelength[0]+"---"+this.wavelength[1]+"---"+this.spectra[0][0]+"---"+this.spectra[0][1]);
        this.historicalConcentrationBySNV=new double[1][this.spectra.length];
        this.historicalConcentrationByMeanCentering=new double[1][this.spectra.length];
        this.historicalConcentrationByAutoscaling=new double[1][this.spectra.length];
        this.historicalConcentrationByNormalization=new double[1][this.spectra.length];
        this.historicalConcentrationByMWS=new double[1][this.spectra.length];
        this.historicalConcentrationBySG=new double[1][this.spectra.length];
        this.historicalConcentrationByMSC=new double[1][this.spectra.length];
        this.historicalConcentrationByNone=new double[1][this.spectra.length];
    }

    @FXML
    /**
     * 1. onButtonClick event for turn on button
     * 2. onButtonClick event for run button
     * 3. onButtonClick event for stop button
     */
    void onButtonClick(ActionEvent event) {
        //------------------------------------for turn on button------------------------------------------
        if(event.getSource()==turnOnButton){
            //create instances for series.
            this.setOfSeriesForSpectrum = new XYChart.Series[this.spectra.length];
            //System.out.println("------------------1--------------------");
            for(int i=0;i<this.spectra.length;i++){
                this.setOfSeriesForSpectrum[i]=new XYChart.Series();
                for(int j=0;j<numberOfVariables;j++){
                    this.setOfSeriesForSpectrum[i].getData().add(new XYChart.Data(this.wavelength[j],this.spectra[i][j]));
                }
            }
            System.out.println("Turn On");
        }
        //----------------------------------------for run button----------------------------------------------
        else if(event.getSource()==runButton) {
            System.out.println("Running");
            //Use concurrency and timer to simulate the spectrum input. Notice need to use Platform.runlater() to call JavaFX thread to handle progress
            spectraChart.getData().clear();
            Timer timer=new Timer();
            TimerTask tt=new TimerTask() {
                @Override
                public void run() {
                    //Call JavaFX thread to handle progress
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //dipslay real-time spectrum
                            if(indexOfSpectra<setOfSeriesForSpectrum.length&&isStopped==false) {
                                spectraChart.getData().clear();
                                setOfSeriesForSpectrum[indexOfSpectra].setName("Spectrum " + (indexOfSpectra + 1));
                                spectrumChart.getData().clear();
                                spectrumChart.getData().add(setOfSeriesForSpectrum[indexOfSpectra]);
                                //System.out.println("------------------2--------------------");
                                try {
            //----------------------------------------------Pre-processing-----------------------------------------
                                    pre_Processing();
             //---------------------------------------------Multivariate calibration-----------------------------------
                                    multivariate_Calibration();
                                } catch (MWException e) {
                                    e.printStackTrace();
                                }
                                //System.out.println("------------------3--------------------");
                                indexOfSpectra++;
                            }
                            //condition to stop timer
                            else if(indexOfSpectra>=setOfSeriesForSpectrum.length||isStopped==true){
                                timer.cancel();
                                isStopped=false;
                                System.out.println("Cancelled");
                            }
                        }
                    });
                }
            };
            //timer execution delay:0ms; timer execution duration:5s
            timer.schedule(tt, 0*1000, 20*1000);
            //execution delay:0s; execution period:15s

        }
        //----------------------------------------for stop button--------------------------------------------
        else if(event.getSource()==stopButton){
            this.isStopped=true;
            //display input spectra
            setOfSeriesForSpectra=new XYChart.Series[indexOfSpectra];
            for(int i=0;i<setOfSeriesForSpectra.length;i++){
                setOfSeriesForSpectra[i]=setOfSeriesForSpectrum[i];
                setOfSeriesForSpectra[i].setName("Spectrum "+(i+1));
            }
            this.spectraChart.getData().addAll(setOfSeriesForSpectra);
        }
    }

    /**
     * Pre-processing
     */
    public void pre_Processing() throws MWException {
        //System.out.println("------------------4--------------------");
        Pre_Processing p=new Pre_Processing(this.spectra[indexOfSpectra]);
        //-------------------------SNV---------------------------------
        p.pre_ProcessingBySNV();
        SNVResult=p.getSNVResult();
        seriesBySNV=new XYChart.Series();
        seriesBySNV.setName("Spectrum(SNV) " + (indexOfSpectra + 1));
        for(int i=0;i<this.SNVResult[0].length;i++){
          seriesBySNV.getData().add(new XYChart.Data(wavelength[i],SNVResult[0][i]));
       }
        //------------------------- Mean Centering---------------------------------
        p.pre_ProcessingByMean_Centering();
        MeanCenteringResult=p.getMean_CenteringResult();
        seriesByMeanCentering=new XYChart.Series();
        seriesByMeanCentering.setName("Spectrum(MeanCentering) " + (indexOfSpectra + 1));
        for(int i=0;i<this.MeanCenteringResult[0].length;i++){
            seriesByMeanCentering.getData().add(new XYChart.Data(wavelength[i],MeanCenteringResult[0][i]));
        }
        //-------------------------Autoscaling--------------------------------
        p.pre_ProcessingByAutoscaling();
        AutoscalingResult=p.getAutoscalingResult();
        seriesByAutoscaling=new XYChart.Series();
        seriesByAutoscaling.setName("Spectrum(Autoscaling) " + (indexOfSpectra + 1));
        for(int i=0;i<this.AutoscalingResult[0].length;i++){
            seriesByAutoscaling.getData().add(new XYChart.Data(wavelength[i],AutoscalingResult[0][i]));
        }
        //------------------------------Normalizaiton---------------------------
        p.pre_ProcessingByNormalization();
        NormalizationResult=p.getNormalizationResult();
        seriesByNormalization=new XYChart.Series();
        seriesByNormalization.setName("Spectrum(Normalization) " + (indexOfSpectra + 1));
        for(int i=0;i<this.NormalizationResult[0].length;i++){
            seriesByNormalization.getData().add(new XYChart.Data(wavelength[i],NormalizationResult[0][i]));
        }
        //------------------------------Moving Window Smoothing---------------------------
        p.pre_ProcessingByMovingWindowSmoothing();
        MWSResult=p.getMovingWindowSmoothingResult();
        seriesByMWS=new XYChart.Series();
        seriesByMWS.setName("Spectrum(MWS) " + (indexOfSpectra + 1));
        for(int i=0;i<this.MWSResult[0].length;i++){
            seriesByMWS.getData().add(new XYChart.Data(wavelength[i],MWSResult[0][i]));
        }
        //------------------------------SG Smoothing---------------------------
        p.pre_ProcessingBySGSmoothing();
        SGResult=p.getSGSmoothingResult();
        seriesBySG=new XYChart.Series();
        seriesBySG.setName("Spectrum(SG) " + (indexOfSpectra + 1));
        for(int i=0;i<this.SGResult[0].length;i++){
            seriesBySG.getData().add(new XYChart.Data(wavelength[i],SGResult[0][i]));
        }
        /*
        //------------------------------MSC---------------------------
        p.pre_ProcessingByMSC();
        MSCResult=p.getMSCResult();
        seriesByMSC=new XYChart.Series();
        seriesByMSC.setName("Spectrum(MSC) " + (indexOfSpectra + 1));
        for(int i=0;i<this.MSCResult[0].length;i++){
            seriesByMSC.getData().add(new XYChart.Data(wavelength[i],MSCResult[0][i]));
        }
        */
        //display pre-processed spectrum on line chart
        pretreatedSpectrumChart.getData().clear();
        pretreatedSpectrumChart.getData().addAll(seriesBySNV,seriesByMeanCentering,seriesByAutoscaling,seriesByNormalization,seriesByMWS,seriesBySG);
        System.out.println("-----------------------Pre-processing done---------------------------");
    }

    /**
     * Multivariate Calibration
     */
    public void multivariate_Calibration() throws MWException {
        //--------------------------No Pre-Processing-------------------------
        //convert the one-dimensional double array storing real-time spectrum into two-dimensional double array
        noPre_ProcesingResult=new double[1][numberOfVariables];
        for(int i=0;i<numberOfVariables;i++) {
            noPre_ProcesingResult[0][i]=spectra[indexOfSpectra][i];
        }
        //use PLS without pre-processing
        PLS_Algorithm pls=new PLS_Algorithm(noPre_ProcesingResult);
        realTimeConcentration=pls.getConcentration();
        historicalConcentrationByNone[0][indexOfSpectra]=realTimeConcentration[0][0];
        //set series
        seriesByNone_PLS=new XYChart.Series();
        seriesByNone_PLS.setName("Urea(None)");
        for(int i=0;i<indexOfSpectra+1;i++) {
            seriesByNone_PLS.getData().add(new XYChart.Data(i+1, historicalConcentrationByNone[0][i]));
        }
        System.out.println("No Pre-Processing_PLS done");
        //-----------------------------------------With Pro-Processing----------------------------------------------
        //----------------------------------------------SNV-------------------------------------------------------
        pls=new PLS_Algorithm(SNVResult);
        realTimeConcentration=pls.getConcentration();
        historicalConcentrationBySNV[0][indexOfSpectra]=realTimeConcentration[0][0];
        //set series
        seriesBySNV_PLS=new XYChart.Series();
        seriesBySNV_PLS.setName("Urea(SNV)");
        for(int i=0;i<indexOfSpectra+1;i++) {
            seriesBySNV_PLS.getData().add(new XYChart.Data(i+1, historicalConcentrationBySNV[0][i]));
        }
        System.out.println("SNV_PLS done");
        //----------------------------------------------Mean Centering-------------------------------------------------------
        pls=new PLS_Algorithm(MeanCenteringResult);
        realTimeConcentration=pls.getConcentration();
        historicalConcentrationByMeanCentering[0][indexOfSpectra]=realTimeConcentration[0][0];
        //set series
        seriesByMeanCentering_PLS=new XYChart.Series();
        seriesByMeanCentering_PLS.setName("Urea(MeanCentering)");
        for(int i=0;i<indexOfSpectra+1;i++) {
            seriesByMeanCentering_PLS.getData().add(new XYChart.Data(i+1, historicalConcentrationByMeanCentering[0][i]));
        }
        System.out.println("MeanCentering_PLS done");
        //----------------------------------------------Autoscaling-------------------------------------------------------
        pls=new PLS_Algorithm(AutoscalingResult);
        realTimeConcentration=pls.getConcentration();
        historicalConcentrationByAutoscaling[0][indexOfSpectra]=realTimeConcentration[0][0];
        //set series
        seriesByAutoscaling_PLS=new XYChart.Series();
        seriesByAutoscaling_PLS.setName("Urea(Autoscaling)");
        for(int i=0;i<indexOfSpectra+1;i++) {
            seriesByAutoscaling_PLS.getData().add(new XYChart.Data(i+1, historicalConcentrationByAutoscaling[0][i]));
        }
        System.out.println("Autoscaling_PLS done");
        //----------------------------------------------Normalization-------------------------------------------------------
        pls=new PLS_Algorithm(NormalizationResult);
        realTimeConcentration=pls.getConcentration();
        historicalConcentrationByNormalization[0][indexOfSpectra]=realTimeConcentration[0][0];
        //set series
        seriesByNormalization_PLS=new XYChart.Series();
        seriesByNormalization_PLS.setName("Urea(Normalization)");
        for(int i=0;i<indexOfSpectra+1;i++) {
            seriesByNormalization_PLS.getData().add(new XYChart.Data(i+1, historicalConcentrationByNormalization[0][i]));
        }
        System.out.println("Normalization_PLS done");
        //----------------------------------------------Moving Window Smoothing-------------------------------------------------------
        pls=new PLS_Algorithm(MWSResult);
        realTimeConcentration=pls.getConcentration();
        historicalConcentrationByMWS[0][indexOfSpectra]=realTimeConcentration[0][0];
        //set series
        seriesByMWS_PLS=new XYChart.Series();
        seriesByMWS_PLS.setName("Urea(MWS)");
        for(int i=0;i<indexOfSpectra+1;i++) {
            seriesByMWS_PLS.getData().add(new XYChart.Data(i+1, historicalConcentrationByMWS[0][i]));
        }
        System.out.println("MWS_PLS done");
        //----------------------------------------------SG-------------------------------------------------------
        pls=new PLS_Algorithm(SGResult);
        realTimeConcentration=pls.getConcentration();
        historicalConcentrationBySG[0][indexOfSpectra]=realTimeConcentration[0][0];
        //set series
        seriesBySG_PLS=new XYChart.Series();
        seriesBySG_PLS.setName("Urea(SG)");
        for(int i=0;i<indexOfSpectra+1;i++) {
            seriesBySG_PLS.getData().add(new XYChart.Data(i+1, historicalConcentrationBySG[0][i]));
        }
        System.out.println("SG_PLS done");
        //----------------------------------------------MSC-------------------------------------------------------
        /*
        pls=new PLS_Algorithm(MSCResult);
        realTimeConcentration=pls.getConcentration();
        historicalConcentrationByMSC[0][indexOfSpectra]=realTimeConcentration[0][0];
        //set series
        seriesByMSC_PLS=new XYChart.Series();
        seriesByMSC_PLS.setName("Urea(MSC)");
        for(int i=0;i<indexOfSpectra+1;i++) {
            seriesByMSC_PLS.getData().add(new XYChart.Data(i+1, historicalConcentrationByMSC[0][i]));
        }
        System.out.println("MSC_PLS done");
        */

        //display concentration on scatter chart
        concentrationChart.getData().clear();
        concentrationChart.getData().addAll(seriesByNone_PLS,seriesBySNV_PLS,seriesByMeanCentering_PLS,seriesByAutoscaling_PLS,seriesByNormalization_PLS,seriesByMWS_PLS,seriesBySG_PLS);
        System.out.println("---------------------Multivariate calibration done--------------------------");
    }
}
