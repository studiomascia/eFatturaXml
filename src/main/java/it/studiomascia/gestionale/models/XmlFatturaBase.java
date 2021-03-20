/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.studiomascia.gestionale.models;


import it.studiomascia.gestionale.xml.FatturaElettronicaType;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author luigi
 */
@Entity
@Table(name = "xmlfatturabase")
@NamedQueries({
//    @NamedQuery(name = "XmlFatturaBase.findAllPassive", query = "SELECT x FROM XmlFatturaBase x WHERE x.attiva IS FALSE"),
    @NamedQuery(name = "XmlFatturaBase.findPassiveNotRegistered", query = "SELECT x FROM XmlFatturaBase x WHERE (x.attiva IS FALSE) AND (x.numeroRegistrazione IS NULL) "),
    @NamedQuery(name = "XmlFatturaBase.findAttiveNotRegistered", query = "SELECT x FROM XmlFatturaBase x WHERE (x.attiva IS TRUE) AND (x.numeroRegistrazione IS NULL) ")
//    @NamedQuery(name = "XmlFatturaBase.findAllAttive", query = "SELECT x FROM XmlFatturaBase x WHERE x.attiva IS TRUE")
})

public class XmlFatturaBase {
     public static final int FATTURA_SALDATA_MANUALE = 2;
     public static final int FATTURA_SALDATA_SISTEMA = 3;
     public static final int FATTURA_PARZIALMENTE_SALDATA = 1;
     public static final int FATTURA_NON_PAGATA = 0;
     
     public static final String STR_FATTURA_SALDATA_SISTEMA = "Pagamento impostato da sistema";
     public static final String STR_FATTURA_SALDATA_MANUALE = "Fattura saldata";
     public static final String STR_FATTURA_PARZIALMENTE_SALDATA = "Fattura parzialmente saldata";
     public static final String STR_FATTURA_NON_PAGATA = "Fattura non pagata";
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_inserimento")
    private Date dataInserimento;
    
    @Size(max = 255)
    @Column(name = "tipo_documento")
    private String tipoDocumento;
    
    @Lob
    @Column(name = "descrizione")
    private String descrizione;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_fattura")
    private Date dataFattura;
    
    @Size(max = 255)
    @Column(name = "numero_fattura")
    private String numeroFattura;
    
    @Column(name = "importo_fattura")
    private BigDecimal importoFattura;
    
    @Size(max = 255)
    @Column(name = "ragione_sociale")
    private String ragioneSociale;
    
    @Column(name = "partita_iva")
    private String partitaIva;
    
    @Size(max = 255)
    @Column(name = "numero_registrazione")
    private String numeroRegistrazione;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_registrazione")
    private Date dataRegistrazione;
    
    @Column(name = "creatore")
    private  String creatore;
    
    @Lob
    private String xmlString;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn( name="idAnagraficaSocieta" )
    private AnagraficaSocieta anagraficaSocieta;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Pagamento> pagamenti;
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ControlloFattura> controlli;
   
