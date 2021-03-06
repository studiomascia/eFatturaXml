//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.3.0 
// Vedere <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2021.03.16 alle 01:10:31 PM CET 
//


package it.studiomascia.gestionale.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per DatiDocumentiCorrelatiType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiDocumentiCorrelatiType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RiferimentoNumeroLinea" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RiferimentoNumeroLineaType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="IdDocumento" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String20Type"/&gt;
 *         &lt;element name="Data" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *         &lt;element name="NumItem" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String20Type" minOccurs="0"/&gt;
 *         &lt;element name="CodiceCommessaConvenzione" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String100LatinType" minOccurs="0"/&gt;
 *         &lt;element name="CodiceCUP" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String15Type" minOccurs="0"/&gt;
 *         &lt;element name="CodiceCIG" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String15Type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiDocumentiCorrelatiType", propOrder = {
    "riferimentoNumeroLinea",
    "idDocumento",
    "data",
    "numItem",
    "codiceCommessaConvenzione",
    "codiceCUP",
    "codiceCIG"
})
public class DatiDocumentiCorrelatiType {

    @XmlElement(name = "RiferimentoNumeroLinea", type = Integer.class)
    @XmlSchemaType(name = "integer")
    protected List<Integer> riferimentoNumeroLinea;
    @XmlElement(name = "IdDocumento", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String idDocumento;
    @XmlElement(name = "Data")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar data;
    @XmlElement(name = "NumItem")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String numItem;
    @XmlElement(name = "CodiceCommessaConvenzione")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String codiceCommessaConvenzione;
    @XmlElement(name = "CodiceCUP")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String codiceCUP;
    @XmlElement(name = "CodiceCIG")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String codiceCIG;

    /**
     * Gets the value of the riferimentoNumeroLinea property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the riferimentoNumeroLinea property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRiferimentoNumeroLinea().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getRiferimentoNumeroLinea() {
        if (riferimentoNumeroLinea == null) {
            riferimentoNumeroLinea = new ArrayList<Integer>();
        }
        return this.riferimentoNumeroLinea;
    }

    /**
     * Recupera il valore della proprietà idDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumento() {
        return idDocumento;
    }

    /**
     * Imposta il valore della proprietà idDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumento(String value) {
        this.idDocumento = value;
    }

    /**
     * Recupera il valore della proprietà data.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getData() {
        return data;
    }

    /**
     * Imposta il valore della proprietà data.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setData(XMLGregorianCalendar value) {
        this.data = value;
    }

    /**
     * Recupera il valore della proprietà numItem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumItem() {
        return numItem;
    }

    /**
     * Imposta il valore della proprietà numItem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumItem(String value) {
        this.numItem = value;
    }

    /**
     * Recupera il valore della proprietà codiceCommessaConvenzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceCommessaConvenzione() {
        return codiceCommessaConvenzione;
    }

    /**
     * Imposta il valore della proprietà codiceCommessaConvenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceCommessaConvenzione(String value) {
        this.codiceCommessaConvenzione = value;
    }

    /**
     * Recupera il valore della proprietà codiceCUP.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceCUP() {
        return codiceCUP;
    }

    /**
     * Imposta il valore della proprietà codiceCUP.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceCUP(String value) {
        this.codiceCUP = value;
    }

    /**
     * Recupera il valore della proprietà codiceCIG.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceCIG() {
        return codiceCIG;
    }

    /**
     * Imposta il valore della proprietà codiceCIG.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceCIG(String value) {
        this.codiceCIG = value;
    }

}
