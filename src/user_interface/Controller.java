package user_interface;

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
    private XYChart.Series seriesBySNV_PLS;
    private XYChart.Series seriesByNone_PLS;
    private boolean isStopped;
    private double[][] noPre_ProcesingResult;
    private double[][] SNVResult;
    private double[][] realTimeConcentration;
    private double[][] historicalConcentrationBySNV;
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
            timer.schedule(tt, 0*1000, 10*1000);
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
        //display the spectrum pre-processed by SNV
        seriesBySNV=new XYChart.Series();
        seriesBySNV.setName("Spectrum(SNV) " + (indexOfSpectra + 1));
        for(int i=0;i<this.SNVResult[0].length;i++){
          seriesBySNV.getData().add(new XYChart.Data(wavelength[i],SNVResult[0][i]));
       }
        pretreatedSpectrumChart.getData().clear();
        pretreatedSpectrumChart.getData().add(seriesBySNV);
        System.out.println("Pre-processing done");
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
            seriesByNone_PLS.getData().add(new XYChart.Data(i, historicalConcentrationByNone[0][i]));
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
            seriesBySNV_PLS.getData().add(new XYChart.Data(i, historicalConcentrationBySNV[0][i]));
        }
        System.out.println("SNV_PLS done");
        //System.out.println("------------------5--------------------");
        //display concentration on scatter chart
        concentrationChart.getData().clear();
        concentrationChart.getData().addAll(seriesByNone_PLS,seriesBySNV_PLS);
        System.out.println("Multivariate calibration done");
    }
}