    @OneToMany(mappedBy="xmlFatturaBase", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Ddt> ddt;
    
    public Set<Ddt> getDDT() {
        return ddt;
    }
     
    public void setDDT(Set<Ddt> listaDDT) {
        this.ddt = listaDDT;
    }
     
    public AnagraficaSocieta getAnagraficaSocieta() {
        return anagraficaSocieta;
    }
    
    public void setAnagraficaSocieta(AnagraficaSocieta provider) {
        this.anagraficaSocieta = provider;
    }
    
    /**
     *  pagamenti
     */
    public Pagamento getUltimoPagamento(){
        Pagamento ret= new Pagamento();
        int tempId=0;
        for ( Pagamento p : pagamenti)
        {
            if (p.getId()> tempId) ret =p ;
        }
        return ret;
    }

    public Set<Pagamento> getPagamenti() {
        return pagamenti;
    }
    
    public void setPagamenti(Set<Pagamento> listaPagamenti) {
        this.pagamenti = listaPagamenti;
    }
    
    public int getStatoPagamento(){
        int ret = FATTURA_NON_PAGATA;
        Pagamento ultimo = getUltimoPagamento();
        if (ultimo != null && ultimo.getId()>0){
            switch (ultimo.getStatoPagamento())
            {
                case Pagamento.PAGAMENTO_ACCONTO:
                    ret = FATTURA_PARZIALMENTE_SALDATA;
                break;
                
                case Pagamento.PAGAMENTO_SALDO:
                    ret = FATTURA_SALDATA_MANUALE;
                break;
                
                case Pagamento.PAGAMENTO_SISTEMA:
                    ret = FATTURA_SALDATA_SISTEMA;
                break;
            }
        }
    
        return ret;
    }
    
     public Boolean isToPay(){
        Boolean isToPay = true;
        Pagamento ultimo = getUltimoPagamento();
        
        if (ultimo.getId()>0){
            int statoPagamento = ultimo.getStatoPagamento();
            if (( statoPagamento== Pagamento.PAGAMENTO_SALDO) || (statoPagamento == Pagamento.PAGAMENTO_SISTEMA))    
            {
                isToPay = false;
            }
        }
        return isToPay;
     }
    
    
    //eliminare questi metodi
    
    public boolean isFatturaSaldataManuale() {
        Boolean ret=false;
        Pagamento ultimo = getUltimoPagamento();
        if (ultimo != null && ultimo.getId()>0){
           if (ultimo.getStatoPagamento()==Pagamento.PAGAMENTO_SALDO) ret =true; 
        }
        return ret;   
    }
    
    public boolean isFatturaSaldataParziale() {
       Boolean ret=false;
        Pagamento ultimo = getUltimoPagamento();
        if (ultimo != null && ultimo.getId()>0){
           if (ultimo.getStatoPagamento()==Pagamento.PAGAMENTO_ACCONTO) ret =true; 
        }
        return ret;      
    }

    public boolean isFatturaSaldataDaSistema() {
         Boolean ret=false;
        Pagamento ultimo = getUltimoPagamento();
        if (ultimo != null && ultimo.getId()>0){
           if (ultimo.getStatoPagamento()==Pagamento.PAGAMENTO_SISTEMA) ret =true; 
        }
        return ret;   
    }
    
    public boolean isFatturaNonPagata() {
        Boolean ret=true;
        Pagamento ultimo = getUltimoPagamento();
        if (ultimo != null && ultimo.getId()>0){
           ret=false;
        }
        return ret;   
    }
    
    // Metodi che agiscono sui controlli
    
    public ControlloFattura getUltimoControllo() {
        ControlloFattura ret = new ControlloFattura();
        int tempId=0;
        for ( ControlloFattura p : controlli)
        {
            if (p.getId() > tempId) ret =p ;
        }
        return ret;
    }
    
    public int getEsitoUltimoControllo() {
        return this.getUltimoControllo().getEsitoControllo();
    }
    
    public Set<ControlloFattura> getControlli() {
        return controlli;
    }

    /**
     * @param controlli the controlli to set
     */
    public void setControlli(Set<ControlloFattura> controlli) {
        this.controlli = controlli;
    }
     
    public boolean isControllataOK() {
        Boolean ret=false;
        if (this.getUltimoControllo().getEsitoControllo() == ControlloFattura.VALIDATA) ret =true;
        return ret;   
    }
    
    public boolean isControllataNOK() {
        Boolean ret=false;
        if (this.getUltimoControllo().getEsitoControllo() == ControlloFattura.BLOCCATA) ret =true;
        return ret; 
    }
    
    public boolean isControllataWAIT() {
       Boolean ret=false;
        if (this.getUltimoControllo().getEsitoControllo() == ControlloFattura.PENDING) ret =true;
        return ret; 
    }

    private boolean attiva;
    
    /**
     * @return the attiva status
     */
    public boolean isAttiva() {
        return this.attiva;
    }

    /**
     * @param attiva the id to set
     */
    public void setAttiva(boolean x ) {
        this.attiva = x;
    }
    
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

     /**
     * @return the tipoDocumento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
    
    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @param descrizione the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    
    /**
     * @return the dataInserimento
     */
    public Date getDataInserimento() {
        return dataInserimento;
    }

    /**
     * @param dataInserimento the dataInserimento to set
     */
    public void setDataInserimento(Date dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    
    
    /**
     * @return the dataFattura
     */
    public Date getDataFattura() {
        return dataFattura;
    }

    /**
     * @param dataFattura the dataFattura to set
     */
    public void setDataFattura(Date dataFattura) {
        this.dataFattura = dataFattura;
    }
    
    /**
     * @return the numeroFattura
     */
    public String getNumeroFattura() {
        return this.numeroFattura;
    }

    /**
     * @param protocolloFattura the numeroFattura to set
     */
    public void setNumeroFattura(String numFattura) {
        this.numeroFattura = numFattura;
    }
    
    /**
     * @return the importoFattura
     */
    public BigDecimal getImportoFattura() {
        return this.importoFattura;
    }

    /**
     * @param protocoimportoFattura the importoFattura to set
     */
    public void setImportoFattura(BigDecimal importoFattura) {
        this.importoFattura = importoFattura;
    }
    
     /**
     * @return the ragioneSociale
     */
    public String getRagioneSociale() {
        return this.ragioneSociale;
    }

    /**
     * @param ragioneSociale the ragioneSociale to set
     */
    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }
    
     /**
     * @return the partitaIva
     */
    public String getPartitaIva() {
        return this.partitaIva;
    }

    /**
     * @param partitaIva the partitaIva to set
     */
    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }
    
    
    /**
     * @return the numeroRegistrazione
     */
    public String getNumeroRegistrazione() {
        return this.numeroRegistrazione;
    }

