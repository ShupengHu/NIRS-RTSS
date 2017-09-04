package user_interface;

import com.mathworks.toolbox.javabuilder.MWException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import multivariate_calibration.PLS_Algorithm;
import pre_treatment.Assessments;
import pre_treatment.PCR_Score;
import pre_treatment.PreProcessing_Methods;
import virtual_input.Spectrum_Input;

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

    @FXML
    private TableView PCAScoreTable;

    @FXML
    private TableColumn methodsColumn;

    @FXML
    private TableColumn VCRColumn;

    @FXML
    private TableColumn PCColumn;

    @FXML
    private TableColumn coefficientColumn;

    @FXML
    private TableColumn RsquaredColumn;

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
    private double[][] historicalSNVResult;
    private double[][] historicalConcentrationBySNV;
    private double[][] historicalMeanCenteringResult;
    private double[][] historicalConcentrationByMeanCentering;
    private double[][] historicalAutoScalingResult;
    private double[][] historicalConcentrationByAutoscaling;
    private double[][] historicalNormalizationResult;
    private double[][] historicalConcentrationByNormalization;
    private double[][] historicalMWSResult;
    private double[][] historicalConcentrationByMWS;
    private double[][] historicalSGResult;
    private double[][] historicalConcentrationBySG;
    private double[][] historicalMSCResult;
    private double[][] historicalConcentrationByMSC;
    private double[][] historicalConcentrationByNone;
    private double VCR_SNV;
    private double VCR_MeanCentering;
    private double VCR_Autoscaling;
    private double VCR_Normalization;
    private double VCR_MWS;
    private double VCR_SG;
    private double VCR_MSC;
    private double VCR_NoPreProcess;
    private double PCs_SNV;
    private double PCs_MeanCentering;
    private double PCs_Autoscaling;
    private double PCs_Normalization;
    private double PCs_MWS;
    private double PCs_SG;
    private double PCs_MSC;
    private double PCs_NoPreProcess;
    private double r_SNV;
    private double r_MeanCentering;
    private double r_Autoscaling;
    private double r_Normalization;
    private double r_MWS;
    private double r_SG;
    private double r_MSC;
    private double r_NoPreProcess;
    private double R_SNV;
    private double R_MeanCentering;
    private double R_Autoscaling;
    private double R_Normalization;
    private double R_MWS;
    private double R_SG;
    private double R_MSC;
    private double R_NoPreProcess;
    private double[][] scores_pc1;

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
        this.historicalSNVResult=new double[5][this.numberOfVariables];
        this.historicalAutoScalingResult=new double[5][this.numberOfVariables];
        this.historicalSGResult=new double[5][this.numberOfVariables];
        this.historicalMWSResult=new double[5][this.numberOfVariables];
        this.historicalMeanCenteringResult=new double[5][this.numberOfVariables];
        this.historicalNormalizationResult=new double[5][this.numberOfVariables];
        this.historicalMSCResult=new double[5][this.numberOfVariables];
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
                                spectrumChart.getData().clear();
                                setOfSeriesForSpectrum[indexOfSpectra].setName("Spectrum " + (indexOfSpectra + 1));
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
            //timer execution delay:0ms; timer execution duration:25s
                      timer.schedule(tt, 0*1000, 25*1000);
        }
        //----------------------------------------for stop button--------------------------------------------
        else if(event.getSource()==stopButton){
            this.isStopped=true;
            //display input spectra
            setOfSeriesForSpectra=new XYChart.Series[indexOfSpectra];
            for(int i=0;i<setOfSeriesForSpectra.length;i++){
                this.setOfSeriesForSpectra[i]=new XYChart.Series();
                for(int j=0;j<numberOfVariables;j++){
                    this.setOfSeriesForSpectra[i].getData().add(new XYChart.Data(this.wavelength[j],this.spectra[i][j]));
                    setOfSeriesForSpectra[i].setName("Spectrum "+(i+1));
                }
            }
            this.spectrumChart.getData().clear();
            this.spectrumChart.getData().addAll(setOfSeriesForSpectra);
        }
    }

    /**
     * Pre-processing
     */
    public void pre_Processing() throws MWException {
        //System.out.println("------------------4--------------------");
        PreProcessing_Methods p = new PreProcessing_Methods(this.spectra[indexOfSpectra]);
        Assessments assessments = new Assessments();
        //-------------------------SNV---------------------------------
        p.pre_ProcessingBySNV();
        SNVResult = p.getSNVResult();
        seriesBySNV = new XYChart.Series();
        seriesBySNV.setName("Spectrum " + (indexOfSpectra + 1) + "(SNV)");
        for (int i = 0; i < this.SNVResult[0].length; i++) {
            //store pre-processed result for all times
            historicalSNVResult[indexOfSpectra][i] = SNVResult[0][i];
            seriesBySNV.getData().add(new XYChart.Data(wavelength[i], SNVResult[0][i]));
        }
        //------------------------- Mean Centering---------------------------------
        p.pre_ProcessingByMean_Centering();
        MeanCenteringResult = p.getMean_CenteringResult();
        seriesByMeanCentering = new XYChart.Series();
        seriesByMeanCentering.setName("Spectrum " + (indexOfSpectra + 1) + "(MeanCentering)");
        for (int i = 0; i < this.MeanCenteringResult[0].length; i++) {
            //store pre-processed result for all times
            historicalMeanCenteringResult[indexOfSpectra][i] = MeanCenteringResult[0][i];
            seriesByMeanCentering.getData().add(new XYChart.Data(wavelength[i], MeanCenteringResult[0][i]));
        }
        //-------------------------Autoscaling--------------------------------
        p.pre_ProcessingByAutoscaling();
        AutoscalingResult = p.getAutoscalingResult();
        seriesByAutoscaling = new XYChart.Series();
        seriesByAutoscaling.setName("Spectrum " + (indexOfSpectra + 1) + "(Autoscaling)");
        for (int i = 0; i < this.AutoscalingResult[0].length; i++) {
            //store pre-processed result for all times
            historicalAutoScalingResult[indexOfSpectra][i] = AutoscalingResult[0][i];
            seriesByAutoscaling.getData().add(new XYChart.Data(wavelength[i], AutoscalingResult[0][i]));
        }
        //------------------------------Normalizaiton---------------------------
        p.pre_ProcessingByNormalization();
        NormalizationResult = p.getNormalizationResult();
        seriesByNormalization = new XYChart.Series();
        seriesByNormalization.setName("Spectrum " + (indexOfSpectra + 1) + "(Normalization)");
        for (int i = 0; i < this.NormalizationResult[0].length; i++) {
            //store pre-processed result for all times
            historicalNormalizationResult[indexOfSpectra][i] = NormalizationResult[0][i];
            seriesByNormalization.getData().add(new XYChart.Data(wavelength[i], NormalizationResult[0][i]));
        }
        //------------------------------Moving Window Smoothing---------------------------
        p.pre_ProcessingByMovingWindowSmoothing();
        MWSResult = p.getMovingWindowSmoothingResult();
        seriesByMWS = new XYChart.Series();
        seriesByMWS.setName("Spectrum " + (indexOfSpectra + 1) + "(MWS)");
        for (int i = 0; i < this.MWSResult[0].length; i++) {
            //store pre-processed result for all times
            historicalMWSResult[indexOfSpectra][i] = MWSResult[0][i];
            seriesByMWS.getData().add(new XYChart.Data(wavelength[i], MWSResult[0][i]));
        }
        //------------------------------SG Smoothing---------------------------
        p.pre_ProcessingBySGSmoothing();
        SGResult = p.getSGSmoothingResult();
        seriesBySG = new XYChart.Series();
        seriesBySG.setName("Spectrum " + (indexOfSpectra + 1) + "(SG)");
        for (int i = 0; i < this.SGResult[0].length; i++) {
            //store pre-processed result for all times
            historicalSGResult[indexOfSpectra][i] = SGResult[0][i];
            seriesBySG.getData().add(new XYChart.Data(wavelength[i], SGResult[0][i]));
        }
        /*
        //------------------------------MSC---------------------------
        p.pre_ProcessingByMSC();
        MSCResult=p.getMSCResult();
        seriesByMSC=new XYChart.Series();
        seriesByMSC.setName("Spectrum " + (indexOfSpectra + 1)+"(MSC)");
        for(int i=0;i<this.MSCResult[0].length;i++){
            //store pre-processed result for all times
            historicalMSCResult[indexOfSpectra][i]=MSCResult[0][i];
            seriesByMSC.getData().add(new XYChart.Data(wavelength[i],MSCResult[0][i]));
        }
*/
        //display pre-processed spectrum on line chart
        pretreatedSpectrumChart.getData().clear();
        pretreatedSpectrumChart.getData().addAll(seriesBySNV, seriesByMeanCentering, seriesByAutoscaling, seriesByNormalization, seriesByMWS, seriesBySG);
        System.out.println("-----------------------Pre-processing done---------------------------");

        /**
         * Pre-processing assessments
         */
        //-----------------------------------------PCA-score----------------------------------------------------------
        if (indexOfSpectra == 4) {
            double [] test={0.0444948000000000,	0.0443834000000000	,0.0442581000000000,	0.0442124000000000	,0.0441836000000000};
            //--------------------------------------------SNV---------------------------------------------------------
            assessments.assessedByPCAScore(historicalSNVResult);
            this.VCR_SNV=assessments.getVCR()[0][0];
            this.PCs_SNV=assessments.getNumberofPCs()[0][0];
            this.scores_pc1=assessments.getScores();
            assessments.assessedByCoefficient(this.scores_pc1,test);
            this.r_SNV=assessments.getCoefficient()[0][0];
            this.R_SNV=assessments.getRsquared()[0][0];
            //----------------------------------------MeanCentering----------------------------------------------------
            assessments.assessedByPCAScore(historicalMeanCenteringResult);
            this.VCR_MeanCentering=assessments.getVCR()[0][0];
            this.PCs_MeanCentering = assessments.getNumberofPCs()[0][0];
            this.scores_pc1=assessments.getScores();
            assessments.assessedByCoefficient(this.scores_pc1,test);
            this.r_MeanCentering=assessments.getCoefficient()[0][0];
            this.R_MeanCentering=assessments.getRsquared()[0][0];
            //--------------------------------------------AutoScaling--------------------------------------------------
            assessments.assessedByPCAScore(historicalAutoScalingResult);
            this.VCR_Autoscaling=assessments.getVCR()[0][0];
            this.PCs_Autoscaling = assessments.getNumberofPCs()[0][0];
            this.scores_pc1=assessments.getScores();
            assessments.assessedByCoefficient(this.scores_pc1,test);
            this.r_Autoscaling=assessments.getCoefficient()[0][0];
            this.R_Autoscaling=assessments.getRsquared()[0][0];
            //------------------------------------------Normalization--------------------------------------------------
            assessments.assessedByPCAScore(historicalNormalizationResult);
            this.VCR_Normalization=assessments.getVCR()[0][0];
            this.PCs_Normalization = assessments.getNumberofPCs()[0][0];
            this.scores_pc1=assessments.getScores();
            assessments.assessedByCoefficient(this.scores_pc1,test);
            this.r_Normalization=assessments.getCoefficient()[0][0];
            this.R_Normalization=assessments.getRsquared()[0][0];
            //------------------------------------------MWS-------------------------------------------------------------
            assessments.assessedByPCAScore(historicalMWSResult);
            this.VCR_MWS=assessments.getVCR()[0][0];
            this.PCs_MWS = assessments.getNumberofPCs()[0][0];
            this.scores_pc1=assessments.getScores();
            assessments.assessedByCoefficient(this.scores_pc1,test);
            this.r_MWS=assessments.getCoefficient()[0][0];
            this.R_MWS=assessments.getRsquared()[0][0];
            //-----------------------------------------------SG---------------------------------------------------------
            assessments.assessedByPCAScore(historicalSGResult);
            this.VCR_SG=assessments.getVCR()[0][0];
            this.PCs_SG = assessments.getNumberofPCs()[0][0];
            this.scores_pc1=assessments.getScores();
            assessments.assessedByCoefficient(this.scores_pc1,test);
            this.r_SG=assessments.getCoefficient()[0][0];
            this.R_SG=assessments.getRsquared()[0][0];
            //-----------------------------------------------No Pre-Processing------------------------------------------
            assessments.assessedByPCAScore(this.spectra);
            this.VCR_NoPreProcess=assessments.getVCR()[0][0];
            this.PCs_NoPreProcess=assessments.getNumberofPCs()[0][0];
            this.scores_pc1=assessments.getScores();
            assessments.assessedByCoefficient(this.scores_pc1,test);
            this.r_NoPreProcess=assessments.getCoefficient()[0][0];
            this.R_NoPreProcess=assessments.getRsquared()[0][0];
            //----------------------------------------------MSC---------------------------------------------------------
        /*
        assessments.assessedByPCAScore(historicalMSCResult);
        this.VCR_MSC=assessments.getVCR()[0][0];
        this.PCs_MSC=assessments.getNumberofPCs()[0][0];
        this.scores_pc1=assessments.getScores();
            assessments.assessedByCoefficient(this.scores_pc1,test);
            this.r_MSC=assessments.getCoefficient()[0][0];
            this.R_MSC=assessments.getRsquared()[0][0];
        */

            //display VCRs on table, JavaFX TableView required a class to build data model
            ObservableList<PCR_Score> PCRScoreData = FXCollections.observableArrayList(
                    new PCR_Score("SNV", this.VCR_SNV,this.PCs_SNV,this.r_SNV,this.R_SNV),
                    new PCR_Score("MeanCentering", this.VCR_MeanCentering,this.PCs_MeanCentering,this.r_MeanCentering,this.R_MeanCentering),
                    new PCR_Score("AutoScaling", this.VCR_Autoscaling,this.PCs_Autoscaling,this.r_Autoscaling,this.R_MSC),
                    new PCR_Score("Normalization", this.VCR_Normalization,this.PCs_Normalization,this.r_Normalization,this.R_Normalization),
                    new PCR_Score("MWS",this.VCR_MWS, this.PCs_MWS,this.r_MWS,this.R_MWS),
                    new PCR_Score("SG", this.VCR_SG,this.PCs_SG,this.r_SG,this.R_SG),
                    new PCR_Score("None",this.VCR_NoPreProcess,this.PCs_NoPreProcess,this.r_NoPreProcess,this.R_NoPreProcess)
                    //new VCRofPC1("MSC",this.VCR_MSC,this.PCs_MSC)
            );
            methodsColumn.setCellValueFactory(new PropertyValueFactory<>("method"));
            VCRColumn.setCellValueFactory(new PropertyValueFactory<>("VCR"));
            PCColumn.setCellValueFactory(new PropertyValueFactory<>("PCs"));
            coefficientColumn.setCellValueFactory(new PropertyValueFactory<>("r"));
            RsquaredColumn.setCellValueFactory(new PropertyValueFactory<>("Rsquared"));
            PCAScoreTable.setItems(PCRScoreData);
            System.out.println("-----------------------PCA-score done---------------------------");
        }
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
