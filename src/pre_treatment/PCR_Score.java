package pre_treatment;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class PCR_Score {
    private SimpleStringProperty method;
    private SimpleDoubleProperty VCR;
    private SimpleDoubleProperty PCs;
    private SimpleDoubleProperty r;
    private SimpleDoubleProperty Rsquared;

    public  PCR_Score(String method, double VCR,double PCs,double r, double Rsquared){
        this.method=new SimpleStringProperty(method);
        this.VCR=new SimpleDoubleProperty(VCR);
        this.PCs=new SimpleDoubleProperty(PCs);
        this.r=new SimpleDoubleProperty(r);
        this.Rsquared=new SimpleDoubleProperty(Rsquared);
    }

    public void setMethod(String method){
        this.method.set(method);
    }

    public void setVCR(double VCR){
        this.PCs.set(VCR);
    }

    public void setPCs(double PCs){
        this.PCs.set(PCs);
    }

    public void setR(double r){
        this.PCs.set(r);
    }

    public void setRsquared(double Rsquared){
        this.PCs.set(Rsquared);
    }
    
    public String getMethod(){
        return this.method.get();
    }

    public double getVCR(){
        return this.VCR.get();
    }

    public double getPCs(){
        return this.PCs.get();
    }

    public double getR(){
        return this.r.get();
    }

    public double getRsquared(){
        return this.Rsquared.get();
    }
}