    /**
     * @param protocolloFattura the numeroRegistrazione to set
     */
    public void setNumeroRegistrazione(String protocolloFattura) {
        this.numeroRegistrazione = protocolloFattura;
    }
    
     

    /**
     * @return the dataRegistrazione
     */
    public Date getDataRegistrazione() {
        return this.dataRegistrazione;
    }

    /**
     * @param dataRegistrazione the dataRegistrazione to set
     */
    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    /**
     * @return the stringaXml
     */
    public String getXmlData() {
        return this.xmlString;
    }

    /**
     * @param data the data to set
     */
    public void setXmlData(String data) {
        this.xmlString = data;
    }
    
    public XmlFatturaBase(Integer id) {
        this.id = id;
        this.attiva=false;
    }

    public XmlFatturaBase() {
        this.attiva=false;
    }
    
     public XmlFatturaBase(boolean isAttiva) {
        this.attiva=isAttiva;
    }

    /**
     * @return the creatore
     */
    public String getCreatore() {
        return creatore;
    }

    /**
     * @param creatore the creatore to set
     */
    public void setCreatore(String creatore) {
        this.creatore = creatore;
    }

    public FatturaElettronicaType getFatturaElettronica()
    {
        FatturaElettronicaType item = new FatturaElettronicaType();
        byte[] byteArr;
        try 
        {
            StringWriter sw = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(FatturaElettronicaType.class);
            // Unmarshaller serve per convertire il file in un oggetto
            Unmarshaller jaxbUnMarshaller = context.createUnmarshaller();
            // Marshaller serve per convertire l'oggetto ottenuto dal file in una stringa xml
            Marshaller jaxbMarshaller = context.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
              byteArr = this.getXmlData().getBytes("UTF-8");
                sw = new StringWriter();
          
                JAXBElement<FatturaElettronicaType> root =jaxbUnMarshaller.unmarshal(new StreamSource(new ByteArrayInputStream(byteArr)), FatturaElettronicaType.class);
                item = root.getValue();
                //jaxbMarshaller.marshal(root, sw);
        }
        catch (JAXBException e) 
        {
            e.printStackTrace();
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        return item;
    }
}
